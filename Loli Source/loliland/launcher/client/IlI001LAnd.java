/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.NativeLong;
import com.sun.jna.Structure;

@Structure.FieldOrder(value={"tv_sec", "tv_usec"})
public class IlI001LAnd
extends Structure {
    public NativeLong tv_sec;
    public NativeLong tv_usec;
}

