package GUI;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.sqlite.*;
/**
 *
 * @author pedromartins
 */
@SuppressWarnings("unused")
public class OfficeDatabase {
    
    static Scanner sc = new Scanner (System.in);
    
    public static void main(String[] args) throws IOException {
        
        SwingUtilities.invokeLater(new Runnable() {
        	public void run() {
        		try {
					loginWindow newLoginWindow = new loginWindow();
					newLoginWindow.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        });
        
        

    }

    
}