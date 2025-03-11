package com.StoreManageBackEnd.StoreManager.data.model;

import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductConverter {

    public static  Product convertFromDTO(NewProductsDTO newProduct){
        Product product = new Product();

        product.setName(newProduct.getName());
        product.setCategory(newProduct.getCategory());
        product.setUnitPrice(newProduct.getUnitPrice());
        product.setStock(newProduct.getStock());
        product.setExpirationDate(newProduct.getExpirationDate());
        product.setCreationDate(LocalDate.now());
        product.setUpdateDate(LocalDate.now());

        return product;
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
