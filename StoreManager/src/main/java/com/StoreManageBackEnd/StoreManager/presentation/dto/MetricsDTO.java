package com.StoreManageBackEnd.StoreManager.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetricsDTO {
    private String category;
    private Double totalInStock;
    private Double valueInStock;
    private Double averageInStock;
}
