/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import loliland.launcher.XLauncher;
import loliland.launcher.client.ii0ii01LanD;

public class l0il0l1iLaNd {
    static LinkedHashMap I1O1I1LaNd = new LinkedHashMap();

    public static ii0ii01LanD I1O1I1LaNd() {
        String string = l0il0l1iLaNd.OOOIilanD(l0il0l1iLaNd.lI00OlAND() + "sync");
        return ii0ii01LanD.I1O1I1LaNd(string.isEmpty() ? "{}" : string);
    }

    public static ii0ii01LanD I1O1I1LaNd(String string) {
        String string2 = l0il0l1iLaNd.OOOIilanD(l0il0l1iLaNd.lI00OlAND() + "hash/" + string);
        return ii0ii01LanD.I1O1I1LaNd(string2.isEmpty() ? "{}" : string2);
    }

    public static String I1O1I1LaNd(String string, String string2, String string3) {
        for (Map.Entry entry : I1O1I1LaNd.entrySet()) {
            string2 = string2.replace((CharSequence)entry.getKey(), (CharSequence)entry.getValue());
        }
        return l0il0l1iLaNd.lI00OlAND() + "download/" + string + "/" + string2;
    }

    public static ii0ii01LanD I1O1I1LaNd(String string, String string2) {
        return l0il0l1iLaNd.I1O1I1LaNd(l0il0l1iLaNd.lli0OiIlAND(), "launcher.auth", ii0ii01LanD.I1O1I1LaNd().I1O1I1LaNd("login", string).I1O1I1LaNd("password", string2).I1O1I1LaNd("hardware_data", XLauncher.getSystemData().OOOIilanD()));
    }

    public static ii0ii01LanD OOOIilanD() {
        String string = l0il0l1iLaNd.OOOIilanD(l0il0l1iLaNd.lli0OiIlAND() + "launcher.address");
        return ii0ii01LanD.I1O1I1LaNd(string.isEmpty() ? "{}" : string);
    }

    private static String lI00OlAND() {
        return "https://launcher.loliland.pro/";
    }

    private static String lli0OiIlAND() {
        return "https://api.loliland.ru/";
    }

    public static String OOOIilanD(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            long l2 = System.currentTimeMillis();
            URL uRL = new URL(string.trim());
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 OPR/68.0.3618.173");
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream(), StandardCharsets.UTF_8));){
                String string2;
                while ((string2 = bufferedReader.readLine()) != null) {
                    stringBuilder.append(string2);
                }
            }
            long l3 = System.currentTimeMillis();
            System.out.println(string + " loaded in " + (l3 - l2) + " ms.");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static ii0ii01LanD I1O1I1LaNd(String string, String string2, ii0ii01LanD ii0ii01LanD2) {
        String string3 = "";
        try {
            Object object;
            URL uRL = new URL(string + string2);
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            httpURLConnection.setRequestProperty("Accept", "text/plain");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 OPR/68.0.3618.173");
            httpURLConnection.setDoOutput(true);
            try (Closeable closeable = httpURLConnection.getOutputStream();){
                object = ii0ii01LanD2.toString().getBytes(StandardCharsets.UTF_8);
                ((OutputStream)closeable).write((byte[])object, 0, ((byte[])object).length);
            }
            closeable = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
            var7_8 = null;
            try {
                String string4;
                object = new StringBuilder();
                while ((string4 = ((BufferedReader)closeable).readLine()) != null) {
                    ((StringBuilder)object).append(string4.trim());
                }
                string3 = ((StringBuilder)object).toString();
            }
            catch (Throwable throwable) {
                var7_8 = throwable;
                throw throwable;
            }
            finally {
                if (closeable != null) {
                    if (var7_8 != null) {
                        try {
                            ((BufferedReader)closeable).close();
                        }
                        catch (Throwable throwable) {
                            var7_8.addSuppressed(throwable);
                        }
                    } else {
                        ((BufferedReader)closeable).close();
                    }
                }
            }
            System.out.println(string + string2 + ": " + string3);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return ii0ii01LanD.I1O1I1LaNd(string3.trim().isEmpty() ? "{}" : string3);
    }

    static {
        I1O1I1LaNd.put("%", "%25");
        I1O1I1LaNd.put("!", "%21");
        I1O1I1LaNd.put("#", "%23");
        I1O1I1LaNd.put("$", "%24");
        I1O1I1LaNd.put("&", "%26");
        I1O1I1LaNd.put("'", "%27");
        I1O1I1LaNd.put("(", "%28");
        I1O1I1LaNd.put(")", "%29");
        I1O1I1LaNd.put("*", "%2A");
        I1O1I1LaNd.put("+", "%2B");
        I1O1I1LaNd.put(",", "%E2%80%9A");
        I1O1I1LaNd.put("-", "%2D");
        I1O1I1LaNd.put(":", "%3A");
        I1O1I1LaNd.put(";", "%3B");
        I1O1I1LaNd.put("<", "%3C");
        I1O1I1LaNd.put("=", "%3D");
        I1O1I1LaNd.put(">", "%3E");
        I1O1I1LaNd.put("?", "%3F");
        I1O1I1LaNd.put("@", "%40");
        I1O1I1LaNd.put("[", "%5B");
        I1O1I1LaNd.put("\\", "%5C");
        I1O1I1LaNd.put("]", "%5D");
        I1O1I1LaNd.put("^", "%5E");
        I1O1I1LaNd.put("`", "%60");
        I1O1I1LaNd.put("{", "%7B");
        I1O1I1LaNd.put("|", "%7C");
        I1O1I1LaNd.put("}", "%7D");
        I1O1I1LaNd.put("~", "%7E");
        I1O1I1LaNd.put(" ", "%20");
    }
}

