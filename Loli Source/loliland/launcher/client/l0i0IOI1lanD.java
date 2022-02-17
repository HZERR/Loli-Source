/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.IIIOlaNd;
import loliland.launcher.client.iIO0LaND;
import loliland.launcher.client.l01l111lAnD;

public class l0i0IOI1lanD
extends iIO0LaND {
    private String I1O1I1LaNd;
    private String OOOIilanD;
    private String lI00OlAND;
    private long lli0OiIlAND;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;

    public l0i0IOI1lanD(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, long l2, long l3, long l4, long l5, long l6) {
        super(string, string2, string3, string4, string5, string6);
        this.I1O1I1LaNd = string7;
        this.OOOIilanD = string8;
        this.lI00OlAND = string9;
        this.lli0OiIlAND = l2;
        this.li0iOILAND = l3;
        this.O1il1llOLANd = l4;
        this.Oill1LAnD = l5;
        this.lIOILand = l6;
    }

    @Override
    public String Oill1LAnD() {
        return this.I1O1I1LaNd;
    }

    @Override
    public String lIOILand() {
        return this.OOOIilanD;
    }

    @Override
    public String lil0liLand() {
        return this.lI00OlAND;
    }

    @Override
    public long iilIi1laND() {
        return this.lli0OiIlAND;
    }

    @Override
    public long lli011lLANd() {
        return this.li0iOILAND;
    }

    @Override
    public long l0illAND() {
        return this.O1il1llOLANd;
    }

    @Override
    public long IO11O0LANd() {
        return this.Oill1LAnD;
    }

    @Override
    public long l11lLANd() {
        return this.lIOILand;
    }

    @Override
    public boolean lO110l1LANd() {
        for (l01l111lAnD l01l111lAnD2 : new IIIOlaNd().I1O1I1LaNd()) {
            if (!this.I1O1I1LaNd().equals(l01l111lAnD2.I1O1I1LaNd()) || !this.OOOIilanD().equals(l01l111lAnD2.OOOIilanD()) || !this.lli0OiIlAND().equals(l01l111lAnD2.lli0OiIlAND())) continue;
            this.I1O1I1LaNd = l01l111lAnD2.Oill1LAnD();
            this.OOOIilanD = l01l111lAnD2.lIOILand();
            this.lI00OlAND = l01l111lAnD2.lil0liLand();
            this.lli0OiIlAND = l01l111lAnD2.iilIi1laND();
            this.li0iOILAND = l01l111lAnD2.lli011lLANd();
            this.O1il1llOLANd = l01l111lAnD2.l0illAND();
            this.Oill1LAnD = l01l111lAnD2.IO11O0LANd();
            this.lIOILand = l01l111lAnD2.l11lLANd();
            return true;
        }
        return false;
    }
}

