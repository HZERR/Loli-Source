/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.font.PrismFontFactory;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

class MacFontFinder {
    private static final int SystemFontType = 2;
    private static final int MonospacedFontType = 1;

    MacFontFinder() {
    }

    private static native String getFont(int var0);

    public static String getSystemFont() {
        return MacFontFinder.getFont(2);
    }

    public static String getMonospacedFont() {
        return MacFontFinder.getFont(1);
    }

    static native float getSystemFontSize();

    public static boolean populateFontFileNameMap(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, ArrayList<String>> hashMap3, Locale locale) {
        String[] arrstring;
        if (hashMap == null || hashMap2 == null || hashMap3 == null) {
            return false;
        }
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        if ((arrstring = MacFontFinder.getFontData()) == null) {
            return false;
        }
        int n2 = 0;
        while (n2 < arrstring.length) {
            String string = arrstring[n2++];
            String string2 = arrstring[n2++];
            String string3 = arrstring[n2++];
            if (!PrismFontFactory.useNativeRasterizer && string3.endsWith(".otf") && (string.indexOf(" Pro W") != -1 || string.indexOf(" ProN W") != -1 || string.indexOf(" Std W") != -1 || string.indexOf(" StdN W") != -1 || string.indexOf("Hiragino") != -1)) continue;
            if (PrismFontFactory.debugFonts) {
                System.err.println("[MacFontFinder] Name=" + string);
                System.err.println("\tFamily=" + string2);
                System.err.println("\tFile=" + string3);
            }
            String string4 = string.toLowerCase(locale);
            String string5 = string2.toLowerCase(locale);
            hashMap.put(string4, string3);
            hashMap2.put(string4, string2);
            ArrayList<String> arrayList = hashMap3.get(string5);
            if (arrayList == null) {
                arrayList = new ArrayList();
                hashMap3.put(string5, arrayList);
            }
            arrayList.add(string);
        }
        return true;
    }

    private static native String[] getFontData();

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            return null;
        });
    }
}
