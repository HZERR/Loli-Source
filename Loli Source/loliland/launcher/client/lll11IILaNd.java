/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.ptr.NativeLongByReference;

public interface lll11IILaNd
extends IOKit {
    public static final lll11IILaNd INSTANCE = Native.load("IOKit", lll11IILaNd.class);

    public int IOConnectCallStructMethod(IOKit.IOConnect var1, int var2, Structure var3, NativeLong var4, Structure var5, NativeLongByReference var6);
}

