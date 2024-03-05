package com.checkout.service;

import com.checkout.data.entity.Product;
import com.checkout.repository.ProductCatalogueRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCatalogueService {

  private final ProductCatalogueRepository productCatalogueRepository;

  public List<Product> findByProductIdIn(Collection<String> productIds) {
    return productCatalogueRepository.findByProductIdIn(productIds);
  }

  public List<Product> findAll() {
    return productCatalogueRepository.findAll();
  }

  public Product save(Product product) {
    return productCatalogueRepository.save(product);
  }
}
