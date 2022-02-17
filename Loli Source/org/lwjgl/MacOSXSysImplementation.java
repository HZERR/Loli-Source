/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl;

import com.apple.eio.FileManager;
import java.awt.Toolkit;
import org.lwjgl.J2SESysImplementation;
import org.lwjgl.LWJGLUtil;

final class MacOSXSysImplementation
extends J2SESysImplementation {
    private static final int JNI_VERSION = 25;

    MacOSXSysImplementation() {
    }

    public int getRequiredJNIVersion() {
        return 25;
    }

    public boolean openURL(String url) {
        try {
            FileManager.openURL(url);
            return true;
        }
        catch (Exception e2) {
            LWJGLUtil.log("Exception occurred while trying to invoke browser: " + e2);
            return false;
        }
    }

    static {
        Toolkit.getDefaultToolkit();
    }
}

