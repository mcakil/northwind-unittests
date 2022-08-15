package com.etiya.northwind.dataAccess.abstracts;

import com.etiya.northwind.entities.concretes.Category;
import com.etiya.northwind.entities.concretes.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query("select a from Employee a")
    Page<Employee> findAllEmployees(Pageable pageable);

    @Query("select e from Employee e where e.reportsTo.employeeId = :reportsTo")
    List<Employee> reportingEmployees(@Param("reportsTo") int reportsTo);
}
