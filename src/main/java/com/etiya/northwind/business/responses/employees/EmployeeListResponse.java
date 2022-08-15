package com.etiya.northwind.business.responses.employees;

import com.etiya.northwind.entities.concretes.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeListResponse {

    private int employeeId;


    private String firstName;


    private String lastName;


    private String title;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + lastName.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final EmployeeListResponse other = (EmployeeListResponse) obj;

        return employeeId == other.employeeId;
    }

    @Override
    public String toString() {
        return String.valueOf(employeeId);
    }

}
