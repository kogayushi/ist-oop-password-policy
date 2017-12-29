package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.AuthenticationFactor;
import com.example.ist.domain.model.identity.MailAddress;

public class NotSameWithMailAddressPolicy implements Policy {
    private final MailAddress mailAddress;

    public NotSameWithMailAddressPolicy(MailAddress mailAddress) {
        this.mailAddress = mailAddress;
    }

    @Override
    public boolean isSatisfiedBy(AuthenticationFactor factor) {
        return this.mailAddress.getValue().equals(factor.getValue()) == false;
    }

}
