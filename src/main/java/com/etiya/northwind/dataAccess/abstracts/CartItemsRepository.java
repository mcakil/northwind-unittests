package com.etiya.northwind.dataAccess.abstracts;

import com.etiya.northwind.entities.concretes.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItem, Integer> {

    @Query("select c from CartItem c where c.cart.cartId =:cartId")
    public List<CartItem> getCartItemByCartId(@Param("cartId") int cartId);

    @Query("select c.cartItemId from CartItem c where c.cart.cartId =:cartId")
    public List<Integer> getCartItemIdByCartId(@Param("cartId") int cartId);


}
