package com.example.ist.domain.model.identity;

import java.util.regex.Pattern;

import lombok.Value;

@Value
public class TelephoneNumber {
    private String value;

    public TelephoneNumber(String value) {
        if(Pattern.matches("^0\\d{1,4}-\\d{1,4}-\\d{4}$", value) == false) {
            throw new IllegalArgumentException("It's not valid telephone number.");
        }
        this.value = value;
    }
}
