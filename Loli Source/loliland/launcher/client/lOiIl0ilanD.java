/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;

public class lOiIl0ilanD {
    private final ArrayList I1O1I1LaNd = new ArrayList();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void I1O1I1LaNd(Runnable runnable) {
        ArrayList arrayList = this.I1O1I1LaNd;
        synchronized (arrayList) {
            this.I1O1I1LaNd.add(runnable);
        }
    }

    private void OOOIilanD(Runnable runnable) {
        try {
            runnable.run();
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void I1O1I1LaNd() {
        ArrayList arrayList = this.I1O1I1LaNd;
        synchronized (arrayList) {
            if (this.I1O1I1LaNd.isEmpty()) {
                return;
            }
            ArrayList arrayList2 = new ArrayList(this.I1O1I1LaNd);
            for (Runnable runnable : arrayList2) {
                this.OOOIilanD(runnable);
            }
            this.I1O1I1LaNd.removeAll(arrayList2);
        }
    }
}

