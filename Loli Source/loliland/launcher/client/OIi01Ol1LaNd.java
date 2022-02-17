/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OIi01Ol1LaNd {
    private static ExecutorService lI00OlAND = Executors.newFixedThreadPool(3);
    public static final byte I1O1I1LaNd = 6;
    public static final int OOOIilanD = 5;
    private String lli0OiIlAND;
    private int li0iOILAND;
    private int O1il1llOLANd;
    private boolean Oill1LAnD;
    private String lIOILand;
    private String lil0liLand;
    private String iilIi1laND;
    private String lli011lLANd;
    private long l0illAND;

    public OIi01Ol1LaNd(String string, int n2) {
        this(string, n2, 5);
    }

    public OIi01Ol1LaNd(String string, int n2, int n3) {
        this.I1O1I1LaNd(string);
        this.I1O1I1LaNd(n2);
        this.OOOIilanD(n3);
        this.I1O1I1LaNd();
    }

    public boolean I1O1I1LaNd() {
        String string;
        try {
            Socket socket = new Socket();
            long l2 = System.currentTimeMillis();
            socket.connect(new InetSocketAddress(this.OOOIilanD(), this.lI00OlAND()), this.O1il1llOLANd);
            this.I1O1I1LaNd(System.currentTimeMillis() - l2);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            byte[] arrby = new byte[]{-2, 1};
            dataOutputStream.write(arrby, 0, arrby.length);
            string = bufferedReader.readLine();
            socket.close();
        }
        catch (Exception exception) {
            this.Oill1LAnD = false;
            return false;
        }
        if (string == null) {
            this.Oill1LAnD = false;
        } else {
            String[] arrstring = string.split("\u0000\u0000\u0000");
            if (arrstring.length >= 6) {
                this.Oill1LAnD = true;
                this.li0iOILAND(arrstring[2].replace("\u0000", ""));
                this.lli0OiIlAND(arrstring[3].replace("\u0000", ""));
                this.lI00OlAND(arrstring[4].replace("\u0000", ""));
                this.OOOIilanD(arrstring[5].replace("\u0000", ""));
            } else {
                this.Oill1LAnD = false;
            }
        }
        return this.Oill1LAnD;
    }

    public String OOOIilanD() {
        return this.lli0OiIlAND;
    }

    public void I1O1I1LaNd(String string) {
        this.lli0OiIlAND = string;
    }

    public int lI00OlAND() {
        return this.li0iOILAND;
    }

    public void I1O1I1LaNd(int n2) {
        this.li0iOILAND = n2;
    }

    public int lli0OiIlAND() {
        return this.O1il1llOLANd * 1000;
    }

    public void OOOIilanD(int n2) {
        this.O1il1llOLANd = n2 * 1000;
    }

    public String li0iOILAND() {
        return this.lIOILand;
    }

    public String O1il1llOLANd() {
        return this.lil0liLand;
    }

    public String Oill1LAnD() {
        return this.iilIi1laND;
    }

    public String lIOILand() {
        return this.lli011lLANd;
    }

    public long lil0liLand() {
        return this.l0illAND;
    }

    public void I1O1I1LaNd(long l2) {
        this.l0illAND = l2;
    }

    public void OOOIilanD(String string) {
        this.lli011lLANd = string;
    }

    public void lI00OlAND(String string) {
        this.iilIi1laND = string;
    }

    public void lli0OiIlAND(String string) {
        this.lIOILand = string;
    }

    public void li0iOILAND(String string) {
        this.lil0liLand = string;
    }

    public boolean iilIi1laND() {
        return this.Oill1LAnD;
    }

    public static ExecutorService lli011lLANd() {
        return lI00OlAND;
    }
}

