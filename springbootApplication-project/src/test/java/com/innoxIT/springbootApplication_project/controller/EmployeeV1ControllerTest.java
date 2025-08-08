package com.innoxIT.springbootApplication_project.controller;

import com.innoxIT.springbootApplication_project.model.EmployeeV1Info;
import com.innoxIT.springbootApplication_project.service.EmployeeV1Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeV1ControllerTest {

    @Mock
    private EmployeeV1Service mangoService;

    @InjectMocks
    private EmployeeV1Controller controller;

    private EmployeeV1Info sampleEmployee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleEmployee = new EmployeeV1Info();
        sampleEmployee.setId("1");
        sampleEmployee.setName("Alice");
        sampleEmployee.setDepartment("IT");
        sampleEmployee.setSalary(70000.0);
    }

    @Test
    void testCreate() {
        when(mangoService.save(any(EmployeeV1Info.class))).thenReturn(sampleEmployee);

        ResponseEntity<EmployeeV1Info> response = controller.create(sampleEmployee);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleEmployee, response.getBody());
        verify(mangoService, times(1)).save(sampleEmployee);
    }

    @Test
    void testAddEmployees() {
        EmployeeV1Info emp1 = new EmployeeV1Info();
        emp1.setId("1");
        emp1.setName("Alice");
        emp1.setDepartment("IT");
        emp1.setSalary(70000.0);

        EmployeeV1Info emp2 = new EmployeeV1Info();
        emp2.setId("2");
        emp2.setName("Bob");
        emp2.setDepartment("HR");
        emp2.setSalary(60000.0);

        List<EmployeeV1Info> employees = Arrays.asList(emp1, emp2);

        // Mocking the saveAll method to return the same list
        when(mangoService.saveAll(employees)).thenReturn(employees);

        ResponseEntity<String> response = controller.addEmployees(employees);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("2 employees added successfully", response.getBody());

        verify(mangoService, times(1)).saveAll(employees);
    }


    @Test
    void testGetAll() {
        when(mangoService.getAll()).thenReturn(List.of(sampleEmployee));

        ResponseEntity<List<EmployeeV1Info>> response = controller.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Alice", response.getBody().get(0).getName());
    }

    @Test
    void testGetByIdFound() {
        when(mangoService.getById("1")).thenReturn(Optional.of(sampleEmployee));

        ResponseEntity<EmployeeV1Info> response = controller.getById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleEmployee, response.getBody());
    }

    @Test
    void testGetByIdNotFound() {
        when(mangoService.getById("999")).thenReturn(Optional.empty());

        ResponseEntity<EmployeeV1Info> response = controller.getById("999");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        when(mangoService.update(eq("1"), any(EmployeeV1Info.class))).thenReturn(sampleEmployee);

        ResponseEntity<EmployeeV1Info> response = controller.update("1", sampleEmployee);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleEmployee, response.getBody());
    }

    @Test
    void testPatchFound() {
        EmployeeV1Info patchData = new EmployeeV1Info();
        patchData.setName("Bob");
        patchData.setSalary(85000.0);

        when(mangoService.getById("1")).thenReturn(Optional.of(sampleEmployee));
        when(mangoService.save(any(EmployeeV1Info.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<EmployeeV1Info> response = controller.patch("1", patchData);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Bob", response.getBody().getName());
        assertEquals(85000.0, response.getBody().getSalary());
        assertEquals("IT", response.getBody().getDepartment());  // unchanged
    }

    @Test
    void testPatchNotFound() {
        when(mangoService.getById("999")).thenReturn(Optional.empty());

        ResponseEntity<EmployeeV1Info> response = controller.patch("999", new EmployeeV1Info());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDelete() {
        doNothing().when(mangoService).delete("1");

        ResponseEntity<Void> response = controller.delete("1");

        assertEquals(204, response.getStatusCodeValue());
        verify(mangoService, times(1)).delete("1");
    }
}
