package com.StoreManageBackEnd.StoreManager.data.model;

import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProductConverter {

    public static  Product convertFromDTO(NewProductsDTO newProduct){
        UUID id = UUID.randomUUID();

        if(newProduct.getExpirationDate() != null){
            return new Product(id,newProduct.getName(), newProduct.getCategory(), newProduct.getUnitPrice(),newProduct.getExpirationDate(),newProduct.getStock(),LocalDate.now(),LocalDate.now());
        }else{
            return new Product(id,newProduct.getName(), newProduct.getCategory(), newProduct.getUnitPrice(),newProduct.getStock(),LocalDate.now(),LocalDate.now());
        }


    }

    public static ProductsDTO convertToDTO(Product product){
        ProductsDTO DtoProduct = new ProductsDTO();

        DtoProduct.setId(product.getId());
        DtoProduct.setName(product.getName());
        DtoProduct.setCategory(product.getCategory());
        DtoProduct.setUnitPrice(product.getUnitPrice());
        DtoProduct.setStock(product.getStock());
        DtoProduct.setExpirationDate(product.getExpirationDate());

        return  DtoProduct;
    }
}
