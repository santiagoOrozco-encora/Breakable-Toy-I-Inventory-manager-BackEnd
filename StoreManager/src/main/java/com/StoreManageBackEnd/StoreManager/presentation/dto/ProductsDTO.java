package com.StoreManageBackEnd.StoreManager.presentation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProductsDTO {
    private UUID id;
    private String name;
    private String category;
    private float unitPrice;
    private LocalDate expirationDate;
    private int stock;


}
