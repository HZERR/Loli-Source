/*
 * Decompiled with CFR 0.150.
 */
package org.java_websocket.handshake;

import java.util.Iterator;

public interface Handshakedata {
    public Iterator<String> iterateHttpFields();

    public String getFieldValue(String var1);

    public boolean hasFieldValue(String var1);

    public byte[] getContent();
}

