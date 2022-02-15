package controllers;

import Helper.Data;
import java.io.IOException;
import java.net.Socket;
import models.ClienteSocket;

public class LoginController {

    public void login(String nome,String telefone,int PORT) throws IOException {
        Socket socket = new Socket(Data.IP_SERVIDOR, Data.PORTA_SERVIDOR);
        ClienteSocket clienteSocket = new ClienteSocket(socket);
        
        clienteSocket.enviarMensagem("Login/" + nome + "/" + telefone + "/" + PORT);
    }
}
