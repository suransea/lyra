package com.sea.lyra;

import com.sea.lyra.jdbc.LyraDriver;

public class Driver extends LyraDriver {
    public Driver() {
    }

    static {
        System.out.println("Redirect to class com.sea.lyra.jdbc.LyraDriver");
    }
}
