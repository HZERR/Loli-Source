/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import loliland.launcher.XLauncher;
import loliland.launcher.client.ii0ii01LanD;
import loliland.launcher.client.l0il0l1iLaNd;
import loliland.launcher.client.l0liiO0land;
import loliland.launcher.client.lIOlLaND;
import loliland.launcher.client.lOO0ii0LAND;
import loliland.launcher.client.llIIi1lanD;

public class liiOlanD {
    private String I1O1I1LaNd;
    private int OOOIilanD;
    private String lI00OlAND;

    public void I1O1I1LaNd() {
        Object object;
        String string = XLauncher.getStorageLauncher() + "auth.json";
        String string2 = "";
        try {
            object = new File(string);
            if (!((File)object).exists()) {
                ((File)object).createNewFile();
            } else {
                string2 = new String(Files.readAllBytes(Paths.get(string, new String[0])));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (string2.isEmpty()) {
            string2 = "{}";
        }
        if (((ii0ii01LanD)(object = ii0ii01LanD.I1O1I1LaNd(string2))).lI00OlAND("login") && ((ii0ii01LanD)object).lI00OlAND("password")) {
            this.I1O1I1LaNd = ((ii0ii01LanD)object).OOOIilanD("login").Oill1LAnD();
            this.lI00OlAND = ((ii0ii01LanD)object).OOOIilanD("password").Oill1LAnD();
        }
        if (this.I1O1I1LaNd != null && this.lI00OlAND != null && !this.I1O1I1LaNd.isEmpty() && !this.lI00OlAND.isEmpty()) {
            ii0ii01LanD ii0ii01LanD2 = l0il0l1iLaNd.I1O1I1LaNd(this.I1O1I1LaNd, this.lI00OlAND);
            System.out.println(ii0ii01LanD2);
            if (!ii0ii01LanD2.lI00OlAND("type")) {
                lIOlLaND.I1O1I1LaNd(1500, "\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430!", "\u041d\u0435\u0442 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u044f \u043a", "api.loliland.ru");
                llIIi1lanD.I1O1I1LaNd(new l0liiO0land());
                return;
            }
            String string3 = ii0ii01LanD2.OOOIilanD("type").Oill1LAnD();
            if (string3.equalsIgnoreCase("error")) {
                llIIi1lanD.I1O1I1LaNd(new l0liiO0land());
                if (ii0ii01LanD2.lI00OlAND("error")) {
                    int n2 = ii0ii01LanD2.OOOIilanD("error").I1O1I1LaNd();
                    switch (n2) {
                        case 0: {
                            lIOlLaND.I1O1I1LaNd(1500, "\u041e\u0448\u0438\u0431\u043a\u0430 \u0430\u0432\u0442\u043e\u0440\u0438\u0437\u0430\u0446\u0438\u0438!", "\u041b\u043e\u0433\u0438\u043d \u0438\u043b\u0438 \u043f\u0430\u0440\u043e\u043b\u044c", "\u0432\u0432\u0435\u0434\u0435\u043d \u043d\u0435 \u0432\u0435\u0440\u043d\u043e");
                        }
                    }
                } else {
                    lIOlLaND.I1O1I1LaNd(1500, "\u041e\u0448\u0438\u0431\u043a\u0430 \u0437\u0430\u043f\u0440\u043e\u0441\u0430!", "Auth error code 1");
                }
            } else {
                this.OOOIilanD = ii0ii01LanD2.OOOIilanD("data").O1il1llOLANd().OOOIilanD("votes").I1O1I1LaNd();
                lIOlLaND.I1O1I1LaNd();
                llIIi1lanD.I1O1I1LaNd(new lOO0ii0LAND());
            }
        } else {
            llIIi1lanD.I1O1I1LaNd(new l0liiO0land());
        }
    }

    public void I1O1I1LaNd(String string, String string2) {
        ii0ii01LanD ii0ii01LanD2 = ii0ii01LanD.I1O1I1LaNd().I1O1I1LaNd("login", string).I1O1I1LaNd("password", string2);
        this.I1O1I1LaNd = string;
        this.lI00OlAND = string2;
        String string3 = ii0ii01LanD2.toString();
        String string4 = XLauncher.getStorageLauncher() + "auth.json";
        try {
            File file = new File(string4);
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.write(Paths.get(string4, new String[0]), string3.getBytes(), new OpenOption[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void OOOIilanD() {
        this.I1O1I1LaNd("", "");
    }

    public String lI00OlAND() {
        return this.I1O1I1LaNd;
    }

    public int lli0OiIlAND() {
        return this.OOOIilanD;
    }

    public void I1O1I1LaNd(int n2) {
        this.OOOIilanD = n2;
    }

    public String li0iOILAND() {
        return this.lI00OlAND;
    }
}

