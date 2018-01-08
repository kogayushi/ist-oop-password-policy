package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.AuthenticationFactor;
import com.example.ist.domain.model.identity.TelephoneNumber;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotSameWithTelephoneNumberPolicy implements Policy {
    private final TelephoneNumber telephoneNumber;

    @Override
    public boolean isSatisfiedBy(AuthenticationFactor factor) {
        return this.telephoneNumber.getValue().equals(factor.getValue()) == false;
    }

}
