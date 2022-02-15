/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import Helper.Data;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.ClienteSocket;

/**
 *
 * @author ramom
 */
public class CadastroController {

    public void Cadastrar(String nome, String telefone,int PORT) {
        try {
            Socket socket = new Socket(Data.IP_SERVIDOR, Data.PORTA_SERVIDOR);
            ClienteSocket clienteSocket = new ClienteSocket(socket);
            clienteSocket.enviarMensagem("Cadastro/" + nome + "/" + telefone + "/" + PORT);
        } catch (IOException ex) {
            Logger.getLogger(CadastroController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
