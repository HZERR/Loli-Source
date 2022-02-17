/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.LibCAPI;
import loliland.launcher.client.I0l11lIOlAnd;
import loliland.launcher.client.O0iIl1ilaND;

public interface lllilIiiLANd
extends I0l11lIOlAnd {
    public static final lllilIiiLANd INSTANCE = Native.load(null, lllilIiiLANd.class);
    public static final int CTL_KERN = 1;
    public static final int CTL_VM = 1;
    public static final int CTL_HW = 6;
    public static final int CTL_MACHDEP = 7;
    public static final int CTL_VFS = 10;
    public static final int KERN_OSTYPE = 1;
    public static final int KERN_OSRELEASE = 2;
    public static final int KERN_OSREV = 3;
    public static final int KERN_VERSION = 4;
    public static final int KERN_MAXVNODES = 5;
    public static final int KERN_MAXPROC = 6;
    public static final int KERN_ARGMAX = 8;
    public static final int KERN_CPTIME = 40;
    public static final int KERN_CPTIME2 = 71;
    public static final int VM_UVMEXP = 4;
    public static final int HW_MACHINE = 1;
    public static final int HW_MODEL = 2;
    public static final int HW_PAGESIZE = 7;
    public static final int HW_CPUSPEED = 12;
    public static final int HW_NCPUFOUND = 21;
    public static final int HW_SMT = 24;
    public static final int HW_NCPUONLINE = 25;
    public static final int VFS_GENERIC = 0;
    public static final int VFS_BCACHESTAT = 3;
    public static final int CPUSTATES = 5;
    public static final int CP_USER = 0;
    public static final int CP_NICE = 1;
    public static final int CP_SYS = 2;
    public static final int CP_INTR = 3;
    public static final int CP_IDLE = 4;
    public static final int UINT64_SIZE = Native.getNativeSize(Long.TYPE);
    public static final int INT_SIZE = Native.getNativeSize(Integer.TYPE);

    public int sysctl(int[] var1, int var2, Pointer var3, O0iIl1ilaND var4, Pointer var5, LibCAPI.size_t var6);
}

