/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.linux.Udev;
import java.io.File;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.O0I1il01lANd;
import loliland.launcher.client.i1i0lOliLaND;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.liOIOOlLAnD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class l1IlIllaND
extends i1i0lOliLaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(l1IlIllaND.class);
    private int OOOIilanD;
    private boolean lI00OlAND;
    private long lli0OiIlAND;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;
    private long lil0liLand;
    private long iilIi1laND;
    private long lli011lLANd;
    private long l0illAND;
    private long IO11O0LANd;
    private String l11lLANd;
    private O0I1il01lANd lO110l1LANd;

    public l1IlIllaND(NetworkInterface networkInterface) throws InstantiationException {
        super(networkInterface, l1IlIllaND.I1O1I1LaNd(networkInterface));
        this.liOIOOLANd();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String I1O1I1LaNd(NetworkInterface networkInterface) {
        String string;
        block12: {
            string = networkInterface.getName();
            Udev.UdevContext udevContext = Udev.INSTANCE.udev_new();
            if (udevContext != null) {
                try {
                    Udev.UdevDevice udevDevice = udevContext.deviceNewFromSyspath("/sys/class/net/" + string);
                    if (udevDevice == null) break block12;
                    try {
                        String string2 = udevDevice.getPropertyValue("ID_VENDOR_FROM_DATABASE");
                        String string3 = udevDevice.getPropertyValue("ID_MODEL_FROM_DATABASE");
                        if (!iiIIIlO1lANd.I1O1I1LaNd(string3)) {
                            if (!iiIIIlO1lANd.I1O1I1LaNd(string2)) {
                                String string4 = string2 + " " + string3;
                                return string4;
                            }
                            String string5 = string3;
                            return string5;
                        }
                    }
                    finally {
                        udevDevice.unref();
                    }
                }
                finally {
                    udevContext.unref();
                }
            }
        }
        return string;
    }

    public static List OOOIilanD(boolean bl) {
        ArrayList<l1IlIllaND> arrayList = new ArrayList<l1IlIllaND>();
        for (NetworkInterface networkInterface : l1IlIllaND.I1O1I1LaNd(bl)) {
            try {
                arrayList.add(new l1IlIllaND(networkInterface));
            }
            catch (InstantiationException instantiationException) {
                I1O1I1LaNd.debug("Network Interface Instantiation failed: {}", (Object)instantiationException.getMessage());
            }
        }
        return arrayList;
    }

    @Override
    public int IO11O0LANd() {
        return this.OOOIilanD;
    }

    @Override
    public boolean lO110l1LANd() {
        return this.lI00OlAND;
    }

    @Override
    public long l0iIlIO1laNd() {
        return this.lli0OiIlAND;
    }

    @Override
    public long iOIl0LAnD() {
        return this.li0iOILAND;
    }

    @Override
    public long iIiO00OLaNd() {
        return this.O1il1llOLANd;
    }

    @Override
    public long ii1li00Land() {
        return this.Oill1LAnD;
    }

    @Override
    public long IOI1LaNd() {
        return this.lIOILand;
    }

    @Override
    public long lI00ilAND() {
        return this.lil0liLand;
    }

    @Override
    public long l0l00lAND() {
        return this.iilIi1laND;
    }

    @Override
    public long iOl10IlLAnd() {
        return this.lli011lLANd;
    }

    @Override
    public long lIiIii1LAnD() {
        return this.l0illAND;
    }

    @Override
    public long II1Iland() {
        return this.IO11O0LANd;
    }

    @Override
    public String li0iOILAND() {
        return this.l11lLANd;
    }

    @Override
    public O0I1il01lANd O1il1llOLANd() {
        return this.lO110l1LANd;
    }

    @Override
    public boolean liOIOOLANd() {
        Object object;
        try {
            object = new File(String.format("/sys/class/net/%s/statistics", this.OOOIilanD()));
            if (!((File)object).isDirectory()) {
                return false;
            }
        }
        catch (SecurityException securityException) {
            return false;
        }
        object = String.format("/sys/class/net/%s/type", this.OOOIilanD());
        String string = String.format("/sys/class/net/%s/carrier", this.OOOIilanD());
        String string2 = String.format("/sys/class/net/%s/statistics/tx_bytes", this.OOOIilanD());
        String string3 = String.format("/sys/class/net/%s/statistics/rx_bytes", this.OOOIilanD());
        String string4 = String.format("/sys/class/net/%s/statistics/tx_packets", this.OOOIilanD());
        String string5 = String.format("/sys/class/net/%s/statistics/rx_packets", this.OOOIilanD());
        String string6 = String.format("/sys/class/net/%s/statistics/tx_errors", this.OOOIilanD());
        String string7 = String.format("/sys/class/net/%s/statistics/rx_errors", this.OOOIilanD());
        String string8 = String.format("/sys/class/net/%s/statistics/collisions", this.OOOIilanD());
        String string9 = String.format("/sys/class/net/%s/statistics/rx_dropped", this.OOOIilanD());
        String string10 = String.format("/sys/class/net/%s/speed", this.OOOIilanD());
        String string11 = String.format("/sys/class/net/%s/ifalias", this.OOOIilanD());
        String string12 = String.format("/sys/class/net/%s/operstate", this.OOOIilanD());
        this.IO11O0LANd = System.currentTimeMillis();
        this.OOOIilanD = liOIOOlLAnD.lli0OiIlAND((String)object);
        this.lI00OlAND = liOIOOlLAnD.lli0OiIlAND(string) > 0;
        this.li0iOILAND = liOIOOlLAnD.lI00OlAND(string2);
        this.lli0OiIlAND = liOIOOlLAnD.lI00OlAND(string3);
        this.Oill1LAnD = liOIOOlLAnD.lI00OlAND(string4);
        this.O1il1llOLANd = liOIOOlLAnD.lI00OlAND(string5);
        this.lil0liLand = liOIOOlLAnD.lI00OlAND(string6);
        this.lIOILand = liOIOOlLAnD.lI00OlAND(string7);
        this.lli011lLANd = liOIOOlLAnD.lI00OlAND(string8);
        this.iilIi1laND = liOIOOlLAnD.lI00OlAND(string9);
        long l2 = liOIOOlLAnD.lI00OlAND(string10);
        this.l0illAND = l2 < 0L ? 0L : l2 << 20;
        this.l11lLANd = liOIOOlLAnD.li0iOILAND(string11);
        this.lO110l1LANd = l1IlIllaND.I1O1I1LaNd(liOIOOlLAnD.li0iOILAND(string12));
        return true;
    }

    private static O0I1il01lANd I1O1I1LaNd(String string) {
        switch (string) {
            case "up": {
                return O0I1il01lANd.I1O1I1LaNd;
            }
            case "down": {
                return O0I1il01lANd.OOOIilanD;
            }
            case "testing": {
                return O0I1il01lANd.lI00OlAND;
            }
            case "dormant": {
                return O0I1il01lANd.li0iOILAND;
            }
            case "notpresent": {
                return O0I1il01lANd.O1il1llOLANd;
            }
            case "lowerlayerdown": {
                return O0I1il01lANd.Oill1LAnD;
            }
        }
        return O0I1il01lANd.lli0OiIlAND;
    }
}

