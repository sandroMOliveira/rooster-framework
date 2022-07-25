package com.rooster.framework.core.injection.util;

import com.rooster.framework.core.annotations.Autowired;
import com.rooster.framework.core.annotations.Qualifier;
import com.rooster.framework.core.injection.RoosterInjection;
import org.burningwave.core.classes.FieldCriteria;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import static org.burningwave.core.assembler.StaticComponentContainer.Fields;

public class RoosterInjectionUtil {

    private RoosterInjectionUtil() {
        super();
    }

    public static void autowire(RoosterInjection roosterInjection, Class<?> clazz, Object classInstance)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Collection<Field> fields = Fields.findAllAndMakeThemAccessible(
                FieldCriteria.forEntireClassHierarchy()
                        .allThoseThatMatch(field -> field.isAnnotationPresent(Autowired.class)),
                clazz
        );
        for (Field field: fields) {
            String qualifier = field.isAnnotationPresent(Qualifier.class) ? field.getAnnotation(Qualifier.class).value()
                    : null;
            Object fieldInstance = roosterInjection.getBeanInstance(field.getType(), field.getName(), qualifier);
            Fields.setDirect(classInstance, field, fieldInstance);
            autowire(roosterInjection, fieldInstance.getClass(), fieldInstance);
        }
    }
}
