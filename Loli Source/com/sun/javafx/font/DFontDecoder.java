/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.font.FontFileWriter;
import java.io.IOException;
import java.security.AccessController;

class DFontDecoder
extends FontFileWriter {
    private static native long createCTFont(String var0);

    private static native void releaseCTFont(long var0);

    private static native int getCTFontFormat(long var0);

    private static native int[] getCTFontTags(long var0);

    private static native byte[] getCTFontTable(long var0, int var2);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void decode(String string) throws IOException {
        if (string == null) {
            throw new IOException("Invalid font name");
        }
        long l2 = 0L;
        try {
            int n2;
            int n3;
            int n4;
            l2 = DFontDecoder.createCTFont(string);
            if (l2 == 0L) {
                throw new IOException("Failure creating CTFont");
            }
            int n5 = DFontDecoder.getCTFontFormat(l2);
            if (n5 != 1953658213 && n5 != 65536 && n5 != 0x4F54544F) {
                throw new IOException("Unsupported Dfont");
            }
            int[] arrn = DFontDecoder.getCTFontTags(l2);
            int n6 = arrn.length;
            int n7 = 12 + 16 * n6;
            byte[][] arrarrby = new byte[n6][];
            for (n4 = 0; n4 < arrn.length; ++n4) {
                n3 = arrn[n4];
                arrarrby[n4] = DFontDecoder.getCTFontTable(l2, n3);
                n2 = arrarrby[n4].length;
                n7 += n2 + 3 & 0xFFFFFFFC;
            }
            DFontDecoder.releaseCTFont(l2);
            l2 = 0L;
            this.setLength(n7);
            this.writeHeader(n5, (short)n6);
            n4 = 12 + 16 * n6;
            for (n3 = 0; n3 < n6; ++n3) {
                n2 = arrn[n3];
                byte[] arrby = arrarrby[n3];
                this.writeDirectoryEntry(n3, n2, 0, n4, arrby.length);
                this.seek(n4);
                this.writeBytes(arrby);
                n4 += arrby.length + 3 & 0xFFFFFFFC;
            }
        }
        finally {
            if (l2 != 0L) {
                DFontDecoder.releaseCTFont(l2);
            }
        }
    }

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            return null;
        });
    }
}

