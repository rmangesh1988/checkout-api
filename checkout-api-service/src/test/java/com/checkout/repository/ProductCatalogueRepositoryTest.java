package com.checkout.repository;

import com.checkout.data.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductCatalogueRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductCatalogueRepository productCatalogueRepository;

    @Test
    void findByProductIdInShouldReturnProductsFromCatalogueForSpecificProductIds() {
        var rolex = Product.builder()
                .productId("001")
                .productName("Rolex")
                .unitPrice(100.0)
                .discountQuantity(3)
                .discountPrice(200.0)
                .build();
        entityManager.persist(rolex);

        var mk = Product.builder()
                .productId("002")
                .productName("Michael Kors")
                .unitPrice(80.0)
                .discountQuantity(2)
                .discountPrice(120)
                .build();
        entityManager.persist(mk);

        var products = productCatalogueRepository.findByProductIdIn(Set.of("001"));

        assertThat(products).isNotEmpty();
        assertThat(products).hasSize(1);
        assertThat(products).contains(rolex);
    }
}
