package ru.otus.crm.service;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.EntityGraphUtil;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

public class DBCashedServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(DBCashedServiceClient.class);
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public void run() {
        long startTime = System.currentTimeMillis();
        var transactionManager = getTransaction();
        var clientTemplate = getClientTemplate();
        MyCache<Long, Client> cache = new MyCache<>();
        CacheService cacheService = new CacheServiceImpl(cache);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, cacheService);

        var clientService = new ClientService();
        clientService.work(dbServiceClient);

        logger.info("DBCashedServiceClient.run() finished in {} ms ", (System.currentTimeMillis() - startTime));
    }

    private static TransactionManagerHibernate getTransaction() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        return new TransactionManagerHibernate(sessionFactory);
    }

    private static DataTemplateHibernate<Client> getClientTemplate() {
        var entityGraphUtil = new EntityGraphUtil<>(Client.class);
        return new DataTemplateHibernate<>(Client.class, entityGraphUtil);
    }
}
