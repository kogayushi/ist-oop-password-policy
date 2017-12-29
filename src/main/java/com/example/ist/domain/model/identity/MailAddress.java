package com.example.ist.domain.model.identity;

import java.util.regex.Pattern;

import lombok.Value;

@Value
public class MailAddress {

    private String value;

    public MailAddress(String value) {
        if (Pattern.matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", value) == false) {
            throw new IllegalArgumentException("it's not valid mail address.");
        }
        this.value = value;
    }
}
