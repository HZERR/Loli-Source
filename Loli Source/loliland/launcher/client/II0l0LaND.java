/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Collections;
import java.util.List;
import loliland.launcher.client.il000OLANd;

public abstract class II0l0LaND
implements il000OLANd {
    private final String I1O1I1LaNd;
    private final String OOOIilanD;
    private final String lI00OlAND;
    private final String lli0OiIlAND;
    private final String li0iOILAND;
    private final String O1il1llOLANd;
    private final List Oill1LAnD;

    protected II0l0LaND(String string, String string2, String string3, String string4, String string5, String string6, List list) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
        this.lI00OlAND = string3;
        this.lli0OiIlAND = string4;
        this.li0iOILAND = string5;
        this.O1il1llOLANd = string6;
        this.Oill1LAnD = Collections.unmodifiableList(list);
    }

    @Override
    public String I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    @Override
    public String OOOIilanD() {
        return this.OOOIilanD;
    }

    @Override
    public String lI00OlAND() {
        return this.lI00OlAND;
    }

    @Override
    public String lli0OiIlAND() {
        return this.lli0OiIlAND;
    }

    @Override
    public String li0iOILAND() {
        return this.li0iOILAND;
    }

    @Override
    public String O1il1llOLANd() {
        return this.O1il1llOLANd;
    }

    @Override
    public List Oill1LAnD() {
        return this.Oill1LAnD;
    }

    public int I1O1I1LaNd(il000OLANd il000OLANd2) {
        return this.I1O1I1LaNd().compareTo(il000OLANd2.I1O1I1LaNd());
    }

    public String toString() {
        return II0l0LaND.I1O1I1LaNd(this, 1);
    }

    private static String I1O1I1LaNd(il000OLANd il000OLANd2, int n2) {
        String string = n2 > 4 ? String.format("%%%ds|-- ", n2 - 4) : String.format("%%%ds", n2);
        StringBuilder stringBuilder = new StringBuilder(String.format(string, ""));
        stringBuilder.append(il000OLANd2.I1O1I1LaNd());
        if (!il000OLANd2.OOOIilanD().isEmpty()) {
            stringBuilder.append(" (").append(il000OLANd2.OOOIilanD()).append(')');
        }
        if (!il000OLANd2.li0iOILAND().isEmpty()) {
            stringBuilder.append(" [s/n: ").append(il000OLANd2.li0iOILAND()).append(']');
        }
        for (il000OLANd il000OLANd3 : il000OLANd2.Oill1LAnD()) {
            stringBuilder.append('\n').append(II0l0LaND.I1O1I1LaNd(il000OLANd3, n2 + 4));
        }
        return stringBuilder.toString();
    }

    public /* synthetic */ int compareTo(Object object) {
        return this.I1O1I1LaNd((il000OLANd)object);
    }
}

