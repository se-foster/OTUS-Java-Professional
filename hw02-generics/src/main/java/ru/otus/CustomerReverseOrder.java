package ru.otus;

import java.util.Stack;

public class CustomerReverseOrder {
    //todo: 2. реализовать методы этого класса - done

    private final Stack<Customer> stack = new Stack<>();

    public void add(Customer customer) {
        stack.push(customer);
    }

    public Customer take() {
        return stack.pop();
    }
}