package com.sea.pxxd;

import com.sea.pxxd.stmt.CreateDatabase;
import com.sea.pxxd.stmt.CreateTable;
import com.sea.pxxd.stmt.Statement;
import com.sea.pxxd.stmt.UseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParser {

    private static final String createDBRegex = "\\s*create\\s+database\\s+(\\w+)\\s*;\\s*";
    private static final String useDatabaseRegex = "\\s*use\\s+(\\w+)\\s*;\\s*";
    private static final String createTableRegex = "\\s*create\\s+table\\s+(\\w+)\\s*\\(\\s*(.*)\\)\\s*;\\s*";

    private Pattern pattern;
    private Matcher matcher;

    public SQLParser() {

    }

    public Statement parse(String sql) throws SQLParseException {

        pattern = Pattern.compile(createDBRegex);
        matcher = pattern.matcher(sql);
        if (matcher.matches()) {
            return new CreateDatabase(matcher);
        }
        pattern = Pattern.compile(useDatabaseRegex);
        matcher = pattern.matcher(sql);
        if (matcher.matches()) {
            return new UseDatabase(matcher);
        }
        pattern = Pattern.compile(createTableRegex);
        matcher = pattern.matcher(sql);
        if (matcher.matches()) {
            return new CreateTable(matcher);
        }
        throw new SQLParseException("The format of the SQL you provide is not right.");
    }
}
