package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.AuthenticationFactor;

public class NotIncludingNamePolicy implements Policy {
    private final String firstName;
    private final String lastName;

    public NotIncludingNamePolicy(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean isSatisfiedBy(AuthenticationFactor factor) {
        return factor.getValue().toUpperCase().contains(this.firstName.toUpperCase()) == false
                && factor.getValue().toUpperCase().contains(this.lastName.toUpperCase()) == false;
    }

}
