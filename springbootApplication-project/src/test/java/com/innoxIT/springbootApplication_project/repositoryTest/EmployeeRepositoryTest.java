package com.innoxIT.springbootApplication_project.repositoryTest;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeRepositoryTest {

    private EmployeeRepository employeeRepository;

    private EmployeeInfo sampleEmployee;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class); // Manual mock
        MockitoAnnotations.openMocks(this);

        sampleEmployee = new EmployeeInfo();
        sampleEmployee.setId(1L);
        sampleEmployee.setName("Alice");
        sampleEmployee.setDepartment("HR");
        sampleEmployee.setSalary(60000);
    }

    @Test
    void testSaveEmployee() {
        when(employeeRepository.save(any(EmployeeInfo.class))).thenReturn(sampleEmployee);

        EmployeeInfo saved = employeeRepository.save(sampleEmployee);

        assertNotNull(saved);
        assertEquals("Alice", saved.getName());
        assertEquals("HR", saved.getDepartment());
        assertEquals(60000, saved.getSalary());

        verify(employeeRepository, times(1)).save(sampleEmployee);
    }

    @Test
    void testFindById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));

        Optional<EmployeeInfo> result = employeeRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
        assertEquals("HR", result.get().getDepartment());
        assertEquals(60000, result.get().getSalary());

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        when(employeeRepository.findAll()).thenReturn(List.of(sampleEmployee));

        List<EmployeeInfo> list = employeeRepository.findAll();

        assertEquals(1, list.size());
        assertEquals("Alice", list.getFirst().getName());
        assertEquals("HR", list.getFirst().getDepartment());
        assertEquals(60000, list.getFirst().getSalary());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testDeleteById() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeRepository.deleteById(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExistsById() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        boolean exists = employeeRepository.existsById(1L);

        assertTrue(exists);
        verify(employeeRepository, times(1)).existsById(1L);
    }
}
