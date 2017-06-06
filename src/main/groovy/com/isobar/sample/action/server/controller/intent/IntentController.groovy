package com.isobar.sample.action.server.controller.intent

import org.json.JSONObject

/**
 * Created by fabio.goncalves on 5/06/2017.
 */
class IntentController {

    protected JSONObject answer(String response) {
//        return "{\"speech\": \"$response\"}"

        JSONObject responseObject = [speech: response]
        return responseObject
    }
}
