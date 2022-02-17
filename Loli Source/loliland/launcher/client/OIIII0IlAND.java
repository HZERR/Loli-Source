/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1I01lLANd;
import loliland.launcher.client.OIl1O0liLAnD;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.OlI1illAND;
import loliland.launcher.client.i1i1l11ilanD;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.iIiO00OLaNd;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.l0l00lAND;
import loliland.launcher.client.l11IlanD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.lii1IO0LaNd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OIIII0IlAND
extends O1I01lLANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(OIIII0IlAND.class);
    private static final int[] OOOIilanD = new int[OIl1O0liLAnD.values().length];
    private Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(this::ll1ILAnd);
    private String lli0OiIlAND;
    private String li0iOILAND = "";
    private String O1il1llOLANd;
    private String Oill1LAnD;
    private String lIOILand;
    private String lil0liLand;
    private String iilIi1laND;
    private OOOOO10iLAND lli011lLANd = OOOOO10iLAND.lIOILand;
    private int l0illAND;
    private int IO11O0LANd;
    private int l11lLANd;
    private long lO110l1LANd;
    private long l0iIlIO1laNd;
    private long iOIl0LAnD;
    private long iIiO00OLaNd;
    private long ii1li00Land;
    private long IOI1LaNd;
    private long lI00ilAND;
    private long l0l00lAND;
    private long iOl10IlLAnd;
    private long lIiIii1LAnD;
    private long II1Iland;

    public OIIII0IlAND(int n2) {
        super(n2);
        this.O1il1llOLANd = liOIOOlLAnD.li0iOILAND(String.format(iI11I1lllaNd.lIOILand, n2));
        this.l0IO0LAnd();
    }

    @Override
    public String lI00OlAND() {
        return this.lli0OiIlAND;
    }

    @Override
    public String lli0OiIlAND() {
        return this.li0iOILAND;
    }

    @Override
    public String li0iOILAND() {
        return this.O1il1llOLANd;
    }

    @Override
    public String O1il1llOLANd() {
        try {
            String string = String.format(iI11I1lllaNd.lil0liLand, this.I1O1I1LaNd());
            String string2 = new File(string).getCanonicalPath();
            if (!string2.equals(string)) {
                return string2;
            }
        }
        catch (IOException iOException) {
            I1O1I1LaNd.trace("Couldn't find cwd for pid {}: {}", (Object)this.I1O1I1LaNd(), (Object)iOException.getMessage());
        }
        return "";
    }

    @Override
    public String Oill1LAnD() {
        return this.Oill1LAnD;
    }

    @Override
    public String lIOILand() {
        return this.lIOILand;
    }

    @Override
    public String lil0liLand() {
        return this.lil0liLand;
    }

    @Override
    public String iilIi1laND() {
        return this.iilIi1laND;
    }

    @Override
    public OOOOO10iLAND lli011lLANd() {
        return this.lli011lLANd;
    }

    @Override
    public int l0illAND() {
        return this.l0illAND;
    }

    @Override
    public int IO11O0LANd() {
        return this.IO11O0LANd;
    }

    @Override
    public int l11lLANd() {
        return this.l11lLANd;
    }

    @Override
    public long lO110l1LANd() {
        return this.lO110l1LANd;
    }

    @Override
    public long l0iIlIO1laNd() {
        return this.l0iIlIO1laNd;
    }

    @Override
    public long iOIl0LAnD() {
        return this.iOIl0LAnD;
    }

    @Override
    public long iIiO00OLaNd() {
        return this.iIiO00OLaNd;
    }

    @Override
    public long ii1li00Land() {
        return this.IOI1LaNd;
    }

    @Override
    public long IOI1LaNd() {
        return this.ii1li00Land;
    }

    @Override
    public long lI00ilAND() {
        return this.lI00ilAND;
    }

    @Override
    public long l0l00lAND() {
        return this.l0l00lAND;
    }

    @Override
    public List liOIOOLANd() {
        return loliland.launcher.client.iIiO00OLaNd.lli0OiIlAND(this.I1O1I1LaNd()).stream().map(n2 -> new i1i1l11ilanD(this.I1O1I1LaNd(), (int)n2)).collect(Collectors.toList());
    }

    @Override
    public long II1i1l0laND() {
        return this.iOl10IlLAnd;
    }

    @Override
    public long OOOliOOllANd() {
        return this.lIiIii1LAnD;
    }

    @Override
    public long IiiilAnD() {
        return this.II1Iland;
    }

    @Override
    public long iOl10IlLAnd() {
        return loliland.launcher.client.iIiO00OLaNd.lI00OlAND(this.I1O1I1LaNd()).length;
    }

    @Override
    public int lIiIii1LAnD() {
        return (Integer)this.lI00OlAND.get();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private int ll1ILAnd() {
        byte[] arrby = new byte[5];
        if (this.li0iOILAND.isEmpty()) return 0;
        try (FileInputStream fileInputStream = new FileInputStream(this.li0iOILAND);){
            if (((InputStream)fileInputStream).read(arrby) != arrby.length) return 0;
            int n2 = arrby[4] == 1 ? 32 : 64;
            return n2;
        }
        catch (IOException iOException) {
            I1O1I1LaNd.warn("Failed to read process file: {}", (Object)this.li0iOILAND);
        }
        return 0;
    }

    @Override
    public long II1Iland() {
        String string = Iill1lanD.OOOIilanD("taskset -p " + this.I1O1I1LaNd());
        String[] arrstring = lOilLanD.OOOIilanD.split(string);
        try {
            return new BigInteger(arrstring[arrstring.length - 1], 16).longValue();
        }
        catch (NumberFormatException numberFormatException) {
            return 0L;
        }
    }

    @Override
    public boolean l0IO0LAnd() {
        Object object;
        String string = String.format(iI11I1lllaNd.iilIi1laND, this.I1O1I1LaNd());
        try {
            object = Paths.get(string, new String[0]);
            this.li0iOILAND = Files.readSymbolicLink((Path)object).toString();
            int n2 = this.li0iOILAND.indexOf(" (deleted)");
            if (n2 != -1) {
                this.li0iOILAND = this.li0iOILAND.substring(0, n2);
            }
        }
        catch (IOException | SecurityException | UnsupportedOperationException | InvalidPathException exception) {
            I1O1I1LaNd.debug("Unable to open symbolic link {}", (Object)string);
        }
        object = liOIOOlLAnD.I1O1I1LaNd(String.format(iI11I1lllaNd.l0illAND, this.I1O1I1LaNd()), ":");
        Map map = liOIOOlLAnD.I1O1I1LaNd(String.format(iI11I1lllaNd.lO110l1LANd, this.I1O1I1LaNd()), ":");
        String string2 = liOIOOlLAnD.li0iOILAND(String.format(iI11I1lllaNd.IO11O0LANd, this.I1O1I1LaNd()));
        if (string2.isEmpty()) {
            this.lli011lLANd = OOOOO10iLAND.lIOILand;
            return false;
        }
        OIIII0IlAND.I1O1I1LaNd(map, string2);
        long l2 = System.currentTimeMillis();
        long[] arrl = lOilLanD.I1O1I1LaNd(string2, OOOIilanD, loliland.launcher.client.iIiO00OLaNd.I1O1I1LaNd, ' ');
        this.ii1li00Land = (l11IlanD.lI00OlAND * l11IlanD.lI00ilAND() + arrl[OIl1O0liLAnD.lIOILand.ordinal()]) * 1000L / l11IlanD.lI00ilAND();
        if (this.ii1li00Land >= l2) {
            this.ii1li00Land = l2 - 1L;
        }
        this.l0illAND = (int)arrl[OIl1O0liLAnD.I1O1I1LaNd.ordinal()];
        this.IO11O0LANd = (int)arrl[OIl1O0liLAnD.Oill1LAnD.ordinal()];
        this.l11lLANd = (int)arrl[OIl1O0liLAnD.O1il1llOLANd.ordinal()];
        this.lO110l1LANd = arrl[OIl1O0liLAnD.lil0liLand.ordinal()];
        this.l0iIlIO1laNd = arrl[OIl1O0liLAnD.iilIi1laND.ordinal()] * OlI1illAND.I1O1I1LaNd;
        this.iOIl0LAnD = arrl[OIl1O0liLAnD.li0iOILAND.ordinal()] * 1000L / l11IlanD.lI00ilAND();
        this.iIiO00OLaNd = arrl[OIl1O0liLAnD.lli0OiIlAND.ordinal()] * 1000L / l11IlanD.lI00ilAND();
        this.iOl10IlLAnd = arrl[OIl1O0liLAnD.OOOIilanD.ordinal()];
        this.lIiIii1LAnD = arrl[OIl1O0liLAnD.lI00OlAND.ordinal()];
        long l3 = lOilLanD.OOOIilanD((String)map.get("nonvoluntary_ctxt_switches"), 0L);
        long l4 = lOilLanD.OOOIilanD((String)map.get("voluntary_ctxt_switches"), 0L);
        this.II1Iland = l4 + l3;
        this.IOI1LaNd = l2 - this.ii1li00Land;
        this.lI00ilAND = lOilLanD.OOOIilanD(object.getOrDefault("read_bytes", ""), 0L);
        this.l0l00lAND = lOilLanD.OOOIilanD(object.getOrDefault("write_bytes", ""), 0L);
        this.lIOILand = lOilLanD.OOOIilanD.split(map.getOrDefault("Uid", ""))[0];
        this.Oill1LAnD = loliland.launcher.client.l0l00lAND.I1O1I1LaNd(this.lIOILand);
        this.iilIi1laND = lOilLanD.OOOIilanD.split(map.getOrDefault("Gid", ""))[0];
        this.lil0liLand = loliland.launcher.client.l0l00lAND.OOOIilanD(this.iilIi1laND);
        this.lli0OiIlAND = map.getOrDefault("Name", "");
        this.lli011lLANd = loliland.launcher.client.iIiO00OLaNd.I1O1I1LaNd(map.getOrDefault("State", "U").charAt(0));
        return true;
    }

    private static void I1O1I1LaNd(Map map, String string) {
        String string2;
        if (map == null || string == null) {
            return;
        }
        int n2 = string.indexOf(40);
        int n3 = string.indexOf(41);
        if (iiIIIlO1lANd.I1O1I1LaNd((String)map.get("Name")) && n2 > 0 && n2 < n3) {
            string2 = string.substring(n2 + 1, n3);
            map.put("Name", string2);
        }
        if (iiIIIlO1lANd.I1O1I1LaNd((String)map.get("State")) && n3 > 0 && string.length() > n3 + 2) {
            string2 = String.valueOf(string.charAt(n3 + 2));
            map.put("State", string2);
        }
    }

    static {
        for (OIl1O0liLAnD oIl1O0liLAnD : OIl1O0liLAnD.values()) {
            OIIII0IlAND.OOOIilanD[oIl1O0liLAnD.ordinal()] = oIl1O0liLAnD.I1O1I1LaNd() - 1;
        }
    }
}

