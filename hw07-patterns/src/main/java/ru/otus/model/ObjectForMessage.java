package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public static ObjectForMessage copyOf(ObjectForMessage toCopy) {
        ObjectForMessage copy = new ObjectForMessage();
        copy.setData(new ArrayList<>(toCopy.getData()));
        return copy;
    }
}
