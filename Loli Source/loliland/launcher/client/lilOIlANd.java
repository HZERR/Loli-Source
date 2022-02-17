/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Properties;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.lii1IO0LaNd;

public final class lilOIlANd {
    private static final String I1O1I1LaNd = "oshi.architecture.properties";
    private final String OOOIilanD;
    private final String lI00OlAND;
    private final String lli0OiIlAND;
    private final String li0iOILAND;
    private final String O1il1llOLANd;
    private final String Oill1LAnD;
    private final String lIOILand;
    private final boolean lil0liLand;
    private final long iilIi1laND;
    private final Supplier lli011lLANd = lii1IO0LaNd.I1O1I1LaNd(this::lli011lLANd);

    public lilOIlANd(String string, String string2, String string3, String string4, String string5, String string6, boolean bl) {
        this(string, string2, string3, string4, string5, string6, bl, -1L);
    }

    public lilOIlANd(String string, String string2, String string3, String string4, String string5, String string6, boolean bl, long l2) {
        this.OOOIilanD = string;
        this.lI00OlAND = string2;
        this.lli0OiIlAND = string3;
        this.li0iOILAND = string4;
        this.O1il1llOLANd = string5;
        this.Oill1LAnD = string6;
        this.lil0liLand = bl;
        StringBuilder stringBuilder = new StringBuilder();
        if (string.contentEquals("GenuineIntel")) {
            stringBuilder.append(bl ? "Intel64" : "x86");
        } else {
            stringBuilder.append(string);
        }
        stringBuilder.append(" Family ").append(string3);
        stringBuilder.append(" Model ").append(string4);
        stringBuilder.append(" Stepping ").append(string5);
        this.lIOILand = stringBuilder.toString();
        if (l2 > 0L) {
            this.iilIi1laND = l2;
        } else {
            Pattern pattern = Pattern.compile("@ (.*)$");
            Matcher matcher = pattern.matcher(string2);
            if (matcher.find()) {
                String string7 = matcher.group(1);
                this.iilIi1laND = lOilLanD.I1O1I1LaNd(string7);
            } else {
                this.iilIi1laND = -1L;
            }
        }
    }

    public String I1O1I1LaNd() {
        return this.OOOIilanD;
    }

    public String OOOIilanD() {
        return this.lI00OlAND;
    }

    public String lI00OlAND() {
        return this.lli0OiIlAND;
    }

    public String lli0OiIlAND() {
        return this.li0iOILAND;
    }

    public String li0iOILAND() {
        return this.O1il1llOLANd;
    }

    public String O1il1llOLANd() {
        return this.Oill1LAnD;
    }

    public String Oill1LAnD() {
        return this.lIOILand;
    }

    public boolean lIOILand() {
        return this.lil0liLand;
    }

    public long lil0liLand() {
        return this.iilIi1laND;
    }

    public String iilIi1laND() {
        return (String)this.lli011lLANd.get();
    }

    private String lli011lLANd() {
        String string = null;
        Properties properties = liOIOOlLAnD.O1il1llOLANd(I1O1I1LaNd);
        StringBuilder stringBuilder = new StringBuilder();
        String string2 = this.OOOIilanD.toUpperCase();
        if (string2.contains("AMD")) {
            stringBuilder.append("amd.");
        } else if (string2.contains("ARM")) {
            stringBuilder.append("arm.");
        } else if (string2.contains("IBM")) {
            int n2 = this.lI00OlAND.indexOf("_POWER");
            if (n2 > 0) {
                string = this.lI00OlAND.substring(n2 + 1);
            }
        } else if (string2.contains("APPLE")) {
            stringBuilder.append("apple.");
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string) && !stringBuilder.toString().equals("arm.")) {
            stringBuilder.append(this.lli0OiIlAND);
            string = properties.getProperty(stringBuilder.toString());
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string)) {
            stringBuilder.append('.').append(this.li0iOILAND);
            string = properties.getProperty(stringBuilder.toString());
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string)) {
            stringBuilder.append('.').append(this.O1il1llOLANd);
            string = properties.getProperty(stringBuilder.toString());
        }
        return iiIIIlO1lANd.I1O1I1LaNd(string) ? "unknown" : string;
    }

    public String toString() {
        return this.Oill1LAnD();
    }
}

