package org.foxminded.springcourse.consoleapp.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

    boolean autogenerated() default false;
}
