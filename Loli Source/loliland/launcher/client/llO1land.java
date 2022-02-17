/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.l1i0ILaND;
import loliland.launcher.client.lOilLanD;

public class llO1land
extends l1i0ILaND {
    private int I1O1I1LaNd;
    private String OOOIilanD = "";
    private OOOOO10iLAND lI00OlAND = OOOOO10iLAND.lIOILand;
    private long lli0OiIlAND;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;
    private long lil0liLand;
    private long iilIi1laND;
    private long lli011lLANd;
    private int l0illAND;

    public llO1land(int n2, String[] arrstring) {
        super(n2);
        this.I1O1I1LaNd(arrstring);
    }

    @Override
    public int lI00OlAND() {
        return this.I1O1I1LaNd;
    }

    @Override
    public String lli0OiIlAND() {
        return this.OOOIilanD;
    }

    @Override
    public OOOOO10iLAND li0iOILAND() {
        return this.lI00OlAND;
    }

    @Override
    public long O1il1llOLANd() {
        return this.O1il1llOLANd;
    }

    @Override
    public long Oill1LAnD() {
        return this.Oill1LAnD;
    }

    @Override
    public long lIOILand() {
        return this.lli0OiIlAND;
    }

    @Override
    public long lil0liLand() {
        return this.li0iOILAND;
    }

    @Override
    public long iilIi1laND() {
        return this.lIOILand;
    }

    @Override
    public long lli011lLANd() {
        return this.lil0liLand;
    }

    @Override
    public long l0illAND() {
        return this.lli011lLANd;
    }

    @Override
    public long IO11O0LANd() {
        return this.iilIi1laND;
    }

    @Override
    public int l11lLANd() {
        return this.l0illAND;
    }

    @Override
    public boolean lO110l1LANd() {
        String string = "ps -aHwwxo tid,state,etime,cputime,nivcsw,nvcsw,majflt,minflt,pri,args -p " + this.I1O1I1LaNd();
        List list = Iill1lanD.I1O1I1LaNd(string);
        for (String string2 : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string2.trim());
            if (arrstring.length <= 1 || this.lI00OlAND() != lOilLanD.lli0OiIlAND(arrstring[1], 0)) continue;
            return this.I1O1I1LaNd(arrstring);
        }
        this.lI00OlAND = OOOOO10iLAND.lIOILand;
        return false;
    }

    private boolean I1O1I1LaNd(String[] arrstring) {
        if (arrstring.length != 10) {
            this.lI00OlAND = OOOOO10iLAND.lIOILand;
            return false;
        }
        this.I1O1I1LaNd = lOilLanD.lli0OiIlAND(arrstring[0], 0);
        switch (arrstring[1].charAt(0)) {
            case 'R': {
                this.lI00OlAND = OOOOO10iLAND.OOOIilanD;
                break;
            }
            case 'I': 
            case 'S': {
                this.lI00OlAND = OOOOO10iLAND.lI00OlAND;
                break;
            }
            case 'D': 
            case 'L': 
            case 'U': {
                this.lI00OlAND = OOOOO10iLAND.lli0OiIlAND;
                break;
            }
            case 'Z': {
                this.lI00OlAND = OOOOO10iLAND.li0iOILAND;
                break;
            }
            case 'T': {
                this.lI00OlAND = OOOOO10iLAND.O1il1llOLANd;
                break;
            }
            default: {
                this.lI00OlAND = OOOOO10iLAND.Oill1LAnD;
            }
        }
        long l2 = lOilLanD.lli0OiIlAND(arrstring[2], 0L);
        this.lli011lLANd = l2 < 1L ? 1L : l2;
        long l3 = System.currentTimeMillis();
        this.iilIi1laND = l3 - this.lli011lLANd;
        this.lIOILand = 0L;
        this.lil0liLand = lOilLanD.lli0OiIlAND(arrstring[3], 0L);
        this.O1il1llOLANd = 0L;
        long l4 = lOilLanD.OOOIilanD(arrstring[3], 0L);
        long l5 = lOilLanD.OOOIilanD(arrstring[5], 0L);
        this.Oill1LAnD = l5 + l4;
        this.li0iOILAND = lOilLanD.OOOIilanD(arrstring[6], 0L);
        this.lli0OiIlAND = lOilLanD.OOOIilanD(arrstring[7], 0L);
        this.l0illAND = lOilLanD.lli0OiIlAND(arrstring[8], 0);
        this.OOOIilanD = arrstring[9];
        return true;
    }
}

