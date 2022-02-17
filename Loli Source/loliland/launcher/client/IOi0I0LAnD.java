/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.lIOI1Land;

public final class IOi0I0LAnD {
    private static final WinDef.DWORD I1O1I1LaNd = new WinDef.DWORD(2L);

    private IOi0I0LAnD() {
    }

    public static List I1O1I1LaNd(boolean bl) {
        List<DesktopWindow> list = WindowUtils.getAllWindows(true);
        ArrayList<lIOI1Land> arrayList = new ArrayList<lIOI1Land>();
        HashMap hashMap = new HashMap();
        for (DesktopWindow desktopWindow : list) {
            WinDef.HWND hWND = desktopWindow.getHWND();
            if (hWND == null) continue;
            boolean bl2 = User32.INSTANCE.IsWindowVisible(hWND);
            if (bl && !bl2) continue;
            if (!hashMap.containsKey(hWND)) {
                IOi0I0LAnD.I1O1I1LaNd(hWND, hashMap);
            }
            IntByReference intByReference = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hWND, intByReference);
            arrayList.add(new lIOI1Land(Pointer.nativeValue(hWND.getPointer()), desktopWindow.getTitle(), desktopWindow.getFilePath(), desktopWindow.getLocAndSize(), intByReference.getValue(), (Integer)hashMap.get(hWND), bl2));
        }
        return arrayList;
    }

    private static void I1O1I1LaNd(WinDef.HWND hWND2, Map map) {
        if (hWND2 != null) {
            int n2 = 1;
            WinDef.HWND hWND3 = new WinDef.HWND(hWND2.getPointer());
            do {
                map.put(hWND3, --n2);
            } while ((hWND3 = User32.INSTANCE.GetWindow(hWND3, I1O1I1LaNd)) != null);
            int n4 = n2 * -1;
            map.replaceAll((hWND, n3) -> n3 + n4);
        }
    }
}

