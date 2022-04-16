package ru.otus;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {
    private final Comparator<Customer> customerComparator = (o1, o2) -> (int) (o1.getScores() - o2.getScores());
    private final NavigableMap<Customer, String> treeMap = new TreeMap<>(customerComparator);

    public static class CustomerServiceEntry extends SimpleImmutableEntry<Customer, String> {
        public CustomerServiceEntry(Customer key, String value) {
            super(key, value);
        }
        public CustomerServiceEntry(Map.Entry<? extends Customer, ? extends String> entry) {
            super(entry);
        }
        @Override
        public Customer getKey() {
            return new Customer(super.getKey());
        }
    }

    public CustomerServiceEntry getSmallest() {
        var result = treeMap.descendingMap().lastEntry();
        if (result == null) {
            return null;
        }
        return new CustomerServiceEntry(result.getKey(), result.getValue());
    }

    public CustomerServiceEntry getNext(Customer customer) {
        var result = treeMap.entrySet().stream()
                .filter(entry -> entry.getKey().getScores() > customer.getScores())
                .min(Comparator.comparingLong(entry -> entry.getKey().getScores()))
                .orElse(null);
        if (result == null) {
            return null;
        }
        return new CustomerServiceEntry(result.getKey(), result.getValue());
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }
}
