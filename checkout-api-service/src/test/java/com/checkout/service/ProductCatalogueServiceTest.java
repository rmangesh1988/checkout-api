package com.checkout.service;


import com.checkout.repository.ProductCatalogueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.checkout.util.TestDataHelper.buildProductCatalogue;
import static com.checkout.util.TestDataHelper.buildProducts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCatalogueServiceTest {

    @Mock
    ProductCatalogueRepository productCatalogueRepository;

    @InjectMocks
    ProductCatalogueService productCatalogueService;

    @Test
    void findAllShouldReturnTheWholeProductCatalogue() {
        var expectedProducts = buildProductCatalogue();
        when(productCatalogueRepository.findAll()).thenReturn(expectedProducts);

        var actualProducts = productCatalogueService.findAll();

        assertEquals(expectedProducts, actualProducts);
        verify(productCatalogueRepository, times(1)).findAll();
        verifyNoMoreInteractions(productCatalogueRepository);
    }

    @Test
    void findByProductIdInShouldReturnProductsFromCatalogueForSpecificProductIds() {
        var productIds = Set.of("001", "002");
        var expectedProducts = buildProducts(productIds);
        when(productCatalogueRepository.findByProductIdIn(productIds)).thenReturn(expectedProducts);

        var actualProducts = productCatalogueService.findByProductIdIn(productIds);

        assertEquals(expectedProducts, actualProducts);
        verify(productCatalogueRepository, times(1)).findByProductIdIn(productIds);
        verifyNoMoreInteractions(productCatalogueRepository);
    }
}
