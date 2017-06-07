package com.isobar.sample.action.server.service.timesheet

import com.isobar.sample.action.server.service.ActionRequest
import com.isobar.sample.action.server.service.AnswerService
import com.isobar.sample.action.server.service.timesheet.model.TimesheetEntry
import com.isobar.sample.action.server.service.timesheet.model.TimesheetRepository
import groovy.transform.CompileStatic
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

        TimesheetEntry entry = timesheetRepository.findByUser(input?.sessionId)

        LOGGER.info("Entry $entry")

        answer('stats empty')
    }

    JSONObject start(ActionRequest input) {

        String session = input?.sessionId

        LOGGER.info("User is $session")

        TimesheetEntry entry = timesheetRepository.findByUser(session)

        if (!entry) {
            entry = new TimesheetEntry(user: session, time: 0)
        }

        if (!entry.time) {
            entry.time = 0
        }

        entry.time++

        LOGGER.info("Entry $entry")

        timesheetRepository.save(entry)

        return answer("Count = $entry.time")

    }


    JSONObject stop(ActionRequest input) {

        return answer('stop')
    }

}
