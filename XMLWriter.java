package XMLReader_Writer;

import java.io.File;
import java.sql.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriter {

	static void writeXML(Connection c, String filename, String tablename) {

		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			Element root = document.createElement("pupillist");
			document.appendChild(root);

			Element pupil = document.createElement("pupil");

			Statement stmt = c.createStatement();
			String sql = "select "+tablename+"id, vornachname, age from "+tablename+";";
			ResultSet rs = stmt.executeQuery(sql);



			while (rs.next()) {
				System.out.println(rs.getString("vornachname"));
				root.appendChild(pupil);
				Element tableid = document.createElement(tablename+"id");
				tableid.appendChild(document.createTextNode(String.valueOf(rs.getLong(tablename+"id"))));
				pupil.appendChild(tableid);
				Element vornachname = document.createElement("vornachname");
				vornachname.appendChild(document.createTextNode(rs.getString("vornachname")));
				pupil.appendChild(vornachname);
				Element age = document.createElement("age");
				age.appendChild(document.createTextNode(String.valueOf(rs.getLong("age"))));
				pupil.appendChild(age);
			}
			rs.close();
			stmt.close();


			// create the xml file
			//transform the DOM Object to an XML File
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(filename));

			transformer.transform(domSource, streamResult);

			System.out.println("XML Datei "+filename+" geschrieben.");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}