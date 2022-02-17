/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.mac.SystemB;
import com.sun.jna.platform.unix.LibCAPI;
import loliland.launcher.client.I0l11lIOlAnd;
import loliland.launcher.client.O0iIl1ilaND;
import loliland.launcher.client.iIl11Land;

public interface lIOOOlIlaNd
extends SystemB,
I0l11lIOlAnd {
    public static final lIOOOlIlaNd INSTANCE = Native.load("System", lIOOOlIlaNd.class);
    public static final int PROC_PIDLISTFDS = 1;
    public static final int PROX_FDTYPE_SOCKET = 2;
    public static final int PROC_PIDFDSOCKETINFO = 3;
    public static final int TSI_T_NTIMERS = 4;
    public static final int SOCKINFO_IN = 1;
    public static final int SOCKINFO_TCP = 2;
    public static final int UTX_USERSIZE = 256;
    public static final int UTX_LINESIZE = 32;
    public static final int UTX_IDSIZE = 4;
    public static final int UTX_HOSTSIZE = 256;
    public static final int AF_INET = 2;
    public static final int AF_INET6 = 30;

    public iIl11Land getutxent();

    public int proc_pidfdinfo(int var1, int var2, int var3, Structure var4, int var5);

    public int sysctl(int[] var1, int var2, Pointer var3, O0iIl1ilaND var4, Pointer var5, LibCAPI.size_t var6);

    public int sysctlbyname(String var1, Pointer var2, O0iIl1ilaND var3, Pointer var4, LibCAPI.size_t var5);

    public int sysctlnametomib(String var1, Pointer var2, O0iIl1ilaND var3);
}

