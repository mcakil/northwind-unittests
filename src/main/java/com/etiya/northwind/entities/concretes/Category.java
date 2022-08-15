package com.etiya.northwind.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {


    @Id
    @Column(name="category_id")
    private int categoryId;

   @Column(name="category_name")
    private String categoryName;

   @Column(name = "description")
   private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
