package my.dnd.app.jdbc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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
			Properties prop = new Properties();
			URL url = JDBCConnection.class.getClassLoader().getResource("config.properties");
			System.out.println(url);
			prop.load(new FileInputStream(url.getFile()));
			String username = (String) prop.get("jdbcuser");
			String password = (String) prop.get("jdbcpassword");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dnddb", username, password);
			instance = new JDBCConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
}
