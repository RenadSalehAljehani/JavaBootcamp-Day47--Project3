package com.example.bankmanagementsystemproject3.InDTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Getter
@Setter
public class EmployeeInDTO {
    private Integer userId;
    @NotEmpty(message = "Username can't be empty.")
    @Size(min = 4, max = 10, message = "Length must be between 4-10 characters.")
    @Check(constraints = "length(username)>=4")
    private String username;

    @NotEmpty(message = "Password cant' be empty.")
    @Size(min = 6, message = "Length must be at least 6 characters.")
    @Check(constraints = "length(password)>=6")
    private String password;

    @NotEmpty(message = "Name can't be empty.")
    @Size(min = 2, max = 20, message = "Length must be between 2-20 characters.")
    @Check(constraints = "length(name)>=2")
    private String name;

        @Email(message = "Invalid email format.")
    @Check(constraints = "email LIKE '_%@_%._%'")
    private String email;

        @NotEmpty(message = "Position can't be empty.")
    private String position;

        @NotNull(message = "Salary can't be empty.")
    @Positive(message = "Salary must be a positive number larger than zero.")
    private BigDecimal salary;
}