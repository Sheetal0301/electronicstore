package com.bikkadIt.electronicstore.services.impl;
import com.bikkadIt.electronicstore.dtos.ProductDto;
import com.bikkadIt.electronicstore.entity.Product;
import com.bikkadIt.electronicstore.exception.ResourceNotFoundException;
import com.bikkadIt.electronicstore.repositories.ProductRepository;
import com.bikkadIt.electronicstore.services.ProductService;
import org.modelmapper.ModelMapper;
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
    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = this.modelMapper.map(productDto, Product.class);
        Product savedProduct= this.productRepository.save(product);
        return this.modelMapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto,Long productId) {
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
        return this.modelMapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void delete(Long productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        this.productRepository.delete(product);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        ProductDto productDto = this.modelMapper.map(product, ProductDto.class);
        return productDto;
    }
    @Override
    public List<ProductDto> getAll() {
        List<Product> products = this.productRepository.findAll();
        List<ProductDto> productDtos = products.stream().map((product) -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        return productDtos;
    }
    @Override
    public List<ProductDto> searchByTitle(String title) {
        List<Product> titleContaining = this.productRepository.findByTitleContaining(title);
        List<ProductDto> productDtoList = titleContaining.stream().map((content) -> this.modelMapper.map(content, ProductDto.class)).collect(Collectors.toList());
        return productDtoList;
    }
    @Override
    public List<ProductDto> searchByLive(boolean live) {
        List<Product> byLive = this.productRepository.findByLive(live);
        List<ProductDto> productDtos = byLive.stream().map((l) -> this.modelMapper.map(l, ProductDto.class)).collect(Collectors.toList());
        return productDtos;
    }
}
