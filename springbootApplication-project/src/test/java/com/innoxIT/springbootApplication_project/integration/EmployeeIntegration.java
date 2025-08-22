package com.innoxIT.springbootApplication_project.integration;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class EmployeeIntegration {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/employees";
        employeeRepository.deleteAll();
        employeeRepository.save(new EmployeeInfo(null, "Alice", "HR", 50000));
        employeeRepository.save(new EmployeeInfo(null, "Bob", "IT", 60000));
    }

    // ✅ CREATE
    @Test
    void testCreateEmployee() {
        EmployeeInfo newEmp = new EmployeeInfo(null, "Charlie", "Finance", 70000);

        ResponseEntity<EmployeeInfo> response =
                restTemplate.postForEntity(baseUrl, newEmp, EmployeeInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Charlie");
    }

    // ✅ READ (GET all)
    @Test
    void testGetEmployees() {
        ResponseEntity<EmployeeInfo[]> response =
                restTemplate.getForEntity(baseUrl, EmployeeInfo[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    // ✅ UPDATE (PUT)
    @Test
    void testUpdateEmployee() {
        EmployeeInfo existing = employeeRepository.findAll().get(0);
        existing.setDepartment("UpdatedDept");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmployeeInfo> request = new HttpEntity<>(existing, headers);

        ResponseEntity<EmployeeInfo> response = restTemplate.exchange(
                baseUrl + "/" + existing.getId(),
                HttpMethod.PUT,
                request,
                EmployeeInfo.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDepartment()).isEqualTo("UpdatedDept");
    }

    // ✅ PATCH
    @Test
    void testPatchEmployee() {
        EmployeeInfo existing = employeeRepository.findAll().get(0);

        String patchJson = """
                {
                  "name": "UpdatedName"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(patchJson, headers);

        ResponseEntity<EmployeeInfo> response = restTemplate.exchange(
                baseUrl + "/" + existing.getId(),
                HttpMethod.PATCH,
                request,
                EmployeeInfo.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("UpdatedName");
    }

    // ✅ DELETE
    @Test
    void testDeleteEmployee() {
        EmployeeInfo existing = employeeRepository.findAll().get(0);

        restTemplate.delete(baseUrl + "/" + existing.getId());

        Optional<EmployeeInfo> deleted = employeeRepository.findById(existing.getId());
        assertThat(deleted).isEmpty();
    }
}
