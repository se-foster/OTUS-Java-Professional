package ru.otus;

public class TestClass {

    @Before
    public void before() {
        System.out.println("Выполнен метод before");
    }

    @After
    public void after() {
        System.out.println("выполнен метод after");
    }

    @Test
    public void test1() {
        System.out.println("выполнен тест 1");
    }

    @Test
    public void test2() {
        throw new RuntimeException("ошибка в тесте 2");
    }

    @Test
    public void test3() {
        System.out.println("выполнен тест 3");
    }
}
