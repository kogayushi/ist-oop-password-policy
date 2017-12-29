package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.AuthenticationFactor;

public interface Policy<T extends AuthenticationFactor> {
    boolean isSatisfiedBy(T factor);
    default String policyName() {
        return this.getClass().getSimpleName();
    }
}
