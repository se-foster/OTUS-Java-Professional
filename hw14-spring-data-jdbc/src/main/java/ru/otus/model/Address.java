package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
public class Address {
    @Id
    private final Long id;

    @Column("street")
    private final String street;

    public Address(String street) {
        this.id = null;
        this.street = street;
    }

    @PersistenceConstructor
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
