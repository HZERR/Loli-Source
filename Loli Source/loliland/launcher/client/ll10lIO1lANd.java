/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.l10lO11lanD;

public class ll10lIO1lANd
extends RuntimeException {
    private static final long I1O1I1LaNd = -7482581936621748005L;

    public ll10lIO1lANd(String string) {
        super("Invalid property: \"" + string + "\" = " + l10lO11lanD.I1O1I1LaNd(string, null));
    }

    public ll10lIO1lANd(String string, String string2) {
        super("Invalid property \"" + string + "\": " + string2);
    }
}

