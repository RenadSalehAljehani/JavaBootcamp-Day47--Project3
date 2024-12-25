package com.example.bankmanagementsystemproject3.Controller;

import com.example.bankmanagementsystemproject3.Api.ApiResponse;
import com.example.bankmanagementsystemproject3.InDTO.EmployeeInDTO;
import com.example.bankmanagementsystemproject3.Model.MyUser;
import com.example.bankmanagementsystemproject3.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // 1 CRUD
    // 1.1 Add employee (register)
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid EmployeeInDTO employeeInDTO) {
        employeeService.register(employeeInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Register Completed."));
    }

    // 1.2 Get employee
    @GetMapping("/get")
    public ResponseEntity displayAccount(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(employeeService.getEmployee(myUser.getId()));
    }

    // 1.3 Update employee
    @PutMapping("/update")
    public ResponseEntity updateEmployee(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid EmployeeInDTO employeeInDTO) {
        employeeService.updateEmployee(myUser.getId(), employeeInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Employee Updated."));
    }

    // 1.4 Delete employee
    @DeleteMapping("/delete")
    public ResponseEntity deleteEmployee(@AuthenticationPrincipal MyUser myUser) {
        employeeService.deleteEmployee(myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Employee Deleted."));
    }

    // Extra endpoint
    @GetMapping("/getAllEmployees")
    public ResponseEntity getAllEmployees(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(employeeService.getAllEmployees(myUser.getId()));
    }
}