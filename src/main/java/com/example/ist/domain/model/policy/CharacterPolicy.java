package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.AuthenticationFactor;

import java.util.regex.Pattern;

public class CharacterPolicy implements Policy {
    private final Pattern pattern;

    public CharacterPolicy(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isSatisfiedBy(AuthenticationFactor factor) {
        return pattern.matcher(factor.getValue()).find();
    }

}
