package com.inventory.services;


import com.inventory.dto.ProductDTO;
import com.inventory.entities.Business;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.Category;
import com.inventory.entities.Product;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.BusinessRepository;
import com.inventory.repositories.CategoryRepository;
import com.inventory.repositories.ProductRepository;
import com.inventory.repositories.WarehouseRepository;
import com.inventory.security.SubscriptionPermissionChecker;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BusinessRepository businessRepository;
    private final BusinessOwnerRepository businessOwnerRepository;


    public void addProduct(ProductDTO request, Long businessId, Long ownerId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        BusinessOwner owner = businessOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        SubscriptionPermissionChecker.checkAddEditPermission(owner); // 🔥 Check here

        if (!business.getBusinessOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Business does not belong to this owner.");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setBusiness(business);

        productRepository.save(product);
    }


    public void updateProduct(Long productId, ProductDTO request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        BusinessOwner owner = product.getBusiness().getBusinessOwner();

        SubscriptionPermissionChecker.checkAddEditPermission(owner);
        
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());

        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    public List<ProductDTO> getAllProducts(Long businessId) {
        return productRepository.findByBusinessId(businessId).stream()
                .map(p -> new ProductDTO(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getStockQuantity(), p.getCategory()))
                .collect(Collectors.toList());
    }
    
    public long countAllProducts() {
        return productRepository.count();
    }

}
