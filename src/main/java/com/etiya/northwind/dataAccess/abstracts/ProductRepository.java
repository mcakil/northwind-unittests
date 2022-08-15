package com.etiya.northwind.dataAccess.abstracts;

import com.etiya.northwind.entities.concretes.Category;
import com.etiya.northwind.entities.concretes.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select a from Product a")
    Page<Product> findAllProducts(Pageable pageable);

    @Query("select p from Product p where p.category.categoryId = :categoryId")
    List<Product> getCategoryProducts(@Param("categoryId") int categoryId);

    @Query("select case when count(p) > 0 then true else false end from Product p where lower(p.productName) like lower(:productName)")
    boolean isProductNameExists(@Param("productName") String productName);

}
