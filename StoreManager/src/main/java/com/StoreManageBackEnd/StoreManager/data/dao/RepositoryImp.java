package com.StoreManageBackEnd.StoreManager.data.dao;

import com.StoreManageBackEnd.StoreManager.data.model.MetricsProduct;
import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RepositoryImp implements ProductDao{
     @Setter
     private static List<Product> DB = new ArrayList<>();

    @Override
    public List<Product> selectAllProducts(Integer page, Integer size, String name, String[] category, Integer stock, String[] sort, boolean[] order) {
        String finalName = (name == null) ? "" : name.toLowerCase();
        String[] finalCategory = (category == null) ? new String[]{} : Arrays.stream(category).map(String::toLowerCase).toArray(String[]::new);

        List<Product> productList = new ArrayList<>(DB.stream()
                .filter(product -> (finalName.isEmpty() || product.getName().toLowerCase().contains(finalName)) && (finalCategory.length == 0 || Arrays.stream(finalCategory).anyMatch(cat -> product.getCategory().toLowerCase().contains(cat))))
                .filter(product -> (stock == null || stock == 3) || (stock == 1 && product.getStock() >= 1) || (stock == 2 && product.getStock() < 1))
                .toList());

        if(sort != null && sort.length > 0) {
            Comparator<Product> comparator = null;

            for (int i=0; i<sort.length; i++ ) {
                Comparator<Product> comparatorField = getProductComparator(sort[i],order[i]);
                if (comparatorField != null) {
                    if(comparator == null){
                        comparator = comparatorField;
                    }else {
                        comparator = comparator.thenComparing(comparatorField);
                    }
                }
            }
            productList.sort(comparator);
        }

        return productList;
    }

    private static Comparator<Product> getProductComparator(String sort,boolean desc) {
        Comparator<Product> comparatorField = null;
        switch (sort) {
            case "name":
                comparatorField = Comparator.comparing(Product::getName, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "stock":
                comparatorField = Comparator.comparing(Product::getStock, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "expirationDate":
                comparatorField = Comparator.comparing(product -> product.getExpirationDate() != null ? product.getExpirationDate().toEpochDay() : Long.MAX_VALUE);
                break;
            case "unitPrice":
                comparatorField = Comparator.comparing(Product::getUnitPrice, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "category":
                comparatorField = Comparator.comparing(Product::getCategory, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            default:
                break;
        }
        if (desc && comparatorField != null){
         comparatorField = comparatorField.reversed();
        }
        return comparatorField;
    }

    @Override
    public Integer countProducts(String name, String category, Integer stock) {
        String finalName = (name == null) ? "" : name.toLowerCase();
        String finalCategory = (category == null) ? "" : category.toLowerCase();

        List<Product> productList = DB.stream()
                .filter(product -> (finalName.isEmpty() || product.getName().toLowerCase().contains(finalName)) && (finalCategory.isEmpty() || product.getCategory().toLowerCase().contains(finalCategory)))
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

    private MetricsProduct calculateMetrics(List<Product> products){
        double totalInStock = products.stream().mapToDouble(Product::getStock).sum();
        double valueInStock = products.stream().mapToDouble(product -> product.getUnitPrice() * product.getStock()).sum();
        double averageInStock = products.stream().mapToDouble(Product::getUnitPrice).average().orElse(0.0);

        return new MetricsProduct(totalInStock,valueInStock,averageInStock);
    }

}
