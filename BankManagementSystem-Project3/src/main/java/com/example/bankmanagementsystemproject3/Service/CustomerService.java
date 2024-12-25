package com.example.bankmanagementsystemproject3.Service;

import com.example.bankmanagementsystemproject3.Api.ApiException;
import com.example.bankmanagementsystemproject3.InDTO.CustomerInDTO;
import com.example.bankmanagementsystemproject3.Model.Account;
import com.example.bankmanagementsystemproject3.Model.Customer;
import com.example.bankmanagementsystemproject3.Model.Employee;
import com.example.bankmanagementsystemproject3.Model.MyUser;
import com.example.bankmanagementsystemproject3.OutDTO.AccountOutDTO;
import com.example.bankmanagementsystemproject3.OutDTO.CustomerOutDTO;
import com.example.bankmanagementsystemproject3.Repository.AuthRepository;
import com.example.bankmanagementsystemproject3.Repository.CustomerRepository;
import com.example.bankmanagementsystemproject3.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    // 1. CRUD
    // 1.1 Add customer (register)
    public void register(CustomerInDTO customerInDTO) {
        String hashPassword = new BCryptPasswordEncoder().encode(customerInDTO.getPassword());
        MyUser myUser = new MyUser(null, customerInDTO.getUsername(), hashPassword, customerInDTO.getName(), customerInDTO.getEmail(), "Customer", null, null);
        Customer customer = new Customer(null, customerInDTO.getPhoneNumber(), myUser, customerInDTO.getAccounts());
        myUser.setCustomer(customer);
        authRepository.save(myUser);
        customerRepository.save(customer);
    }

    // 1.2 Get customer
    public CustomerOutDTO getCustomer(Integer userId) {
        MyUser myUser = authRepository.findMyUserById(userId);
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer Not Found.");
        }

        return new CustomerOutDTO(myUser.getUsername(), myUser.getPassword(), myUser.getName(), myUser.getEmail(), customer.getPhoneNumber(), customer.getAccounts());
    }

    // 1.3 Update customer
    public void updateCustomer(Integer userId, CustomerInDTO customerInDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer Not Found.");
        }

        // Update user details
        myUser.setUsername(customerInDTO.getUsername());
        String hashPassword = new BCryptPasswordEncoder().encode(customerInDTO.getPassword());
        myUser.setPassword(hashPassword);
        myUser.setName(customerInDTO.getName());
        myUser.setEmail(customerInDTO.getEmail());

        // Save changes
        authRepository.save(myUser);

        // Update customer details
        customer.setPhoneNumber(customer.getPhoneNumber());
        customer.setAccounts(customer.getAccounts());
        customer.setUser(myUser);

        // Save changes
        customerRepository.save(customer);
    }

    // 1.4 Delete customer
    public void deleteCustomer(Integer userId) {
        MyUser myUser = authRepository.findMyUserById(userId);
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer Not Found.");
        }
        myUser.setCustomer(null);
        customerRepository.delete(customer);
        authRepository.delete(myUser);
    }

    // Admin endpoint
    public List<CustomerOutDTO> getAllCustomers(Integer userId) {
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new ApiException("No Customers Found.");
        }

        List<CustomerOutDTO> customerOutDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerOutDTO customerOutDTO = new CustomerOutDTO(customer.getUser().getUsername(),
                    customer.getUser().getPassword(), customer.getUser().getName(),
                    customer.getUser().getEmail(), customer.getPhoneNumber(),
                    customer.getAccounts());
            customerOutDTOS.add(customerOutDTO);
        }
        return customerOutDTOS;
    }
}