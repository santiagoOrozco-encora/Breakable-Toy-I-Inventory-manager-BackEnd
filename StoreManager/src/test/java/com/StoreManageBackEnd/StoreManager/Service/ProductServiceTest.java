package com.StoreManageBackEnd.StoreManager.Service;

import com.StoreManageBackEnd.StoreManager.data.dao.ProductDao;
import com.StoreManageBackEnd.StoreManager.data.model.Product;
import com.StoreManageBackEnd.StoreManager.data.model.ProductConverter;
import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import com.StoreManageBackEnd.StoreManager.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.support.PagedListHolder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @Mock
    private ProductConverter dtoConverter;

    @InjectMocks
    private ProductService productService;

    NewProductsDTO newDto;
    Product product;
    Product product1;
    Product product2;
    Product product3;
    Product product4;
    Product product5;
    List<Product> productsList;
    UUID idProduct;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        idProduct = UUID.randomUUID();
        newDto = new NewProductsDTO("Product 01","Category 01",10F,5,LocalDate.of(2025,3,10));
        product = new Product(idProduct,"Product 02","Category 02",10F,LocalDate.of(2025,3,10),5,LocalDate.now(),LocalDate.now());
        product = new Product(idProduct,"Product 02","Category 02",10F,LocalDate.of(2025,3,10),5,LocalDate.now(),LocalDate.now());
        product1 = new Product(UUID.randomUUID(), "Product Name 1", "Category1", 110.50F, LocalDate.of(2025, 10, 2), 10, LocalDate.now(), LocalDate.now());
        product2 = new Product(UUID.randomUUID(), "Product Name 2", "Category2", 115.50F, LocalDate.of(2025, 10, 5), 15, LocalDate.now(), LocalDate.now());
        product3 = new Product(UUID.randomUUID(), "Product Name 3", "Category1", 115.50F, LocalDate.of(2025, 10, 7), 15, LocalDate.now(), LocalDate.now());
        product4 = new Product(UUID.randomUUID(), "Product Name 4", "Category2", 115.50F, LocalDate.of(2025, 10, 12), 15, LocalDate.now(), LocalDate.now());
        product5 = new Product(UUID.randomUUID(), "Product Name 5", "Category3", 115.50F, LocalDate.of(2025, 10, 3), 15, LocalDate.now(), LocalDate.now());
        productsList = new ArrayList<>();
        productsList.add(product1);
        productsList.add(product2);
        productsList.add(product3);
        productsList.add(product4);
        productsList.add(product5);
    }

    @Test
    void testAddProduct(){
        //Set up
        when(productDao.insertProduct(any(Product.class))).thenReturn(1);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        //Test
        int result = productService.addProduct(newDto);

        //Assert
        verify(productDao,times(1)).insertProduct(productCaptor.capture());

        Product productAdded = productCaptor.getValue();
        assertEquals(1,result);

        assertEquals("Product 01",productAdded.getName());
        assertEquals("Category 01",productAdded.getCategory());
        assertEquals(10,productAdded.getUnitPrice());
        assertEquals(5,productAdded.getStock());
        assertEquals(LocalDate.of(2025,3,10),productAdded.getExpirationDate());
        assertEquals(LocalDate.now(),productAdded.getUpdateDate());
        assertEquals(LocalDate.now(),productAdded.getCreationDate());

    }

    @Test
    void testGetProductsNoFilters() {
        //Set up
        Integer page = 0;
        Integer size = 10;
        String[] categories = {};  // No categories
        String[] sort = {};  // No sorting
        boolean[] order = {};  // No order

        when(productDao.selectAllProducts(eq(page), eq(size), eq(null), eq(categories), eq(null), eq(sort), eq(order)))
                .thenReturn(productsList);

        //Test
        PagedListHolder<ProductsDTO> result = productService.getProducts(page, size, null, categories, null, sort, order);
        verify(productDao,times(1)).selectAllProducts(page,size,null,categories,null,sort,order);

        assertNotNull(result);
        assertEquals(5, result.getSource().size());

        ProductsDTO returnedProductDTO1 = result.getSource().getFirst();
        assertEquals("Product Name 1", returnedProductDTO1.getName());
        assertEquals("Category1", returnedProductDTO1.getCategory());
        assertEquals(10, returnedProductDTO1.getStock());
        assertEquals(110.50F,returnedProductDTO1.getUnitPrice());


        ProductsDTO returnedProductDTO2 = result.getSource().get(1);
        assertEquals("Product Name 2", returnedProductDTO2.getName());
        assertEquals("Category2", returnedProductDTO2.getCategory());
        assertEquals(15, returnedProductDTO2.getStock());
        assertEquals(115.5F,returnedProductDTO2.getUnitPrice());
    }

    @Test
    void testGetProductsAllFilters(){
        //Set up
        Integer page = 0;
        Integer size = 10;
        String[] categories = {"Category2"};  // No categories
        String[] sort = {};  // No sorting
        boolean[] order = {true};  // No order

        when(productDao.selectAllProducts(eq(page), eq(size), eq("Product"), eq(categories), eq(1), eq(sort), eq(order)))
                .thenReturn(List.of(product2,product4));

        //Test
        PagedListHolder<ProductsDTO> result = productService.getProducts(page, size, "Product", categories, 1, sort, order);
        verify(productDao,times(1)).selectAllProducts(page,size,"Product",categories,1,sort,order);

        assertNotNull(result);
        assertEquals(2, result.getSource().size());

        ProductsDTO returnedProductDTO1 = result.getSource().getFirst();
        assertEquals("Product Name 2", returnedProductDTO1.getName());
        assertEquals("Category2", returnedProductDTO1.getCategory());
        assertEquals(15, returnedProductDTO1.getStock());
        assertEquals(115.5F,returnedProductDTO1.getUnitPrice());

        ProductsDTO returnedProductDTO2 = result.getSource().get(1);
        assertEquals("Product Name 4", returnedProductDTO2.getName());
        assertEquals("Category2", returnedProductDTO2.getCategory());
        assertEquals(15, returnedProductDTO2.getStock());
        assertEquals(115.5F,returnedProductDTO2.getUnitPrice());
    }

    @Test
    void testUpdateProductById(){
        //Set up
        when(productDao.updateProductById(any(UUID.class),any(NewProductsDTO.class))).thenReturn(1);

        //Test
        int result = productService.updateProductById(idProduct,newDto);
        verify(productDao,times(1)).updateProductById(idProduct,newDto);

        //Assert
        assertEquals(1,result);
    }

    @Test
    void testResetStock(){
        //Set up
        when(productDao.resetStock(any(UUID.class),any(Boolean.class))).thenReturn(1);

        //Test
        int result = productService.resetStock(idProduct,true);

        //Assert
        assertEquals(1,result);
        verify(productDao,times(1)).resetStock(idProduct,true);
    }

    @Test
    void testDeleteProduct(){
        //Set up
        when(productDao.deleteProductById(any(UUID.class))).thenReturn(1);

        //Test
        int result = productService.deleteProduct(idProduct);

        //Assert
        assertEquals(1,result);
        verify(productDao,times(1)).deleteProductById(idProduct);
    }

    @Test
    void testGetMetrics(){
        //Set up
        MetricsDTO metrics1 = new MetricsDTO("category1",25D,32.4D,23.4D);
        MetricsDTO metrics2 = new MetricsDTO("category2",20D,34.4D,123.4D);

        when(productDao.getMetrics()).thenReturn(List.of(metrics1,metrics2));

        List<MetricsDTO> result = productService.getMetrics();

        //Assert
        assertNotNull(result);
        assertEquals(2,result.size());
        verify(productDao,times(1)).getMetrics();
        assertEquals("category1",result.getFirst().getCategory());
        assertEquals("category2",result.get(1).getCategory());

        assertEquals(25D,result.getFirst().getTotalInStock());
        assertEquals(20D,result.get(1).getTotalInStock());

        assertEquals(32.4D,result.getFirst().getValueInStock());
        assertEquals(34.4D,result.get(1).getValueInStock());

        assertEquals(23.4D,result.getFirst().getAverageInStock());
        assertEquals(123.4D,result.get(1).getAverageInStock());
    }
}
