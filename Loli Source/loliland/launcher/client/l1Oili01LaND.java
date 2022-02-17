/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

public class l1Oili01LaND {
    private String I1O1I1LaNd;
    private String OOOIilanD;
    private String lI00OlAND;

    public l1Oili01LaND(String string, String string2, String string3) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
        this.lI00OlAND = string3;
    }

    public String I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    public String OOOIilanD() {
        return this.OOOIilanD;
    }

    public String lI00OlAND() {
        return this.lI00OlAND;
    }

    public String lli0OiIlAND() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\\').append(this.I1O1I1LaNd);
        if (this.OOOIilanD != null) {
            stringBuilder.append('(').append(this.OOOIilanD).append(')');
        }
        stringBuilder.append('\\').append(this.lI00OlAND);
        return stringBuilder.toString();
    }
}

