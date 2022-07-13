package ru.otus.core.repository;

import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.query.Query;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Predicate;

public class EntityGraphUtil<T> {

    private final Class<T> clazz;

    public EntityGraphUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void apply(Session session, Query<T> query) {
        var entityGraph = session.createEntityGraph(clazz);
        var fields = getFields();
        entityGraph.addAttributeNodes(getFieldsName(fields));
        query.applyGraph(entityGraph, GraphSemantic.FETCH);
    }

    private Field[] getFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(getFieldByAnnotations())
                .toArray(Field[]::new);
    }

    private Predicate<Field> getFieldByAnnotations() {
        return field -> field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(OneToOne.class);
    }

    private String[] getFieldsName(Field[] fields) {
        return Arrays.stream(fields)
                .map(Field::getName)
                .toArray(String[]::new);
    }
}
