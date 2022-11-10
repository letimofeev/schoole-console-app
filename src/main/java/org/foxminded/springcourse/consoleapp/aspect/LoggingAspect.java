package org.foxminded.springcourse.consoleapp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Around("org.foxminded.springcourse.consoleapp.aspect.Pointcuts.allMethods()")
    public Object aroundAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String shortClassName = methodSignature.getDeclaringType().getSimpleName();
        String fullMethodName = shortClassName + "." + methodSignature.getName();
        log.debug("[LoggingAspect] Begin of method: {}", fullMethodName);
        long startTimeMillis = System.currentTimeMillis();
        Object targetMethodResult = proceedingJoinPoint.proceed();
        long timeElapsed = System.currentTimeMillis() - startTimeMillis;
        log.debug("[LoggingAspect] End of method {}; Time elapsed: {} ms", fullMethodName, timeElapsed);
        return targetMethodResult;
    }
}
