/*
 * Decompiled with CFR 0.150.
 */
package org.java_websocket.exceptions;

import org.java_websocket.exceptions.InvalidDataException;

public class InvalidHandshakeException
extends InvalidDataException {
    private static final long serialVersionUID = -1426533877490484964L;

    public InvalidHandshakeException() {
        super(1002);
    }

    public InvalidHandshakeException(String s2, Throwable t2) {
        super(1002, s2, t2);
    }

    public InvalidHandshakeException(String s2) {
        super(1002, s2);
    }

    public InvalidHandshakeException(Throwable t2) {
        super(1002, t2);
    }
}

