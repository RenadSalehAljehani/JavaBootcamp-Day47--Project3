package com.example.bankmanagementsystemproject3.InDTO;

import com.example.bankmanagementsystemproject3.Model.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Set;

@Setter
@Getter
public class CustomerInDTO {

    private String username;
    private Integer userId;
    @NotEmpty(message = "Username can't be empty.")
    @Size(min = 4, max = 10, message = "Length must be between 4-10 characters.")
    @Check(constraints = "length(username)>=4")
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

    @NotEmpty(message = "Phone number can't be empty.")
    @Pattern(regexp = "^05\\d{8}$")
    private String phoneNumber;

    private Set<Account> accounts;
}