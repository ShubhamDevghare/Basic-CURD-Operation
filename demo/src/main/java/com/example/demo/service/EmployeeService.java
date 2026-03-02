package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.EmployeeRequestDTO;
import com.example.demo.entity.Employee;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;

    public EmployeeService(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    // Create
    public Employee createEmployee(EmployeeRequestDTO dto) {

        if (employeeRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setAddress(dto.getAddress());

        return employeeRepo.save(employee);
    }

    // Find By Id
    public Employee findEmployee(Long id) {

        return employeeRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id " + id));
    }

    // Get All
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    // Update
    @Transactional
    public Employee updateEmployee(Long id, EmployeeRequestDTO dto) {

        Employee existing = employeeRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id " + id));

        if (!existing.getEmail().equals(dto.getEmail())) {
            if (employeeRepo.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            existing.setEmail(dto.getEmail());
        }

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());

        return existing;
    }

    // Delete
    @Transactional
    public void deleteEmployee(Long id) {

        if (!employeeRepo.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id " + id);
        }

        employeeRepo.deleteById(id);
    }
}