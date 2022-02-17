/*
 * Decompiled with CFR 0.150.
 */
package org.java_websocket.exceptions;

import org.java_websocket.exceptions.InvalidDataException;

public class LimitExceededException
extends InvalidDataException {
    private static final long serialVersionUID = 6908339749836826785L;
    private final int limit;

    public LimitExceededException() {
        this(Integer.MAX_VALUE);
    }

    public LimitExceededException(int limit) {
        super(1009);
        this.limit = limit;
    }

    public LimitExceededException(String s2, int limit) {
        super(1009, s2);
        this.limit = limit;
    }

    public LimitExceededException(String s2) {
        this(s2, Integer.MAX_VALUE);
    }

    public int getLimit() {
        return this.limit;
    }
}

