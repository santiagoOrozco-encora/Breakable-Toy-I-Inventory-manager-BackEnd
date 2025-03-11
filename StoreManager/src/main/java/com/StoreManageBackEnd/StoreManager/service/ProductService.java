package com.StoreManageBackEnd.StoreManager.service;


import com.StoreManageBackEnd.StoreManager.data.dao.ProductDao;
import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.data.model.ProductConverter;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductDao productDao;

    @Autowired
    //Constructor
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    //Add a new product service
    public int addProduct(NewProductsDTO newDto){
        Product product = ProductConverter.convertFromDTO(newDto);
        return productDao.insertProduct(product);
    }

    //Get all products service
    public Page<ProductsDTO> getProducts(Integer page, Integer size,String name,String category,Integer stock){
        Pageable pageable = PageRequest.of(page, size);

        List<Product> productsList = this.productDao.selectAllProducts( page,size,name,category,stock);
        List<ProductsDTO> dtoList = productsList.stream().map(ProductConverter::convertToDTO).toList();
        long total = this.productDao.countProducts(name,category,stock);

        return  new PageImpl<>(dtoList,pageable,total);
    }

    //Delete a product by id service
    public int deleteProduct(UUID id){
        return productDao.deleteProductbyId(id);

    }

    //Get metrics of the categories
    public List<MetricsDTO> getMetrics(){
        return this.productDao.getMetrics();
    }
}
