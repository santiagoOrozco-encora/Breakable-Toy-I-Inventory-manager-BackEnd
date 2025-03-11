package com.StoreManageBackEnd.StoreManager.presentation.controller;

import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import com.StoreManageBackEnd.StoreManager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequestMapping("api/v1/product")
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService personService){
        this.productService = personService;
    }


    //Adding a new product
    @PostMapping
    public ResponseEntity addProduct(@RequestBody NewProductsDTO newProduct){
        return ResponseEntity.ok(productService.addProduct(newProduct));
    }

    //Getting the list of products paginated, may be filtered
    @GetMapping
    public ResponseEntity<Page<ProductsDTO>> getProducts(
            @RequestParam(defaultValue = "0")Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer stock
    ){
         Page<ProductsDTO> productsPage = this.productService.getProducts(page,size,name,category,stock);

         return ResponseEntity.ok(productsPage);
    }

    //Metrics of the products
    @GetMapping("/metrics")
    public List<MetricsDTO> productsMetrics(){
        return this.productService.getMetrics();
    }


}
