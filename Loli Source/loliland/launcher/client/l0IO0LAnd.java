/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.i00ilaNd;
import loliland.launcher.client.iOiiIlILand;
import loliland.launcher.client.l000iLAnd;
import loliland.launcher.client.lIOI1Land;
import loliland.launcher.client.liI0O1laNd;

public final class l0IO0LAnd {
    private l0IO0LAnd() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List I1O1I1LaNd(boolean bl) {
        CoreFoundation.CFArrayRef cFArrayRef = iOiiIlILand.INSTANCE.CGWindowListCopyWindowInfo(bl ? 17 : 0, 0);
        int n2 = cFArrayRef.getCount();
        ArrayList<lIOI1Land> arrayList = new ArrayList<lIOI1Land>();
        CoreFoundation.CFStringRef cFStringRef = CoreFoundation.CFStringRef.createCFString("kCGWindowIsOnscreen");
        CoreFoundation.CFStringRef cFStringRef2 = CoreFoundation.CFStringRef.createCFString("kCGWindowNumber");
        CoreFoundation.CFStringRef cFStringRef3 = CoreFoundation.CFStringRef.createCFString("kCGWindowOwnerPID");
        CoreFoundation.CFStringRef cFStringRef4 = CoreFoundation.CFStringRef.createCFString("kCGWindowLayer");
        CoreFoundation.CFStringRef cFStringRef5 = CoreFoundation.CFStringRef.createCFString("kCGWindowBounds");
        CoreFoundation.CFStringRef cFStringRef6 = CoreFoundation.CFStringRef.createCFString("kCGWindowName");
        CoreFoundation.CFStringRef cFStringRef7 = CoreFoundation.CFStringRef.createCFString("kCGWindowOwnerName");
        try {
            for (int i2 = 0; i2 < n2; ++i2) {
                boolean bl2;
                Pointer pointer = cFArrayRef.getValueAtIndex(i2);
                CoreFoundation.CFDictionaryRef cFDictionaryRef = new CoreFoundation.CFDictionaryRef(pointer);
                boolean bl3 = bl2 = (pointer = cFDictionaryRef.getValue(cFStringRef)) == null || new CoreFoundation.CFBooleanRef(pointer).booleanValue();
                if (bl && !bl2) continue;
                pointer = cFDictionaryRef.getValue(cFStringRef2);
                long l2 = new CoreFoundation.CFNumberRef(pointer).longValue();
                pointer = cFDictionaryRef.getValue(cFStringRef3);
                long l3 = new CoreFoundation.CFNumberRef(pointer).longValue();
                pointer = cFDictionaryRef.getValue(cFStringRef4);
                int n3 = new CoreFoundation.CFNumberRef(pointer).intValue();
                pointer = cFDictionaryRef.getValue(cFStringRef5);
                liI0O1laNd liI0O1laNd2 = new liI0O1laNd();
                iOiiIlILand.INSTANCE.CGRectMakeWithDictionaryRepresentation(new CoreFoundation.CFDictionaryRef(pointer), liI0O1laNd2);
                Rectangle rectangle = new Rectangle(i00ilaNd.I1O1I1LaNd(liI0O1laNd2.origin.x), i00ilaNd.I1O1I1LaNd(liI0O1laNd2.origin.y), i00ilaNd.I1O1I1LaNd(liI0O1laNd2.size.width), i00ilaNd.I1O1I1LaNd(liI0O1laNd2.size.height));
                pointer = cFDictionaryRef.getValue(cFStringRef6);
                String string = l000iLAnd.I1O1I1LaNd(pointer, false);
                pointer = cFDictionaryRef.getValue(cFStringRef7);
                String string2 = l000iLAnd.I1O1I1LaNd(pointer, false);
                string = string.isEmpty() ? string2 : string + "(" + string2 + ")";
                arrayList.add(new lIOI1Land(l2, string, string2, rectangle, l3, n3, bl2));
            }
        }
        finally {
            cFStringRef.release();
            cFStringRef2.release();
            cFStringRef3.release();
            cFStringRef4.release();
            cFStringRef5.release();
            cFStringRef6.release();
            cFStringRef7.release();
            cFArrayRef.release();
        }
        return arrayList;
    }
}

