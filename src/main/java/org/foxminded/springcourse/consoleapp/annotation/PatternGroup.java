package org.foxminded.springcourse.consoleapp.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {PARAMETER, TYPE, TYPE_PARAMETER, TYPE_USE})
public @interface PatternGroup {

    int group();
}
