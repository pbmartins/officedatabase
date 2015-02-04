package GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(400,150));
        setResizable(false);
        setLocationRelativeTo(null);

		//Load Login Database
		loginDatabase = loadLogin();

		//Structure
		enterHandler enter_handler = new enterHandler();
		
		UserLabel = new JLabel("Utilizador:");
		add(UserLabel);
		UserField = new JTextField("",25);
		UserField.addActionListener(enter_handler);
		add(UserField);

		PassLabel = new JLabel("Password:");
		add(PassLabel);
		PassField = new JPasswordField("",25);
		PassField.addActionListener(enter_handler);
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
  					window newWindow;
					try {
						newWindow = new window();
						newWindow.setVisible(true);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
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
	
	private class enterHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			login credentials = new login();
  			credentials.user = UserField.getText();
  			credentials.pass = new String(PassField.getPassword());
  			int c=0;

  			for (int i=0; i<loginDatabase.length; i++) {
  				if (loginDatabase[i].user.equals(credentials.user) && loginDatabase[i].pass.equals(credentials.pass)) {
  					setVisible(false);
  					window newWindow;
					try {
						newWindow = new window();
						newWindow.setVisible(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
  					c++;
  					break;
  				}
  			}

  			if (c==0) JOptionPane.showMessageDialog(null, "O username e/ou password estão errados!");
			
		}
		
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