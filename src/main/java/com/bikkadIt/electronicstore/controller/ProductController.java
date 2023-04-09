package com.bikkadIt.electronicstore.controller;

import com.bikkadIt.electronicstore.dtos.ProductDto;
import com.bikkadIt.electronicstore.payloads.ApiResponse;
import com.bikkadIt.electronicstore.payloads.AppConstants;
import com.bikkadIt.electronicstore.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    private Logger logger= LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto)
    {
        logger.info("Initiated request for save the product details");
        ProductDto savedProduct = this.productService.create(productDto);
        logger.info("Completed request for save the product details");
        return new ResponseEntity<ProductDto>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,@PathVariable Long productId)
    {
        logger.info("Initiated request for update the product details");
        ProductDto updatedProduct = this.productService.update(productDto, productId);
        logger.info("Completed request for update the product details");
        return new ResponseEntity<ProductDto>(updatedProduct,HttpStatus.OK);
    }
    @GetMapping("/getById/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable Long productId)
    {
        logger.info("Initiated request for get product details:{}"+productId);
        ProductDto productDto = this.productService.getProductById(productId);
        logger.info("Completed request for get the product details");
        return new ResponseEntity<ProductDto>(productDto,HttpStatus.OK);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<ProductDto>> getAllProduct()
    {
        logger.info("Initiated request for get all product details");
        List<ProductDto> productDtos = this.productService.getAll();
        logger.info("Completed request for get all product details");
        return new ResponseEntity<List<ProductDto>>(productDtos,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId)
    {
        logger.info("Initiated request for delete product details :{}"+productId);
        this.productService.delete(productId);
        logger.info("Completed request for delete product details");
        return new ResponseEntity<ApiResponse>(new ApiResponse(AppConstants.DELETE_PRODUCT,true),HttpStatus.OK);
    }

    @GetMapping("/searchTitle/{title}")
    public ResponseEntity<List<ProductDto>> searchByTitle(@PathVariable String title)
    {
        logger.info("Initiated request call for getting product by title");
        List<ProductDto> searchByTitle = this.productService.searchByTitle(title);
        logger.info("Completed request call for getting product by title");
        return new ResponseEntity<List<ProductDto>>(searchByTitle,HttpStatus.OK);
    }

    @GetMapping("/search/{live}")
    public ResponseEntity<List<ProductDto>> searchBylive(@PathVariable boolean live)
    {
        logger.info("Initiated request call for getting product by live");
        List<ProductDto> searchByLive = this.productService.searchByLive(live);
        logger.info("Completed request call for getting product by live");
        return new ResponseEntity<List<ProductDto>>(searchByLive,HttpStatus.OK);
    }
}
