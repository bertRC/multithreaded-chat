package ru.itpark.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) {
        int count = 0;
        try (ServerSocket serverSocket = new ServerSocket(9876)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                System.out.println(count++);
                new Thread(() -> {
                    try (
                            final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    ) {
                        // echo
                        String line;
                        while ((line = reader.readLine()) != null) {
                            writer.write(line);
                            writer.newLine();
                            writer.flush();
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
