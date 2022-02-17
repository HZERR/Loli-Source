/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import loliland.launcher.client.IiI1l1IllANd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.l0liOI11laND;
import loliland.launcher.client.lIOOOlIlaNd;
import loliland.launcher.client.lOilLanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class I0OIOlaNd
extends IiI1l1IllANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(I0OIOlaNd.class);
    private static final lIOOOlIlaNd OOOIilanD = lIOOOlIlaNd.INSTANCE;
    private static final String lI00OlAND = "Internet6:";
    private static final String lli0OiIlAND = "default";

    I0OIOlaNd() {
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
        if (0 != OOOIilanD.gethostname(arrby, arrby.length)) {
            return super.OOOIilanD();
        }
        return Native.toString(arrby);
    }

    @Override
    public String lli0OiIlAND() {
        return I0OIOlaNd.I1O1I1LaNd(Iill1lanD.I1O1I1LaNd("route -n get default"));
    }

    @Override
    public String li0iOILAND() {
        List list = Iill1lanD.I1O1I1LaNd("netstat -nr");
        boolean bl = false;
        for (String string : list) {
            if (bl && string.startsWith(lli0OiIlAND)) {
                String[] arrstring = lOilLanD.OOOIilanD.split(string);
                if (arrstring.length <= 2 || !arrstring[2].contains("G")) continue;
                return arrstring[1].split("%")[0];
            }
            if (!string.startsWith(lI00OlAND)) continue;
            bl = true;
        }
        return "";
    }
}

