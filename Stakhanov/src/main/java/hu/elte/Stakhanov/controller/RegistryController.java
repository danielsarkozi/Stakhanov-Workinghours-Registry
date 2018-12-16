/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.Stakhanov.controller;

import hu.elte.Stakhanov.entities.Person;
import hu.elte.Stakhanov.entities.Registry;
import hu.elte.Stakhanov.entities.Team;
import hu.elte.Stakhanov.repositories.PersonRepository;
import hu.elte.Stakhanov.repositories.RegistryRepository;
import hu.elte.Stakhanov.security.AuthenticatedUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lokos
 */
@CrossOrigin
@RestController
@RequestMapping("registries")
public class RegistryController {
    @Autowired
    private RegistryRepository registryRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired 
    private AuthenticatedUser authenticatedUser;
    
    @GetMapping("")
    public ResponseEntity<Iterable<Registry>> getAll() {
        Person person = authenticatedUser.getPerson();
        
        Person.Role role = person.getRole();
        //System.out.println(role.equals(Person.Role.ROLE_ADMIN));
        if (role.equals(Person.Role.ROLE_ADMIN)) {
            return ResponseEntity.ok(registryRepository.findAll());
        }else{
            return ResponseEntity.ok(person.getRegistry());
        }
        
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Registry> get(@PathVariable Integer id) {
        Optional<Registry> registry = registryRepository.findById(id);
        
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        
        System.out.println(role.equals(Person.Role.ROLE_ADMIN) || registry.get().getOwner().getId().equals(person));
        if  (registry.isPresent() && (role.equals(Person.Role.ROLE_ADMIN)
            || registry.get().getOwner().getId().equals(person.getId()))) {
            return ResponseEntity.ok(registry.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("")
    public ResponseEntity<Registry> post(@RequestBody Registry registry) {
        Person person = authenticatedUser.getPerson();
        person.addRegistry(registry);
        registry.setOwner(person);
        
        personRepository.save(person);
        Registry savedRegistryEntity = registryRepository.save(registry);
        return ResponseEntity.ok(savedRegistryEntity);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Registry> put(@RequestBody Registry registry, @PathVariable Integer id) {
        Optional<Registry> oRegistryEntity = registryRepository.findById(id);
        if (oRegistryEntity.isPresent()) {
            registry.setId(id);
            return ResponseEntity.ok(registryRepository.save(registry));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        Optional<Registry> oRegistryEntity = registryRepository.findById(id);
        
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (oRegistryEntity.isPresent() && (role.equals(Person.Role.ROLE_ADMIN)
            || oRegistryEntity.get().getOwner().getId().equals(person.getId()))) {
            registryRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    boolean isPersonInListById(Person person, List<Person> list){
        boolean b = false;
        
        for (Person p : list){
            //System.out.println(p.getId() + "   " + person.getId());
            if (Objects.equals(p.getId(), person.getId())){
                b = true;
            }
        }
        
        return b;
    }
}
