package ru.practicum.yandex.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItem {
    @Id
    Integer id;

    @Setter
    @Column("cart_id")
    Integer cartId;

    @Setter
    @Column("item_id")
    Integer itemId;

    @Column("count")
    Integer count;


    public CartItem(Integer cartId, Integer itemId) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.count = 0;
    }
}
