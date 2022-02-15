package models;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

public class ClienteSocket {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClienteSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(
                socket.getOutputStream(), true);
    }
    public String getIP(){
        String a = socket.getInetAddress().toString();
        String aux = a.split("/")[1];
        String aux2 = aux.split(":")[0];
        return aux2;
    }

    public SocketAddress getRemoteSocketAdress() {
        return socket.getRemoteSocketAddress();
    }

    public boolean enviarMensagem(String msg) {
        out.println(msg);

        return !out.checkError();
    }

    public void close() throws IOException {
        try {
            in.close();
            out.close();
            socket.close();
            System.out.println("O socket foi fechado.");
        } catch (IOException e) {
            System.err.println("Erro ao fechar socket: " + e.getMessage());
        }
    }

    public String getMessage() {
        try {
            return in.readLine();
        } catch (IOException erro) {
            return null;
        }
    }
}
