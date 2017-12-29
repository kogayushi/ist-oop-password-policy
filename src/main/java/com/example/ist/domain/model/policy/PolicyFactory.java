package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.ContactInformation;
import com.example.ist.domain.model.identity.Person;
import com.example.ist.domain.model.identity.User;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class PolicyFactory {

    @SuppressWarnings("unchecked")
    public PasswordPolicy generatePasswordPolicyFor(User user) {
        Set<Policy> policies = generateCommonPolicy(user);

        return new PasswordPolicy(policies);
    }


    @SuppressWarnings("unchecked")
    public UsernamePolicy generateUsernamePolicyFor(User user) {
        Set<Policy> policies = generateCommonPolicy(user);

        return new UsernamePolicy(policies);
    }

    private Set<Policy> generateCommonPolicy(User user) {
        Set<Policy> policies = new LinkedHashSet<>();

        policies.add(new LengthPolicy(8, 20));
        // Compositeパターン refer -> https://ja.wikipedia.org/wiki/Composite_%E3%83%91%E3%82%BF%E3%83%BC%E3%83%B3
        policies.add(new CharacterCombinationPolicy());

        policies.add(new NotSameWithCurrentUsernamePolicy(user.getUsername()));
        policies.add(new NotSameWithCurrentPasswordPolicy(user.getPassword()));

        Person person = user.getPerson();

        policies.add(new NotIncludingNamePolicy(person.getFirstName(), person.getLastName()));

        ContactInformation contactInformation = person.getContactInformation();
        policies.add(new NotSameWithMailAddressPolicy(contactInformation.getMailAddress()));
        policies.add(new NotSameWithTelephonNumberPolicy(contactInformation.getTelephoneNumber()));
        return policies;
    }
}
