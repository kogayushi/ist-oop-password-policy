package com.example.ist.oop.command;

import lombok.Value;

import java.util.UUID;

@Value
public class ChangePasswordCommand {
    private final UUID id;
    private final String newPassword;
}
