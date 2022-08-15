package com.etiya.northwind.business.requests.categoryRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {

    @NotNull
    private int categoryId;

    @NotNull
    private String categoryName;

    private String description;
}
