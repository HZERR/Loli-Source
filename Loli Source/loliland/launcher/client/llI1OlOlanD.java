/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import loliland.launcher.XLauncher;
import loliland.launcher.client.IiIl0lLANd;
import loliland.launcher.client.OOOOllANd;
import loliland.launcher.client.liIlILAnD;

public class llI1OlOlanD {
    private static ExecutorService I1O1I1LaNd = Executors.newFixedThreadPool(1);

    public static void I1O1I1LaNd() {
        I1O1I1LaNd.execute(() -> {
            Object object2;
            for (Object object2 : XLauncher.getClientManager().lI00OlAND().values()) {
                for (liIlILAnD liIlILAnD2 : ((OOOOllANd)object2).l0iIlIO1laNd()) {
                    liIlILAnD2.I1O1I1LaNd();
                }
            }
            Object object3 = new int[]{0};
            object2 = new Timer();
            ((Timer)object2).scheduleAtFixedRate((TimerTask)new IiIl0lLANd((int[])object3), 0L, 5000L);
        });
    }
}

