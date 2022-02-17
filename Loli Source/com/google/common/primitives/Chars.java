/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible(emulated=true)
public final class Chars {
    public static final int BYTES = 2;

    private Chars() {
    }

    public static int hashCode(char value) {
        return value;
    }

    public static char checkedCast(long value) {
        char result = (char)value;
        if ((long)result != value) {
            throw new IllegalArgumentException("Out of range: " + value);
        }
        return result;
    }

    public static char saturatedCast(long value) {
        if (value > 65535L) {
            return '\uffff';
        }
        if (value < 0L) {
            return '\u0000';
        }
        return (char)value;
    }

    public static int compare(char a2, char b2) {
        return a2 - b2;
    }

    public static boolean contains(char[] array, char target) {
        for (char value : array) {
            if (value != target) continue;
            return true;
        }
        return false;
    }

    public static int indexOf(char[] array, char target) {
        return Chars.indexOf(array, target, 0, array.length);
    }

    private static int indexOf(char[] array, char target, int start, int end) {
        for (int i2 = start; i2 < end; ++i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static int indexOf(char[] array, char[] target) {
        Preconditions.checkNotNull(array, "array");
        Preconditions.checkNotNull(target, "target");
        if (target.length == 0) {
            return 0;
        }
        block0: for (int i2 = 0; i2 < array.length - target.length + 1; ++i2) {
            for (int j2 = 0; j2 < target.length; ++j2) {
                if (array[i2 + j2] != target[j2]) continue block0;
            }
            return i2;
        }
        return -1;
    }

    public static int lastIndexOf(char[] array, char target) {
        return Chars.lastIndexOf(array, target, 0, array.length);
    }

    private static int lastIndexOf(char[] array, char target, int start, int end) {
        for (int i2 = end - 1; i2 >= start; --i2) {
            if (array[i2] != target) continue;
            return i2;
        }
        return -1;
    }

    public static char min(char ... array) {
        Preconditions.checkArgument(array.length > 0);
        char min = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            if (array[i2] >= min) continue;
            min = array[i2];
        }
        return min;
    }

    public static char max(char ... array) {
        Preconditions.checkArgument(array.length > 0);
        char max = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            if (array[i2] <= max) continue;
            max = array[i2];
        }
        return max;
    }

    public static char[] concat(char[] ... arrays) {
        int length = 0;
        for (char[] array : arrays) {
            length += array.length;
        }
        char[] result = new char[length];
        int pos = 0;
        for (char[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    @GwtIncompatible(value="doesn't work")
    public static byte[] toByteArray(char value) {
        return new byte[]{(byte)(value >> 8), (byte)value};
    }

    @GwtIncompatible(value="doesn't work")
    public static char fromByteArray(byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 2, "array too small: %s < %s", bytes.length, 2);
        return Chars.fromBytes(bytes[0], bytes[1]);
    }

    @GwtIncompatible(value="doesn't work")
    public static char fromBytes(byte b1, byte b2) {
        return (char)(b1 << 8 | b2 & 0xFF);
    }

    public static char[] ensureCapacity(char[] array, int minLength, int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return array.length < minLength ? Chars.copyOf(array, minLength + padding) : array;
    }

    private static char[] copyOf(char[] original, int length) {
        char[] copy = new char[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }

    public static String join(String separator, char ... array) {
        Preconditions.checkNotNull(separator);
        int len = array.length;
        if (len == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(len + separator.length() * (len - 1));
        builder.append(array[0]);
        for (int i2 = 1; i2 < len; ++i2) {
            builder.append(separator).append(array[i2]);
        }
        return builder.toString();
    }

    public static Comparator<char[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static char[] toArray(Collection<Character> collection) {
        if (collection instanceof CharArrayAsList) {
            return ((CharArrayAsList)collection).toCharArray();
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        char[] array = new char[len];
        for (int i2 = 0; i2 < len; ++i2) {
            array[i2] = ((Character)Preconditions.checkNotNull(boxedArray[i2])).charValue();
        }
        return array;
    }

    public static List<Character> asList(char ... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new CharArrayAsList(backingArray);
    }

    @GwtCompatible
    private static class CharArrayAsList
    extends AbstractList<Character>
    implements RandomAccess,
    Serializable {
        final char[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;

        CharArrayAsList(char[] array) {
            this(array, 0, array.length);
        }

        CharArrayAsList(char[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public int size() {
            return this.end - this.start;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Character get(int index) {
            Preconditions.checkElementIndex(index, this.size());
            return Character.valueOf(this.array[this.start + index]);
        }

        @Override
        public boolean contains(Object target) {
            return target instanceof Character && Chars.indexOf(this.array, ((Character)target).charValue(), this.start, this.end) != -1;
        }

        @Override
        public int indexOf(Object target) {
            int i2;
            if (target instanceof Character && (i2 = Chars.indexOf(this.array, ((Character)target).charValue(), this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object target) {
            int i2;
            if (target instanceof Character && (i2 = Chars.lastIndexOf(this.array, ((Character)target).charValue(), this.start, this.end)) >= 0) {
                return i2 - this.start;
            }
            return -1;
        }

        @Override
        public Character set(int index, Character element) {
            Preconditions.checkElementIndex(index, this.size());
            char oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element).charValue();
            return Character.valueOf(oldValue);
        }

        @Override
        public List<Character> subList(int fromIndex, int toIndex) {
            int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new CharArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof CharArrayAsList) {
                CharArrayAsList that = (CharArrayAsList)object;
                int size = this.size();
                if (that.size() != size) {
                    return false;
                }
                for (int i2 = 0; i2 < size; ++i2) {
                    if (this.array[this.start + i2] == that.array[that.start + i2]) continue;
                    return false;
                }
                return true;
            }
            return super.equals(object);
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (int i2 = this.start; i2 < this.end; ++i2) {
                result = 31 * result + Chars.hashCode(this.array[i2]);
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(this.size() * 3);
            builder.append('[').append(this.array[this.start]);
            for (int i2 = this.start + 1; i2 < this.end; ++i2) {
                builder.append(", ").append(this.array[i2]);
            }
            return builder.append(']').toString();
        }

        char[] toCharArray() {
            int size = this.size();
            char[] result = new char[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }

    private static enum LexicographicalComparator implements Comparator<char[]>
    {
        INSTANCE;


        @Override
        public int compare(char[] left, char[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i2 = 0; i2 < minLength; ++i2) {
                int result = Chars.compare(left[i2], right[i2]);
                if (result == 0) continue;
                return result;
            }
            return left.length - right.length;
        }
    }
}

