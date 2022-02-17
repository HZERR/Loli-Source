/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;

@Structure.FieldOrder(value={"major", "minor", "build", "reserved", "release"})
public class lOiILanD
extends Structure {
    public byte major;
    public byte minor;
    public byte build;
    public byte reserved;
    public short release;
}

