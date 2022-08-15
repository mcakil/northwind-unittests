package com.etiya.northwind.dataAccess.abstracts;

import com.etiya.northwind.entities.concretes.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("select c from Cart c where c.customer.customerId =:customerId ")
    public Cart getByCustomerId(@Param("customerId") String customerId);

}
