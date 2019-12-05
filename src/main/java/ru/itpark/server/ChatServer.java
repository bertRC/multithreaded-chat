package ru.itpark.server;

import ru.itpark.model.ServerToClientConnector;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatServer {
    public static void main(String[] args) {
        final BlockingQueue<ServerToClientConnector> connectors = new LinkedBlockingQueue<>();
        try (ServerSocket serverSocket = new ServerSocket(9876)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                System.out.println(connectors.size());
                new Thread(() -> {
                    try {
                        ServerToClientConnector connector = new ServerToClientConnector(socket);
                        connectors.add(connector);
                        String line;
                        while ((line = connector.readLine()) != null) {
                            for (ServerToClientConnector otherConnector : connectors) {
                                otherConnector.send(line);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
