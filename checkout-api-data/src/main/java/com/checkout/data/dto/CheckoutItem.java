package com.checkout.data.dto;

import static com.checkout.data.util.ExceptionMessage.PRODUCT_ID_BLANK;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;

public record CheckoutItem(@JsonValue @NotBlank(message = PRODUCT_ID_BLANK) String productId) {}
