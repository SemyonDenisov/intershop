package ru.practicum.yandex.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name="cart_item")
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    Integer id;

    @Setter
    @Column("cart_id")
    Integer cartId;

    @Setter
    @Column("item_id")
    Integer itemId;



    public CartItem(Integer cartId, Integer itemId) {
        this.cartId = cartId;
        this.itemId = itemId;
    }
}
