package com.StoreManageBackEnd.StoreManager.data.model;

import static org.junit.jupiter.api.Assertions.*;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

public class ProductConverterTest {

    @Test
    void testConvertFromDTO(){

        //Sample preparation
        NewProductsDTO expectedSample = new NewProductsDTO();
        expectedSample.setName("Laptop");
        expectedSample.setCategory("Electronics");
        expectedSample.setStock(5);
        expectedSample.setUnitPrice(2599f);
        expectedSample.setExpirationDate(LocalDate.of(2025,12,12));

        //Act test
        Product product = ProductConverter.convertFromDTO(expectedSample);

        //Assertions
        assertNotNull(product, "The product should not be null.");
        assertEquals("Laptop",product.getName());
        assertEquals("Electronics",product.getCategory());
        assertEquals(5,product.getStock());
        assertEquals(2599,product.getUnitPrice());
        assertEquals(LocalDate.of(2025,12,12),product.getExpirationDate());
        assertEquals(LocalDate.now(),product.getCreationDate());
        assertEquals(LocalDate.now(),product.getUpdateDate());


    }

    @Test
    void testConvertToDTO(){
        //Sample preparation
        Product expectedSample = new Product();
        expectedSample.setName("Laptop");
        expectedSample.setCategory("Electronics");
        expectedSample.setStock(10);
        expectedSample.setUnitPrice(2400F);
        expectedSample.setExpirationDate(LocalDate.now());
        expectedSample.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        //Act Test
        ProductsDTO DtoProduct = ProductConverter.convertToDTO(expectedSample);

        //Assertions
        assertNotNull(DtoProduct,"DTO should not be null");
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),DtoProduct.getId());
        assertEquals("Laptop",DtoProduct.getName());
        assertEquals("Electronics",DtoProduct.getCategory());
        assertEquals(2400,DtoProduct.getUnitPrice());
        assertEquals(LocalDate.now(),DtoProduct.getExpirationDate());
        assertEquals(10,DtoProduct.getStock());
    }
}
