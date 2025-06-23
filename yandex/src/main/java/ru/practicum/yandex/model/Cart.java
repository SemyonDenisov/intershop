package ru.practicum.yandex.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToMany
    @JoinTable(
            name = "cart_items", // имя join-таблицы
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    Set<Item> items;

    public Cart() {
        id=1;
        items = new HashSet<>();
    }

    public int getId() {
        return id;
    }
    public Set<Item> getItems() {
        return items;
    }
    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
