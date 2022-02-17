/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;
import loliland.launcher.client.IIIOI1LAnd;

@Structure.FieldOrder(value={"soi_stat", "soi_so", "soi_pcb", "soi_type", "soi_protocol", "soi_family", "soi_options", "soi_linger", "soi_state", "soi_qlen", "soi_incqlen", "soi_qlimit", "soi_timeo", "soi_error", "soi_oobmark", "soi_rcv", "soi_snd", "soi_kind", "rfu_1", "soi_proto"})
public class li01I0I0LaND
extends Structure {
    public long[] soi_stat = new long[17];
    public long soi_so;
    public long soi_pcb;
    public int soi_type;
    public int soi_protocol;
    public int soi_family;
    public short soi_options;
    public short soi_linger;
    public short soi_state;
    public short soi_qlen;
    public short soi_incqlen;
    public short soi_qlimit;
    public short soi_timeo;
    public short soi_error;
    public int soi_oobmark;
    public int[] soi_rcv = new int[6];
    public int[] soi_snd = new int[6];
    public int soi_kind;
    public int rfu_1;
    public IIIOI1LAnd soi_proto;
}

