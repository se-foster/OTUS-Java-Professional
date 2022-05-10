package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFramework {
    public static void runTests(String className) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        Class<?> clazz = Class.forName(className);

        collectAnnotations(clazz, beforeMethods, afterMethods, testMethods);
        checkAnnotationsAmount(beforeMethods, afterMethods);
        executeTests(clazz, beforeMethods, afterMethods, testMethods);
    }

    private static void collectAnnotations(Class<?> clazz, List<Method> beforeMethods, List<Method> afterMethods,
                                           List<Method> testMethods) {
        for (Method method : clazz.getMethods()) {
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (annotation.annotationType().equals(Before.class)) {
                    beforeMethods.add(method);
                } else if (annotation.annotationType().equals(After.class)) {
                    afterMethods.add(method);
                } else if (annotation.annotationType().equals(Test.class))
                    testMethods.add(method);
            }
        }
    }

    private static void checkAnnotationsAmount(List<Method> beforeMethods, List<Method> afterMethods) {
        if (beforeMethods.size() > 1) {
            throw new IllegalArgumentException("В тесте может быть не более одного метода @Before");
        }
        if (afterMethods.size() > 1) {
            throw new IllegalArgumentException("В тесте может быть не более одного метода @After");
        }
    }

    private static void executeTests(Class<?> clazz, List<Method> beforeMethods, List<Method> afterMethods,
                                     List<Method> testMethods)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        int succeed = 0;
        int failed = 0;
        for (Method method : testMethods) {
            Object testObject = clazz.getConstructor().newInstance();
            if (!beforeMethods.isEmpty())
                beforeMethods.get(0).invoke(testObject);
            try {
                method.invoke(testObject);
                succeed++;
            } catch (Exception e) {
                System.out.println("Тест: " + method.getName() + " завершился неудачей! Причина: " + e.getCause());
                failed++;
            }
            if (!afterMethods.isEmpty()) {
                afterMethods.get(0).invoke(testObject);
            }
        }
        printStatistic(succeed, failed);
    }

    private static void printStatistic(int succeed, int failed) {
        int total = succeed + failed;
        System.out.println("Выполнено тестов: " + total + " успешно: " + succeed + ", провалено: " + failed);
    }
}
