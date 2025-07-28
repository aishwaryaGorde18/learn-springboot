package com.innoxIT.springbootApplication_project.service;

import com.innoxIT.springbootApplication_project.model.Mongo.EmployeeInfoMongo;
import com.innoxIT.springbootApplication_project.repository.MongoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceMongoImpl implements EmployeeServiceMongo {

    @Autowired
    private MongoRepo mangoRepo;

    @Override
    public EmployeeInfoMongo save(EmployeeInfoMongo employee) {
        return mangoRepo.save(employee);
    }

    @Override
    public List<EmployeeInfoMongo> saveAll(List<EmployeeInfoMongo> employees) {
        return mangoRepo.saveAll(employees);
    }

    @Override
    public List<EmployeeInfoMongo> getAll() {
        return mangoRepo.findAll();
    }

    @Override
    public Optional<EmployeeInfoMongo> getById(String id) {
        return mangoRepo.findById(id);
    }

    @Override
    public EmployeeInfoMongo update(String id, EmployeeInfoMongo updatedEmployee) {
        EmployeeInfoMongo existing = mangoRepo.findById(id)
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
