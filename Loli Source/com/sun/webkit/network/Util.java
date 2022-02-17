/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.network.URLs;
import java.net.MalformedURLException;

public final class Util {
    private Util() {
        throw new AssertionError();
    }

    public static String adjustUrlForWebKit(String string) throws MalformedURLException {
        int n2;
        if (URLs.newURL(string).getProtocol().equals("file") && (n2 = "file:".length()) < string.length() && string.charAt(n2) != '/') {
            string = string.substring(0, n2) + "///" + string.substring(n2);
        }
        return string;
    }

    static String formatHeaders(String string) {
        return string.trim().replaceAll("(?m)^", "    ");
    }
}

