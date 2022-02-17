/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.ii0ii01LanD;
import loliland.launcher.client.lO1i01LAnd;

public class l0i1I11ILANd {
    private Object I1O1I1LaNd;

    public l0i1I11ILANd(Object object) {
        this.I1O1I1LaNd = object;
    }

    public int I1O1I1LaNd() {
        if (this.I1O1I1LaNd instanceof Integer) {
            return (Integer)this.I1O1I1LaNd;
        }
        try {
            return Integer.parseInt(this.Oill1LAnD());
        }
        catch (Exception exception) {
            try {
                return (int)this.OOOIilanD();
            }
            catch (Exception exception2) {
                try {
                    return (int)this.lli0OiIlAND();
                }
                catch (Exception exception3) {
                    throw new lO1i01LAnd("\u0410\u0440\u0433\u0443\u043c\u0435\u043d\u0442 " + this.Oill1LAnD() + " \u043d\u0435 \u043a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d \u0432 int!", null);
                }
            }
        }
    }

    public float OOOIilanD() {
        try {
            return Float.parseFloat(this.Oill1LAnD());
        }
        catch (Exception exception) {
            throw new lO1i01LAnd("\u0410\u0440\u0433\u0443\u043c\u0435\u043d\u0442 " + this.Oill1LAnD() + " \u043d\u0435 \u043a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d \u0432 int!", null);
        }
    }

    public long lI00OlAND() {
        if (this.I1O1I1LaNd instanceof Long) {
            return (Long)this.I1O1I1LaNd;
        }
        if (this.I1O1I1LaNd instanceof Integer) {
            return ((Integer)this.I1O1I1LaNd).intValue();
        }
        try {
            return Long.parseLong(this.Oill1LAnD());
        }
        catch (Exception exception) {
            try {
                return (long)this.lli0OiIlAND();
            }
            catch (Exception exception2) {
                try {
                    return (long)this.OOOIilanD();
                }
                catch (Exception exception3) {
                    throw new lO1i01LAnd("\u0410\u0440\u0433\u0443\u043c\u0435\u043d\u0442 " + this.Oill1LAnD() + " \u043d\u0435 \u043a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d \u0432 long!", null);
                }
            }
        }
    }

    public double lli0OiIlAND() {
        try {
            return Double.parseDouble(this.Oill1LAnD());
        }
        catch (Exception exception) {
            throw new lO1i01LAnd("\u0410\u0440\u0433\u0443\u043c\u0435\u043d\u0442 " + this.Oill1LAnD() + " \u043d\u0435 \u043a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d \u0432 double!", null);
        }
    }

    public boolean li0iOILAND() {
        try {
            return Boolean.parseBoolean(this.Oill1LAnD());
        }
        catch (Exception exception) {
            throw new lO1i01LAnd("\u0410\u0440\u0433\u0443\u043c\u0435\u043d\u0442 " + this.Oill1LAnD() + " \u043d\u0435 \u043a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d \u0432 boolean!", null);
        }
    }

    public List I1O1I1LaNd(Class class_) {
        LinkedList linkedList = new LinkedList();
        try {
            if (!(this.I1O1I1LaNd instanceof List)) {
                throw new lO1i01LAnd("\u0410\u0440\u0433\u0443\u043c\u0435\u043d\u0442 " + this.Oill1LAnD() + " \u043d\u0435 \u043a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d \u0432 List!", null);
            }
            List list = (List)this.I1O1I1LaNd;
            list.forEach(object -> {
                if (class_.isAssignableFrom(object.getClass())) {
                    linkedList.add(object);
                }
            });
        }
        catch (Exception exception) {
            // empty catch block
        }
        return linkedList;
    }

    public ii0ii01LanD O1il1llOLANd() {
        if (this.I1O1I1LaNd instanceof ii0ii01LanD) {
            return (ii0ii01LanD)this.I1O1I1LaNd;
        }
        if (this.I1O1I1LaNd instanceof Map) {
            ii0ii01LanD ii0ii01LanD2 = new ii0ii01LanD(null);
            ii0ii01LanD.I1O1I1LaNd(ii0ii01LanD2, new LinkedHashMap((Map)this.I1O1I1LaNd));
            return ii0ii01LanD2;
        }
        try {
            return ii0ii01LanD.I1O1I1LaNd(this.Oill1LAnD());
        }
        catch (Exception exception) {
            throw new lO1i01LAnd("\u0410\u0440\u0433\u0443\u043c\u0435\u043d\u0442 " + this.Oill1LAnD() + " \u043d\u0435 \u043a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d \u0432 Json!", null);
        }
    }

    public String Oill1LAnD() {
        return this.I1O1I1LaNd.toString();
    }

    public Object lIOILand() {
        return this.I1O1I1LaNd;
    }
}

