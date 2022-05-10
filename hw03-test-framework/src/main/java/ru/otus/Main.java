package ru.otus;

public class Main {
    public static void main(String[] args) {
        try {
            TestFramework.runTests("ru.otus.TestClass");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}