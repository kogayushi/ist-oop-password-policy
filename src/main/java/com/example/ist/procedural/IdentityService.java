package com.example.ist.procedural;

import com.example.ist.domain.model.identity.Password;
import com.example.ist.domain.model.identity.User;
import com.example.ist.domain.model.identity.UserRepository;
import com.example.ist.domain.model.identity.Username;
import com.example.ist.exception.ViolatedPolicyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class IdentityService {
    private static final Pattern INCLUDING_UPPER_CASE_ALPHABET_AT_LEAST_ONE              = Pattern.compile("^.*[A-Z].*$");
    private static final Pattern INCLUDING_LOWER_CASE_ALPHABET_AT_LEAST_ONE              = Pattern.compile("^.*[a-z].*$");
    private static final Pattern INCLUDING_NUMBER_AT_LEAST_ONE                           = Pattern.compile("^.*[0-9].*$");
    private static final Pattern NOT_INCLUDING_ALLOWED_CHARACTER                         = Pattern.compile("^(?!(.*[^a-zA-Z0-9!\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~]+.*)).*$");
    private static final String INPUTTED_AUTHENTICATION_FACTOR_VIOLATED_CHARACTER_POLICY = "inputted %s violated character policy";

    private final UserRepository userRepository;

    public void changePassword(String id, String newPassword) {
        User user = this.userRepository.userFromId(UUID.fromString(id));

        this.validateCommonPolicy(newPassword, user, "password");

        this.userRepository.updatePassword(user.getId(), new Password(newPassword));

    }



    public void changeUsername(String id, String newUsername) {
        User user = this.userRepository.userFromId(UUID.fromString(id));

        this.validateCommonPolicy(newUsername, user, "username");

        userRepository.updateUsername(user.getId(), new Username(newUsername));

    }

    private void validateCommonPolicy(String newAuthenticationFactor, User user, String name) {
        if (newAuthenticationFactor.length() < 8 || newAuthenticationFactor.length() > 20) {
            String msg = "inputted " + name + " violated password length policy";
            log.warn(msg); // it's warn just for testing.
            throw new ViolatedPolicyException(msg);
        }

        if (INCLUDING_UPPER_CASE_ALPHABET_AT_LEAST_ONE.matcher(newAuthenticationFactor).find() == false) {
            String msg = "inputted " + name + " violated character policy";
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }

        if (INCLUDING_LOWER_CASE_ALPHABET_AT_LEAST_ONE.matcher(newAuthenticationFactor).find() == false) {
            String msg = String.format(INPUTTED_AUTHENTICATION_FACTOR_VIOLATED_CHARACTER_POLICY, name);
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }

        if (INCLUDING_NUMBER_AT_LEAST_ONE.matcher(newAuthenticationFactor).find() == false) {
            String msg = String.format(INPUTTED_AUTHENTICATION_FACTOR_VIOLATED_CHARACTER_POLICY, name);
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }

        if (NOT_INCLUDING_ALLOWED_CHARACTER.matcher(newAuthenticationFactor).find() == false) {
            String msg = String.format(INPUTTED_AUTHENTICATION_FACTOR_VIOLATED_CHARACTER_POLICY, name);
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }

        if (user.getUsername().equalsIgnoreCase(newAuthenticationFactor)) {
            String msg = "inputted " + name + " violated not same with current username policy";
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }

        if (user.getPassword().equalsIgnoreCase(newAuthenticationFactor)) { // Usually, stored password is hashed. But because this is just sample, it's not.
            String msg = "inputted " + name + " violated not same with current password policy";
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }

        if (newAuthenticationFactor.toUpperCase().contains(user.getPerson().getFirstName().toUpperCase())) {
            String msg = "inputted " + name + " violated not including first name policy";
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }

        if (newAuthenticationFactor.toUpperCase().contains(user.getPerson().getLastName().toUpperCase())) {
            String msg = "inputted " + name + " violated not including last name policy";
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }

        if (newAuthenticationFactor.equals(user.getPerson().getContactInformation().getMailAddress().getValue())) {
            String msg = "inputted " + name + " violated not same with mail address policy";
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }

        if (newAuthenticationFactor.equals(user.getPerson().getContactInformation().getTelephoneNumber().getValue())) {
            String msg = "inputted " + name + " violated not same with mail address policy";
            log.warn(msg);
            throw new ViolatedPolicyException(msg);
        }
    }
}
