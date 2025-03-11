package com.StoreManageBackEnd.StoreManager.data.dao;

import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductDao {

    //Get products paginated
    List<Product> selectAllProducts(Integer page, Integer size,String name,String category,Integer stock);

    //Get total of products
    Integer countProducts(String name,String category,Integer stock);

    int deleteProductbyId(UUID id);

    int updateProductById();

    int insertProduct(Product newProduct);

    List<MetricsDTO> getMetrics();

}
