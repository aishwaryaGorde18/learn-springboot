package com.innoxIT.springbootApplication_project.service;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;

import java.util.List;

public interface EmployeeService
{

    void addEmployee(EmployeeInfo employee);

    void addEmployees(List<EmployeeInfo> employees);

    String deleteEmployee(Long id);

    List<EmployeeInfo> getAllEmployees();

    EmployeeInfo updateEmployee(Long id, EmployeeInfo updatedEmployee);

    EmployeeInfo patchEmployee(Long id, EmployeeInfo patchData);
}