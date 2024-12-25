package com.example.bankmanagementsystemproject3.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertFalse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(20) not null")
    private String accountNumber;

    @Column(columnDefinition = "decimal not null")
    private BigDecimal balance;

    @Column(columnDefinition = "boolean default false")
    @AssertFalse(message = "The account must not be active by default.")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnore
    private Customer customer;
}