package com.StoreManageBackEnd.StoreManager.service;


import com.StoreManageBackEnd.StoreManager.data.dao.ProductDao;
import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.data.model.ProductConverter;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
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
    public PagedListHolder<ProductsDTO> getProducts(Integer page, Integer size, String name, String[] category, Integer stock, String[] sort, boolean[] order){
        List<Product> productsList = this.productDao.selectAllProducts( page,size,name,category,stock,sort,order);
        List<ProductsDTO> dtoList = productsList.stream().map(ProductConverter::convertToDTO).toList();
        MutableSortDefinition sorting =  new MutableSortDefinition("name",false,true);

        PagedListHolder<ProductsDTO> productPage = new PagedListHolder<>(dtoList, sorting);
        productPage.setPageSize(size);
        productPage.setPage(page);

        return  productPage;
    }

    //Update products using the ID
    public Integer updateProductById(UUID id,NewProductsDTO updatedProduct){
        return this.productDao.updateProductById(id,updatedProduct);
    }

    //Set default stock of the product
    public Integer resetStock(UUID id,boolean action){
        return this.productDao.resetStock(id,action);
    }

    //Delete a product by id service
    public int deleteProduct(UUID id){
        return productDao.deleteProductById(id);

    }

    //Get metrics of the categories
    public List<MetricsDTO> getMetrics(){
        return this.productDao.getMetrics();
    }
}
