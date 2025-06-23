package ru.practicum.yandex.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name="title")
    String title;
    @Column(name="description")
    String description;
    @Column(name="price")
    double price;
    @Column(name="count")
    int count;
    @Column(name="img_path")
    String imgPath;

    @ManyToMany(mappedBy = "items")
    Set<Cart> carts;

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getId() {
        return id;
    }
}
