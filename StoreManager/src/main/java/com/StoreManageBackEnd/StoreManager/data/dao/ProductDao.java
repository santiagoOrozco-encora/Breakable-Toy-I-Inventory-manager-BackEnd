package com.StoreManageBackEnd.StoreManager.data.dao;

import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

public interface ProductDao {

    //Get products paginated
    Page<Product> selectAllProducts(Integer page, Integer size, String name, String[] category, Integer stock, String[] sort, boolean[] order);

    //Get total of products
    Integer countProducts(String name,String category,Integer stock);

    //Delete a product by id
    int deleteProductById(UUID id);

    //Update a product by id
    int updateProductById(UUID id, NewProductsDTO updatedProduct);

    //Reset stock of a product
    int resetStock(UUID id,boolean action);

    //Insert a new product
    int insertProduct(Product newProduct);

    //Get metrics of the products
    List<MetricsDTO> getMetrics();

}
