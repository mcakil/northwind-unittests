package com.etiya.northwind.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDataRequest {
    @NotNull
    @Positive
    int number;
    @NotNull
    @Positive
    int itemAmount;
}
