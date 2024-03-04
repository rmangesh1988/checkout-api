package com.checkout.repository;

import com.checkout.data.entity.Product;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductCatalogueRepository extends ListCrudRepository<Product, Long> {

    List<Product> findByProductIdIn(Collection<String> productIds);
}
