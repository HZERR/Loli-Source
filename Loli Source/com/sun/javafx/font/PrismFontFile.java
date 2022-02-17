/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontConstants;
import com.sun.javafx.font.FontFileReader;
import com.sun.javafx.font.FontFileWriter;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.OpenTypeGlyphMapper;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.PrismMetrics;
import com.sun.javafx.font.WoffDecoder;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PrismFontFile
implements FontResource,
FontConstants {
    private int fontInstallationType = -1;
    String familyName;
    String fullName;
    String psName;
    String localeFamilyName;
    String localeFullName;
    String styleName;
    String localeStyleName;
    String filename;
    int filesize;
    FontFileReader filereader;
    int numGlyphs = -1;
    short indexToLocFormat;
    int fontIndex;
    boolean isCFF;
    boolean isEmbedded = false;
    boolean isCopy = false;
    boolean isTracked = false;
    boolean isDecoded = false;
    boolean isRegistered = true;
    Map<FontStrikeDesc, WeakReference<PrismFontStrike>> strikeMap = new ConcurrentHashMap<FontStrikeDesc, WeakReference<PrismFontStrike>>();
    HashMap<Integer, int[]> bbCache = null;
    static final int[] EMPTY_BOUNDS = new int[4];
    private Object peer;
    int directoryCount = 1;
    int numTables;
    DirectoryEntry[] tableDirectory;
    private static final int fsSelectionItalicBit = 1;
    private static final int fsSelectionBoldBit = 32;
    private static final int MACSTYLE_BOLD_BIT = 1;
    private static final int MACSTYLE_ITALIC_BIT = 2;
    private boolean isBold;
    private boolean isItalic;
    private float upem;
    private float ascent;
    private float descent;
    private float linegap;
    private int numHMetrics;
    public static final int MAC_PLATFORM_ID = 1;
    public static final int MACROMAN_SPECIFIC_ID = 0;
    public static final int MACROMAN_ENGLISH_LANG = 0;
    public static final int MS_PLATFORM_ID = 3;
    public static final short MS_ENGLISH_LOCALE_ID = 1033;
    public static final int FAMILY_NAME_ID = 1;
    public static final int STYLE_NAME_ID = 2;
    public static final int FULL_NAME_ID = 4;
    public static final int PS_NAME_ID = 6;
    private static Map<String, Short> lcidMap;
    static short nameLocaleID;
    private OpenTypeGlyphMapper mapper = null;
    char[] advanceWidths = null;
    private float[] styleMetrics;

    protected PrismFontFile(String string, String string2, int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        this.filename = string2;
        this.isRegistered = bl;
        this.isEmbedded = bl2;
        this.isCopy = bl3;
        this.isTracked = bl4;
        this.init(string, n2);
    }

    WeakReference<PrismFontFile> createFileDisposer(PrismFontFactory prismFontFactory) {
        FileDisposer fileDisposer = new FileDisposer(this.filename, this.isTracked);
        WeakReference weakReference = Disposer.addRecord(this, fileDisposer);
        fileDisposer.setFactory(prismFontFactory, weakReference);
        return weakReference;
    }

    void setIsDecoded(boolean bl) {
        this.isDecoded = bl;
    }

    protected synchronized void disposeOnShutdown() {
        if (this.isCopy || this.isDecoded) {
            AccessController.doPrivileged(() -> {
                try {
                    boolean bl = new File(this.filename).delete();
                    if (!bl && PrismFontFactory.debugFonts) {
                        System.err.println("Temp file not deleted : " + this.filename);
                    }
                    this.isDecoded = false;
                    this.isCopy = false;
                }
                catch (Exception exception) {
                    // empty catch block
                }
                return null;
            });
            if (PrismFontFactory.debugFonts) {
                System.err.println("Temp file deleted: " + this.filename);
            }
        }
    }

    @Override
    public int getDefaultAAMode() {
        return 0;
    }

    public boolean isInstalledFont() {
        if (this.fontInstallationType == -1) {
            PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
            this.fontInstallationType = prismFontFactory.isInstalledFont(this.filename) ? 1 : 0;
        }
        return this.fontInstallationType > 0;
    }

    @Override
    public String getFileName() {
        return this.filename;
    }

    protected int getFileSize() {
        return this.filesize;
    }

    protected int getFontIndex() {
        return this.fontIndex;
    }

    @Override
    public String getFullName() {
        return this.fullName;
    }

    @Override
    public String getPSName() {
        if (this.psName == null) {
            this.psName = this.fullName;
        }
        return this.psName;
    }

    @Override
    public String getFamilyName() {
        return this.familyName;
    }

    @Override
    public String getStyleName() {
        return this.styleName;
    }

    @Override
    public String getLocaleFullName() {
        return this.localeFullName;
    }

    @Override
    public String getLocaleFamilyName() {
        return this.localeFamilyName;
    }

    @Override
    public String getLocaleStyleName() {
        return this.localeStyleName;
    }

    @Override
    public int getFeatures() {
        return -1;
    }

    public Map getStrikeMap() {
        return this.strikeMap;
    }

    protected abstract PrismFontStrike createStrike(float var1, BaseTransform var2, int var3, FontStrikeDesc var4);

    @Override
    public FontStrike getStrike(float f2, BaseTransform baseTransform, int n2) {
        FontStrikeDesc fontStrikeDesc = new FontStrikeDesc(f2, baseTransform, n2);
        WeakReference<PrismFontStrike> weakReference = this.strikeMap.get(fontStrikeDesc);
        PrismFontStrike prismFontStrike = null;
        if (weakReference != null) {
            prismFontStrike = (PrismFontStrike)weakReference.get();
        }
        if (prismFontStrike == null) {
            prismFontStrike = this.createStrike(f2, baseTransform, n2, fontStrikeDesc);
            DisposerRecord disposerRecord = prismFontStrike.getDisposer();
            weakReference = disposerRecord != null ? Disposer.addRecord(prismFontStrike, disposerRecord) : new WeakReference<PrismFontStrike>(prismFontStrike);
            this.strikeMap.put(fontStrikeDesc, weakReference);
        }
        return prismFontStrike;
    }

    protected abstract int[] createGlyphBoundingBox(int var1);

    @Override
    public float[] getGlyphBoundingBox(int n2, float f2, float[] arrf) {
        int[] arrn;
        if (arrf == null || arrf.length < 4) {
            arrf = new float[4];
        }
        if (n2 >= this.getNumGlyphs()) {
            arrf[3] = 0.0f;
            arrf[2] = 0.0f;
            arrf[1] = 0.0f;
            arrf[0] = 0.0f;
            return arrf;
        }
        if (this.bbCache == null) {
            this.bbCache = new HashMap();
        }
        if ((arrn = this.bbCache.get(n2)) == null) {
            arrn = this.createGlyphBoundingBox(n2);
            if (arrn == null) {
                arrn = EMPTY_BOUNDS;
            }
            this.bbCache.put(n2, arrn);
        }
        float f3 = f2 / (float)this.getUnitsPerEm();
        arrf[0] = (float)arrn[0] * f3;
        arrf[1] = (float)arrn[1] * f3;
        arrf[2] = (float)arrn[2] * f3;
        arrf[3] = (float)arrn[3] * f3;
        return arrf;
    }

    int getNumGlyphs() {
        if (this.numGlyphs == -1) {
            FontFileReader.Buffer buffer = this.readTable(1835104368);
            this.numGlyphs = buffer.getChar(4);
        }
        return this.numGlyphs;
    }

    protected boolean isCFF() {
        return this.isCFF;
    }

    @Override
    public Object getPeer() {
        return this.peer;
    }

    @Override
    public void setPeer(Object object) {
        this.peer = object;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    synchronized FontFileReader.Buffer readTable(int n2) {
        FontFileReader.Buffer buffer = null;
        boolean bl = false;
        try {
            bl = this.filereader.openFile();
            DirectoryEntry directoryEntry = this.getDirectoryEntry(n2);
            if (directoryEntry != null) {
                buffer = this.filereader.readBlock(directoryEntry.offset, directoryEntry.length);
            }
        }
        catch (Exception exception) {
            if (PrismFontFactory.debugFonts) {
                exception.printStackTrace();
            }
        }
        finally {
            if (bl) {
                try {
                    this.filereader.closeFile();
                }
                catch (Exception exception) {}
            }
        }
        return buffer;
    }

    public int getFontCount() {
        return this.directoryCount;
    }

    DirectoryEntry getDirectoryEntry(int n2) {
        for (int i2 = 0; i2 < this.numTables; ++i2) {
            if (this.tableDirectory[i2].tag != n2) continue;
            return this.tableDirectory[i2];
        }
        return null;
    }

    private void init(String string, int n2) throws Exception {
        this.filereader = new FontFileReader(this.filename);
        FontFileWriter fontFileWriter = null;
        try {
            if (!this.filereader.openFile()) {
                throw new FileNotFoundException("Unable to create FontResource for file " + this.filename);
            }
            FontFileReader.Buffer buffer = this.filereader.readBlock(0, 12);
            int n3 = buffer.getInt();
            if (n3 == 2001684038) {
                fontFileWriter = new WoffDecoder();
                File file = fontFileWriter.openFile();
                ((WoffDecoder)fontFileWriter).decode(this.filereader);
                fontFileWriter.closeFile();
                this.filereader.closeFile();
                this.filereader = new FontFileReader(file.getPath());
                if (!this.filereader.openFile()) {
                    throw new FileNotFoundException("Unable to create FontResource for file " + this.filename);
                }
                buffer = this.filereader.readBlock(0, 12);
                n3 = buffer.getInt();
            }
            this.filesize = (int)this.filereader.getLength();
            int n4 = 0;
            if (n3 == 1953784678) {
                buffer.getInt();
                this.directoryCount = buffer.getInt();
                if (n2 >= this.directoryCount) {
                    throw new Exception("Bad collection index");
                }
                this.fontIndex = n2;
                buffer = this.filereader.readBlock(12 + 4 * n2, 4);
                n4 = buffer.getInt();
                buffer = this.filereader.readBlock(n4, 4);
                n3 = buffer.getInt();
            }
            switch (n3) {
                case 65536: 
                case 1953658213: {
                    break;
                }
                case 0x4F54544F: {
                    this.isCFF = true;
                    break;
                }
                default: {
                    throw new Exception("Unsupported sfnt " + this.filename);
                }
            }
            buffer = this.filereader.readBlock(n4 + 4, 2);
            this.numTables = buffer.getShort();
            int n5 = n4 + 12;
            FontFileReader.Buffer buffer2 = this.filereader.readBlock(n5, this.numTables * 16);
            this.tableDirectory = new DirectoryEntry[this.numTables];
            for (int i2 = 0; i2 < this.numTables; ++i2) {
                DirectoryEntry directoryEntry;
                this.tableDirectory[i2] = directoryEntry = new DirectoryEntry();
                directoryEntry.tag = buffer2.getInt();
                buffer2.skip(4);
                directoryEntry.offset = buffer2.getInt();
                directoryEntry.length = buffer2.getInt();
                if (directoryEntry.offset + directoryEntry.length <= this.filesize) continue;
                throw new Exception("bad table, tag=" + directoryEntry.tag);
            }
            DirectoryEntry directoryEntry = this.getDirectoryEntry(1751474532);
            FontFileReader.Buffer buffer3 = this.filereader.readBlock(directoryEntry.offset, directoryEntry.length);
            this.upem = buffer3.getShort(18) & 0xFFFF;
            if (!(16.0f <= this.upem) || !(this.upem <= 16384.0f)) {
                this.upem = 2048.0f;
            }
            this.indexToLocFormat = buffer3.getShort(50);
            if (this.indexToLocFormat < 0 || this.indexToLocFormat > 1) {
                throw new Exception("Bad indexToLocFormat");
            }
            FontFileReader.Buffer buffer4 = this.readTable(1751672161);
            if (buffer4 == null) {
                this.numHMetrics = -1;
            } else {
                this.ascent = -((float)buffer4.getShort(4));
                this.descent = -((float)buffer4.getShort(6));
                this.linegap = buffer4.getShort(8);
                this.numHMetrics = buffer4.getChar(34) & 0xFFFF;
            }
            this.getNumGlyphs();
            this.setStyle();
            this.checkCMAP();
            this.initNames();
            if (this.familyName == null || this.fullName == null) {
                String string2;
                String string3 = string2 = string != null ? string : "";
                if (this.fullName == null) {
                    String string4 = this.fullName = this.familyName != null ? this.familyName : string2;
                }
                if (this.familyName == null) {
                    this.familyName = this.fullName != null ? this.fullName : string2;
                }
                throw new Exception("Font name not found.");
            }
            if (fontFileWriter != null) {
                this.isDecoded = true;
                this.filename = this.filereader.getFilename();
                PrismFontFactory.getFontFactory().addDecodedFont(this);
            }
        }
        catch (Exception exception) {
            if (fontFileWriter != null) {
                fontFileWriter.deleteFile();
            }
            throw exception;
        }
        finally {
            this.filereader.closeFile();
        }
    }

    private void setStyle() {
        DirectoryEntry directoryEntry = this.getDirectoryEntry(1330851634);
        if (directoryEntry != null) {
            FontFileReader.Buffer buffer = this.filereader.readBlock(directoryEntry.offset, directoryEntry.length);
            int n2 = buffer.getChar(62) & 0xFFFF;
            this.isItalic = (n2 & 1) != 0;
            this.isBold = (n2 & 0x20) != 0;
        } else {
            DirectoryEntry directoryEntry2 = this.getDirectoryEntry(1751474532);
            FontFileReader.Buffer buffer = this.filereader.readBlock(directoryEntry2.offset, directoryEntry2.length);
            short s2 = buffer.getShort(44);
            this.isItalic = (s2 & 2) != 0;
            this.isBold = (s2 & 1) != 0;
        }
    }

    @Override
    public boolean isBold() {
        return this.isBold;
    }

    @Override
    public boolean isItalic() {
        return this.isItalic;
    }

    public boolean isDecoded() {
        return this.isDecoded;
    }

    public boolean isRegistered() {
        return this.isRegistered;
    }

    @Override
    public boolean isEmbeddedFont() {
        return this.isEmbedded;
    }

    public int getUnitsPerEm() {
        return (int)this.upem;
    }

    public short getIndexToLocFormat() {
        return this.indexToLocFormat;
    }

    public int getNumHMetrics() {
        return this.numHMetrics;
    }

    void initNames() throws Exception {
        byte[] arrby = new byte[256];
        DirectoryEntry directoryEntry = this.getDirectoryEntry(1851878757);
        FontFileReader.Buffer buffer = this.filereader.readBlock(directoryEntry.offset, directoryEntry.length);
        buffer.skip(2);
        int n2 = buffer.getShort();
        int n3 = buffer.getShort() & 0xFFFF;
        for (int i2 = 0; i2 < n2; ++i2) {
            short s2 = buffer.getShort();
            if (s2 != 3 && s2 != 1) {
                buffer.skip(10);
                continue;
            }
            short s3 = buffer.getShort();
            if (s2 == 3 && s3 > 1 || s2 == 1 && s3 != 0) {
                buffer.skip(8);
                continue;
            }
            short s4 = buffer.getShort();
            if (s2 == 1 && s4 != 0) {
                buffer.skip(6);
                continue;
            }
            short s5 = buffer.getShort();
            int n4 = buffer.getShort() & 0xFFFF;
            int n5 = (buffer.getShort() & 0xFFFF) + n3;
            String string = null;
            switch (s5) {
                case 1: {
                    if (this.familyName != null && s4 != 1033 && s4 != nameLocaleID) break;
                    buffer.get(n5, arrby, 0, n4);
                    String string2 = s2 == 1 ? "US-ASCII" : "UTF-16BE";
                    string = new String(arrby, 0, n4, string2);
                    if (this.familyName == null || s4 == 1033) {
                        this.familyName = string;
                    }
                    if (s4 != nameLocaleID) break;
                    this.localeFamilyName = string;
                    break;
                }
                case 4: {
                    if (this.fullName != null && s4 != 1033 && s4 != nameLocaleID) break;
                    buffer.get(n5, arrby, 0, n4);
                    String string2 = s2 == 1 ? "US-ASCII" : "UTF-16BE";
                    string = new String(arrby, 0, n4, string2);
                    if (this.fullName == null || s4 == 1033) {
                        this.fullName = string;
                    }
                    if (s4 != nameLocaleID) break;
                    this.localeFullName = string;
                    break;
                }
                case 6: {
                    if (this.psName != null) break;
                    buffer.get(n5, arrby, 0, n4);
                    String string2 = s2 == 1 ? "US-ASCII" : "UTF-16BE";
                    this.psName = new String(arrby, 0, n4, string2);
                    break;
                }
                case 2: {
                    if (this.styleName != null && s4 != 1033 && s4 != nameLocaleID) break;
                    buffer.get(n5, arrby, 0, n4);
                    String string2 = s2 == 1 ? "US-ASCII" : "UTF-16BE";
                    string = new String(arrby, 0, n4, string2);
                    if (this.styleName == null || s4 == 1033) {
                        this.styleName = string;
                    }
                    if (s4 != nameLocaleID) break;
                    this.localeStyleName = string;
                    break;
                }
            }
            if (this.localeFamilyName == null) {
                this.localeFamilyName = this.familyName;
            }
            if (this.localeFullName == null) {
                this.localeFullName = this.fullName;
            }
            if (this.localeStyleName != null) continue;
            this.localeStyleName = this.styleName;
        }
    }

    private void checkCMAP() throws Exception {
        DirectoryEntry directoryEntry = this.getDirectoryEntry(1668112752);
        if (directoryEntry != null) {
            if (directoryEntry.length < 4) {
                throw new Exception("Invalid cmap table length");
            }
            FontFileReader.Buffer buffer = this.filereader.readBlock(directoryEntry.offset, 4);
            short s2 = buffer.getShort();
            int n2 = buffer.getShort();
            int n3 = n2 * 8;
            if (n2 <= 0 || directoryEntry.length < n3 + 4) {
                throw new Exception("Invalid cmap subtables count");
            }
            FontFileReader.Buffer buffer2 = this.filereader.readBlock(directoryEntry.offset + 4, n3);
            for (int i2 = 0; i2 < n2; ++i2) {
                short s3 = buffer2.getShort();
                short s4 = buffer2.getShort();
                int n4 = buffer2.getInt();
                if (n4 >= 0 && n4 < directoryEntry.length) continue;
                throw new Exception("Invalid cmap subtable offset");
            }
        }
    }

    private static void addLCIDMapEntry(Map<String, Short> map, String string, short s2) {
        map.put(string, s2);
    }

    private static synchronized void createLCIDMap() {
        if (lcidMap != null) {
            return;
        }
        HashMap<String, Short> hashMap = new HashMap<String, Short>(200);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar", (short)1025);
        PrismFontFile.addLCIDMapEntry(hashMap, "bg", (short)1026);
        PrismFontFile.addLCIDMapEntry(hashMap, "ca", (short)1027);
        PrismFontFile.addLCIDMapEntry(hashMap, "zh", (short)1028);
        PrismFontFile.addLCIDMapEntry(hashMap, "cs", (short)1029);
        PrismFontFile.addLCIDMapEntry(hashMap, "da", (short)1030);
        PrismFontFile.addLCIDMapEntry(hashMap, "de", (short)1031);
        PrismFontFile.addLCIDMapEntry(hashMap, "el", (short)1032);
        PrismFontFile.addLCIDMapEntry(hashMap, "es", (short)1034);
        PrismFontFile.addLCIDMapEntry(hashMap, "fi", (short)1035);
        PrismFontFile.addLCIDMapEntry(hashMap, "fr", (short)1036);
        PrismFontFile.addLCIDMapEntry(hashMap, "iw", (short)1037);
        PrismFontFile.addLCIDMapEntry(hashMap, "hu", (short)1038);
        PrismFontFile.addLCIDMapEntry(hashMap, "is", (short)1039);
        PrismFontFile.addLCIDMapEntry(hashMap, "it", (short)1040);
        PrismFontFile.addLCIDMapEntry(hashMap, "ja", (short)1041);
        PrismFontFile.addLCIDMapEntry(hashMap, "ko", (short)1042);
        PrismFontFile.addLCIDMapEntry(hashMap, "nl", (short)1043);
        PrismFontFile.addLCIDMapEntry(hashMap, "no", (short)1044);
        PrismFontFile.addLCIDMapEntry(hashMap, "pl", (short)1045);
        PrismFontFile.addLCIDMapEntry(hashMap, "pt", (short)1046);
        PrismFontFile.addLCIDMapEntry(hashMap, "rm", (short)1047);
        PrismFontFile.addLCIDMapEntry(hashMap, "ro", (short)1048);
        PrismFontFile.addLCIDMapEntry(hashMap, "ru", (short)1049);
        PrismFontFile.addLCIDMapEntry(hashMap, "hr", (short)1050);
        PrismFontFile.addLCIDMapEntry(hashMap, "sk", (short)1051);
        PrismFontFile.addLCIDMapEntry(hashMap, "sq", (short)1052);
        PrismFontFile.addLCIDMapEntry(hashMap, "sv", (short)1053);
        PrismFontFile.addLCIDMapEntry(hashMap, "th", (short)1054);
        PrismFontFile.addLCIDMapEntry(hashMap, "tr", (short)1055);
        PrismFontFile.addLCIDMapEntry(hashMap, "ur", (short)1056);
        PrismFontFile.addLCIDMapEntry(hashMap, "in", (short)1057);
        PrismFontFile.addLCIDMapEntry(hashMap, "uk", (short)1058);
        PrismFontFile.addLCIDMapEntry(hashMap, "be", (short)1059);
        PrismFontFile.addLCIDMapEntry(hashMap, "sl", (short)1060);
        PrismFontFile.addLCIDMapEntry(hashMap, "et", (short)1061);
        PrismFontFile.addLCIDMapEntry(hashMap, "lv", (short)1062);
        PrismFontFile.addLCIDMapEntry(hashMap, "lt", (short)1063);
        PrismFontFile.addLCIDMapEntry(hashMap, "fa", (short)1065);
        PrismFontFile.addLCIDMapEntry(hashMap, "vi", (short)1066);
        PrismFontFile.addLCIDMapEntry(hashMap, "hy", (short)1067);
        PrismFontFile.addLCIDMapEntry(hashMap, "eu", (short)1069);
        PrismFontFile.addLCIDMapEntry(hashMap, "mk", (short)1071);
        PrismFontFile.addLCIDMapEntry(hashMap, "tn", (short)1074);
        PrismFontFile.addLCIDMapEntry(hashMap, "xh", (short)1076);
        PrismFontFile.addLCIDMapEntry(hashMap, "zu", (short)1077);
        PrismFontFile.addLCIDMapEntry(hashMap, "af", (short)1078);
        PrismFontFile.addLCIDMapEntry(hashMap, "ka", (short)1079);
        PrismFontFile.addLCIDMapEntry(hashMap, "fo", (short)1080);
        PrismFontFile.addLCIDMapEntry(hashMap, "hi", (short)1081);
        PrismFontFile.addLCIDMapEntry(hashMap, "mt", (short)1082);
        PrismFontFile.addLCIDMapEntry(hashMap, "se", (short)1083);
        PrismFontFile.addLCIDMapEntry(hashMap, "gd", (short)1084);
        PrismFontFile.addLCIDMapEntry(hashMap, "ms", (short)1086);
        PrismFontFile.addLCIDMapEntry(hashMap, "kk", (short)1087);
        PrismFontFile.addLCIDMapEntry(hashMap, "ky", (short)1088);
        PrismFontFile.addLCIDMapEntry(hashMap, "sw", (short)1089);
        PrismFontFile.addLCIDMapEntry(hashMap, "tt", (short)1092);
        PrismFontFile.addLCIDMapEntry(hashMap, "bn", (short)1093);
        PrismFontFile.addLCIDMapEntry(hashMap, "pa", (short)1094);
        PrismFontFile.addLCIDMapEntry(hashMap, "gu", (short)1095);
        PrismFontFile.addLCIDMapEntry(hashMap, "ta", (short)1097);
        PrismFontFile.addLCIDMapEntry(hashMap, "te", (short)1098);
        PrismFontFile.addLCIDMapEntry(hashMap, "kn", (short)1099);
        PrismFontFile.addLCIDMapEntry(hashMap, "ml", (short)1100);
        PrismFontFile.addLCIDMapEntry(hashMap, "mr", (short)1102);
        PrismFontFile.addLCIDMapEntry(hashMap, "sa", (short)1103);
        PrismFontFile.addLCIDMapEntry(hashMap, "mn", (short)1104);
        PrismFontFile.addLCIDMapEntry(hashMap, "cy", (short)1106);
        PrismFontFile.addLCIDMapEntry(hashMap, "gl", (short)1110);
        PrismFontFile.addLCIDMapEntry(hashMap, "dv", (short)1125);
        PrismFontFile.addLCIDMapEntry(hashMap, "qu", (short)1131);
        PrismFontFile.addLCIDMapEntry(hashMap, "mi", (short)1153);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_IQ", (short)2049);
        PrismFontFile.addLCIDMapEntry(hashMap, "zh_CN", (short)2052);
        PrismFontFile.addLCIDMapEntry(hashMap, "de_CH", (short)2055);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_GB", (short)2057);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_MX", (short)2058);
        PrismFontFile.addLCIDMapEntry(hashMap, "fr_BE", (short)2060);
        PrismFontFile.addLCIDMapEntry(hashMap, "it_CH", (short)2064);
        PrismFontFile.addLCIDMapEntry(hashMap, "nl_BE", (short)2067);
        PrismFontFile.addLCIDMapEntry(hashMap, "no_NO_NY", (short)2068);
        PrismFontFile.addLCIDMapEntry(hashMap, "pt_PT", (short)2070);
        PrismFontFile.addLCIDMapEntry(hashMap, "ro_MD", (short)2072);
        PrismFontFile.addLCIDMapEntry(hashMap, "ru_MD", (short)2073);
        PrismFontFile.addLCIDMapEntry(hashMap, "sr_CS", (short)2074);
        PrismFontFile.addLCIDMapEntry(hashMap, "sv_FI", (short)2077);
        PrismFontFile.addLCIDMapEntry(hashMap, "az_AZ", (short)2092);
        PrismFontFile.addLCIDMapEntry(hashMap, "se_SE", (short)2107);
        PrismFontFile.addLCIDMapEntry(hashMap, "ga_IE", (short)2108);
        PrismFontFile.addLCIDMapEntry(hashMap, "ms_BN", (short)2110);
        PrismFontFile.addLCIDMapEntry(hashMap, "uz_UZ", (short)2115);
        PrismFontFile.addLCIDMapEntry(hashMap, "qu_EC", (short)2155);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_EG", (short)3073);
        PrismFontFile.addLCIDMapEntry(hashMap, "zh_HK", (short)3076);
        PrismFontFile.addLCIDMapEntry(hashMap, "de_AT", (short)3079);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_AU", (short)3081);
        PrismFontFile.addLCIDMapEntry(hashMap, "fr_CA", (short)3084);
        PrismFontFile.addLCIDMapEntry(hashMap, "sr_CS", (short)3098);
        PrismFontFile.addLCIDMapEntry(hashMap, "se_FI", (short)3131);
        PrismFontFile.addLCIDMapEntry(hashMap, "qu_PE", (short)3179);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_LY", (short)4097);
        PrismFontFile.addLCIDMapEntry(hashMap, "zh_SG", (short)4100);
        PrismFontFile.addLCIDMapEntry(hashMap, "de_LU", (short)4103);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_CA", (short)4105);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_GT", (short)4106);
        PrismFontFile.addLCIDMapEntry(hashMap, "fr_CH", (short)4108);
        PrismFontFile.addLCIDMapEntry(hashMap, "hr_BA", (short)4122);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_DZ", (short)5121);
        PrismFontFile.addLCIDMapEntry(hashMap, "zh_MO", (short)5124);
        PrismFontFile.addLCIDMapEntry(hashMap, "de_LI", (short)5127);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_NZ", (short)5129);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_CR", (short)5130);
        PrismFontFile.addLCIDMapEntry(hashMap, "fr_LU", (short)5132);
        PrismFontFile.addLCIDMapEntry(hashMap, "bs_BA", (short)5146);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_MA", (short)6145);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_IE", (short)6153);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_PA", (short)6154);
        PrismFontFile.addLCIDMapEntry(hashMap, "fr_MC", (short)6156);
        PrismFontFile.addLCIDMapEntry(hashMap, "sr_BA", (short)6170);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_TN", (short)7169);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_ZA", (short)7177);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_DO", (short)7178);
        PrismFontFile.addLCIDMapEntry(hashMap, "sr_BA", (short)7194);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_OM", (short)8193);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_JM", (short)8201);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_VE", (short)8202);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_YE", (short)9217);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_CO", (short)9226);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_SY", (short)10241);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_BZ", (short)10249);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_PE", (short)10250);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_JO", (short)11265);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_TT", (short)11273);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_AR", (short)11274);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_LB", (short)12289);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_ZW", (short)12297);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_EC", (short)12298);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_KW", (short)13313);
        PrismFontFile.addLCIDMapEntry(hashMap, "en_PH", (short)13321);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_CL", (short)13322);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_AE", (short)14337);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_UY", (short)14346);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_BH", (short)15361);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_PY", (short)15370);
        PrismFontFile.addLCIDMapEntry(hashMap, "ar_QA", (short)16385);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_BO", (short)16394);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_SV", (short)17418);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_HN", (short)18442);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_NI", (short)19466);
        PrismFontFile.addLCIDMapEntry(hashMap, "es_PR", (short)20490);
        lcidMap = hashMap;
    }

    private static short getLCIDFromLocale(Locale locale) {
        if (locale.equals(Locale.US) || locale.getLanguage().equals("en")) {
            return 1033;
        }
        if (lcidMap == null) {
            PrismFontFile.createLCIDMap();
        }
        String string = locale.toString();
        while (!string.isEmpty()) {
            Short s2 = lcidMap.get(string);
            if (s2 != null) {
                return s2;
            }
            int n2 = string.lastIndexOf(95);
            if (n2 < 1) {
                return 1033;
            }
            string = string.substring(0, n2);
        }
        return 1033;
    }

    private static short getSystemLCID() {
        if (PrismFontFactory.isWindows) {
            return PrismFontFactory.getSystemLCID();
        }
        return PrismFontFile.getLCIDFromLocale(Locale.getDefault());
    }

    @Override
    public CharToGlyphMapper getGlyphMapper() {
        if (this.mapper == null) {
            this.mapper = new OpenTypeGlyphMapper(this);
        }
        return this.mapper;
    }

    @Override
    public FontStrike getStrike(float f2, BaseTransform baseTransform) {
        return this.getStrike(f2, baseTransform, this.getDefaultAAMode());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public float getAdvance(int n2, float f2) {
        if (n2 == 65535) {
            return 0.0f;
        }
        if (this.advanceWidths == null && this.numHMetrics > 0) {
            PrismFontFile prismFontFile = this;
            synchronized (prismFontFile) {
                FontFileReader.Buffer buffer = this.readTable(1752003704);
                if (buffer == null) {
                    this.numHMetrics = -1;
                    return 0.0f;
                }
                char[] arrc = new char[this.numHMetrics];
                for (int i2 = 0; i2 < this.numHMetrics; ++i2) {
                    arrc[i2] = buffer.getChar(i2 * 4);
                }
                this.advanceWidths = arrc;
            }
        }
        if (this.numHMetrics > 0) {
            char c2 = n2 < this.numHMetrics ? this.advanceWidths[n2] : this.advanceWidths[this.numHMetrics - 1];
            return (float)(c2 & 0xFFFF) * f2 / this.upem;
        }
        return 0.0f;
    }

    public PrismMetrics getFontMetrics(float f2) {
        return new PrismMetrics(this.ascent * f2 / this.upem, this.descent * f2 / this.upem, this.linegap * f2 / this.upem, this, f2);
    }

    float[] getStyleMetrics(float f2) {
        float[] arrf;
        if (this.styleMetrics == null) {
            Object object;
            int n2;
            arrf = new float[9];
            FontFileReader.Buffer buffer = this.readTable(1330851634);
            int n3 = n2 = buffer != null ? buffer.capacity() : 0;
            if (n2 >= 30) {
                arrf[5] = (float)buffer.getShort(26) / this.upem;
                arrf[6] = (float)(-buffer.getShort(28)) / this.upem;
            } else {
                arrf[5] = 0.05f;
                arrf[6] = -0.4f;
            }
            if (n2 >= 74) {
                arrf[2] = (float)(-buffer.getShort(68)) / this.upem;
                arrf[3] = (float)(-buffer.getShort(70)) / this.upem;
                arrf[4] = (float)buffer.getShort(72) / this.upem;
            } else {
                arrf[2] = this.ascent / this.upem;
                arrf[3] = this.descent / this.upem;
                arrf[4] = this.linegap / this.upem;
            }
            if (n2 >= 90) {
                arrf[0] = (float)buffer.getShort(86) / this.upem;
                arrf[1] = buffer.getShort(88);
                arrf[1] = (double)(arrf[1] / this.ascent) < 0.5 ? 0.0f : arrf[1] / this.upem;
            }
            if (arrf[0] == 0.0f || arrf[1] == 0.0f) {
                RectBounds rectBounds;
                int n4;
                object = this.getStrike(f2, BaseTransform.IDENTITY_TRANSFORM);
                CharToGlyphMapper charToGlyphMapper = this.getGlyphMapper();
                int n5 = charToGlyphMapper.getMissingGlyphCode();
                if (arrf[0] == 0.0f) {
                    n4 = charToGlyphMapper.charToGlyph('x');
                    if (n4 != n5) {
                        rectBounds = object.getGlyph(n4).getBBox();
                        arrf[0] = rectBounds.getHeight() / f2;
                    } else {
                        arrf[0] = -this.ascent * 0.6f / this.upem;
                    }
                }
                if (arrf[1] == 0.0f) {
                    n4 = charToGlyphMapper.charToGlyph('H');
                    if (n4 != n5) {
                        rectBounds = object.getGlyph(n4).getBBox();
                        arrf[1] = rectBounds.getHeight() / f2;
                    } else {
                        arrf[1] = -this.ascent * 0.9f / this.upem;
                    }
                }
            }
            if ((object = this.readTable(1886352244)) == null || ((FontFileReader.Buffer)object).capacity() < 12) {
                arrf[8] = 0.1f;
                arrf[7] = 0.05f;
            } else {
                arrf[8] = (float)(-((FontFileReader.Buffer)object).getShort(8)) / this.upem;
                arrf[7] = (float)((FontFileReader.Buffer)object).getShort(10) / this.upem;
            }
            this.styleMetrics = arrf;
        }
        arrf = new float[9];
        for (int i2 = 0; i2 < 9; ++i2) {
            arrf[i2] = this.styleMetrics[i2] * f2;
        }
        return arrf;
    }

    byte[] getTableBytes(int n2) {
        FontFileReader.Buffer buffer = this.readTable(n2);
        byte[] arrby = null;
        if (buffer != null) {
            arrby = new byte[buffer.capacity()];
            buffer.get(0, arrby, 0, buffer.capacity());
        }
        return arrby;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof PrismFontFile)) {
            return false;
        }
        PrismFontFile prismFontFile = (PrismFontFile)object;
        return this.filename.equals(prismFontFile.filename) && this.fullName.equals(prismFontFile.fullName);
    }

    public int hashCode() {
        return this.filename.hashCode() + 71 * this.fullName.hashCode();
    }

    static {
        nameLocaleID = PrismFontFile.getSystemLCID();
    }

    static class DirectoryEntry {
        int tag;
        int offset;
        int length;

        DirectoryEntry() {
        }
    }

    static class FileDisposer
    implements DisposerRecord {
        String fileName;
        boolean isTracked;
        PrismFontFactory factory;
        WeakReference<PrismFontFile> refKey;

        public FileDisposer(String string, boolean bl) {
            this.fileName = string;
            this.isTracked = bl;
        }

        public void setFactory(PrismFontFactory prismFontFactory, WeakReference<PrismFontFile> weakReference) {
            this.factory = prismFontFactory;
            this.refKey = weakReference;
        }

        @Override
        public synchronized void dispose() {
            if (this.fileName != null) {
                AccessController.doPrivileged(() -> {
                    block5: {
                        try {
                            Object t2;
                            File file = new File(this.fileName);
                            int n2 = (int)file.length();
                            file.delete();
                            if (this.isTracked) {
                                FontFileWriter.FontTracker.getTracker().subBytes(n2);
                            }
                            if (this.factory != null && this.refKey != null && (t2 = this.refKey.get()) == null) {
                                this.factory.removeTmpFont(this.refKey);
                                this.factory = null;
                                this.refKey = null;
                            }
                            if (PrismFontFactory.debugFonts) {
                                System.err.println("FileDisposer=" + this.fileName);
                            }
                        }
                        catch (Exception exception) {
                            if (!PrismFontFactory.debugFonts) break block5;
                            exception.printStackTrace();
                        }
                    }
                    return null;
                });
                this.fileName = null;
            }
        }
    }
}

