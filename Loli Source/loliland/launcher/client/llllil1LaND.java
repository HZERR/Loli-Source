/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Arrays;
import java.util.function.Supplier;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.llII0lOiLAnD;

public abstract class llllil1LaND
implements llII0lOiLAnD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(this::lli0OiIlAND, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(this::li0iOILAND, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(this::O1il1llOLANd, lii1IO0LaNd.I1O1I1LaNd());

    @Override
    public double I1O1I1LaNd() {
        return (Double)this.I1O1I1LaNd.get();
    }

    protected abstract double lli0OiIlAND();

    @Override
    public int[] OOOIilanD() {
        return (int[])this.OOOIilanD.get();
    }

    protected abstract int[] li0iOILAND();

    @Override
    public double lI00OlAND() {
        return (Double)this.lI00OlAND.get();
    }

    protected abstract double O1il1llOLANd();

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CPU Temperature=").append(this.I1O1I1LaNd()).append("\u00b0C, ");
        stringBuilder.append("Fan Speeds=").append(Arrays.toString(this.OOOIilanD())).append(", ");
        stringBuilder.append("CPU Voltage=").append(this.lI00OlAND());
        return stringBuilder.toString();
    }
}

