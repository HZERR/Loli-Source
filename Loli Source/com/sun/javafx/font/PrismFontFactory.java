/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.font.AndroidFontFinder;
import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.DFontDecoder;
import com.sun.javafx.font.FontConfigManager;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.FontFileWriter;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.LogicalFont;
import com.sun.javafx.font.MacFontFinder;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismCompositeFontResource;
import com.sun.javafx.font.PrismFont;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.WindowsFontMap;
import com.sun.javafx.text.GlyphLayout;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public abstract class PrismFontFactory
implements FontFactory {
    public static final boolean debugFonts;
    public static final boolean isWindows;
    public static final boolean isLinux;
    public static final boolean isMacOSX;
    public static final boolean isIOS;
    public static final boolean isAndroid;
    public static final boolean isEmbedded;
    public static final int cacheLayoutSize;
    static boolean useNativeRasterizer;
    private static int subPixelMode;
    public static final int SUB_PIXEL_ON = 1;
    public static final int SUB_PIXEL_Y = 2;
    public static final int SUB_PIXEL_NATIVE = 4;
    private static float fontSizeLimit;
    private static boolean lcdEnabled;
    private static float lcdContrast;
    private static String jreFontDir;
    private static final String jreDefaultFont = "Lucida Sans Regular";
    private static final String jreDefaultFontLC = "lucida sans regular";
    private static final String jreDefaultFontFile = "LucidaSansRegular.ttf";
    private static final String T2K_FACTORY = "com.sun.javafx.font.t2k.T2KFactory";
    private static final String CT_FACTORY = "com.sun.javafx.font.coretext.CTFactory";
    private static final String DW_FACTORY = "com.sun.javafx.font.directwrite.DWFactory";
    private static final String FT_FACTORY = "com.sun.javafx.font.freetype.FTFactory";
    HashMap<String, FontResource> fontResourceMap = new HashMap();
    HashMap<String, CompositeFontResource> compResourceMap = new HashMap();
    private static PrismFontFactory theFontFactory;
    private HashMap<String, PrismFontFile> fileNameToFontResourceMap = new HashMap();
    private ArrayList<WeakReference<PrismFontFile>> tmpFonts;
    private static final String[] STR_ARRAY;
    private volatile HashMap<String, String> fontToFileMap = null;
    private HashMap<String, String> fileToFontMap = null;
    private HashMap<String, String> fontToFamilyNameMap = null;
    private HashMap<String, ArrayList<String>> familyToFontListMap = null;
    private static String sysFontDir;
    private static String userFontDir;
    private static ArrayList<String> allFamilyNames;
    private static ArrayList<String> allFontNames;
    private static Thread fileCloser;
    private HashMap<String, PrismFontFile> embeddedFonts;
    private int numEmbeddedFonts = 0;
    private static float systemFontSize;
    private static String systemFontFamily;
    private static String monospaceFontFamily;

    private static String getNativeFactoryName() {
        if (isWindows) {
            return DW_FACTORY;
        }
        if (isMacOSX || isIOS) {
            return CT_FACTORY;
        }
        if (isLinux || isAndroid) {
            return FT_FACTORY;
        }
        return null;
    }

    public static float getFontSizeLimit() {
        return fontSizeLimit;
    }

    public static synchronized PrismFontFactory getFontFactory() {
        if (theFontFactory != null) {
            return theFontFactory;
        }
        String string = null;
        if (useNativeRasterizer) {
            string = PrismFontFactory.getNativeFactoryName();
        }
        if (string == null) {
            useNativeRasterizer = false;
            string = T2K_FACTORY;
        }
        if (debugFonts) {
            System.err.println("Loading FontFactory " + string);
            if (subPixelMode != 0) {
                String string2 = "Subpixel: enabled";
                if ((subPixelMode & 2) != 0) {
                    string2 = string2 + ", vertical";
                }
                if ((subPixelMode & 4) != 0) {
                    string2 = string2 + ", native";
                }
                System.err.println(string2);
            }
        }
        if ((theFontFactory = PrismFontFactory.getFontFactory(string)) == null) {
            if (useNativeRasterizer) {
                useNativeRasterizer = false;
                string = T2K_FACTORY;
            } else {
                useNativeRasterizer = true;
                string = PrismFontFactory.getNativeFactoryName();
            }
            if (string != null) {
                theFontFactory = PrismFontFactory.getFontFactory(string);
            }
            if (debugFonts) {
                System.err.println("*** Loading primary font factory failed. ***");
                System.err.println("*** Fallbacking to " + string + " ***");
            }
        }
        return theFontFactory;
    }

    private static synchronized PrismFontFactory getFontFactory(String string) {
        try {
            Class<?> class_ = Class.forName(string);
            Method method = class_.getMethod("getFactory", null);
            return (PrismFontFactory)method.invoke(null, new Object[0]);
        }
        catch (Throwable throwable) {
            if (debugFonts) {
                System.err.println("Loading font factory failed " + string);
            }
            return null;
        }
    }

    protected abstract PrismFontFile createFontFile(String var1, String var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) throws Exception;

    public abstract GlyphLayout createGlyphLayout();

    private PrismFontFile createFontResource(String string, int n2) {
        return this.createFontResource(string, n2, true, false, false, false);
    }

    private PrismFontFile createFontResource(String string, int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        String string2 = (string + n2).toLowerCase();
        PrismFontFile prismFontFile = this.fileNameToFontResourceMap.get(string2);
        if (prismFontFile != null) {
            return prismFontFile;
        }
        try {
            prismFontFile = this.createFontFile(null, string, n2, bl, bl2, bl3, bl4);
            if (bl) {
                this.storeInMap(prismFontFile.getFullName(), prismFontFile);
                this.fileNameToFontResourceMap.put(string2, prismFontFile);
            }
            return prismFontFile;
        }
        catch (Exception exception) {
            if (debugFonts) {
                exception.printStackTrace();
            }
            return null;
        }
    }

    private PrismFontFile createFontResource(String string, String string2) {
        return this.createFontResource(string, string2, true, false, false, false);
    }

    private PrismFontFile createFontResource(String string, String string2, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        if (string2 == null) {
            return null;
        }
        String string3 = string2.toLowerCase();
        if (string3.endsWith(".ttc")) {
            PrismFontFile prismFontFile;
            int n2 = 0;
            PrismFontFile prismFontFile2 = null;
            do {
                String string4 = (string2 + n2).toLowerCase();
                try {
                    prismFontFile = this.fileNameToFontResourceMap.get(string4);
                    if (prismFontFile != null) {
                        if (!string.equals(prismFontFile.getFullName())) continue;
                        return prismFontFile;
                    }
                    prismFontFile = this.createFontFile(string, string2, n2, bl, bl2, bl3, bl4);
                }
                catch (Exception exception) {
                    if (debugFonts) {
                        exception.printStackTrace();
                    }
                    return null;
                }
                String string5 = prismFontFile.getFullName();
                if (bl) {
                    this.storeInMap(string5, prismFontFile);
                    this.fileNameToFontResourceMap.put(string4, prismFontFile);
                }
                if (n2 != 0 && !string.equals(string5)) continue;
                prismFontFile2 = prismFontFile;
            } while (++n2 < prismFontFile.getFontCount());
            return prismFontFile2;
        }
        return this.createFontResource(string2, 0, bl, bl2, bl3, bl4);
    }

    private String dotStyleStr(boolean bl, boolean bl2) {
        if (!bl) {
            if (!bl2) {
                return "";
            }
            return ".italic";
        }
        if (!bl2) {
            return ".bold";
        }
        return ".bolditalic";
    }

    private void storeInMap(String string, FontResource fontResource) {
        if (string == null || fontResource == null) {
            return;
        }
        if (fontResource instanceof PrismCompositeFontResource) {
            System.err.println(string + " is a composite " + fontResource);
            Thread.dumpStack();
            return;
        }
        this.fontResourceMap.put(string.toLowerCase(), fontResource);
    }

    synchronized void addDecodedFont(PrismFontFile prismFontFile) {
        prismFontFile.setIsDecoded(true);
        this.addTmpFont(prismFontFile);
    }

    private synchronized void addTmpFont(PrismFontFile prismFontFile) {
        if (this.tmpFonts == null) {
            this.tmpFonts = new ArrayList();
        }
        WeakReference<PrismFontFile> weakReference = prismFontFile.isRegistered() ? new WeakReference<PrismFontFile>(prismFontFile) : prismFontFile.createFileDisposer(this);
        this.tmpFonts.add(weakReference);
        this.addFileCloserHook();
    }

    synchronized void removeTmpFont(WeakReference<PrismFontFile> weakReference) {
        if (this.tmpFonts != null) {
            this.tmpFonts.remove(weakReference);
        }
    }

    /*
     * WARNING - void declaration
     */
    public synchronized FontResource getFontResource(String string, boolean bl, boolean bl2, boolean bl3) {
        void var9_14;
        int n2;
        String string2;
        Object object;
        if (string == null || string.isEmpty()) {
            return null;
        }
        String string22 = string.toLowerCase();
        String string3 = this.dotStyleStr(bl, bl2);
        Object object3 = this.lookupResource(string22 + string3, bl3);
        if (object3 != null) {
            return object3;
        }
        if (this.embeddedFonts != null && bl3) {
            object3 = this.lookupResource(string22 + string3, false);
            if (object3 != null) {
                return new PrismCompositeFontResource((FontResource)object3, string22 + string3);
            }
            for (PrismFontFile object22 : this.embeddedFonts.values()) {
                object = object22.getFamilyName().toLowerCase();
                if (!((String)object).equals(string22)) continue;
                return new PrismCompositeFontResource(object22, string22 + string3);
            }
        }
        if (isWindows && (string2 = WindowsFontMap.findFontFile(string22, n2 = (bl ? 1 : 0) + (bl2 ? 2 : 0))) != null && (object3 = this.createFontResource(null, string2)) != null) {
            if (bl == object3.isBold() && bl2 == object3.isItalic() && !string3.isEmpty()) {
                this.storeInMap(string22 + string3, (FontResource)object3);
            }
            if (bl3) {
                object3 = new PrismCompositeFontResource((FontResource)object3, string22 + string3);
            }
            return object3;
        }
        this.getFullNameToFileMap();
        ArrayList<String> arrayList = this.familyToFontListMap.get(string22);
        if (arrayList == null) {
            return null;
        }
        Object var9_13 = null;
        object = null;
        Object object2 = null;
        Object object4 = null;
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            String string4 = (String)iterator.next();
            String string5 = string4.toLowerCase();
            object3 = this.fontResourceMap.get(string5);
            if (object3 == null) {
                String string6 = this.findFile(string5);
                if (string6 != null) {
                    object3 = this.getFontResource(string4, string6);
                }
                if (object3 == null) continue;
                this.storeInMap(string5, (FontResource)object3);
            }
            if (bl == object3.isBold() && bl2 == object3.isItalic()) {
                this.storeInMap(string22 + string3, (FontResource)object3);
                if (bl3) {
                    object3 = new PrismCompositeFontResource((FontResource)object3, string22 + string3);
                }
                return object3;
            }
            if (!object3.isBold()) {
                if (!object3.isItalic()) {
                    Object object5 = object3;
                    continue;
                }
                object2 = object3;
                continue;
            }
            if (!object3.isItalic()) {
                object = object3;
                continue;
            }
            object4 = object3;
        }
        object3 = !bl && !bl2 ? (object != null ? object : (object2 != null ? object2 : object4)) : (bl && !bl2 ? (var9_14 != null ? var9_14 : (object4 != null ? object4 : object2)) : (!bl && bl2 ? (object4 != null ? object4 : (var9_14 != null ? var9_14 : object)) : (object2 != null ? object2 : (object != null ? object : var9_14))));
        if (object3 != null) {
            this.storeInMap(string22 + string3, (FontResource)object3);
            if (bl3) {
                object3 = new PrismCompositeFontResource((FontResource)object3, string22 + string3);
            }
        }
        return object3;
    }

    @Override
    public synchronized PGFont createFont(String string, boolean bl, boolean bl2, float f2) {
        FontResource fontResource = null;
        if (string != null && !string.isEmpty()) {
            PGFont pGFont = LogicalFont.getLogicalFont(string, bl, bl2, f2);
            if (pGFont != null) {
                return pGFont;
            }
            fontResource = this.getFontResource(string, bl, bl2, true);
        }
        if (fontResource == null) {
            return LogicalFont.getLogicalFont("System", bl, bl2, f2);
        }
        return new PrismFont(fontResource, fontResource.getFullName(), f2);
    }

    @Override
    public synchronized PGFont createFont(String string, float f2) {
        FontResource fontResource = null;
        if (string != null && !string.isEmpty()) {
            PGFont pGFont = LogicalFont.getLogicalFont(string, f2);
            if (pGFont != null) {
                return pGFont;
            }
            fontResource = this.getFontResource(string, null, true);
        }
        if (fontResource == null) {
            return LogicalFont.getLogicalFont("System Regular", f2);
        }
        return new PrismFont(fontResource, fontResource.getFullName(), f2);
    }

    private PrismFontFile getFontResource(String string, String string2) {
        PrismFontFile prismFontFile = null;
        if (isMacOSX) {
            DFontDecoder dFontDecoder;
            block9: {
                dFontDecoder = null;
                if (string != null && string2.endsWith(".dfont")) {
                    dFontDecoder = new DFontDecoder();
                    try {
                        dFontDecoder.openFile();
                        dFontDecoder.decode(string);
                        dFontDecoder.closeFile();
                        string2 = dFontDecoder.getFile().getPath();
                    }
                    catch (Exception exception) {
                        string2 = null;
                        dFontDecoder.deleteFile();
                        dFontDecoder = null;
                        if (!debugFonts) break block9;
                        exception.printStackTrace();
                    }
                }
            }
            if (string2 != null) {
                prismFontFile = this.createFontResource(string, string2);
            }
            if (dFontDecoder != null) {
                if (prismFontFile != null) {
                    this.addDecodedFont(prismFontFile);
                } else {
                    dFontDecoder.deleteFile();
                }
            }
        } else {
            prismFontFile = this.createFontResource(string, string2);
        }
        return prismFontFile;
    }

    @Override
    public synchronized PGFont deriveFont(PGFont pGFont, boolean bl, boolean bl2, float f2) {
        FontResource fontResource = pGFont.getFontResource();
        return new PrismFont(fontResource, fontResource.getFullName(), f2);
    }

    private FontResource lookupResource(String string, boolean bl) {
        if (bl) {
            return this.compResourceMap.get(string);
        }
        return this.fontResourceMap.get(string);
    }

    public synchronized FontResource getFontResource(String string, String string2, boolean bl) {
        Object object;
        String string3;
        FontResource fontResource = null;
        if (string != null) {
            string3 = string.toLowerCase();
            object = this.lookupResource(string3, bl);
            if (object != null) {
                return object;
            }
            if (this.embeddedFonts != null && bl) {
                fontResource = this.lookupResource(string3, false);
                if (fontResource != null) {
                    fontResource = new PrismCompositeFontResource(fontResource, string3);
                }
                if (fontResource != null) {
                    return fontResource;
                }
            }
        }
        if (isWindows && string != null && (object = WindowsFontMap.findFontFile(string3 = string.toLowerCase(), -1)) != null && (fontResource = this.createFontResource(null, (String)object)) != null) {
            if (bl) {
                fontResource = new PrismCompositeFontResource(fontResource, string3);
            }
            return fontResource;
        }
        this.getFullNameToFileMap();
        if (string != null && string2 != null && (fontResource = this.getFontResource(string, string2)) != null) {
            if (bl) {
                fontResource = new PrismCompositeFontResource(fontResource, string.toLowerCase());
            }
            return fontResource;
        }
        if (string != null && (fontResource = this.getFontResourceByFullName(string, bl)) != null) {
            return fontResource;
        }
        if (string2 != null && (fontResource = this.getFontResourceByFileName(string2, bl)) != null) {
            return fontResource;
        }
        return null;
    }

    boolean isInstalledFont(String string) {
        String string2;
        if (isWindows) {
            if (string.toLowerCase().contains("\\windows\\fonts")) {
                return true;
            }
            File file = new File(string);
            string2 = file.getName();
        } else {
            if (isMacOSX && string.toLowerCase().contains("/library/fonts")) {
                return true;
            }
            File file = new File(string);
            string2 = file.getPath();
        }
        this.getFullNameToFileMap();
        return this.fileToFontMap.get(string2.toLowerCase()) != null;
    }

    private synchronized FontResource getFontResourceByFileName(String string, boolean bl) {
        if (this.fontToFileMap.size() <= 1) {
            return null;
        }
        String string2 = this.fileToFontMap.get(string.toLowerCase());
        FontResource fontResource = null;
        if (string2 == null) {
            fontResource = this.createFontResource(string, 0);
            if (fontResource != null) {
                String string3 = fontResource.getFullName().toLowerCase();
                this.storeInMap(string3, fontResource);
                if (bl) {
                    fontResource = new PrismCompositeFontResource(fontResource, string3);
                }
            }
        } else {
            String string4;
            String string5 = string2.toLowerCase();
            fontResource = this.lookupResource(string5, bl);
            if (fontResource == null && (string4 = this.findFile(string5)) != null) {
                fontResource = this.getFontResource(string2, string4);
                if (fontResource != null) {
                    this.storeInMap(string5, fontResource);
                }
                if (bl) {
                    fontResource = new PrismCompositeFontResource(fontResource, string5);
                }
            }
        }
        return fontResource;
    }

    private synchronized FontResource getFontResourceByFullName(String string, boolean bl) {
        String string2 = string.toLowerCase();
        if (this.fontToFileMap.size() <= 1) {
            string = "Lucida Sans Regular";
        }
        FontResource fontResource = null;
        String string3 = this.findFile(string2);
        if (string3 != null && (fontResource = this.getFontResource(string, string3)) != null) {
            this.storeInMap(string2, fontResource);
            if (bl) {
                fontResource = new PrismCompositeFontResource(fontResource, string2);
            }
        }
        return fontResource;
    }

    FontResource getDefaultFontResource(boolean bl) {
        FontResource fontResource = this.lookupResource("lucida sans regular", bl);
        if (fontResource == null) {
            fontResource = this.createFontResource("Lucida Sans Regular", jreFontDir + "LucidaSansRegular.ttf");
            if (fontResource == null) {
                String string;
                String string2;
                Object object = this.fontToFileMap.keySet().iterator();
                while (object.hasNext() && (fontResource = this.createFontResource("lucida sans regular", string2 = this.findFile(string = object.next()))) == null) {
                }
                if (fontResource == null && isLinux && (object = FontConfigManager.getDefaultFontPath()) != null) {
                    fontResource = this.createFontResource("lucida sans regular", (String)object);
                }
                if (fontResource == null) {
                    return null;
                }
            }
            this.storeInMap("lucida sans regular", fontResource);
            if (bl) {
                fontResource = new PrismCompositeFontResource(fontResource, "lucida sans regular");
            }
        }
        return fontResource;
    }

    private String findFile(String string) {
        if (string.equals("lucida sans regular")) {
            return jreFontDir + "LucidaSansRegular.ttf";
        }
        this.getFullNameToFileMap();
        String string2 = this.fontToFileMap.get(string);
        if (isWindows) {
            string2 = PrismFontFactory.getPathNameWindows(string2);
        }
        return string2;
    }

    private static native byte[] getFontPath();

    private static native String regReadFontLink(String var0);

    private static native String getEUDCFontFile();

    private static void getPlatformFontDirs() {
        if (userFontDir != null || sysFontDir != null) {
            return;
        }
        byte[] arrby = PrismFontFactory.getFontPath();
        String string = new String(arrby);
        int n2 = string.indexOf(59);
        if (n2 < 0) {
            sysFontDir = string;
        } else {
            sysFontDir = string.substring(0, n2);
            userFontDir = string.substring(n2 + 1, string.length());
        }
    }

    static ArrayList<String>[] getLinkedFonts(String string, boolean bl) {
        String[] arrstring;
        String string2;
        ArrayList[] arrarrayList = new ArrayList[]{new ArrayList(), new ArrayList()};
        if (isMacOSX) {
            arrarrayList[0].add("/Library/Fonts/Arial Unicode.ttf");
            arrarrayList[1].add("Arial Unicode MS");
            arrarrayList[0].add(jreFontDir + "LucidaSansRegular.ttf");
            arrarrayList[1].add("Lucida Sans Regular");
            arrarrayList[0].add("/System/Library/Fonts/Apple Symbols.ttf");
            arrarrayList[1].add("Apple Symbols");
            arrarrayList[0].add("/System/Library/Fonts/STHeiti Light.ttf");
            arrarrayList[1].add("Heiti SC Light");
            return arrarrayList;
        }
        if (!isWindows) {
            return arrarrayList;
        }
        if (bl) {
            arrarrayList[0].add(null);
            arrarrayList[1].add(string);
        }
        if ((string2 = PrismFontFactory.regReadFontLink(string)) != null && string2.length() > 0) {
            arrstring = string2.split("\u0000");
            int n2 = arrstring.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                String string3;
                String[] arrstring2 = arrstring[i2].split(",");
                int n3 = arrstring2.length;
                String string4 = PrismFontFactory.getPathNameWindows(arrstring2[0]);
                String string5 = string3 = n3 > 1 ? arrstring2[1] : null;
                if (string3 != null && arrarrayList[1].contains(string3) || string3 == null && arrarrayList[0].contains(string4)) continue;
                arrarrayList[0].add(string4);
                arrarrayList[1].add(string3);
            }
        }
        if ((arrstring = PrismFontFactory.getEUDCFontFile()) != null) {
            arrarrayList[0].add(arrstring);
            arrarrayList[1].add(null);
        }
        arrarrayList[0].add(jreFontDir + "LucidaSansRegular.ttf");
        arrarrayList[1].add("Lucida Sans Regular");
        if (PlatformUtil.isWinVistaOrLater()) {
            arrarrayList[0].add(PrismFontFactory.getPathNameWindows("mingliub.ttc"));
            arrarrayList[1].add("MingLiU-ExtB");
            if (PlatformUtil.isWin7OrLater()) {
                arrarrayList[0].add(PrismFontFactory.getPathNameWindows("seguisym.ttf"));
                arrarrayList[1].add("Segoe UI Symbol");
            } else {
                arrarrayList[0].add(PrismFontFactory.getPathNameWindows("cambria.ttc"));
                arrarrayList[1].add("Cambria Math");
            }
        }
        return arrarrayList;
    }

    private void resolveWindowsFonts(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, ArrayList<String>> hashMap3) {
        String string;
        ArrayList<String> arrayList = null;
        for (String object : hashMap2.keySet()) {
            Object n3 = hashMap.get(object);
            if (n3 != null) continue;
            int string2 = object.indexOf("  ");
            if (string2 > 0) {
                string = object.substring(0, string2);
                n3 = hashMap.get(string = string.concat(object.substring(string2 + 1)));
                if (n3 == null || hashMap2.containsKey(string)) continue;
                hashMap.remove(string);
                hashMap.put(object, (String)n3);
                continue;
            }
            if (object.equals("marlett")) {
                hashMap.put(object, "marlett.ttf");
                continue;
            }
            if (object.equals("david")) {
                n3 = hashMap.get("david regular");
                if (n3 == null) continue;
                hashMap.remove("david regular");
                hashMap.put("david", (String)n3);
                continue;
            }
            if (arrayList == null) {
                arrayList = new ArrayList<String>();
            }
            arrayList.add(object);
        }
        if (arrayList != null) {
            HashSet hashSet = new HashSet();
            HashMap<String, String> hashMap4 = new HashMap<String, String>();
            hashMap4.putAll(hashMap);
            for (String string3 : hashMap2.keySet()) {
                hashMap4.remove(string3);
            }
            for (String i2 : hashMap4.keySet()) {
                hashSet.add(hashMap4.get(i2));
                hashMap.remove(i2);
            }
            this.resolveFontFiles(hashSet, arrayList, hashMap, hashMap2, hashMap3);
            if (arrayList.size() > 0) {
                int n2 = arrayList.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    ArrayList<String> arrayList2;
                    string = arrayList.get(i2);
                    String string2 = hashMap2.get(string);
                    if (string2 != null && (arrayList2 = hashMap3.get(string2)) != null && arrayList2.size() <= 1) {
                        hashMap3.remove(string2);
                    }
                    hashMap2.remove(string);
                }
            }
        }
    }

    private void resolveFontFiles(HashSet<String> hashSet, ArrayList<String> arrayList, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, ArrayList<String>> hashMap3) {
        for (String string : hashSet) {
            try {
                PrismFontFile prismFontFile;
                int n2 = 0;
                String string2 = PrismFontFactory.getPathNameWindows(string);
                while ((prismFontFile = this.createFontResource(string2, n2++)) != null) {
                    String string3 = prismFontFile.getFullName().toLowerCase();
                    String string4 = prismFontFile.getLocaleFullName().toLowerCase();
                    if (arrayList.contains(string3) || arrayList.contains(string4)) {
                        hashMap.put(string3, string);
                        arrayList.remove(string3);
                        if (arrayList.contains(string4)) {
                            arrayList.remove(string4);
                            String string5 = prismFontFile.getFamilyName();
                            String string6 = string5.toLowerCase();
                            hashMap2.remove(string4);
                            hashMap2.put(string3, string5);
                            ArrayList<String> arrayList2 = hashMap3.get(string6);
                            if (arrayList2 != null) {
                                arrayList2.remove(prismFontFile.getLocaleFullName());
                            } else {
                                String string7 = prismFontFile.getLocaleFamilyName().toLowerCase();
                                arrayList2 = hashMap3.get(string7);
                                if (arrayList2 != null) {
                                    hashMap3.remove(string7);
                                }
                                arrayList2 = new ArrayList();
                                hashMap3.put(string6, arrayList2);
                            }
                            arrayList2.add(prismFontFile.getFullName());
                        }
                    }
                    if (n2 < prismFontFile.getFontCount()) continue;
                }
            }
            catch (Exception exception) {
                if (!debugFonts) continue;
                exception.printStackTrace();
            }
        }
    }

    static native void populateFontFileNameMap(HashMap<String, String> var0, HashMap<String, String> var1, HashMap<String, ArrayList<String>> var2, Locale var3);

    static String getPathNameWindows(final String string) {
        if (string == null) {
            return null;
        }
        PrismFontFactory.getPlatformFontDirs();
        File file = new File(string);
        if (file.isAbsolute()) {
            return string;
        }
        if (userFontDir == null) {
            return sysFontDir + "\\" + string;
        }
        String string2 = AccessController.doPrivileged(new PrivilegedAction<String>(){

            @Override
            public String run() {
                File file = new File(sysFontDir + "\\" + string);
                if (file.exists()) {
                    return file.getAbsolutePath();
                }
                return userFontDir + "\\" + string;
            }
        });
        if (string2 != null) {
            return string2;
        }
        return null;
    }

    @Override
    public String[] getFontFamilyNames() {
        if (allFamilyNames == null) {
            ArrayList<String> arrayList = new ArrayList<String>();
            LogicalFont.addFamilies(arrayList);
            if (this.embeddedFonts != null) {
                for (PrismFontFile object : this.embeddedFonts.values()) {
                    if (arrayList.contains(object.getFamilyName())) continue;
                    arrayList.add(object.getFamilyName());
                }
            }
            this.getFullNameToFileMap();
            for (String string : this.fontToFamilyNameMap.values()) {
                if (arrayList.contains(string)) continue;
                arrayList.add(string);
            }
            Collections.sort(arrayList);
            allFamilyNames = new ArrayList<String>(arrayList);
        }
        return allFamilyNames.toArray(STR_ARRAY);
    }

    @Override
    public String[] getFontFullNames() {
        if (allFontNames == null) {
            ArrayList<String> arrayList = new ArrayList<String>();
            LogicalFont.addFullNames(arrayList);
            if (this.embeddedFonts != null) {
                for (PrismFontFile object : this.embeddedFonts.values()) {
                    if (arrayList.contains(object.getFullName())) continue;
                    arrayList.add(object.getFullName());
                }
            }
            this.getFullNameToFileMap();
            for (ArrayList arrayList2 : this.familyToFontListMap.values()) {
                for (String string : arrayList2) {
                    arrayList.add(string);
                }
            }
            Collections.sort(arrayList);
            allFontNames = arrayList;
        }
        return allFontNames.toArray(STR_ARRAY);
    }

    @Override
    public String[] getFontFullNames(String string) {
        ArrayList<String> arrayList;
        String[] arrstring = LogicalFont.getFontsInFamily(string);
        if (arrstring != null) {
            return arrstring;
        }
        if (this.embeddedFonts != null) {
            arrayList = null;
            for (PrismFontFile prismFontFile : this.embeddedFonts.values()) {
                if (!prismFontFile.getFamilyName().equalsIgnoreCase(string)) continue;
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(prismFontFile.getFullName());
            }
            if (arrayList != null) {
                return arrayList.toArray(STR_ARRAY);
            }
        }
        this.getFullNameToFileMap();
        string = string.toLowerCase();
        arrayList = this.familyToFontListMap.get(string);
        if (arrayList != null) {
            return arrayList.toArray(STR_ARRAY);
        }
        return STR_ARRAY;
    }

    public final int getSubPixelMode() {
        return subPixelMode;
    }

    public boolean isLCDTextSupported() {
        return lcdEnabled;
    }

    @Override
    public boolean isPlatformFont(String string) {
        if (string == null) {
            return false;
        }
        String string2 = string.toLowerCase();
        if (LogicalFont.isLogicalFont(string2)) {
            return true;
        }
        if (string2.startsWith("lucida sans")) {
            return true;
        }
        String string3 = PrismFontFactory.getSystemFont("System").toLowerCase();
        return string2.startsWith(string3);
    }

    public static boolean isJreFont(FontResource fontResource) {
        String string = fontResource.getFileName();
        return string.startsWith(jreFontDir);
    }

    public static float getLCDContrast() {
        if (lcdContrast == -1.0f) {
            lcdContrast = isWindows ? (float)PrismFontFactory.getLCDContrastWin32() / 1000.0f : 1.3f;
        }
        return lcdContrast;
    }

    private synchronized void addFileCloserHook() {
        if (fileCloser == null) {
            Runnable runnable = () -> {
                if (this.embeddedFonts != null) {
                    for (PrismFontFile object : this.embeddedFonts.values()) {
                        object.disposeOnShutdown();
                    }
                }
                if (this.tmpFonts != null) {
                    for (WeakReference weakReference : this.tmpFonts) {
                        PrismFontFile prismFontFile = (PrismFontFile)weakReference.get();
                        if (prismFontFile == null) continue;
                        prismFontFile.disposeOnShutdown();
                    }
                }
            };
            AccessController.doPrivileged(() -> {
                ThreadGroup threadGroup;
                ThreadGroup threadGroup2 = threadGroup = Thread.currentThread().getThreadGroup();
                while (threadGroup2 != null) {
                    threadGroup = threadGroup2;
                    threadGroup2 = threadGroup.getParent();
                }
                fileCloser = new Thread(threadGroup, runnable);
                fileCloser.setContextClassLoader(null);
                Runtime.getRuntime().addShutdownHook(fileCloser);
                return null;
            });
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public PGFont loadEmbeddedFont(String string, InputStream inputStream, float f2, boolean bl) {
        if (!this.hasPermission()) {
            return this.createFont("System Regular", f2);
        }
        if (FontFileWriter.hasTempPermission()) {
            return this.loadEmbeddedFont0(string, inputStream, f2, bl);
        }
        FontFileWriter.FontTracker fontTracker = FontFileWriter.FontTracker.getTracker();
        boolean bl2 = false;
        try {
            bl2 = fontTracker.acquirePermit();
            if (!bl2) {
                PGFont pGFont = null;
                return pGFont;
            }
            PGFont pGFont = this.loadEmbeddedFont0(string, inputStream, f2, bl);
            return pGFont;
        }
        catch (InterruptedException interruptedException) {
            PGFont pGFont = null;
            return pGFont;
        }
        finally {
            if (bl2) {
                fontTracker.releasePermit();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private PGFont loadEmbeddedFont0(String string, InputStream inputStream, float f2, boolean bl) {
        PrismFontFile prismFontFile = null;
        FontFileWriter fontFileWriter = new FontFileWriter();
        try {
            int n2;
            File file = fontFileWriter.openFile();
            byte[] arrby = new byte[8192];
            while ((n2 = inputStream.read(arrby)) >= 0) {
                fontFileWriter.writeBytes(arrby, 0, n2);
            }
            fontFileWriter.closeFile();
            prismFontFile = this.loadEmbeddedFont(string, file.getPath(), bl, true, fontFileWriter.isTracking());
            if (prismFontFile != null && prismFontFile.isDecoded()) {
                fontFileWriter.deleteFile();
            }
            this.addFileCloserHook();
        }
        catch (Exception exception) {
            fontFileWriter.deleteFile();
        }
        finally {
            if (prismFontFile == null) {
                fontFileWriter.deleteFile();
            }
        }
        if (prismFontFile != null) {
            if (f2 <= 0.0f) {
                f2 = PrismFontFactory.getSystemFontSize();
            }
            return new PrismFont(prismFontFile, prismFontFile.getFullName(), f2);
        }
        return null;
    }

    @Override
    public PGFont loadEmbeddedFont(String string, String string2, float f2, boolean bl) {
        if (!this.hasPermission()) {
            return this.createFont("System Regular", f2);
        }
        this.addFileCloserHook();
        PrismFontFile prismFontFile = this.loadEmbeddedFont(string, string2, bl, false, false);
        if (prismFontFile != null) {
            if (f2 <= 0.0f) {
                f2 = PrismFontFactory.getSystemFontSize();
            }
            return new PrismFont(prismFontFile, prismFontFile.getFullName(), f2);
        }
        return null;
    }

    private void removeEmbeddedFont(String string) {
        PrismFontFile prismFontFile = this.embeddedFonts.get(string);
        if (prismFontFile == null) {
            return;
        }
        this.embeddedFonts.remove(string);
        String string2 = string.toLowerCase();
        this.fontResourceMap.remove(string2);
        this.compResourceMap.remove(string2);
        Iterator<CompositeFontResource> iterator = this.compResourceMap.values().iterator();
        while (iterator.hasNext()) {
            CompositeFontResource compositeFontResource = iterator.next();
            if (compositeFontResource.getSlotResource(0) != prismFontFile) continue;
            iterator.remove();
        }
    }

    protected boolean registerEmbeddedFont(String string) {
        return true;
    }

    public int test_getNumEmbeddedFonts() {
        return this.numEmbeddedFonts;
    }

    private synchronized PrismFontFile loadEmbeddedFont(String string, String string2, boolean bl, boolean bl2, boolean bl3) {
        FontResource fontResource;
        ++this.numEmbeddedFonts;
        PrismFontFile prismFontFile = this.createFontResource(string, string2, bl, true, bl2, bl3);
        if (prismFontFile == null) {
            return null;
        }
        String string3 = prismFontFile.getFamilyName();
        if (string3 == null || string3.length() == 0) {
            return null;
        }
        String string4 = prismFontFile.getFullName();
        if (string4 == null || string4.length() == 0) {
            return null;
        }
        String string5 = prismFontFile.getPSName();
        if (string5 == null || string5.length() == 0) {
            return null;
        }
        boolean bl4 = true;
        if (this.embeddedFonts != null && (fontResource = (FontResource)this.embeddedFonts.get(string4)) != null && prismFontFile.equals(fontResource)) {
            bl4 = false;
        }
        if (bl4 && !this.registerEmbeddedFont(prismFontFile.getFileName())) {
            return null;
        }
        if (!bl) {
            if (bl2 && !prismFontFile.isDecoded()) {
                this.addTmpFont(prismFontFile);
            }
            return prismFontFile;
        }
        if (this.embeddedFonts == null) {
            this.embeddedFonts = new HashMap();
        }
        if (string != null && !string.isEmpty()) {
            this.embeddedFonts.put(string, prismFontFile);
            this.storeInMap(string, prismFontFile);
        }
        this.removeEmbeddedFont(string4);
        this.embeddedFonts.put(string4, prismFontFile);
        this.storeInMap(string4, prismFontFile);
        string3 = string3 + this.dotStyleStr(prismFontFile.isBold(), prismFontFile.isItalic());
        this.storeInMap(string3, prismFontFile);
        this.compResourceMap.remove(string3.toLowerCase());
        return prismFontFile;
    }

    private void logFontInfo(String string, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, ArrayList<String>> hashMap3) {
        System.err.println(string);
        for (String string2 : hashMap.keySet()) {
            System.err.println("font=" + string2 + " file=" + hashMap.get(string2));
        }
        for (String string2 : hashMap2.keySet()) {
            System.err.println("font=" + string2 + " family=" + hashMap2.get(string2));
        }
        for (String string2 : hashMap3.keySet()) {
            System.err.println("family=" + string2 + " fonts=" + hashMap3.get(string2));
        }
    }

    private synchronized HashMap<String, String> getFullNameToFileMap() {
        if (this.fontToFileMap == null) {
            HashMap<String, String> hashMap = new HashMap<String, String>(100);
            this.fontToFamilyNameMap = new HashMap(100);
            this.familyToFontListMap = new HashMap(50);
            this.fileToFontMap = new HashMap(100);
            if (isWindows) {
                PrismFontFactory.getPlatformFontDirs();
                PrismFontFactory.populateFontFileNameMap(hashMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
                if (debugFonts) {
                    System.err.println("Windows Locale ID=" + PrismFontFactory.getSystemLCID());
                    this.logFontInfo(" *** WINDOWS FONTS BEFORE RESOLVING", hashMap, this.fontToFamilyNameMap, this.familyToFontListMap);
                }
                this.resolveWindowsFonts(hashMap, this.fontToFamilyNameMap, this.familyToFontListMap);
                if (debugFonts) {
                    this.logFontInfo(" *** WINDOWS FONTS AFTER RESOLVING", hashMap, this.fontToFamilyNameMap, this.familyToFontListMap);
                }
            } else if (isMacOSX || isIOS) {
                MacFontFinder.populateFontFileNameMap(hashMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
            } else if (isLinux) {
                FontConfigManager.populateMaps(hashMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.getDefault());
                if (debugFonts) {
                    this.logFontInfo(" *** FONTCONFIG LOCATED FONTS:", hashMap, this.fontToFamilyNameMap, this.familyToFontListMap);
                }
            } else if (isAndroid) {
                AndroidFontFinder.populateFontFileNameMap(hashMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
            } else {
                this.fontToFileMap = hashMap;
                return this.fontToFileMap;
            }
            for (String string : hashMap.keySet()) {
                String string2 = hashMap.get(string);
                this.fileToFontMap.put(string2.toLowerCase(), string);
            }
            this.fontToFileMap = hashMap;
            if (isAndroid) {
                this.populateFontFileNameMapGeneric(AndroidFontFinder.getSystemFontsDir());
            }
            this.populateFontFileNameMapGeneric(jreFontDir);
        }
        return this.fontToFileMap;
    }

    @Override
    public final boolean hasPermission() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new AllPermission());
            }
            return true;
        }
        catch (SecurityException securityException) {
            return false;
        }
    }

    void addToMaps(PrismFontFile prismFontFile) {
        if (prismFontFile == null) {
            return;
        }
        String string = prismFontFile.getFullName();
        String string2 = prismFontFile.getFamilyName();
        if (string == null || string2 == null) {
            return;
        }
        String string3 = string.toLowerCase();
        String string4 = string2.toLowerCase();
        this.fontToFileMap.put(string3, prismFontFile.getFileName());
        this.fontToFamilyNameMap.put(string3, string2);
        ArrayList<String> arrayList = this.familyToFontListMap.get(string4);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.familyToFontListMap.put(string4, arrayList);
        }
        arrayList.add(string);
    }

    void populateFontFileNameMapGeneric(String string) {
        File file = new File(string);
        String[] arrstring = null;
        try {
            arrstring = AccessController.doPrivileged(() -> file.list(TTFilter.getInstance()));
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (arrstring == null) {
            return;
        }
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            try {
                PrismFontFile prismFontFile;
                String string2 = string + File.separator + arrstring[i2];
                if (!this.registerEmbeddedFont(string2)) continue;
                int n2 = 0;
                if ((prismFontFile = this.createFontResource(string2, n2++)) == null) continue;
                this.addToMaps(prismFontFile);
                while (n2 < prismFontFile.getFontCount() && (prismFontFile = this.createFontResource(string2, n2++)) != null) {
                    this.addToMaps(prismFontFile);
                }
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    static native int getLCDContrastWin32();

    private static native int getSystemFontSizeNative();

    private static native String getSystemFontNative();

    public static float getSystemFontSize() {
        if (systemFontSize == -1.0f) {
            if (isWindows) {
                float f2 = Screen.getMainScreen().getUIScale();
                systemFontSize = (float)PrismFontFactory.getSystemFontSizeNative() / f2;
            } else if (isMacOSX || isIOS) {
                systemFontSize = MacFontFinder.getSystemFontSize();
            } else if (isAndroid) {
                systemFontSize = AndroidFontFinder.getSystemFontSize();
            } else if (isEmbedded) {
                try {
                    int n2 = Screen.getMainScreen().getResolutionY();
                    systemFontSize = (float)n2 / 6.0f;
                }
                catch (NullPointerException nullPointerException) {
                    systemFontSize = 13.0f;
                }
            } else {
                systemFontSize = 13.0f;
            }
        }
        return systemFontSize;
    }

    public static String getSystemFont(String string) {
        if (string.equals("System")) {
            if (systemFontFamily == null) {
                if (isWindows) {
                    systemFontFamily = PrismFontFactory.getSystemFontNative();
                    if (systemFontFamily == null) {
                        systemFontFamily = "Arial";
                    }
                } else if (isMacOSX || isIOS) {
                    systemFontFamily = MacFontFinder.getSystemFont();
                    if (systemFontFamily == null) {
                        systemFontFamily = "Lucida Grande";
                    }
                } else {
                    systemFontFamily = isAndroid ? AndroidFontFinder.getSystemFont() : "Lucida Sans";
                }
            }
            return systemFontFamily;
        }
        if (string.equals("SansSerif")) {
            return "Arial";
        }
        if (string.equals("Serif")) {
            return "Times New Roman";
        }
        if (monospaceFontFamily != null || isMacOSX) {
            // empty if block
        }
        if (monospaceFontFamily == null) {
            monospaceFontFamily = "Courier New";
        }
        return monospaceFontFamily;
    }

    static native short getSystemLCID();

    static {
        fontSizeLimit = 80.0f;
        lcdContrast = -1.0f;
        isWindows = PlatformUtil.isWindows();
        isMacOSX = PlatformUtil.isMac();
        isLinux = PlatformUtil.isLinux();
        isIOS = PlatformUtil.isIOS();
        isAndroid = PlatformUtil.isAndroid();
        isEmbedded = PlatformUtil.isEmbedded();
        int[] arrn = new int[]{65536};
        debugFonts = AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            String string = System.getProperty("prism.debugfonts", "");
            boolean bl = "true".equals(string);
            jreFontDir = System.getProperty("java.home", "") + File.separator + "lib" + File.separator + "fonts" + File.separator;
            String string2 = System.getProperty("com.sun.javafx.fontSize");
            systemFontSize = -1.0f;
            if (string2 != null) {
                try {
                    systemFontSize = Float.parseFloat(string2);
                }
                catch (NumberFormatException numberFormatException) {
                    System.err.println("Cannot parse font size '" + string2 + "'");
                }
            }
            if ((string2 = System.getProperty("prism.subpixeltext", "on")).indexOf("on") != -1 || string2.indexOf("true") != -1) {
                subPixelMode = 1;
            }
            if (string2.indexOf("native") != -1) {
                subPixelMode |= 5;
            }
            if (string2.indexOf("vertical") != -1) {
                subPixelMode |= 7;
            }
            if ((string2 = System.getProperty("prism.fontSizeLimit")) != null) {
                try {
                    fontSizeLimit = Float.parseFloat(string2);
                    if (fontSizeLimit <= 0.0f) {
                        fontSizeLimit = Float.POSITIVE_INFINITY;
                    }
                }
                catch (NumberFormatException numberFormatException) {
                    System.err.println("Cannot parse fontSizeLimit '" + string2 + "'");
                }
            }
            useNativeRasterizer = isMacOSX || isWindows || isLinux;
            String string3 = useNativeRasterizer ? "native" : "t2k";
            String string4 = System.getProperty("prism.text", string3);
            useNativeRasterizer = useNativeRasterizer ? !string4.equals("t2k") : string4.equals("native");
            boolean bl2 = isMacOSX && !useNativeRasterizer || isIOS || isAndroid || isEmbedded;
            String string5 = bl2 ? "false" : "true";
            String string6 = System.getProperty("prism.lcdtext", string5);
            lcdEnabled = string6.equals("true");
            string2 = System.getProperty("prism.cacheLayoutSize");
            if (string2 != null) {
                try {
                    arrn[0] = Integer.parseInt(string2);
                    if (arrn[0] < 0) {
                        arrn[0] = 0;
                    }
                }
                catch (NumberFormatException numberFormatException) {
                    System.err.println("Cannot parse cache layout size '" + string2 + "'");
                }
            }
            return bl;
        });
        cacheLayoutSize = arrn[0];
        theFontFactory = null;
        STR_ARRAY = new String[0];
        sysFontDir = null;
        userFontDir = null;
        fileCloser = null;
        systemFontFamily = null;
        monospaceFontFamily = null;
    }

    private static class TTFilter
    implements FilenameFilter {
        static TTFilter ttFilter;

        @Override
        public boolean accept(File file, String string) {
            int n2 = string.length() - 4;
            if (n2 <= 0) {
                return false;
            }
            return string.startsWith(".ttf", n2) || string.startsWith(".TTF", n2) || string.startsWith(".ttc", n2) || string.startsWith(".TTC", n2) || string.startsWith(".otf", n2) || string.startsWith(".OTF", n2);
        }

        private TTFilter() {
        }

        static TTFilter getInstance() {
            if (ttFilter == null) {
                ttFilter = new TTFilter();
            }
            return ttFilter;
        }
    }
}

