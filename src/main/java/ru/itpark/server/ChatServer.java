package ru.itpark.server;

import ru.itpark.model.ServerToClientLinker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    public static void main(String[] args) {
        int count = 0;
        final Set<ServerToClientLinker> linkers = new HashSet<>();
        try (ServerSocket serverSocket = new ServerSocket(9876)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                System.out.println(count++);
                new Thread(() -> {
                    try {
                        ServerToClientLinker linker = new ServerToClientLinker(socket);
                        linkers.add(linker);
                        String line;
                        while ((line = linker.readLine()) != null) {
                            for (ServerToClientLinker otherLinker : linkers) {
                                otherLinker.send(line);
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
