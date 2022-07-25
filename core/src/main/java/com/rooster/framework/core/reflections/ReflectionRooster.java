package com.rooster.framework.core.reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionRooster {

    public static List<Field> checkFields(Object clazz) {
        Field[] fields = clazz.getClass().getDeclaredFields();
        return Arrays.stream(fields).collect(Collectors.toList());
    }

    public static List<Method> checkMethods(Object clazz) {
        Method[] methods = clazz.getClass().getDeclaredMethods();
        return Arrays.stream(methods).collect(Collectors.toList());
    }

}
