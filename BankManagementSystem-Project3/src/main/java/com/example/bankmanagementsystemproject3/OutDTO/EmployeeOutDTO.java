package com.example.bankmanagementsystemproject3.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class EmployeeOutDTO {
    private String username;

    private String password;

    private String name;

    private String email;

    private String position;

    private BigDecimal salary;
}