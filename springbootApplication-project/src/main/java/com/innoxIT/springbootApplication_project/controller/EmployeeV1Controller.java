package com.innoxIT.springbootApplication_project.controller;

import com.innoxIT.springbootApplication_project.model.EmployeeV1Info;
import com.innoxIT.springbootApplication_project.service.EmployeeV1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeV1Controller {

    @Autowired
    private EmployeeV1Service mangoService;

    @PostMapping
    public ResponseEntity<EmployeeV1Info> create(@RequestBody EmployeeV1Info employee) {
        return ResponseEntity.ok(mangoService.save(employee));
    }

    @PostMapping("/addEmployees")
    public ResponseEntity<String> addEmployees(@RequestBody List<EmployeeV1Info> employees) {
        mangoService.saveAll(employees);
        return ResponseEntity.ok(employees.size() + " employees added successfully");
    }

    @GetMapping
    public ResponseEntity<List<EmployeeV1Info>> getAll() {
        return ResponseEntity.ok(mangoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeV1Info> getById(@PathVariable String id) {
        return mangoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeV1Info> update(@PathVariable String id, @RequestBody EmployeeV1Info employee) {
        return ResponseEntity.ok(mangoService.update(id, employee));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeV1Info> patch(@PathVariable String id, @RequestBody EmployeeV1Info patchData) {
        Optional<EmployeeV1Info> optional = mangoService.getById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EmployeeV1Info existing = optional.get();

        if (patchData.getName() != null) {
            existing.setName(patchData.getName());
        }
        if (patchData.getDepartment() != null) {
            existing.setDepartment(patchData.getDepartment());
        }
        if (patchData.getSalary() != null) {
            existing.setSalary(patchData.getSalary());
        }

        EmployeeV1Info updated = mangoService.save(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        mangoService.delete(id);
        return ResponseEntity.noContent().build();
}
}
