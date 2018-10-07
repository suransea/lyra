package com.sea.lyra;

import com.sea.lyra.jdbc.LyraDriver;

public class Driver extends LyraDriver {
    static {
        System.out.println("Redirect to class com.sea.lyra.jdbc.LyraDriver");
    }

    public Driver() {
    }
}
