
package models;


public class Pessoa {

    private String nome;
    private String telefone;
    private int PORT;

    public Pessoa(String nome, String telefone, int PORT) {
        this.nome = nome;
        this.telefone = telefone;
        this.PORT = PORT;
    }

    public int getPORT() {
        return PORT;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}
