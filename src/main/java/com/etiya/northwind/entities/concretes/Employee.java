package com.etiya.northwind.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @Column(name = "employee_id")
    private int employeeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "title")
    private String title;
    @ManyToOne
    @JoinColumn(name = "reports_to")
    private Employee reportsTo;

    @OneToMany(mappedBy = "reportsTo")
    private List<Employee> reportingEmployees;

    @OneToMany(mappedBy = "employee")
    private List<Order> orders;

}
