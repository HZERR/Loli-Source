/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.l0O0OI0lanD;
import loliland.launcher.client.lii1IO0LaNd;

public abstract class l1i0ILaND
implements l0O0OI0lanD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(this::l0iIlIO1laNd, lii1IO0LaNd.I1O1I1LaNd());
    private final int OOOIilanD;

    protected l1i0ILaND(int n2) {
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

    private double l0iIlIO1laNd() {
        return (double)this.l0illAND() > 0.0 ? (double)(this.iilIi1laND() + this.lli011lLANd()) / (double)this.l0illAND() : 0.0;
    }

    @Override
    public double I1O1I1LaNd(l0O0OI0lanD l0O0OI0lanD2) {
        if (l0O0OI0lanD2 != null && this.OOOIilanD == l0O0OI0lanD2.I1O1I1LaNd() && this.lI00OlAND() == l0O0OI0lanD2.lI00OlAND() && this.l0illAND() > l0O0OI0lanD2.l0illAND()) {
            return (double)(this.lli011lLANd() - l0O0OI0lanD2.lli011lLANd() + this.iilIi1laND() - l0O0OI0lanD2.iilIi1laND()) / (double)(this.l0illAND() - l0O0OI0lanD2.l0illAND());
        }
        return this.OOOIilanD();
    }

    public String toString() {
        return "OSThread [threadId=" + this.lI00OlAND() + ", owningProcessId=" + this.I1O1I1LaNd() + ", name=" + this.lli0OiIlAND() + ", state=" + (Object)((Object)this.li0iOILAND()) + ", kernelTime=" + this.iilIi1laND() + ", userTime=" + this.lli011lLANd() + ", upTime=" + this.l0illAND() + ", startTime=" + this.IO11O0LANd() + ", startMemoryAddress=0x" + String.format("%x", this.O1il1llOLANd()) + ", contextSwitches=" + this.Oill1LAnD() + ", minorFaults=" + this.lIOILand() + ", majorFaults=" + this.lil0liLand() + "]";
    }
}

