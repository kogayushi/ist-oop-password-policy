package com.example.ist.oop.command;

import lombok.Value;

import java.util.UUID;

@Value
public class ChangeUsernameCommand {
    private final UUID id;
    private String newUsername;
}
