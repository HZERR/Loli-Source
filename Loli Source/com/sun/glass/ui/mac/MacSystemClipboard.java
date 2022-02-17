/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.SystemClipboard;
import com.sun.glass.ui.mac.MacPasteboard;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MacSystemClipboard
extends SystemClipboard {
    static final String FILE_SCHEME = "file";
    private static final String BAD_URI_MSG = "bad URI in com.sun.glass.ui.mac.MacSystemClipboard for file: ";
    private static final String BAD_URL_MSG = "bad URL in com.sun.glass.ui.mac.MacSystemClipboard for file: ";
    static final boolean SUPPORT_10_5_API = true;
    static final boolean SUPPORT_10_5_API_FORCE = false;
    static final boolean SUPPORT_10_6_API = false;
    long seed = 0L;
    final MacPasteboard pasteboard;

    public MacSystemClipboard(String string) {
        super(string);
        switch (string) {
            case "DND": {
                this.pasteboard = new MacPasteboard(2);
                break;
            }
            case "SYSTEM": {
                this.pasteboard = new MacPasteboard(1);
                break;
            }
            default: {
                this.pasteboard = new MacPasteboard(string);
            }
        }
    }

    @Override
    protected boolean isOwner() {
        return this.seed == this.pasteboard.getSeed();
    }

    @Override
    protected int supportedSourceActionsFromSystem() {
        return this.pasteboard.getAllowedOperation();
    }

    @Override
    protected void pushTargetActionToSystem(int n2) {
    }

    @Override
    protected void pushToSystem(HashMap<String, Object> hashMap, int n2) {
        HashMap<String, byte[]> hashMap2 = null;
        HashMap[] arrhashMap = null;
        block19: for (String string : hashMap.keySet()) {
            Object object = hashMap.get(string);
            if (object == null) continue;
            switch (string) {
                case "text/uri-list": {
                    Object object2 = this.putToItemList(((String)object).split("\n"), true);
                    if (object2.isEmpty()) continue block19;
                    arrhashMap = new HashMap[object2.size()];
                    object2.toArray(arrhashMap);
                    break;
                }
                case "application/x-java-rawimage": 
                case "application/x-java-drag-image": {
                    List<HashMap<String, Object>> list;
                    Object object2 = null;
                    if (object instanceof Pixels) {
                        object2 = (Pixels)object;
                    } else if (object instanceof ByteBuffer) {
                        try {
                            list = (ByteBuffer)object;
                            ((Buffer)((Object)list)).rewind();
                            object2 = Application.GetApplication().createPixels(((ByteBuffer)((Object)list)).getInt(), ((ByteBuffer)((Object)list)).getInt(), ((ByteBuffer)((Object)list)).slice());
                        }
                        catch (Exception exception) {}
                    } else if (object instanceof IntBuffer) {
                        try {
                            list = (IntBuffer)object;
                            ((Buffer)((Object)list)).rewind();
                            object2 = Application.GetApplication().createPixels(((IntBuffer)((Object)list)).get(), ((IntBuffer)((Object)list)).get(), ((IntBuffer)((Object)list)).slice());
                        }
                        catch (Exception exception) {}
                    } else {
                        throw new RuntimeException(object.getClass().getName() + " cannot be converted to Pixels");
                    }
                    if (object2 == null) continue block19;
                    if (hashMap2 == null) {
                        hashMap2 = new HashMap();
                    }
                    hashMap2.put(FormatEncoder.mimeToUtf(string), (byte[])object2);
                    break;
                }
                case "text/plain": 
                case "text/html": 
                case "text/rtf": {
                    Object object2;
                    if (object instanceof String) {
                        object2 = (String)object;
                        if (hashMap2 == null) {
                            hashMap2 = new HashMap();
                        }
                        hashMap2.put(FormatEncoder.mimeToUtf(string), (byte[])object2);
                        break;
                    }
                    System.err.println("DelayedCallback not implemented yet: RT-14593");
                    Thread.dumpStack();
                    break;
                }
                case "application/x-java-file-list": {
                    List<HashMap<String, Object>> list;
                    Object object2 = (String[])object;
                    if (hashMap.get("text/uri-list") == null) {
                        list = this.putToItemList((String[])object2, true);
                        if (list.isEmpty()) continue block19;
                        arrhashMap = new HashMap[list.size()];
                        list.toArray(arrhashMap);
                        break;
                    }
                    if (hashMap2 == null) {
                        hashMap2 = new HashMap();
                    }
                    list = null;
                    for (int i2 = 0; i2 < ((Object)object2).length; ++i2) {
                        Object object3 = object2[i2];
                        String string2 = FileSystems.getDefault().getPath((String)object3, new String[0]).toUri().toASCIIString();
                        if (list == null) {
                            list = new StringBuilder();
                        }
                        ((StringBuilder)((Object)list)).append(string2);
                        if (i2 >= ((Object)object2).length - 1) continue;
                        ((StringBuilder)((Object)list)).append("\n");
                    }
                    if (list == null) continue block19;
                    if (hashMap2.get("public.utf8-plain-text") != null) break;
                    hashMap2.remove("public.utf8-plain-text");
                    hashMap2.put("public.utf8-plain-text", (byte[])((StringBuilder)((Object)list)).toString());
                    break;
                }
                default: {
                    if (hashMap2 == null) {
                        hashMap2 = new HashMap<String, byte[]>();
                    }
                    hashMap2.put(FormatEncoder.mimeToUtf(string), this.serialize(object));
                }
            }
        }
        if (hashMap2 != null) {
            if (arrhashMap == null || arrhashMap.length == 0) {
                arrhashMap = new HashMap[]{hashMap2};
            } else {
                arrhashMap[0].putAll(hashMap2);
            }
        }
        if (arrhashMap != null) {
            this.seed = this.pasteboard.putItems(arrhashMap, n2);
        }
    }

    @Override
    protected Object popFromSystem(String string) {
        String[][] arrstring = this.pasteboard.getUTFs();
        if (arrstring == null) {
            return null;
        }
        switch (string) {
            case "application/x-java-rawimage": {
                ArrayList<Pixels> arrayList = new ArrayList<Pixels>();
                for (int i2 = 0; i2 < arrstring.length; ++i2) {
                    byte[] arrby = this.pasteboard.getItemAsRawImage(i2);
                    if (arrby == null) continue;
                    Pixels pixels = this.getPixelsForRawImage(arrby);
                    arrayList.add(pixels);
                    break;
                }
                return this.getObjectFromList(arrayList);
            }
            case "text/plain": 
            case "text/html": 
            case "text/rtf": 
            case "text/uri-list": {
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int i3 = 0; i3 < arrstring.length; ++i3) {
                    String string2 = this.pasteboard.getItemStringForUTF(i3, FormatEncoder.mimeToUtf(string));
                    if (string2 == null) continue;
                    arrayList.add(string2);
                    break;
                }
                return this.getObjectFromList(arrayList);
            }
            case "application/x-java-file-list": {
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int i4 = 0; i4 < arrstring.length; ++i4) {
                    String string3 = this.pasteboard.getItemStringForUTF(i4, "public.file-url");
                    if (string3 == null) continue;
                    arrayList.add(MacSystemClipboard._convertFileReferencePath(string3));
                }
                String[] arrstring2 = null;
                if (arrayList.size() > 0) {
                    arrstring2 = new String[arrayList.size()];
                    arrayList.toArray(arrstring2);
                }
                return arrstring2;
            }
        }
        ArrayList<ByteBuffer> arrayList = new ArrayList<ByteBuffer>();
        for (int i5 = 0; i5 < arrstring.length; ++i5) {
            byte[] arrby = this.pasteboard.getItemBytesForUTF(i5, FormatEncoder.mimeToUtf(string));
            if (arrby == null) continue;
            ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
            arrayList.add(byteBuffer);
            break;
        }
        return this.getObjectFromList(arrayList);
    }

    private Object getObjectFromList(List<?> list) {
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    protected String[] mimesFromSystem() {
        String[][] arrstring = this.pasteboard.getUTFs();
        ArrayList<String> arrayList = new ArrayList<String>();
        if (arrstring != null) {
            for (String[] arrstring2 : arrstring) {
                if (arrstring2 == null) continue;
                for (String string : arrstring2) {
                    String string2 = FormatEncoder.utfToMime(string);
                    if (string2 == null || arrayList.contains(string2)) continue;
                    arrayList.add(string2);
                }
            }
        }
        String[][] arrstring3 = new String[arrayList.size()];
        arrayList.toArray((T[])arrstring3);
        return arrstring3;
    }

    @Override
    public String toString() {
        return "Mac OS X " + this.pasteboard.getName() + " Clipboard";
    }

    private URI createUri(String string, String string2) {
        URI uRI = null;
        try {
            uRI = new URI(string);
        }
        catch (URISyntaxException uRISyntaxException) {
            System.err.println(string2 + string);
            Thread.dumpStack();
        }
        return uRI;
    }

    private HashMap<String, Object> getItemFromURIString(String string) {
        Serializable serializable;
        String string2;
        String string3 = null;
        if (string.indexOf(58) == -1) {
            string2 = "public.file-url";
            string3 = FileSystems.getDefault().getPath(string, new String[0]).toUri().toASCIIString();
        } else {
            string2 = "public.url";
            serializable = this.createUri(string, BAD_URI_MSG);
            if (serializable != null) {
                string3 = ((URI)serializable).toASCIIString();
            }
        }
        if (string3 != null) {
            serializable = new HashMap();
            ((HashMap)serializable).put(string2, string3);
            return serializable;
        }
        return null;
    }

    private List<HashMap<String, Object>> putToItemList(String[] arrstring, boolean bl) {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        for (String string : arrstring) {
            HashMap<String, Object> hashMap;
            if (bl && string.startsWith("#") || (hashMap = this.getItemFromURIString(string)) == null) continue;
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    private static native String _convertFileReferencePath(String var0);

    private byte[] serialize(Object object) {
        if (object instanceof String) {
            String string = (String)object;
            return string.getBytes();
        }
        if (object instanceof ByteBuffer) {
            ByteBuffer byteBuffer = (ByteBuffer)object;
            return byteBuffer.array();
        }
        throw new RuntimeException("can not handle " + object);
    }

    private static class FormatEncoder {
        private static final String DYNAMIC_UTI_PREFIX = "dyn.";
        private static final Map<String, String> utm = new HashMap<String, String>();
        private static final Map<String, String> mtu = new HashMap<String, String>();

        private FormatEncoder() {
        }

        public static synchronized String mimeToUtf(String string) {
            if (mtu.containsKey(string)) {
                return mtu.get(string);
            }
            String string2 = FormatEncoder._convertMIMEtoUTI(string);
            mtu.put(string, string2);
            utm.put(string2, string);
            return string2;
        }

        public static synchronized String utfToMime(String string) {
            if (utm.containsKey(string)) {
                return utm.get(string);
            }
            if (string.startsWith(DYNAMIC_UTI_PREFIX)) {
                String string2 = FormatEncoder._convertUTItoMIME(string);
                mtu.put(string2, string);
                utm.put(string, string2);
                return string2;
            }
            return null;
        }

        private static native String _convertMIMEtoUTI(String var0);

        private static native String _convertUTItoMIME(String var0);

        static {
            utm.put("public.utf8-plain-text", "text/plain");
            utm.put("public.html", "text/html");
            utm.put("public.rtf", "text/rtf");
            utm.put("public.url", "text/uri-list");
            utm.put("public.file-url", "application/x-java-file-list");
            utm.put("public.tiff", "application/x-java-rawimage");
            utm.put("public.png", "application/x-java-rawimage");
            utm.put("application.x-java-rawimage", "application/x-java-rawimage");
            utm.put("application.x-java-drag-image", "application/x-java-drag-image");
            utm.put("application.x-java-drag-image-offset", "application/x-java-drag-image-offset");
            mtu.put("text/plain", "public.utf8-plain-text");
            mtu.put("text/html", "public.html");
            mtu.put("text/rtf", "public.rtf");
            mtu.put("text/uri-list", "public.url");
            mtu.put("application/x-java-file-list", "public.file-url");
            mtu.put("application/x-java-rawimage", "application.x-java-rawimage");
            mtu.put("application/x-java-drag-image", "application.x-java-drag-image");
            mtu.put("application/x-java-drag-image-offset", "application.x-java-drag-image-offset");
        }
    }
}

