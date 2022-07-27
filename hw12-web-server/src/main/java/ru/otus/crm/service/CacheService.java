package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;

import ru.otus.crm.model.Client;

public interface CacheService {
    Client saveClient(Client client);

    Client update(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();

    void saveAll(List<Client> clientList);

    boolean isConsistent();
}
