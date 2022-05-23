package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LoggingProxy {

    private LoggingProxy() {
    }

    public static Logging getProxy(Logging loggingImpl) {
        return (Logging) Proxy.newProxyInstance(
                Logging.class.getClassLoader(),
                new Class[]{Logging.class},
                new LoggingInvocationHandler(loggingImpl));
    }

    static class LoggingInvocationHandler implements InvocationHandler {
        private final Logging logging;
        public LoggingInvocationHandler(Logging loggingImpl) {
            this.logging = loggingImpl;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isAnnotationPresent(Log.class)) {
                StringBuilder params = new StringBuilder();
                if (args.length == 1) {
                    params.append(", param: ");
                } else if (args.length > 1) {
                    params.append(", params: ");
                }
                for (int i = 0; i < args.length; i++) {
                    if (i > 0) {
                        params.append(", ");
                    }
                    params.append(args[i]);
                }
                System.out.println("executed method: " + method.getName() + params);
            }
            return method.invoke(logging, args);
        }
    }
}
