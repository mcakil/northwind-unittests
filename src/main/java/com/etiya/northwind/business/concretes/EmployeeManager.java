package com.etiya.northwind.business.concretes;

import com.etiya.northwind.business.abstracts.EmployeeService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.employeeRequests.CreateEmployeeRequest;
import com.etiya.northwind.business.requests.employeeRequests.UpdateEmployeeRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.employees.EmployeeListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.helpers.PageableSorter;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.core.results.SuccessResult;
import com.etiya.northwind.dataAccess.abstracts.EmployeeRepository;
import com.etiya.northwind.entities.concretes.Customer;
import com.etiya.northwind.entities.concretes.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeManager implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private ModelMapperService modelMapperService;

    @Autowired
    public EmployeeManager(EmployeeRepository employeeRepository, ModelMapperService modelMapperService) {
        this.employeeRepository = employeeRepository;
        this.modelMapperService = modelMapperService;
    }


    @Override
    public Result add(CreateEmployeeRequest createEmployeeRequest) {
        isEmployeeExist(createEmployeeRequest.getEmployeeId());
        checkIfReportsToExceeds(createEmployeeRequest.getReportsTo());
        Employee employee = this.modelMapperService.forRequest().map(createEmployeeRequest, Employee.class);
        employee.setReportsTo(employeeRepository.findById(createEmployeeRequest.getReportsTo()).orElseThrow(()-> new BusinessException("No employee found to report to.")));
        employeeRepository.save(employee);
        return new SuccessResult("Added");
    }

    @Override
    public Result update(UpdateEmployeeRequest updateEmployeeRequest) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(updateEmployeeRequest.getEmployeeId());
        optionalEmployee.ifPresentOrElse(employee ->
                {
                    employee = this.modelMapperService
                            .forRequest()
                            .map(updateEmployeeRequest, Employee.class);
                    employeeRepository.save(employee);
                },
                () -> {
                    throw new BusinessException("employee not found");
                });


        return new SuccessResult("Updated");
    }

    @Override
    public Result delete(int employeeId) {
        checkEmployeeExists(employeeId);
        this.employeeRepository.deleteById(employeeId);
        return new SuccessResult("Deleted successfully.");
    }

    @Override
    public DataResult<List<EmployeeListResponse>> getAll() {
        List<Employee> result = this.employeeRepository.findAll();
        List<EmployeeListResponse> response = result.stream()
                .map(employee -> this.modelMapperService.forResponse()
                        .map(employee, EmployeeListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult<EmployeeListResponse> getById(int employeeId) {
        Employee employee = this.employeeRepository.findById(employeeId).orElseThrow(() -> new BusinessException("Employee not found."));
        EmployeeListResponse response = this.modelMapperService.forResponse().map(employee, EmployeeListResponse.class);

        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult<PageDataResponse<EmployeeListResponse>> getByPage(PageDataRequest pageDataRequest) {
        Pageable pageable = PageRequest.of(pageDataRequest.getNumber() -1, pageDataRequest.getItemAmount());
        return new SuccessDataResult<>(getPageDataResponse(pageDataRequest.getNumber(), pageable));
    }

    @Override
    public DataResult<PageDataResponse<EmployeeListResponse>> getByPageWithSorting(PageSortRequest pageSortRequest) {
        return new SuccessDataResult<>(getPageDataResponse(pageSortRequest.getNumber(), PageableSorter.getSortedPageable(pageSortRequest)));
    }

    private PageDataResponse<EmployeeListResponse> getPageDataResponse(int pageNumber, Pageable pageable) {
        Page<Employee> pages = this.employeeRepository.findAllEmployees(pageable);
        List<EmployeeListResponse> response =
                pages.getContent().stream().map(employee -> this.modelMapperService.forResponse().map(employee, EmployeeListResponse.class)).collect(Collectors.toList());

        return new PageDataResponse<>(response, pages.getTotalPages(), pages.getTotalElements(), pageNumber);
    }

    private void checkEmployeeExists(int employeeId) {
        if (!employeeRepository.existsById(employeeId)){
            throw new BusinessException("Employee does not exist.");
        }
    }

    private void isEmployeeExist(int employeeId) {
        if (employeeRepository.existsById(employeeId)){
            throw new BusinessException("Employee is already exist.");
        }
    }

    private void checkIfReportsToExceeds(int reportsTo) {
        if (this.employeeRepository.reportingEmployees(reportsTo).size() >= 10) {
            throw new BusinessException("A manager can only manage up to 10 employees.");
        }
    }
    
}
