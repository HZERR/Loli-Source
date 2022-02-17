/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.l01l111lAnD;

public abstract class iIO0LaND
implements l01l111lAnD {
    private String I1O1I1LaNd;
    private String OOOIilanD;
    private String lI00OlAND;
    private String lli0OiIlAND;
    private String li0iOILAND;
    private String O1il1llOLANd;

    protected iIO0LaND(String string, String string2, String string3, String string4, String string5, String string6) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
        this.lI00OlAND = string3;
        this.lli0OiIlAND = string4;
        this.li0iOILAND = string5;
        this.O1il1llOLANd = string6;
    }

    protected iIO0LaND() {
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

    public String toString() {
        return "OSFileStore [name=" + this.I1O1I1LaNd() + ", volume=" + this.OOOIilanD() + ", label=" + this.lI00OlAND() + ", logicalVolume=" + this.Oill1LAnD() + ", mount=" + this.lli0OiIlAND() + ", description=" + this.lIOILand() + ", fsType=" + this.lil0liLand() + ", options=\"" + this.li0iOILAND() + "\", uuid=" + this.O1il1llOLANd() + ", freeSpace=" + this.iilIi1laND() + ", usableSpace=" + this.lli011lLANd() + ", totalSpace=" + this.l0illAND() + ", freeInodes=" + this.IO11O0LANd() + ", totalInodes=" + this.l11lLANd() + "]";
    }
}

