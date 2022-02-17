/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.IOKitUtil;
import com.sun.jna.platform.mac.SystemB;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.I11llANd;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.OOiO1lAnd;
import loliland.launcher.client.i00ilaNd;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.l0OllOOlland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.liiIILaND;
import loliland.launcher.client.lilOIlANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class liiiI1iLAnD
extends l0OllOOlland {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(liiiI1iLAnD.class);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(liiiI1iLAnD::IOI1LaNd);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(liiiI1iLAnD::lI00ilAND);
    private static final int lli0OiIlAND = 7;
    private static final int li0iOILAND = 1463508716;
    private static final int O1il1llOLANd = 0x100000C;
    private static final int Oill1LAnD = 458787763;

    liiiI1iLAnD() {
    }

    @Override
    protected lilOIlANd IO11O0LANd() {
        String string;
        String string2;
        int n2;
        String string3;
        String string4;
        String string5;
        String string6 = liiIILaND.I1O1I1LaNd("machdep.cpu.brand_string", "");
        long l2 = 0L;
        if (string6.startsWith("Apple")) {
            string5 = (String)this.OOOIilanD.get();
            string4 = "0";
            string3 = "0";
            n2 = liiIILaND.I1O1I1LaNd("hw.cputype", 0);
            int n3 = liiIILaND.I1O1I1LaNd("hw.cpufamily", 0);
            if (n3 == 1463508716) {
                n2 = (Integer)((IIOOOlIiLanD)this.lI00OlAND.get()).I1O1I1LaNd();
                n3 = (Integer)((IIOOOlIiLanD)this.lI00OlAND.get()).OOOIilanD();
            }
            l2 = (Long)((IIOOOlIiLanD)this.lI00OlAND.get()).lI00OlAND();
            string2 = String.format("0x%08x", n3);
            string = String.format("%08x%08x", n2, n3);
        } else {
            string5 = liiIILaND.I1O1I1LaNd("machdep.cpu.vendor", "");
            n2 = liiIILaND.I1O1I1LaNd("machdep.cpu.stepping", -1);
            string4 = n2 < 0 ? "" : Integer.toString(n2);
            n2 = liiIILaND.I1O1I1LaNd("machdep.cpu.model", -1);
            string3 = n2 < 0 ? "" : Integer.toString(n2);
            n2 = liiIILaND.I1O1I1LaNd("machdep.cpu.family", -1);
            string2 = n2 < 0 ? "" : Integer.toString(n2);
            long l3 = 0L;
            l3 |= (long)liiIILaND.I1O1I1LaNd("machdep.cpu.signature", 0);
            string = String.format("%016x", l3 |= (liiIILaND.I1O1I1LaNd("machdep.cpu.feature_bits", 0L) & 0xFFFFFFFFFFFFFFFFL) << 32);
        }
        if (l2 == 0L) {
            l2 = liiIILaND.I1O1I1LaNd("hw.cpufrequency", 0L);
        }
        n2 = liiIILaND.I1O1I1LaNd("hw.cpu64bit_capable", 0) != 0 ? 1 : 0;
        return new lilOIlANd(string5, string6, string2, string3, string4, string, n2 != 0, l2);
    }

    @Override
    protected List l0illAND() {
        int n2 = liiIILaND.I1O1I1LaNd("hw.logicalcpu", 1);
        int n3 = liiIILaND.I1O1I1LaNd("hw.physicalcpu", 1);
        int n4 = liiIILaND.I1O1I1LaNd("hw.packages", 1);
        ArrayList<I11llANd> arrayList = new ArrayList<I11llANd>(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            arrayList.add(new I11llANd(i2, i2 * n3 / n2, i2 * n4 / n2));
        }
        return arrayList;
    }

    @Override
    public long[] iIiO00OLaNd() {
        SystemB.HostCpuLoadInfo hostCpuLoadInfo;
        long[] arrl = new long[OOiO1lAnd.values().length];
        int n2 = SystemB.INSTANCE.mach_host_self();
        if (0 != SystemB.INSTANCE.host_statistics(n2, 3, hostCpuLoadInfo = new SystemB.HostCpuLoadInfo(), new IntByReference(hostCpuLoadInfo.size()))) {
            I1O1I1LaNd.error("Failed to get System CPU ticks. Error code: {} ", (Object)Native.getLastError());
            return arrl;
        }
        arrl[OOiO1lAnd.I1O1I1LaNd.I1O1I1LaNd()] = hostCpuLoadInfo.cpu_ticks[0];
        arrl[OOiO1lAnd.OOOIilanD.I1O1I1LaNd()] = hostCpuLoadInfo.cpu_ticks[3];
        arrl[OOiO1lAnd.lI00OlAND.I1O1I1LaNd()] = hostCpuLoadInfo.cpu_ticks[1];
        arrl[OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] = hostCpuLoadInfo.cpu_ticks[2];
        return arrl;
    }

    @Override
    public long[] lO110l1LANd() {
        long[] arrl = new long[]{liiIILaND.I1O1I1LaNd("hw.cpufrequency", this.I1O1I1LaNd().lil0liLand())};
        return arrl;
    }

    @Override
    public long l11lLANd() {
        return liiIILaND.I1O1I1LaNd("hw.cpufrequency_max", this.I1O1I1LaNd().lil0liLand());
    }

    @Override
    public double[] I1O1I1LaNd(int n2) {
        if (n2 < 1 || n2 > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        double[] arrd = new double[n2];
        int n3 = SystemB.INSTANCE.getloadavg(arrd, n2);
        if (n3 < n2) {
            Arrays.fill(arrd, -1.0);
        }
        return arrd;
    }

    @Override
    public long[][] ii1li00Land() {
        IntByReference intByReference;
        PointerByReference pointerByReference;
        IntByReference intByReference2;
        long[][] arrl = new long[this.Oill1LAnD()][OOiO1lAnd.values().length];
        int n2 = SystemB.INSTANCE.mach_host_self();
        if (0 != SystemB.INSTANCE.host_processor_info(n2, 2, intByReference2 = new IntByReference(), pointerByReference = new PointerByReference(), intByReference = new IntByReference())) {
            I1O1I1LaNd.error("Failed to update CPU Load. Error code: {}", (Object)Native.getLastError());
            return arrl;
        }
        int[] arrn = pointerByReference.getValue().getIntArray(0L, intByReference.getValue());
        for (int i2 = 0; i2 < intByReference2.getValue(); ++i2) {
            int n3 = i2 * 4;
            arrl[i2][OOiO1lAnd.I1O1I1LaNd.I1O1I1LaNd()] = i00ilaNd.I1O1I1LaNd(arrn[n3 + 0]);
            arrl[i2][OOiO1lAnd.OOOIilanD.I1O1I1LaNd()] = i00ilaNd.I1O1I1LaNd(arrn[n3 + 3]);
            arrl[i2][OOiO1lAnd.lI00OlAND.I1O1I1LaNd()] = i00ilaNd.I1O1I1LaNd(arrn[n3 + 1]);
            arrl[i2][OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] = i00ilaNd.I1O1I1LaNd(arrn[n3 + 2]);
        }
        return arrl;
    }

    @Override
    public long l0iIlIO1laNd() {
        return 0L;
    }

    @Override
    public long iOIl0LAnD() {
        return 0L;
    }

    private static String IOI1LaNd() {
        String string = null;
        IOKit.IOService iOService = IOKitUtil.getMatchingService("IOPlatformExpertDevice");
        if (iOService != null) {
            byte[] arrby = iOService.getByteArrayProperty("manufacturer");
            if (arrby != null) {
                string = Native.toString(arrby, StandardCharsets.UTF_8);
            }
            iOService.release();
        }
        return iiIIIlO1lANd.I1O1I1LaNd(string) ? "Apple Inc." : string;
    }

    private static IIOOOlIiLanD lI00ilAND() {
        int n2 = 7;
        int n3 = 1463508716;
        long l2 = 0L;
        IOKit.IOIterator iOIterator = IOKitUtil.getMatchingServices("IOPlatformDevice");
        if (iOIterator != null) {
            Object object;
            HashSet<String> hashSet = new HashSet<String>();
            IOKit.IORegistryEntry iORegistryEntry = iOIterator.next();
            while (iORegistryEntry != null) {
                if (iORegistryEntry.getName().startsWith("cpu")) {
                    long l3;
                    object = iORegistryEntry.getByteArrayProperty("clock-frequency");
                    if (object != null && (l3 = lOilLanD.I1O1I1LaNd(object, ((byte[])object).length, false) * 1000L) > l2) {
                        l2 = l3;
                    }
                    if ((object = iORegistryEntry.getByteArrayProperty("compatible")) != null) {
                        for (String string : new String((byte[])object, StandardCharsets.UTF_8).split("\u0000")) {
                            if (string.isEmpty()) continue;
                            hashSet.add(string);
                        }
                    }
                }
                iORegistryEntry.release();
                iORegistryEntry = iOIterator.next();
            }
            iOIterator.release();
            object = Arrays.asList("ARM,v8", "apple,firestorm", "apple,icestorm");
            hashSet.retainAll((Collection<?>)object);
            if (hashSet.size() == object.size()) {
                n2 = 0x100000C;
                n3 = 458787763;
            }
        }
        return new IIOOOlIiLanD(n2, n3, l2);
    }
}

