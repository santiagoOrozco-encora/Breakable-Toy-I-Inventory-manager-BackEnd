package com.StoreManageBackEnd.StoreManager.presentation.controller;

import com.StoreManageBackEnd.StoreManager.presentation.dto.MetricsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.NewProductsDTO;
import com.StoreManageBackEnd.StoreManager.presentation.dto.ProductsDTO;
import com.StoreManageBackEnd.StoreManager.service.ProductService;
import com.StoreManageBackEnd.StoreManager.data.model.BatchOperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/product")
//@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {
    private final ProductService productService;
    private final Log log = LogFactory.getLog(this.getClass());    
    @Autowired
    public ProductController(ProductService personService){
        this.productService = personService;
    }

    //Adding a new product

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody NewProductsDTO newProduct){
        try {
            int productId = productService.addProduct(newProduct);
            if(productId == 1){
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of(
                            "success", true,
                            "message", "Product added successfully"
                        ));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                            "success", false,
                            "message", "Product not added"
                        ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                        "success", false,
                        "message", e.getMessage()
                    ));
        } catch (Exception e) {
            log.error("Error adding product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "message", "An error occurred while adding the product"
                    ));
        }
    }

    @PostMapping("/addProducts")
    public ResponseEntity<?> addProducts(@RequestBody NewProductsDTO[] newProducts){
        if (newProducts == null || newProducts.length == 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                        "success", false,
                        "message", "No products provided"
                    ));
        }
            try {
        BatchOperationResult result = this.productService.addProducts(newProducts);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("successCount", result.successCount());
        
        if (result.failureCount() > 0) {
            response.put("failureCount", result.failureCount());
            response.put("errors", result.errorMessages());
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
        log.error("Error processing batch insert: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "message", "An error occurred while processing the request",
                    "error", e.getMessage()
                ));
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
    public ResponseEntity<Integer> updateProduct(@RequestParam(required = true)UUID id,@RequestBody NewProductsDTO updatedProduct){
        return ResponseEntity.ok(this.productService.updateProductById(id,updatedProduct));
    }

    //Set out of stock
    @PostMapping("/products/{id}/outofstock")
    public ResponseEntity<Integer> outStock(@PathVariable("id") UUID id){
        return ResponseEntity.ok(this.productService.resetStock(id,true));
    }

    //Set default stock
    @PutMapping("/products/{id}/instock")
    public ResponseEntity<Integer> inStock(@PathVariable("id") UUID id){
        return ResponseEntity.ok(this.productService.resetStock(id,false));
    }

    @DeleteMapping()
    public ResponseEntity<Integer> deleteById(@RequestParam(required = true) UUID id){
        return  ResponseEntity.ok(this.productService.deleteProduct(id));
    }

    //Metrics of the products
    @GetMapping("/metrics")
    public ResponseEntity<List<MetricsDTO>> productsMetrics(){
        return ResponseEntity.ok(this.productService.getMetrics());
    }


}
