package com.etiya.northwind.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="cart_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    public CartItem(CartItem cartItem) {
        setCartItemId(cartItem.getCartItemId());
        setCart(cartItem.getCart());
        setProduct(cartItem.getProduct());
        setQuantity(cartItem.getQuantity());
        setUnitPrice(cartItem.getUnitPrice());
    }

    @Id
    @Column(name="cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

    @ManyToOne
    @JoinColumn(name="cart_id")
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="product_id")
    @JsonIgnore
    private Product product;

    @Column(name="quantity")
    private int quantity;

    @Column(name="unit_price")
    private double unitPrice;
}
