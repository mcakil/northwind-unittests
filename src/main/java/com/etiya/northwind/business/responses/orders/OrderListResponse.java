package com.etiya.northwind.business.responses.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderListResponse {

    private int orderId;
    private String contactName;
    private String employeeName;
    private LocalDate orderDate;
}
