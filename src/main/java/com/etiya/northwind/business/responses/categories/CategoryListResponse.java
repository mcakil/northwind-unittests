package com.etiya.northwind.business.responses.categories;

import com.etiya.northwind.entities.concretes.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListResponse {

    private int categoryId;

    private String categoryName;

    private String description;


}
