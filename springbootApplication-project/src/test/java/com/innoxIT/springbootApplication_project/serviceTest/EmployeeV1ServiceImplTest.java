package com.innoxIT.springbootApplication_project.serviceTest;

import com.innoxIT.springbootApplication_project.model.EmployeeV1Info;
import com.innoxIT.springbootApplication_project.repository.EmployeeV1Repository;
import com.innoxIT.springbootApplication_project.service.EmployeeV1ServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeV1ServiceImplTest {

    private EmployeeV1Repository mangoRepo;
    private EmployeeV1ServiceImpl employeeV1Service;

    @BeforeEach
    void setUp() {
        mangoRepo = mock(EmployeeV1Repository.class);
        employeeV1Service = new EmployeeV1ServiceImpl();
        employeeV1Service.mangoRepo = mangoRepo; // Manual injection
    }

    @Test
    void testSave() {
        EmployeeV1Info emp = new EmployeeV1Info();
        emp.setName("John");

        when(mangoRepo.save(emp)).thenReturn(emp);

        EmployeeV1Info result = employeeV1Service.save(emp);
        assertEquals("John", result.getName());
    }

    @Test
    void testGetAll() {
        when(mangoRepo.findAll()).thenReturn(List.of(new EmployeeV1Info(), new EmployeeV1Info()));

        List<EmployeeV1Info> result = employeeV1Service.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void testGetById() {
        EmployeeV1Info emp = new EmployeeV1Info();
        emp.setId("123");
        emp.setName("Jane");

        when(mangoRepo.findById("123")).thenReturn(Optional.of(emp));

        Optional<EmployeeV1Info> result = employeeV1Service.getById("123");
        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getName());
    }

    @Test
    void testDelete() {
        employeeV1Service.delete("123");
        verify(mangoRepo, times(1)).deleteById("123");
    }
}
