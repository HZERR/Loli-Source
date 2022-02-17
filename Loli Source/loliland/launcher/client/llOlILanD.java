/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.O1l0i01lLAND;

public class llOlILanD
extends O1l0i01lLAND {
    private final String I1O1I1LaNd;
    private final String OOOIilanD;

    public llOlILanD(String string, String string2) {
        super(string + "." + string2 + "()");
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
    }

    public String lli0OiIlAND() {
        return this.I1O1I1LaNd;
    }

    public String li0iOILAND() {
        return this.OOOIilanD;
    }

    @Override
    public int I1O1I1LaNd(O1l0i01lLAND o1l0i01lLAND) {
        if (this.lI00OlAND() == o1l0i01lLAND.lI00OlAND()) {
            return 0;
        }
        if (this.lI00OlAND() > o1l0i01lLAND.lI00OlAND()) {
            return -1;
        }
        return 1;
    }

    @Override
    public /* synthetic */ int compareTo(Object object) {
        return this.I1O1I1LaNd((O1l0i01lLAND)object);
    }
}

