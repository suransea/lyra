package com.sea.lyrad.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockUtil {
    private static Lock singleLock = null;

    public static Lock getSingleLock() {
        if (singleLock == null) {
            singleLock = new ReentrantLock(true);
        }
        return singleLock;
    }

    public static Lock getNewLock() {
        return new ReentrantLock(true);
    }
}
