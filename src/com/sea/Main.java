package com.sea;

public class Main {

    public static void main(String[] args) {
        //解析命令行参数
        ArgsParser argsParser = new ArgsParser(args);

        String username;
        String passwd;

        if(argsParser.getUsername()==null){
            username="root";
        }
    }
}
