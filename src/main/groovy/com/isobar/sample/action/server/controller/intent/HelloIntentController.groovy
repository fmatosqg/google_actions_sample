package com.isobar.sample.action.server.controller.intent

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

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    @ResponseBody
    String helloGet() {

        return helloPost(null)
    }

    @RequestMapping(value = "hello", method = RequestMethod.POST)
    @ResponseBody
    String helloPost(@RequestBody def body) {


        LOGGER.info("post hit ${body}")

        def param = null

        if (body) {
            param = body['result']['parameters']['any']


            def source = body?.originalRequest?.source
            source += "/" + body?.result?.source
            LOGGER.info("Source $source")
        }
        if (param) {
            LOGGER.info("Param is $param")
            return helloAnswer("Hello. Who's $param?")
        } else {
            return helloAnswer("Hello hello")
        }
    }
}
