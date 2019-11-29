package ru.itpark.client;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        try (
                final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                final Socket socket = new Socket("localhost", 9876);
                final BufferedWriter chatWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {
            System.out.print("Enter your name please: ");
            final String name = consoleReader.readLine();

            chatWriter.write(name + " has joined.");
            chatWriter.newLine();
            chatWriter.flush();

            final Thread chatReaderThread = new Thread(() -> {
                String line;
                try (final BufferedReader chatReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    while ((line = chatReader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            final Thread chatWriterThread = new Thread(() -> {
                String message;
                try {
                    while ((message = consoleReader.readLine()) != null) {
                        chatWriter.write(name + ": " + message);
                        chatWriter.newLine();
                        chatWriter.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            chatReaderThread.start();
            chatWriterThread.start();

            chatReaderThread.join();
            chatWriterThread.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
