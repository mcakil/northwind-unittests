package com.etiya.northwind.business.concretes;

import com.etiya.northwind.TestSupport;
import com.etiya.northwind.business.requests.customerRequests.CreateCustomerRequest;
import com.etiya.northwind.business.requests.customerRequests.UpdateCustomerRequest;
import com.etiya.northwind.business.responses.customers.CustomerListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.mapping.ModelMapperManager;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.dataAccess.abstracts.CustomerRepository;
import com.etiya.northwind.entities.concretes.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomerManagerTest {

    ModelMapperService modelMapperService;
    CustomerRepository mockCustomerRepository;
    CustomerManager customerManager;


    @BeforeEach
    void setup() {

        modelMapperService = new ModelMapperManager(new ModelMapper());
        mockCustomerRepository = mock(CustomerRepository.class);
        customerManager = new CustomerManager(mockCustomerRepository, modelMapperService);
    }

    @Test
    void addTest_whenCustomerExists_dontAllowDuplicates() {

        CreateCustomerRequest customerRequest = CreateCustomerRequest.builder().customerId("TEST").build();

        when(mockCustomerRepository.existsById(customerRequest.getCustomerId())).thenReturn(true);

        Assertions.assertThrows(BusinessException.class, ()-> customerManager.add(customerRequest));

    }

    @Test
    void updateTest_whenCustomerDoesntExist_throwBusinessException(){
        Customer customer = Customer.builder().customerId("TEST").build();
        UpdateCustomerRequest updateCustomerRequest = this.modelMapperService.forRequest().map(customer, UpdateCustomerRequest.class);
        when(mockCustomerRepository.existsById(updateCustomerRequest.getCustomerId())).thenReturn(false);
        Assertions.assertThrows(BusinessException.class, ()-> customerManager.update(updateCustomerRequest));
    }

    @Test
    void updateTest_whenCustomerIsUpdated_assertDataIntegrity() {
        Customer customer = Customer.builder().customerId("TEST").build();
        when(mockCustomerRepository.findById(customer.getCustomerId())).thenReturn(Optional.of(customer));
        UpdateCustomerRequest updateRequest = this.modelMapperService.forRequest().map(customer, UpdateCustomerRequest.class);
        customerManager.update(updateRequest);

        CustomerListResponse expected = this.modelMapperService.forResponse().map(customer, CustomerListResponse.class);
        CustomerListResponse actual = customerManager.getById(customer.getCustomerId()).getData();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllUsersTest_itShouldReturnUserDtoList() {
        List<Customer> customerList = TestSupport.generateCustomers();
        when(mockCustomerRepository.findAll()).thenReturn(customerList);

        List<CustomerListResponse> customerResponseList =
            customerList.stream()
                    .map(c -> this.modelMapperService
                            .forResponse()
                            .map(c, CustomerListResponse.class))
                    .collect(Collectors.toList());
        DataResult<List<CustomerListResponse>> expected = new SuccessDataResult<>(customerResponseList);

        DataResult<List<CustomerListResponse>> actual = customerManager.getAll();

        Assertions.assertEquals(expected,actual);
        Mockito.verify(mockCustomerRepository).findAll();

    }

    @Test
    void getByIdTest_whenCustomerIdDoesntExist_shouldThrowBusinessException() {

        when(mockCustomerRepository.findById("ANTOM")).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, ()-> customerManager.getById("ANTOM"));
    }

    @Test
    void getByIdTest_whenCustomerIdExists_shouldReturnCustomerWithThatId() {
        Customer customer = Customer.builder().customerId("TEST").build();
        when(mockCustomerRepository.findById(customer.getCustomerId())).thenReturn(Optional.of(customer));
        CustomerListResponse customerListResponse = this.modelMapperService.forResponse().map(customer, CustomerListResponse.class);
        DataResult<CustomerListResponse> expected = new SuccessDataResult<>(customerListResponse);

        DataResult<CustomerListResponse> actual = customerManager.getById(customer.getCustomerId());

        Assertions.assertEquals(expected, actual);
    }
}