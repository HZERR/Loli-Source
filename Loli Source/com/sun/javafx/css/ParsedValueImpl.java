/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.StyleConverterImpl;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ParsedValueImpl<V, T>
extends ParsedValue<V, T> {
    private final boolean lookup;
    private final boolean containsLookups;
    private static int indent = 0;
    private int hash = Integer.MIN_VALUE;
    private static final byte NULL_VALUE = 0;
    private static final byte VALUE = 1;
    private static final byte VALUE_ARRAY = 2;
    private static final byte ARRAY_OF_VALUE_ARRAY = 3;
    private static final byte STRING = 4;
    private static final byte COLOR = 5;
    private static final byte ENUM = 6;
    private static final byte BOOLEAN = 7;
    private static final byte URL = 8;
    private static final byte SIZE = 9;

    public final boolean isLookup() {
        return this.lookup;
    }

    public final boolean isContainsLookups() {
        return this.containsLookups;
    }

    private static boolean getContainsLookupsFlag(Object object) {
        boolean bl;
        block4: {
            block6: {
                block5: {
                    block3: {
                        bl = false;
                        if (!(object instanceof Size)) break block3;
                        bl = false;
                        break block4;
                    }
                    if (!(object instanceof ParsedValueImpl)) break block5;
                    ParsedValueImpl parsedValueImpl = (ParsedValueImpl)object;
                    bl = parsedValueImpl.lookup || parsedValueImpl.containsLookups;
                    break block4;
                }
                if (!(object instanceof ParsedValueImpl[])) break block6;
                ParsedValueImpl[] arrparsedValueImpl = (ParsedValueImpl[])object;
                for (int i2 = 0; i2 < arrparsedValueImpl.length && !bl; ++i2) {
                    if (arrparsedValueImpl[i2] == null) continue;
                    bl = bl || arrparsedValueImpl[i2].lookup || arrparsedValueImpl[i2].containsLookups;
                }
                break block4;
            }
            if (!(object instanceof ParsedValueImpl[][])) break block4;
            ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])object;
            for (int i3 = 0; i3 < arrparsedValueImpl.length && !bl; ++i3) {
                if (arrparsedValueImpl[i3] == null) continue;
                for (int i4 = 0; i4 < arrparsedValueImpl[i3].length && !bl; ++i4) {
                    if (arrparsedValueImpl[i3][i4] == null) continue;
                    bl = bl || arrparsedValueImpl[i3][i4].lookup || arrparsedValueImpl[i3][i4].containsLookups;
                }
            }
        }
        return bl;
    }

    public static boolean containsFontRelativeSize(ParsedValue parsedValue, boolean bl) {
        boolean bl2;
        block4: {
            Object v2;
            block6: {
                block5: {
                    block3: {
                        bl2 = false;
                        v2 = parsedValue.getValue();
                        if (!(v2 instanceof Size)) break block3;
                        Size size = (Size)v2;
                        bl2 = size.getUnits() == SizeUnits.PERCENT ? bl : !size.isAbsolute();
                        break block4;
                    }
                    if (!(v2 instanceof ParsedValue)) break block5;
                    ParsedValueImpl parsedValueImpl = (ParsedValueImpl)v2;
                    bl2 = ParsedValueImpl.containsFontRelativeSize(parsedValueImpl, bl);
                    break block4;
                }
                if (!(v2 instanceof ParsedValue[])) break block6;
                ParsedValue[] arrparsedValue = (ParsedValue[])v2;
                for (int i2 = 0; i2 < arrparsedValue.length && !bl2; ++i2) {
                    if (arrparsedValue[i2] == null) continue;
                    bl2 = ParsedValueImpl.containsFontRelativeSize(arrparsedValue[i2], bl);
                }
                break block4;
            }
            if (!(v2 instanceof ParsedValueImpl[][])) break block4;
            ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])v2;
            for (int i3 = 0; i3 < arrparsedValueImpl.length && !bl2; ++i3) {
                if (arrparsedValueImpl[i3] == null) continue;
                for (int i4 = 0; i4 < arrparsedValueImpl[i3].length && !bl2; ++i4) {
                    if (arrparsedValueImpl[i3][i4] == null) continue;
                    bl2 = ParsedValueImpl.containsFontRelativeSize(arrparsedValueImpl[i3][i4], bl);
                }
            }
        }
        return bl2;
    }

    public ParsedValueImpl(V v2, StyleConverter<V, T> styleConverter, boolean bl) {
        super(v2, styleConverter);
        this.lookup = bl;
        this.containsLookups = bl || ParsedValueImpl.getContainsLookupsFlag(v2);
    }

    public ParsedValueImpl(V v2, StyleConverter<V, T> styleConverter) {
        this(v2, styleConverter, false);
    }

    @Override
    public T convert(Font font) {
        return (T)(this.converter != null ? this.converter.convert(this, font) : this.value);
    }

    private static String spaces() {
        return new String(new char[indent]).replace('\u0000', ' ');
    }

    private static void indent() {
        indent += 2;
    }

    private static void outdent() {
        indent = Math.max(0, indent - 2);
    }

    public String toString() {
        String string = System.lineSeparator();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ParsedValueImpl.spaces()).append(this.lookup ? "<Value lookup=\"true\">" : "<Value>").append(string);
        ParsedValueImpl.indent();
        if (this.value != null) {
            this.appendValue(stringBuilder, this.value, "value");
        } else {
            this.appendValue(stringBuilder, "null", "value");
        }
        stringBuilder.append(ParsedValueImpl.spaces()).append("<converter>").append(this.converter).append("</converter>").append(string);
        ParsedValueImpl.outdent();
        stringBuilder.append(ParsedValueImpl.spaces()).append("</Value>");
        return stringBuilder.toString();
    }

    private void appendValue(StringBuilder stringBuilder, Object object, String string) {
        String string2 = System.lineSeparator();
        if (object instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])object;
            stringBuilder.append(ParsedValueImpl.spaces()).append('<').append(string).append(" layers=\"").append(arrparsedValueImpl.length).append("\">").append(string2);
            ParsedValueImpl.indent();
            for (ParsedValueImpl[] arrparsedValueImpl2 : arrparsedValueImpl) {
                stringBuilder.append(ParsedValueImpl.spaces()).append("<layer>").append(string2);
                ParsedValueImpl.indent();
                if (arrparsedValueImpl2 == null) {
                    stringBuilder.append(ParsedValueImpl.spaces()).append("null").append(string2);
                    continue;
                }
                for (ParsedValueImpl parsedValueImpl : arrparsedValueImpl2) {
                    if (parsedValueImpl == null) {
                        stringBuilder.append(ParsedValueImpl.spaces()).append("null").append(string2);
                        continue;
                    }
                    stringBuilder.append(parsedValueImpl);
                }
                ParsedValueImpl.outdent();
                stringBuilder.append(ParsedValueImpl.spaces()).append("</layer>").append(string2);
            }
            ParsedValueImpl.outdent();
            stringBuilder.append(ParsedValueImpl.spaces()).append("</").append(string).append('>').append(string2);
        } else if (object instanceof ParsedValueImpl[]) {
            ParsedValueImpl[] arrparsedValueImpl = (ParsedValueImpl[])object;
            stringBuilder.append(ParsedValueImpl.spaces()).append('<').append(string).append(" values=\"").append(arrparsedValueImpl.length).append("\">").append(string2);
            ParsedValueImpl.indent();
            for (ParsedValueImpl parsedValueImpl : arrparsedValueImpl) {
                if (parsedValueImpl == null) {
                    stringBuilder.append(ParsedValueImpl.spaces()).append("null").append(string2);
                    continue;
                }
                stringBuilder.append(parsedValueImpl);
            }
            ParsedValueImpl.outdent();
            stringBuilder.append(ParsedValueImpl.spaces()).append("</").append(string).append('>').append(string2);
        } else if (object instanceof ParsedValueImpl) {
            stringBuilder.append(ParsedValueImpl.spaces()).append('<').append(string).append('>').append(string2);
            ParsedValueImpl.indent();
            stringBuilder.append(object);
            ParsedValueImpl.outdent();
            stringBuilder.append(ParsedValueImpl.spaces()).append("</").append(string).append('>').append(string2);
        } else {
            stringBuilder.append(ParsedValueImpl.spaces()).append('<').append(string).append('>');
            stringBuilder.append(object);
            stringBuilder.append("</").append(string).append('>').append(string2);
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        ParsedValueImpl parsedValueImpl = (ParsedValueImpl)object;
        if (this.hash != parsedValueImpl.hash) {
            return false;
        }
        if (this.value instanceof ParsedValueImpl[][]) {
            if (!(parsedValueImpl.value instanceof ParsedValueImpl[][])) {
                return false;
            }
            ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])this.value;
            ParsedValueImpl[][] arrparsedValueImpl2 = (ParsedValueImpl[][])parsedValueImpl.value;
            if (arrparsedValueImpl.length != arrparsedValueImpl2.length) {
                return false;
            }
            for (int i2 = 0; i2 < arrparsedValueImpl.length; ++i2) {
                if (arrparsedValueImpl[i2] == null && arrparsedValueImpl2[i2] == null) continue;
                if (arrparsedValueImpl[i2] == null || arrparsedValueImpl2[i2] == null) {
                    return false;
                }
                if (arrparsedValueImpl[i2].length != arrparsedValueImpl2[i2].length) {
                    return false;
                }
                for (int i3 = 0; i3 < arrparsedValueImpl[i2].length; ++i3) {
                    ParsedValueImpl parsedValueImpl2 = arrparsedValueImpl[i2][i3];
                    ParsedValueImpl parsedValueImpl3 = arrparsedValueImpl2[i2][i3];
                    if (!(parsedValueImpl2 != null ? !parsedValueImpl2.equals(parsedValueImpl3) : parsedValueImpl3 != null)) continue;
                    return false;
                }
            }
            return true;
        }
        if (this.value instanceof ParsedValueImpl[]) {
            if (!(parsedValueImpl.value instanceof ParsedValueImpl[])) {
                return false;
            }
            ParsedValueImpl[] arrparsedValueImpl = (ParsedValueImpl[])this.value;
            ParsedValueImpl[] arrparsedValueImpl3 = (ParsedValueImpl[])parsedValueImpl.value;
            if (arrparsedValueImpl.length != arrparsedValueImpl3.length) {
                return false;
            }
            for (int i4 = 0; i4 < arrparsedValueImpl.length; ++i4) {
                ParsedValueImpl parsedValueImpl4 = arrparsedValueImpl[i4];
                ParsedValueImpl parsedValueImpl5 = arrparsedValueImpl3[i4];
                if (!(parsedValueImpl4 != null ? !parsedValueImpl4.equals(parsedValueImpl5) : parsedValueImpl5 != null)) continue;
                return false;
            }
            return true;
        }
        if (this.value instanceof String && parsedValueImpl.value instanceof String) {
            return this.value.toString().equalsIgnoreCase(parsedValueImpl.value.toString());
        }
        return this.value != null ? this.value.equals(parsedValueImpl.value) : parsedValueImpl.value == null;
    }

    public int hashCode() {
        if (this.hash == Integer.MIN_VALUE) {
            this.hash = 17;
            if (this.value instanceof ParsedValueImpl[][]) {
                ParsedValueImpl[][] arrparsedValueImpl = (ParsedValueImpl[][])this.value;
                for (int i2 = 0; i2 < arrparsedValueImpl.length; ++i2) {
                    for (int i3 = 0; i3 < arrparsedValueImpl[i2].length; ++i3) {
                        ParsedValueImpl parsedValueImpl = arrparsedValueImpl[i2][i3];
                        this.hash = 37 * this.hash + (parsedValueImpl != null && parsedValueImpl.value != null ? parsedValueImpl.value.hashCode() : 0);
                    }
                }
            } else if (this.value instanceof ParsedValueImpl[]) {
                ParsedValueImpl[] arrparsedValueImpl = (ParsedValueImpl[])this.value;
                for (int i4 = 0; i4 < arrparsedValueImpl.length; ++i4) {
                    if (arrparsedValueImpl[i4] == null || arrparsedValueImpl[i4].value == null) continue;
                    ParsedValueImpl parsedValueImpl = arrparsedValueImpl[i4];
                    this.hash = 37 * this.hash + (parsedValueImpl != null && parsedValueImpl.value != null ? parsedValueImpl.value.hashCode() : 0);
                }
            } else {
                this.hash = 37 * this.hash + (this.value != null ? this.value.hashCode() : 0);
            }
        }
        return this.hash;
    }

    public final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        dataOutputStream.writeBoolean(this.lookup);
        if (this.converter instanceof StyleConverterImpl) {
            dataOutputStream.writeBoolean(true);
            ((StyleConverterImpl)this.converter).writeBinary(dataOutputStream, stringStore);
        } else {
            dataOutputStream.writeBoolean(false);
            if (this.converter != null) {
                System.err.println("cannot writeBinary " + this.converter.getClass().getName());
            }
        }
        if (this.value instanceof ParsedValue) {
            dataOutputStream.writeByte(1);
            ParsedValue parsedValue = (ParsedValue)this.value;
            if (parsedValue instanceof ParsedValueImpl) {
                ((ParsedValueImpl)parsedValue).writeBinary(dataOutputStream, stringStore);
            } else {
                ParsedValueImpl parsedValueImpl = new ParsedValueImpl(parsedValue.getValue(), parsedValue.getConverter());
                parsedValueImpl.writeBinary(dataOutputStream, stringStore);
            }
        } else if (this.value instanceof ParsedValue[]) {
            dataOutputStream.writeByte(2);
            ParsedValue[] arrparsedValue = (ParsedValue[])this.value;
            if (arrparsedValue != null) {
                dataOutputStream.writeByte(1);
            } else {
                dataOutputStream.writeByte(0);
            }
            int n2 = arrparsedValue != null ? arrparsedValue.length : 0;
            dataOutputStream.writeInt(n2);
            for (int i2 = 0; i2 < n2; ++i2) {
                if (arrparsedValue[i2] != null) {
                    dataOutputStream.writeByte(1);
                    ParsedValue parsedValue = arrparsedValue[i2];
                    if (parsedValue instanceof ParsedValueImpl) {
                        ((ParsedValueImpl)parsedValue).writeBinary(dataOutputStream, stringStore);
                        continue;
                    }
                    ParsedValueImpl parsedValueImpl = new ParsedValueImpl(parsedValue.getValue(), parsedValue.getConverter());
                    parsedValueImpl.writeBinary(dataOutputStream, stringStore);
                    continue;
                }
                dataOutputStream.writeByte(0);
            }
        } else if (this.value instanceof ParsedValue[][]) {
            dataOutputStream.writeByte(3);
            ParsedValue[][] arrparsedValue = (ParsedValue[][])this.value;
            if (arrparsedValue != null) {
                dataOutputStream.writeByte(1);
            } else {
                dataOutputStream.writeByte(0);
            }
            int n3 = arrparsedValue != null ? arrparsedValue.length : 0;
            dataOutputStream.writeInt(n3);
            for (int i3 = 0; i3 < n3; ++i3) {
                ParsedValue[] arrparsedValue2 = arrparsedValue[i3];
                if (arrparsedValue2 != null) {
                    dataOutputStream.writeByte(1);
                } else {
                    dataOutputStream.writeByte(0);
                }
                int n4 = arrparsedValue2 != null ? arrparsedValue2.length : 0;
                dataOutputStream.writeInt(n4);
                for (int i4 = 0; i4 < n4; ++i4) {
                    if (arrparsedValue2[i4] != null) {
                        dataOutputStream.writeByte(1);
                        ParsedValue parsedValue = arrparsedValue2[i4];
                        if (parsedValue instanceof ParsedValueImpl) {
                            ((ParsedValueImpl)parsedValue).writeBinary(dataOutputStream, stringStore);
                            continue;
                        }
                        ParsedValueImpl parsedValueImpl = new ParsedValueImpl(parsedValue.getValue(), parsedValue.getConverter());
                        parsedValueImpl.writeBinary(dataOutputStream, stringStore);
                        continue;
                    }
                    dataOutputStream.writeByte(0);
                }
            }
        } else if (this.value instanceof Color) {
            Color color = (Color)this.value;
            dataOutputStream.writeByte(5);
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getRed()));
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getGreen()));
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getBlue()));
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getOpacity()));
        } else if (this.value instanceof Enum) {
            Enum enum_ = (Enum)this.value;
            int n5 = stringStore.addString(enum_.name());
            dataOutputStream.writeByte(6);
            dataOutputStream.writeShort(n5);
        } else if (this.value instanceof Boolean) {
            Boolean bl = (Boolean)this.value;
            dataOutputStream.writeByte(7);
            dataOutputStream.writeBoolean(bl);
        } else if (this.value instanceof Size) {
            Size size = (Size)this.value;
            dataOutputStream.writeByte(9);
            double d2 = size.getValue();
            long l2 = Double.doubleToLongBits(d2);
            dataOutputStream.writeLong(l2);
            int n6 = stringStore.addString(size.getUnits().name());
            dataOutputStream.writeShort(n6);
        } else if (this.value instanceof String) {
            dataOutputStream.writeByte(4);
            int n7 = stringStore.addString((String)this.value);
            dataOutputStream.writeShort(n7);
        } else if (this.value instanceof URL) {
            dataOutputStream.writeByte(8);
            int n8 = stringStore.addString(this.value.toString());
            dataOutputStream.writeShort(n8);
        } else if (this.value == null) {
            dataOutputStream.writeByte(0);
        } else {
            throw new InternalError("cannot writeBinary " + this);
        }
    }

    public static ParsedValueImpl readBinary(int n2, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        boolean bl = dataInputStream.readBoolean();
        boolean bl2 = dataInputStream.readBoolean();
        StyleConverter<?, ?> styleConverter = bl2 ? StyleConverterImpl.readBinary(dataInputStream, arrstring) : null;
        byte by = dataInputStream.readByte();
        if (by == 1) {
            ParsedValueImpl parsedValueImpl = ParsedValueImpl.readBinary(n2, dataInputStream, arrstring);
            return new ParsedValueImpl(parsedValueImpl, styleConverter, bl);
        }
        if (by == 2) {
            int n3;
            if (n2 >= 4) {
                dataInputStream.readByte();
            }
            ParsedValueImpl[] arrparsedValueImpl = (n3 = dataInputStream.readInt()) > 0 ? new ParsedValueImpl[n3] : null;
            for (int i2 = 0; i2 < n3; ++i2) {
                byte by2 = dataInputStream.readByte();
                arrparsedValueImpl[i2] = by2 == 1 ? ParsedValueImpl.readBinary(n2, dataInputStream, arrstring) : null;
            }
            return new ParsedValueImpl(arrparsedValueImpl, styleConverter, bl);
        }
        if (by == 3) {
            int n4;
            if (n2 >= 4) {
                dataInputStream.readByte();
            }
            ParsedValueImpl[][] arrparsedValueImpl = (n4 = dataInputStream.readInt()) > 0 ? new ParsedValueImpl[n4][0] : (ParsedValueImpl[][])null;
            for (int i3 = 0; i3 < n4; ++i3) {
                int n5;
                if (n2 >= 4) {
                    dataInputStream.readByte();
                }
                arrparsedValueImpl[i3] = (n5 = dataInputStream.readInt()) > 0 ? new ParsedValueImpl[n5] : null;
                for (int i4 = 0; i4 < n5; ++i4) {
                    byte by3 = dataInputStream.readByte();
                    arrparsedValueImpl[i3][i4] = by3 == 1 ? ParsedValueImpl.readBinary(n2, dataInputStream, arrstring) : null;
                }
            }
            return new ParsedValueImpl(arrparsedValueImpl, styleConverter, bl);
        }
        if (by == 5) {
            double d2 = Double.longBitsToDouble(dataInputStream.readLong());
            double d3 = Double.longBitsToDouble(dataInputStream.readLong());
            double d4 = Double.longBitsToDouble(dataInputStream.readLong());
            double d5 = Double.longBitsToDouble(dataInputStream.readLong());
            return new ParsedValueImpl(Color.color(d2, d3, d4, d5), styleConverter, bl);
        }
        if (by == 6) {
            short s2;
            short s3 = dataInputStream.readShort();
            String string = arrstring[s3];
            if (n2 == 2 && (s2 = dataInputStream.readShort()) >= arrstring.length) {
                throw new IllegalArgumentException("bad version " + n2);
            }
            ParsedValueImpl parsedValueImpl = new ParsedValueImpl(string, styleConverter, bl);
            return parsedValueImpl;
        }
        if (by == 7) {
            Boolean bl3 = dataInputStream.readBoolean();
            return new ParsedValueImpl(bl3, styleConverter, bl);
        }
        if (by == 9) {
            double d6 = Double.longBitsToDouble(dataInputStream.readLong());
            SizeUnits sizeUnits = SizeUnits.PX;
            String string = arrstring[dataInputStream.readShort()];
            try {
                sizeUnits = Enum.valueOf(SizeUnits.class, string);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.err.println(illegalArgumentException.toString());
            }
            catch (NullPointerException nullPointerException) {
                System.err.println(nullPointerException.toString());
            }
            return new ParsedValueImpl(new Size(d6, sizeUnits), styleConverter, bl);
        }
        if (by == 4) {
            String string = arrstring[dataInputStream.readShort()];
            return new ParsedValueImpl(string, styleConverter, bl);
        }
        if (by == 8) {
            String string = arrstring[dataInputStream.readShort()];
            try {
                URL uRL = new URL(string);
                return new ParsedValueImpl(uRL, styleConverter, bl);
            }
            catch (MalformedURLException malformedURLException) {
                throw new InternalError("Excpeption in Value.readBinary: " + malformedURLException);
            }
        }
        if (by == 0) {
            return new ParsedValueImpl(null, styleConverter, bl);
        }
        throw new InternalError("unknown type: " + by);
    }
}

