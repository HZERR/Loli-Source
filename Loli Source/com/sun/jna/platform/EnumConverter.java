/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

public class EnumConverter<T extends Enum<T>>
implements TypeConverter {
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T fromNative(Object input, FromNativeContext context) {
        Integer i2 = (Integer)input;
        Enum[] vals = (Enum[])this.clazz.getEnumConstants();
        return (T)vals[i2];
    }

    @Override
    public Integer toNative(Object input, ToNativeContext context) {
        Enum t2 = (Enum)this.clazz.cast(input);
        return t2.ordinal();
    }

    public Class<Integer> nativeType() {
        return Integer.class;
    }
}

