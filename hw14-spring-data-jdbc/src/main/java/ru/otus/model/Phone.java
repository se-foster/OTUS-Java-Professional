package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
public class Phone {
    @Id
    private final Long id;

    @Column("number")
    private final String number;

    @Column("client_id")
    private final long clientId;

    public Phone(String number, long clientId) {
        id = null;
        this.number = number;
        this.clientId = clientId;
    }

    @PersistenceConstructor
    public Phone(Long id, String number, long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}
