package ru.otus.config;

import org.hibernate.cfg.Configuration;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.EntityGraphUtil;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.CacheService;
import ru.otus.crm.service.CacheServiceImpl;
import ru.otus.crm.service.DbServiceClientImpl;

public class DbConfig {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public DbServiceClientImpl getServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        EntityGraphUtil<Client> entityGraphUtil = new EntityGraphUtil<>(Client.class);
        var clientTemplate = new DataTemplateHibernate<>(Client.class, entityGraphUtil);

        MyCache<Long, Client> cache = new MyCache<>();
        CacheService cacheService = new CacheServiceImpl(cache);

        return new DbServiceClientImpl(transactionManager, clientTemplate, cacheService);
    }
}
