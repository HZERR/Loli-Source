/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.mac.SystemB;
import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.ptr.IntByReference;
import loliland.launcher.client.O0iIl1ilaND;
import loliland.launcher.client.lIOOOlIlaNd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class liiIILaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(liiIILaND.class);
    private static final String OOOIilanD = "Failed syctl call: {}, Error code: {}";

    private liiIILaND() {
    }

    public static int I1O1I1LaNd(String string, int n2) {
        IntByReference intByReference = new IntByReference(SystemB.INT_SIZE);
        Memory memory = new Memory(intByReference.getValue());
        if (0 != lIOOOlIlaNd.INSTANCE.sysctlbyname(string, (Pointer)memory, intByReference, null, 0)) {
            I1O1I1LaNd.error("Failed sysctl call: {}, Error code: {}", (Object)string, (Object)Native.getLastError());
            return n2;
        }
        return ((Pointer)memory).getInt(0L);
    }

    public static long I1O1I1LaNd(String string, long l2) {
        IntByReference intByReference = new IntByReference(SystemB.UINT64_SIZE);
        Memory memory = new Memory(intByReference.getValue());
        if (0 != lIOOOlIlaNd.INSTANCE.sysctlbyname(string, (Pointer)memory, intByReference, null, 0)) {
            I1O1I1LaNd.error(OOOIilanD, (Object)string, (Object)Native.getLastError());
            return l2;
        }
        return ((Pointer)memory).getLong(0L);
    }

    public static String I1O1I1LaNd(String string, String string2) {
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND();
        if (0 != lIOOOlIlaNd.INSTANCE.sysctlbyname(string, null, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            I1O1I1LaNd.error(OOOIilanD, (Object)string, (Object)Native.getLastError());
            return string2;
        }
        Memory memory = new Memory(o0iIl1ilaND.getValue().longValue() + 1L);
        if (0 != lIOOOlIlaNd.INSTANCE.sysctlbyname(string, (Pointer)memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            I1O1I1LaNd.error(OOOIilanD, (Object)string, (Object)Native.getLastError());
            return string2;
        }
        return memory.getString(0L);
    }

    public static boolean I1O1I1LaNd(String string, Structure structure) {
        if (0 != lIOOOlIlaNd.INSTANCE.sysctlbyname(string, structure.getPointer(), new O0iIl1ilaND(new LibCAPI.size_t((long)structure.size())), null, LibCAPI.size_t.ZERO)) {
            I1O1I1LaNd.error(OOOIilanD, (Object)string, (Object)Native.getLastError());
            return false;
        }
        structure.read();
        return true;
    }

    public static Memory I1O1I1LaNd(String string) {
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND();
        if (0 != lIOOOlIlaNd.INSTANCE.sysctlbyname(string, null, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            I1O1I1LaNd.error(OOOIilanD, (Object)string, (Object)Native.getLastError());
            return null;
        }
        Memory memory = new Memory(o0iIl1ilaND.getValue().longValue());
        if (0 != lIOOOlIlaNd.INSTANCE.sysctlbyname(string, (Pointer)memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            I1O1I1LaNd.error(OOOIilanD, (Object)string, (Object)Native.getLastError());
            return null;
        }
        return memory;
    }
}

