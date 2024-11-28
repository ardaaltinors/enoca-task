package com.enoca.ecommerce.services;

import com.enoca.ecommerce.dtos.ProductRequestDTO;
import com.enoca.ecommerce.dtos.ProductResponseDTO;
import com.enoca.ecommerce.entities.Product;
import com.enoca.ecommerce.repositories.ProductRepository;
import com.enoca.ecommerce.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    // Create a new product
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        if(productRepository.existsByName(productRequestDTO.getName())) {
            throw new IllegalArgumentException("Product name already in use.");
        }
        Product product = productMapper.toEntity(productRequestDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }

    // Retrieve all products
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Retrieve a product by ID
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
        return productMapper.toResponseDTO(product);
    }

    // Update a product
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        if(!product.getName().equals(productRequestDTO.getName()) &&
                productRepository.existsByName(productRequestDTO.getName())) {
            throw new IllegalArgumentException("Product name already in use.");
        }

        productMapper.updateEntityFromDto(productRequestDTO, product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(updatedProduct);
    }

    // Delete a product
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }
}
