package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.annotation.CommandMatching;
import org.foxminded.springcourse.consoleapp.annotation.PatternGroup;
import org.foxminded.springcourse.consoleapp.exception.CommandHandlerMapperException;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommandHandlerMapper {

    private final List<CommandHandler> commandHandlers;

    public CommandHandlerMapper(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    public String applyCommandHandler(String command) {
        for (CommandHandler commandHandler : commandHandlers) {
            for (Method method : commandHandler.getClass().getDeclaredMethods()) {
                CommandMatching annotation = method.getAnnotation(CommandMatching.class);
                if (annotation != null) {
                    String regex = annotation.regex();
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(command);
                    List<Object> args = getAnnotatedRegexGroups(method, matcher);
                    if (args != null) {
                        try {
                            return method.invoke(commandHandler, args.toArray()).toString();
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new CommandHandlerMapperException(e);
                        }
                    }
                }
            }
        }
        throw new CommandHandlerMapperException("No handlers found for command: " + command);
    }

    private List<Object> getAnnotatedRegexGroups(Method method, Matcher matcher) {
        List<Object> args = null;
        if (matcher.find()) {
            args = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                PatternGroup annotation = parameter.getAnnotation(PatternGroup.class);
                if (annotation != null) {
                    int groupNumber = annotation.value();
                    Class<?> parameterType = parameter.getType();
                    String group = matcher.group(groupNumber);
                    Object arg = getResolvedTypeGroup(group, parameterType);
                    args.add(arg);

                }
            }

        }
        return args;
    }

    private Object getResolvedTypeGroup(String group, Class<?> parameterType) {
        if (parameterType == int.class) {
            return Integer.parseInt(group);
        } else if (parameterType == String.class) {
            return group;
        } else {
            throw new CommandHandlerMapperException("Unsupported annotated parameter type: " +
                    parameterType.getSimpleName());
        }
    }
}
