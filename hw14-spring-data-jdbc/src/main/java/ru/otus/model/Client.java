package ru.otus.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
public class Client {

    @Id
    private final Long id;

    @Column("name")
    private final String name;

    @Column("address_id")
    private final Long addressId;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    public Client(String name, Long addressId, Set<Phone> phones) {
        this.id = null;
        this.name = name;
        this.addressId = addressId;
        this.phones = phones;
    }

    @PersistenceConstructor
    public Client(Long id, String name, Long addressId, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.addressId = addressId;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getAddressId() {
        return addressId;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addressId=" + addressId +
                ", phones=" + phones +
                '}';
    }
}
