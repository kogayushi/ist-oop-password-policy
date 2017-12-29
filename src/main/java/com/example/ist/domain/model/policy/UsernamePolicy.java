package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.Username;
import com.example.ist.exception.ViolatedPolicyException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@SuppressWarnings("unchecked")
@Slf4j
@RequiredArgsConstructor
@Value
public class UsernamePolicy {

    private final Set<Policy> policies;

    public void validate(Username username) {
        for (Policy policy : policies) {
            if (policy.isSatisfiedBy(username) == false) {
                String msg = String.format("inputted username violated %s", policy.policyName());
                log.warn(msg);
                throw new ViolatedPolicyException(msg);
            }
        }
    }
}
