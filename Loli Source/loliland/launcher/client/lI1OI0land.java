/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;

@Structure.FieldOrder(value={"insi_fport", "insi_lport", "insi_gencnt", "insi_flags", "insi_flow", "insi_vflag", "insi_ip_ttl", "rfu_1", "insi_faddr", "insi_laddr", "insi_v4", "insi_v6"})
public class lI1OI0land
extends Structure {
    public int insi_fport;
    public int insi_lport;
    public long insi_gencnt;
    public int insi_flags;
    public int insi_flow;
    public byte insi_vflag;
    public byte insi_ip_ttl;
    public int rfu_1;
    public int[] insi_faddr = new int[4];
    public int[] insi_laddr = new int[4];
    public byte insi_v4;
    public byte[] insi_v6 = new byte[9];
}

