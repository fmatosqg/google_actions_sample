package com.isobar.sample.action.server.controller.intent

import groovy.transform.CompileStatic
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Created by fabio.goncalves on 5/06/2017.
 */
@Controller
class HelloIntentController extends IntentController {

    final Logger LOGGER = LoggerFactory.getLogger(HelloIntentController)

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

        if (body) {
            def source = body?.originalRequest?.source
            source += "/" + body?.result?.source
            LOGGER.info("Action $action |||| Source $source")
        }

        JSONObject result = null
        switch (action) {
            case 'animals':
                result = intentAnimals(param)
                break
            case 'hello':
                result = intentHello(param)
                break
            case 'test':
                result = answer('test')
                break
        }

        return result as String

    }

    JSONObject intentAnimals(String s) {

        if (s) {
            return answer("Hmm, a $s person.")
        } else {
            return answer("I didn't get that.")
        }
    }

    JSONObject intentHello(String param) {
        if (param) {
            LOGGER.info("Param is $param")
            return answer("Hello. Who's $param?")
        } else {
            return answer("Hello hello.")
        }
    }
}
