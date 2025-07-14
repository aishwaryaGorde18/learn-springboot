package com.innoxIT.springbootApplication_project.controller;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository repo;


    @PostMapping("/addEmployee")
    public void addEmployee(@RequestBody EmployeeInfo employees) {
        repo.save(employees);
    }

    @PostMapping("/addEmployees")
    public ResponseEntity<String> addEmployees(@RequestBody List<EmployeeInfo> employees) {
        repo.saveAll(employees);
        return ResponseEntity.ok(employees.size() + " employees added successfully");
    }


    @DeleteMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        EmployeeRepository.deleteBy(id);
        return "Deleted employee with ID: " + id;
    }

    @GetMapping("/employees")
    public List<EmployeeInfo> getAllEmployees() {
        return repo.findAll();
    }

    @PutMapping("/employees/{id}")
    public EmployeeInfo updateEmployee(@PathVariable Long id, @RequestBody EmployeeInfo updatedEmployee) {
        return repo.findById(id).map(existing ->
        {
            existing.setName(updatedEmployee.getName());
            existing.setDepartment(updatedEmployee.getDepartment());
            existing.setSalary(updatedEmployee.getSalary());
            return repo.save(existing);
        }).orElse(null);

    }

    @PatchMapping("/employees/{id}")
    public ResponseEntity<EmployeeInfo> simplePatchEmployee(@PathVariable Long id, @RequestBody EmployeeInfo patch) {
        EmployeeInfo existing = repo.findById(id).get();

        if (patch.getName() != null) {
            existing.setName(patch.getName());
        }
        if (patch.getDepartment() != null) {
            existing.setDepartment(patch.getDepartment());
        }
        if (patch.getSalary() != 0.0) {
            existing.setSalary(patch.getSalary());
        }

        EmployeeInfo updated = repo.save(existing);
        return ResponseEntity.ok(updated);
    }
}


