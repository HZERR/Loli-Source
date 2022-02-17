/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import loliland.launcher.XLauncher;
import loliland.launcher.client.O0IlOiILAnD;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.ii0ii01LanD;
import loliland.launcher.client.l0OO0lllAnd;
import loliland.launcher.client.l0il0l1iLaNd;
import loliland.launcher.client.l11OliLAnD;
import loliland.launcher.client.l1lIOlAND;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.lI10ilAnd;
import loliland.launcher.client.lIIiO0laNd;
import loliland.launcher.client.lIOlLaND;
import loliland.launcher.client.lIl1Oi0llaND;
import loliland.launcher.client.lOO0ii0LAND;
import loliland.launcher.client.liIlILAnD;
import loliland.launcher.client.ll0111iIlAND;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.opengl.GL11;

public class OOOOllANd {
    private String I1O1I1LaNd;
    private String OOOIilanD;
    private String lI00OlAND;
    private List lli0OiIlAND;
    private Map li0iOILAND;
    private Map O1il1llOLANd;
    private List Oill1LAnD;
    private List lIOILand;
    private String lil0liLand;
    private String iilIi1laND;
    private LinkedList lli011lLANd = new LinkedList();
    private float l0illAND;
    private float IO11O0LANd;
    private float l11lLANd;

    public OOOOllANd(ii0ii01LanD ii0ii01LanD2) {
        this.I1O1I1LaNd = ii0ii01LanD2.OOOIilanD("system").Oill1LAnD();
        this.OOOIilanD = ii0ii01LanD2.OOOIilanD("display").Oill1LAnD();
        this.lI00OlAND = ii0ii01LanD2.OOOIilanD("version").Oill1LAnD();
        this.lli0OiIlAND = ii0ii01LanD2.OOOIilanD("updateVerify").I1O1I1LaNd(String.class);
        this.li0iOILAND = new HashMap();
        this.I1O1I1LaNd(ii0ii01LanD2.OOOIilanD("updateVersion").O1il1llOLANd());
        this.O1il1llOLANd = new HashMap();
        this.iOIl0LAnD();
        this.Oill1LAnD = ii0ii01LanD2.OOOIilanD("jvmArgs").I1O1I1LaNd(String.class);
        this.lIOILand = ii0ii01LanD2.OOOIilanD("classPath").I1O1I1LaNd(String.class);
        this.lil0liLand = ii0ii01LanD2.OOOIilanD("mainClass").Oill1LAnD();
        this.iilIi1laND = ii0ii01LanD2.OOOIilanD("minecraftArguments").Oill1LAnD();
    }

    private void I1O1I1LaNd(ii0ii01LanD ii0ii01LanD2) {
        for (String string : ii0ii01LanD2.OOOIilanD()) {
            this.li0iOILAND.put(string, ii0ii01LanD2.OOOIilanD(string).Oill1LAnD());
        }
    }

