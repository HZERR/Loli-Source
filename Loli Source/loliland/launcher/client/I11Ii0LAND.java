/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;

final class I11Ii0LAND
implements Supplier {
    final Supplier I1O1I1LaNd;
    volatile Object OOOIilanD;
    volatile long lI00OlAND;
    final /* synthetic */ Supplier lli0OiIlAND;
    final /* synthetic */ long li0iOILAND;

    I11Ii0LAND(Supplier supplier, long l2) {
        this.lli0OiIlAND = supplier;
        this.li0iOILAND = l2;
        this.I1O1I1LaNd = this.lli0OiIlAND;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Object get() {
        long l2 = this.lI00OlAND;
        long l3 = System.nanoTime();
        if (l2 == 0L || this.li0iOILAND >= 0L && l3 - l2 >= 0L) {
            I11Ii0LAND i11Ii0LAND = this;
            synchronized (i11Ii0LAND) {
                if (l2 == this.lI00OlAND) {
                    Object t2 = this.I1O1I1LaNd.get();
                    this.OOOIilanD = t2;
                    l2 = l3 + this.li0iOILAND;
                    this.lI00OlAND = l2 == 0L ? 1L : l2;
                    return t2;
                }
            }
        }
        return this.OOOIilanD;
    }
}

