/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Arrays;
import java.util.List;
import loliland.launcher.client.l0l0iIlaND;
import loliland.launcher.client.l10lO11lanD;

public abstract class lI10lAnd
implements l0l0iIlaND {
    public static final String I1O1I1LaNd = "oshi.network.filesystem.types";
    public static final String OOOIilanD = "oshi.pseudo.filesystem.types";
    protected static final List lI00OlAND = Arrays.asList(l10lO11lanD.I1O1I1LaNd("oshi.network.filesystem.types", "").split(","));
    protected static final List lli0OiIlAND = Arrays.asList(l10lO11lanD.I1O1I1LaNd("oshi.pseudo.filesystem.types", "").split(","));

    @Override
    public List I1O1I1LaNd() {
        return this.I1O1I1LaNd(false);
    }
}

