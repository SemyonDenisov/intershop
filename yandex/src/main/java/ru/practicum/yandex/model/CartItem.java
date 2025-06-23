package ru.practicum.yandex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int cartId;
    int itemId;
    int quantity;

    public CartItem(int cartId, int itemId, int quantity) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }
    public int getCartId() {
        return cartId;
    }
    public int getItemId() {
        return itemId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
