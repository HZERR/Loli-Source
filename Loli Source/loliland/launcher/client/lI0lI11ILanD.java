/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import loliland.launcher.client.l01O01Iiland;
import loliland.launcher.client.l11OliLAnD;
import loliland.launcher.client.l1lIOlAND;

public class lI0lI11ILanD
extends l11OliLAnD {
    public static ExecutorService Oill1LAnD = Executors.newFixedThreadPool(2);
    private URL lIOILand;
    private boolean lil0liLand;

    public lI0lI11ILanD(String string, String string2, String string3) {
        super(string2, string3);
        this.lil0liLand = string2 != null && string3 != null;
        try {
            this.lIOILand = new URL(string);
        }
        catch (MalformedURLException malformedURLException) {
            OOOIilanD.error("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u0430\u044f \u0441\u0441\u044b\u043b\u043a\u0430 {}", this.lIOILand);
        }
    }

    @Override
    public void lI00OlAND() {
        try {
            if (!this.lil0liLand) {
                this.Oill1LAnD();
                return;
            }
            this.O1il1llOLANd = l1lIOlAND.lI00OlAND(this.lli0OiIlAND, this.li0iOILAND);
            if (this.O1il1llOLANd == null || !this.O1il1llOLANd.exists() || !l1lIOlAND.I1O1I1LaNd(this.O1il1llOLANd, this.lIOILand)) {
                this.O1il1llOLANd = l1lIOlAND.I1O1I1LaNd(this.lIOILand, this.lli0OiIlAND, this.li0iOILAND);
            }
            try {
                this.I1O1I1LaNd();
            }
            catch (Exception exception) {
                this.Oill1LAnD();
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private void Oill1LAnD() {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection)this.lIOILand.openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 OPR/68.0.3618.173");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() / 100 == 2) {
                if (this.lil0liLand) {
                    l1lIOlAND.I1O1I1LaNd(httpURLConnection.getInputStream(), this.O1il1llOLANd);
                }
                l01O01Iiland l01O01Iiland2 = l01O01Iiland.I1O1I1LaNd(httpURLConnection.getHeaderField("Content-Type"));
                this.I1O1I1LaNd(httpURLConnection.getInputStream(), l01O01Iiland2);
            }
        }
        catch (Exception exception) {
            OOOIilanD.error("\u0427\u0451\u0442 \u043d\u0435\u043f\u043e\u043b\u0443\u0447\u0438\u043b\u043e\u0441\u044c \u0441\u043a\u0430\u0447\u0430\u0442\u044c \u0442\u0435\u043a\u0441\u0442\u0443\u0440\u043a\u0443", (Throwable)exception);
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}

