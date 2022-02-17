/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.mac.CoreFoundation;
import loliland.launcher.client.liI0O1laNd;

public interface iOiiIlILand
extends Library {
    public static final iOiiIlILand INSTANCE = Native.load("CoreGraphics", iOiiIlILand.class);
    public static final int kCGNullWindowID = 0;
    public static final int kCGWindowListOptionAll = 0;
    public static final int kCGWindowListOptionOnScreenOnly = 1;
    public static final int kCGWindowListOptionOnScreenAboveWindow = 2;
    public static final int kCGWindowListOptionOnScreenBelowWindow = 4;
    public static final int kCGWindowListOptionIncludingWindow = 8;
    public static final int kCGWindowListExcludeDesktopElements = 16;

    public CoreFoundation.CFArrayRef CGWindowListCopyWindowInfo(int var1, int var2);

    public boolean CGRectMakeWithDictionaryRepresentation(CoreFoundation.CFDictionaryRef var1, liI0O1laNd var2);
}

