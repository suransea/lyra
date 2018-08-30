package com.sea.pxx;

import org.kohsuke.args4j.*;

public class ArgsParser {

    @Option(name = "-u", usage = "username", metaVar = "str")
    private String username;

    @Option(name = "-p", usage = "if input password", metaVar = "str")
    private boolean password = false;

    public String getAddress() {
        return address;
    }

    @Option(name = "-h", usage = "IP address")
    private String address;

    ArgsParser(String[] args) {
        CmdLineParser cmdLineParser = new CmdLineParser(this);
        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            System.out.println("\nUsage:");
            cmdLineParser.printUsage(System.out);
            System.exit(1);
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isPassword() {
        return password;
    }
}
