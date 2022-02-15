package server;

import Helper.Data;
import models.ClienteSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Pessoa;

public class servidorChat {

    private final int PORT = 4000;
    private ServerSocket serverSocket;
    private final List<ClienteSocket> clientes;
    private List<Pessoa> pessoasCadastradas;

    public void adicionaPessoa(Pessoa pessoa) {
        pessoasCadastradas.add(pessoa);
    }

    public servidorChat() {
        clientes = new ArrayList<>();
        pessoasCadastradas = new ArrayList<>();
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor de chat iniciado em: " + serverSocket.getInetAddress().getHostAddress() + " e porta " + PORT);

        loopConexao();
    }

    public void loopConexao() throws IOException {
        while (!serverSocket.isClosed()) {
            System.out.println("Aguardando conexão de novo cliente");
            final ClienteSocket clienteSocket;

            clienteSocket = new ClienteSocket(serverSocket.accept());
            System.out.println("Cliente " + clienteSocket.getRemoteSocketAdress() + " conectado");

            Thread mensagens = new Thread(() -> {
                try {
                    loopMensagem(clienteSocket);
                } catch (IOException ex) {
                    Logger.getLogger(servidorChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            mensagens.start();
            clientes.add(clienteSocket);
        }
    }

    private void loopMensagem(ClienteSocket clienteSocket) throws IOException {
        String msg;
        while ((msg = clienteSocket.getMessage()) != null) {
            System.out.println("Mensagem recebida do cliente " + clienteSocket.getRemoteSocketAdress()+ ": " + msg);
            Boolean entrou = false;
            switch (msg.split("/")[0]) {
                case "Saiu":
                    for (Pessoa umaPessoa : pessoasCadastradas) {
                        System.out.println(umaPessoa.getPORT() == Integer.parseInt(msg.split("/")[1]));
                        if (umaPessoa.getPORT() == Integer.parseInt(msg.split("/")[1])) {
                            String nome = umaPessoa.getNome();
                            mandaMensagemTodos(nome + " saiu do servidor", umaPessoa.getPORT());
                            Socket socket = new Socket(Data.IP_SERVIDOR, umaPessoa.getPORT());
                            ClienteSocket clienteSocketRes = new ClienteSocket(socket);
                            pessoasCadastradas.remove(umaPessoa);
                            clienteSocketRes.close();
                            break;
                        }
                    }
                    if (pessoasCadastradas.isEmpty()) {
                        return;
                    }
                    break;
                case "Porta": {
                    Pessoa pessoa = new Pessoa("ApenasPorta", "ApenasPorta", Integer.parseInt(msg.split("/")[1]));
                    pessoasCadastradas.add(pessoa);
                    break;
                }
                case "Login":
                    for (Pessoa umaPessoa : pessoasCadastradas) {
                        if (msg.split("/")[1].equals(umaPessoa.getNome()) && msg.split("/")[2].equals(umaPessoa.getTelefone()) && umaPessoa.getPORT() == Integer.parseInt(msg.split("/")[3])) {
                            Socket socket = new Socket(Data.IP_SERVIDOR, umaPessoa.getPORT());
                            ClienteSocket clienteSocketRes = new ClienteSocket(socket);
                            // envia mensagem de volta
                            clienteSocketRes.enviarMensagem("Você foi logado com sucesso!/" + umaPessoa.getPORT());

                            entrou = true;
                            break;
                        }
                    }
                    if (entrou == false) {
                        Socket socket = new Socket(Data.IP_SERVIDOR, Integer.parseInt(msg.split("/")[3]));
                        ClienteSocket clienteSocketRes = new ClienteSocket(socket);
                        mandaMensagemParaCliente(clienteSocketRes, "Erro:Nenhum usuário foi encontrado com esse nome e telefone.");
                    }
                    break;
                case "Cadastro": {
                    Pessoa pessoa = new Pessoa(msg.split("/")[1], msg.split("/")[2], Integer.parseInt(msg.split("/")[3]));
                    for (Pessoa umaPessoa : pessoasCadastradas) {
                        if (umaPessoa.getPORT() == Integer.parseInt(msg.split("/")[3])) {
                            umaPessoa.setNome(pessoa.getNome());
                            umaPessoa.setTelefone(pessoa.getTelefone());
                        }
                    }       
                    //trata ip
                    Socket socket = new Socket(Data.IP_SERVIDOR, Integer.parseInt(msg.split("/")[3]));
                    ClienteSocket clienteSocketRes = new ClienteSocket(socket);
                    // envia mensagem de volta
                    mandaMensagemParaCliente(clienteSocketRes, "Você foi cadastrado com sucesso!");
                    break;
                }
                default:
                    mandaMensagemTodos(msg, Integer.parseInt(msg.split("/")[1]));
                    break;
            }
        }
    }

    public static void main(String[] args) {
        final servidorChat servidor = new servidorChat();
        try {
            servidor.start();

        } catch (IOException ex) {
            System.out.println("Erro no servidor: " + ex.getMessage());
        }
    }

    public void mandaMensagemParaCliente(final ClienteSocket socket, final String msg) {
        socket.enviarMensagem(msg);
    }

    public String formataString(String msg, int PORT) {
        String formatada = "";
        for (Pessoa umaPessoa : pessoasCadastradas) {
            if (umaPessoa.getPORT() == PORT) {
                formatada = umaPessoa.getNome() + ": " + msg;
            }
        }
        return formatada;
    }

    private void mandaMensagemTodos(String msg, int PORT) {
        int cont = 0;
        if (pessoasCadastradas.size() != 0) {
            String msgFormatada = formataString(msg, PORT);
            for (Pessoa umaPessoa : pessoasCadastradas) {
                try {
                    if (umaPessoa.getPORT() != PORT) {
                        Socket socket = new Socket(Data.IP_SERVIDOR, umaPessoa.getPORT());
                        ClienteSocket clienteSocketRes = new ClienteSocket(socket);
                        // envia mensagem de volta
                        mandaMensagemParaCliente(clienteSocketRes, msgFormatada);
                        cont++;
                        clienteSocketRes.close();
                    }
                } catch (IOException ex) {
                    System.out.println("Não foi possível enviar a mensagem!");
                }
            }
            System.out.println("Mensagem enviada para " + cont + " pessoas.");
        }
    }
}
