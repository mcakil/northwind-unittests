package com.etiya.northwind.business.requests.productRequests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    @NotNull
    private int productId;

    @NotNull
    private String productName;

    private double unitPrice;

    private int unitsInStock;

    private int categoryId;

    @NotNull
    private int discontinued;
}
