/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.linux.LibC;
import loliland.launcher.client.I0l11lIOlAnd;
import loliland.launcher.client.O0lOlOOlaNd;

public interface l1IOlANd
extends LibC,
I0l11lIOlAnd {
    public static final l1IOlANd INSTANCE = Native.load("c", l1IOlANd.class);

    public O0lOlOOlaNd getutxent();
}

