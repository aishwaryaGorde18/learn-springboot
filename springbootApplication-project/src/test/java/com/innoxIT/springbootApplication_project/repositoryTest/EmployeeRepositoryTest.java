package com.innoxIT.springbootApplication_project.repositoryTest;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeRepositoryMockTest {

    @Mock
    private EmployeeRepository employeeRepository;

    EmployeeRepositoryMockTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        EmployeeInfo employee = new EmployeeInfo(1L, "John Doe", "Developer", 50000);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<EmployeeInfo> result = employeeRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<EmployeeInfo> employees = Arrays.asList(
                new EmployeeInfo(1L, "John Doe", "Developer", 50000),
                new EmployeeInfo(2L, "Jane Smith", "Tester", 45000)
        );
        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeInfo> result = employeeRepository.findAll();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }
}
