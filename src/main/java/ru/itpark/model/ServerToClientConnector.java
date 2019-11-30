package ru.itpark.model;

import java.io.*;
import java.net.Socket;

public class ServerToClientConnector {
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public ServerToClientConnector(Socket socket) throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public synchronized void send(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }
}
