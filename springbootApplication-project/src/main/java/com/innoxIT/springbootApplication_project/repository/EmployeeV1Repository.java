package com.innoxIT.springbootApplication_project.repository;

import com.innoxIT.springbootApplication_project.model.EmployeeV1Info;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeV1Repository extends MongoRepository<EmployeeV1Info, String> {
}
