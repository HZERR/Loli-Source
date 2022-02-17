/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;

@Structure.FieldOrder(value={"Capabilities", "Technology", "Reserved", "Chemistry", "DesignedCapacity", "FullChargedCapacity", "DefaultAlert1", "DefaultAlert2", "CriticalBias", "CycleCount"})
public class liilililLAND
extends Structure {
    public int Capabilities;
    public byte Technology;
    public byte[] Reserved = new byte[3];
    public byte[] Chemistry = new byte[4];
    public int DesignedCapacity;
    public int FullChargedCapacity;
    public int DefaultAlert1;
    public int DefaultAlert2;
    public int CriticalBias;
    public int CycleCount;
}

