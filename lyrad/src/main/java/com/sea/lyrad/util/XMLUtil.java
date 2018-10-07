package com.sea.lyrad.util;

import org.dom4j.Element;

import java.util.Iterator;

/**
 * XML工具
 */
public class XMLUtil {
    private XMLUtil() {
    }

    /**
     * 获取表结点
     *
     * @param root 根结点
     * @param name 表名
     * @return 表结点元素 or null
     */
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
