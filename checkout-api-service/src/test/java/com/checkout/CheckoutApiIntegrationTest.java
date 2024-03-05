package com.checkout;

import static com.checkout.data.util.ExceptionMessage.NO_PRODUCTS_SELECTED;
import static com.checkout.data.util.ExceptionMessage.PRODUCT_ID_BLANK;
import static com.checkout.data.util.ExceptionMessage.PRODUCT_NOT_FOUND;
import static com.checkout.data.util.ExceptionMessage.VALIDATION_FAILURE;
import static com.checkout.util.URIMapping.V1_CHECKOUT_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.checkout.data.dto.ApiExceptionResponse;
import com.checkout.data.dto.CheckoutItem;
import com.checkout.data.dto.CheckoutResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(
    classes = CheckoutApiApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class CheckoutApiIntegrationTest {

  @LocalServerPort int port;

  @Autowired TestRestTemplate testRestTemplate;

  @Test
  void shouldReturnTotalCostWhenGivenAListOfProducts() {

    var checkoutItems =
        List.of(
            new CheckoutItem("001"),
            new CheckoutItem("002"),
            new CheckoutItem("001"),
            new CheckoutItem("004"),
            new CheckoutItem("003"));
    var httpEntity = new HttpEntity<>(checkoutItems);
    var checkoutResponseResponseEntity =
        testRestTemplate.exchange(
            createURLWithPort(V1_CHECKOUT_URI),
            HttpMethod.POST,
            httpEntity,
            CheckoutResponse.class);

    assertEquals(
        HttpStatusCode.valueOf(OK.value()), checkoutResponseResponseEntity.getStatusCode());
    assertEquals(APPLICATION_JSON, checkoutResponseResponseEntity.getHeaders().getContentType());
    assertEquals(360.0, checkoutResponseResponseEntity.getBody().price());
  }

  @Test
  void shouldThrowExceptionAsNotFoundWhenInvalidProductIdsAreProvided() {

    var checkoutItems = List.of(new CheckoutItem("00111"), new CheckoutItem("002"));
    var httpEntity = new HttpEntity<>(checkoutItems);
    var apiExceptionResponseResponseEntity =
        testRestTemplate.exchange(
            createURLWithPort(V1_CHECKOUT_URI),
            HttpMethod.POST,
            httpEntity,
            ApiExceptionResponse.class);

    assertEquals(
        HttpStatusCode.valueOf(NOT_FOUND.value()),
        apiExceptionResponseResponseEntity.getStatusCode());
    assertEquals(
        APPLICATION_JSON, apiExceptionResponseResponseEntity.getHeaders().getContentType());
    assertEquals(
        String.format(PRODUCT_NOT_FOUND, "00111"),
        apiExceptionResponseResponseEntity.getBody().getMessage());
  }

  @Test
  void shouldThrowExceptionAsBadRequestWhenBlankProductIdsAreProvided() {

    var checkoutItems = List.of(new CheckoutItem(""), new CheckoutItem("002"));
    var httpEntity = new HttpEntity<>(checkoutItems);
    var apiExceptionResponseResponseEntity =
        testRestTemplate.exchange(
            createURLWithPort(V1_CHECKOUT_URI),
            HttpMethod.POST,
            httpEntity,
            ApiExceptionResponse.class);
    var responseBody = apiExceptionResponseResponseEntity.getBody();

    assertEquals(
        HttpStatusCode.valueOf(BAD_REQUEST.value()),
        apiExceptionResponseResponseEntity.getStatusCode());
    assertEquals(
        APPLICATION_JSON, apiExceptionResponseResponseEntity.getHeaders().getContentType());
    assertEquals(VALIDATION_FAILURE, responseBody.getMessage());
    assertThat(responseBody.getErrors()).hasSize(1);
    assertThat(responseBody.getErrors()).containsExactly(PRODUCT_ID_BLANK);
  }

  @Test
  void shouldThrowExceptionAsBadRequestWhenEmptyProductListIsProvided() {

    var httpEntity = new HttpEntity<>(Collections.emptyList());
    var apiExceptionResponseResponseEntity =
        testRestTemplate.exchange(
            createURLWithPort(V1_CHECKOUT_URI),
            HttpMethod.POST,
            httpEntity,
            ApiExceptionResponse.class);
    var responseBody = apiExceptionResponseResponseEntity.getBody();

    assertEquals(
        HttpStatusCode.valueOf(BAD_REQUEST.value()),
        apiExceptionResponseResponseEntity.getStatusCode());
    assertEquals(
        APPLICATION_JSON, apiExceptionResponseResponseEntity.getHeaders().getContentType());
    assertEquals(VALIDATION_FAILURE, responseBody.getMessage());
    assertThat(responseBody.getErrors()).hasSize(1);
    assertThat(responseBody.getErrors()).containsExactly(NO_PRODUCTS_SELECTED);
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}
