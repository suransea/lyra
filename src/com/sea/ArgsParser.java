package com.sea;

import org.kohsuke.args4j.*;

public class ArgsParser {

    @Option(name = "-u", usage = "username")
    private String username;

    @Option(name = "-p", usage = "if input password")
    private boolean password;

    public ArgsParser(String args) {
        CmdLineParser cmdLineParser = new CmdLineParser(this);
        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
        }
    }
}
