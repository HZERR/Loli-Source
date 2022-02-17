/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;
import loliland.launcher.client.OIOIlAnd;
import loliland.launcher.client.OO11OLANd;
import loliland.launcher.client.lOiILanD;

@Structure.FieldOrder(value={"key", "vers", "pLimitData", "keyInfo", "result", "status", "data8", "data32", "bytes"})
public class lIlOO1llAnd
extends Structure {
    public int key;
    public lOiILanD vers;
    public OIOIlAnd pLimitData;
    public OO11OLANd keyInfo;
    public byte result;
    public byte status;
    public byte data8;
    public int data32;
    public byte[] bytes = new byte[32];
}

