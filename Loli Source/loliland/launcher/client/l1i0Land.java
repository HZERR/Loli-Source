/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

@Structure.FieldOrder(value={"acOnLine", "batteryPresent", "charging", "discharging", "spare1", "tag", "maxCapacity", "remainingCapacity", "rate", "estimatedTime", "defaultAlert1", "defaultAlert2"})
public class l1i0Land
extends Structure {
    public byte acOnLine;
    public byte batteryPresent;
    public byte charging;
    public byte discharging;
    public byte[] spare1 = new byte[3];
    public byte tag;
    public int maxCapacity;
    public int remainingCapacity;
    public int rate;
    public int estimatedTime;
    public int defaultAlert1;
    public int defaultAlert2;

    public l1i0Land(Pointer pointer) {
        super(pointer);
        this.read();
    }

    public l1i0Land() {
    }
}

