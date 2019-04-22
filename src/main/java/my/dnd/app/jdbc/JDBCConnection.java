package my.dnd.app.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import my.properties.Ref;

public class JDBCConnection {
	
	private static JDBCConnection instance;
	private static Connection connection;
	private JDBCConnection() {}
	public static JDBCConnection getInstance() {
		if(instance == null) {
			setUpConnection();
		}
		return instance;
	}
	
	private static void setUpConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String username = Ref.getJDBCUser();
			String password = Ref.getJDBCPassword();
			String schema = Ref.getSchema();
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+schema, username, password);
			instance = new JDBCConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
}
