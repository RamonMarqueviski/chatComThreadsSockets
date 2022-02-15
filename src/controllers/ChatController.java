/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import Helper.Data;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import models.ClienteSocket;

/**
 *
 * @author ramom
 */
public class ChatController {
    public void sair(int PORT){
        try {
            Socket socket = new Socket(Data.IP_SERVIDOR, Data.PORTA_SERVIDOR);
            ClienteSocket clienteSocket = new ClienteSocket(socket);
            clienteSocket.enviarMensagem("Saiu" + "/" + PORT);
            
            System.exit(0);
        } catch (IOException ex) {
            System.err.println("Ocorreu um erro ao tentar enviar a mensagem ao servidor.");
        }
    }
    
    public void enviarMensagem(String msg,JTextArea taChat,int PORT,JTextField tfMsg){
        try {
            taChat.append("Você: " + msg + "\n");
            Socket socket = new Socket(Data.IP_SERVIDOR, Data.PORTA_SERVIDOR);
            ClienteSocket clienteSocket = new ClienteSocket(socket);
            clienteSocket.enviarMensagem(msg + "/" + PORT);

            tfMsg.setText(null);
        } catch (IOException ex) {
            System.out.println("Ocorreu um erro ao enviar mensagem!");
        }
    }
}
