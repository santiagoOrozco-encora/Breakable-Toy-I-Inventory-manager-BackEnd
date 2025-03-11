package com.StoreManageBackEnd.StoreManager.data.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class Product {
    private UUID id;
    private String name;
    private String category;
    private float unitPrice;
    private LocalDate expirationDate;
    private int stock;
    private LocalDate creationDate;
    private LocalDate updateDate;

    //Constructor
//    public Product(UUID id, String name, String category, float unitPrice, Date expirationDate, Integer stock) {
//        this.id = id;
//        this.name = name;
//        this.category = category;
//        this.unitPrice = unitPrice;
//        this.expirationDate = expirationDate;
//        this.stock = stock;
//        this.creationDate = new Date();
//        this.updateDate = new Date();
//    }
}
