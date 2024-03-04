package com.checkout.data.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;

import static com.checkout.data.util.ExceptionMessage.PRODUCT_ID_BLANK;

public record CheckoutItem(@JsonValue @NotBlank(message = PRODUCT_ID_BLANK) String productId) {

}
