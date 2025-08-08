package com.innoxIT.springbootApplication_project.serviceTest;

import com.innoxIT.springbootApplication_project.model.EmployeeInfo;
import com.innoxIT.springbootApplication_project.repository.EmployeeRepository;
import com.innoxIT.springbootApplication_project.service.EmployeeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private AutoCloseable closeable;

    private EmployeeInfo employee1;
    private EmployeeInfo employee2;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        employee1 = new EmployeeInfo(1L, "Alice", "Engineering", 50000);
        employee2 = new EmployeeInfo(2L, "Bob", "HR", 40000);
    }

    @Test
    void testSave() {
        when(employeeRepository.save(employee1)).thenReturn(employee1);
        EmployeeInfo saved = employeeService.save(employee1);
        assertEquals("Alice", saved.getName());
        verify(employeeRepository, times(1)).save(employee1);
    }

    @Test
    void testSaveAll() {
        List<EmployeeInfo> list = Arrays.asList(employee1, employee2);
        when(employeeRepository.saveAll(list)).thenReturn(list);
        List<EmployeeInfo> result = employeeService.saveAll(list);
        assertEquals(2, result.size());
        verify(employeeRepository).saveAll(list);
    }

    @Test
    void testGetAll() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));
        List<EmployeeInfo> all = employeeService.getAll();
        assertEquals(2, all.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        Optional<EmployeeInfo> found = employeeService.getById(1L);
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }

    @Test
    void testUpdateWhenFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(EmployeeInfo.class))).thenAnswer(i -> i.getArgument(0));

        EmployeeInfo updatedData = new EmployeeInfo();
        updatedData.setName("Alicia");
        updatedData.setDepartment("DevOps");
        updatedData.setSalary(60000);

        EmployeeInfo updated = employeeService.update(1L, updatedData);

        assertEquals("Alicia", updated.getName());
        assertEquals("DevOps", updated.getDepartment());
        assertEquals(60000.0, updated.getSalary());

        verify(employeeRepository).save(employee1);
    }

    @Test
    void testUpdateWhenNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        EmployeeInfo data = new EmployeeInfo();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.update(99L, data);
        });

        assertEquals("Employee not found with id: 99", exception.getMessage());
    }

    @Test
    void testDelete() {
        employeeService.delete(1L);
        verify(employeeRepository).deleteById(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
