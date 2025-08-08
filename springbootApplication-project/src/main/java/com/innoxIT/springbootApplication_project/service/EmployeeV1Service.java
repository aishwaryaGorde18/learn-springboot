package com.innoxIT.springbootApplication_project.service;

import com.innoxIT.springbootApplication_project.model.EmployeeV1Info;

import java.util.List;
import java.util.Optional;

public interface EmployeeV1Service {
    EmployeeV1Info save(EmployeeV1Info employee);
    List<EmployeeV1Info> saveAll(List<EmployeeV1Info> employees);
    List<EmployeeV1Info> getAll();
    EmployeeV1Info update(String id, EmployeeV1Info employee);
    void delete(String id);
    Optional<EmployeeV1Info> getById(String id);
}