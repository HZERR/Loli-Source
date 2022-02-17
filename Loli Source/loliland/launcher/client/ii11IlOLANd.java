/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.IOKitUtil;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.IOll1OIlaND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ii11IlOLANd
extends IOll1OIlaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(ii11IlOLANd.class);

    ii11IlOLANd(byte[] arrby) {
        super(arrby);
        I1O1I1LaNd.debug("Initialized MacDisplay");
    }

    public static List OOOIilanD() {
        ArrayList<ii11IlOLANd> arrayList = new ArrayList<ii11IlOLANd>();
        IOKit.IOIterator iOIterator = IOKitUtil.getMatchingServices("IODisplayConnect");
        if (iOIterator != null) {
            CoreFoundation.CFStringRef cFStringRef = CoreFoundation.CFStringRef.createCFString("IODisplayEDID");
            IOKit.IORegistryEntry iORegistryEntry = iOIterator.next();
            while (iORegistryEntry != null) {
                IOKit.IORegistryEntry iORegistryEntry2 = iORegistryEntry.getChildEntry("IOService");
                if (iORegistryEntry2 != null) {
                    CoreFoundation.CFTypeRef cFTypeRef = iORegistryEntry2.createCFProperty(cFStringRef);
                    if (cFTypeRef != null) {
                        CoreFoundation.CFDataRef cFDataRef = new CoreFoundation.CFDataRef(cFTypeRef.getPointer());
                        int n2 = cFDataRef.getLength();
                        Pointer pointer = cFDataRef.getBytePtr();
                        arrayList.add(new ii11IlOLANd(pointer.getByteArray(0L, n2)));
                        cFDataRef.release();
                    }
                    iORegistryEntry2.release();
                }
                iORegistryEntry.release();
                iORegistryEntry = iOIterator.next();
            }
            iOIterator.release();
            cFStringRef.release();
        }
        return arrayList;
    }
}

