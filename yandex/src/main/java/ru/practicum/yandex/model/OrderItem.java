package ru.practicum.yandex.model;

import jakarta.persistence.*;

@Entity
@Table(name="order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="order_id")
    int orderId;
    @Column(name="item_id")
    int itemId;

    @Column(name="quantity")
    int quantity;

    public int getQuantity() {
        return quantity;
    }
    public int getItemId() {
        return itemId;
    }

    public OrderItem() {}

    public OrderItem(int orderId, int itemId, int quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
