package XMLReader_Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Runner {

	static Connection getConnection(String url, String user, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			String url = "jdbc:mysql://localhost:3306/pupildb";
			String user = "mosi";
			String password = "3105seno";
			String tablename = "pupil";
			String pathname = "C:\\Temp\\XMLReadWrite\\" ;
			Connection c = getConnection(url, user, password);

			dropTable(c,tablename);
			XMLReader.createTable(c,tablename);
			XMLReader.readXML(c,pathname+"in.xml", tablename);
			XMLWriter.writeXML(c,pathname+"out.xml", tablename);

			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void dropTable(Connection c, String tableName) {
		try {
			Statement stmt = c.createStatement();
			String sql = "drop table if exists " + tableName + ";";
			System.out.println("Table " + tableName + " wurde gelöscht!");
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

