/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;

@Structure.FieldOrder(value={"fi_openflags", "fi_status", "fi_offset", "fi_type", "fi_guardflags"})
public class iOIIiland
extends Structure {
    public int fi_openflags;
    public int fi_status;
    public long fi_offset;
    public int fi_type;
    public int fi_guardflags;
}

