package com.StoreManageBackEnd.StoreManager.data.dao;

import com.StoreManageBackEnd.StoreManager.data.model.Product;
import static org.junit.jupiter.api.Assertions.*;

import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositoryImpTest {

    @InjectMocks
    private RepositoryImp repository;

    private static List<Product> DB;
    UUID id01;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        DB = new ArrayList<>();
        id01 = UUID.randomUUID();
        Product product01 = new Product(id01,"Product01","Category01",10.5F, LocalDate.of(2025,3,12),5,LocalDate.now(),LocalDate.now());
        Product product02 = new Product(UUID.randomUUID(),"Product02","Category02",20.5F, LocalDate.of(2025,3,10),0,LocalDate.now(),LocalDate.now());
        Product product03 = new Product(UUID.randomUUID(),"Product03","Category01",30.5F, LocalDate.of(2025,3,5),50,LocalDate.now(),LocalDate.now());
        Product product04 = new Product(UUID.randomUUID(),"Product04","Category02",40.5F, LocalDate.of(2025,3,1),10,LocalDate.now(),LocalDate.now());
        Product product05 = new Product(UUID.randomUUID(),"Product05","Category03",50.5F, LocalDate.of(2025,3,20),2,LocalDate.now(),LocalDate.now());
        DB.add(product01);
        DB.add(product02);
        DB.add(product03);
        DB.add(product04);
        DB.add(product05);


        RepositoryImp.setDB(DB);
        repository = new RepositoryImp();
    }

    //Get all products test
    @Test
    void testSelectAllProductsByName(){
        //Test
        Page<Product> result = repository.selectAllProducts(0,10,"Product01",null,null,null,null);

        //Assert
        assertNotNull(result);
        assertEquals(1,result.getTotalElements());
        assertEquals("Product01",result.getContent().get(0).getName());
        assertEquals("Category01",result.getContent().get(0).getCategory());
    }
    @Test
    void testSelectAllProductsByCategory(){
        //Set up
        String[] categories = {"Category02","Category03"};
        //Test
        Page<Product> result = repository.selectAllProducts(0,10,null,categories,null,null,null);

        //Assert
        assertNotNull(result);
        assertEquals(3,result.getTotalElements());
        List<Product> content = result.getContent();
        assertEquals(3, content.size());
        assertEquals("Category02", content.get(0).getCategory());
        assertEquals("Category02", content.get(1).getCategory());
        assertEquals("Category03", content.get(2).getCategory());
    }
    @Test
    void testSelectAllProductsByStock(){
        //Test
        Page<Product> result = repository.selectAllProducts(0,10,null,null,2,null,null);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Product02", result.getContent().get(0).getName());
        assertEquals(0, result.getContent().get(0).getStock());
    }
    @Test
    void testSelectAllProductsBySort(){
        //Set up
        String[] sort = {"expirationDate"};
        boolean[] order = {false};
        //Test
        Page<Product> result = repository.selectAllProducts(0,10,null,null,null,sort,order);

        //Assert
        assertNotNull(result);
        assertEquals(5, result.getTotalElements());
        List<Product> content = result.getContent();
        assertEquals("Product04", content.get(0).getName());
        assertEquals("Product03", content.get(1).getName());
        assertEquals("Product02", content.get(2).getName());
        assertEquals("Product05", content.get(4).getName());
    }

    @Test
