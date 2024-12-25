package com.example.bankmanagementsystemproject3.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Employee {
    @Id
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null")
    private String position;

    @Column(columnDefinition = "decimal not null")
    private BigDecimal salary;

    @OneToOne
    @MapsId
    @JsonIgnore
    private MyUser user;
}