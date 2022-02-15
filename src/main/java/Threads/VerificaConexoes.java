/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import models.ClienteSocket;
import Helper.Data;
import java.net.InetAddress;


/**
 *
 * @author ramom
 */
public class VerificaConexoes extends Thread {

    public int PORT;
    private ServerSocket serverSocket;

    public VerificaConexoes(ServerSocket serverSocket, int PORT) {
        this.serverSocket = serverSocket;
        this.PORT = PORT;
    }

    public int getPORT() {
        return PORT;
    }

    @Override
    public void run() {
        start();
    }

    public void start() {
        try {
             InetAddress addr = InetAddress.getByName("127.0.0.1");
            serverSocket = new ServerSocket(PORT,40,addr);
            System.out.println("O Servidor foi iniciado no ip - " + serverSocket.getInetAddress().getHostAddress() + " com a porta " + PORT);

            mandaMensagemParaSalvarPorta();

            loopConexao();

        } catch (IOException ex) {
            System.err.println("Erro ao iniciar servidor na porta: " + PORT);
            //Se a porta já está sendo inciada, ele tenta novamente.
            setPORT(PORT + 1);
            start();
        }
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }


    public void loopConexao() throws IOException {
        VerificaSockets verificador = new VerificaSockets(serverSocket);
        verificador.start();
    }

    public void mandaMensagemParaSalvarPorta() {
        try {
            Socket socket = new Socket(Data.IP_SERVIDOR, Data.PORTA_SERVIDOR);
            ClienteSocket clienteSocket = new ClienteSocket(socket);
            clienteSocket.enviarMensagem("Porta/" + PORT);
            clienteSocket.close();
        } catch (IOException ex) {
            System.err.println("Ocorreu um erro ao tentar conectar com o servidor.");
        }
    }

}
