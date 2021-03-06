/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.Stakhanov.controller;

import hu.elte.Stakhanov.entities.Person;
import hu.elte.Stakhanov.repositories.PersonRepository;
import hu.elte.Stakhanov.security.AuthenticatedUser;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lokos
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("people")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired 
    private AuthenticatedUser authenticatedUser;
    
    @PostMapping("/register")
    public ResponseEntity<Person> register(@RequestBody Person user) {
        Optional<Person> oUser = personRepository.findByUsername(user.getUsername());
        if (oUser.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(Person.Role.ROLE_USER);
        return ResponseEntity.ok(personRepository.save(user));
    }

    @PostMapping("login")
    public ResponseEntity login() {
        return ResponseEntity.ok(authenticatedUser.getPerson());
    }
}