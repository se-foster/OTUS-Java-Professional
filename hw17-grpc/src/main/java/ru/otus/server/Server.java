package ru.otus.server;

import java.io.IOException;

import io.grpc.ServerBuilder;
import ru.otus.ConnectionParams;
import ru.otus.server.service.ServerNumberService;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {

        var server = ServerBuilder
                .forPort(ConnectionParams.SERVER_PORT)
                .addService(new ServerNumberService())
                .build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
