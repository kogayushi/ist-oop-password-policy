package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.AuthenticationFactor;

public interface Policy {
    boolean isSatisfiedBy(AuthenticationFactor factor);
    default String policyName() {
        return this.getClass().getSimpleName();
    }
}
