package com.example.productmanagement.service;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public List<Product> getAllProducts(Sort sort) {
        return productRepository.findAll(sort);
    }
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    
    public List<Product> findByCategory(String category){
      return productRepository.findByCategory(category);
    }


    @Override
    public Product saveProduct(Product product) {
        // Validation logic can go here
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageble) {
        return productRepository.findByNameContaining(keyword,pageble);
    }
    
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> advancedSearchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice){
        return productRepository.searchProducts(name, category, minPrice, maxPrice);
    }

    @Override
    public List<String> findAllCategories() {
        return productRepository.findAllCategories();
    }
}
