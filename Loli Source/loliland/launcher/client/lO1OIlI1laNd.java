/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import loliland.launcher.client.OOiOLand;

public class lO1OIlI1laNd
implements OOiOLand {
    private final String I1O1I1LaNd;
    private final Map OOOIilanD;
    private final Set lI00OlAND;

    protected lO1OIlI1laNd(String string, Map map, Set set) {
        this.I1O1I1LaNd = string;
        for (Map.Entry entry : map.entrySet()) {
            map.put(entry.getKey(), Collections.unmodifiableSet((Set)entry.getValue()));
        }
        this.OOOIilanD = Collections.unmodifiableMap(map);
        this.lI00OlAND = Collections.unmodifiableSet(set);
    }

    @Override
    public String I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    @Override
    public Map lI00OlAND() {
        return this.OOOIilanD;
    }

    @Override
    public Set OOOIilanD() {
        return this.lI00OlAND;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Logical Volume Group: ");
        stringBuilder.append(this.I1O1I1LaNd).append("\n |-- PVs: ");
        stringBuilder.append(this.lI00OlAND.toString());
        for (Map.Entry entry : this.OOOIilanD.entrySet()) {
            stringBuilder.append("\n |-- LV: ").append((String)entry.getKey());
            Set set = (Set)entry.getValue();
            if (set.isEmpty()) continue;
            stringBuilder.append(" --> ").append(set);
        }
        return stringBuilder.toString();
    }
}

