package com.checkout.service;

import com.checkout.data.dto.CheckoutItem;
import com.checkout.data.dto.CheckoutResponse;
import com.checkout.data.entity.Product;
import com.checkout.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.checkout.data.util.ExceptionMessage.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ProductCatalogueService productCatalogueService;

    /**
     * Computes the final price of products in the cart, by applying necessary discounts.
     * @param checkoutItems List of items/products in the cart.
     * @return CheckoutResponse, that holds the final price.
     * @throws EntityNotFoundException if invalid product id is found in the products cart list.
     */
    public CheckoutResponse performCheckout(List<CheckoutItem> checkoutItems) {

        var productIds = checkoutItems.stream().map(CheckoutItem::productId).collect(Collectors.toSet());
        var productsInCatalogue = productCatalogueService.findByProductIdIn(productIds);
        if(productIds.size() != productsInCatalogue.size()) {
            var productIdsInCatalogue = productsInCatalogue.stream().map(Product::getProductId).collect(Collectors.toSet());
            var inValidProductIds = new HashSet<>(CollectionUtils.removeAll(productIds, productIdsInCatalogue));
            throw new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, String.join(", ", inValidProductIds)));
        }

        //Aggregate selected product ids
        var selectedProductIdsCount = checkoutItems.stream().collect(Collectors.groupingBy(CheckoutItem::productId, Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
        //Apply discount logic
        var totalPrice = selectedProductIdsCount.entrySet().stream().mapToDouble(e -> calculateIndividualProductPrice(e, productsInCatalogue)).sum();
        return new CheckoutResponse(totalPrice);
    }

    private double calculateIndividualProductPrice(Map.Entry<String, Integer> entry, List<Product> products) {
        var product = products.stream().filter(p -> entry.getKey().equalsIgnoreCase(p.getProductId())).findFirst().get();
        var totalQuantity = entry.getValue();
        var discountQuantity = product.getDiscountQuantity();
        double finalPriceOfProduct;
        var unitPrice = product.getUnitPrice();
        if(discountQuantity != 0) {
            var quantityWithoutDiscount = totalQuantity % discountQuantity;
            finalPriceOfProduct = (quantityWithoutDiscount * unitPrice) + ((totalQuantity - quantityWithoutDiscount) / discountQuantity * product.getDiscountPrice());
        } else {
            finalPriceOfProduct = totalQuantity * unitPrice;
        }
        return finalPriceOfProduct;
    }
}
