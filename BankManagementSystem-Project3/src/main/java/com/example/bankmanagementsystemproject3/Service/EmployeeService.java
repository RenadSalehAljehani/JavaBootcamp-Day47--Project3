package com.example.bankmanagementsystemproject3.Service;

import com.example.bankmanagementsystemproject3.Api.ApiException;
import com.example.bankmanagementsystemproject3.InDTO.EmployeeInDTO;
import com.example.bankmanagementsystemproject3.Model.Employee;
import com.example.bankmanagementsystemproject3.Model.MyUser;
import com.example.bankmanagementsystemproject3.OutDTO.EmployeeOutDTO;
import com.example.bankmanagementsystemproject3.Repository.AuthRepository;
import com.example.bankmanagementsystemproject3.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final AuthRepository authRepository;
    private final EmployeeRepository employeeRepository;

    // 1. CRUD
    // 1.1 Add employee (register)
    public void register(EmployeeInDTO employeeInDTO) {
        String hashPassword = new BCryptPasswordEncoder().encode(employeeInDTO.getPassword());
        MyUser myUser = new MyUser(null, employeeInDTO.getUsername(), hashPassword, employeeInDTO.getName(), employeeInDTO.getEmail(), "Employee", null, null);
        Employee employee = new Employee(null, employeeInDTO.getPosition(), employeeInDTO.getSalary(), myUser);
        myUser.setEmployee(employee);
        authRepository.save(myUser);
        employeeRepository.save(employee);
    }

    // 1.2 Get employee
    public EmployeeOutDTO getEmployee(Integer userId) {
        MyUser myUser = authRepository.findMyUserById(userId);
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }
        return new EmployeeOutDTO(myUser.getUsername(), myUser.getPassword(), myUser.getName(), myUser.getEmail(), employee.getPosition(), employee.getSalary());
    }

    // 1.3 Update employee
    public void updateEmployee(Integer userId, EmployeeInDTO employeeInDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }

        // Update user details
        myUser.setUsername(employeeInDTO.getUsername());
        String hashPassword = new BCryptPasswordEncoder().encode(employeeInDTO.getPassword());
        myUser.setPassword(hashPassword);
        myUser.setName(employeeInDTO.getName());
        myUser.setEmail(employeeInDTO.getEmail());

        // Save changes
        authRepository.save(myUser);

        // Update employee details
        employee.setPosition(employeeInDTO.getPosition());
        employee.setSalary(employeeInDTO.getSalary());
        employee.setUser(myUser);

        // Save changes
        employeeRepository.save(employee);
    }

    // 1.4 Delete employee
    public void deleteEmployee(Integer userId) {
        MyUser myUser = authRepository.findMyUserById(userId);
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }
        myUser.setEmployee(null);
        employeeRepository.delete(employee);
        authRepository.delete(myUser);
    }

    // Admin endpoint
    public List<EmployeeOutDTO> getAllEmployees(Integer userId) {
        MyUser myUser = authRepository.findMyUserById(userId);
        if (myUser == null) {
            throw new ApiException("User Not Found.");
        }
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new ApiException("No Employees Found.");
        }

        List<EmployeeOutDTO> employeeOutDTOS = new ArrayList<>();
        for (Employee employee: employees) {
            EmployeeOutDTO employeeOutDTO = new EmployeeOutDTO(employee.getUser().getUsername(),
                    employee.getUser().getPassword(), employee.getUser().getName(),
                    employee.getUser().getEmail(), employee.getPosition(),
                    employee.getSalary());
            employeeOutDTOS.add(employeeOutDTO);
        }
        return employeeOutDTOS;
    }
}