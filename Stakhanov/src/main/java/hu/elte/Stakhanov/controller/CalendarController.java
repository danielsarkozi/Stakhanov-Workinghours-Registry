/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.Stakhanov.controller;

import hu.elte.Stakhanov.entities.Calendar;
import hu.elte.Stakhanov.entities.Person;
import hu.elte.Stakhanov.entities.Registry;
import hu.elte.Stakhanov.repositories.CalendarRepository;
import hu.elte.Stakhanov.repositories.RegistryRepository;
import hu.elte.Stakhanov.security.AuthenticatedUser;
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
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("calendars")
public class CalendarController {
    @Autowired
    private CalendarRepository calendarRepository;
    
    @Autowired
    private RegistryRepository registryRepository;
    
    @Autowired 
    private AuthenticatedUser authenticatedUser;
    
    @GetMapping("")
    public ResponseEntity<Iterable<Calendar>> getAll() {
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        //System.out.println(role.equals(Person.Role.ROLE_ADMIN));
        if (role.equals(Person.Role.ROLE_ADMIN)) {
            return ResponseEntity.ok(calendarRepository.findAll());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Calendar> get(@PathVariable Integer id) {
        Optional<Calendar> calendar = calendarRepository.findById(id);
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if  (calendar.isPresent()&& (role.equals(Person.Role.ROLE_ADMIN)
            || isPersonInListById(person, calendar.get().getTeam().getTeam_mates()))) {
            return ResponseEntity.ok(calendar.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    //nem szükséges
    @PostMapping("")
    public ResponseEntity<Calendar> post(@RequestBody Calendar calendar) {
        Calendar savedCalendarEntity = calendarRepository.save(calendar);
        return ResponseEntity.ok(savedCalendarEntity);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Calendar> put(@RequestBody Calendar calendar, @PathVariable Integer id) {
        Optional<Calendar> oCalendarEntity = calendarRepository.findById(id);
        
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (oCalendarEntity.isPresent() && (role.equals(Person.Role.ROLE_ADMIN)
            || oCalendarEntity.get().getTeam().getBoss().getId().equals(person.getId()))) {
            calendar.setId(id);
            return ResponseEntity.ok(calendarRepository.save(calendar));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    //nem szükséges
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        Optional<Calendar> oCalendarEntity = calendarRepository.findById(id);
        
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (oCalendarEntity.isPresent() && role.equals(Person.Role.ROLE_ADMIN)) {
            if (!oCalendarEntity.get().getRegistries().isEmpty()){
                for (Registry r : oCalendarEntity.get().getRegistries()){
                    registryRepository.deleteById(r.getId());
                }
            }
            calendarRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/registries")
    public ResponseEntity<Iterable<Registry>> registries(@PathVariable Integer id) {
        Optional<Calendar> oCalendar = calendarRepository.findById(id);
        
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (oCalendar.isPresent() && (role.equals(Person.Role.ROLE_ADMIN)
            || isPersonInListById(person, oCalendar.get().getTeam().getTeam_mates()))) {
            return ResponseEntity.ok(oCalendar.get().getRegistries());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/registries")
    public ResponseEntity<Registry> insertRegistry(@PathVariable Integer id, @RequestBody Registry registry) {
        Optional<Calendar> oCalendar = calendarRepository.findById(id);
        
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (oCalendar.isPresent()) {
            Calendar calendar = oCalendar.get();
            registry.setCalendar(calendar);
            registry.setOwner(person);
            return ResponseEntity.ok(registryRepository.save(registry));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    boolean isPersonInListById(Person person, List<Person> list){
        boolean b = false;
        
        for (Person p : list){
            if (Objects.equals(p.getId(), person.getId())){
                b = true;
            }
        }
        
        return b;
    }
}
