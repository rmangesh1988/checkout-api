package com.checkout.logging;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

  @Pointcut("within(com.checkout.controller..*)")
  public void restPointCut() {}

  @Around("restPointCut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info(
        "Entering into {}.{} with argument[s] = {} ",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(),
        Arrays.toString(joinPoint.getArgs()));

    try {
      var result = joinPoint.proceed();
      log.info(
          "Exiting from {}.{} with result = {}",
          joinPoint.getSignature().getDeclaringTypeName(),
          joinPoint.getSignature().getName(),
          result);
      return result;
    } catch (Exception ex) {
      log.error(
          "Error in {}.{}, exception = {} ",
          joinPoint.getSignature().getDeclaringTypeName(),
          joinPoint.getSignature().getName(),
          ex.getMessage());
      throw ex;
    }
  }
}
