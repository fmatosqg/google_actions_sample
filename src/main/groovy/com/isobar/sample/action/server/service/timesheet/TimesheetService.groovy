package com.isobar.sample.action.server.service.timesheet

import com.isobar.sample.action.server.service.ActionRequest
import com.isobar.sample.action.server.service.AnswerService
import com.isobar.sample.action.server.service.timesheet.model.TimesheetEntry
import com.isobar.sample.action.server.service.timesheet.model.TimesheetRepository
import groovy.transform.CompileStatic
import org.joda.time.DateTime
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by fabio.goncalves on 7/06/2017.
 */
@Service
@CompileStatic
class TimesheetService extends AnswerService {


    private final Logger LOGGER = LoggerFactory.getLogger(TimesheetService)
    private final TimesheetRepository timesheetRepository


    @Autowired
    TimesheetService(TimesheetRepository timesheetRepository) {
        this.timesheetRepository = timesheetRepository
    }

    JSONObject stats(ActionRequest input) {

        List<TimesheetEntry> entries = timesheetRepository.findByUser(input?.sessionId)

        LOGGER.info("Entry $entries")

        String response = entries
                .findAll { entry -> entry.task }
                .collect { entry -> "$entry.task - $entry.accumulatedTime seconds" }
                .join(',\n')

        answer("stats $response")
    }

    JSONObject start(ActionRequest input) {

        checkForActiveTask(input)

        if (input.result.actionIncomplete) {
            return answer(input.result.fulfillment.speech)
        }

        String session = input?.sessionId
        String taskName = input.result.parameters['task']

        LOGGER.info("User is $session, params are ${input.result.parameters}")

        TimesheetEntry startedEntry = timesheetRepository.findByUserAndTask(session, taskName)

        if (!startedEntry) {
            startedEntry = new TimesheetEntry(user: session, task: taskName, accumulatedTime: 0L, startTime: 0L)
        }

        startedEntry.active = true
        startedEntry.startTime = DateTime.now().getMillis()

        LOGGER.info("Entry $startedEntry")
        timesheetRepository.save(startedEntry)

        return answer("Adding time under '$taskName'. Accumulated $startedEntry.accumulatedTime seconds so far.")

    }

    TimesheetEntry checkForActiveTask(ActionRequest actionRequest) {


        TimesheetEntry activeTask = timesheetRepository.findByActive(true)
        if (activeTask) {
            LOGGER.info("Found active task $activeTask")

            activeTask.active = false
            long timeElapsed = DateTime.now().getMillis() - activeTask.startTime
            timeElapsed /= 1000L
            activeTask.accumulatedTime += timeElapsed

            timesheetRepository.save(activeTask)

            return activeTask
        }
    }

    JSONObject stop(ActionRequest input) {

        TimesheetEntry entry = checkForActiveTask(input)

        if ( entry ) {
            return answer("Stop recording on $entry.task")
        } else {
            return answer("Already was stopped")
        }

    }

}
