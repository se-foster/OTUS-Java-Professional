package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;

import ru.otus.cachehw.MyCache;
import ru.otus.crm.model.Client;

public class CacheServiceImpl implements CacheService {

    private final MyCache<Long, Client> myCache;

    public CacheServiceImpl(MyCache<Long, Client> myCache) {
        this.myCache = myCache;
    }

    public Client saveClient(Client client) {
        myCache.put(client.getId(), client);
        return client;
    }

    public Client update(Client client) {
        return myCache.update(client.getId(), client);
    }

    public Optional<Client> getClient(long id) {
        return Optional.ofNullable(myCache.get(id));
    }

    public List<Client> findAll() {
        return myCache.findAll();
    }

    public void saveAll(List<Client> clientList) {
        if(myCache.getMaxSize() < clientList.size()) {
            myCache.clearCache();
            clientList.forEach(client -> myCache.put(client.getId(),client));
        }
    }
}
