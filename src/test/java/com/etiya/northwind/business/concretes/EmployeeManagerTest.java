package com.etiya.northwind.business.concretes;

import com.etiya.northwind.TestSupport;
import com.etiya.northwind.business.requests.employeeRequests.CreateEmployeeRequest;
import com.etiya.northwind.business.requests.employeeRequests.UpdateEmployeeRequest;
import com.etiya.northwind.business.responses.employees.EmployeeListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.mapping.ModelMapperManager;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.dataAccess.abstracts.EmployeeRepository;
import com.etiya.northwind.entities.concretes.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeManagerTest {

    ModelMapperService modelMapperService;
    EmployeeRepository mockEmployeeRepository;
    EmployeeManager employeeManager;


    @BeforeEach
    void setup() {

        modelMapperService = new ModelMapperManager(new ModelMapper());
        mockEmployeeRepository = mock(EmployeeRepository.class);
        employeeManager = new EmployeeManager(mockEmployeeRepository, modelMapperService);
    }

    @Test
    void addTest_whenEmployeeExists_dontAllowDuplicates() {

        CreateEmployeeRequest employeeRequest = new CreateEmployeeRequest(
                1,"TEST","TEST","TEST",2
        );

        when(mockEmployeeRepository.existsById(employeeRequest.getEmployeeId())).thenReturn(true);

        Assertions.assertThrows(BusinessException.class, ()-> employeeManager.add(employeeRequest));

    }

    @Test
    void updateTest_whenEmployeeDoesntExist_throwBusinessException(){
        Employee employee = TestSupport.generateEmployee();
        UpdateEmployeeRequest updateEmployeeRequest = this.modelMapperService.forRequest().map(employee, UpdateEmployeeRequest.class);
        when(mockEmployeeRepository.existsById(updateEmployeeRequest.getEmployeeId())).thenReturn(false);
        Assertions.assertThrows(BusinessException.class, ()-> employeeManager.update(updateEmployeeRequest));
    }

    @Test
    void updateTest_whenEmployeeIsUpdated_assertDataIntegrity() {
        Employee employee = TestSupport.generateEmployee();
        when(mockEmployeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
        UpdateEmployeeRequest updateRequest = this.modelMapperService.forRequest().map(employee, UpdateEmployeeRequest.class);
        employeeManager.update(updateRequest);

        EmployeeListResponse expected = this.modelMapperService.forResponse().map(employee, EmployeeListResponse.class);
        EmployeeListResponse actual = employeeManager.getById(employee.getEmployeeId()).getData();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllUsersTest_itShouldReturnUserDtoList() {
        List<Employee> employeeList = TestSupport.generateEmployees();
        when(mockEmployeeRepository.findAll()).thenReturn(employeeList);
        List<EmployeeListResponse> employeeResponseList =
                employeeList.stream()
                        .map(c -> this.modelMapperService
                                .forResponse()
                                .map(c, EmployeeListResponse.class))
                        .collect(Collectors.toList());
        DataResult<List<EmployeeListResponse>> expected = new SuccessDataResult<>(employeeResponseList);

        DataResult<List<EmployeeListResponse>> actual = employeeManager.getAll();

        Assertions.assertEquals(expected,actual);
        Mockito.verify(mockEmployeeRepository).findAll();

    }

    @Test
    void getByIdTest_whenEmployeeIdDoesntExist_shouldThrowBusinessException() {

        when(mockEmployeeRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, ()-> employeeManager.getById(1));
    }

    @Test
    void getByIdTest_whenEmployeeIdExists_shouldReturnEmployeeWithThatId() {
        Employee employee = TestSupport.generateEmployee();
        when(mockEmployeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));
        EmployeeListResponse employeeListResponse = this.modelMapperService.forResponse().map(employee, EmployeeListResponse.class);
        DataResult<EmployeeListResponse> expected = new SuccessDataResult<>(employeeListResponse);

        DataResult<EmployeeListResponse> actual = employeeManager.getById(employee.getEmployeeId());

        Assertions.assertEquals(expected, actual);
    }
}