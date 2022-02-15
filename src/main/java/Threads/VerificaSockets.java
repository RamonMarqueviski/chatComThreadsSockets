/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import View.Chat;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import models.ClienteSocket;

/**
 *
 * @author ramom
 */
public class VerificaSockets extends Thread {

    private ClienteSocket clienteSocket;
    private final ServerSocket serverSocket;
    private Chat tela;

    public VerificaSockets(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        while (true) {
            //Tenta conectar com o servidor
            System.out.println("Aguardando conexão de novo cliente");
            try (Socket socket = serverSocket.accept()) {
                try {
                    clienteSocket = new ClienteSocket(socket);
                    
                    System.out.println("Cliente " + clienteSocket.getRemoteSocketAdress() + " conectado");
                   
                    mensagemLoop(clienteSocket);
                } catch (IOException ex) {
                    System.err.println("Erro ao aceitar conexão.");
                }

            } catch (IOException ex) {
               System.err.println("Erro ao aceitar conexão.");
            }
        }
    }

    private void mensagemLoop(ClienteSocket clienteSocket) {
        try {
            String msg = clienteSocket.getMessage();
            System.out.println("Mensagem recebida do cliente" + clienteSocket.getRemoteSocketAdress()+ ": " + msg);
            if (msg != null) {
                if (msg.split("/")[0].equals("saiu")) {
                    this.tela.taChat.append("Saiu");
                    serverSocket.close();
                    this.stop();
                    //Quando receber a mensagem: "saiu", ele fecha o servidor e também corta a thread
                    
                } else if (msg.split("/")[0].equals("Você foi logado com sucesso!")) {
                    JOptionPane.showMessageDialog(null, "Bem vindo!");
                    this.tela = new Chat(Integer.parseInt(msg.split("/")[1]));
                    tela.setVisible(true);
                    
                    //Autenticacao de login
                } else if (msg.equals("Você foi cadastrado com sucesso!")) {
                    JOptionPane.showMessageDialog(null, "Você foi cadastrado com sucesso!");
                    //Cadastro
                } else {
                    if (!msg.split(":")[0].equals("Erro")) {
                        tela.taChat.append(msg.split("/")[0] + "\n");
                        //Mensagem de chat
                    } else {
                        JOptionPane.showMessageDialog(null, msg);
                        //Mensagem de erro
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(VerificaSockets.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException ex) {
                System.out.println("Erro: Erro ao fechar socket");
            }
        }
    }
}
