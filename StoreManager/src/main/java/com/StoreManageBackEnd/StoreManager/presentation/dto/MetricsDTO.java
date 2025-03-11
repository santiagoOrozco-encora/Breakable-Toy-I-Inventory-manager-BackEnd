package com.StoreManageBackEnd.StoreManager.presentation.dto;

public class MetricsDTO {
    private String category;
    private double totalInStock;
    private double valueInStock;

    public MetricsDTO(String category, double totalInStock, double valueInStock, double averageInStock) {
        this.category = category;
        this.totalInStock = totalInStock;
        this.valueInStock = valueInStock;
        this.averageInStock = averageInStock;
    }

    private  double averageInStock;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getTotalInStock() {
        return totalInStock;
    }

    public void setTotalInStock(Float totalInStock) {
        this.totalInStock = totalInStock;
    }

    public double getValueInStock() {
        return valueInStock;
    }

    public void setValueInStock(Float valueInStock) {
        this.valueInStock = valueInStock;
    }

    public double getAverageInStock() {
        return averageInStock;
    }

    public void setAverageInStock(Float averageInStock) {
        this.averageInStock = averageInStock;
    }
}
