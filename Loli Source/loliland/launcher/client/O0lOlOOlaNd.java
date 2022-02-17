/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;
import loliland.launcher.client.IOOlOOLaND;
import loliland.launcher.client.ilOIlAND;

@Structure.FieldOrder(value={"ut_type", "ut_pid", "ut_line", "ut_id", "ut_user", "ut_host", "ut_exit", "ut_session", "ut_tv", "ut_addr_v6", "reserved"})
public class O0lOlOOlaNd
extends Structure {
    public short ut_type;
    public int ut_pid;
    public byte[] ut_line = new byte[32];
    public byte[] ut_id = new byte[4];
    public byte[] ut_user = new byte[32];
    public byte[] ut_host = new byte[256];
    public ilOIlAND ut_exit;
    public int ut_session;
    public IOOlOOLaND ut_tv;
    public int[] ut_addr_v6 = new int[4];
    public byte[] reserved = new byte[20];
}

