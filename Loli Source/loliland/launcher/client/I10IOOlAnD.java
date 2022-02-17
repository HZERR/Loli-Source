/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;

@Structure.FieldOrder(value={"key", "dataSize", "dataType", "bytes"})
public class I10IOOlAnD
extends Structure {
    public byte[] key = new byte[5];
    public int dataSize;
    public byte[] dataType = new byte[5];
    public byte[] bytes = new byte[32];
}

