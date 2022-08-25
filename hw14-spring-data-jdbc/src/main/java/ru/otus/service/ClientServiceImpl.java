package ru.otus.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.dao.repository.ClientRepository;
import ru.otus.dao.sessionmanager.TransactionManager;
import ru.otus.model.Client;

@Service
public class ClientServiceImpl implements ClientService {

    private final TransactionManager transactionManager;
    private final ClientRepository repository;

    public ClientServiceImpl(TransactionManager transactionManager, ClientRepository repository) {
        this.transactionManager = transactionManager;
        this.repository = repository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> repository.save(client));
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(repository::findAll);
    }
}
