package org.foxminded.springcourse.consoleapp.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* org.foxminded.springcourse.consoleapp.*.*.*(..))")
    public void allMethods() {
    }
}
