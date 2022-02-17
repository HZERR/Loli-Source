/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.LinkedList;
import java.util.List;
import loliland.launcher.client.I1IIll1LaND;
import loliland.launcher.client.I1Ol1IlaND;
import loliland.launcher.client.O1Ol0i0LAnd;
import loliland.launcher.client.OOOIilanD;
import loliland.launcher.client.OilILAnD;
import loliland.launcher.client.Oli0liiLAnd;
import loliland.launcher.client.iIlI0LAND;
import loliland.launcher.client.ii0ii01LanD;
import loliland.launcher.client.l0i1IIOlaND;
import loliland.launcher.client.l110LAND;
import loliland.launcher.client.l1ll0llAND;
import loliland.launcher.client.lO1O1I01lAnd;
import loliland.launcher.client.lOllOiLANd;
import loliland.launcher.client.lilOIlANd;
import loliland.launcher.client.ll1OLAnd;

public class OIOiO10LaND {
    private I1Ol1IlaND I1O1I1LaNd;
    private lO1O1I01lAnd OOOIilanD;
    private l0i1IIOlaND lI00OlAND;
    private List lli0OiIlAND;
    private List li0iOILAND;
    private long O1il1llOLANd;

    public static OIOiO10LaND I1O1I1LaNd() {
        OOOIilanD oOOIilanD = new OOOIilanD();
        lOllOiLANd lOllOiLANd2 = oOOIilanD.lli0OiIlAND();
        Oli0liiLAnd oli0liiLAnd = lOllOiLANd2.OOOIilanD();
        lilOIlANd lilOIlANd2 = oli0liiLAnd.I1O1I1LaNd();
        OilILAnD oilILAnD = lOllOiLANd2.lI00OlAND();
        iIlI0LAND iIlI0LAND2 = lOllOiLANd2.I1O1I1LaNd();
        ll1OLAnd ll1OLAnd2 = iIlI0LAND2.O1il1llOLANd();
        List list = lOllOiLANd2.lli011lLANd();
        List list2 = O1Ol0i0LAnd.I1O1I1LaNd(oOOIilanD);
        LinkedList<l1ll0llAND> linkedList = new LinkedList<l1ll0llAND>();
        for (l110LAND l110LAND2 : list) {
            String string = l110LAND2.lI00OlAND();
            if (string.endsWith(")") && string.contains("(")) {
                String[] arrstring = string.split(" ");
                string = string.replace(arrstring[arrstring.length - 1], "").trim();
            }
            linkedList.add(new l1ll0llAND(l110LAND2.I1O1I1LaNd(), string, l110LAND2.li0iOILAND()));
        }
        return new OIOiO10LaND(new I1Ol1IlaND(System.getProperty("os.name"), System.getProperty("os.arch"), I1IIll1LaND.lli0OiIlAND()), new lO1O1I01lAnd(lilOIlANd2.O1il1llOLANd(), lilOIlANd2.OOOIilanD(), oli0liiLAnd.lIOILand(), oli0liiLAnd.Oill1LAnD(), oli0liiLAnd.OOOIilanD()), new l0i1IIOlaND(iIlI0LAND2.I1O1I1LaNd(), ll1OLAnd2.I1O1I1LaNd(), ll1OLAnd2.lli0OiIlAND(), iIlI0LAND2.lli0OiIlAND()), linkedList, new LinkedList(list2), oilILAnD.I1O1I1LaNd());
    }

    public ii0ii01LanD OOOIilanD() {
        return ii0ii01LanD.I1O1I1LaNd().I1O1I1LaNd("operatingSystem", this.I1O1I1LaNd.I1O1I1LaNd()).I1O1I1LaNd("processor", this.OOOIilanD.I1O1I1LaNd()).I1O1I1LaNd("baseboard", this.lI00OlAND.I1O1I1LaNd()).I1O1I1LaNd("graphicsCards", this.lli0OiIlAND).I1O1I1LaNd("displays", this.li0iOILAND).I1O1I1LaNd("totalMemory", this.O1il1llOLANd);
    }

    public I1Ol1IlaND lI00OlAND() {
        return this.I1O1I1LaNd;
    }

    public lO1O1I01lAnd lli0OiIlAND() {
        return this.OOOIilanD;
    }

    public l0i1IIOlaND li0iOILAND() {
        return this.lI00OlAND;
    }

    public List O1il1llOLANd() {
        return this.lli0OiIlAND;
    }

    public List Oill1LAnD() {
        return this.li0iOILAND;
    }

    public long lIOILand() {
        return this.O1il1llOLANd;
    }

    public OIOiO10LaND(I1Ol1IlaND i1Ol1IlaND, lO1O1I01lAnd lO1O1I01lAnd2, l0i1IIOlaND l0i1IIOlaND2, List list, List list2, long l2) {
        this.I1O1I1LaNd = i1Ol1IlaND;
        this.OOOIilanD = lO1O1I01lAnd2;
        this.lI00OlAND = l0i1IIOlaND2;
        this.lli0OiIlAND = list;
        this.li0iOILAND = list2;
        this.O1il1llOLANd = l2;
    }
}

