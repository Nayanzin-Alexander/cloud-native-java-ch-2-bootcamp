package com.nayanzin.demo.configuration.aspects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@Aspect
@Component
public class LoggingAroundAspect {

    private Log log = LogFactory.getLog(getClass());

    @Around("execution(* com.nayanzin.demo..restrepository..* (..))")
    public Object logExecutionTimeInRestRepositoryLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime start = LocalDateTime.now();
        Throwable toThrow = null;
        Object returnValue = null;
        try {
            returnValue = joinPoint.proceed();
        } catch (Throwable t) {
            toThrow = t;
        }
        LocalDateTime stop = LocalDateTime.now();
        log.debug(joinPoint.getSignature()
                + " starting "  + start.toString()
                + " finishing @ " + stop.toString()
                + " with duration "
                + stop.minusNanos(start.getNano()).getNano() + " nanoseconds.");
        if (nonNull(toThrow)) {
            throw toThrow;
        }
        return returnValue;
    }

}
