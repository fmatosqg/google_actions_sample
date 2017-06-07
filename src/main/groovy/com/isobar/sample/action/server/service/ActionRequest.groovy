package com.isobar.sample.action.server.service

import groovy.transform.CompileStatic
import groovy.transform.ToString

/**
 * Created by fabio.goncalves on 7/06/2017.
 */
@CompileStatic
@ToString
class ActionRequest {

    String sessionId
    OriginalRequest originalRequest
    Result result

    @ToString
    static class Result {
        String action
        Float score
        Boolean actionIncomplete
        def parameters
        Fulfillment fulfillment
    }

    @ToString
    static class Fulfillment {
        String speech
    }

    @ToString
    static class OriginalRequest {
        String source
        Data data
    }

    @ToString
    static class Data {
        User user
    }

    @ToString
    static class User {
        String userId
        String locale

    }
}
