package com.StoreManageBackEnd.StoreManager.presentation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewProductsDTO {
    private String name;
    private String category;
    private float unitPrice;
    private int stock;
    private LocalDate expirationDate;

}
