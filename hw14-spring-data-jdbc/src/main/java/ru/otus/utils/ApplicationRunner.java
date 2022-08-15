package ru.otus.utils;

import ru.otus.config.DbConfig;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.server.WebServer;
import ru.otus.server.WebServerWithFilterBasedSecurity;
import ru.otus.service.TemplateProcessor;
import ru.otus.service.TemplateProcessorImpl;
import ru.otus.service.UserAuthService;
import ru.otus.service.UserAuthServiceImpl;

public class ApplicationRunner {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public void run() throws Exception {
        UserDao userDao = new InMemoryUserDao();
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        DbConfig config = new DbConfig();
        DBServiceClient serviceClient = config.getServiceClient();

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        WebServer webServer = new WebServerWithFilterBasedSecurity(WEB_SERVER_PORT, serviceClient, templateProcessor, authService);

        webServer.start();
        webServer.join();
    }
}
