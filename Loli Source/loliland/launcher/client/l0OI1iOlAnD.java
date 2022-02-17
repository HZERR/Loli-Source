/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;
import loliland.launcher.client.lO000lIlaND;

@Structure.FieldOrder(value={"ut_type", "ut_tv", "ut_id", "ut_pid", "ut_user", "ut_line", "ut_host", "ut_spare"})
public class l0OI1iOlAnD
extends Structure {
    public short ut_type;
    public lO000lIlaND ut_tv;
    public byte[] ut_id = new byte[8];
    public int ut_pid;
    public byte[] ut_user = new byte[32];
    public byte[] ut_line = new byte[16];
    public byte[] ut_host = new byte[128];
    public byte[] ut_spare = new byte[64];
}

