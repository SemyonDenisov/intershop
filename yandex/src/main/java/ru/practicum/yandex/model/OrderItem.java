package ru.practicum.yandex.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name="order_item")
@NoArgsConstructor
public class OrderItem {
    @Id
    int id;

    @Getter
    @Column("order_id")
    int orderId;
    @Getter
    @Column("item_id")
    int itemId;

    @Getter
    @Column("quantity")
    int quantity;

    public OrderItem(int orderId, int itemId, int quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
