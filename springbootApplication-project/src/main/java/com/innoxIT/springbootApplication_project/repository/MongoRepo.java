package com.innoxIT.springbootApplication_project.repository;

import com.innoxIT.springbootApplication_project.model.Mongo.EmployeeInfoMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoRepo extends MongoRepository<EmployeeInfoMongo, String> {
}
