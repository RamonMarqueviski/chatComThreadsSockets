import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import server.servidorChat;

public class clienteChat {
    private Scanner scanner;
    private Socket clienteSocket;
    private final String SERVER_ADRESS = "127.0.0.1";
    private final int PORT = 4000;
    private PrintWriter out;

    public clienteChat(){
        scanner = new Scanner(System.in);
    }
    public void start() throws IOException{
        clienteSocket = new Socket(SERVER_ADRESS,PORT);
        
      this.out = new PrintWriter(clienteSocket.getOutputStream(),true);
        
        System.out.println("O cliente foi iniciado no servidor: " + SERVER_ADRESS + ":" + PORT);
        messageLoop();
    }
    public void messageLoop() throws IOException{
        String mensagem;
        do{
            System.out.println("Digite uma mensagem ou Exit para finalizar a conexão");
            mensagem = scanner.next();
            out.println(mensagem);
        }while(!mensagem.equalsIgnoreCase("exit"));
    }
 
    public static void main(String[] args) {
        try {
            clienteChat clienteChat = new clienteChat();
            clienteChat.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar cliente: " + ex.getMessage());
        }
        System.out.println("Encerrado.");
    }
}