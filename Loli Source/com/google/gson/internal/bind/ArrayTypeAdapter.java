/*
 * Decompiled with CFR 0.150.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public final class ArrayTypeAdapter<E>
extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Type type = typeToken.getType();
            if (!(type instanceof GenericArrayType || type instanceof Class && ((Class)type).isArray())) {
                return null;
            }
            Type componentType = $Gson$Types.getArrayComponentType(type);
            TypeAdapter<?> componentTypeAdapter = gson.getAdapter(TypeToken.get(componentType));
            return new ArrayTypeAdapter(gson, componentTypeAdapter, $Gson$Types.getRawType(componentType));
        }
    };
    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;

    public ArrayTypeAdapter(Gson context, TypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(context, componentTypeAdapter, componentType);
        this.componentType = componentType;
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        ArrayList<E> list = new ArrayList<E>();
        in.beginArray();
        while (in.hasNext()) {
            E instance = this.componentTypeAdapter.read(in);
            list.add(instance);
        }
        in.endArray();
        Object array = Array.newInstance(this.componentType, list.size());
        for (int i2 = 0; i2 < list.size(); ++i2) {
            Array.set(array, i2, list.get(i2));
        }
        return array;
    }

    @Override
    public void write(JsonWriter out, Object array) throws IOException {
        if (array == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        int length = Array.getLength(array);
        for (int i2 = 0; i2 < length; ++i2) {
            Object value = Array.get(array, i2);
            this.componentTypeAdapter.write(out, value);
        }
        out.endArray();
    }
}