    private void iOIl0LAnD() {
        Object object;
        Object object2;
        String string = "{}";
        try {
            object2 = XLauncher.getStorageClients() + "clients" + File.separator + this.O1il1llOLANd() + File.separator + "version_hashes.json";
            object = new File((String)object2);
            ((File)object).getParentFile().mkdirs();
            if (!((File)object).exists()) {
                ((File)object).createNewFile();
            } else {
                string = new String(Files.readAllBytes(Paths.get((String)object2, new String[0])));
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        if (string.isEmpty()) {
            string = "{}";
        }
        if (((ii0ii01LanD)(object2 = ii0ii01LanD.I1O1I1LaNd(string))).lI00OlAND("version_hashes")) {
            object = ((ii0ii01LanD)object2).OOOIilanD("version_hashes").O1il1llOLANd();
            for (String string2 : ((ii0ii01LanD)object).OOOIilanD()) {
                this.O1il1llOLANd.put(string2, ((ii0ii01LanD)object).OOOIilanD(string2).Oill1LAnD());
            }
        }
    }

    public void I1O1I1LaNd() {
        String string = ii0ii01LanD.I1O1I1LaNd().I1O1I1LaNd("version_hashes", this.O1il1llOLANd).toString();
        String string2 = XLauncher.getStorageClients() + "clients" + File.separator + this.O1il1llOLANd() + File.separator + "version_hashes.json";
        try {
            File file = new File(string2);
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.write(Paths.get(string2, new String[0]), string.getBytes(), new OpenOption[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String OOOIilanD() {
        StringBuilder stringBuilder = new StringBuilder();
        for (liIlILAnD liIlILAnD2 : this.lli011lLANd) {
            stringBuilder.append("@").append(liIlILAnD2.OOOIilanD()).append(":").append(liIlILAnD2.lI00OlAND());
        }
        return stringBuilder.toString().replaceFirst("@", "");
    }

    public int lI00OlAND() {
        int n2 = -1;
        for (liIlILAnD liIlILAnD2 : this.lli011lLANd) {
            int n3 = liIlILAnD2.li0iOILAND();
            if (n3 < 0) continue;
            if (n2 == -1) {
                n2 = 0;
            }
            n2 += liIlILAnD2.li0iOILAND();
        }
        return n2;
    }

    public int lli0OiIlAND() {
        int n2 = 0;
        for (liIlILAnD liIlILAnD2 : this.lli011lLANd) {
            n2 += liIlILAnD2.O1il1llOLANd();
        }
        return n2;
    }

    public void I1O1I1LaNd(llIIi1lanD llIIi1lanD2, double d2, double d3) {
        boolean bl = llIIi1lanD.I1O1I1LaNd() && (double)O0IlOiILAnD.OOOIilanD() > d2 + 7.0 && (double)O0IlOiILAnD.OOOIilanD() < d2 + 328.0 && (double)O0IlOiILAnD.lI00OlAND() > d3 + 10.0 && (double)O0IlOiILAnD.lI00OlAND() < d3 + 95.0;
        this.l0illAND = llIIi1lanD.I1O1I1LaNd(bl, this.l0illAND, 0.0625f);
        lI0il11LaND.I1O1I1LaNd(d2, d3, llIIi1lanD.I1O1I1LaNd, -1, llIIi1lanD2.lli0OiIlAND, "clients/client.png");
        String string = "https://loliland.ru/assets/img/servers/" + this.I1O1I1LaNd + ".png";
        l11OliLAnD l11OliLAnD2 = l1lIOlAND.I1O1I1LaNd(string);
        lI0il11LaND.I1O1I1LaNd(l11OliLAnD2);
        int n2 = 54;
        lI0il11LaND.I1O1I1LaNd(d2 + 15.0, d3 + 26.0, (double)n2, (double)n2, 0, llIIi1lanD2.lli0OiIlAND * 0.5f);
        lI0il11LaND.I1O1I1LaNd(d2 + 13.0, d3 + 24.0, (double)n2, (double)n2, -1, llIIi1lanD2.lli0OiIlAND);
        iO11lland.lli0OiIlAND.I1O1I1LaNd(this.OOOIilanD, d2 + 90.0, d3 + 25.0, 17, 1.5, -1, llIIi1lanD2.lli0OiIlAND);
        int n3 = this.lI00OlAND();
        int n4 = this.lli0OiIlAND();
        iO11lland.OOOIilanD.I1O1I1LaNd(n3 == 0 && n4 == 0 ? "\u041e\u0431\u043d\u043e\u0432\u043b\u0435\u043d\u0438\u0435 \u043e\u043d\u043b\u0430\u0439\u043d\u0430" : (n3 != -1 ? n3 + "/" + n4 : "\u041f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430"), d2 + 90.0, d3 + 58.0, 14, 1.5, -1, (1.0f - this.l0illAND) * llIIi1lanD2.lli0OiIlAND);
        iO11lland.OOOIilanD.I1O1I1LaNd("\u0418\u0433\u0440\u0430\u0442\u044c!", d2 + 90.0, d3 + 58.0, 14, 1.5, -1, llIIi1lanD2.lli0OiIlAND * this.l0illAND);
        lI0il11LaND.I1O1I1LaNd(d2 + 26.0 + 34.0, d3 + 85.0, llIIi1lanD.I1O1I1LaNd, -1, llIIi1lanD2.lli0OiIlAND, "clients/online.png");
        double d4 = n3 != -1 ? (double)n3 / (double)n4 : 1.0;
        GL11.glEnable(3089);
        lI0il11LaND.I1O1I1LaNd((int)(d2 + 26.0 + 34.0), (int)d3 + 85, (int)(256.0 * d4), 10);
        lI0il11LaND.I1O1I1LaNd(d2 + 26.0 + 34.0, d3 + 85.0, llIIi1lanD.I1O1I1LaNd, -1, llIIi1lanD2.lli0OiIlAND, "clients/online_full.png");
        GL11.glDisable(3089);
        if (bl) {
            if (llIIi1lanD.OOOIilanD()) {
                this.iIiO00OLaNd();
            }
        }
        if (this.l0illAND > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(d2 + 26.0, d3 + 19.0, llIIi1lanD.I1O1I1LaNd, -1, llIIi1lanD2.lli0OiIlAND * this.l0illAND, "clients/icon_hover.png");
        }
    }

    private void iIiO00OLaNd() {
        lI10ilAnd.OOOIilanD().submit(() -> {
            try {
                lIOlLaND.I1O1I1LaNd("\u0417\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0434\u0430\u043d\u043d\u044b\u0445...", "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u043f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435, \u043f\u043e\u043a\u0430 \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430", "\u0434\u0430\u043d\u043d\u044b\u0445 \u043d\u0435 \u0431\u0443\u0434\u0435\u0442 \u0437\u0430\u043a\u043e\u043d\u0447\u0435\u043d\u0430");
                ii0ii01LanD ii0ii01LanD2 = l0il0l1iLaNd.I1O1I1LaNd(this.O1il1llOLANd());
                lIOlLaND.I1O1I1LaNd("\u0417\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0434\u0430\u043d\u043d\u044b\u0445...", "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u043f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435, \u043f\u043e\u043a\u0430 \u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0430", "\u0434\u0430\u043d\u043d\u044b\u0445 \u043d\u0435 \u0431\u0443\u0434\u0435\u0442 \u0437\u0430\u043a\u043e\u043d\u0447\u0435\u043d\u0430");
                HashMap hashMap = lIl1Oi0llaND.I1O1I1LaNd(ii0ii01LanD2);
                l0OO0lllAnd l0OO0lllAnd2 = new l0OO0lllAnd(this, hashMap);
                l0OO0lllAnd2.I1O1I1LaNd();
                lIOlLaND.I1O1I1LaNd();
                if (l0OO0lllAnd2.lli0OiIlAND() || l0OO0lllAnd2.OOOIilanD()) {
                    lI10ilAnd.I1O1I1LaNd().I1O1I1LaNd(() -> ll0111iIlAND.I1O1I1LaNd(new ll0111iIlAND(l0OO0lllAnd2)));
                    boolean bl = true;
                    if (l0OO0lllAnd2.lli0OiIlAND()) {
                        l0OO0lllAnd2.li0iOILAND();
                    }
                    if (l0OO0lllAnd2.OOOIilanD()) {
                        bl = l0OO0lllAnd2.lI00OlAND();
                    }
                    if (!l0OO0lllAnd2.Oill1LAnD().isEmpty()) {
                        this.lli011lLANd().putAll(l0OO0lllAnd2.Oill1LAnD());
                        this.I1O1I1LaNd();
                    }
                    if (l0OO0lllAnd2.iilIi1laND().get()) {
                        lIOlLaND.I1O1I1LaNd();
                        lI10ilAnd.I1O1I1LaNd().I1O1I1LaNd(() -> ll0111iIlAND.I1O1I1LaNd(new lOO0ii0LAND()));
                        return;
                    }
                    if (bl) {
                        lI10ilAnd.lI00OlAND().submit(() -> lIIiO0laNd.I1O1I1LaNd(this, hashMap));
                    }
                } else {
                    lI10ilAnd.lI00OlAND().submit(() -> lIIiO0laNd.I1O1I1LaNd(this, hashMap));
                }
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public long li0iOILAND() {
        if (this.lI00OlAND.equalsIgnoreCase("1.4.7")) {
            return 1000L;
        }
        return 9000L;
    }

    public String O1il1llOLANd() {
        return this.I1O1I1LaNd;
    }

    public String Oill1LAnD() {
        return this.OOOIilanD;
    }

    public String lIOILand() {
        return this.lI00OlAND;
    }

    public List lil0liLand() {
        return this.lli0OiIlAND;
    }

    public Map iilIi1laND() {
        return this.li0iOILAND;
    }

    public Map lli011lLANd() {
        return this.O1il1llOLANd;
    }

    public List l0illAND() {
        return this.Oill1LAnD;
    }

    public List IO11O0LANd() {
        return this.lIOILand;
    }

    public String l11lLANd() {
        return this.lil0liLand;
    }

    public String lO110l1LANd() {
        return this.iilIi1laND;
    }

    public LinkedList l0iIlIO1laNd() {
        return this.lli011lLANd;
    }

    private /* synthetic */ void lI00ilAND() {
        lIIiO0laNd.I1O1I1LaNd(this, new HashMap());
    }
}

