/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import loliland.launcher.client.I1O1I1LaNd;
import loliland.launcher.client.OOOIilanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Iill1lanD {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(Iill1lanD.class);
    private static final String[] OOOIilanD = Iill1lanD.I1O1I1LaNd();

    private Iill1lanD() {
    }

    private static String[] I1O1I1LaNd() {
        I1O1I1LaNd i1O1I1LaNd = loliland.launcher.client.OOOIilanD.I1O1I1LaNd();
        if (i1O1I1LaNd == loliland.launcher.client.I1O1I1LaNd.I1O1I1LaNd) {
            return new String[]{"LANGUAGE=C"};
        }
        if (i1O1I1LaNd != loliland.launcher.client.I1O1I1LaNd.lil0liLand) {
            return new String[]{"LC_ALL=C"};
        }
        return null;
    }

    public static List I1O1I1LaNd(String string) {
        String[] arrstring = string.split(" ");
        return Iill1lanD.I1O1I1LaNd(arrstring);
    }

    public static List I1O1I1LaNd(String[] arrstring) {
        return Iill1lanD.I1O1I1LaNd(arrstring, OOOIilanD);
    }

    public static List I1O1I1LaNd(String[] arrstring, String[] arrstring2) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(arrstring, arrstring2);
        }
        catch (IOException | SecurityException exception) {
            I1O1I1LaNd.trace("Couldn't run command {}: {}", (Object)Arrays.toString(arrstring), (Object)exception.getMessage());
            return new ArrayList(0);
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()));){
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                arrayList.add(string);
            }
            process.waitFor();
        }
        catch (IOException iOException) {
            I1O1I1LaNd.trace("Problem reading output from {}: {}", (Object)Arrays.toString(arrstring), (Object)iOException.getMessage());
            return new ArrayList(0);
        }
        catch (InterruptedException interruptedException) {
            I1O1I1LaNd.trace("Interrupted while reading output from {}: {}", (Object)Arrays.toString(arrstring), (Object)interruptedException.getMessage());
            Thread.currentThread().interrupt();
        }
        return arrayList;
    }

    public static String OOOIilanD(String string) {
        return Iill1lanD.I1O1I1LaNd(string, 0);
    }

    public static String I1O1I1LaNd(String string, int n2) {
        List list = Iill1lanD.I1O1I1LaNd(string);
        if (n2 >= 0 && n2 < list.size()) {
            return (String)list.get(n2);
        }
        return "";
    }
}

