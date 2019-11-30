package ru.itpark.client;

import ru.itpark.model.ServerToClientConnector;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        try (
                final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        ) {
            System.out.print("Enter your name please: ");
            final String name = consoleReader.readLine();

            final Socket socket = new Socket("localhost", 9876);
            final ServerToClientConnector connector = new ServerToClientConnector(socket);

            connector.send(name + " has joined.");

            new Thread(() -> {
                String message;
                try {
                    while ((message = consoleReader.readLine()) != null) {
                        connector.send(name + ": " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String line;
            while ((line = connector.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
