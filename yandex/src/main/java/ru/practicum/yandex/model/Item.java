package ru.practicum.yandex.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Table(name="items")
@NoArgsConstructor
public class Item {
    @Getter
    @Setter
    @Id
    int id;
    @Setter
    @Getter
    @Column("title")
    String title;
    @Setter
    @Getter
    @Column("description")
    String description;
    @Setter
    @Getter
    @Column("price")
    double price;
    @Setter
    @Getter
    @Column("count")
    int count;
    @Setter
    @Getter
    @Column("img_path")
    String imgPath;

    @Transient
    Set<Cart> carts;

    public Item(String title, String description, double price, int count, String imgPath) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.count = count;
        this.imgPath = imgPath;
    }
}
