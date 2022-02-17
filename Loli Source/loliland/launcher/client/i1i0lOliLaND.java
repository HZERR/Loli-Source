/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import loliland.launcher.client.i00ilaNd;
import loliland.launcher.client.lOIlIi0IlaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.lii1IO0LaNd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class i1i0lOliLaND
implements lOIlIi0IlaNd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(i1i0lOliLaND.class);
    private static final String OOOIilanD = "oshi.vmmacaddr.properties";
    private NetworkInterface lI00OlAND;
    private String lli0OiIlAND;
    private String li0iOILAND;
    private int O1il1llOLANd;
    private int Oill1LAnD;
    private String lIOILand;
    private String[] lil0liLand;
    private Short[] iilIi1laND;
    private String[] lli011lLANd;
    private Short[] l0illAND;
    private final Supplier IO11O0LANd = lii1IO0LaNd.I1O1I1LaNd(i1i0lOliLaND::OOOliOOllANd);

    protected i1i0lOliLaND(NetworkInterface networkInterface) throws InstantiationException {
        this(networkInterface, networkInterface.getDisplayName());
    }

    protected i1i0lOliLaND(NetworkInterface networkInterface, String string) throws InstantiationException {
        this.lI00OlAND = networkInterface;
        try {
            ArrayList<String> arrayList;
            this.lli0OiIlAND = this.lI00OlAND.getName();
            this.li0iOILAND = string;
            this.O1il1llOLANd = this.lI00OlAND.getIndex();
            this.Oill1LAnD = this.lI00OlAND.getMTU();
            byte[] arrby = this.lI00OlAND.getHardwareAddress();
            if (arrby != null) {
                arrayList = new ArrayList<String>(6);
                for (byte by : arrby) {
                    arrayList.add(String.format("%02x", by));
                }
                this.lIOILand = String.join((CharSequence)":", arrayList);
            } else {
                this.lIOILand = "unknown";
            }
            arrayList = new ArrayList();
            Object object = new ArrayList();
            ArrayList<String> arrayList2 = new ArrayList<String>();
            ArrayList<Short> arrayList3 = new ArrayList<Short>();
            for (InterfaceAddress interfaceAddress : this.lI00OlAND.getInterfaceAddresses()) {
                InetAddress inetAddress = interfaceAddress.getAddress();
                if (inetAddress.getHostAddress().length() <= 0) continue;
                if (inetAddress.getHostAddress().contains(":")) {
                    arrayList2.add(inetAddress.getHostAddress().split("%")[0]);
                    arrayList3.add(interfaceAddress.getNetworkPrefixLength());
                    continue;
                }
                arrayList.add(inetAddress.getHostAddress());
                ((ArrayList)object).add(interfaceAddress.getNetworkPrefixLength());
            }
            this.lil0liLand = arrayList.toArray(new String[0]);
            this.iilIi1laND = ((ArrayList)object).toArray(new Short[0]);
            this.lli011lLANd = arrayList2.toArray(new String[0]);
            this.l0illAND = arrayList3.toArray(new Short[0]);
        }
        catch (SocketException socketException) {
            throw new InstantiationException(socketException.getMessage());
        }
    }

    protected static List I1O1I1LaNd(boolean bl) {
        List list = i1i0lOliLaND.II1i1l0laND();
        return bl ? list : i1i0lOliLaND.II1i1l0laND().stream().filter(networkInterface -> !i1i0lOliLaND.I1O1I1LaNd(networkInterface)).collect(Collectors.toList());
    }

    private static List II1i1l0laND() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            return enumeration == null ? Collections.emptyList() : Collections.list(enumeration);
        }
        catch (SocketException socketException) {
            I1O1I1LaNd.error("Socket exception when retrieving interfaces: {}", (Object)socketException.getMessage());
            return Collections.emptyList();
        }
    }

    private static boolean I1O1I1LaNd(NetworkInterface networkInterface) {
        try {
            return networkInterface.getHardwareAddress() == null;
        }
        catch (SocketException socketException) {
            I1O1I1LaNd.error("Socket exception when retrieving interface information for {}: {}", (Object)networkInterface, (Object)socketException.getMessage());
            return false;
        }
    }

    @Override
    public NetworkInterface I1O1I1LaNd() {
        return this.lI00OlAND;
    }

    @Override
    public String OOOIilanD() {
        return this.lli0OiIlAND;
    }

    @Override
    public int lI00OlAND() {
        return this.O1il1llOLANd;
    }

    @Override
    public String lli0OiIlAND() {
        return this.li0iOILAND;
    }

    @Override
    public int Oill1LAnD() {
        return this.Oill1LAnD;
    }

    @Override
    public String lIOILand() {
        return this.lIOILand;
    }

    @Override
    public String[] lil0liLand() {
        return Arrays.copyOf(this.lil0liLand, this.lil0liLand.length);
    }

    @Override
    public Short[] iilIi1laND() {
        return Arrays.copyOf(this.iilIi1laND, this.iilIi1laND.length);
    }

    @Override
    public String[] lli011lLANd() {
        return Arrays.copyOf(this.lli011lLANd, this.lli011lLANd.length);
    }

    @Override
    public Short[] l0illAND() {
        return Arrays.copyOf(this.l0illAND, this.l0illAND.length);
    }

    @Override
    public boolean l0IO0LAnd() {
        String string = this.lIOILand().length() > 7 ? this.lIOILand().substring(0, 8) : this.lIOILand();
        return ((Properties)this.IO11O0LANd.get()).containsKey(string.toUpperCase());
    }

    private static Properties OOOliOOllANd() {
        return liOIOOlLAnD.O1il1llOLANd(OOOIilanD);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: ").append(this.OOOIilanD());
        if (!this.OOOIilanD().equals(this.lli0OiIlAND())) {
            stringBuilder.append(" (").append(this.lli0OiIlAND()).append(")");
        }
        if (!this.li0iOILAND().isEmpty()) {
            stringBuilder.append(" [IfAlias=").append(this.li0iOILAND()).append("]");
        }
        stringBuilder.append("\n");
        stringBuilder.append("  MAC Address: ").append(this.lIOILand()).append("\n");
        stringBuilder.append("  MTU: ").append(lOilLanD.I1O1I1LaNd(this.Oill1LAnD())).append(", ").append("Speed: ").append(this.lIiIii1LAnD()).append("\n");
        Object[] arrobject = this.lil0liLand();
        if (this.lil0liLand.length == this.iilIi1laND.length) {
            for (int i2 = 0; i2 < this.iilIi1laND.length; ++i2) {
                int n2 = i2;
                arrobject[n2] = (String)arrobject[n2] + "/" + this.iilIi1laND[i2];
            }
        }
        stringBuilder.append("  IPv4: ").append(Arrays.toString(arrobject)).append("\n");
        Object[] arrobject2 = this.lli011lLANd();
        if (this.lli011lLANd.length == this.l0illAND.length) {
            for (int i3 = 0; i3 < this.l0illAND.length; ++i3) {
                int n3 = i3;
                arrobject2[n3] = (String)arrobject2[n3] + "/" + this.l0illAND[i3];
            }
        }
        stringBuilder.append("  IPv6: ").append(Arrays.toString(arrobject2)).append("\n");
        stringBuilder.append("  Traffic: received ").append(this.iIiO00OLaNd()).append(" packets/").append(i00ilaNd.I1O1I1LaNd(this.l0iIlIO1laNd())).append(" (" + this.IOI1LaNd() + " err, ").append(this.l0l00lAND() + " drop);");
        stringBuilder.append(" transmitted ").append(this.ii1li00Land()).append(" packets/").append(i00ilaNd.I1O1I1LaNd(this.iOIl0LAnD())).append(" (" + this.lI00ilAND() + " err, ").append(this.iOl10IlLAnd() + " coll);");
        return stringBuilder.toString();
    }
}

