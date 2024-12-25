package com.example.bankmanagementsystemproject3.Repository;

import com.example.bankmanagementsystemproject3.Model.Account;
import com.example.bankmanagementsystemproject3.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByIdAndCustomer(Integer id, Customer customer);

    Account findAccountById(Integer id);
}