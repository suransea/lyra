package com.sea.pxxd;

import com.sea.pxxd.stmt.CreateDatabase;
import com.sea.pxxd.stmt.Statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParser {

    public static final String createDBRegex = "\\s*create\\s+database\\s+(\\w+)\\s*;\\s*";

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
        throw new SQLParseException("The format of the SQL you provide is not right.");
    }
}
