/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

@Structure.FieldOrder(value={"number", "maxMhz", "currentMhz", "mhzLimit", "maxIdleState", "currentIdleState"})
public class llO001i0laND
extends Structure {
    public int number;
    public int maxMhz;
    public int currentMhz;
    public int mhzLimit;
    public int maxIdleState;
    public int currentIdleState;

    public llO001i0laND(Pointer pointer) {
        super(pointer);
        this.read();
    }

    public llO001i0laND() {
    }
}

