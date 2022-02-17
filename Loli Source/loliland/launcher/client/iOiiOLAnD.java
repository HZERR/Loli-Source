/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.mac.CoreFoundation;
import loliland.launcher.client.ll0OlllanD;

public interface iOiiOLAnD
extends Library {
    public static final iOiiOLAnD INSTANCE = Native.load("SystemConfiguration", iOiiOLAnD.class);

    public CoreFoundation.CFArrayRef SCNetworkInterfaceCopyAll();

    public CoreFoundation.CFStringRef SCNetworkInterfaceGetBSDName(ll0OlllanD var1);

    public CoreFoundation.CFStringRef SCNetworkInterfaceGetLocalizedDisplayName(ll0OlllanD var1);
}

