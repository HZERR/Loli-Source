/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class iiIIIlO1lANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(iiIIIlO1lANd.class);

    private iiIIIlO1lANd() {
    }

    public static void I1O1I1LaNd(long l2) {
        try {
            I1O1I1LaNd.trace("Sleeping for {} ms", (Object)l2);
            Thread.sleep(l2);
        }
        catch (InterruptedException interruptedException) {
            I1O1I1LaNd.warn("Interrupted while sleeping for {} ms: {}", (Object)l2, (Object)interruptedException.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static boolean I1O1I1LaNd(String string, String string2) {
        if (string2.length() > 0 && string2.charAt(0) == '^') {
            return !iiIIIlO1lANd.I1O1I1LaNd(string, string2.substring(1));
        }
        return string.matches(string2.replace("?", ".?").replace("*", ".*?"));
    }

    public static boolean I1O1I1LaNd(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean OOOIilanD(String string) {
        return iiIIIlO1lANd.I1O1I1LaNd(string) || "unknown".equals(string);
    }
}

