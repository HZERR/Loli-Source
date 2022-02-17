/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.NativeLong;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.IOKitUtil;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import loliland.launcher.client.I10IOOlAnD;
import loliland.launcher.client.OO11OLANd;
import loliland.launcher.client.lIOOOlIlaNd;
import loliland.launcher.client.lIlOO1llAnd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lll11IILaNd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class l1i0iO00LAnd {
    private static final Logger lIOILand = LoggerFactory.getLogger(l1i0iO00LAnd.class);
    private static final lll11IILaNd lil0liLand = lll11IILaNd.INSTANCE;
    private static Map iilIi1laND = new ConcurrentHashMap();
    private static final byte[] lli011lLANd = lOilLanD.OOOIilanD("sp78", 5);
    private static final byte[] l0illAND = lOilLanD.OOOIilanD("fpe2", 5);
    private static final byte[] IO11O0LANd = lOilLanD.OOOIilanD("flt ", 5);
    public static final String I1O1I1LaNd = "FNum";
    public static final String OOOIilanD = "F%dAc";
    public static final String lI00OlAND = "TC0P";
    public static final String lli0OiIlAND = "VC0C";
    public static final byte li0iOILAND = 5;
    public static final byte O1il1llOLANd = 9;
    public static final int Oill1LAnD = 2;

    private l1i0iO00LAnd() {
    }

    public static IOKit.IOConnect I1O1I1LaNd() {
        IOKit.IOService iOService = IOKitUtil.getMatchingService("AppleSMC");
        if (iOService != null) {
            PointerByReference pointerByReference = new PointerByReference();
            int n2 = lil0liLand.IOServiceOpen(iOService, lIOOOlIlaNd.INSTANCE.mach_task_self(), 0, pointerByReference);
            iOService.release();
            if (n2 == 0) {
                return new IOKit.IOConnect(pointerByReference.getValue());
            }
            if (lIOILand.isErrorEnabled()) {
                lIOILand.error(String.format("Unable to open connection to AppleSMC service. Error: 0x%08x", n2));
            }
        } else {
            lIOILand.error("Unable to locate AppleSMC service");
        }
        return null;
    }

    public static int I1O1I1LaNd(IOKit.IOConnect iOConnect) {
        return lil0liLand.IOServiceClose(iOConnect);
    }

    public static double I1O1I1LaNd(IOKit.IOConnect iOConnect, String string) {
        I10IOOlAnD i10IOOlAnD = new I10IOOlAnD();
        int n2 = l1i0iO00LAnd.I1O1I1LaNd(iOConnect, string, i10IOOlAnD);
        if (n2 == 0 && i10IOOlAnD.dataSize > 0) {
            if (Arrays.equals(i10IOOlAnD.dataType, lli011lLANd) && i10IOOlAnD.dataSize == 2) {
                return (double)i10IOOlAnD.bytes[0] + (double)i10IOOlAnD.bytes[1] / 256.0;
            }
            if (Arrays.equals(i10IOOlAnD.dataType, l0illAND) && i10IOOlAnD.dataSize == 2) {
                return lOilLanD.I1O1I1LaNd(i10IOOlAnD.bytes, i10IOOlAnD.dataSize, 2);
            }
            if (Arrays.equals(i10IOOlAnD.dataType, IO11O0LANd) && i10IOOlAnD.dataSize == 4) {
                return ByteBuffer.wrap(i10IOOlAnD.bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            }
        }
        return 0.0;
    }

    public static long OOOIilanD(IOKit.IOConnect iOConnect, String string) {
        I10IOOlAnD i10IOOlAnD = new I10IOOlAnD();
        int n2 = l1i0iO00LAnd.I1O1I1LaNd(iOConnect, string, i10IOOlAnD);
        if (n2 == 0) {
            return lOilLanD.I1O1I1LaNd(i10IOOlAnD.bytes, i10IOOlAnD.dataSize);
        }
        return 0L;
    }

    public static int I1O1I1LaNd(IOKit.IOConnect iOConnect, lIlOO1llAnd lIlOO1llAnd2, lIlOO1llAnd lIlOO1llAnd3) {
        if (iilIi1laND.containsKey(lIlOO1llAnd2.key)) {
            OO11OLANd oO11OLANd = (OO11OLANd)iilIi1laND.get(lIlOO1llAnd2.key);
            lIlOO1llAnd3.keyInfo.dataSize = oO11OLANd.dataSize;
            lIlOO1llAnd3.keyInfo.dataType = oO11OLANd.dataType;
            lIlOO1llAnd3.keyInfo.dataAttributes = oO11OLANd.dataAttributes;
        } else {
            lIlOO1llAnd2.data8 = (byte)9;
            int n2 = l1i0iO00LAnd.I1O1I1LaNd(iOConnect, 2, lIlOO1llAnd2, lIlOO1llAnd3);
            if (n2 != 0) {
                return n2;
            }
            OO11OLANd oO11OLANd = new OO11OLANd();
            oO11OLANd.dataSize = lIlOO1llAnd3.keyInfo.dataSize;
            oO11OLANd.dataType = lIlOO1llAnd3.keyInfo.dataType;
            oO11OLANd.dataAttributes = lIlOO1llAnd3.keyInfo.dataAttributes;
            iilIi1laND.put(lIlOO1llAnd2.key, oO11OLANd);
        }
        return 0;
    }

    public static int I1O1I1LaNd(IOKit.IOConnect iOConnect, String string, I10IOOlAnD i10IOOlAnD) {
        lIlOO1llAnd lIlOO1llAnd2 = new lIlOO1llAnd();
        lIlOO1llAnd lIlOO1llAnd3 = new lIlOO1llAnd();
        lIlOO1llAnd2.key = (int)lOilLanD.lI00OlAND(string, 4);
        int n2 = l1i0iO00LAnd.I1O1I1LaNd(iOConnect, lIlOO1llAnd2, lIlOO1llAnd3);
        if (n2 == 0) {
            i10IOOlAnD.dataSize = lIlOO1llAnd3.keyInfo.dataSize;
            i10IOOlAnD.dataType = lOilLanD.I1O1I1LaNd(lIlOO1llAnd3.keyInfo.dataType, 4, 5);
            lIlOO1llAnd2.keyInfo.dataSize = i10IOOlAnD.dataSize;
            lIlOO1llAnd2.data8 = (byte)5;
            n2 = l1i0iO00LAnd.I1O1I1LaNd(iOConnect, 2, lIlOO1llAnd2, lIlOO1llAnd3);
            if (n2 == 0) {
                System.arraycopy(lIlOO1llAnd3.bytes, 0, i10IOOlAnD.bytes, 0, i10IOOlAnD.bytes.length);
                return 0;
            }
        }
        return n2;
    }

    public static int I1O1I1LaNd(IOKit.IOConnect iOConnect, int n2, lIlOO1llAnd lIlOO1llAnd2, lIlOO1llAnd lIlOO1llAnd3) {
        return lil0liLand.IOConnectCallStructMethod(iOConnect, n2, lIlOO1llAnd2, new NativeLong((long)lIlOO1llAnd2.size()), lIlOO1llAnd3, new NativeLongByReference(new NativeLong((long)lIlOO1llAnd3.size())));
    }
}

