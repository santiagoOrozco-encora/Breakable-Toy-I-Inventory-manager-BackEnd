package com.StoreManageBackEnd.StoreManager.service;


import com.StoreManageBackEnd.StoreManager.data.dao.ProductDao;
import com.StoreManageBackEnd.StoreManager.data.model.BatchOperationResult;
import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.data.model.ProductConverter;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductDao productDao;
    private static final Log log = LogFactory.getLog(ProductService.class);

    @Autowired
    //Constructor
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    //Add a new product service
    @Transactional
    public int addProduct(NewProductsDTO newDto) {
    
    if (newDto == null) {
        log.warn("Attempted to add a null product");
        throw new IllegalArgumentException("Product data cannot be null");
    }

    try {   
        log.debug("Attempting to add product: {}");
        
        Product product = ProductConverter.convertFromDTO(newDto);
        
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        int result = productDao.insertProduct(product);
        
        if (result <= 0) {
            String errorMsg = "Failed to persist product in the database";
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        log.info("Successfully added product [ID: {}, Name: {}]");
        return result;
        
    } catch (IllegalArgumentException e) {  
        log.warn("Validation error adding product: {}");
        throw e; 
        
    } catch (Exception e) {
        String errorMsg = String.format("Unexpected error adding product: {}", e.getMessage());
        log.error(errorMsg);
        throw new RuntimeException(errorMsg, e);
    }
    }

    //Add multiple products service
    @Transactional
    public BatchOperationResult addProducts(NewProductsDTO[] newDtos){
        if (newDtos == null || newDtos.length == 0) {
            log.warn("Attempted to add empty or null batch of products");
            return new BatchOperationResult(0, 0, List.of("No products provided"));
        }

        int successCount = 0;
        int failureCount = 0;
        List<String> errorMessages = new ArrayList<>();

        for(NewProductsDTO dto : newDtos){
            try{
                Product product = ProductConverter.convertFromDTO(dto);

                // Insert the Product into the database
                int inserted = productDao.insertProduct(product);

            // If the insertion was successful, increment the counter
            if (inserted > 0) {
                successCount++;
            } else {
                failureCount++;
                errorMessages.add("Failed to insert product: " + dto);
            }
            }catch (Exception e){
            log.error("Error adding product: {}");
            throw new RuntimeException("Error adding product: " + e.getMessage());
            }
        }
        return new BatchOperationResult(successCount, failureCount, errorMessages);
    }

    //Get all products service
    public PagedListHolder<ProductsDTO> getProducts(
        int page, int size,
        String name, String[] category,
        int stock, String[] sort,
        boolean[] order){
            
            try{
            if(page < 0 || size < 0){
                throw new IllegalArgumentException("Page and size must be non-negative");
            }

            List<Product> productsList = this.productDao.selectAllProducts(
                page,size,name,
                category,stock,
                sort,order);
            
            List<ProductsDTO> dtoList = productsList.stream().map(ProductConverter::convertToDTO).toList();
            MutableSortDefinition sorting =  new MutableSortDefinition("name",false,true);

            PagedListHolder<ProductsDTO> productPage = new PagedListHolder<>(dtoList, sorting);
            productPage.setPageSize(size);
            productPage.setPage(page);

            return  productPage;
            }catch (Exception e){
                log.error("Error getting products: {}");
                throw new RuntimeException("Error getting products: " + e.getMessage());
            }
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
