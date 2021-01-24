package com.mrfisherman.library.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.lang.String.format;

@Aspect
@Component
@Order(5)
public class LoggerAspect {

    Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Before(value = "execution(* com.mrfisherman.library.service.*.delete*(..))")
    private void onDeleteMethodPerformedInServiceLayer(JoinPoint joinPoint) {
        logger.info(format("DELETE operation was performed in %s with arguments %s",
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs())));
    }

}
