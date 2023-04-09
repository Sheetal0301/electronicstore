package com.bikkadIt.electronicstore.services.impl;
import com.bikkadIt.electronicstore.controller.UserController;
import com.bikkadIt.electronicstore.dtos.ProductDto;
import com.bikkadIt.electronicstore.entity.Product;
import com.bikkadIt.electronicstore.exception.ResourceNotFoundException;
import com.bikkadIt.electronicstore.repositories.ProductRepository;
import com.bikkadIt.electronicstore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;
    Logger logger= LoggerFactory.getLogger(ProductServiceImpl.class);
    @Override
    public ProductDto create(ProductDto productDto) {
        logger.info("Initiated dao call for save the product details");
        Product product = this.modelMapper.map(productDto, Product.class);
        Product savedProduct= this.productRepository.save(product);
        logger.info("Completed dao call for save the product details");
        return this.modelMapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto,Long productId) {
        logger.info("Initiated dao call for update the product details");
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        product.setDescription(productDto.getDescription());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setAddedDate(productDto.getAddedDate());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        Product updatedProduct = this.productRepository.save(product);
        logger.info("Completed dao call for update the product details");
        return this.modelMapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void delete(Long productId) {
        logger.info("Initiated dao call for delete product details :{}"+productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        logger.info("Completed dao call for delete product details");
        this.productRepository.delete(product);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        logger.info("Initiated dao call for get product by Id :{}"+productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        ProductDto productDto = this.modelMapper.map(product, ProductDto.class);
        logger.info("Completed dao call for get product by Id ");
        return productDto;
    }
    @Override
    public List<ProductDto> getAll() {
        logger.info("Initiated dao call for getting all product details");
        List<Product> products = this.productRepository.findAll();
        List<ProductDto> productDtos = products.stream().map((product) -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        logger.info("Completed dao call for getting all product details");
        return productDtos;
    }
    @Override
    public List<ProductDto> searchByTitle(String title) {
        logger.info("Initiated dao call for getting user by title");
        List<Product> titleContaining = this.productRepository.findByTitleContaining(title);
        List<ProductDto> productDtoList = titleContaining.stream().map((content) -> this.modelMapper.map(content, ProductDto.class)).collect(Collectors.toList());
        logger.info("Completed dao call for getting user by title");
        return productDtoList;
    }
    @Override
    public List<ProductDto> searchByLive(boolean live) {
        logger.info("Initiated dao call for getting user by live");
        List<Product> byLive = this.productRepository.findByLive(live);
        List<ProductDto> productDtos = byLive.stream().map((l) -> this.modelMapper.map(l, ProductDto.class)).collect(Collectors.toList());
        logger.info("Completed dao call for getting user by live");
        return productDtos;
    }
}
