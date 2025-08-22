package com.innoxIT.springbootApplication_project.integration;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/employees";
    }

    @Test
    void testCreateGetUpdateDeleteEmployee() {
        // 1. Create
        EmployeeInfo emp = new EmployeeInfo();
        emp.setName("John Doe");
        emp.setDepartment("IT");
        emp.setSalary(50000);

        ResponseEntity<EmployeeInfo> createResponse =
                restTemplate.postForEntity(getBaseUrl(), emp, EmployeeInfo.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody()).isNotNull();

        Long empId = createResponse.getBody().getId();
        assertThat(empId).isNotNull();

        // 2. Get All
        ResponseEntity<EmployeeInfo[]> getAllResponse =
                restTemplate.getForEntity(getBaseUrl(), EmployeeInfo[].class);

        assertThat(getAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<EmployeeInfo> employees = Arrays.asList(getAllResponse.getBody());
        assertThat(employees).isNotEmpty();
        assertThat(employees.stream().anyMatch(e -> e.getId().equals(empId))).isTrue();

        // 3. Update
        emp.setId(empId);
        emp.setName("John Updated");
        HttpEntity<EmployeeInfo> updateRequest = new HttpEntity<>(emp);

        ResponseEntity<EmployeeInfo> updateResponse =
                restTemplate.exchange(getBaseUrl() + "/" + empId, HttpMethod.PUT, updateRequest, EmployeeInfo.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getId()).isEqualTo(empId);
        assertThat(updateResponse.getBody().getName()).isEqualTo("John Updated");

        // 4. Delete
        restTemplate.delete(getBaseUrl() + "/" + empId);

        ResponseEntity<EmployeeInfo[]> afterDeleteResponse =
                restTemplate.getForEntity(getBaseUrl(), EmployeeInfo[].class);
        List<EmployeeInfo> afterDelete = Arrays.asList(afterDeleteResponse.getBody());

        assertThat(afterDelete.stream().noneMatch(e -> e.getId().equals(empId))).isTrue();
}
}
