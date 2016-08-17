package org.quickjedis.core;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlHelper {

	public static String GetNodeAttr(Node xmlNode, String attrName) {
		NamedNodeMap namedNodeMap = xmlNode.getAttributes();
		Node node = namedNodeMap.getNamedItem(attrName);
		return node.getNodeValue();
	}

	public static List<Node> GetXmlNodes(Node xmlNode, String nodeName) {
		List<Node> nodes = new ArrayList<Node>();
		NodeList nodeLst = xmlNode.getChildNodes();
		int len = nodeLst.getLength();
		for (int i = 0; i < len; i++) {
			Node node = nodeLst.item(i);
			if (node.getNodeName().toLowerCase() == nodeName.toLowerCase())
				nodes.add(node);
		}
		return nodes;
	}

	public static Node GetXmlNode(Node xmlNode, String nodeName) {
		NodeList nodeLst = xmlNode.getChildNodes();
		int len = nodeLst.getLength();
		for (int i = 0; i < len; i++) {
			Node node = nodeLst.item(i);
			if (node.getNodeName().toLowerCase() == nodeName.toLowerCase())
				return node;
		}
		return null;
	}

	public static Node GetXmlNodeFromFile(String fileName, String NodeLocalName) throws Exception {
		File file = new File(fileName);
		if (!file.exists())
			return (Node) null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document xmlDocument = db.parse(new FileInputStream(file));
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		String xpathStr = NodeLocalName;
		return (Node) xpath.evaluate(xpathStr, xmlDocument, XPathConstants.NODE);
	}
}
