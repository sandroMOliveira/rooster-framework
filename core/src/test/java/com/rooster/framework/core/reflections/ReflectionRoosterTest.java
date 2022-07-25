package com.rooster.framework.core.reflections;

import com.rooster.framework.core.model.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionRoosterTest {

    @Test
    public void checkReflections() {
        Order order = new Order("Pao de queijo", "food", LocalDateTime.now());
        List<Field> fields = ReflectionRooster.checkFields(order);
        assertFalse(fields.isEmpty());
    }

    @Test
    public void checkMethods() {
        Order order = new Order("Pao de queijo", "food", LocalDateTime.now());
        List<Method> fields = ReflectionRooster.checkMethods(order);
        assertFalse(fields.isEmpty());
    }

}