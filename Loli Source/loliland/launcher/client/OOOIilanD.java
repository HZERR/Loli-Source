/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Platform;
import java.util.function.Supplier;
import loliland.launcher.client.I0l0Land;
import loliland.launcher.client.I1O1I1LaNd;
import loliland.launcher.client.I1l1lANd;
import loliland.launcher.client.IOI0I0l1laNd;
import loliland.launcher.client.IOi0ii1OlanD;
import loliland.launcher.client.Ili1LAnd;
import loliland.launcher.client.O11IOLAnd;
import loliland.launcher.client.i0iiLanD;
import loliland.launcher.client.iII1iOllanD;
import loliland.launcher.client.illOlaND;
import loliland.launcher.client.l11IlanD;
import loliland.launcher.client.l1I110ilaNd;
import loliland.launcher.client.lO0lOOlAnD;
import loliland.launcher.client.lOllOiLANd;
import loliland.launcher.client.liO0iO10laNd;
import loliland.launcher.client.liO0lAND;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lliOlIllAnd;

public class OOOIilanD {
    private static final I1O1I1LaNd I1O1I1LaNd = Platform.isWindows() ? loliland.launcher.client.I1O1I1LaNd.I1O1I1LaNd : (Platform.isLinux() ? loliland.launcher.client.I1O1I1LaNd.OOOIilanD : (Platform.isMac() ? loliland.launcher.client.I1O1I1LaNd.lI00OlAND : (Platform.isSolaris() ? loliland.launcher.client.I1O1I1LaNd.li0iOILAND : (Platform.isFreeBSD() ? loliland.launcher.client.I1O1I1LaNd.O1il1llOLANd : (Platform.isAIX() ? loliland.launcher.client.I1O1I1LaNd.Oill1LAnD : (Platform.isOpenBSD() ? loliland.launcher.client.I1O1I1LaNd.lIOILand : loliland.launcher.client.I1O1I1LaNd.lil0liLand))))));
    private static final String OOOIilanD = "Operating system not supported: JNA Platform type ";
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(OOOIilanD::li0iOILAND);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(OOOIilanD::O1il1llOLANd);

    public OOOIilanD() {
        if (loliland.launcher.client.OOOIilanD.I1O1I1LaNd().equals((Object)loliland.launcher.client.I1O1I1LaNd.lil0liLand)) {
            throw new UnsupportedOperationException(OOOIilanD + Platform.getOSType());
        }
    }

    public static I1O1I1LaNd I1O1I1LaNd() {
        return I1O1I1LaNd;
    }

    @Deprecated
    public static I1O1I1LaNd OOOIilanD() {
        I1O1I1LaNd i1O1I1LaNd = loliland.launcher.client.OOOIilanD.I1O1I1LaNd();
        return i1O1I1LaNd.equals((Object)loliland.launcher.client.I1O1I1LaNd.lI00OlAND) ? loliland.launcher.client.I1O1I1LaNd.lli0OiIlAND : i1O1I1LaNd;
    }

    public liO0lAND lI00OlAND() {
        return (liO0lAND)this.lI00OlAND.get();
    }

    private static liO0lAND li0iOILAND() {
        switch (I1O1I1LaNd) {
            case I1O1I1LaNd: {
                return new IOI0I0l1laNd();
            }
            case OOOIilanD: {
                return new l11IlanD();
            }
            case lI00OlAND: {
                return new iII1iOllanD();
            }
            case li0iOILAND: {
                return new IOi0ii1OlanD();
            }
            case O1il1llOLANd: {
                return new O11IOLAnd();
            }
            case Oill1LAnD: {
                return new I0l0Land();
            }
            case lIOILand: {
                return new lO0lOOlAnD();
            }
        }
        return null;
    }

    public lOllOiLANd lli0OiIlAND() {
        return (lOllOiLANd)this.lli0OiIlAND.get();
    }

    private static lOllOiLANd O1il1llOLANd() {
        switch (I1O1I1LaNd) {
            case I1O1I1LaNd: {
                return new liO0iO10laNd();
            }
            case OOOIilanD: {
                return new l1I110ilaNd();
            }
            case lI00OlAND: {
                return new I1l1lANd();
            }
            case li0iOILAND: {
                return new lliOlIllAnd();
            }
            case O1il1llOLANd: {
                return new illOlaND();
            }
            case Oill1LAnD: {
                return new i0iiLanD();
            }
            case lIOILand: {
                return new Ili1LAnd();
            }
        }
        return null;
    }
}

