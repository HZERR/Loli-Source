/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import loliland.launcher.client.I0l11lIOlAnd;
import loliland.launcher.client.IIOOlanD;

public interface il00iLAnD
extends I0l11lIOlAnd {
    public static final il00iLAnD INSTANCE = Native.load("c", il00iLAnD.class);
    public static final int UTX_USERSIZE = 32;
    public static final int UTX_LINESIZE = 32;
    public static final int UTX_IDSIZE = 4;
    public static final int UTX_HOSTSIZE = 257;

    public IIOOlanD getutxent();
}

