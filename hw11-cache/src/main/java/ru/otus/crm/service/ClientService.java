package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.model.Client;

public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    public void work(DBServiceClient dbServiceClient) {
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        for (int i = 0; i < 10; i++) {
            dbServiceClient.saveClient(new Client("dbServiceSecond"));
        }
        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));

        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
