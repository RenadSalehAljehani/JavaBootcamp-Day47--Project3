package com.example.bankmanagementsystemproject3.OutDTO;

import com.example.bankmanagementsystemproject3.Model.Account;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class CustomerOutDTO {
    private String username;

    private String password;

    private String name;

    private String email;

    //@Pattern(regexp = "^05\\d{8}$")
    private String phoneNumber;

    private Set<Account> accounts;
}