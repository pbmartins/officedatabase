package GUI;

import java.sql.*;
import javax.swing.*;
import org.sqlite.*;

@SuppressWarnings("unused")

public class sqlConnection {
	
	Connection conn = null;
	
	public static Connection dbConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/pedromartins/Documents/Projects/officedatabase.sqlite");
			JOptionPane.showMessageDialog(null, "Connection successful!");
			
			return conn;
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}
}
