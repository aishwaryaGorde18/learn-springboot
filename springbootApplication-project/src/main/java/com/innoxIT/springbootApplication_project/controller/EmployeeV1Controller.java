package com.innoxIT.springbootApplication_project.controller;

import com.innoxIT.springbootApplication_project.model.Mongo.EmployeeInfoMongo;
import com.innoxIT.springbootApplication_project.service.EmployeeServiceMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeV1Controller {

    @Autowired
    private EmployeeServiceMongo mangoService;

    @PostMapping
    public ResponseEntity<EmployeeInfoMongo> create(@RequestBody EmployeeInfoMongo employee) {
        return ResponseEntity.ok(mangoService.save(employee));
    }

    @PostMapping("/addEmployees")
    public ResponseEntity<String> addEmployees(@RequestBody List<EmployeeInfoMongo> employees) {
        mangoService.saveAll(employees);
        return ResponseEntity.ok(employees.size() + " employees added successfully");
    }

    @GetMapping
    public ResponseEntity<List<EmployeeInfoMongo>> getAll() {
        return ResponseEntity.ok(mangoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeInfoMongo> getById(@PathVariable String id) {
        return mangoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeInfoMongo> update(@PathVariable String id, @RequestBody EmployeeInfoMongo employee) {
        return ResponseEntity.ok(mangoService.update(id, employee));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeInfoMongo> patch(@PathVariable String id, @RequestBody EmployeeInfoMongo patchData) {
        Optional<EmployeeInfoMongo> optional = mangoService.getById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EmployeeInfoMongo existing = optional.get();

        if (patchData.getName() != null) {
            existing.setName(patchData.getName());
        }
        if (patchData.getDepartment() != null) {
            existing.setDepartment(patchData.getDepartment());
        }
        if (patchData.getSalary() != null) {
            existing.setSalary(patchData.getSalary());
        }

        EmployeeInfoMongo updated = mangoService.save(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        mangoService.delete(id);
        return ResponseEntity.noContent().build();
}
}
