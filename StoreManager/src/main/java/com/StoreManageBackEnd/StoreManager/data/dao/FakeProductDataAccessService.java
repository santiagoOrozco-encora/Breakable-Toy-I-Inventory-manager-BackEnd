package com.StoreManageBackEnd.StoreManager.data.dao;

import com.StoreManageBackEnd.StoreManager.data.model.MetricsProduct;
import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FakeProductDataAccessService implements ProductDao{
     private static List<Product> DB = new ArrayList<>();

    @Override
    public List<Product> selectAllProducts(Integer page, Integer size, String name, String category, Integer stock,String sort) {
        String finalName = (name == null) ? "" : name.toLowerCase();
        String finalCategory = (category == null) ? "" : category.toLowerCase();

        List<Product> productList = new ArrayList<>(DB.stream()
                .filter(product -> (finalName.isEmpty() || (product.getName() != null && product.getName().toLowerCase().contains(finalName))) &&
                        (finalCategory.isEmpty() || (product.getCategory() != null && product.getCategory().toLowerCase().contains(finalCategory))))
                .filter(product -> (stock == null || stock == 3) || (stock == 1 && product.getStock() >= 1) || (stock == 2 && product.getStock() < 1))
                .toList());

        Comparator<Product> comparator = null;
        switch (sort){
            case "name":
                comparator = Comparator.comparing(Product::getName,Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case  "stock":
                comparator = Comparator.comparing(Product::getStock,Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case  "expirationDate":
                comparator = Comparator.comparing( product -> product.getExpirationDate() != null ? product.getExpirationDate().toEpochDay() : Long.MAX_VALUE);
                break;
            case  "unitPrice":
                comparator = Comparator.comparing(Product::getUnitPrice,Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case  "category":
                comparator = Comparator.comparing(Product::getCategory,Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            default:
                break;
        }

        if(comparator != null){
            productList.sort(comparator);
        }

        return productList;
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
    public int deleteProductById(UUID id) {
        Optional<Product> oldProduct = DB.stream().filter(product -> product.getId().equals(id)).findFirst();

        if(oldProduct.isPresent()){
            DB.removeIf(product -> product.getId().equals(id));
            return 1;
        }
        return 0;
    }

    @Override
    public int updateProductById(UUID id, NewProductsDTO updatedProduct) {
        Optional<Product> oldProduct = DB.stream().filter(product -> product.getId().equals(id)).findFirst();

        if(oldProduct.isPresent()){
            Product product = oldProduct.get();
            product.setName(updatedProduct.getName());
            product.setCategory(updatedProduct.getCategory());
            product.setStock(updatedProduct.getStock());
            product.setUnitPrice(updatedProduct.getUnitPrice());
            product.setExpirationDate(updatedProduct.getExpirationDate());
            product.setUpdateDate(LocalDate.now());
            return 1;
        }
        return 0;
    }

    @Override
    public int resetStock(UUID id, boolean action) {
        Optional<Product> oldProduct = DB.stream().filter(product -> product.getId().equals(id)).findFirst();

        if(oldProduct.isPresent()){
            Product product = oldProduct.get();
            if(action){
            product.setStock(0);
            product.setUpdateDate(LocalDate.now());
            } else{
                product.setStock(10);
                product.setUpdateDate(LocalDate.now());
            }
            return 1;
        }
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
