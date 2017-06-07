package com.isobar.sample.action.server.service.timesheet.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Created by fabio.goncalves on 7/06/2017.
 */
@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@CompileStatic
@ToString
class TimesheetEntry {

    @Id
    @GeneratedValue
    Long id

    String user

    String task
    Long accumulatedTime
    Long startTime
    Boolean active

}
