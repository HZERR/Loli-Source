/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import loliland.launcher.client.i0O00iOland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class IiI1l1IllANd
implements i0O00iOland {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(IiI1l1IllANd.class);
    private static final String OOOIilanD = "nameserver";

    @Override
    public String I1O1I1LaNd() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        }
        catch (UnknownHostException unknownHostException) {
            I1O1I1LaNd.error("Unknown host exception when getting address of local host: {}", (Object)unknownHostException.getMessage());
            return "";
        }
    }

    @Override
    public String OOOIilanD() {
        try {
            String string = InetAddress.getLocalHost().getHostName();
            int n2 = string.indexOf(46);
            if (n2 == -1) {
                return string;
            }
            return string.substring(0, n2);
        }
        catch (UnknownHostException unknownHostException) {
            I1O1I1LaNd.error("Unknown host exception when getting address of local host: {}", (Object)unknownHostException.getMessage());
            return "";
        }
    }

    @Override
    public String[] lI00OlAND() {
        List list = liOIOOlLAnD.I1O1I1LaNd("/etc/resolv.conf");
        String string = OOOIilanD;
        int n2 = 3;
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i2 = 0; i2 < list.size() && arrayList.size() < n2; ++i2) {
            String string2;
            String string3 = (String)list.get(i2);
            if (!string3.startsWith(string) || (string2 = string3.substring(string.length()).replaceFirst("^[ \t]+", "")).length() == 0 || string2.charAt(0) == '#' || string2.charAt(0) == ';') continue;
            String string4 = string2.split("[ \t#;]", 2)[0];
            arrayList.add(string4);
        }
        return arrayList.toArray(new String[0]);
    }

    protected static String I1O1I1LaNd(List list) {
        for (String string : list) {
            String string2 = string.replaceFirst("^\\s+", "");
            if (!string2.startsWith("gateway:")) continue;
            String[] arrstring = lOilLanD.OOOIilanD.split(string2);
            if (arrstring.length < 2) {
                return "";
            }
            return arrstring[1].split("%")[0];
        }
        return "";
    }

    public String toString() {
        return String.format("Host name: %s, Domain name: %s, DNS servers: %s, IPv4 Gateway: %s, IPv6 Gateway: %s", this.OOOIilanD(), this.I1O1I1LaNd(), Arrays.toString(this.lI00OlAND()), this.lli0OiIlAND(), this.li0iOILAND());
    }
}

