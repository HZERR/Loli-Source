/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.OOOOO10iLAND;

public class lIiIii1LAnD {
    private final int I1O1I1LaNd;
    private final long OOOIilanD;
    private final long lI00OlAND;
    private final long lli0OiIlAND;
    private final OOOOO10iLAND li0iOILAND;
    private final int O1il1llOLANd;

    public lIiIii1LAnD(int n2, double d2, char c2, long l2, long l3, int n3) {
        this.I1O1I1LaNd = n2;
        this.OOOIilanD = l3;
        this.lI00OlAND = l2;
        this.lli0OiIlAND = (long)((double)(l3 + l2) / (d2 / 100.0 + 5.0E-4));
        switch (c2) {
            case 'I': 
            case 'S': {
                this.li0iOILAND = OOOOO10iLAND.lI00OlAND;
                break;
            }
            case 'U': {
                this.li0iOILAND = OOOOO10iLAND.lli0OiIlAND;
                break;
            }
            case 'R': {
                this.li0iOILAND = OOOOO10iLAND.OOOIilanD;
                break;
            }
            case 'Z': {
                this.li0iOILAND = OOOOO10iLAND.li0iOILAND;
                break;
            }
            case 'T': {
                this.li0iOILAND = OOOOO10iLAND.O1il1llOLANd;
                break;
            }
            default: {
                this.li0iOILAND = OOOOO10iLAND.Oill1LAnD;
            }
        }
        this.O1il1llOLANd = n3;
    }

    public int I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    public long OOOIilanD() {
        return this.OOOIilanD;
    }

    public long lI00OlAND() {
        return this.lI00OlAND;
    }

    public long lli0OiIlAND() {
        return this.lli0OiIlAND;
    }

    public OOOOO10iLAND li0iOILAND() {
        return this.li0iOILAND;
    }

    public int O1il1llOLANd() {
        return this.O1il1llOLANd;
    }
}

