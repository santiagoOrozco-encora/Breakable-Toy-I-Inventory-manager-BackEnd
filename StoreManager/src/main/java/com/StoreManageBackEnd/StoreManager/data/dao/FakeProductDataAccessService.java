package com.StoreManageBackEnd.StoreManager.data.dao;

import com.StoreManageBackEnd.StoreManager.data.model.MetricsProduct;
import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FakeProductDataAccessService implements ProductDao{
     private static List<Product> DB = new ArrayList<>();

    @Override
    public List<Product> selectAllProducts(Integer page, Integer size, String name, String category, Integer stock) {
        int start = page * size;
        String finalName = (name == null) ? "" : name.toLowerCase();
        String finalCategory = (category == null) ? "" : category.toLowerCase();

        List<Product> productList = DB.stream()
                .filter(product -> (finalName.isEmpty() || (product.getName() != null && product.getName().toLowerCase().contains(finalName))) &&
                        (finalCategory.isEmpty() || (product.getCategory() != null && product.getCategory().toLowerCase().contains(finalCategory))))
                .filter(product -> (stock == null || stock == 3) || (stock == 1 && product.getStock() >= 1) || (stock == 2 && product.getStock() < 1))
                .toList();
        int end = Math.min((page+1)*size,productList.size());

        if(end<=productList.size()) {
            return productList.subList(start, end);
        }else{
            return  productList.subList(start,productList.size());
        }
    }

    @Override
    public Integer countProducts(String name, String category, Integer stock) {
        String finalName = (name == null) ? "" : name.toLowerCase();
        String finalCategory = (category == null) ? "" : category.toLowerCase();

        List<Product> productList = DB.stream()
                .filter(product -> (finalName.isEmpty() || (product.getName() != null && product.getName().toLowerCase().contains(finalName))) &&
                        (finalCategory.isEmpty() || (product.getCategory() != null && product.getCategory().toLowerCase().contains(finalCategory))))
                .filter(product -> (stock == null || stock == 3) || (stock == 1 && product.getStock() >= 1) || (stock == 2 && product.getStock() < 1))
                .toList();
        return productList.size();
    }


    @Override
    public int deleteProductbyId(UUID id) {
        return 0;
    }

    @Override
    public int updateProductById() {
        return 0;
    }

    @Override
    public int insertProduct(Product newProduct) {
        boolean isDuplicated = DB.stream().anyMatch(product -> product.getName().equalsIgnoreCase(newProduct.getName()));
        if(!isDuplicated){
            UUID id = UUID.randomUUID();
            newProduct.setId(id);
            DB.add(newProduct);
            return 1;
        }
        return 0;
    }

    @Override
    public List<MetricsDTO> getMetrics() {

        return DB.stream().collect(Collectors.groupingBy(Product::getCategory)).entrySet().stream().distinct().map(entry ->{
            MetricsProduct summary = calculateMetrics(entry.getValue());
            return new MetricsDTO(
                    entry.getKey(),
                    summary.getTotalInStock(),
                    summary.getValueInStock(),
                    summary.getAverageInStock()
            );
        }).collect(Collectors.toList());
    }

//    public List<MetricsDTO> getMetricsv2(){
//        return DB.stream().forEach(
//
//        );
//    }


    private MetricsProduct calculateMetrics(List<Product> products){
        double totalInStock = products.stream().mapToDouble(Product::getStock).sum();
        double valueInStock = products.stream().mapToDouble(product -> product.getUnitPrice() * product.getStock()).sum();
        double averageInStock = products.stream().mapToDouble(Product::getUnitPrice).average().orElse(0.0);

        return new MetricsProduct(totalInStock,valueInStock,averageInStock);
    }
}
