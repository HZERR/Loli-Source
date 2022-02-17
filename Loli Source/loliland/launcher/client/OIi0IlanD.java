/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OO1iiOlanD;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.l1i0ILaND;
import loliland.launcher.client.lOilLanD;

public class OIi0IlanD
extends l1i0ILaND {
    private int I1O1I1LaNd;
    private OOOOO10iLAND OOOIilanD = OOOOO10iLAND.lIOILand;
    private long lI00OlAND;
    private long lli0OiIlAND;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private int lIOILand;

    public OIi0IlanD(int n2, String[] arrstring) {
        super(n2);
        this.I1O1I1LaNd(arrstring);
    }

    @Override
    public int lI00OlAND() {
        return this.I1O1I1LaNd;
    }

    @Override
    public OOOOO10iLAND li0iOILAND() {
        return this.OOOIilanD;
    }

    @Override
    public long Oill1LAnD() {
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
        return this.Oill1LAnD;
    }

    @Override
    public long IO11O0LANd() {
        return this.O1il1llOLANd;
    }

    @Override
    public int l11lLANd() {
        return this.lIOILand;
    }

    @Override
    public boolean lO110l1LANd() {
        List list = Iill1lanD.I1O1I1LaNd("ps -m -o THREAD -p " + this.I1O1I1LaNd());
        if (list.size() > 2) {
            list.remove(0);
            list.remove(0);
            for (String string : list) {
                String[] arrstring = lOilLanD.OOOIilanD.split(string.trim());
                if (arrstring.length != 13 || !arrstring[3].equals(String.valueOf(this.lI00OlAND()))) continue;
                String[] arrstring2 = new String[]{arrstring[3], arrstring[4], arrstring[6]};
                this.I1O1I1LaNd(arrstring2);
            }
        }
        this.OOOIilanD = OOOOO10iLAND.lIOILand;
        return false;
    }

    private boolean I1O1I1LaNd(String[] arrstring) {
        this.I1O1I1LaNd = lOilLanD.lli0OiIlAND(arrstring[0], 0);
        this.OOOIilanD = OO1iiOlanD.I1O1I1LaNd(arrstring[1].charAt(0));
        this.lIOILand = lOilLanD.lli0OiIlAND(arrstring[2], 0);
        return true;
    }
}

