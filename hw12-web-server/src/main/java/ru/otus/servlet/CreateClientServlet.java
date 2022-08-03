package ru.otus.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.config.Htmls;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.service.TemplateProcessor;

public class CreateClientServlet extends HttpServlet {

    private final DBServiceClient serviceClient;
    private final TemplateProcessor templateProcessor;

    public CreateClientServlet(TemplateProcessor templateProcessor, DBServiceClient serviceClient) {
        this.templateProcessor = templateProcessor;
        this.serviceClient = serviceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(Htmls.CREATE_CLIENT, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("newName");
        String street = req.getParameter("street");
        String[] phones = req.getParameterValues("phone");

        saveClient(name, street, phones);

        resp.sendRedirect("/menu");
    }

    private void saveClient(String name, String street, String[] phones) {
        var address = new Address(null, street);
        List<Phone> phoneList = getPhoneList(phones);
        var client = new Client(null, name, address, phoneList);
        serviceClient.saveClient(client);
    }

    private List<Phone> getPhoneList(String[] phones) {
        return Arrays.stream(phones)
                .filter(phone -> !phone.equals(""))
                .map(phone -> new Phone(null, phone))
                .toList();
    }
}
