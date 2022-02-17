/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.PowrProf;

public interface lilO1llaND
extends PowrProf {
    public static final lilO1llaND INSTANCE = Native.load("PowrProf", lilO1llaND.class);
}

