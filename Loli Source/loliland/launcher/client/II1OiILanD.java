/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;
import loliland.launcher.client.I11llANd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.l0OllOOlland;
import loliland.launcher.client.l10lO11lanD;
import loliland.launcher.client.l11IlanD;
import loliland.launcher.client.l1IOlANd;
import loliland.launcher.client.lO110l1LANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.lilOIlANd;
import loliland.launcher.client.lli011lLANd;

final class II1OiILanD
extends l0OllOOlland {
    private static final String I1O1I1LaNd = "oshi.cpu.freq.path";

    II1OiILanD() {
    }

    @Override
    protected lilOIlANd IO11O0LANd() {
        Object object;
        String string = "";
        String string2 = "";
        String string3 = "";
        String string4 = "";
        String string5 = "";
        long l2 = 0L;
        boolean bl = false;
        StringBuilder stringBuilder = new StringBuilder();
        String[] arrstring = new String[]{};
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.lI00OlAND);
        block25: for (Object object2 : list) {
            object = lOilLanD.I1O1I1LaNd.split((CharSequence)object2);
            if (((String[])object).length < 2) {
                if (!((String)object2).startsWith("CPU architecture: ")) continue;
                string3 = ((String)object2).replace("CPU architecture: ", "").trim();
                continue;
            }
            block14 : switch (object[0]) {
                case "vendor_id": 
                case "CPU implementer": {
                    string = object[1];
                    break;
                }
                case "model name": 
                case "Processor": {
                    string2 = object[1];
                    break;
                }
                case "flags": {
                    for (String string6 : arrstring = object[1].toLowerCase().split(" ")) {
                        if (!"lm".equals(string6)) continue;
                        bl = true;
                        break block14;
                    }
                    continue block25;
                }
                case "stepping": {
                    string5 = object[1];
                    break;
                }
                case "CPU variant": {
                    if (stringBuilder.toString().startsWith("r")) break;
                    stringBuilder.insert(0, "r" + (String)object[1]);
                    break;
                }
                case "CPU revision": {
                    if (stringBuilder.toString().contains("p")) break;
                    stringBuilder.append('p').append(object[1]);
                    break;
                }
                case "model": 
                case "CPU part": {
                    string4 = object[1];
                    break;
                }
                case "cpu family": {
                    string3 = object[1];
                    break;
                }
                case "cpu MHz": {
                    l2 = lOilLanD.I1O1I1LaNd(object[1]);
                    break;
                }
            }
        }
        if (string2.contains("Hz")) {
            l2 = -1L;
        } else {
            long l3 = lli011lLANd.lli0OiIlAND();
            if (l3 > l2) {
                l2 = l3;
            }
        }
        if (string5.isEmpty()) {
            string5 = stringBuilder.toString();
        }
        String string7 = II1OiILanD.I1O1I1LaNd(string, string5, string4, string3, arrstring);
        if (string.startsWith("0x")) {
            Object object2;
            List list2 = Iill1lanD.I1O1I1LaNd("lscpu");
            object2 = list2.iterator();
            while (object2.hasNext()) {
                object = (String)object2.next();
                if (!((String)object).startsWith("Architecture:")) continue;
                string = ((String)object).replace("Architecture:", "").trim();
            }
        }
        return new lilOIlANd(string, string2, string3, string4, string5, string7, bl, l2);
    }

    @Override
    protected List l0illAND() {
        Map map = II1OiILanD.IOI1LaNd();
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.lI00OlAND);
        ArrayList<I11llANd> arrayList = new ArrayList<I11llANd>();
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        boolean bl = true;
        for (String string : list) {
            if (string.startsWith("processor")) {
                if (!bl) {
                    arrayList.add(new I11llANd(n2, n3, n4, map.getOrDefault(n2, 0)));
                } else {
                    bl = false;
                }
                n2 = lOilLanD.I1O1I1LaNd(string, 0);
                continue;
            }
            if (string.startsWith("core id") || string.startsWith("cpu number")) {
                n3 = lOilLanD.I1O1I1LaNd(string, 0);
                continue;
            }
            if (!string.startsWith("physical id")) continue;
            n4 = lOilLanD.I1O1I1LaNd(string, 0);
        }
        arrayList.add(new I11llANd(n2, n3, n4, map.getOrDefault(n2, 0)));
        return arrayList;
    }

    private static Map IOI1LaNd() {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        List list = Iill1lanD.I1O1I1LaNd("lscpu -p=cpu,node");
        for (String string : list) {
            String[] arrstring;
            if (string.startsWith("#") || (arrstring = string.split(",")).length != 2) continue;
            hashMap.put(lOilLanD.lli0OiIlAND(arrstring[0], 0), lOilLanD.lli0OiIlAND(arrstring[1], 0));
        }
        return hashMap;
    }

    @Override
    public long[] iIiO00OLaNd() {
        long[] arrl = lO110l1LANd.I1O1I1LaNd();
        if (LongStream.of(arrl).sum() == 0L) {
            arrl = lO110l1LANd.I1O1I1LaNd();
        }
        long l2 = l11IlanD.lI00ilAND();
        for (int i2 = 0; i2 < arrl.length; ++i2) {
            arrl[i2] = arrl[i2] * 1000L / l2;
        }
        return arrl;
    }

    @Override
    public long[] lO110l1LANd() {
        int n2;
        String string = l10lO11lanD.I1O1I1LaNd(I1O1I1LaNd, "");
        long[] arrl = new long[this.Oill1LAnD()];
        long l2 = 0L;
        for (n2 = 0; n2 < arrl.length; ++n2) {
            arrl[n2] = liOIOOlLAnD.OOOIilanD(string + "/cpu" + n2 + "/cpufreq/scaling_cur_freq");
            if (arrl[n2] == 0L) {
                arrl[n2] = liOIOOlLAnD.OOOIilanD(string + "/cpu" + n2 + "/cpufreq/cpuinfo_cur_freq");
            }
            if (l2 >= arrl[n2]) continue;
            l2 = arrl[n2];
        }
        if (l2 > 0L) {
            n2 = 0;
            while (n2 < arrl.length) {
                int n3 = n2++;
                arrl[n3] = arrl[n3] * 1000L;
            }
            return arrl;
        }
        Arrays.fill(arrl, -1L);
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.lI00OlAND);
        int n4 = 0;
        for (String string2 : list) {
            if (!string2.toLowerCase().contains("cpu mhz")) continue;
            arrl[n4] = Math.round(lOilLanD.I1O1I1LaNd(string2, 0.0) * 1000000.0);
            if (++n4 < arrl.length) continue;
            break;
        }
        return arrl;
    }

    @Override
    public long l11lLANd() {
        File file;
        File[] arrfile;
        String string = l10lO11lanD.I1O1I1LaNd(I1O1I1LaNd, "");
        long l2 = Arrays.stream(this.lI00OlAND()).max().orElse(-1L);
        if (l2 > 0L) {
            l2 /= 1000L;
        }
        if ((arrfile = (file = new File(string + "/cpufreq")).listFiles()) != null) {
            for (int i2 = 0; i2 < arrfile.length; ++i2) {
                File file2 = arrfile[i2];
                if (!file2.getName().startsWith("policy")) continue;
                long l3 = liOIOOlLAnD.OOOIilanD(string + "/cpufreq/" + file2.getName() + "/scaling_max_freq");
                if (l3 == 0L) {
                    l3 = liOIOOlLAnD.OOOIilanD(string + "/cpufreq/" + file2.getName() + "/cpuinfo_max_freq");
                }
                if (l2 >= l3) continue;
                l2 = l3;
            }
        }
        if (l2 > 0L) {
            long l4 = lli011lLANd.lli0OiIlAND();
            return l4 > (l2 *= 1000L) ? l4 : l2;
        }
        return -1L;
    }

    @Override
    public double[] I1O1I1LaNd(int n2) {
        if (n2 < 1 || n2 > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        double[] arrd = new double[n2];
        int n3 = l1IOlANd.INSTANCE.getloadavg(arrd, n2);
        if (n3 < n2) {
            for (int i2 = Math.max(n3, 0); i2 < arrd.length; ++i2) {
                arrd[i2] = -1.0;
            }
        }
        return arrd;
    }

    @Override
    public long[][] ii1li00Land() {
        long[][] arrl = lO110l1LANd.I1O1I1LaNd(this.Oill1LAnD());
        if (LongStream.of(arrl[0]).sum() == 0L) {
            arrl = lO110l1LANd.I1O1I1LaNd(this.Oill1LAnD());
        }
        long l2 = l11IlanD.lI00ilAND();
        for (int i2 = 0; i2 < arrl.length; ++i2) {
            for (int i3 = 0; i3 < arrl[i2].length; ++i3) {
                arrl[i2][i3] = arrl[i2][i3] * 1000L / l2;
            }
        }
        return arrl;
    }

    private static String I1O1I1LaNd(String string, String string2, String string3, String string4, String[] arrstring) {
        boolean bl = false;
        String string5 = "Processor Information";
        for (String string6 : Iill1lanD.I1O1I1LaNd("dmidecode -t 4")) {
            if (!bl && string6.contains(string5)) {
                string5 = "ID:";
                bl = true;
                continue;
            }
            if (!bl || !string6.contains(string5)) continue;
            return string6.split(string5)[1].trim();
        }
        string5 = "eax=";
        for (String string6 : Iill1lanD.I1O1I1LaNd("cpuid -1r")) {
            if (!string6.contains(string5) || !string6.trim().startsWith("0x00000001")) continue;
            String string7 = "";
            String string8 = "";
            for (String string9 : lOilLanD.OOOIilanD.split(string6)) {
                if (string9.startsWith("eax=")) {
                    string7 = lOilLanD.OOOIilanD(string9, "eax=0x");
                    continue;
                }
                if (!string9.startsWith("edx=")) continue;
                string8 = lOilLanD.OOOIilanD(string9, "edx=0x");
            }
            return string8 + string7;
        }
        if (string.startsWith("0x")) {
            return II1OiILanD.I1O1I1LaNd(string, string2, string3, string4) + "00000000";
        }
        return II1OiILanD.I1O1I1LaNd(string2, string3, string4, arrstring);
    }

    private static String I1O1I1LaNd(String string, String string2, String string3, String string4) {
        int n2 = 0;
        if (string2.startsWith("r") && string2.contains("p")) {
            String[] arrstring = string2.substring(1).split("p");
            n2 |= lOilLanD.I1O1I1LaNd(arrstring[1], 0);
            n2 |= lOilLanD.I1O1I1LaNd(arrstring[0], 0) << 20;
        }
        n2 |= lOilLanD.I1O1I1LaNd(string3, 0) << 4;
        n2 |= lOilLanD.I1O1I1LaNd(string4, 0) << 16;
        return String.format("%08X", n2 |= lOilLanD.I1O1I1LaNd(string, 0) << 24);
    }

    @Override
    public long l0iIlIO1laNd() {
        return lO110l1LANd.OOOIilanD();
    }

    @Override
    public long iOIl0LAnD() {
        return lO110l1LANd.lI00OlAND();
    }
}

