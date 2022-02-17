/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.DiskArbitration;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.IOKitUtil;
import com.sun.jna.platform.mac.SystemB;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import loliland.launcher.client.ii0l1lAnd;
import loliland.launcher.client.l000iLAnd;
import loliland.launcher.client.l00i1O0LAnD;
import loliland.launcher.client.lI10lAnd;
import loliland.launcher.client.liiIILaND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class i1iilAND
extends lI10lAnd {
    private static final Logger lil0liLand = LoggerFactory.getLogger(i1iilAND.class);
    public static final String li0iOILAND = "oshi.os.mac.filesystem.path.excludes";
    public static final String O1il1llOLANd = "oshi.os.mac.filesystem.path.includes";
    public static final String Oill1LAnD = "oshi.os.mac.filesystem.volume.excludes";
    public static final String lIOILand = "oshi.os.mac.filesystem.volume.includes";
    private static final List iilIi1laND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.mac.filesystem.path.excludes");
    private static final List lli011lLANd = l00i1O0LAnD.I1O1I1LaNd("oshi.os.mac.filesystem.path.includes");
    private static final List l0illAND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.mac.filesystem.volume.excludes");
    private static final List IO11O0LANd = l00i1O0LAnD.I1O1I1LaNd("oshi.os.mac.filesystem.volume.includes");
    private static final Pattern l11lLANd = Pattern.compile("/dev/disk\\d");
    private static final int lO110l1LANd = 1;
    private static final int l0iIlIO1laNd = 2;
    private static final int iOIl0LAnD = 4;
    private static final int iIiO00OLaNd = 8;
    private static final int ii1li00Land = 16;
    private static final int IOI1LaNd = 32;
    private static final int lI00ilAND = 64;
    private static final int l0l00lAND = 128;
    private static final int iOl10IlLAnd = 256;
    private static final int lIiIii1LAnD = 1024;
    private static final int II1Iland = 4096;
    private static final int l0IO0LAnd = 8192;
    private static final int liOIOOLANd = 16384;
    private static final int II1i1l0laND = 32768;
    private static final int OOOliOOllANd = 0x100000;
    private static final int IiiilAnD = 0x200000;
    private static final int ll1ILAnd = 0x400000;
    private static final int l1i00lLAnD = 0x800000;
    private static final int lili0l0laNd = 0x1000000;
    private static final int liIIllIland = 0x2000000;
    private static final int l0Oil0IILAnd = 0x4000000;
    private static final int l00OlIlAnd = 0x10000000;
    private static final Map liO1Oli1lanD = new HashMap();

    @Override
    public List I1O1I1LaNd(boolean bl) {
        return i1iilAND.I1O1I1LaNd(null, bl);
    }

    static List I1O1I1LaNd(String string) {
        return i1iilAND.I1O1I1LaNd(string, false);
    }

    private static List I1O1I1LaNd(String string, boolean bl) {
        ArrayList<ii0l1lAnd> arrayList = new ArrayList<ii0l1lAnd>();
        int n2 = SystemB.INSTANCE.getfsstat64(null, 0, 0);
        if (n2 > 0) {
            DiskArbitration.DASessionRef dASessionRef = DiskArbitration.INSTANCE.DASessionCreate(CoreFoundation.INSTANCE.CFAllocatorGetDefault());
            if (dASessionRef == null) {
                lil0liLand.error("Unable to open session to DiskArbitration framework.");
            } else {
                CoreFoundation.CFStringRef cFStringRef = CoreFoundation.CFStringRef.createCFString("DAVolumeName");
                SystemB.Statfs[] arrstatfs = new SystemB.Statfs[n2];
                n2 = SystemB.INSTANCE.getfsstat64(arrstatfs, n2 * new SystemB.Statfs().size(), 16);
                for (int i2 = 0; i2 < n2; ++i2) {
                    String string2 = Native.toString(arrstatfs[i2].f_mntfromname, StandardCharsets.UTF_8);
                    String string3 = Native.toString(arrstatfs[i2].f_mntonname, StandardCharsets.UTF_8);
                    String string4 = Native.toString(arrstatfs[i2].f_fstypename, StandardCharsets.UTF_8);
                    int n3 = arrstatfs[i2].f_flags;
                    if (bl && (n3 & 0x1000) == 0 || !string3.equals("/") && (lli0OiIlAND.contains(string4) || l00i1O0LAnD.I1O1I1LaNd(string3, string2, lli011lLANd, iilIi1laND, IO11O0LANd, l0illAND))) continue;
                    String string5 = "Volume";
                    if (l11lLANd.matcher(string2).matches()) {
                        string5 = "Local Disk";
                    } else if (string2.startsWith("localhost:") || string2.startsWith("//") || string2.startsWith("smb://") || lI00OlAND.contains(string4)) {
                        string5 = "Network Drive";
                    }
                    String string6 = "";
                    File file = new File(string3);
                    if (string6.isEmpty() && (string6 = file.getName()).isEmpty()) {
                        string6 = file.getPath();
                    }
                    if (string != null && !string.equals(string6)) continue;
                    StringBuilder stringBuilder = new StringBuilder((1 & n3) == 0 ? "rw" : "ro");
                    String string7 = liO1Oli1lanD.entrySet().stream().filter(entry -> ((Integer)entry.getKey() & n3) > 0).map(Map.Entry::getValue).collect(Collectors.joining(","));
                    if (!string7.isEmpty()) {
                        stringBuilder.append(',').append(string7);
                    }
                    String string8 = "";
                    String string9 = string2.replace("/dev/disk", "disk");
                    if (string9.startsWith("disk")) {
                        Object object;
                        CoreFoundation.CFDictionaryRef cFDictionaryRef;
                        DiskArbitration.DADiskRef dADiskRef = DiskArbitration.INSTANCE.DADiskCreateFromBSDName(CoreFoundation.INSTANCE.CFAllocatorGetDefault(), dASessionRef, string2);
                        if (dADiskRef != null) {
                            cFDictionaryRef = DiskArbitration.INSTANCE.DADiskCopyDescription(dADiskRef);
                            if (cFDictionaryRef != null) {
                                object = cFDictionaryRef.getValue(cFStringRef);
                                string6 = l000iLAnd.I1O1I1LaNd((Pointer)object);
                                cFDictionaryRef.release();
                            }
                            dADiskRef.release();
                        }
                        if ((cFDictionaryRef = IOKitUtil.getBSDNameMatchingDict(string9)) != null && (object = IOKitUtil.getMatchingServices(cFDictionaryRef)) != null) {
                            IOKit.IORegistryEntry iORegistryEntry = ((IOKit.IOIterator)object).next();
                            if (iORegistryEntry != null && iORegistryEntry.conformsTo("IOMedia")) {
                                string8 = iORegistryEntry.getStringProperty("UUID");
                                if (string8 != null) {
                                    string8 = string8.toLowerCase();
                                }
                                iORegistryEntry.release();
                            }
                            ((IOKit.IOObject)object).release();
                        }
                    }
                    arrayList.add(new ii0l1lAnd(string6, string2, string6, string3, stringBuilder.toString(), string8 == null ? "" : string8, "", string5, string4, file.getFreeSpace(), file.getUsableSpace(), file.getTotalSpace(), arrstatfs[i2].f_ffree, arrstatfs[i2].f_files));
                }
                cFStringRef.release();
                dASessionRef.release();
            }
        }
        return arrayList;
    }

    @Override
    public long OOOIilanD() {
        return liiIILaND.I1O1I1LaNd("kern.num_files", 0);
    }

    @Override
    public long lI00OlAND() {
        return liiIILaND.I1O1I1LaNd("kern.maxfiles", 0);
    }

    static {
        liO1Oli1lanD.put(2, "synchronous");
        liO1Oli1lanD.put(4, "noexec");
        liO1Oli1lanD.put(8, "nosuid");
        liO1Oli1lanD.put(16, "nodev");
        liO1Oli1lanD.put(32, "union");
        liO1Oli1lanD.put(64, "asynchronous");
        liO1Oli1lanD.put(128, "content-protection");
        liO1Oli1lanD.put(256, "exported");
        liO1Oli1lanD.put(1024, "quarantined");
        liO1Oli1lanD.put(4096, "local");
        liO1Oli1lanD.put(8192, "quotas");
        liO1Oli1lanD.put(16384, "rootfs");
        liO1Oli1lanD.put(32768, "volfs");
        liO1Oli1lanD.put(0x100000, "nobrowse");
        liO1Oli1lanD.put(0x200000, "noowners");
        liO1Oli1lanD.put(0x400000, "automounted");
        liO1Oli1lanD.put(0x800000, "journaled");
        liO1Oli1lanD.put(0x1000000, "nouserxattr");
        liO1Oli1lanD.put(0x2000000, "defwrite");
        liO1Oli1lanD.put(0x4000000, "multilabel");
        liO1Oli1lanD.put(0x10000000, "noatime");
    }
}

