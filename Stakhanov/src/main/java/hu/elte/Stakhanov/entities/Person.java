/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.Stakhanov.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author lokos
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Person implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private boolean enabled;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    
    public enum Role {
        ROLE_GUEST, ROLE_USER, ROLE_ADMIN
    }
    
    @Column
    private String fullname;
    
    @Column
    private String education;
    
    @Column
    @Temporal(TemporalType.DATE)
    private Date birth_date;
    
    @Column
    private String pos;
    
    @JoinTable
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Team> co_teams;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "boss")
    private List<Team> sub_teams;
    
    @OneToMany(mappedBy = "owner")
    private List<Registry> registry;
    
    public void addCo_teams(Team t){
        co_teams.add(t);
    }
    
    public void addSub_teams(Team t){
        sub_teams.add(t);
    }
}