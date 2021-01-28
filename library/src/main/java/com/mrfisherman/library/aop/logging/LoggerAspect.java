package com.mrfisherman.library.aop.logging;

import com.mrfisherman.library.model.pojo.Email;
import com.mrfisherman.library.service.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.lang.String.format;

@Aspect
@Component
@Order(5)
@Slf4j
public class LoggerAspect {

    private final String receiverEmail;
    private final EmailService emailService;

    public LoggerAspect(@Value("${exception.handler.receiver.mail}") String receiverEmail, EmailService emailService) {
        this.receiverEmail = receiverEmail;
        this.emailService = emailService;
    }

    @Pointcut("execution(* com.mrfisherman.library..*(..))")
    private void allMethodsInProject() {
    }

    @Before("execution(* com.mrfisherman.library.service.*.delete*(..))")
    private void onDeleteMethodPerformedInServiceLayer(JoinPoint joinPoint) {
        log.info(format("DELETE operation was performed in %s with arguments %s",
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs())));
    }

    @AfterThrowing(pointcut = "allMethodsInProject()", throwing = "exception")
    private void onThrowingExceptionOnAnyMethod(JoinPoint joinPoint, Throwable exception) {
        log.warn(joinPoint.getSignature() + " threw exception " + exception.getClass());
    }

    @AfterThrowing(pointcut = "allMethodsInProject()", throwing = "nullPointer")
    private void onThrowingNullPointerExceptionOnAnyMethod(JoinPoint joinPoint, NullPointerException nullPointer) {
        String logMessage = createLogMessage(joinPoint, nullPointer);
        log.error(logMessage);

        String emailMessage = createEmailMessage(joinPoint, nullPointer);
        emailService.sendMessage(prepareAlertEmail(emailMessage));
    }

    private String createEmailMessage(JoinPoint joinPoint, NullPointerException nullPointer) {
        return format("Null pointer exception was thrown on method %s"
                        + "\nwith args: %s,"
                        + "\nwith message: %s,"
                        + "\nstack cause: %s",
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()),
                nullPointer.getMessage(),
                nullPointer.getCause());
    }

    private String createLogMessage(JoinPoint joinPoint, NullPointerException nullPointer) {
        return format("Null pointer exception was thrown on method %s with args: %s, message: %s",
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()),
                nullPointer.getMessage());
    }

    private Email prepareAlertEmail(String message) {
        return new Email(receiverEmail, "Alert: NullPointerException in project", message);
    }
}
