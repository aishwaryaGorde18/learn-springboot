package com.innoxIT.springbootApplication_project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll(); // clear test DB before each test
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeInfo emp = new EmployeeInfo(null, "John Doe", "IT", 50000);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        List<EmployeeInfo> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(1);
    }

    @Test
    void testGetAllEmployees() throws Exception {
        employeeRepository.save(new EmployeeInfo(null, "Alice", "HR", 40000));
        employeeRepository.save(new EmployeeInfo(null, "Bob", "Finance", 60000));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        EmployeeInfo emp = employeeRepository.save(new EmployeeInfo(null, "Alice", "HR", 40000));

        emp.setName("Alice Updated");
        emp.setDepartment("IT");
        emp.setSalary(55000);

        mockMvc.perform(put("/employees/" + emp.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        EmployeeInfo emp = employeeRepository.save(new EmployeeInfo(null, "Mark", "IT", 70000));

        mockMvc.perform(delete("/employees/" + emp.getId()))
                .andExpect(status().isNoContent());

        assertThat(employeeRepository.findAll()).isEmpty();
    }
}