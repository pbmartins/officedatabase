package GUI;

import java.io.IOException;
import java.util.Scanner;
import javax.swing.SwingUtilities;
/**
 *
 * @author pedromartins
 */
public class OfficeDatabase {
    
    static Scanner sc = new Scanner (System.in);
    
    public static void main(String[] args) throws IOException {
        
        SwingUtilities.invokeLater(new Runnable() {
        	public void run() {
        		try {
					new loginWindow();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        });
        

    }

    
}