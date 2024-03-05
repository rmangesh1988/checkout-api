package com.checkout.data.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessage {

  public static final String PRODUCT_NOT_FOUND =
      "Product id(s) [%s] are incorrect or no longer present. Please remove from the cart and proceed!";

  public static final String NO_PRODUCTS_SELECTED = "No products in the cart!";

  public static final String PRODUCT_ID_BLANK = "Product id cannot be blank!";

  public static final String VALIDATION_FAILURE = "Validation failure";
}
