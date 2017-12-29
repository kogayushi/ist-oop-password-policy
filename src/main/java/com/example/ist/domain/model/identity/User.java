package com.example.ist.domain.model.identity;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class User {
    
    private UUID id;
    private String username;
    private String password;
    private Person person;
    
    public User(UUID id, String username, String password, Person person) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.person = person;
    }
    

    public boolean notExists() {
        return this.id == null;
    }
}