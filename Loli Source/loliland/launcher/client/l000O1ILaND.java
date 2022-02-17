/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.i1i1OlLaNd;

public class l000O1ILaND {
    private Process I1O1I1LaNd;

    public l000O1ILaND(Process process, i1i1OlLaNd i1i1OlLaNd2) {
        this.I1O1I1LaNd = process;
    }

    public boolean I1O1I1LaNd() {
        try {
            this.I1O1I1LaNd.exitValue();
        }
        catch (IllegalThreadStateException illegalThreadStateException) {
            return true;
        }
        return false;
    }

    public void OOOIilanD() {
        this.I1O1I1LaNd.destroy();
        try {
            this.I1O1I1LaNd.waitFor();
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public String toString() {
        return "JavaProcess[isRunning=" + this.I1O1I1LaNd() + "]";
    }
}

