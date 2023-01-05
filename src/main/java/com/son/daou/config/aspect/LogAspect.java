package com.son.daou.config.aspect;

import com.son.daou.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Around("@annotation(PerformanceLogging)")
    public Object methodPerformance(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getName();
        LocalDateTime start = LocalDateTime.now();

        Object result = proceedingJoinPoint.proceed();

        LocalDateTime end = LocalDateTime.now();
        log.info("{} {} request time [{}] / response time [{}] / interval [{}]",
                className,
                methodName,
                start,
                end,
                end.toInstant(DateTimeUtils.ZONE_OFFSET).toEpochMilli() - start.toInstant(DateTimeUtils.ZONE_OFFSET).toEpochMilli()
        );
        return result;
    }

}
