/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import java.io.IOException;
import java.lang.reflect.Field;
import loliland.launcher.client.I1IIll1LaND;

public class lI10lOlAND {
    public static String I1O1I1LaNd(Process process) {
        String string = null;
        try {
            if (process.getClass().getName().equals("java.lang.Win32Process") || process.getClass().getName().equals("java.lang.ProcessImpl")) {
                Field field = process.getClass().getDeclaredField("handle");
                field.setAccessible(true);
                WinNT.HANDLE hANDLE = new WinNT.HANDLE();
                hANDLE.setPointer(Pointer.createConstant(field.getLong(process)));
                string = String.valueOf(Kernel32.INSTANCE.GetProcessId(hANDLE));
                field.setAccessible(false);
            } else if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field field = process.getClass().getDeclaredField("pid");
                field.setAccessible(true);
                string = String.valueOf(field.getLong(process));
                field.setAccessible(false);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return string;
    }

    public static void I1O1I1LaNd(String string) throws IOException {
        if (string == null) {
            return;
        }
        if (I1IIll1LaND.O1il1llOLANd()) {
            Runtime.getRuntime().exec("cmd /c TASKKILL /F /PID " + string);
        } else {
            Runtime.getRuntime().exec("kill -15 " + string);
        }
    }
}

