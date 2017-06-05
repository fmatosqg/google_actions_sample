package com.isobar.sample.action.server.controller

import groovy.transform.CompileStatic
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
@CompileStatic
class SampleController {

    final Logger LOGGER = LoggerFactory.getLogger(SampleController)

    @RequestMapping(value = "ping")
    @ResponseBody
    String ping() {

        return "pong"
    }

}
