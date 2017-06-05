package com.isobar.sample.action.server.controller.intent

/**
 * Created by fabio.goncalves on 5/06/2017.
 */
class IntentController {

    protected String helloAnswer(String response) {
        return "{\"speech\": \"$response\"}"

//        def responseObject = [speech: response]
//        return responseObject
    }
}
