package com.rooster.framework.core.injection;

import com.rooster.framework.core.annotations.Component;
import com.rooster.framework.core.injection.util.RoosterInjectionUtil;
import com.rooster.framework.core.util.CheckUtils;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.SearchConfig;

import javax.management.RuntimeErrorException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class RoosterInjection {

    private Map<Class<?>, Class<?>> diMap;
    private  Map<Class<?>, Object> applicationScope;


    private static RoosterInjection roosterInjection;

    private RoosterInjection() {
        super();
        diMap = new HashMap<>();
        applicationScope = new HashMap<>();
    }

    /**
     * Start application here
     */
    public static  void startApplication(Class<?> mainClassz) {
        try {
            synchronized (RoosterInjection.class) {
                if (roosterInjection == null) {
                    roosterInjection = new RoosterInjection();
                    roosterInjection.initRoosterFramework(mainClassz);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in inject classes to start application", e);
        }
    }

    public void initRoosterFramework(Class<?> mainClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Class<?>[] classes = getClasses(mainClass.getPackage().getName(), true);
        ComponentContainer container = ComponentContainer.getInstance();
        ClassHunter classHunter = container.getClassHunter();
        String packageRealPath = mainClass.getPackage().getName().replace(".", "/");

        try (ClassHunter.SearchResult result = classHunter.findBy(
                SearchConfig.forResources(packageRealPath)
                        .by(ClassCriteria.create().allThoseThatMatch(cls -> {
                            return cls.getAnnotations() != null;
                        }))
        )) {
            Collection<Class<?>> types = result.getClasses();
            types.stream().forEach(implClass -> {
                Class<?>[] interfaces = implClass.getInterfaces();
                if (interfaces.length == 0) {
                    diMap.put(implClass, implClass);
                } else {
                    Arrays.stream(interfaces).forEach(iface -> {
                        diMap.put(implClass, iface);
                    });
                }
            });
        }

        for (Class<?> clazz: classes) {
            if (clazz.isAnnotationPresent(Component.class)) {
                Object classInstance = clazz.getDeclaredConstructors()[0].newInstance();
                applicationScope.put(clazz, classInstance);
                RoosterInjectionUtil.autowire(this, clazz, classInstance);
            }
        }
    }

    public static <T> T getServices(Class<?> clazz) {
        try {
            return roosterInjection.getBeanInstance(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getBeanInstance(Class<?> interfaceClass)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return (T) getBeanInstance(interfaceClass, null, null);
    }

    public <T> Object getBeanInstance(Class<?> interfaceClass, String fieldName, String qualifier)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> implClazz = getImplementationClass(interfaceClass, fieldName, qualifier);
        if (applicationScope.containsKey(implClazz)) {
            return applicationScope.get(implClazz);
        }

        synchronized (applicationScope) {
            Object service = implClazz.getConstructor().newInstance();
            applicationScope.put(implClazz, service);
            return service;
        }
    }

    private Class<?> getImplementationClass(Class<?> interfaceClazz, final String fieldName, String qualifier) {
        Set<Map.Entry<Class<?>, Class<?>>> implementationsClasses = diMap.entrySet().stream()
                .filter(classClassEntry -> classClassEntry.getValue() == interfaceClazz).collect(Collectors.toSet());
        String errorMessage = "";
        if (CheckUtils.isNullOrEmpty(implementationsClasses)) {
            errorMessage =  "Oh no! The rooster not found implementation for interface " + interfaceClazz.getName();
        } else if (implementationsClasses.size() == 1) {
            Optional<Map.Entry<Class<?>, Class<?>>> optional = implementationsClasses.stream().findFirst();
            if (optional.isPresent()) return optional.get().getKey();
        } else if (implementationsClasses.size() > 1) {
            final String findBy = (qualifier == null || qualifier.trim().length() == 0) ? fieldName : qualifier;
            Optional<Map.Entry<Class<?>, Class<?>>> optional = implementationsClasses.stream()
                    .filter(entry -> entry.getKey().getSimpleName().equalsIgnoreCase(findBy)).findAny();
            if (optional.isPresent()) return optional.get().getKey();
        } else {
            errorMessage =  "There are " + implementationsClasses.size() + " of interface " + interfaceClazz.getName()
                    + " Expected single implementation or make use of @CustomQualifier to resolve conflict";
        }
        throw new RuntimeErrorException(new Error(errorMessage));
    }

    public Class<?>[] getClasses(String packageName, boolean recursive) {
        ComponentContainer componentContainer = ComponentContainer.getInstance();
        ClassHunter classHunter = componentContainer.getClassHunter();
        String packageRealPath = packageName.replace(".", "/");
        SearchConfig searchConfig = SearchConfig.forResources(packageRealPath);
        if (!recursive) {
            searchConfig.findInChildren();
        }

        try (ClassHunter.SearchResult result = classHunter.findBy(searchConfig)) {
            Collection<Class<?>> classes = result.getClasses();
            return classes.toArray(new Class[classes.size()]);
        }
    }

}
