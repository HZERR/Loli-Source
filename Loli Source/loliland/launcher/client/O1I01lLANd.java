/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.lii1IO0LaNd;

public abstract class O1I01lLANd
implements iiO1LAnD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(this::ll1ILAnd, lii1IO0LaNd.I1O1I1LaNd());
    private int OOOIilanD;

    protected O1I01lLANd(int n2) {
        this.OOOIilanD = n2;
    }

    @Override
    public int I1O1I1LaNd() {
        return this.OOOIilanD;
    }

    @Override
    public double OOOIilanD() {
        return (Double)this.I1O1I1LaNd.get();
    }

    private double ll1ILAnd() {
        return (double)this.ii1li00Land() > 0.0 ? (double)(this.iOIl0LAnD() + this.iIiO00OLaNd()) / (double)this.ii1li00Land() : 0.0;
    }

    @Override
    public double I1O1I1LaNd(iiO1LAnD iiO1LAnD2) {
        if (iiO1LAnD2 != null && this.OOOIilanD == iiO1LAnD2.I1O1I1LaNd() && this.ii1li00Land() > iiO1LAnD2.ii1li00Land()) {
            return (double)(this.iIiO00OLaNd() - iiO1LAnD2.iIiO00OLaNd() + this.iOIl0LAnD() - iiO1LAnD2.iOIl0LAnD()) / (double)(this.ii1li00Land() - iiO1LAnD2.ii1li00Land());
        }
        return this.OOOIilanD();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("OSProcess@");
        stringBuilder.append(Integer.toHexString(this.hashCode()));
        stringBuilder.append("[processID=").append(this.OOOIilanD);
        stringBuilder.append(", name=").append(this.lI00OlAND()).append(']');
        return stringBuilder.toString();
    }
}

