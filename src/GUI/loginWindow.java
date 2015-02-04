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
import java.sql.*;
import org.sqlite.*;

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
@SuppressWarnings("unused")
public class loginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel UserLabel;
	private JLabel PassLabel;
	private JTextField UserField;
	private JPasswordField PassField;
	private JButton LoginButton;
	
	Connection connection = null;

	public loginWindow() throws IOException {
		super("Office Database - Login");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(400,150));
        setResizable(false);
        setLocationRelativeTo(null);

		//Load Login Database
		connection = sqlConnection.dbConnector();

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
			try {
				String query = "select * from login where username=? and password=?";
				PreparedStatement pst = connection.prepareStatement(query);
				pst.setString(1, UserField.getText());
				pst.setString(2, new String(PassField.getPassword()));
				
				ResultSet rs = pst.executeQuery();
				int c=0;
				
				while(rs.next()==true) {
					c++;
				}
				
				if(c==1) {
					setVisible(false);
  					window newWindow;
					try {
						newWindow = new window();
						newWindow.setVisible(true);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				} else JOptionPane.showMessageDialog(null, "O username e/ou password estão errados!");
				rs.close();
				pst.close();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, e1);
			}
            
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
			try {
				String query = "select * from login where username=? and password=?";
				PreparedStatement pst = connection.prepareStatement(query);
				pst.setString(1, UserField.getText());
				pst.setString(2, new String(PassField.getPassword()));
				
				ResultSet rs = pst.executeQuery();
				int c=0;
				
				while(rs.next()==true) {
					c++;
				}
				
				if(c==1) {
					setVisible(false);
  					window newWindow;
					try {
						newWindow = new window();
						newWindow.setVisible(true);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				} else JOptionPane.showMessageDialog(null, "O username e/ou password estão errados!");
				rs.close();
				pst.close();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, e1);
			}
			
		}
		
	}


}