package com.rooster.framework.core.model;

import java.time.LocalDateTime;

public class Order {

    private String name;

    private String type;

    private LocalDateTime orderAt;

    Order() {}

    public Order(String name, String type, LocalDateTime orderAt) {
        this.name = name;
        this.type = type;
        this.orderAt = orderAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(LocalDateTime orderAt) {
        this.orderAt = orderAt;
    }
}
