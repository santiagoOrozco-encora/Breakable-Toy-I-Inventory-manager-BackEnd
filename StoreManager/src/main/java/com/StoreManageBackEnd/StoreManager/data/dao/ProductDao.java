package com.StoreManageBackEnd.StoreManager.data.dao;

import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;

import java.util.List;
import java.util.UUID;

public interface ProductDao {

    //Get products paginated
    List<Product> selectAllProducts(Integer page, Integer size, String name, String[] category, Integer stock, String[] sort, boolean[] order);

    //Get total of products
    Integer countProducts(String name,String category,Integer stock);

    int deleteProductById(UUID id);

    int updateProductById(UUID id, NewProductsDTO updatedProduct);

    int resetStock(UUID id,boolean action);

    int insertProduct(Product newProduct);

    List<MetricsDTO> getMetrics();

}
