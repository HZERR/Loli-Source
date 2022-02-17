/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.linux.LibC;
import com.sun.jna.ptr.PointerByReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import loliland.launcher.client.IiI1l1IllANd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.l0liOI11laND;
import loliland.launcher.client.l1IOlANd;
import loliland.launcher.client.lOilLanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class IliOOllOlanD
extends IiI1l1IllANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(IliOOllOlanD.class);
    private static final l1IOlANd OOOIilanD = l1IOlANd.INSTANCE;
    private static final String lI00OlAND = "0.0.0.0";
    private static final String lli0OiIlAND = "::/0";

    IliOOllOlanD() {
    }

    @Override
    public String I1O1I1LaNd() {
        l0liOI11laND l0liOI11laND2 = new l0liOI11laND();
        l0liOI11laND2.ai_flags = 2;
        String string = "";
        try {
            string = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException unknownHostException) {
            I1O1I1LaNd.error("Unknown host exception when getting address of local host: {}", (Object)unknownHostException.getMessage());
            return "";
        }
        PointerByReference pointerByReference = new PointerByReference();
        int n2 = OOOIilanD.getaddrinfo(string, null, l0liOI11laND2, pointerByReference);
        if (n2 > 0) {
            if (I1O1I1LaNd.isErrorEnabled()) {
                I1O1I1LaNd.error("Failed getaddrinfo(): {}", (Object)OOOIilanD.gai_strerror(n2));
            }
            return "";
        }
        l0liOI11laND l0liOI11laND3 = new l0liOI11laND(pointerByReference.getValue());
        String string2 = l0liOI11laND3.ai_canonname.trim();
        OOOIilanD.freeaddrinfo(pointerByReference.getValue());
        return string2;
    }

    @Override
    public String OOOIilanD() {
        byte[] arrby = new byte[256];
        if (0 != LibC.INSTANCE.gethostname(arrby, arrby.length)) {
            return super.OOOIilanD();
        }
        return Native.toString(arrby);
    }

    @Override
    public String lli0OiIlAND() {
        List list = Iill1lanD.I1O1I1LaNd("route -A inet -n");
        if (list.size() <= 2) {
            return "";
        }
        String string = "";
        int n2 = Integer.MAX_VALUE;
        for (int i2 = 2; i2 < list.size(); ++i2) {
            String[] arrstring = lOilLanD.OOOIilanD.split((CharSequence)list.get(i2));
            if (arrstring.length <= 4 || !arrstring[0].equals(lI00OlAND)) continue;
            boolean bl = arrstring[3].indexOf(71) != -1;
            int n3 = lOilLanD.lli0OiIlAND(arrstring[4], Integer.MAX_VALUE);
            if (!bl || n3 >= n2) continue;
            n2 = n3;
            string = arrstring[1];
        }
        return string;
    }

    @Override
    public String li0iOILAND() {
        List list = Iill1lanD.I1O1I1LaNd("route -A inet6 -n");
        if (list.size() <= 2) {
            return "";
        }
        String string = "";
        int n2 = Integer.MAX_VALUE;
        for (int i2 = 2; i2 < list.size(); ++i2) {
            String[] arrstring = lOilLanD.OOOIilanD.split((CharSequence)list.get(i2));
            if (arrstring.length <= 3 || !arrstring[0].equals(lli0OiIlAND)) continue;
            boolean bl = arrstring[2].indexOf(71) != -1;
            int n3 = lOilLanD.lli0OiIlAND(arrstring[3], Integer.MAX_VALUE);
            if (!bl || n3 >= n2) continue;
            n2 = n3;
            string = arrstring[1];
        }
        return string;
    }
}

