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
import org.springframework.dao.IncorrectResultSizeDataAccessException
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

        String userId = getUserId(input)

        String response = stats(userId)

        answerWithSuggestions("stats $response", userId, ["start", "stats", "stop"])
    }


    String getUserId(ActionRequest actionRequest) {

        String userId

        if (!actionRequest.originalRequest) {
            userId = "empty source + ${actionRequest.sessionId}"

        } else if (actionRequest.originalRequest.source == 'google') {
            userId = actionRequest.originalRequest.data.user.userId
        } else {
            userId = "${actionRequest.originalRequest.source} + ${actionRequest.sessionId}"
        }

        return userId
    }

    JSONObject start(ActionRequest input) {

        String userId = getUserId(input)

        if (input.result.actionIncomplete) {

            List suggestions = timesheetRepository.findByUser(userId)
                    .sort { 0-it.startTime }
                    .collect { it.task }
            return answerWithSuggestions(input.result.fulfillment.speech, userId, suggestions)
        }

        String taskName = input.result.parameters['task']

        stopActiveTask(userId)


        LOGGER.info("User is $userId, params are ${input.result.parameters}")

        TimesheetEntry startedEntry = startTask(userId, taskName)
        LOGGER.info("Entry $startedEntry")


        return answerWithSuggestions("Adding time under '$taskName'. Accumulated $startedEntry.accumulatedTime seconds so far.", userId, ["stats", "stop"])

    }

    JSONObject stop(ActionRequest input) {

        String userId = getUserId(input)

        TimesheetEntry entry = stopActiveTask(userId)

        if (entry) {
            return answerWithSuggestions("Stop recording on $entry.task", userId, ["stats"])
        } else {
            return answer("Already was stopped")
        }

    }

    TimesheetEntry startTask(String userId, String taskName) {

        TimesheetEntry startedEntry = timesheetRepository.findByUserAndTask(userId, taskName)

        if (!startedEntry) {
            startedEntry = new TimesheetEntry(user: userId, task: taskName, accumulatedTime: 0L, startTime: 0L)
        }

        startedEntry.active = true
        startedEntry.startTime = DateTime.now().getMillis()

        timesheetRepository.save(startedEntry)

        return startedEntry

    }

    TimesheetEntry stopActiveTask(String userId) {
        TimesheetEntry activeTask = null

        try {
            activeTask = timesheetRepository.findByUserAndActive(userId, true)
        } catch (IncorrectResultSizeDataAccessException e) {
            timesheetRepository.findByUser(userId)
                    .each {
                it.active = false
                timesheetRepository.save(it)
            }
        }

        if (activeTask) {
            LOGGER.info("Found active task $activeTask")

            activeTask.active = false
            long timeElapsed = DateTime.now().getMillis() - activeTask.startTime
            timeElapsed /= 1000L

            if (activeTask.accumulatedTime == null) {
                activeTask.accumulatedTime = 0
            }
            activeTask.accumulatedTime += timeElapsed

            timesheetRepository.save(activeTask)

            return activeTask
        }
    }

    String stats(String userId) {
        TimesheetEntry activeTask = stopActiveTask(userId)
        if (activeTask) {
            startTask(activeTask.user, activeTask.task)
        }


        List<TimesheetEntry> entries = timesheetRepository.findByUser(userId)

        LOGGER.info("Entry $entries")

        String response = entries
                .findAll { entry -> entry.task }
                .sort { 0-it.accumulatedTime }
                .collect { entry ->
            def active = entry.active ? "*active*" : ""
            "$entry.task - $entry.accumulatedTime seconds $active"
        }
        .join(',\n')

        return response
    }
}
