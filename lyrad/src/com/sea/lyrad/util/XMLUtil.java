package com.sea.lyrad.util;

import org.dom4j.Element;

import java.util.Iterator;

public class XMLUtil {
    private XMLUtil() {
    }

    public static Element getTableElement(Element root, String name) {
        for (Iterator<Element> it = root.elementIterator("table"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("name").equals(name)) {
                return element;
            }
        }
        return null;
    }
}
