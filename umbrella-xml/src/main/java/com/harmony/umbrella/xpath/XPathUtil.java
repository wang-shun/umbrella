/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.harmony.umbrella.xpath;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.harmony.umbrella.util.Assert;

/**
 * @author wuxii@foxmail.com
 */
public class XPathUtil {

    private Document document;

    private XPathFactory xPathFactory;

    private XPath xpath;

    public XPathUtil(Document document) {
        Assert.notNull(document, "document must not be null");
        this.document = document;
        this.xPathFactory = XPathFactory.newInstance();
        this.xpath = xPathFactory.newXPath();
    }

    public String getAttribute(String expression) throws XPathException {
        Object evaluate = xpath.evaluate(expression, document, XPathConstants.NODE);
        System.out.println(evaluate);
        return null;
    }

    public Map<String, String> getAttributes(String expression) throws XPathException {
        Map<String, String> attributes = new HashMap<String, String>();
        Element element = getElement(expression);
        NamedNodeMap nnm = element.getAttributes();
        for (int i = 0, max = nnm.getLength(); i < max; i++) {
            Node item = nnm.item(i);
            attributes.put(item.getNodeName(), item.getNodeValue());
        }
        return attributes;
    }

    public Element getElement(String expression) throws XPathException {
        Object obj = xpath.evaluate(expression, document, XPathConstants.NODE);
        if (obj instanceof Element) {
            return (Element) obj;
        }
        throw new XPathException(expression + " is not element path expression");
    }

    public Element[] getElements(String expression) throws XPathException {
        Object obj = xpath.evaluate(expression, document, XPathConstants.NODESET);
        if (obj instanceof NodeList) {
            NodeList nodeList = ((NodeList) obj);
            Element[] result = new Element[nodeList.getLength()];
            for (int i = 0, max = nodeList.getLength(); i < max; i++) {
                result[i] = (Element) nodeList.item(i);
            }
            return result;
        }
        throw new XPathException(expression + " is not elements path expression");
    }
}
