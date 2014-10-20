package com.unionpay.cloudatlas.util.hbase.index.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.unionpay.cloudatlas.util.hbase.index.bean.Index;

public class XMLUtil {

    public static Set<Index> readIndexFromTable(String table) {
        NodeList nodes = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = XMLUtil.class.getClassLoader()
                    .getResourceAsStream("index.xml");
            Document doc = builder.parse(in);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();

            XPathExpression expr = xpath.compile("/indexes/index[@table='"
                    + table + "']");
            nodes = (NodeList) expr.evaluate(doc,
                    XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readIndexFromNodes(nodes);
    }

    private static Set<Index> readIndexFromNodes(NodeList nodes) {
        Set<Index> set = new HashSet<Index>();
        List<String> list;
        String indexName;
        Index index;
        for (int i = 0; i < nodes.getLength(); i++) {
            list = new ArrayList<String>();
            Node now = nodes.item(i);
            indexName = now.getAttributes().getNamedItem("name")
                    .getNodeValue();
            NodeList columnNodes = now.getChildNodes();
            for (int j = 0; j < columnNodes.getLength(); j++) {
                Node col = columnNodes.item(j);
                if (col != null && col.getNodeType() == Node.ELEMENT_NODE)
                    list.add(col.getAttributes().getNamedItem("qualifier")
                            .getNodeValue());
            }
            index = new Index(indexName, list);
            set.add(index);
        }
        return set;
    }
}
