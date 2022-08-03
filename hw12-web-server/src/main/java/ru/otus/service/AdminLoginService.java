package ru.otus.service;

import java.util.List;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;

public class AdminLoginService extends AbstractLoginService {

    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal userPrincipal) {
        return List.of(new RolePrincipal("admin"));
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        if (login.equals(ADMIN_LOGIN)) {
            return new UserPrincipal(ADMIN_LOGIN, new Password(ADMIN_PASSWORD));
        }
        else return null;
    }
}
