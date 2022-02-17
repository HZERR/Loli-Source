/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;

public interface Cfgmgr32
extends Library {
    public static final Cfgmgr32 INSTANCE = Native.load("Cfgmgr32", Cfgmgr32.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int CR_SUCCESS = 0;
    public static final int CR_BUFFER_SMALL = 26;
    public static final int CM_LOCATE_DEVNODE_NORMAL = 0;
    public static final int CM_LOCATE_DEVNODE_PHANTOM = 1;
    public static final int CM_LOCATE_DEVNODE_CANCELREMOVE = 2;
    public static final int CM_LOCATE_DEVNODE_NOVALIDATION = 4;
    public static final int CM_LOCATE_DEVNODE_BITS = 7;

    public int CM_Locate_DevNode(IntByReference var1, String var2, int var3);

    public int CM_Get_Parent(IntByReference var1, int var2, int var3);

    public int CM_Get_Child(IntByReference var1, int var2, int var3);

    public int CM_Get_Sibling(IntByReference var1, int var2, int var3);

    public int CM_Get_Device_ID(int var1, Pointer var2, int var3, int var4);

    public int CM_Get_Device_ID_Size(IntByReference var1, int var2, int var3);
}

