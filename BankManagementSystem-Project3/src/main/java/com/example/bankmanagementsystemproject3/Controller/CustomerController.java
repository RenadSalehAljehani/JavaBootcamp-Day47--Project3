package com.example.bankmanagementsystemproject3.Controller;

import com.example.bankmanagementsystemproject3.Api.ApiResponse;
import com.example.bankmanagementsystemproject3.InDTO.CustomerInDTO;
import com.example.bankmanagementsystemproject3.Model.MyUser;
import com.example.bankmanagementsystemproject3.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // 1. CRUD
    // 1.1 Add customer (register)
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid CustomerInDTO customerInDTO) {
        customerService.register(customerInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Register Completed."));
    }

    // 1.2 Get customer
    @GetMapping("/get")
    public ResponseEntity getCustomer(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(customerService.getCustomer(myUser.getId()));
    }

    // 1.3 Update customer
    @PutMapping("/update")
    public ResponseEntity updateCustomer(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid CustomerInDTO customerInDTO) {
        customerService.updateCustomer(myUser.getId(), customerInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Customer Updated."));
    }

    // 1.4 Delete customer
    @DeleteMapping("/delete")
    public ResponseEntity deleteCustomer(@AuthenticationPrincipal MyUser myUser) {
        customerService.deleteCustomer(myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Customer Deleted."));
    }

    // Extra endpoint
    @GetMapping("/getAllCustomers")
    public ResponseEntity getAllCustomers(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(customerService.getAllCustomers(myUser.getId()));
    }
}