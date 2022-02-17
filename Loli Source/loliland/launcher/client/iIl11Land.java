/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Structure;
import com.sun.jna.platform.mac.SystemB;

@Structure.FieldOrder(value={"ut_user", "ut_id", "ut_line", "ut_pid", "ut_type", "ut_tv", "ut_host", "ut_pad"})
public class iIl11Land
extends Structure {
    public byte[] ut_user = new byte[256];
    public byte[] ut_id = new byte[4];
    public byte[] ut_line = new byte[32];
    public int ut_pid;
    public short ut_type;
    public SystemB.Timeval ut_tv;
    public byte[] ut_host = new byte[256];
    public byte[] ut_pad = new byte[16];
}

