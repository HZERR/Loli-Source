/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.IliO01LAnd;
import loliland.launcher.client.O1iO000Land;
import loliland.launcher.client.i0O1LAnd;
import loliland.launcher.client.i11IllAnd;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.iiOlLand;
import loliland.launcher.client.il1OI0lAnd;
import loliland.launcher.client.l1l0lAnd;
import loliland.launcher.client.lOIOlOIIlAnd;
import loliland.launcher.client.lOOOLaNd;
import loliland.launcher.client.lOOOliOlanD;
import loliland.launcher.client.li0i01LanD;
import loliland.launcher.client.llllil1LaND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class lllilI0land
extends llllil1LaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lllilI0land.class);
    private static final String OOOIilanD = "COM exception: {}";

    lllilI0land() {
    }

    @Override
    public double lli0OiIlAND() {
        double d2 = lllilI0land.Oill1LAnD();
        if (d2 > 0.0) {
            return d2;
        }
        d2 = lllilI0land.lIOILand();
        return d2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static double Oill1LAnD() {
        li0i01LanD li0i01LanD2 = li0i01LanD.I1O1I1LaNd();
        boolean bl = false;
        try {
            bl = li0i01LanD2.OOOIilanD();
            WbemcliUtil.WmiResult wmiResult = i0O1LAnd.I1O1I1LaNd(li0i01LanD2, "Hardware", "CPU");
            if (wmiResult.getResultCount() > 0) {
                WbemcliUtil.WmiResult wmiResult2;
                I1O1I1LaNd.debug("Found Temperature data in Open Hardware Monitor");
                String string = ii00llanD.I1O1I1LaNd(wmiResult, lOIOlOIIlAnd.I1O1I1LaNd, 0);
                if (string.length() > 0 && (wmiResult2 = lOOOLaNd.I1O1I1LaNd(li0i01LanD2, string, "Temperature")).getResultCount() > 0) {
                    double d2 = 0.0;
                    for (int i2 = 0; i2 < wmiResult2.getResultCount(); ++i2) {
                        d2 += (double)ii00llanD.iilIi1laND(wmiResult2, l1l0lAnd.I1O1I1LaNd, i2);
                    }
                    double d3 = d2 / (double)wmiResult2.getResultCount();
                    return d3;
                }
            }
        }
        catch (COMException cOMException) {
            I1O1I1LaNd.warn(OOOIilanD, (Object)cOMException.getMessage());
        }
        finally {
            if (bl) {
                li0i01LanD2.lI00OlAND();
            }
        }
        return 0.0;
    }

    private static double lIOILand() {
        double d2 = 0.0;
        long l2 = 0L;
        WbemcliUtil.WmiResult wmiResult = O1iO000Land.I1O1I1LaNd();
        if (wmiResult.getResultCount() > 0) {
            I1O1I1LaNd.debug("Found Temperature data in WMI");
            l2 = ii00llanD.Oill1LAnD(wmiResult, IliO01LAnd.I1O1I1LaNd, 0);
        }
        if (l2 > 2732L) {
            d2 = (double)l2 / 10.0 - 273.15;
        } else if (l2 > 274L) {
            d2 = (double)l2 - 273.0;
        }
        return d2 < 0.0 ? 0.0 : d2;
    }

    @Override
    public int[] li0iOILAND() {
        int[] arrn = lllilI0land.lil0liLand();
        if (arrn.length > 0) {
            return arrn;
        }
        arrn = lllilI0land.iilIi1laND();
        if (arrn.length > 0) {
            return arrn;
        }
        return new int[0];
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int[] lil0liLand() {
        li0i01LanD li0i01LanD2 = li0i01LanD.I1O1I1LaNd();
        boolean bl = false;
        try {
            bl = li0i01LanD2.OOOIilanD();
            WbemcliUtil.WmiResult wmiResult = i0O1LAnd.I1O1I1LaNd(li0i01LanD2, "Hardware", "CPU");
            if (wmiResult.getResultCount() > 0) {
                WbemcliUtil.WmiResult wmiResult2;
                I1O1I1LaNd.debug("Found Fan data in Open Hardware Monitor");
                String string = ii00llanD.I1O1I1LaNd(wmiResult, lOIOlOIIlAnd.I1O1I1LaNd, 0);
                if (string.length() > 0 && (wmiResult2 = lOOOLaNd.I1O1I1LaNd(li0i01LanD2, string, "Fan")).getResultCount() > 0) {
                    int[] arrn = new int[wmiResult2.getResultCount()];
                    for (int i2 = 0; i2 < wmiResult2.getResultCount(); ++i2) {
                        arrn[i2] = (int)ii00llanD.iilIi1laND(wmiResult2, l1l0lAnd.I1O1I1LaNd, i2);
                    }
                    int[] arrn2 = arrn;
                    return arrn2;
                }
            }
        }
        catch (COMException cOMException) {
            I1O1I1LaNd.warn(OOOIilanD, (Object)cOMException.getMessage());
        }
        finally {
            if (bl) {
                li0i01LanD2.lI00OlAND();
            }
        }
        return new int[0];
    }

    private static int[] iilIi1laND() {
        WbemcliUtil.WmiResult wmiResult = il1OI0lAnd.I1O1I1LaNd();
        if (wmiResult.getResultCount() > 1) {
            I1O1I1LaNd.debug("Found Fan data in WMI");
            int[] arrn = new int[wmiResult.getResultCount()];
            for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
                arrn[i2] = (int)ii00llanD.li0iOILAND(wmiResult, iiOlLand.I1O1I1LaNd, i2);
            }
            return arrn;
        }
        return new int[0];
    }

    @Override
    public double O1il1llOLANd() {
        double d2 = lllilI0land.lli011lLANd();
        if (d2 > 0.0) {
            return d2;
        }
        d2 = lllilI0land.l0illAND();
        return d2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static double lli011lLANd() {
        li0i01LanD li0i01LanD2 = li0i01LanD.I1O1I1LaNd();
        boolean bl = false;
        try {
            bl = li0i01LanD2.OOOIilanD();
            WbemcliUtil.WmiResult wmiResult = i0O1LAnd.I1O1I1LaNd(li0i01LanD2, "Sensor", "Voltage");
            if (wmiResult.getResultCount() > 0) {
                WbemcliUtil.WmiResult wmiResult2;
                I1O1I1LaNd.debug("Found Voltage data in Open Hardware Monitor");
                String string = null;
                for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
                    String string2 = ii00llanD.I1O1I1LaNd(wmiResult, lOIOlOIIlAnd.I1O1I1LaNd, i2);
                    if (!string2.toLowerCase().contains("cpu")) continue;
                    string = string2;
                    break;
                }
                if (string == null) {
                    string = ii00llanD.I1O1I1LaNd(wmiResult, lOIOlOIIlAnd.I1O1I1LaNd, 0);
                }
                if ((wmiResult2 = lOOOLaNd.I1O1I1LaNd(li0i01LanD2, string, "Voltage")).getResultCount() > 0) {
                    double d2 = ii00llanD.iilIi1laND(wmiResult2, l1l0lAnd.I1O1I1LaNd, 0);
                    return d2;
                }
            }
        }
        catch (COMException cOMException) {
            I1O1I1LaNd.warn(OOOIilanD, (Object)cOMException.getMessage());
        }
        finally {
            if (bl) {
                li0i01LanD2.lI00OlAND();
            }
        }
        return 0.0;
    }

    private static double l0illAND() {
        WbemcliUtil.WmiResult wmiResult = i11IllAnd.I1O1I1LaNd();
        if (wmiResult.getResultCount() > 1) {
            I1O1I1LaNd.debug("Found Voltage data in WMI");
            int n2 = ii00llanD.lil0liLand(wmiResult, lOOOliOlanD.I1O1I1LaNd, 0);
            if (n2 > 0) {
                if ((n2 & 0x80) == 0) {
                    n2 = ii00llanD.O1il1llOLANd(wmiResult, lOOOliOlanD.OOOIilanD, 0);
                    if ((n2 & 1) > 0) {
                        return 5.0;
                    }
                    if ((n2 & 2) > 0) {
                        return 3.3;
                    }
                    if ((n2 & 4) > 0) {
                        return 2.9;
                    }
                } else {
                    return (double)(n2 & 0x7F) / 10.0;
                }
            }
        }
        return 0.0;
    }
}

