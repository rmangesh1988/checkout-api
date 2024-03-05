package com.checkout;

import com.checkout.data.entity.Product;
import com.checkout.service.ProductCatalogueService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CheckoutApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(CheckoutApiApplication.class, args);
  }

  @Profile({"local", "test"})
  @Bean
  CommandLineRunner run(ProductCatalogueService productCatalogueService) {
    return args -> {
      productCatalogueService.save(
          Product.builder()
              .productId("001")
              .productName("Rolex")
              .unitPrice(100.0)
              .discountQuantity(3)
              .discountPrice(200.0)
              .build());

      productCatalogueService.save(
          Product.builder()
              .productId("002")
              .productName("Michael Kors")
              .unitPrice(80.0)
              .discountQuantity(2)
              .discountPrice(120)
              .build());

      productCatalogueService.save(
          Product.builder().productId("003").productName("Swatch").unitPrice(50.0).build());

      productCatalogueService.save(
          Product.builder().productId("004").productName("Casio").unitPrice(30.0).build());
    };
  }
}
