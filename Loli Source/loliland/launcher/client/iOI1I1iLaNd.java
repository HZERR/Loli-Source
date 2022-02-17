/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class iOI1I1iLaNd {
    private static final DateTimeFormatter I1O1I1LaNd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final String OOOIilanD;
    private final String lI00OlAND;
    private final long lli0OiIlAND;
    private final String li0iOILAND;

    public iOI1I1iLaNd(String string, String string2, long l2, String string3) {
        this.OOOIilanD = string;
        this.lI00OlAND = string2;
        this.lli0OiIlAND = l2;
        this.li0iOILAND = string3;
    }

    public String I1O1I1LaNd() {
        return this.OOOIilanD;
    }

    public String OOOIilanD() {
        return this.lI00OlAND;
    }

    public long lI00OlAND() {
        return this.lli0OiIlAND;
    }

    public String lli0OiIlAND() {
        return this.li0iOILAND;
    }

    public String toString() {
        String string = this.lli0OiIlAND == 0L ? "No login" : LocalDateTime.ofInstant(Instant.ofEpochMilli(this.lli0OiIlAND), ZoneId.systemDefault()).format(I1O1I1LaNd);
        String string2 = "";
        if (!(this.li0iOILAND.isEmpty() || this.li0iOILAND.equals("::") || this.li0iOILAND.equals("0.0.0.0"))) {
            string2 = ", (" + this.li0iOILAND + ")";
        }
        return String.format("%s, %s, %s%s", this.OOOIilanD, this.lI00OlAND, string, string2);
    }
}

