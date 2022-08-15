package com.etiya.northwind.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name="order_details")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderDetailsId.class)
@Builder
public class OrderDetails{

    @Id
    @Column(name="order_id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @JsonIgnore
    private Order order;

    @Id
    @Column(name="product_id")
    private int productId;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(name="unit_price")
    private double unitPrice;

    @Column(name="quantity")
    private int quantity;

    @Column(name="discount")
    private double discount;
}
