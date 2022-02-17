/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.google.gson.Gson;
import java.util.LinkedHashMap;
import java.util.Set;
import loliland.launcher.client.Il0OLAnD;
import loliland.launcher.client.l0i1I11ILANd;

public class ii0ii01LanD {
    private LinkedHashMap I1O1I1LaNd = new LinkedHashMap();

    private ii0ii01LanD() {
    }

    private ii0ii01LanD(String string) {
        try {
            this.I1O1I1LaNd = new Gson().fromJson(string, LinkedHashMap.class);
        }
        catch (Exception exception) {
            this.I1O1I1LaNd = new LinkedHashMap();
        }
    }

    public static ii0ii01LanD I1O1I1LaNd() {
        return new ii0ii01LanD();
    }

    public static ii0ii01LanD I1O1I1LaNd(String string) {
        return new ii0ii01LanD(string);
    }

    public ii0ii01LanD I1O1I1LaNd(String string, Object object) {
        this.I1O1I1LaNd.put(string, object);
        return this;
    }

    public ii0ii01LanD I1O1I1LaNd(String string, ii0ii01LanD ii0ii01LanD2) {
        this.I1O1I1LaNd.put(string, ii0ii01LanD2.I1O1I1LaNd);
        return this;
    }

    public l0i1I11ILANd OOOIilanD(String string) {
        return new l0i1I11ILANd(this.I1O1I1LaNd.get(string));
    }

    public boolean lI00OlAND(String string) {
        return this.I1O1I1LaNd.containsKey(string);
    }

    public Set OOOIilanD() {
        return this.I1O1I1LaNd.keySet();
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this.I1O1I1LaNd);
    }

    public void lI00OlAND() {
        this.I1O1I1LaNd.clear();
    }

    public boolean lli0OiIlAND() {
        return this.I1O1I1LaNd.isEmpty();
    }

    public void li0iOILAND() {
        String string = (String)this.I1O1I1LaNd.keySet().stream().findFirst().get();
        this.I1O1I1LaNd.remove(string);
    }

    /* synthetic */ ii0ii01LanD(Il0OLAnD il0OLAnD) {
        this();
    }

    static /* synthetic */ LinkedHashMap I1O1I1LaNd(ii0ii01LanD ii0ii01LanD2, LinkedHashMap linkedHashMap) {
        ii0ii01LanD2.I1O1I1LaNd = linkedHashMap;
        return ii0ii01LanD2.I1O1I1LaNd;
    }
}

