/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.i110iIl0LAnd;
import loliland.launcher.client.il1iIIILaND;
import loliland.launcher.client.l0illAND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lil0liLand;

final class IOlllOLAND
extends il1iIIILaND {
    private static final DateTimeFormatter I1O1I1LaNd = DateTimeFormatter.ofPattern("MMM d uuuu HH:mm:ss", Locale.ENGLISH);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(this::O1il1llOLANd);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(this::Oill1LAnD);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(this::lIOILand);
    private final Supplier li0iOILAND = lii1IO0LaNd.I1O1I1LaNd(this::lil0liLand);
    private final Supplier O1il1llOLANd = lii1IO0LaNd.I1O1I1LaNd(this::iilIi1laND);
    private final Supplier Oill1LAnD = lii1IO0LaNd.I1O1I1LaNd(IOlllOLAND::lli011lLANd);
    private final Supplier lIOILand = lii1IO0LaNd.I1O1I1LaNd(lil0liLand::lI00OlAND);

    IOlllOLAND() {
    }

    @Override
    public String I1O1I1LaNd() {
        return (String)this.OOOIilanD.get();
    }

    @Override
    public String lI00OlAND() {
        return (String)this.lI00OlAND.get();
    }

    @Override
    public String lli0OiIlAND() {
        return (String)this.lli0OiIlAND.get();
    }

    @Override
    public String li0iOILAND() {
        return (String)this.li0iOILAND.get();
    }

    @Override
    public String OOOIilanD() {
        return (String)this.O1il1llOLANd.get();
    }

    private String O1il1llOLANd() {
        String string = null;
        string = l0illAND.lil0liLand();
        if (string == null && (string = i110iIl0LAnd.I1O1I1LaNd((i110iIl0LAnd)this.Oill1LAnD.get())) == null) {
            return "unknown";
        }
        return string;
    }

    private String Oill1LAnD() {
        String string = null;
        string = l0illAND.iilIi1laND();
        if (string == null && (string = i110iIl0LAnd.OOOIilanD((i110iIl0LAnd)this.Oill1LAnD.get())) == null) {
            return "unknown";
        }
        return string;
    }

    private String lIOILand() {
        String string = null;
        string = l0illAND.I1O1I1LaNd((String)((O1IiIiI1LAND)this.lIOILand.get()).OOOIilanD());
        if (string == null && (string = i110iIl0LAnd.lI00OlAND((i110iIl0LAnd)this.Oill1LAnD.get())) == null) {
            return "unknown";
        }
        return string;
    }

    private String lil0liLand() {
        String string = null;
        string = l0illAND.lli011lLANd();
        if (string == null && (string = i110iIl0LAnd.lli0OiIlAND((i110iIl0LAnd)this.Oill1LAnD.get())) == null) {
            return "unknown";
        }
        return string;
    }

    private String iilIi1laND() {
        String string = null;
        string = (String)((O1IiIiI1LAND)this.lIOILand.get()).I1O1I1LaNd();
        if (string == null && (string = i110iIl0LAnd.li0iOILAND((i110iIl0LAnd)this.Oill1LAnD.get())) == null) {
            return "unknown";
        }
        return string;
    }

    private static i110iIl0LAnd lli011lLANd() {
        String string = null;
        String string2 = null;
        String string3 = null;
        List list = Iill1lanD.I1O1I1LaNd("vcgencmd version");
        if (list.size() >= 3) {
            try {
                string = DateTimeFormatter.ISO_LOCAL_DATE.format(I1O1I1LaNd.parse((CharSequence)list.get(0)));
            }
            catch (DateTimeParseException dateTimeParseException) {
                string = "unknown";
            }
            String[] arrstring = lOilLanD.OOOIilanD.split((CharSequence)list.get(1));
            string2 = arrstring[arrstring.length - 1];
            string3 = ((String)list.get(2)).replace("version ", "");
            return new i110iIl0LAnd(string, string2, string3, "RPi", "Bootloader", null);
        }
        return new i110iIl0LAnd(null, null, null, null, null, null);
    }
}

