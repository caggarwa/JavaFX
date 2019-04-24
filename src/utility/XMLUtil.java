package utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLUtil {

	private String filePath;
	private Document doc = null;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		if (!file.exists())
			throw new FileNotFoundException();

		this.filePath = filePath;
	}

	public Document getDoc() {
		return doc;
	}

	/**
	 * This method is to load XMl to given filepath
	 */
	public void loadXML() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		if (filePath != null && !filePath.equals("")) {
			factory.setNamespaceAware(true);
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.parse(filePath);
				Log.info("XMl Document object initialized for the file " + filePath);
			} catch (Exception e) {
				Log.error("Exception occured while loading the xml file " + filePath);
				Log.error(e.toString());

			}

		} else {
			Log.error("XML File Path is either null or blank");
		}
	}

	/**
	 * This methos is to get xpath value of XML
	 * 
	 * @param tagXPath
	 *            : Xpath of tag
	 * @return : return single String value
	 */
	public String getXpathValue(String tagXPath) {

		XPathExpression expr;
		String val = "";
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			expr = xpath.compile(tagXPath);
			val = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (Exception e) {
			Log.error("Exception occured while reading the xml tag " + tagXPath);
			Log.error(e.toString());
		}
		return val;
	}

	/**
	 * This method will return tag count for given xpath
	 * 
	 * @param tagXPath
	 * @return
	 */
	public int getTagCount(String tagXPath) {

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		XPathExpression expr;
		NodeList nl = null;
		try {
			expr = xpath.compile(tagXPath);
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (Exception e) {
			Log.error("getTagCount : Exception occured while reading the xml tag " + tagXPath);
			Log.error(e.toString());
		}

		if (nl == null)
			return 0;

		return nl.getLength();
	}

	/**
	 * This method will given xpath value of XML Tags
	 * 
	 * @param tagXPath
	 *            : Xpath of Xml tag
	 * @param isNode
	 *            : True in case wants to retrieve list of xmlTag values
	 * @return
	 */
	public List<String> getXpathValue(String tagXPath, boolean isNode) {

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		List<String> list = new ArrayList<String>();
		XPathExpression expr;
		if (isNode) {
			try {
				expr = xpath.compile(tagXPath);
				NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

				for (int i = 0; i < nl.getLength(); i++) {
					list.add(nl.item(i).getTextContent().trim());
				}

			} catch (Exception e) {
				Log.error("getXpathValue : Exception occured while reading the xml tag " + tagXPath);
				Log.error(e.toString());
			}

		} else {
			Log.info("getXpathValue : Specified Xpath " + tagXPath + " doesn't have any Child Node");
			return null;
		}

		return list;
	}

	public static String getXMLValue(String fileName, String expression) {
		String sXpathvalue = null;

		try {

			Log.info("getXMLValue : Reading XML Value for Xpath: " + expression + " From File: " + fileName);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			InputStream inputStream = new FileInputStream(new File(fileName));
			org.w3c.dom.Document doc = builderFactory.newDocumentBuilder().parse(inputStream);
			StringWriter stw = new StringWriter();
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.transform(new DOMSource(doc), new StreamResult(stw));
			DocumentBuilder builder = null;
			builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(new ByteArrayInputStream(stw.toString().getBytes()));
			XPath xPath = XPathFactory.newInstance().newXPath();
			// String expression =
			// "/Entities/Entity/Fields/Field[@Name='hierarchical-path']/Value";
			// read a string value
			sXpathvalue = xPath.compile(expression).evaluate(xmlDocument);

		} catch (Exception ex) {
			Log.error(" getXMLValue : Exception occured while reading XML Value:" + ex.toString());

		}

		return sXpathvalue;

	}
	
	

}
