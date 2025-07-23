package ru.practicum.yandex.dto;

import ru.practicum.yandex.model.Item;

import java.util.List;

public class OrderWithItems {
    int id;
    List<Item> items;
    double totalSum;

    public OrderWithItems() {}
    public OrderWithItems(int id, List<Item> items, double totalSum) {
        this.id = id;
        this.items = items;
        this.totalSum = totalSum;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public List<Item> getItems() {
        return items;
    }
    public double getTotalSum() {
        return totalSum;
    }
}
