package com.innoxIT.springbootApplication_project.service;

import com.innoxIT.springbootApplication_project.model.EmployeeV1Info;
import com.innoxIT.springbootApplication_project.repository.EmployeeV1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeV1ServiceImpl implements EmployeeV1Service {

    @Autowired
    private EmployeeV1Repository mangoRepo;

    @Override
    public EmployeeV1Info save(EmployeeV1Info employee) {
        return mangoRepo.save(employee);
    }

    @Override
    public List<EmployeeV1Info> saveAll(List<EmployeeV1Info> employees) {
        return mangoRepo.saveAll(employees);
    }

    @Override
    public List<EmployeeV1Info> getAll() {
        return mangoRepo.findAll();
    }

    @Override
    public Optional<EmployeeV1Info> getById(String id) {
        return mangoRepo.findById(id);
    }

    @Override
    public EmployeeV1Info update(String id, EmployeeV1Info updatedEmployee) {
        EmployeeV1Info existing = mangoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        existing.setName(updatedEmployee.getName());
        existing.setDepartment(updatedEmployee.getDepartment());
        existing.setSalary(updatedEmployee.getSalary());

        return mangoRepo.save(existing);
    }

    @Override
    public void delete(String id) {
        mangoRepo.deleteById(id);
}
}
