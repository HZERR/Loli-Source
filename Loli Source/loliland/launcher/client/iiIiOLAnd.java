/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.function.Supplier;
import loliland.launcher.client.OO10lO1LANd;
import loliland.launcher.client.i0l10iiilaNd;
import loliland.launcher.client.l11IliilLANd;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1l10l0LaNd;

public class iiIiOLAnd
extends i0l10iiilaNd {
    private Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(OO10lO1LANd::I1O1I1LaNd, lii1IO0LaNd.I1O1I1LaNd());

    @Override
    public ll1l10l0LaNd lli0OiIlAND() {
        for (Perfstat.perfstat_protocol_t perfstat_protocol_t2 : (Perfstat.perfstat_protocol_t[])this.I1O1I1LaNd.get()) {
            if (!"tcp".equals(Native.toString(perfstat_protocol_t2.name))) continue;
            return new ll1l10l0LaNd(perfstat_protocol_t2.u.tcp.established, perfstat_protocol_t2.u.tcp.initiated, perfstat_protocol_t2.u.tcp.accepted, perfstat_protocol_t2.u.tcp.dropped, perfstat_protocol_t2.u.tcp.dropped, perfstat_protocol_t2.u.tcp.opackets, perfstat_protocol_t2.u.tcp.ipackets, 0L, perfstat_protocol_t2.u.tcp.ierrors, 0L);
        }
        return new ll1l10l0LaNd(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    }

    @Override
    public l11IliilLANd li0iOILAND() {
        for (Perfstat.perfstat_protocol_t perfstat_protocol_t2 : (Perfstat.perfstat_protocol_t[])this.I1O1I1LaNd.get()) {
            if (!"udp".equals(Native.toString(perfstat_protocol_t2.name))) continue;
            return new l11IliilLANd(perfstat_protocol_t2.u.udp.opackets, perfstat_protocol_t2.u.udp.ipackets, perfstat_protocol_t2.u.udp.no_socket, perfstat_protocol_t2.u.udp.ierrors);
        }
        return new l11IliilLANd(0L, 0L, 0L, 0L);
    }
}

