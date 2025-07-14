package com.innoxIT.springbootApplication_project.repository;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeInfo, Long> {
    static void deleteBy(Long id) {
    }
}



