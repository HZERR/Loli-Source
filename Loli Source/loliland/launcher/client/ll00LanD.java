/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Cfgmgr32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;

public interface ll00LanD
extends Cfgmgr32 {
    public static final ll00LanD INSTANCE = Native.load("cfgmgr32", ll00LanD.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int CM_DRP_DEVICEDESC = 1;
    public static final int CM_DRP_SERVICE = 5;
    public static final int CM_DRP_CLASS = 8;
    public static final int CM_DRP_MFG = 12;
    public static final int CM_DRP_FRIENDLYNAME = 13;

    public boolean CM_Get_DevNode_Registry_Property(int var1, int var2, IntByReference var3, Pointer var4, IntByReference var5, int var6);
}

