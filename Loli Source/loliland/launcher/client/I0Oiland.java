/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Platform;
import java.time.LocalDate;
import java.util.List;
import loliland.launcher.client.I0ilI1lanD;
import loliland.launcher.client.O1O1i10LaNd;
import loliland.launcher.client.OOOIilanD;
import loliland.launcher.client.Oli10OlAnd;
import loliland.launcher.client.iI0i1I1Land;
import loliland.launcher.client.l1lOlAND;
import loliland.launcher.client.lOI01II1lAnD;
import loliland.launcher.client.llIlILaNd;
import loliland.launcher.client.llOIlIilAND;

public abstract class I0Oiland
implements I0ilI1lanD {
    private String I1O1I1LaNd;
    private String OOOIilanD;
    private double lI00OlAND;
    private double lli0OiIlAND;
    private double li0iOILAND;
    private double O1il1llOLANd;
    private double Oill1LAnD;
    private double lIOILand;
    private boolean lil0liLand;
    private boolean iilIi1laND;
    private boolean lli011lLANd;
    private llOIlIilAND l0illAND;
    private int IO11O0LANd;
    private int l11lLANd;
    private int lO110l1LANd;
    private int l0iIlIO1laNd;
    private String iOIl0LAnD;
    private LocalDate iIiO00OLaNd;
    private String ii1li00Land;
    private String IOI1LaNd;
    private double lI00ilAND;

    protected I0Oiland(String string, String string2, double d2, double d3, double d4, double d5, double d6, double d7, boolean bl, boolean bl2, boolean bl3, llOIlIilAND llOIlIilAND2, int n2, int n3, int n4, int n5, String string3, LocalDate localDate, String string4, String string5, double d8) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
        this.lI00OlAND = d2;
        this.lli0OiIlAND = d3;
        this.li0iOILAND = d4;
        this.O1il1llOLANd = d5;
        this.Oill1LAnD = d6;
        this.lIOILand = d7;
        this.lil0liLand = bl;
        this.iilIi1laND = bl2;
        this.lli011lLANd = bl3;
        this.l0illAND = llOIlIilAND2;
        this.IO11O0LANd = n2;
        this.l11lLANd = n3;
        this.lO110l1LANd = n4;
        this.l0iIlIO1laNd = n5;
        this.iOIl0LAnD = string3;
        this.iIiO00OLaNd = localDate;
        this.ii1li00Land = string4;
        this.IOI1LaNd = string5;
        this.lI00ilAND = d8;
    }

    @Override
    public String I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    @Override
    public String OOOIilanD() {
        return this.OOOIilanD;
    }

    @Override
    public double lI00OlAND() {
        return this.lI00OlAND;
    }

    @Override
    public double lli0OiIlAND() {
        return this.lli0OiIlAND;
    }

    @Override
    public double li0iOILAND() {
        return this.li0iOILAND;
    }

    @Override
    public double O1il1llOLANd() {
        return this.O1il1llOLANd;
    }

    @Override
    public double Oill1LAnD() {
        return this.Oill1LAnD;
    }

    @Override
    public double lIOILand() {
        return this.lIOILand;
    }

    @Override
    public boolean lil0liLand() {
        return this.lil0liLand;
    }

    @Override
    public boolean iilIi1laND() {
        return this.iilIi1laND;
    }

    @Override
    public boolean lli011lLANd() {
        return this.lli011lLANd;
    }

    @Override
    public llOIlIilAND l0illAND() {
        return this.l0illAND;
    }

    @Override
    public int IO11O0LANd() {
        return this.IO11O0LANd;
    }

    @Override
    public int l11lLANd() {
        return this.l11lLANd;
    }

    @Override
    public int lO110l1LANd() {
        return this.lO110l1LANd;
    }

    @Override
    public int l0iIlIO1laNd() {
        return this.l0iIlIO1laNd;
    }

    @Override
    public String iOIl0LAnD() {
        return this.iOIl0LAnD;
    }

    @Override
    public LocalDate iIiO00OLaNd() {
        return this.iIiO00OLaNd;
    }

    @Override
    public String ii1li00Land() {
        return this.ii1li00Land;
    }

    @Override
    public String IOI1LaNd() {
        return this.IOI1LaNd;
    }

    @Override
    public double lI00ilAND() {
        return this.lI00ilAND;
    }

    @Override
    public boolean l0l00lAND() {
        List list = I0Oiland.iOl10IlLAnd();
        for (I0ilI1lanD i0ilI1lanD : list) {
            if (!i0ilI1lanD.I1O1I1LaNd().equals(this.I1O1I1LaNd)) continue;
            this.I1O1I1LaNd = i0ilI1lanD.I1O1I1LaNd();
            this.OOOIilanD = i0ilI1lanD.OOOIilanD();
            this.lI00OlAND = i0ilI1lanD.lI00OlAND();
            this.lli0OiIlAND = i0ilI1lanD.lli0OiIlAND();
            this.li0iOILAND = i0ilI1lanD.li0iOILAND();
            this.O1il1llOLANd = i0ilI1lanD.O1il1llOLANd();
            this.Oill1LAnD = i0ilI1lanD.Oill1LAnD();
            this.lIOILand = i0ilI1lanD.lIOILand();
            this.lil0liLand = i0ilI1lanD.lil0liLand();
            this.iilIi1laND = i0ilI1lanD.iilIi1laND();
            this.lli011lLANd = i0ilI1lanD.lli011lLANd();
            this.l0illAND = i0ilI1lanD.l0illAND();
            this.IO11O0LANd = i0ilI1lanD.IO11O0LANd();
            this.l11lLANd = i0ilI1lanD.l11lLANd();
            this.lO110l1LANd = i0ilI1lanD.lO110l1LANd();
            this.l0iIlIO1laNd = i0ilI1lanD.l0iIlIO1laNd();
            this.iOIl0LAnD = i0ilI1lanD.iOIl0LAnD();
            this.iIiO00OLaNd = i0ilI1lanD.iIiO00OLaNd();
            this.ii1li00Land = i0ilI1lanD.ii1li00Land();
            this.IOI1LaNd = i0ilI1lanD.IOI1LaNd();
            this.lI00ilAND = i0ilI1lanD.lI00ilAND();
            return true;
        }
        return false;
    }

    private static List iOl10IlLAnd() {
        switch (loliland.launcher.client.OOOIilanD.I1O1I1LaNd()) {
            case I1O1I1LaNd: {
                return iI0i1I1Land.iOl10IlLAnd();
            }
            case lI00OlAND: {
                return l1lOlAND.iOl10IlLAnd();
            }
            case OOOIilanD: {
                return O1O1i10LaNd.iOl10IlLAnd();
            }
            case li0iOILAND: {
                return lOI01II1lAnD.iOl10IlLAnd();
            }
            case O1il1llOLANd: {
                return llIlILaNd.iOl10IlLAnd();
            }
            case Oill1LAnD: {
                return Oli10OlAnd.iOl10IlLAnd();
            }
        }
        throw new UnsupportedOperationException("Operating system not supported: " + Platform.getOSType());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: ").append(this.I1O1I1LaNd()).append(", ");
        stringBuilder.append("Device Name: ").append(this.OOOIilanD()).append(",\n ");
        stringBuilder.append("RemainingCapacityPercent: ").append(this.lI00OlAND() * 100.0).append("%, ");
        stringBuilder.append("Time Remaining: ").append(I0Oiland.I1O1I1LaNd(this.lli0OiIlAND())).append(", ");
        stringBuilder.append("Time Remaining Instant: ").append(I0Oiland.I1O1I1LaNd(this.li0iOILAND())).append(",\n ");
        stringBuilder.append("Power Usage Rate: ").append(this.O1il1llOLANd()).append("mW, ");
        stringBuilder.append("Voltage: ");
        if (this.Oill1LAnD() > 0.0) {
            stringBuilder.append(this.Oill1LAnD()).append("V, ");
        } else {
            stringBuilder.append("unknown");
        }
        stringBuilder.append("Amperage: ").append(this.lIOILand()).append("mA,\n ");
        stringBuilder.append("Power OnLine: ").append(this.lil0liLand()).append(", ");
        stringBuilder.append("Charging: ").append(this.iilIi1laND()).append(", ");
        stringBuilder.append("Discharging: ").append(this.lli011lLANd()).append(",\n ");
        stringBuilder.append("Capacity Units: ").append((Object)this.l0illAND()).append(", ");
        stringBuilder.append("Current Capacity: ").append(this.IO11O0LANd()).append(", ");
        stringBuilder.append("Max Capacity: ").append(this.l11lLANd()).append(", ");
        stringBuilder.append("Design Capacity: ").append(this.lO110l1LANd()).append(",\n ");
        stringBuilder.append("Cycle Count: ").append(this.l0iIlIO1laNd()).append(", ");
        stringBuilder.append("Chemistry: ").append(this.iOIl0LAnD()).append(", ");
        stringBuilder.append("Manufacture Date: ").append(this.iIiO00OLaNd() != null ? this.iIiO00OLaNd() : "unknown").append(", ");
        stringBuilder.append("Manufacturer: ").append(this.ii1li00Land()).append(",\n ");
        stringBuilder.append("SerialNumber: ").append(this.IOI1LaNd()).append(", ");
        stringBuilder.append("Temperature: ");
        if (this.lI00ilAND() > 0.0) {
            stringBuilder.append(this.lI00ilAND()).append("\u00b0C");
        } else {
            stringBuilder.append("unknown");
        }
        return stringBuilder.toString();
    }

    private static String I1O1I1LaNd(double d2) {
        String string;
        if (d2 < -1.5) {
            string = "Charging";
        } else if (d2 < 0.0) {
            string = "Unknown";
        } else {
            int n2 = (int)(d2 / 3600.0);
            int n3 = (int)(d2 % 3600.0 / 60.0);
            string = String.format("%d:%02d", n2, n3);
        }
        return string;
    }
}

