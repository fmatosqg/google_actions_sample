package com.isobar.sample.action.server.service.timesheet.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import groovy.transform.CompileStatic

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Index

/**
 * Created by fabio.goncalves on 7/06/2017.
 */
@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@CompileStatic
class TimesheetEntry {

    @Id
    @GeneratedValue
    Long id

    String user

    String task
    Integer time


    @Override
    public String toString() {
        return "TimesheetEntry{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", task='" + task + '\'' +
                ", time=" + time +
                '}';
    }
}
