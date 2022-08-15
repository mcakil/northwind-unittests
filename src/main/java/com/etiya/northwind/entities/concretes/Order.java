package com.etiya.northwind.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name="orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @Column(name="order_id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name="customer_id")
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="employee_id")
    @JsonIgnore
    private Employee employee;

    @Column(name="order_date")
    private LocalDate orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderDetails> orderDetails;
}
