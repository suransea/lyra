package com.sea.lyra.jdbc;

public interface Function {
    default void call() {
    }

    default Object callAndReturn() {
        return null;
    }

    default void call(Object... objects) {
    }

    default Object callAndReturn(Object... objects) {
        return null;
    }
}
