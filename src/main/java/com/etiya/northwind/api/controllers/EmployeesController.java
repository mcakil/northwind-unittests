package com.etiya.northwind.api.controllers;

import com.etiya.northwind.business.abstracts.EmployeeService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.employeeRequests.CreateEmployeeRequest;
import com.etiya.northwind.business.requests.employeeRequests.UpdateEmployeeRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.employees.EmployeeListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {
    private EmployeeService employeeService;

    @Autowired
    public EmployeesController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/getall")
    public DataResult<List<EmployeeListResponse>> getAll(){
        return this.employeeService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateEmployeeRequest createEmployeeRequest){
        return this.employeeService.add(createEmployeeRequest);
    }

    @PostMapping("/update")
    public Result update(@RequestBody @Valid UpdateEmployeeRequest updateEmployeeRequest){
        return this.employeeService.update(updateEmployeeRequest);
    }

    @DeleteMapping("/delete/{employeeId}")
    public Result delete(@Valid @PathVariable int employeeId){
        return this.employeeService.delete(employeeId);
    }

    @GetMapping("/getbyid/{employeeId}")
    public DataResult<EmployeeListResponse> getById(@PathVariable int employeeId){
        return this.employeeService.getById(employeeId);
    }

    @PostMapping("/getByPage")
    public DataResult<PageDataResponse<EmployeeListResponse>> getByPage(@RequestBody @Valid PageDataRequest pageDataRequest){
        return this.employeeService.getByPage(pageDataRequest);
    }

    @PostMapping("/getByPageWithSorting")
    public DataResult<PageDataResponse<EmployeeListResponse>> getByPageWithSorting(@RequestBody @Valid PageSortRequest pageSortRequest){
        return this.employeeService.getByPageWithSorting(pageSortRequest);
    }
}
