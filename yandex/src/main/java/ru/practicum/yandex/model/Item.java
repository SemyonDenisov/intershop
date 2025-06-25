package ru.practicum.yandex.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="items")
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
}
