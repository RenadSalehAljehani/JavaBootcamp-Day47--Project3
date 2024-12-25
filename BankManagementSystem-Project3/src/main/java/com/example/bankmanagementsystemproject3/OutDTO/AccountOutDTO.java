package com.example.bankmanagementsystemproject3.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class AccountOutDTO {
    private String accountNumber;

    private BigDecimal balance;
}