package com.StoreManageBackEnd.StoreManager.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetricsProduct {
    private double totalInStock;
    private double valueInStock;
    private double averageInStock;

}
