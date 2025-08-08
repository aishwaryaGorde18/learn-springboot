package com.innoxIT.springbootApplication_project.repositoryTest;

import com.innoxIT.springbootApplication_project.model.EmployeeV1Info;
import com.innoxIT.springbootApplication_project.repository.EmployeeV1Repository;
import com.innoxIT.springbootApplication_project.repositoryTest.MongoAuditConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(MongoAuditConfig.class) // Import audit config if you have one
class EmployeeV1RepositoryTest {

    @Autowired
    private EmployeeV1Repository employeeV1Repository;

    @Test
    @DisplayName("Should save and retrieve employee with auditing")
    void testSaveAndFindById() {
        EmployeeV1Info employee = new EmployeeV1Info();
        employee.setName("John Doe");
        employee.setDepartment("IT");
        employee.setSalary(75000.0);

        EmployeeV1Info saved = employeeV1Repository.save(employee);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt()); // Should be set by auditing
        assertNotNull(saved.getUpdatedAt());

        Optional<EmployeeV1Info> found = employeeV1Repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    @DisplayName("Should find all employees")
    void testFindAll() {
        employeeV1Repository.deleteAll();

        EmployeeV1Info e1 = new EmployeeV1Info();
        e1.setName("Alice");
        e1.setDepartment("HR");
        e1.setSalary(60000.0);

        EmployeeV1Info e2 = new EmployeeV1Info();
        e2.setName("Bob");
        e2.setDepartment("Finance");
        e2.setSalary(65000.0);

        employeeV1Repository.saveAll(List.of(e1, e2));

        List<EmployeeV1Info> all = employeeV1Repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("Should update employee and set updatedAt")
    void testUpdate() {
        EmployeeV1Info emp = new EmployeeV1Info();
        emp.setName("Charlie");
        emp.setDepartment("DevOps");
        emp.setSalary(70000.0);

        EmployeeV1Info saved = employeeV1Repository.save(emp);

        saved.setSalary(80000.0);
        EmployeeV1Info updated = employeeV1Repository.save(saved);

        assertEquals(80000.0, updated.getSalary());
        assertTrue(updated.getUpdatedAt().after(updated.getCreatedAt()));
    }

    @Test
    @DisplayName("Should delete employee by ID")
    void testDeleteById() {
        EmployeeV1Info emp = new EmployeeV1Info();
        emp.setName("Temp");
        emp.setDepartment("Testing");
        emp.setSalary(40000.0);

        EmployeeV1Info saved = employeeV1Repository.save(emp);

        employeeV1Repository.deleteById(saved.getId());

        Optional<EmployeeV1Info> result = employeeV1Repository.findById(saved.getId());
        assertFalse(result.isPresent());
    }
}
