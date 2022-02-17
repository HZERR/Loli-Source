/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.unix.LibCAPI;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O0iIl1ilaND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lllilIiiLANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class lIl0iilland {
    private static final String I1O1I1LaNd = "sysctl -n ";
    private static final Logger OOOIilanD = LoggerFactory.getLogger(lIl0iilland.class);
    private static final String lI00OlAND = "Failed sysctl call: {}, Error code: {}";

    private lIl0iilland() {
    }

    public static int I1O1I1LaNd(int[] arrn, int n2) {
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND(new LibCAPI.size_t((long)lllilIiiLANd.INT_SIZE));
        Memory memory = new Memory(o0iIl1ilaND.getValue().longValue());
        if (0 != lllilIiiLANd.INSTANCE.sysctl(arrn, arrn.length, memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            OOOIilanD.error(lI00OlAND, (Object)arrn, (Object)Native.getLastError());
            return n2;
        }
        return ((Pointer)memory).getInt(0L);
    }

    public static long I1O1I1LaNd(int[] arrn, long l2) {
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND(new LibCAPI.size_t((long)lllilIiiLANd.UINT64_SIZE));
        Memory memory = new Memory(o0iIl1ilaND.getValue().longValue());
        if (0 != lllilIiiLANd.INSTANCE.sysctl(arrn, arrn.length, memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            OOOIilanD.warn(lI00OlAND, (Object)arrn, (Object)Native.getLastError());
            return l2;
        }
        return ((Pointer)memory).getLong(0L);
    }

    public static String I1O1I1LaNd(int[] arrn, String string) {
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND();
        if (0 != lllilIiiLANd.INSTANCE.sysctl(arrn, arrn.length, null, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            OOOIilanD.warn(lI00OlAND, (Object)arrn, (Object)Native.getLastError());
            return string;
        }
        Memory memory = new Memory(o0iIl1ilaND.getValue().longValue() + 1L);
        if (0 != lllilIiiLANd.INSTANCE.sysctl(arrn, arrn.length, memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            OOOIilanD.warn(lI00OlAND, (Object)arrn, (Object)Native.getLastError());
            return string;
        }
        return memory.getString(0L);
    }

    public static boolean I1O1I1LaNd(int[] arrn, Structure structure) {
        if (0 != lllilIiiLANd.INSTANCE.sysctl(arrn, arrn.length, structure.getPointer(), new O0iIl1ilaND(new LibCAPI.size_t((long)structure.size())), null, LibCAPI.size_t.ZERO)) {
            OOOIilanD.error(lI00OlAND, (Object)arrn, (Object)Native.getLastError());
            return false;
        }
        structure.read();
        return true;
    }

    public static Memory I1O1I1LaNd(int[] arrn) {
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND();
        if (0 != lllilIiiLANd.INSTANCE.sysctl(arrn, arrn.length, null, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            OOOIilanD.error(lI00OlAND, (Object)arrn, (Object)Native.getLastError());
            return null;
        }
        Memory memory = new Memory(o0iIl1ilaND.getValue().longValue());
        if (0 != lllilIiiLANd.INSTANCE.sysctl(arrn, arrn.length, memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            OOOIilanD.error(lI00OlAND, (Object)arrn, (Object)Native.getLastError());
            return null;
        }
        return memory;
    }

    public static int I1O1I1LaNd(String string, int n2) {
        return lOilLanD.lli0OiIlAND(Iill1lanD.OOOIilanD(I1O1I1LaNd + string), n2);
    }

    public static long I1O1I1LaNd(String string, long l2) {
        return lOilLanD.OOOIilanD(Iill1lanD.OOOIilanD(I1O1I1LaNd + string), l2);
    }

    public static String I1O1I1LaNd(String string, String string2) {
        String string3 = Iill1lanD.OOOIilanD(I1O1I1LaNd + string);
        if (null == string3 || string3.isEmpty()) {
            return string2;
        }
        return string3;
    }
}

