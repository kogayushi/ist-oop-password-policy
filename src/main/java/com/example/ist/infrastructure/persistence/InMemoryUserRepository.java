package com.example.ist.infrastructure.persistence;

import com.example.ist.domain.model.identity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserRepository implements UserRepository {

    private Map<String, User> inmemoryWithUsername = initialize();

    private Map<String, User> initialize() {
        // @formatter:off
        Map<String, User> users = new HashMap<>();
        users.put(
                "Yushi.Koga.314" ,
                new User(
                        UUID.fromString("8A1E74BD-FBC9-43B2-9AAC-0D356022F887"),
                        "Yushi.Koga.314",
                        "Passw0rd",
                        new Person(
                                "yushi",
                                "koga",
                                Sex.MALE,
                                new ContactInformation(
                                        new TelephoneNumber("090-1234-5678"),
                                        new MailAddress("K314@is-tech.co.jp")))));
        // @formatter:on

        return users;
    }

    @Override
    public User userFromId(UUID id) {
        return this.convertKeyFromUsernameToId().get(id.toString());
    }

    @Override
    public void updatePassword(UUID id, Password password) {
        // do nothing, just logging. 本題と関係ない
        User user = this.convertKeyFromUsernameToId().get(id.toString());
        user.setPassword(password.getValue());
        this.inmemoryWithUsername.put(user.getUsername(), user);
        log.info("{}'s password has been updated with {}", id , password);
    }

    @Override
    public void updateUsername(UUID id, Username username) {

    }

    private Map<String, User> convertKeyFromUsernameToId() {
        Map<String, User> converted = inmemoryWithUsername.entrySet().stream().collect(Collectors.toMap(user -> user.getValue().getId().toString(), user -> user.getValue()));
        return converted;
    }

}
