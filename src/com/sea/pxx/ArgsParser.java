package com.sea.pxx;

import org.kohsuke.args4j.*;

public class ArgsParser {

    @Option(name = "-u", usage = "username", metaVar = "str")
    private String username = "root";

    @Option(name = "-p", usage = "if input password")
    private boolean password = false;

    @Option(name = "-h", usage = "IP address", metaVar = "str")
    private String address = "127.0.0.1";

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

    public String getAddress() {
        return address;
    }
}