void testPagination() {
    // First page with 2 items
    Page<Product> firstPage = repository.selectAllProducts(0, 2, null, null, null, null, null);
    assertEquals(5, firstPage.getTotalElements());
    assertEquals(2, firstPage.getContent().size());
    assertEquals(3, firstPage.getTotalPages());
    
    // Second page with 2 items
    Page<Product> secondPage = repository.selectAllProducts(1, 2, null, null, null, null, null);
    assertEquals(5, secondPage.getTotalElements());
    assertEquals(2, secondPage.getContent().size());
    assertEquals(3, secondPage.getTotalPages());
    
    // Third page with 1 item
    Page<Product> thirdPage = repository.selectAllProducts(2, 2, null, null, null, null, null);
    assertEquals(5, thirdPage.getTotalElements());
    assertEquals(1, thirdPage.getContent().size());
    assertEquals(3, thirdPage.getTotalPages());
    
    // Verify page content
    assertEquals("Product01", firstPage.getContent().get(0).getName());
    assertEquals("Product02", firstPage.getContent().get(1).getName());
    assertEquals("Product03", secondPage.getContent().get(0).getName());
    assertEquals("Product04", secondPage.getContent().get(1).getName());
    assertEquals("Product05", thirdPage.getContent().get(0).getName());
}

    //Count products test
    @Test
    void testCountProductsByName(){
        //String name, String category, Integer stock

        //Test
        Integer result = repository.countProducts("Product01",null,null);

        //Assert
        assertNotNull(result);
        assertEquals(1,result);

    }
    @Test
    void testCountProductsByCategory(){
        //String name, String category, Integer stock

        //Test
        Integer result = repository.countProducts(null,"Category02",null);

        //Assert
        assertNotNull(result);
        assertEquals(2,result);

    }
    @Test
    void testCountProductsByStock(){
        //String name, String category, Integer stock

        //Test
        Integer result = repository.countProducts(null,null,1);

        //Assert
        assertNotNull(result);
        assertEquals(4,result);

    }

    //Delete product test
    @Test
    void testDeleteProductByIdSuccess(){
        //Test
        Integer result = repository.deleteProductById(id01);

        //Assert
        assertNotNull(result);
        assertEquals(1,result);
    }
    @Test
    void testDeleteProductByIdFail(){
        //Test
        Integer result = repository.deleteProductById(UUID.randomUUID());

        //Assert
        assertNotNull(result);
        assertEquals(0,result);
    }

    //Update product tes
    @Test
    void testUpdateProductByIdSuccess(){
        //Set up
        NewProductsDTO updateProduct = new NewProductsDTO("Update product","cat01",43.4F,0,LocalDate.now());
        //Test
        Integer result = repository.updateProductById(id01,updateProduct);

        //Assert
        assertNotNull(result);
        assertEquals(1,result);
    }
    @Test
    void testUpdateProductByIdFail(){
        //Set up
        NewProductsDTO updateProduct = new NewProductsDTO("Update product","cat01",43.4F,0,LocalDate.now());
        //Test
        Integer result = repository.updateProductById(UUID.randomUUID(),updateProduct);

        //Assert
        assertNotNull(result);
        assertEquals(0,result);
    }

    //Reset Stock test
    @Test
    void testResetStockSuccess(){
        //Test
        Integer result = repository.resetStock(id01,true);

        //Assert
        assertNotNull(result);
        assertEquals(1,result);
    }
    @Test
    void testResetStockFail(){
//Test
        Integer result = repository.resetStock(UUID.randomUUID(),true);

        //Assert
        assertNotNull(result);
        assertEquals(0,result);
    }

    //Insert product test
    @Test
    void testInsertProductSuccess(){
        //Set up
        Product newProduct = new Product(UUID.randomUUID(),"New product","New category",101.3F,LocalDate.now(),32,LocalDate.now(),LocalDate.now());

        //Test
        Integer result = repository.insertProduct(newProduct);

        //Assert
        assertNotNull(result);
        assertEquals(1,result);
    }
    @Test
    void testInsertProductFail(){
        //Set up
        Product newProduct = new Product(id01,"Product01","New category",101.3F,LocalDate.now(),32,LocalDate.now(),LocalDate.now());

        //Test
        Integer result = repository.insertProduct(newProduct);

        //Assert
        assertNotNull(result);
        assertEquals(0,result);
    }

    //Get metrics test
    @Test
    void testGetMetrics(){
        //Test
        List<MetricsDTO> metrics = repository.getMetrics();

        //Assert
        assertNotNull(metrics);
        assertEquals(3,metrics.size());

        assertEquals(1577.5,metrics.getFirst().getValueInStock());
        assertEquals(55,metrics.getFirst().getTotalInStock());
        assertEquals(20.5,metrics.getFirst().getAverageInStock());

        assertEquals(405,metrics.getLast().getValueInStock());
        assertEquals(10,metrics.getLast().getTotalInStock());
        assertEquals(30.5,metrics.getLast().getAverageInStock());

        assertEquals(101,metrics.get(1).getValueInStock());
        assertEquals(2,metrics.get(1).getTotalInStock());
        assertEquals(50.5,metrics.get(1).getAverageInStock());
    }
}
