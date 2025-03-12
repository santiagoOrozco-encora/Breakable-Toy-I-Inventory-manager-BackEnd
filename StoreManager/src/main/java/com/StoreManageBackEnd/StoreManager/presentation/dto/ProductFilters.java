package com.StoreManageBackEnd.StoreManager.presentation.dto;

import lombok.Data;

@Data
public class ProductFilters {
    private String name;
    private String category;
    private Integer stock;
}
