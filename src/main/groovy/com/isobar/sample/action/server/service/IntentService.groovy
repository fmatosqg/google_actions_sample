package com.isobar.sample.action.server.service

import groovy.transform.CompileStatic
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Created by fabio.goncalves on 6/06/2017.
 */
@Service
@CompileStatic
class IntentService extends AnswerService {

    final Logger LOGGER = LoggerFactory.getLogger(AnswerService)

    JSONObject intentAnimals(def params) {

        String val = params['any']

        if (val) {
            return answer("Hmm, a $val person.")
        } else {
            return answer("I didn't get that.")
        }
    }

    JSONObject intentHello(def params) {

        String val = params['any']

        if (val) {
            LOGGER.info("Param is $val")
            return answer("Hello. Who's $val?")
        } else {
            return answer("Hello hello.")
        }
    }

    JSONObject test(String s) {
        return answer(s)
    }

    JSONObject help() {

        return answerWithSuggestions("Suggestions","demouser",["demo","start","stats"])

    }
}
