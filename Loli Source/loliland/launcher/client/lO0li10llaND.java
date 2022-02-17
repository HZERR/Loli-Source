/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.solaris.LibKstat;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;
import loliland.launcher.client.i00ilaNd;
import loliland.launcher.client.lIiIlOi0lanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class lO0li10llaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lO0li10llaND.class);
    private static final LibKstat OOOIilanD = LibKstat.INSTANCE;
    private static final LibKstat.KstatCtl lI00OlAND = OOOIilanD.kstat_open();
    private static final ReentrantLock lli0OiIlAND = new ReentrantLock();

    private lO0li10llaND() {
    }

    public static lIiIlOi0lanD I1O1I1LaNd() {
        return new lIiIlOi0lanD(null);
    }

    public static String I1O1I1LaNd(LibKstat.Kstat kstat, String string) {
        if (kstat.ks_type != 1 && kstat.ks_type != 4) {
            throw new IllegalArgumentException("Not a kstat_named or kstat_timer kstat.");
        }
        Pointer pointer = OOOIilanD.kstat_data_lookup(kstat, string);
        if (pointer == null) {
            I1O1I1LaNd.debug("Failed to lookup kstat value for key {}", (Object)string);
            return "";
        }
        LibKstat.KstatNamed kstatNamed = new LibKstat.KstatNamed(pointer);
        switch (kstatNamed.data_type) {
            case 0: {
                return Native.toString(kstatNamed.value.charc, StandardCharsets.UTF_8);
            }
            case 1: {
                return Integer.toString(kstatNamed.value.i32);
            }
            case 2: {
                return i00ilaNd.OOOIilanD(kstatNamed.value.ui32);
            }
            case 3: {
                return Long.toString(kstatNamed.value.i64);
            }
            case 4: {
                return i00ilaNd.li0iOILAND(kstatNamed.value.ui64);
            }
            case 9: {
                return kstatNamed.value.str.addr.getString(0L);
            }
        }
        I1O1I1LaNd.error("Unimplemented kstat data type {}", (Object)kstatNamed.data_type);
        return "";
    }

    public static long OOOIilanD(LibKstat.Kstat kstat, String string) {
        if (kstat.ks_type != 1 && kstat.ks_type != 4) {
            throw new IllegalArgumentException("Not a kstat_named or kstat_timer kstat.");
        }
        Pointer pointer = OOOIilanD.kstat_data_lookup(kstat, string);
        if (pointer == null) {
            if (I1O1I1LaNd.isErrorEnabled()) {
                I1O1I1LaNd.error("Failed lo lookup kstat value on {}:{}:{} for key {}", Native.toString(kstat.ks_module, StandardCharsets.US_ASCII), kstat.ks_instance, Native.toString(kstat.ks_name, StandardCharsets.US_ASCII), string);
            }
            return 0L;
        }
        LibKstat.KstatNamed kstatNamed = new LibKstat.KstatNamed(pointer);
        switch (kstatNamed.data_type) {
            case 1: {
                return kstatNamed.value.i32;
            }
            case 2: {
                return i00ilaNd.I1O1I1LaNd(kstatNamed.value.ui32);
            }
            case 3: {
                return kstatNamed.value.i64;
            }
            case 4: {
                return kstatNamed.value.ui64;
            }
        }
        I1O1I1LaNd.error("Unimplemented or non-numeric kstat data type {}", (Object)kstatNamed.data_type);
        return 0L;
    }

    static /* synthetic */ ReentrantLock OOOIilanD() {
        return lli0OiIlAND;
    }

    static /* synthetic */ LibKstat.KstatCtl lI00OlAND() {
        return lI00OlAND;
    }

    static /* synthetic */ LibKstat lli0OiIlAND() {
        return OOOIilanD;
    }

    static /* synthetic */ Logger li0iOILAND() {
        return I1O1I1LaNd;
    }
}

