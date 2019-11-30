package ru.itpark.client;

import ru.itpark.model.ServerToClientLinker;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        try (
                final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                final Socket socket = new Socket("localhost", 9876);
        ) {
            final ServerToClientLinker linker = new ServerToClientLinker(socket);

            System.out.print("Enter your name please: ");
            final String name = consoleReader.readLine();

            linker.send(name + " has joined.");

            new Thread(() -> {
                String message;
                try {
                    while ((message = consoleReader.readLine()) != null) {
                        linker.send(name + ": " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String line;
            while ((line = linker.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
