package DBConnection;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DBcon extends Config{
	Connection con=null;
	public  Connection dbcon() throws SQLException 
	{
		String path="jdbc:mysql://localhost:3306/"+Config.db;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=(Connection) DriverManager.getConnection(path, Config.dbuser, Config.dbpass);
		} 
		catch (ClassNotFoundException | SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return con;
	}


}
