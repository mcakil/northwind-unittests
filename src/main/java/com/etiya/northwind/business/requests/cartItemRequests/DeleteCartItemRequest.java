package com.etiya.northwind.business.requests.cartItemRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCartItemRequest {

    @NotNull
    private int cartItemId;
}
