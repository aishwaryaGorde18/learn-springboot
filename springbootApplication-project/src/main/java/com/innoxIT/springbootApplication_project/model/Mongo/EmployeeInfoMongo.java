package com.innoxIT.springbootApplication_project.model.Mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
@Data
public class EmployeeInfoMongo {

    @Id
    private String id;
    public String name;
    public String department;
    public Double salary;
}
