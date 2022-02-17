/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;
import loliland.launcher.client.IlI001LAnd;

@Structure.FieldOrder(value={"ut_user", "ut_id", "ut_line", "ut_pid", "ut_type", "ut_tv", "ut_session", "ut_syslen", "ut_host"})
public class IIOOlanD
extends Structure {
    public byte[] ut_user = new byte[32];
    public byte[] ut_id = new byte[4];
    public byte[] ut_line = new byte[32];
    public int ut_pid;
    public short ut_type;
    public IlI001LAnd ut_tv;
    public int ut_session;
    public short ut_syslen;
    public byte[] ut_host = new byte[257];
}

