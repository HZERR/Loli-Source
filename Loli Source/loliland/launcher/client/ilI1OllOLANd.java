/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

@Structure.FieldOrder(value={"numbufs", "numbufpages", "numdirtypages", "numcleanpages", "pendingwrites", "pendingreads", "numwrites", "numreads", "cachehits", "busymapped", "dmapages", "highpages", "delwribufs", "kvaslots", "kvaslots_avail", "highflips", "highflops", "dmaflips"})
public class ilI1OllOLANd
extends Structure {
    public long numbufs;
    public long numbufpages;
    public long numdirtypages;
    public long numcleanpages;
    public long pendingwrites;
    public long pendingreads;
    public long numwrites;
    public long numreads;
    public long cachehits;
    public long busymapped;
    public long dmapages;
    public long highpages;
    public long delwribufs;
    public long kvaslots;
    public long kvaslots_avail;
    public long highflips;
    public long highflops;
    public long dmaflips;

    public ilI1OllOLANd(Pointer pointer) {
        super(pointer);
        this.read();
    }
}

