/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import loliland.launcher.client.IiI1il11lanD;
import loliland.launcher.client.O1i1llaNd;

@Structure.FieldOrder(value={"ai_flags", "ai_family", "ai_socktype", "ai_protocol", "ai_addrlen", "ai_addr", "ai_canonname", "ai_next"})
public class l0liOI11laND
extends Structure {
    public int ai_flags;
    public int ai_family;
    public int ai_socktype;
    public int ai_protocol;
    public int ai_addrlen;
    public IiI1il11lanD ai_addr;
    public String ai_canonname;
    public O1i1llaNd ai_next;

    public l0liOI11laND() {
    }

    public l0liOI11laND(Pointer pointer) {
        super(pointer);
        this.read();
    }
}

