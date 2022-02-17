/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Window;
import java.io.File;
import java.security.AccessController;

final class MacCommonDialogs {
    MacCommonDialogs() {
    }

    private static native void _initIDs();

    private static native CommonDialogs.FileChooserResult _showFileOpenChooser(long var0, String var2, String var3, boolean var4, CommonDialogs.ExtensionFilter[] var5, int var6);

    private static native CommonDialogs.FileChooserResult _showFileSaveChooser(long var0, String var2, String var3, String var4, CommonDialogs.ExtensionFilter[] var5, int var6);

    private static native File _showFolderChooser(long var0, String var2, String var3);

    static CommonDialogs.FileChooserResult showFileChooser_impl(Window window, String string, String string2, String string3, int n2, boolean bl, CommonDialogs.ExtensionFilter[] arrextensionFilter, int n3) {
        long l2;
        long l3 = l2 = window != null ? window.getNativeWindow() : 0L;
        if (n2 == 0) {
            return MacCommonDialogs._showFileOpenChooser(l2, string, string3, bl, arrextensionFilter, n3);
        }
        if (n2 == 1) {
            return MacCommonDialogs._showFileSaveChooser(l2, string, string2, string3, arrextensionFilter, n3);
        }
        return null;
    }

    static File showFolderChooser_impl(Window window, String string, String string2) {
        long l2 = window != null ? window.getNativeWindow() : 0L;
        return MacCommonDialogs._showFolderChooser(l2, string, string2);
    }

    static boolean isFileNSURLEnabled() {
        return AccessController.doPrivileged(() -> Boolean.getBoolean("glass.macosx.enableFileNSURL"));
    }

    static {
        MacCommonDialogs._initIDs();
    }
}

