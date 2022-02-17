/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import loliland.launcher.client.lOiIl0ilanD;

public class lI10ilAnd {
    private static lOiIl0ilanD I1O1I1LaNd = new lOiIl0ilanD();
    private static ExecutorService OOOIilanD = Executors.newFixedThreadPool(2);
    private static ExecutorService lI00OlAND = Executors.newFixedThreadPool(1);

    public static lOiIl0ilanD I1O1I1LaNd() {
        return I1O1I1LaNd;
    }

    public static ExecutorService OOOIilanD() {
        return OOOIilanD;
    }

    public static ExecutorService lI00OlAND() {
        return lI00OlAND;
    }
}

