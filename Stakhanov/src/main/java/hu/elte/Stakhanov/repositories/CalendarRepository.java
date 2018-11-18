/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.Stakhanov.repositories;

import hu.elte.Stakhanov.entities.Calendar;
import hu.elte.Stakhanov.entities.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lokos
 */
@Repository
public interface CalendarRepository extends CrudRepository<Calendar, Integer> {
    
}
