/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.LibCAPI;
import loliland.launcher.client.I0l11lIOlAnd;
import loliland.launcher.client.O0iIl1ilaND;
import loliland.launcher.client.l0OI1iOlAnD;

public interface Iil1Ol1laNd
extends I0l11lIOlAnd {
    public static final Iil1Ol1laNd INSTANCE = Native.load("libc", Iil1Ol1laNd.class);
    public static final int UTX_USERSIZE = 32;
    public static final int UTX_LINESIZE = 16;
    public static final int UTX_IDSIZE = 8;
    public static final int UTX_HOSTSIZE = 128;
    public static final int UINT64_SIZE = Native.getNativeSize(Long.TYPE);
    public static final int INT_SIZE = Native.getNativeSize(Integer.TYPE);
    public static final int CPUSTATES = 5;
    public static final int CP_USER = 0;
    public static final int CP_NICE = 1;
    public static final int CP_SYS = 2;
    public static final int CP_INTR = 3;
    public static final int CP_IDLE = 4;

    public int sysctl(int[] var1, int var2, Pointer var3, O0iIl1ilaND var4, Pointer var5, LibCAPI.size_t var6);

    public int sysctlbyname(String var1, Pointer var2, O0iIl1ilaND var3, Pointer var4, LibCAPI.size_t var5);

    public int sysctlnametomib(String var1, Pointer var2, O0iIl1ilaND var3);

    public l0OI1iOlAnD getutxent();
}

