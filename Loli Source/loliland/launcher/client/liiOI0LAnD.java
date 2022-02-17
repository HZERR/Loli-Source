/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.llllil1LaND;

final class liiOI0LAnD
extends llllil1LaND {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(liiOI0LAnD::Oill1LAnD, lii1IO0LaNd.I1O1I1LaNd());

    liiOI0LAnD() {
    }

    @Override
    public double lli0OiIlAND() {
        return (Double)((IIOOOlIiLanD)this.I1O1I1LaNd.get()).I1O1I1LaNd();
    }

    @Override
    public int[] li0iOILAND() {
        return (int[])((IIOOOlIiLanD)this.I1O1I1LaNd.get()).OOOIilanD();
    }

    @Override
    public double O1il1llOLANd() {
        return (Double)((IIOOOlIiLanD)this.I1O1I1LaNd.get()).lI00OlAND();
    }

    private static IIOOOlIiLanD Oill1LAnD() {
        Object[] arrobject;
        double d2 = 0.0;
        ArrayList<Double> arrayList = new ArrayList<Double>();
        ArrayList<Double> arrayList2 = new ArrayList<Double>();
        ArrayList<Integer> arrayList3 = new ArrayList<Integer>();
        for (String string : Iill1lanD.I1O1I1LaNd("systat -ab sensors")) {
            arrobject = lOilLanD.OOOIilanD.split(string);
            if (arrobject.length <= 1) continue;
            if (arrobject[0].contains("cpu")) {
                if (arrobject[0].contains("temp0")) {
                    arrayList.add(lOilLanD.OOOIilanD(arrobject[1], Double.NaN));
                    continue;
                }
                if (!arrobject[0].contains("volt0")) continue;
                d2 = lOilLanD.OOOIilanD(arrobject[1], 0.0);
                continue;
            }
            if (arrobject[0].contains("temp0")) {
                arrayList2.add(lOilLanD.OOOIilanD(arrobject[1], Double.NaN));
                continue;
            }
            if (!arrobject[0].contains("fan")) continue;
            arrayList3.add(lOilLanD.lli0OiIlAND(arrobject[1], 0));
        }
        double d3 = arrayList.isEmpty() ? liiOI0LAnD.I1O1I1LaNd(arrayList2) : liiOI0LAnD.I1O1I1LaNd(arrayList);
        arrobject = new int[arrayList3.size()];
        for (int i2 = 0; i2 < arrobject.length; ++i2) {
            arrobject[i2] = (String)((Object)((Integer)arrayList3.get(i2)));
        }
        return new IIOOOlIiLanD(d3, arrobject, d2);
    }

    private static double I1O1I1LaNd(List list) {
        double d2 = 0.0;
        int n2 = 0;
        for (Double d3 : list) {
            if (d3.isNaN()) continue;
            d2 += d3.doubleValue();
            ++n2;
        }
        return n2 > 0 ? d2 / (double)n2 : 0.0;
    }
}

