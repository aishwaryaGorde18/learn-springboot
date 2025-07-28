package com.innoxIT.springbootApplication_project.service;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    EmployeeInfo save(EmployeeInfo employee);
    List<EmployeeInfo> saveAll(List<EmployeeInfo> employees);
    List<EmployeeInfo> getAll();
    EmployeeInfo update(Long id, EmployeeInfo employee);
    void delete(Long id);

    Optional<EmployeeInfo> getById(Long id);

}