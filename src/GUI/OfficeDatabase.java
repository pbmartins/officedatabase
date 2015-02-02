package GUI;

import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;
/**
 *
 * @author pedromartins
 */
public class OfficeDatabase {
    
    static Scanner sc = new Scanner (System.in);
    
    public static void main(String[] args) throws IOException {
        
        loginWindow LoginWindow = new loginWindow();
        LoginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoginWindow.setSize(400,150);
        LoginWindow.setVisible(true);
        LoginWindow.setResizable(false);
        LoginWindow.setLocationRelativeTo(null);

    }

    
}