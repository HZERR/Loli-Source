/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.IOKitUtil;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import loliland.launcher.client.OI1l11LaNd;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.il1iIIILaND;
import loliland.launcher.client.lii1IO0LaNd;

final class iOIiIIOOlaNd
extends il1iIIILaND {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(iOIiIIOOlaNd::O1il1llOLANd);

    iOIiIIOOlaNd() {
    }

    @Override
    public String I1O1I1LaNd() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).I1O1I1LaNd();
    }

    @Override
    public String OOOIilanD() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).OOOIilanD();
    }

    @Override
    public String lI00OlAND() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).lI00OlAND();
    }

    @Override
    public String lli0OiIlAND() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).lli0OiIlAND();
    }

    @Override
    public String li0iOILAND() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).li0iOILAND();
    }

    private static OI1l11LaNd O1il1llOLANd() {
        String string = null;
        String string2 = null;
        String string3 = null;
        String string4 = null;
        String string5 = null;
        IOKit.IOService iOService = IOKitUtil.getMatchingService("IOPlatformExpertDevice");
        if (iOService != null) {
            byte[] arrby;
            IOKit.IOIterator iOIterator = iOService.getChildIterator("IODeviceTree");
            if (iOIterator != null) {
                IOKit.IORegistryEntry iORegistryEntry = iOIterator.next();
                while (iORegistryEntry != null) {
                    switch (iORegistryEntry.getName()) {
                        case "rom": {
                            arrby = iORegistryEntry.getByteArrayProperty("vendor");
                            if (arrby != null) {
                                string = Native.toString(arrby, StandardCharsets.UTF_8);
                            }
                            if ((arrby = iORegistryEntry.getByteArrayProperty("version")) != null) {
                                string4 = Native.toString(arrby, StandardCharsets.UTF_8);
                            }
                            if ((arrby = iORegistryEntry.getByteArrayProperty("release-date")) == null) break;
                            string5 = Native.toString(arrby, StandardCharsets.UTF_8);
                            break;
                        }
                        case "chosen": {
                            arrby = iORegistryEntry.getByteArrayProperty("booter-name");
                            if (arrby == null) break;
                            string2 = Native.toString(arrby, StandardCharsets.UTF_8);
                            break;
                        }
                        case "efi": {
                            arrby = iORegistryEntry.getByteArrayProperty("firmware-abi");
                            if (arrby == null) break;
                            string3 = Native.toString(arrby, StandardCharsets.UTF_8);
                            break;
                        }
                        default: {
                            if (!iiIIIlO1lANd.I1O1I1LaNd(string2)) break;
                            string2 = iORegistryEntry.getStringProperty("IONameMatch");
                        }
                    }
                    iORegistryEntry.release();
                    iORegistryEntry = iOIterator.next();
                }
                iOIterator.release();
            }
            if (iiIIIlO1lANd.I1O1I1LaNd(string) && (arrby = iOService.getByteArrayProperty("manufacturer")) != null) {
                string = Native.toString(arrby, StandardCharsets.UTF_8);
            }
            if (iiIIIlO1lANd.I1O1I1LaNd(string4) && (arrby = iOService.getByteArrayProperty("target-type")) != null) {
                string4 = Native.toString(arrby, StandardCharsets.UTF_8);
            }
            if (iiIIIlO1lANd.I1O1I1LaNd(string2) && (arrby = iOService.getByteArrayProperty("device_type")) != null) {
                string2 = Native.toString(arrby, StandardCharsets.UTF_8);
            }
            iOService.release();
        }
        return new OI1l11LaNd(iiIIIlO1lANd.I1O1I1LaNd(string) ? "unknown" : string, iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2, iiIIIlO1lANd.I1O1I1LaNd(string3) ? "unknown" : string3, iiIIIlO1lANd.I1O1I1LaNd(string4) ? "unknown" : string4, iiIIIlO1lANd.I1O1I1LaNd(string5) ? "unknown" : string5);
    }
}

