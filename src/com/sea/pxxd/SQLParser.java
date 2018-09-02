package com.sea.pxxd;

import com.sea.pxxd.stmt.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParser {

    private static Map<String, String> regexps;

    static {
        regexps = new HashMap<>();
        regexps.put(
                "CreateDatabase",
                "\\s*create\\s+database\\s+(\\w+)\\s*;\\s*"
        );
        regexps.put(
                "UseDatabase",
                "\\s*use\\s+(\\w+)\\s*;\\s*"
        );
        regexps.put(
                "CreateTable",
                "\\s*create\\s+table\\s+(\\w+)\\s*\\(\\s*(.*)\\)\\s*;\\s*"
        );
        regexps.put(
                "Insert",
                "insert\\s+into\\s+(\\w+)\\s+values\\s*(\\s*\\(.*\\)\\s*)\\s*;\\s*"
        );
    }

    public SQLParser() {

    }

    public Statement parse(String sql) throws SQLParseException {

        for (Map.Entry<String, String> entry : regexps.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getValue());
            Matcher matcher = pattern.matcher(sql.trim().toLowerCase());
            if (matcher.matches()) {
                try {
                    Class stmt = Class.forName("com.sea.pxxd.stmt." + entry.getKey());
                    return (Statement) stmt.getConstructor(Matcher.class).newInstance(matcher);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SQLParseException("Unknown error.");
                }
            }
        }
        throw new SQLParseException("The format of the SQL you provide is not right.");
    }
}
