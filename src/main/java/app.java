import java.io.IOException;

import javax.swing.JOptionPane;

import View.Home;

public class app {
    public static void main(String[] args) {
        try {
            Home home = new Home();
            home.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu algum erro ao conectar com o servidor!");
        }
    }

}
