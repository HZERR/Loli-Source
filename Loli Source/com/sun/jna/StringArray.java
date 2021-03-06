/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeString;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringArray
extends Memory
implements Function.PostCallRead {
    private String encoding;
    private List<NativeString> natives = new ArrayList<NativeString>();
    private Object[] original;

    public StringArray(String[] strings) {
        this(strings, false);
    }

    public StringArray(String[] strings, boolean wide) {
        this((Object[])strings, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
    }

    public StringArray(String[] strings, String encoding) {
        this((Object[])strings, encoding);
    }

    public StringArray(WString[] strings) {
        this(strings, "--WIDE-STRING--");
    }

    private StringArray(Object[] strings, String encoding) {
        super((strings.length + 1) * Native.POINTER_SIZE);
        this.original = strings;
        this.encoding = encoding;
        for (int i2 = 0; i2 < strings.length; ++i2) {
            Pointer p2 = null;
            if (strings[i2] != null) {
                NativeString ns = new NativeString(strings[i2].toString(), encoding);
                this.natives.add(ns);
                p2 = ns.getPointer();
            }
            this.setPointer(Native.POINTER_SIZE * i2, p2);
        }
        this.setPointer(Native.POINTER_SIZE * strings.length, null);
    }

    @Override
    public void read() {
        boolean returnWide = this.original instanceof WString[];
        boolean wide = "--WIDE-STRING--".equals(this.encoding);
        for (int si = 0; si < this.original.length; ++si) {
            Pointer p2 = this.getPointer(si * Native.POINTER_SIZE);
            CharSequence s2 = null;
            if (p2 != null) {
                String string = s2 = wide ? p2.getWideString(0L) : p2.getString(0L, this.encoding);
                if (returnWide) {
                    s2 = new WString((String)s2);
                }
            }
            this.original[si] = s2;
        }
    }

    @Override
    public String toString() {
        boolean wide = "--WIDE-STRING--".equals(this.encoding);
        String s2 = wide ? "const wchar_t*[]" : "const char*[]";
        s2 = s2 + Arrays.asList(this.original);
        return s2;
    }
}

