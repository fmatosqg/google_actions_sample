package com.isobar.sample.action.server.controller.intent

import com.isobar.sample.action.server.service.DelayService
import com.isobar.sample.action.server.service.IntentService
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
class IntentController {

    final Logger LOGGER = LoggerFactory.getLogger(IntentController)

    @Autowired
    DelayService delayService

    @Autowired
    IntentService intentService

    @RequestMapping(value = "test", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    String helloGet() {

        def param = [result: [action: 'test']]
        return helloPost(param)
    }

    @RequestMapping(value = "intent", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    String helloPost(@RequestBody def body) {

        def action = body?.result?.action
        String param = body?.result?.parameters?.any

        LOGGER.info("post hit ${body}")
        LOGGER.info("Score ${body?.result?.score} / incomplete ${body?.result?.actionIncomplete} / Fulfillment ${body?.result?.fulfillment}")

        if (body) {
            def source = body?.originalRequest?.source
            source += "/" + body?.result?.source
            LOGGER.info("Action $action |||| Source $source")
        }

        JSONObject result = null
        switch (action) {
            case 'animals':
                result = intentService.intentAnimals(param)
                break
            case 'hello':
                result = intentService.intentHello(param)
                break
            case 'test':
                result = intentService.test('test')
                break
            case 'delay':
                return delayService.process(body)
                break
        }

        return result as String

    }



}
