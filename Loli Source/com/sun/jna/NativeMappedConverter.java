/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Klass;
import com.sun.jna.NativeMapped;
import com.sun.jna.Pointer;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.WeakHashMap;

public class NativeMappedConverter
implements TypeConverter {
    private static final Map<Class<?>, Reference<NativeMappedConverter>> converters = new WeakHashMap();
    private final Class<?> type;
    private final Class<?> nativeType;
    private final NativeMapped instance;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static NativeMappedConverter getInstance(Class<?> cls) {
        Map<Class<?>, Reference<NativeMappedConverter>> map = converters;
        synchronized (map) {
            NativeMappedConverter nmc;
            Reference<NativeMappedConverter> r2 = converters.get(cls);
            NativeMappedConverter nativeMappedConverter = nmc = r2 != null ? r2.get() : null;
            if (nmc == null) {
                nmc = new NativeMappedConverter(cls);
                converters.put(cls, new SoftReference<NativeMappedConverter>(nmc));
            }
            return nmc;
        }
    }

    public NativeMappedConverter(Class<?> type) {
        if (!NativeMapped.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Type must derive from " + NativeMapped.class);
        }
        this.type = type;
        this.instance = this.defaultValue();
        this.nativeType = this.instance.nativeType();
    }

    public NativeMapped defaultValue() {
        if (this.type.isEnum()) {
            return (NativeMapped)this.type.getEnumConstants()[0];
        }
        return (NativeMapped)Klass.newInstance(this.type);
    }

    @Override
    public Object fromNative(Object nativeValue, FromNativeContext context) {
        return this.instance.fromNative(nativeValue, context);
    }

    @Override
    public Class<?> nativeType() {
        return this.nativeType;
    }

    @Override
    public Object toNative(Object value, ToNativeContext context) {
        if (value == null) {
            if (Pointer.class.isAssignableFrom(this.nativeType)) {
                return null;
            }
            value = this.defaultValue();
        }
        return ((NativeMapped)value).toNative();
    }
}

