/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;
import loliland.launcher.client.lI1OI0land;

@Structure.FieldOrder(value={"tcpsi_ini", "tcpsi_state", "tcpsi_timer", "tcpsi_mss", "tcpsi_flags", "rfu_1", "tcpsi_tp"})
public class l0Ii0I10LAND
extends Structure {
    public lI1OI0land tcpsi_ini;
    public int tcpsi_state;
    public int[] tcpsi_timer = new int[4];
    public int tcpsi_mss;
    public int tcpsi_flags;
    public int rfu_1;
    public long tcpsi_tp;
}

