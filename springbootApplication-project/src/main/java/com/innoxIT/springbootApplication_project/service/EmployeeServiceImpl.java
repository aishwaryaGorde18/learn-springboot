package com.innoxIT.springbootApplication_project.service;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeInfo save(EmployeeInfo employee) {
        return employeeRepository.save(employee);
    }
    @Override
    public List<EmployeeInfo> saveAll(List<EmployeeInfo> employees) {
        return employeeRepository.saveAll(employees);
    }
    @Override
    public List<EmployeeInfo> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<EmployeeInfo> getById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public EmployeeInfo update(Long id, EmployeeInfo updatedEmployee) {
        EmployeeInfo existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        existing.setName(updatedEmployee.getName());
        existing.setDepartment(updatedEmployee.getDepartment());
        existing.setSalary(updatedEmployee.getSalary());

        return employeeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}