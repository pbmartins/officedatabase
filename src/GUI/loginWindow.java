package GUI;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 *
 * @author pedromartins
 */
public class loginWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel UserLabel;
	private JLabel PassLabel;
	private JTextField UserField;
	private JPasswordField PassField;
	private JButton LoginButton;

	static login loginDatabase[];

	public loginWindow() throws IOException {
		super("Office Database - Login");
		setLayout(new FlowLayout());

		//Load Login Database
		loginDatabase = loadLogin();

		//Structure
		UserLabel = new JLabel("Utilizador:");
		add(UserLabel);
		UserField = new JTextField("",25);
		add(UserField);

		PassLabel = new JLabel("Password:");
		add(PassLabel);
		PassField = new JPasswordField("",25);
		add(PassField);

		LoginButton = new JButton("Login");
		loginHandler login_handler = new loginHandler();
		LoginButton.addMouseListener(login_handler);
		LoginButton.addMouseMotionListener(login_handler);
		add(LoginButton);

	}

	private class loginHandler implements MouseListener, MouseMotionListener {
		public void mouseClicked(MouseEvent event) {
  			login credentials = new login();
  			credentials.user = UserField.getText();
  			credentials.pass = new String(PassField.getPassword());
  			int c=0;

  			for (int i=0; i<loginDatabase.length; i++) {
  				if (loginDatabase[i].user.equals(credentials.user) && loginDatabase[i].pass.equals(credentials.pass)) {
  					setVisible(false);
               try {
                  window MainWindow = new window();
                  MainWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                  MainWindow.setSize(600,400);
                  MainWindow.setVisible(true);
                  MainWindow.setResizable(false);
                  MainWindow.setLocationRelativeTo(null);
               } catch (IOException e) {}
               c++;
  				   break;
  				}
  			}

  			if (c==0) JOptionPane.showMessageDialog(null, "O username e/ou password estão errados!");
            
        }

        public void mousePressed(MouseEvent event) {}

        public void mouseReleased(MouseEvent event) {}

        public void mouseEntered(MouseEvent event) {}

        public void mouseExited(MouseEvent event) {}

        public void mouseMoved(MouseEvent event) {}

        public void mouseDragged(MouseEvent event) {}
	}

	public static login[] loadLogin() throws IOException {
        File database_file = new File("login.txt");
        Scanner sf = new Scanner(database_file);
        int interval = 5, i=0;
        
        login loginDatabase[] = new login[interval];
        login b[];
        
        
        while (sf.hasNextLine()) {
            if (i<interval) {
                login newC = new login();
                newC.user = sf.nextLine();
                newC.pass = sf.nextLine();
                loginDatabase[i] = newC;
                i++;
            } else {
                b = loginDatabase;
                loginDatabase = new login[interval+=interval];
                for (int j=0; j<b.length; j++) {
                    loginDatabase[j] = b[j];
                }
                login newC = new login();
                newC.user = sf.nextLine();
                newC.pass = sf.nextLine();
                loginDatabase[i] = newC;
                i++;
            }
        }
        sf.close();
        
        if (i<interval) {
            b = loginDatabase;
            loginDatabase = new login[i];
            for (int j=0; j<i; j++) {
                loginDatabase[j] = b[j];
            }
        }
        
        return loginDatabase;
    }
}

class login {
	String user;
	String pass;
}