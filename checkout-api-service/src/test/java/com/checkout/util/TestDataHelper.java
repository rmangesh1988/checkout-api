package com.checkout.util;

import com.checkout.data.entity.Product;
import java.util.List;
import java.util.Set;

public class TestDataHelper {

  public static List<Product> buildProductCatalogue() {
    return List.of(
        Product.builder()
            .id(1L)
            .productId("001")
            .productName("Rolex")
            .unitPrice(100.0)
            .discountQuantity(3)
            .discountPrice(200.0)
            .build(),
        Product.builder()
            .id(2L)
            .productId("002")
            .productName("Michael Kors")
            .unitPrice(80.0)
            .discountQuantity(2)
            .discountPrice(120)
            .build(),
        Product.builder().id(3L).productId("003").productName("Swatch").unitPrice(50.0).build(),
        Product.builder().id(4L).productId("004").productName("Casio").unitPrice(30.0).build());
  }

  public static List<Product> buildProducts(Set<String> productIds) {
    return buildProductCatalogue().stream()
        .filter(p -> productIds.contains(p.getProductId()))
        .toList();
  }
}
