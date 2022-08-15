package com.etiya.northwind.business.concretes;

import com.etiya.northwind.business.abstracts.CustomerService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.customerRequests.CreateCustomerRequest;
import com.etiya.northwind.business.requests.customerRequests.UpdateCustomerRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.customers.CustomerListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.mapping.ModelMapperManager;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.dataAccess.abstracts.CustomerRepository;
import com.etiya.northwind.entities.concretes.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomerManagerTest {

    ModelMapperService modelMapperService;
    CustomerRepository mockCustomerRepository;
    CustomerManager customerManager;
    List<Customer> customers;

    @BeforeEach
    void setup() {
        //modelmapper sıkıntı çıkarırsa mockla
        modelMapperService = new ModelMapperManager(new ModelMapper());
        mockCustomerRepository = mock(CustomerRepository.class);
        customerManager = new CustomerManager(mockCustomerRepository, modelMapperService);
        customers = new ArrayList<Customer>();
    }

    @Test
    void addTest_whenCustomerExists_dontAllowDuplicates() {

        CreateCustomerRequest customerRequest = new CreateCustomerRequest(
            "ANTOM", "Etiya", "Test", "Secretary"
        );

        when(mockCustomerRepository.existsById(customerRequest.getCustomerId())).thenReturn(true);

        Assertions.assertThrows(BusinessException.class, ()-> customerManager.add(customerRequest));

    }

    @Test
    void updateTest_whenCustomerDoesntExist_throwBusinessException(){
        Customer customer = new Customer("TEST1","Test","Test","Test",null,null);
        UpdateCustomerRequest updateCustomerRequest = this.modelMapperService.forRequest().map(customer, UpdateCustomerRequest.class);
        when(mockCustomerRepository.findById("TEST1")).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, ()-> customerManager.update(updateCustomerRequest));
    }

    @Test
    void updateTest_whenCustomerIsUpdated_assertDataIntegrity() {
        Customer customer = new Customer("TEST1","Test","Test","Test",null,null);
        when(mockCustomerRepository.findById(customer.getCustomerId())).thenReturn(Optional.of(customer));
        UpdateCustomerRequest updateRequest = this.modelMapperService.forRequest().map(customer, UpdateCustomerRequest.class);
        customerManager.update(updateRequest);

        CustomerListResponse expected = this.modelMapperService.forResponse().map(customer, CustomerListResponse.class);
        CustomerListResponse actual = customerManager.getById(customer.getCustomerId()).getData();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getByIdTest_whenCustomerIdDoesntExist_shouldThrowBusinessException() {

        when(mockCustomerRepository.findById("ANTOM")).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, ()-> customerManager.getById("ANTOM"));
    }

    @Test
    void getByIdTest_whenCustomerIdExists_shouldReturnCustomerWithThatId() {
        Customer customer = new Customer("TEST1","Test","Test","Test",null,null);
        when(mockCustomerRepository.findById(customer.getCustomerId())).thenReturn(Optional.of(customer));
        CustomerListResponse customerListResponse = this.modelMapperService.forResponse().map(customer, CustomerListResponse.class);
        DataResult<CustomerListResponse> expected = new SuccessDataResult<>(customerListResponse);

        DataResult<CustomerListResponse> actual = customerManager.getById(customer.getCustomerId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByPage() {
    }

    @Test
    void getByPageWithSorting() {
    }

    @Test
    void findById() {
    }
}