package com.innoxIT.springbootApplication_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class,
                org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class,
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
        })
 class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllEmployees() throws Exception {
        EmployeeInfo e1 = new EmployeeInfo(1L, "Alice", "IT", 50000);
        EmployeeInfo e2 = new EmployeeInfo(2L, "Bob", "HR", 45000);

        when(employeeService.getAll()).thenReturn(Arrays.asList(e1, e2));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeInfo employee = new EmployeeInfo(null, "John", "Finance", 60000);
        EmployeeInfo savedEmployee = new EmployeeInfo(1L, "John", "Finance", 60000);

        when(employeeService.save(any(EmployeeInfo.class))).thenReturn(savedEmployee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        Long id = 1L;
        EmployeeInfo updated = new EmployeeInfo(id, "Mike", "IT", 70000);

        when(employeeService.update(eq(id), any(EmployeeInfo.class))).thenReturn(updated);

        mockMvc.perform(put("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mike"));
    }

    @Test
    void testPatchEmployee() throws Exception {
        Long id = 1L;
        EmployeeInfo existing = new EmployeeInfo(id, "Sam", "Sales", 70000);
        EmployeeInfo updated = new EmployeeInfo(id, "Sam", "Sales", 75000);

        // Mock service to return existing employee when looking up by ID
        when(employeeService.getById(id)).thenReturn(Optional.of(existing));
        // Mock service to return updated employee after patch
        when(employeeService.save(any(EmployeeInfo.class))).thenReturn(updated);

        // Partial update with only salary
        EmployeeInfo patch = new EmployeeInfo();
        patch.setSalary(75000);

        mockMvc.perform(patch("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary").value(75000));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Long employeeId = 1L;

        doNothing().when(employeeService).delete(employeeId);

        mockMvc.perform(delete("/employees/{id}", employeeId))
                .andExpect(status().isNoContent());
    }
}

