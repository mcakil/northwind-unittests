package com.etiya.northwind.api.controllers;

import com.etiya.northwind.business.abstracts.CustomerService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.customerRequests.CreateCustomerRequest;
import com.etiya.northwind.business.requests.customerRequests.UpdateCustomerRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.customers.CustomerListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.dataAccess.abstracts.CustomerRepository;
import com.etiya.northwind.entities.concretes.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {
    private CustomerService customerService;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomersController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/getall")
    public DataResult<List<CustomerListResponse>> getAll(){
        return this.customerService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCustomerRequest createCustomerRequest){
        return this.customerService.add(createCustomerRequest);
    }

    @GetMapping("/get")
    public List<Customer> get(){
        return customerRepository.findAll();
    }



    @PostMapping("/update")
    public Result update(@RequestBody @Valid UpdateCustomerRequest updateCustomerRequest){
        return this.customerService.update(updateCustomerRequest);
    }

    @DeleteMapping("/delete/{customerId}")
    public Result delete(@Valid @PathVariable String customerId){
        return this.customerService.delete(customerId);
    }

    @GetMapping("/getbyid/{customerId}")
    public DataResult<CustomerListResponse> getById(@PathVariable String customerId){
        return this.customerService.getById(customerId);
    }

    @PostMapping("/getByPage")
    public DataResult<PageDataResponse<CustomerListResponse>> getByPage(@RequestBody @Valid  PageDataRequest pageDataRequest){
        return this.customerService.getByPage(pageDataRequest);
    }

    @PostMapping("/getByPageWithSorting")
    public DataResult<PageDataResponse<CustomerListResponse>> getByPageWithSorting(@RequestBody @Valid  PageSortRequest pageSortRequest){
        return this.customerService.getByPageWithSorting(pageSortRequest);
    }
}
