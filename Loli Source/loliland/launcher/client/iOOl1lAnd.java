/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import loliland.launcher.client.O1iOl0lAND;
import loliland.launcher.client.O1liliLANd;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.l1i0ILaND;

public class iOOl1lAnd
extends l1i0ILaND {
    private final int I1O1I1LaNd;
    private String OOOIilanD;
    private OOOOO10iLAND lI00OlAND;
    private long lli0OiIlAND;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;
    private long lil0liLand;
    private int iilIi1laND;

    public iOOl1lAnd(int n2, int n3, String string, O1iOl0lAND o1iOl0lAND) {
        super(n2);
        this.I1O1I1LaNd = n3;
        this.I1O1I1LaNd(string, o1iOl0lAND);
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
        return this.lli0OiIlAND;
    }

    @Override
    public long Oill1LAnD() {
        return this.li0iOILAND;
    }

    @Override
    public long iilIi1laND() {
        return this.O1il1llOLANd;
    }

    @Override
    public long lli011lLANd() {
        return this.Oill1LAnD;
    }

    @Override
    public long IO11O0LANd() {
        return this.lIOILand;
    }

    @Override
    public long l0illAND() {
        return this.lil0liLand;
    }

    @Override
    public int l11lLANd() {
        return this.iilIi1laND;
    }

    @Override
    public boolean lO110l1LANd() {
        Set<Integer> set = Collections.singleton(this.I1O1I1LaNd());
        Map map = O1liliLANd.I1O1I1LaNd(set);
        if (map == null) {
            map = O1liliLANd.OOOIilanD(set);
        }
        return this.I1O1I1LaNd(this.OOOIilanD.split("/")[0], (O1iOl0lAND)map.get(this.lI00OlAND()));
    }

    private boolean I1O1I1LaNd(String string, O1iOl0lAND o1iOl0lAND) {
        if (o1iOl0lAND == null) {
            this.lI00OlAND = OOOOO10iLAND.lIOILand;
            return false;
        }
        this.OOOIilanD = o1iOl0lAND.I1O1I1LaNd().contains("/") || string.isEmpty() ? o1iOl0lAND.I1O1I1LaNd() : string + "/" + o1iOl0lAND.I1O1I1LaNd();
        if (o1iOl0lAND.lil0liLand() == 5) {
            this.lI00OlAND = OOOOO10iLAND.lil0liLand;
        } else {
            switch (o1iOl0lAND.lIOILand()) {
                case 0: {
                    this.lI00OlAND = OOOOO10iLAND.I1O1I1LaNd;
                    break;
                }
                case 2: 
                case 3: {
                    this.lI00OlAND = OOOOO10iLAND.OOOIilanD;
                    break;
                }
                case 4: {
                    this.lI00OlAND = OOOOO10iLAND.O1il1llOLANd;
                    break;
                }
                case 5: {
                    this.lI00OlAND = OOOOO10iLAND.lI00OlAND;
                    break;
                }
                case 1: 
                case 6: {
                    this.lI00OlAND = OOOOO10iLAND.lli0OiIlAND;
                    break;
                }
                default: {
                    this.lI00OlAND = OOOOO10iLAND.Oill1LAnD;
                }
            }
        }
        this.lli0OiIlAND = o1iOl0lAND.iilIi1laND();
        this.li0iOILAND = o1iOl0lAND.lli011lLANd();
        this.O1il1llOLANd = o1iOl0lAND.O1il1llOLANd();
        this.Oill1LAnD = o1iOl0lAND.li0iOILAND();
        this.lIOILand = o1iOl0lAND.lli0OiIlAND();
        this.lil0liLand = System.currentTimeMillis() - o1iOl0lAND.lli0OiIlAND();
        this.iilIi1laND = o1iOl0lAND.Oill1LAnD();
        return true;
    }
}

