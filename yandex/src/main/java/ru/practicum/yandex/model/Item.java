package ru.practicum.yandex.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="items")
@NoArgsConstructor
public class Item {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Setter
    @Getter
    @Column(name="title")
    String title;
    @Setter
    @Getter
    @Column(name="description")
    String description;
    @Setter
    @Getter
    @Column(name="price")
    double price;
    @Setter
    @Getter
    @Column(name="count")
    int count;
    @Setter
    @Getter
    @Column(name="img_path")
    String imgPath;

    @ManyToMany(mappedBy = "items")
    Set<Cart> carts;

    public Item(String title, String description, double price, int count, String imgPath) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.count = count;
        this.imgPath = imgPath;
    }
}
