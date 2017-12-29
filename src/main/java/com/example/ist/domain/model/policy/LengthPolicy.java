package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.AuthenticationFactor;

public class LengthPolicy implements Policy {
    private final int min;
    private final int max;

    public LengthPolicy(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isSatisfiedBy(AuthenticationFactor factor) {
        int length =factor.getValue().length();
        return this.min < length && length < max;
    }

}
