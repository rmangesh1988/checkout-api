package com.checkout.service;

import com.checkout.data.dto.CheckoutItem;
import com.checkout.data.entity.Product;
import com.checkout.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.checkout.util.TestDataHelper.buildProducts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    ProductCatalogueService productCatalogueService;

    @InjectMocks
    CheckoutService checkoutService;

    @Test
    void shouldThrowExceptionWhenInvalidProductIdIsProvided() {

        when(productCatalogueService.findByProductIdIn(Set.of("00111", "002"))).thenReturn(List.of(
                Product.builder()
                        .productId("002")
                        .productName("Michael Kors")
                        .unitPrice(80.0)
                        .discountQuantity(2)
                        .discountPrice(120)
                        .build()
        ));
        var selectedProductIds = List.of(
                new CheckoutItem("00111"),
                new CheckoutItem("002")
        );
        assertThrows(EntityNotFoundException.class, () -> checkoutService.performCheckout(selectedProductIds));

        verify(productCatalogueService, times(1)).findByProductIdIn(anySet());
        verifyNoMoreInteractions(productCatalogueService);
    }

    @ParameterizedTest
    @MethodSource("checkoutItems")
    void shouldPerformCheckoutAndReturnFinalPrice(List<CheckoutItem> checkoutItems, double expectedPrice) {
        var productIds = checkoutItems.stream().map(ci -> ci.productId()).collect(Collectors.toSet());
        when(productCatalogueService.findByProductIdIn(productIds)).thenReturn(buildProducts(productIds));
        var checkoutResponse = checkoutService.performCheckout(checkoutItems);

        assertEquals(checkoutResponse.price(), expectedPrice);
        verify(productCatalogueService, times(1)).findByProductIdIn(productIds);
        verifyNoMoreInteractions(productCatalogueService);

    }

    private static Stream<Arguments> checkoutItems() {
        var item001 = new CheckoutItem("001");
        var item002 = new CheckoutItem("002");
        var item004 = new CheckoutItem("004");
        var item003 = new CheckoutItem("003");
        return Stream.of(
                //Product list with no applicable discount
                Arguments.of(List.of(
                        item001,
                        item002,
                        item001,
                        item004,
                        item003
                ), 360.0),
                //Product list with applicable discount once
                Arguments.of(List.of(
                        item001,
                        item002,
                        item001,
                        item004,
                        item003,
                        item001
                ), 360.0),
                //Product list with single product having applicable discount twice
                Arguments.of(List.of(
                        item001,
                        item002,
                        item001,
                        item004,
                        item003,
                        item001,
                        item001,
                        item001,
                        item001
                ), 560.0),
                //Product list with multiple products having applicable discount twice
                Arguments.of(List.of(
                        item001,
                        item002,
                        item001,
                        item004,
                        item003,
                        item001,
                        item001,
                        item001,
                        item001,
                        item002,
                        item002,
                        item002
                ), 720.0),
                //Product list with applicable discount once
                Arguments.of(List.of(
                        item001,
                        item002,
                        item001,
                        item004,
                        item003,
                        item002
                ), 400.0),
                //Product list with no discount
                Arguments.of(List.of(
                        item004,
                        item003
                ), 80.0),
                //Single product
                Arguments.of(List.of(
                        item001
                ), 100.0)
        );
    }
}
