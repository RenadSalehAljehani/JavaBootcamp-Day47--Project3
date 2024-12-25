package com.example.bankmanagementsystemproject3.InDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountInDTO {
    private Integer customerId;

    @NotEmpty(message = "Account number can't be empty.")
    @Pattern(regexp = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$")
    private String accountNumber;

    @NotNull(message = "Balance can't be empty.")
    @Positive(message = "Balance must be a positive number larger than zero.")
    private BigDecimal balance;
}