package com.isobar.sample.action.server.service

import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Created by fabio.goncalves on 6/06/2017.
 */
@Service
class IntentService extends AnswerService {

    final Logger LOGGER = LoggerFactory.getLogger(AnswerService)

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

    JSONObject test(String s) {
        return answer(s)
    }
}
