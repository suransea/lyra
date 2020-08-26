package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.db.DatabasePool;
import com.sea.lyrad.db.table.Table;
import com.sea.lyrad.db.table.TableAttribute;
import com.sea.lyrad.util.AESCrypto;
import com.sea.lyrad.util.Log;
import com.sea.lyrad.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 涉及到文件操作时的数据库管理器
 */
public class DBManager {

    private static final String PATH = System.getenv("HOME") + "/.lyra/database";
    private final DatabasePool databasePool;

    private DBManager() {
        databasePool = new DatabasePool();
    }

    /**
     * 获取DBManager的实例
     *
     * @return DBManager对象
     */
    public static DBManager getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * 获取全部数据库的名字列表
     *
     * @return names
     */
    public List<String> getDBNames() {
        File database = new File(PATH);
        List<String> fileNames = new ArrayList<>();
        File[] files = database.listFiles();
        if (files == null) {
            return fileNames;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(".xml")) {
                fileNames.add(fileName.substring(0, fileName.length() - 4));
            }
        }
        return fileNames;
    }

    /**
     * 删除目标数据库
     *
     * @param name 数据库名
     * @throws DBProcessException 目标数据库不存在
     */
    public void deleteDatabase(String name) throws DBProcessException {
        File file = new File(PATH + "/" + name + ".xml");
        if (!file.delete()) {
            throw new DBProcessException("The target database is not exist.");
        }
    }

    /**
     * 将目标数据库对象写入文件，通常是修改后的数据库对象调用此方法与文件同步
     *
     * @param database 目标数据库对象
     * @throws DBProcessException 文件IO异常
     */
    public void write(Database database) throws DBProcessException {
        Document document = database.getDocument();
        try {
            OutputStream outputStream = new FileOutputStream(DBManager.PATH + "/" + database.getName() + ".xml");
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            document.write(writer);
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            Log.a(e.getMessage());
            throw new DBProcessException("IO error.");
        }
    }

    /**
     * 从数据库对象池中获取数据库对象
     *
     * @param name 目标数据库名
     * @return 数据库对象
     * @throws DBProcessException 目标数据库不存在
     */
    public Database getDatabase(String name) throws DBProcessException {
        return databasePool.getDatabase(name);
    }

    /**
     * 从文件中创建出数据库对象
     *
     * @param dbName 目标数据库名
     * @return 数据库对象
     * @throws DBProcessException 文件无法读取
     */
    public Database takeDatabase(String dbName) throws DBProcessException {
        SAXReader reader = new SAXReader();
        Document document;
        try {
            InputStream inputStream = new FileInputStream(PATH + "/" + dbName + ".xml");
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            document = reader.read(streamReader);
        } catch (DocumentException | FileNotFoundException e) {
            Log.a(e.getMessage());
            throw new DBProcessException("Unknown error.");
        }
        Element rootElement = document.getRootElement();
        Database database = new Database(rootElement.attributeValue("name"), document);
        for (Iterator<Element> it = rootElement.elementIterator("table"); it.hasNext(); ) {
            Element tableElement = it.next();
            Table table = new Table(tableElement.attributeValue("name"));
            for (Iterator<Element> subIt = tableElement.elementIterator("attr"); subIt.hasNext(); ) {
                Element attributeElement = subIt.next();
                String name = attributeElement.attributeValue("name");
                String type = attributeElement.attributeValue("type");
                String length = attributeElement.attributeValue("length");
                TableAttribute tableAttribute = new TableAttribute(name, type, length);
                table.addAttribute(tableAttribute);
            }
            database.getTables().add(table);
        }
        return database;
    }

    /**
     * 认证密码是否正确
     *
     * @param name     用户名
     * @param password 密码
     * @return true if password is right
     * @throws DBProcessException 内部表“user”丢失
     */
    public boolean verify(String name, String password) throws DBProcessException {
        AESCrypto aes = new AESCrypto("5494");
        String encodePassword = aes.encode(password);
        Database userDB = getDatabase("lyra");
        Element rootElement = userDB.getDocument().getRootElement();
        Element tableElement = XMLUtil.findTableElement(rootElement, "user");
        if (tableElement == null) {
            String message = "Error: inside table `user` lost";
            Log.a(message);
            throw new DBProcessException(message);
        }
        for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("username").equals(name) &&
                    element.attributeValue("passwd").equals(encodePassword)) {
                return true;
            }
        }
        return false;
    }

    private static class Singleton {
        private static final DBManager INSTANCE = new DBManager();
    }
}
