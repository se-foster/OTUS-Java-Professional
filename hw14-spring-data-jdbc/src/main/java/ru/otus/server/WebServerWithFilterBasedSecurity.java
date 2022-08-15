package ru.otus.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.config.Urls;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.service.TemplateProcessor;
import ru.otus.service.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;

import java.util.Arrays;

public class WebServerWithFilterBasedSecurity extends WebServerSimple {
    private final UserAuthService authService;

    public WebServerWithFilterBasedSecurity(int port, DBServiceClient dbServiceClient, TemplateProcessor templateProcessor,
                                            UserAuthService authService) {
        super(port, dbServiceClient, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), Urls.LOGIN);
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
