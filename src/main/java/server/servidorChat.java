package server;

import Helper.Data;
import models.ClienteSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import models.Pessoa;

public class servidorChat extends Thread {

    private final int PORT = 4000;
    private String ip;
    private ServerSocket serverSocket;
    private final List<ClienteSocket> clientes;
    private List<Pessoa> pessoasCadastradas;

    public void adicionaPessoa(Pessoa pessoa) {
        pessoasCadastradas.add(pessoa);
    }

    public servidorChat(String ip) {
        clientes = new ArrayList<>();
        pessoasCadastradas = new ArrayList<>();
        this.ip = ip;
    }

    @Override
    public void run() {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(ip);
            serverSocket = new ServerSocket(PORT, 40, addr);

            System.out.println("Servidor de chat iniciado em: " + serverSocket.getInetAddress().getHostAddress()
                    + " e porta " + PORT);
          //  JOptionPane.showMessageDialog(null, "Servidor iniciado");
            VerificaConexaoServidor verifica = new VerificaConexaoServidor(serverSocket);
            verifica.start();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
    
    public void fechar() throws IOException{
        System.out.println("Fechando servidor");
        if(serverSocket != null){
        serverSocket.close();
        }
    }
}
