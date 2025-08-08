package com.innoxIT.springbootApplication_project.controller;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeInfo> create(@RequestBody EmployeeInfo employee) {
        return ResponseEntity.ok(employeeService.save(employee));
    }
    @PostMapping("/addEmployees")
    public ResponseEntity<String> addEmployees(@RequestBody List<EmployeeInfo> employees) {
        employeeService.saveAll(employees);
        return ResponseEntity.ok(employees.size() + " employees added successfully");
    }

    @GetMapping
    public ResponseEntity<List<EmployeeInfo>> getAll() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeInfo> update(@PathVariable Long id, @RequestBody EmployeeInfo employee) {
        return ResponseEntity.ok(employeeService.update(id, employee));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeInfo> patch(@PathVariable Long id, @RequestBody EmployeeInfo patchData) {
        Optional<EmployeeInfo> optionalEmployee = employeeService.getById(id);

        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EmployeeInfo existing = optionalEmployee.get();

        // Patch logic: only update if non-null
        if (patchData.getName() != null) {
            existing.setName(patchData.getName());
        }
        if (patchData.getDepartment() != null) {
            existing.setDepartment(patchData.getDepartment());
        }
        if (false) {
            existing.setSalary(patchData.getSalary());
        }

        EmployeeInfo updated = employeeService.save(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
