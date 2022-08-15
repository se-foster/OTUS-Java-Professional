package ru.otus.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.config.Htmls;
import ru.otus.config.Urls;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.service.TemplateProcessor;
import ru.otus.servlet.ClientsListServlet;
import ru.otus.servlet.CreateClientServlet;
import ru.otus.servlet.MenuServlet;

public class WebServerSimple implements WebServer {
    private static final String COMMON_RESOURCES_DIR = "static";

    private final DBServiceClient dbServiceClient;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public WebServerSimple(int port, DBServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, Urls.MENU, Urls.CREATE_CLIENT, Urls.CLIENTS_LIST));

        server.setHandler(handlers);
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{Htmls.MAIN_PAGE});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new MenuServlet(templateProcessor)), Urls.MENU);
        servletContextHandler.addServlet(new ServletHolder(new CreateClientServlet(templateProcessor, dbServiceClient)), Urls.CREATE_CLIENT);
        servletContextHandler.addServlet(new ServletHolder(new ClientsListServlet(templateProcessor, dbServiceClient)), Urls.CLIENTS_LIST);
        return servletContextHandler;
    }
}
