package XMLReader_Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  

public class XMLReader {		
	static void createTable(Connection c, String tableName) {
		try {
			Statement stmt = c.createStatement();
			String sql = "create table if not exists " + tableName + " ("+tableName+"id int auto_increment, vornachname varchar(50), age int, primary key("+tableName+"id));";
			stmt.executeUpdate(sql);
			System.out.println("Table " + tableName + " wurde erstellt!");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void readXML(Connection c, String filename, String tablename) {
		try {
			File f = new File(filename);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(f);  
			doc.getDocumentElement().normalize();  
			NodeList nodeList = doc.getElementsByTagName("pupil"); 
			for (int itr = 0; itr < nodeList.getLength(); itr++)   
			{  
				Node node = nodeList.item(itr);  
				System.out.println("\nNode Name :" + node.getNodeName());  
				if (node.getNodeType() == Node.ELEMENT_NODE)   
				{  
					Element eElement = (Element) node;
					String sql = "insert into " + tablename + " (vornachname, age) values (?, ?);";
					PreparedStatement stmt = c.prepareStatement(sql);

					stmt.setString(1, eElement.getElementsByTagName("vornachname").item(0).getTextContent());
					stmt.setLong(2, Long.parseLong(eElement.getElementsByTagName("age").item(0).getTextContent()));
					stmt.executeUpdate();
					stmt.close();
					System.out.println("Datensatz wurde eingefügt.");
				}  
			}  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Konnte Datei "+filename+" nicht öffnen!");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
