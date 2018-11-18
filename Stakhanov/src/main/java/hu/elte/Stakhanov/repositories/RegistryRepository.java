/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.Stakhanov.repositories;


import hu.elte.Stakhanov.entities.Person;
import hu.elte.Stakhanov.entities.Registry;
import hu.elte.Stakhanov.entities.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lokos
 */
@Repository
public interface RegistryRepository extends CrudRepository<Registry, Integer> {
    public Iterable<Registry> findAllByOwner(Person owner);
}