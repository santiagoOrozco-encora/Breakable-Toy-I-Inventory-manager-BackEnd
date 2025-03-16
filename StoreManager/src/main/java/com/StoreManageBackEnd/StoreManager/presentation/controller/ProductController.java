package com.StoreManageBackEnd.StoreManager.presentation.controller;

import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import com.StoreManageBackEnd.StoreManager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/product")
@RestController
//@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService personService){
        this.productService = personService;
    }

    //Adding a new product
    @PostMapping("/addProduct")
    public HttpStatus addProduct(@RequestBody NewProductsDTO newProduct){
        if(this.productService.addProduct(newProduct) == 1){
        return HttpStatus.CREATED;
        }else{
            return  HttpStatus.BAD_REQUEST;
        }
    }

    //Getting the list of products paginated, may be filtered
    @GetMapping
    public ResponseEntity<PagedListHolder<ProductsDTO>> getProducts(
            @RequestParam(defaultValue = "0")Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false,defaultValue = "") String name,
            @RequestParam(required = false,defaultValue = "") String[] category,
            @RequestParam(required = false,defaultValue = "") Integer stock,
            @RequestParam(required = false,defaultValue = "") String[] sort,
            @RequestParam(required = false,defaultValue = "") boolean[] order

    ){
         PagedListHolder<ProductsDTO> productsPage = this.productService.getProducts(page,size,name,category,stock,sort,order);

         return ResponseEntity.ok(productsPage);
    }

    //Update product with ID
    @PutMapping("/update")
    public ResponseEntity updateProduct(@RequestParam(required = true)UUID id,@RequestBody NewProductsDTO updatedProduct){
        return ResponseEntity.ok(this.productService.updateProductById(id,updatedProduct));
    }

    //Set out of stock
    @PostMapping("/products/{id}/outofstock")
    public ResponseEntity outStock(@PathVariable("id") UUID id){
        return ResponseEntity.ok(this.productService.resetStock(id,true));
    }

    //Set default stock
    @PutMapping("/products/{id}/instock")
    public ResponseEntity inStock(@PathVariable("id") UUID id){
        return ResponseEntity.ok(this.productService.resetStock(id,false));
    }

    @DeleteMapping()
    public ResponseEntity deleteById(@RequestParam(required = true) UUID id){
        return  ResponseEntity.ok(this.productService.deleteProduct(id));
    }

    //Metrics of the products
    @GetMapping("/metrics")
    public List<MetricsDTO> productsMetrics(){
        return this.productService.getMetrics();
    }


}
