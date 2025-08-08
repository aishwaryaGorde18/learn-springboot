package com.innoxIT.springbootApplication_project.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innoxIT.springbootApplication_project.controller.EmployeeV1Controller;
import com.innoxIT.springbootApplication_project.model.EmployeeV1Info;
import com.innoxIT.springbootApplication_project.service.EmployeeV1Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeV1Controller.class)
public class EmployeeV1ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeV1Service mangoService;

    private EmployeeV1Info employee;

    @BeforeEach
    public void setup() {
        employee = new EmployeeV1Info();
        employee.setId("123");
        employee.setName("John");
        employee.setDepartment("IT");
        employee.setSalary(50000.0);
    }

    @Test
    public void testCreate() throws Exception {
        Mockito.when(mangoService.save(any(EmployeeV1Info.class))).thenReturn(employee);

        mockMvc.perform(post("/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void testAddEmployees() throws Exception {
        Mockito.when(mangoService.saveAll(any())).thenReturn(Arrays.asList(employee));

        mockMvc.perform(post("/v1/employees/addEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(Arrays.asList(employee))))
                .andExpect(status().isOk())
                .andExpect(content().string("1 employees added successfully"));
    }

    @Test
    public void testGetAll() throws Exception {
        Mockito.when(mangoService.getAll()).thenReturn(Arrays.asList(employee));

        mockMvc.perform(get("/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    public void testGetById() throws Exception {
        Mockito.when(mangoService.getById("123")).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/v1/employees/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void testUpdate() throws Exception {
        Mockito.when(mangoService.update(Mockito.eq("123"), any(EmployeeV1Info.class))).thenReturn(employee);

        mockMvc.perform(put("/v1/employees/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void testPatch() throws Exception {
        Mockito.when(mangoService.getById("123")).thenReturn(Optional.of(employee));
        Mockito.when(mangoService.save(any(EmployeeV1Info.class))).thenReturn(employee);

        mockMvc.perform(patch("/v1/employees/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(mangoService).delete("123");

        mockMvc.perform(delete("/v1/employees/123"))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
