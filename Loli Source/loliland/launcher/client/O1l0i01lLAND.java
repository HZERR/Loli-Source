/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import loliland.launcher.client.llOlILanD;

public class O1l0i01lLAND
implements Comparable {
    private final String I1O1I1LaNd;
    private final Map OOOIilanD = Maps.newHashMap();
    private long lI00OlAND;

    public O1l0i01lLAND(String string) {
        this.I1O1I1LaNd = string;
    }

    public String I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    public Collection OOOIilanD() {
        ArrayList arrayList = Lists.newArrayList(this.OOOIilanD.values());
        Collections.sort(arrayList);
        return arrayList;
    }

    public O1l0i01lLAND I1O1I1LaNd(String string) {
        O1l0i01lLAND o1l0i01lLAND = (O1l0i01lLAND)this.OOOIilanD.get(string);
        if (o1l0i01lLAND == null) {
            o1l0i01lLAND = new O1l0i01lLAND(string);
            this.OOOIilanD.put(string, o1l0i01lLAND);
        }
        return o1l0i01lLAND;
    }

    public O1l0i01lLAND I1O1I1LaNd(String string, String string2) {
        llOlILanD llOlILanD2 = new llOlILanD(string, string2);
        O1l0i01lLAND o1l0i01lLAND = (O1l0i01lLAND)this.OOOIilanD.get(llOlILanD2.I1O1I1LaNd());
        if (o1l0i01lLAND == null) {
            o1l0i01lLAND = llOlILanD2;
            this.OOOIilanD.put(llOlILanD2.I1O1I1LaNd(), llOlILanD2);
        }
        return o1l0i01lLAND;
    }

    public long lI00OlAND() {
        return this.lI00OlAND;
    }

    public void I1O1I1LaNd(long l2) {
        this.lI00OlAND += l2;
    }

    private void I1O1I1LaNd(StackTraceElement[] arrstackTraceElement, int n2, long l2) {
        this.I1O1I1LaNd(l2);
        if (arrstackTraceElement.length - n2 == 0) {
            return;
        }
        StackTraceElement stackTraceElement = arrstackTraceElement[arrstackTraceElement.length - (n2 + 1)];
        this.I1O1I1LaNd(stackTraceElement.getClassName(), stackTraceElement.getMethodName()).I1O1I1LaNd(arrstackTraceElement, n2 + 1, l2);
    }

    public void I1O1I1LaNd(StackTraceElement[] arrstackTraceElement, long l2) {
        this.I1O1I1LaNd(arrstackTraceElement, 0, l2);
    }

    public int I1O1I1LaNd(O1l0i01lLAND o1l0i01lLAND) {
        return this.I1O1I1LaNd().compareTo(o1l0i01lLAND.I1O1I1LaNd());
    }

    void I1O1I1LaNd(StringBuilder stringBuilder, int n2) {
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder2.append(" ");
        }
        String string = stringBuilder2.toString();
        for (O1l0i01lLAND o1l0i01lLAND : this.OOOIilanD()) {
            stringBuilder.append(string).append(o1l0i01lLAND.I1O1I1LaNd());
            stringBuilder.append(" ");
            stringBuilder.append(o1l0i01lLAND.lI00OlAND()).append("ms");
            stringBuilder.append("\n");
            o1l0i01lLAND.I1O1I1LaNd(stringBuilder, n2 + 1);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        this.I1O1I1LaNd(stringBuilder, 0);
        return stringBuilder.toString();
    }

    public /* synthetic */ int compareTo(Object object) {
        return this.I1O1I1LaNd((O1l0i01lLAND)object);
    }
}

