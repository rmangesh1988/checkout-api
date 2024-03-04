package com.checkout.controller;

import com.checkout.data.dto.CheckoutResponse;
import com.checkout.exception.EntityNotFoundException;
import com.checkout.service.CheckoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.checkout.data.util.ExceptionMessage.NO_PRODUCTS_SELECTED;
import static com.checkout.data.util.ExceptionMessage.PRODUCT_ID_BLANK;
import static com.checkout.data.util.ExceptionMessage.PRODUCT_NOT_FOUND;
import static com.checkout.data.util.ExceptionMessage.VALIDATION_FAILURE;
import static com.checkout.util.URIMapping.V1_CHECKOUT_URI;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckoutController.class)
@AutoConfigureMockMvc
class CheckoutControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CheckoutService checkoutService;

    @Test
    void shouldReturnTotalCostWhenGivenAListOfProducts() throws Exception {

        when(checkoutService.performCheckout(anyList())).thenReturn(new CheckoutResponse(360.0));

        var productIds = """
                [
                "001",
                "002",
                "001",
                "004",
                "003"
                ]
                """;

        mockMvc.perform(post(V1_CHECKOUT_URI).contentType(APPLICATION_JSON).content(productIds))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.price", is(360.0)))
                .andDo(print());

        verify(checkoutService, times(1)).performCheckout(anyList());
        verifyNoMoreInteractions(checkoutService);
    }

    @Test
    void shouldThrowExceptionAsNotFoundWhenInvalidProductIdsAreProvided() throws Exception {

        var exceptionMessage = String.format(PRODUCT_NOT_FOUND, "0011");
        doThrow(new EntityNotFoundException(exceptionMessage)).when(checkoutService).performCheckout(anyList());

        var productIds = """
                [
                "0011",
                "002",
                "001",
                "004",
                "003"
                ]
                """;

        mockMvc.perform(post(V1_CHECKOUT_URI).contentType(APPLICATION_JSON).content(productIds))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(exceptionMessage)))
                .andDo(print());

        verify(checkoutService, times(1)).performCheckout(anyList());
        verifyNoMoreInteractions(checkoutService);
    }

    @Test
    void shouldThrowExceptionAsBadRequestWhenBlankProductIdsAreProvided() throws Exception {

        var productIds = """
                [
                "",
                "002"
                ]
                """;

        mockMvc.perform(post(V1_CHECKOUT_URI).contentType(APPLICATION_JSON).content(productIds))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.errors[*]", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(PRODUCT_ID_BLANK)))
                .andDo(print());

        verifyNoInteractions(checkoutService);
    }

    @Test
    void shouldThrowExceptionAsBadRequestWhenEmptyProductListIsProvided() throws Exception {

        var productIds = """
                [
                ]
                """;

        mockMvc.perform(post(V1_CHECKOUT_URI).contentType(APPLICATION_JSON).content(productIds))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.errors[*]", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(NO_PRODUCTS_SELECTED)))
                .andDo(print());

        verifyNoInteractions(checkoutService);
    }
}
