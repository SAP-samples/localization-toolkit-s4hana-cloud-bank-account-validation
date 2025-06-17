
package com.sap.poland.whitelist.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class XMLUtils {

    static class XMLException extends RuntimeException {

        public XMLException(String message) {
            super(message);
        }

        public XMLException(Throwable cause) {
            super(cause);
        }
    }

    /**
     * Returns child elements that have the specified element name.
     *
     * @param parent Parent element
     * @param name Name of the children elements to retrieve.
     * @return List of children. If there's no child with the specified name, an
     * empty list is returned.
     * @throws NullPointerException when any of the parameters is null.
     */
    static List<Element> getChildElements(Element parent, String name) {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(name, "name");

        List<Element> resultSet = new ArrayList<>();
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeName().equals(name) && (child.getNodeType() == Node.ELEMENT_NODE)) {
                resultSet.add((Element) child);
            }
            child = child.getNextSibling();
        }
        return resultSet;
    }

    /**
     * Returns the root element of the xml document contained in the input
     * stream.
     *
     * @param iStream Input stream containing a xml document.
     * @return Root element of the xml document.
     * @throws IOException Error during read.
     * @throws XMLException Other errors, parsing failed, not a xml document
     * etc.
     * @throws NullPointerException when the parameter is null.
     */
    static Element getRootElement(InputStream iStream) throws IOException, XMLException {
        Objects.requireNonNull(iStream, "iStream cannot be null");
        Document document = documentFromStream(iStream);
        return document.getDocumentElement();
    }

    /**
     * Converts a xml document in an in-memory stream into a DOM.
     *
     * @param iStream Input stream containing a xml document.
     * @return DOM representation of the xml document.
     * @throws IOException - Error during read
     * @throws XMLException - Other errors, parsing failed, not a xml document
     * etc.StringAtt
     */
    static Document documentFromStream(InputStream iStream) throws IOException, XMLException {
        try {
            DocumentBuilder domBuilder = newDocumentBuilder();
            Document dom = domBuilder.parse(iStream);
            return dom;
        } catch (SAXException pcEx) {
            throw new XMLException(pcEx);
        }
    }

    /**
     * Creates a new DOM document. 
     * @return new DOM document
     * @throws XMLException Creation of the document failed in the DOM engine.
     */
    static Document newDocument() throws XMLException {
        DocumentBuilder domBuilder = newDocumentBuilder();
        Document document = domBuilder.newDocument();
        document.setXmlStandalone(true);
        return document;
    }
    
    /**
     * Creates a new instance of a document builder
     * @return DOM Document builder.
     * @throws XMLException Creation of the document builder failed. 
     */
    private static DocumentBuilder newDocumentBuilder() throws XMLException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XMLException(e);
        }
    }
    
    /**
     * Create document from a string. 
     * @param xml String containing a valid xml.
     * @return DOM document. 
     * @throws XMLException The xml failed to be parsed. 
     * @throws NullPointerException when the parameter is null. 
     */
    static Document createDocumentFromString(String xml) throws XMLException {
        Objects.requireNonNull(xml, "xml");
        
        try {
            DocumentBuilder builder = newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            return document;
        } catch (XMLException | IOException | SAXException e) {
            throw new XMLException(e);
        }
    }
    
    /**
     * Returns the first child with given name or null. 
     * @param parent Parent element
     * @param childName Name of the child
     * @return Child element or null
     * @throws NullPointerException One of the parameters is null. 
     */
    static Element getFirstChild(Element parent, String childName){
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(childName, "childName");

        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeName().equals(childName) && (child.getNodeType() == Node.ELEMENT_NODE)) {
                return (Element) child;
            }
            child = child.getNextSibling();
        }
        return null;
    }
    
    /**
     * Returns node value of the first child with given name or null. 
     * @param parent Parent element
     * @param childName Name of the child
     * @return Value of the Child element or null
     * @throws NullPointerException One of the parameters is null. 
     */
    static String getValueOfFirstChild(Element parent, String childName){
        Element child = getFirstChild(parent, childName);
        return child == null ? null : child.getTextContent();
    }
    
    
    /**
     * Create a child element and append it into parent. 
     * @param parent Parent element.
     * @param childName Name of the child element
     * @param textContent Element text content. If the value is null or empty, the child will not contain any text value. 
     * @return Newly created element. 
     * @throws XMLException The parent element is not a part of a DOM document. 
     */
    static Element createAppendChildElement(Element parent, String childName, String textContent) throws XMLException{
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(childName, "childName");
        Document document = parent.getOwnerDocument();
        if(document == null) {
            throw new XMLException("Owner document not set.");
        }
        Element child = parent.getOwnerDocument().createElement(childName);
        parent.appendChild(child);
        if(textContent != null && !textContent.isEmpty()){
            child.setTextContent(textContent);
        }
        return child;
    }
    
    /**
     * Renders given document to a string. 
     * @param doc Document to render 
     * @return XML representation of the document. 
     * @throws XMLException Something failed internally during rendering. 
     */
    public static String documentToString(Document doc) throws XMLException {
        try {
            DOMSource domSource = new DOMSource(doc);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult target = new StreamResult(baos);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(domSource, target);
            return baos.toString();
        } catch (TransformerFactoryConfigurationError | IllegalArgumentException | TransformerException ex) {
            throw new XMLException(ex);
        }
   }
    
  

}
