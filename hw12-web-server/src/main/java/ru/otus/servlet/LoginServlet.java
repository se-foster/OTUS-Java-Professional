package ru.otus.servlet;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.config.Htmls;
import ru.otus.config.Urls;
import ru.otus.service.TemplateProcessor;
import ru.otus.service.UserAuthService;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;

    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(Htmls.LOGIN, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect(Urls.MENU);
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }
    }
}
