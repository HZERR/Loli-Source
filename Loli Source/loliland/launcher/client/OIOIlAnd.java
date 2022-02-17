/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;

@Structure.FieldOrder(value={"version", "length", "cpuPLimit", "gpuPLimit", "memPLimit"})
public class OIOIlAnd
extends Structure {
    public short version;
    public short length;
    public int cpuPLimit;
    public int gpuPLimit;
    public int memPLimit;
}

