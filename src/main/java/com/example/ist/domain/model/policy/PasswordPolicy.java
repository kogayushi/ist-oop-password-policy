package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.Password;
import com.example.ist.exception.ViolatedPolicyException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Value
public class PasswordPolicy {

    private final Set<Policy> policies;

    public void validate(Password password) {
        for (Policy policy : policies) {
            if (policy.isSatisfiedBy(password) == false) {
                String msg = String.format("inputted password violated %s", policy.policyName());
                log.warn(msg);
                throw new ViolatedPolicyException(msg);
            }
        }
    }
}
