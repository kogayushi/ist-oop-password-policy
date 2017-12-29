package com.example.ist.domain.model.identity;

import lombok.Value;

@Value
public class ContactInformation {
    private TelephoneNumber telephoneNumber;
    private MailAddress mailAddress;
}
