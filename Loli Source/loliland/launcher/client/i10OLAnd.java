/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.LibCAPI;
import loliland.launcher.client.Iil1Ol1laNd;
import loliland.launcher.client.O0iIl1ilaND;
import loliland.launcher.client.llllil1LaND;

final class i10OLAnd
extends llllil1LaND {
    i10OLAnd() {
    }

    @Override
    public double lli0OiIlAND() {
        return i10OLAnd.Oill1LAnD();
    }

    private static double Oill1LAnD() {
        String string = "dev.cpu.%d.temperature";
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND(new LibCAPI.size_t((long)Iil1Ol1laNd.INT_SIZE));
        Memory memory = new Memory(o0iIl1ilaND.getValue().longValue());
        int n2 = 0;
        double d2 = 0.0;
        while (0 == Iil1Ol1laNd.INSTANCE.sysctlbyname(String.format(string, n2), memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            d2 += (double)((Pointer)memory).getInt(0L) / 10.0 - 273.15;
            ++n2;
        }
        return n2 > 0 ? d2 / (double)n2 : Double.NaN;
    }

    @Override
    public int[] li0iOILAND() {
        return new int[0];
    }

    @Override
    public double O1il1llOLANd() {
        return 0.0;
    }
}

