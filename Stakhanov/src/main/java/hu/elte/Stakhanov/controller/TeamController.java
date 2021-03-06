/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.Stakhanov.controller;

import hu.elte.Stakhanov.entities.Team;
import hu.elte.Stakhanov.entities.Calendar;
import hu.elte.Stakhanov.entities.Person;
import hu.elte.Stakhanov.entities.Registry;
import hu.elte.Stakhanov.entities.Team;
import hu.elte.Stakhanov.repositories.CalendarRepository;
import hu.elte.Stakhanov.repositories.PersonRepository;
import hu.elte.Stakhanov.repositories.RegistryRepository;
import hu.elte.Stakhanov.repositories.TeamRepository;
import hu.elte.Stakhanov.security.AuthenticatedUser;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("teams")
public class TeamController {
    
    @Autowired
    private CalendarRepository calendarRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private RegistryRepository registryRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired 
    private AuthenticatedUser authenticatedUser;
    
    @GetMapping("")
    public ResponseEntity<Iterable<Team>> getAll() {
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        //System.out.println(role.equals(Person.Role.ROLE_ADMIN));
        return ResponseEntity.ok(teamRepository.findAll());
        /*if (role.equals(Person.Role.ROLE_ADMIN)) {
            return ResponseEntity.ok(teamRepository.findAll());
        } else{
            List<Team> teams = new ArrayList<>();
            for (Team t : teamRepository.findAll()){
                //System.out.println(t.getTeam_name());
                if(isPersonInListById(person, t.getTeam_mates())){
                    teams.add(t);
                }
            }
            return ResponseEntity.ok(teams);
        }*/
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Team> get(@PathVariable Integer id) {
        Optional<Team> team = teamRepository.findById(id);
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (team.isPresent() && (role.equals(Person.Role.ROLE_ADMIN)
            || isPersonInListById(person, team.get().getTeam_mates()))) {
            return ResponseEntity.ok(team.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/teammates")
    public ResponseEntity<Iterable<publicPerson>> getTeammates(@PathVariable Integer id) {
        Optional<Team> team = teamRepository.findById(id);
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (team.isPresent() && (role.equals(Person.Role.ROLE_ADMIN)
            || isPersonInListById(person, team.get().getTeam_mates()))) {
            
            List<publicPerson> people = new ArrayList<>();
            
            for(Person p : team.get().getTeam_mates()){
                people.add(new publicPerson(p.getFullname(), p.getEducation(), p.getBirth_date(), p.getPos()));
            }
            return ResponseEntity.ok(people);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    @PostMapping("")
    public ResponseEntity<Team> post(@RequestBody Team team) {
        Person person = authenticatedUser.getPerson();
        
        Calendar calendar = new Calendar();
        calendarRepository.save(calendar);
        calendar.setTeam(team);
        //calendarRepository.save(calendar);

        team.setCalendar(calendar);
        team.setBoss(person);
        team.addTeam_mates(person);
        
        //person.addSub_teams(team);
        //person.addCo_teams(team);
        
        person.getSub_teams().add(team);
        person.getCo_teams().add(team);
        
        
        Team savedTeamEntity = teamRepository.save(team);
        personRepository.save(person);
        
        return ResponseEntity.ok(savedTeamEntity);
    }
    
    @PostMapping("/{id}/user")
    public ResponseEntity<Person> post(@PathVariable Integer id){
        Optional<Team> oTeam = teamRepository.findById(id);
        Person person = authenticatedUser.getPerson();
        if (oTeam.isPresent()){
            Team team = oTeam.get();
            if(!isPersonInListById(person, team.getTeam_mates())){
                team.addTeam_mates(person);
                person.addCo_teams(team);

                teamRepository.save(team);
                Person savedPerson = personRepository.save(person);
                return ResponseEntity.ok(savedPerson);
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Team> put(@RequestBody Team team, @PathVariable Integer id) {
        Optional<Team> oTeamEntity = teamRepository.findById(id);
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (oTeamEntity.isPresent() && (role.equals(Person.Role.ROLE_ADMIN)
            || isPersonInListById(person, oTeamEntity.get().getTeam_mates()))) {
            team.setId(id);
            return ResponseEntity.ok(teamRepository.save(team));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/registry")
    public ResponseEntity<Registry> postRegistry(@RequestBody Registry registry, @PathVariable Integer id){
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        Optional<Team> oTeamEntity = teamRepository.findById(id);
        
        if (oTeamEntity.isPresent() && (role.equals(Person.Role.ROLE_ADMIN)
            || isPersonInListById(person, oTeamEntity.get().getTeam_mates()))) {
            
            Calendar calendar = oTeamEntity.get().getCalendar();
            
            person.addRegistry(registry);
            registry.setOwner(person);
            
            registry.setCalendar(calendar);
            calendar.addRegistries(registry);
            
            personRepository.save(person);
            calendarRepository.save(calendar);
            return ResponseEntity.ok(registryRepository.save(registry));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        Optional<Team> oTeamEntity = teamRepository.findById(id);
        
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (oTeamEntity.isPresent() && (role.equals(Person.Role.ROLE_ADMIN)
            || oTeamEntity.get().getBoss().getId().equals(person.getId()))) {
            if(oTeamEntity.get().getCalendar() != null){
                if (!oTeamEntity.get().getCalendar().getRegistries().isEmpty()){
                    for (Registry r : oTeamEntity.get().getCalendar().getRegistries()){
                        registryRepository.deleteById(r.getId());
                    }
            }
                calendarRepository.deleteById(oTeamEntity.get().getCalendar().getId());
            }
            teamRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/calendar")
    public ResponseEntity<Calendar> calendar(@PathVariable Integer id) {
        Optional<Team> oTeam = teamRepository.findById(id);
        
        Person person = authenticatedUser.getPerson();
        Person.Role role = person.getRole();
        
        if (oTeam.isPresent()&& (role.equals(Person.Role.ROLE_ADMIN) 
            || isPersonInListById(person, oTeam.get().getTeam_mates()) )) {
            return ResponseEntity.ok(oTeam.get().getCalendar());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    //nem szükséges
    @PostMapping("/{id}/calendar")
    public ResponseEntity<Calendar> insertCalendar(@PathVariable Integer id) {
        Optional<Team> oTeam = teamRepository.findById(id);
        Calendar calendar = new Calendar();
        if (oTeam.isPresent()) {
            Team team = oTeam.get();
            calendar.setTeam(team);
            LocalDateTime a = LocalDateTime.now();
            calendar.setCreated_at(a);
            return ResponseEntity.ok(calendarRepository.save(calendar));
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
    
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    class publicPerson{
        private String name;
        private String education;
        private Date birthDate;
        private String position;
    }
}
