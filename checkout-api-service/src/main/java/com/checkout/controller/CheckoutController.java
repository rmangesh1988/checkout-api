package com.checkout.controller;

import com.checkout.data.dto.ApiExceptionResponse;
import com.checkout.data.dto.CheckoutItem;
import com.checkout.data.dto.CheckoutResponse;
import com.checkout.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Checkout", description = "Checkout service APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping(V1_CHECKOUT_URI)
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Operation(
            summary = "Performs checkout",
            description = "Performs checkout by responding with final cart price"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CheckoutResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ApiExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ApiExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})
    })
    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody @NotEmpty(message = NO_PRODUCTS_SELECTED) List<@Valid CheckoutItem> checkoutItems) {
        var checkoutResponse = checkoutService.performCheckout(checkoutItems);
        return ResponseEntity.ok(checkoutResponse);
    }

}
