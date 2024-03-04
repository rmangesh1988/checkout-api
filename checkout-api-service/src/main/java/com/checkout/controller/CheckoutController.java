package com.checkout.controller;

import com.checkout.data.dto.CheckoutItem;
import com.checkout.data.dto.CheckoutResponse;
import com.checkout.service.CheckoutService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.checkout.data.util.ExceptionMessage.NO_PRODUCTS_SELECTED;
import static com.checkout.util.URIMapping.V1_CHECKOUT_URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(V1_CHECKOUT_URI)
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody @NotEmpty(message = NO_PRODUCTS_SELECTED) List<@Valid CheckoutItem> checkoutItems) {
        var checkoutResponse = checkoutService.performCheckout(checkoutItems);
        return ResponseEntity.ok(checkoutResponse);
    }

}
