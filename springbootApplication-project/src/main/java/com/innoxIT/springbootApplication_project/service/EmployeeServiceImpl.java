package com.innoxIT.springbootApplication_project.service;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    @Override
    public void addEmployee(EmployeeInfo employee) {
        repo.save(employee);
    }

    @Override
    public void addEmployees(List<EmployeeInfo> employees) {
        repo.saveAll(employees);
    }

    @Override
    public String deleteEmployee(Long id) {
        repo.deleteById(id);
        return "Deleted employee with ID: " + id;
    }

    @Override
    public List<EmployeeInfo> getAllEmployees() {
        return repo.findAll();
    }

    @Override
    public EmployeeInfo updateEmployee(Long id, EmployeeInfo updatedEmployee) {
        return repo.findById(id).map(existing -> {
            existing.setName(updatedEmployee.getName());
            existing.setDepartment(updatedEmployee.getDepartment());
            existing.setSalary(updatedEmployee.getSalary());
            return repo.save(existing);
        }).orElse(null);
    }

    @Override
    public EmployeeInfo patchEmployee(Long id, EmployeeInfo patchData) {
        Optional<EmployeeInfo> optional = repo.findById(id);
        if (optional.isEmpty()) return null;

        EmployeeInfo existing = optional.get();

        if (patchData.getName() != null) {
            existing.setName(patchData.getName());
        }
        if (patchData.getDepartment() != null) {
            existing.setDepartment(patchData.getDepartment());
        }
        if (patchData.getSalary() != 0.0) {
            existing.setSalary(patchData.getSalary());
        }

        return repo.save(existing);
}
}
