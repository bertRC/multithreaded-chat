package ru.itpark.model;

import java.io.*;
import java.net.Socket;

public class ServerToClientLinker {
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public ServerToClientLinker(Socket socket) throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void send(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readline() throws IOException {
        return reader.readLine();
    }
}
