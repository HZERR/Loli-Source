/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

class BufferFactory {
    static final int SIZEOF_BYTE = 1;
    static final int SIZEOF_SHORT = 2;
    static final int SIZEOF_CHAR = 2;
    static final int SIZEOF_INT = 4;
    static final int SIZEOF_FLOAT = 4;
    static final int SIZEOF_LONG = 8;
    static final int SIZEOF_DOUBLE = 8;
    private static final boolean isLittleEndian;

    BufferFactory() {
    }

    static boolean isLittleEndian() {
        return isLittleEndian;
    }

    static ByteBuffer newDirectByteBuffer(int n2) {
        return BufferFactory.nativeOrder(ByteBuffer.allocateDirect(n2));
    }

    static ByteBuffer nativeOrder(ByteBuffer byteBuffer) {
        return byteBuffer.order(ByteOrder.nativeOrder());
    }

    static boolean isDirect(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof ByteBuffer) {
            return ((ByteBuffer)object).isDirect();
        }
        if (object instanceof FloatBuffer) {
            return ((FloatBuffer)object).isDirect();
        }
        if (object instanceof DoubleBuffer) {
            return ((DoubleBuffer)object).isDirect();
        }
        if (object instanceof CharBuffer) {
            return ((CharBuffer)object).isDirect();
        }
        if (object instanceof ShortBuffer) {
            return ((ShortBuffer)object).isDirect();
        }
        if (object instanceof IntBuffer) {
            return ((IntBuffer)object).isDirect();
        }
        if (object instanceof LongBuffer) {
            return ((LongBuffer)object).isDirect();
        }
        throw new RuntimeException("Unexpected buffer type " + object.getClass().getName());
    }

    static int getDirectBufferByteOffset(Object object) {
        if (object == null) {
            return 0;
        }
        if (object instanceof Buffer) {
            int n2 = ((Buffer)object).position();
            if (object instanceof ByteBuffer) {
                return n2;
            }
            if (object instanceof FloatBuffer) {
                return n2 * 4;
            }
            if (object instanceof IntBuffer) {
                return n2 * 4;
            }
            if (object instanceof ShortBuffer) {
                return n2 * 2;
            }
            if (object instanceof DoubleBuffer) {
                return n2 * 8;
            }
            if (object instanceof LongBuffer) {
                return n2 * 8;
            }
            if (object instanceof CharBuffer) {
                return n2 * 2;
            }
        }
        throw new RuntimeException("Disallowed array backing store type in buffer " + object.getClass().getName());
    }

    static Object getArray(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof ByteBuffer) {
            return ((ByteBuffer)object).array();
        }
        if (object instanceof FloatBuffer) {
            return ((FloatBuffer)object).array();
        }
        if (object instanceof IntBuffer) {
            return ((IntBuffer)object).array();
        }
        if (object instanceof ShortBuffer) {
            return ((ShortBuffer)object).array();
        }
        if (object instanceof DoubleBuffer) {
            return ((DoubleBuffer)object).array();
        }
        if (object instanceof LongBuffer) {
            return ((LongBuffer)object).array();
        }
        if (object instanceof CharBuffer) {
            return ((CharBuffer)object).array();
        }
        throw new RuntimeException("Disallowed array backing store type in buffer " + object.getClass().getName());
    }

    static int getIndirectBufferByteOffset(Object object) {
        if (object == null) {
            return 0;
        }
        if (object instanceof Buffer) {
            int n2 = ((Buffer)object).position();
            if (object instanceof ByteBuffer) {
                return ((ByteBuffer)object).arrayOffset() + n2;
            }
            if (object instanceof FloatBuffer) {
                return 4 * (((FloatBuffer)object).arrayOffset() + n2);
            }
            if (object instanceof IntBuffer) {
                return 4 * (((IntBuffer)object).arrayOffset() + n2);
            }
            if (object instanceof ShortBuffer) {
                return 2 * (((ShortBuffer)object).arrayOffset() + n2);
            }
            if (object instanceof DoubleBuffer) {
                return 8 * (((DoubleBuffer)object).arrayOffset() + n2);
            }
            if (object instanceof LongBuffer) {
                return 8 * (((LongBuffer)object).arrayOffset() + n2);
            }
            if (object instanceof CharBuffer) {
                return 2 * (((CharBuffer)object).arrayOffset() + n2);
            }
        }
        throw new RuntimeException("Unknown buffer type " + object.getClass().getName());
    }

    static {
        ByteBuffer byteBuffer = BufferFactory.newDirectByteBuffer(4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        intBuffer.put(0, 168496141);
        isLittleEndian = 3085 == shortBuffer.get(0);
    }
}

