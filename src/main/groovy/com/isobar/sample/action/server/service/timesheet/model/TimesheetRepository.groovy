package com.isobar.sample.action.server.service.timesheet.model

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import javax.transaction.Transactional

/**
 * Created by fabio.goncalves on 7/06/2017.
 */
@Repository
@Transactional
interface TimesheetRepository extends CrudRepository<TimesheetEntry, Long> {

    List<TimesheetEntry> findAll()

    TimesheetEntry findById(Long id)

    TimesheetEntry findByUser(String user)
}
