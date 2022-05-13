package ru.otus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFramework {

    private final Class<?> clazz;
    private final List<Method> beforeMethods;
    private final List<Method> testMethods;
    private final List<Method> afterMethods;

    public TestFramework(Class<?> clazz) {
        this.clazz = clazz;
        this.beforeMethods = new ArrayList<>();
        this.testMethods = new ArrayList<>();
        this.afterMethods = new ArrayList<>();
    }

    public static void runTests(String className) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        TestFramework testFramework = new TestFramework(Class.forName(className));
        testFramework.run();
    }

    private void run() throws InvocationTargetException, NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        collectAnnotations();
        checkAnnotationsAmount();
        executeTests();
    }

    private void collectAnnotations() {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }
    }

    private void checkAnnotationsAmount() {
        if (beforeMethods.size() > 1) {
            throw new IllegalArgumentException("В тесте может быть не более одного метода @Before");
        }
        if (afterMethods.size() > 1) {
            throw new IllegalArgumentException("В тесте может быть не более одного метода @After");
        }
    }

    private void executeTests()
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

    private void printStatistic(int succeed, int failed) {
        int total = succeed + failed;
        System.out.println("Выполнено тестов: " + total + " успешно: " + succeed + ", провалено: " + failed);
    }
}
