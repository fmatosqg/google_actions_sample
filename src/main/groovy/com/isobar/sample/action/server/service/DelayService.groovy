package com.isobar.sample.action.server.service

import groovy.transform.CompileStatic
import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Created by fabio.goncalves on 6/06/2017.
 */
@Service
@CompileStatic
class DelayService extends AnswerService {

    private Logger LOGGER = LoggerFactory.getLogger(DelayService)

    JSONObject process(ActionRequest input) {

        def fulfillment = input?.result?.fulfillment

        def value = input?.result?.parameters['duration']

        DelayModel par = null

        if (input?.result?.actionIncomplete) {
            return answer(fulfillment?.speech, "slack :whaaat?")
        }

        try {
            par = value as DelayModel
        } catch (GroovyCastException e) {
            LOGGER.info("Par $value")
            par = new DelayModel()
            return answer("I didn't understand")
        }

        final JSONObject response

        LOGGER.info("Delay $par")

        if (par.unit == 's') {
            int ms = (par.amount as int) * 1000
            Thread.sleep(ms)
            response = answer("accepted $par")
        } else {
            response = answer("can't delay for $par.amount $par.unit")
        }



        return response
    }

    private static class DelayModel {

        String amount
        String unit

        DelayModel() {

        }

        @Override
        public String toString() {
            return "delay for $amount $unit";
        }
    }


}
