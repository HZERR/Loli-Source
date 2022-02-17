/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.ptr.PointerByReference;
import loliland.launcher.client.l0liOI11laND;

public interface I0l11lIOlAnd
extends Library,
LibCAPI {
    public static final int AI_CANONNAME = 2;
    public static final int UT_LINESIZE = 32;
    public static final int UT_NAMESIZE = 32;
    public static final int UT_HOSTSIZE = 256;
    public static final int LOGIN_PROCESS = 6;
    public static final int USER_PROCESS = 7;

    public int getpid();

    public int getaddrinfo(String var1, String var2, l0liOI11laND var3, PointerByReference var4);

    public void freeaddrinfo(Pointer var1);

    public String gai_strerror(int var1);

    public void setutxent();

    public void endutxent();
}

