/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;

@Structure.FieldOrder(value={"BatteryTag", "Timeout", "PowerState", "LowCapacity", "HighCapacity"})
public class iiIiiOiLAnD
extends Structure {
    public int BatteryTag;
    public int Timeout;
    public int PowerState;
    public int LowCapacity;
    public int HighCapacity;
}

