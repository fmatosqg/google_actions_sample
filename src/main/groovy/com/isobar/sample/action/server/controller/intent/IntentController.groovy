package com.isobar.sample.action.server.controller.intent

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.isobar.sample.action.server.service.ActionRequest
import com.isobar.sample.action.server.service.DelayService
import com.isobar.sample.action.server.service.IntentService
import com.isobar.sample.action.server.service.timesheet.TimesheetService
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Created by fabio.goncalves on 5/06/2017.
 */
@Controller
@CompileStatic
class IntentController {

    final Logger LOGGER = LoggerFactory.getLogger(IntentController)

    @Autowired
    DelayService delayService

    @Autowired
    IntentService intentService

    @Autowired
    TimesheetService timesheetService

    @RequestMapping(value = "test", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    String helloGet() {

        ActionRequest actionRequest = new ActionRequest(result: new ActionRequest.Result(action: 'test'))
        return "test"
    }

    @RequestMapping(value = "intent", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    String helloPost(@RequestBody String body) {

        ObjectMapper objectMapper= new ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ActionRequest actionRequest = objectMapper.readValue(body, ActionRequest)


        LOGGER.info("body ${body}")
        LOGGER.info("Action ${actionRequest?.result?.action}  / Score ${actionRequest?.result?.score} / incomplete ${actionRequest?.result?.actionIncomplete} / Fulfillment ${actionRequest.result}")


        JSONObject result = null

        switch (actionRequest?.result?.action) {
            case 'animals':
                result = intentService.intentAnimals(actionRequest.result.parameters)
                break
            case 'hello':
                result = intentService.intentHello(actionRequest.result.parameters)
                break
            case 'test':
                result = intentService.test('test')
                break
            case 'delay':
                return delayService.process(actionRequest)
                break
            case 'timesheet.stats':
                result = timesheetService.stats(actionRequest)
                break
            case 'timesheet.start':
                result = timesheetService.start(actionRequest)
                break
            case 'timesheet.stop':
                result = timesheetService.stop(actionRequest)
                break
        }

        return result as String

    }


}
