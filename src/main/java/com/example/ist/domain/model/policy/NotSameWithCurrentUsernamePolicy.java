package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.AuthenticationFactor;

public class NotSameWithCurrentUsernamePolicy implements Policy {
    private final String username;

    public NotSameWithCurrentUsernamePolicy(String username) {
        this.username = username;
    }

    @Override
    public boolean isSatisfiedBy(AuthenticationFactor factor) {
        return this.username.equals(factor.getValue()) == false;
    }

}
