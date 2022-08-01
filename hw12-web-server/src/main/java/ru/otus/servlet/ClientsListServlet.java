package ru.otus.servlet;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.config.Htmls;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.service.TemplateProcessor;

public class ClientsListServlet extends HttpServlet {

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient serviceClient;

    public ClientsListServlet(TemplateProcessor templateProcessor, DBServiceClient serviceClient) {
        this.templateProcessor = templateProcessor;
        this.serviceClient = serviceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var clients = serviceClient.findAll();

        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(Htmls.CLIENTS_LIST, Map.of("clients", clients)));
    }
}
