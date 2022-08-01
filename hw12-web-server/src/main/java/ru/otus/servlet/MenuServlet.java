package ru.otus.servlet;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.config.Htmls;
import ru.otus.service.TemplateProcessor;

public class MenuServlet extends HttpServlet {

    private final TemplateProcessor templateProcessor;

    public MenuServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(Htmls.MENU, Collections.emptyMap()));
    }
}
