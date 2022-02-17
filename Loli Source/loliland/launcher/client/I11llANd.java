/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

public class I11llANd {
    private final int I1O1I1LaNd;
    private final int OOOIilanD;
    private final int lI00OlAND;
    private final int lli0OiIlAND;
    private final int li0iOILAND;

    public I11llANd(int n2, int n3, int n4) {
        this(n2, n3, n4, 0, 0);
    }

    public I11llANd(int n2, int n3, int n4, int n5) {
        this(n2, n3, n4, n5, 0);
    }

    public I11llANd(int n2, int n3, int n4, int n5, int n6) {
        this.I1O1I1LaNd = n2;
        this.OOOIilanD = n3;
        this.lI00OlAND = n4;
        this.lli0OiIlAND = n5;
        this.li0iOILAND = n6;
    }

    public int I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    public int OOOIilanD() {
        return this.OOOIilanD;
    }

    public int lI00OlAND() {
        return this.lI00OlAND;
    }

    public int lli0OiIlAND() {
        return this.lli0OiIlAND;
    }

    public int li0iOILAND() {
        return this.li0iOILAND;
    }

    public String toString() {
        return "LogicalProcessor [processorNumber=" + this.I1O1I1LaNd + ", coreNumber=" + this.OOOIilanD + ", packageNumber=" + this.lI00OlAND + ", numaNode=" + this.lli0OiIlAND + ", processorGroup=" + this.li0iOILAND + "]";
    }
}

