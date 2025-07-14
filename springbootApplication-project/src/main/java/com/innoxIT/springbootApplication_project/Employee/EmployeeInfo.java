package com.innoxIT.springbootApplication_project.Employee;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.repository.cdi.Eager;

@Entity
@Table(name = "employees")
@Data
public class EmployeeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String name;
    public String department;
    public Double salary;  //
}
