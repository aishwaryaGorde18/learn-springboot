package com.innoxIT.springbootApplication_project.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeInfo employee;

    @BeforeEach
    void setUp() {
        employee = new EmployeeInfo();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setDepartment("IT");
        employee.setSalary(50000);
    }

    @Test
    void testCreateEmployee() throws Exception {
        when(employeeService.save(any(EmployeeInfo.class))).thenReturn(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(jsonPath("$.salary").value(50000));
    }
    @Test
    void testAddMultipleEmployees() throws Exception {
        List<EmployeeInfo> employeeList = List.of(employee);

        when(employeeService.saveAll(anyList())).thenReturn(employeeList);

        mockMvc.perform(post("/employees/addEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeList)))
                .andExpect(status().isOk())
                .andExpect(content().string("1 employees added successfully"));
    }

    @Test
    void testGetAllEmployees() throws Exception {
        List<EmployeeInfo> employeeList = List.of(employee);

        when(employeeService.getAll()).thenReturn(employeeList);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].department").value("IT"))
                .andExpect(jsonPath("$[0].salary").value(50000));
    }


    @Test
    void testUpdateEmployee() throws Exception {
        when(employeeService.update(eq(1L), any(EmployeeInfo.class))).thenReturn(employee);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testPatchEmployee() throws Exception {
        // Only name is updated
        EmployeeInfo patchData = new EmployeeInfo();
        patchData.setName("Jane Doe");

        EmployeeInfo patched = new EmployeeInfo();
        patched.setId(1L);
        patched.setName("Jane Doe");
        patched.setDepartment("IT");
        patched.setSalary(50000);

        when(employeeService.getById(1L)).thenReturn(Optional.of(employee));
        when(employeeService.save(any(EmployeeInfo.class))).thenReturn(patched);

        mockMvc.perform(patch("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void testPatchEmployee_NotFound() throws Exception {
        EmployeeInfo patchData = new EmployeeInfo();
        patchData.setName("Jane Doe");

        when(employeeService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/employees/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchData)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).delete(1L);

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).delete(1L);
    }
}
