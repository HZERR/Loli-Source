/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.inputmaps;

import java.security.AccessController;
import java.security.PrivilegedAction;
import org.pushingpixels.lafwidget.utils.LookUtils;
import org.pushingpixels.substance.api.inputmaps.InputMapSet;
import org.pushingpixels.substance.internal.inputmaps.AquaInputMapSet;
import org.pushingpixels.substance.internal.inputmaps.BaseInputMapSet;
import org.pushingpixels.substance.internal.inputmaps.GnomeInputMapSet;
import org.pushingpixels.substance.internal.inputmaps.WindowsInputMapSet;

public class SubstanceInputMapUtilities {
    public static InputMapSet getSystemInputMapSet() {
        if (LookUtils.IS_OS_MAC) {
            return new AquaInputMapSet();
        }
        if (LookUtils.IS_OS_WINDOWS) {
            return new WindowsInputMapSet();
        }
        try {
            String desktop = AccessController.doPrivileged(new PrivilegedAction<String>(){

                @Override
                public String run() {
                    return System.getProperty("sun.desktop");
                }
            });
            if ("gnome".equals(desktop)) {
                return new GnomeInputMapSet();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return SubstanceInputMapUtilities.getCrossPlatformInputMapSet();
    }

    public static InputMapSet getCrossPlatformInputMapSet() {
        return new BaseInputMapSet();
    }
}

