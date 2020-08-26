package com.sea.lyrad.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁工具
 */
public class LockUtil {

    private LockUtil() {
    }

    public static Lock getSingleLock() {
        return LockInstance.INSTANCE;
    }

    public static Lock newLock() {
        return new ReentrantLock(true);
    }

    private static class LockInstance {
        private static final Lock INSTANCE = new ReentrantLock(true);
    }
}
