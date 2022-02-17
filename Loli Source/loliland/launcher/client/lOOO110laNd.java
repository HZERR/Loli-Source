/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import loliland.launcher.client.liIli0OlAND;

public final class lOOO110laNd {
    private final String I1O1I1LaNd;
    private final byte[] OOOIilanD;
    private final int lI00OlAND;
    private final byte[] lli0OiIlAND;
    private final int li0iOILAND;
    private final liIli0OlAND O1il1llOLANd;
    private final int Oill1LAnD;
    private final int lIOILand;
    private int lil0liLand;

    public lOOO110laNd(String string, byte[] arrby, int n2, byte[] arrby2, int n3, liIli0OlAND liIli0OlAND2, int n4, int n5, int n6) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = Arrays.copyOf(arrby, arrby.length);
        this.lI00OlAND = n2;
        this.lli0OiIlAND = Arrays.copyOf(arrby2, arrby2.length);
        this.li0iOILAND = n3;
        this.O1il1llOLANd = liIli0OlAND2;
        this.Oill1LAnD = n4;
        this.lIOILand = n5;
        this.lil0liLand = n6;
    }

    public String I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    public byte[] OOOIilanD() {
        return Arrays.copyOf(this.OOOIilanD, this.OOOIilanD.length);
    }

    public int lI00OlAND() {
        return this.lI00OlAND;
    }

    public byte[] lli0OiIlAND() {
        return Arrays.copyOf(this.lli0OiIlAND, this.lli0OiIlAND.length);
    }

    public int li0iOILAND() {
        return this.li0iOILAND;
    }

    public liIli0OlAND O1il1llOLANd() {
        return this.O1il1llOLANd;
    }

    public int Oill1LAnD() {
        return this.Oill1LAnD;
    }

    public int lIOILand() {
        return this.lIOILand;
    }

    public int lil0liLand() {
        return this.lil0liLand;
    }

    public String toString() {
        String string = "*";
        try {
            string = InetAddress.getByAddress(this.OOOIilanD).toString();
        }
        catch (UnknownHostException unknownHostException) {
            // empty catch block
        }
        String string2 = "*";
        try {
            string2 = InetAddress.getByAddress(this.lli0OiIlAND).toString();
        }
        catch (UnknownHostException unknownHostException) {
            // empty catch block
        }
        return "IPConnection [type=" + this.I1O1I1LaNd + ", localAddress=" + string + ", localPort=" + this.lI00OlAND + ", foreignAddress=" + string2 + ", foreignPort=" + this.li0iOILAND + ", state=" + (Object)((Object)this.O1il1llOLANd) + ", transmitQueue=" + this.Oill1LAnD + ", receiveQueue=" + this.lIOILand + ", owningProcessId=" + this.lil0liLand + "]";
    }
}

