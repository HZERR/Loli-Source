/*
 * Decompiled with CFR 0.150.
 */
package org.java_websocket.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidEncodingException;

public class Charsetfunctions {
    private static final CodingErrorAction codingErrorAction = CodingErrorAction.REPORT;
    private static final int[] utf8d = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 11, 6, 6, 6, 5, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 0, 1, 2, 3, 5, 8, 7, 1, 1, 1, 4, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    private Charsetfunctions() {
    }

    public static byte[] utf8Bytes(String s2) {
        try {
            return s2.getBytes("UTF8");
        }
        catch (UnsupportedEncodingException e2) {
            throw new InvalidEncodingException(e2);
        }
    }

    public static byte[] asciiBytes(String s2) {
        try {
            return s2.getBytes("ASCII");
        }
        catch (UnsupportedEncodingException e2) {
            throw new InvalidEncodingException(e2);
        }
    }

    public static String stringAscii(byte[] bytes) {
        return Charsetfunctions.stringAscii(bytes, 0, bytes.length);
    }

    public static String stringAscii(byte[] bytes, int offset, int length) {
        try {
            return new String(bytes, offset, length, "ASCII");
        }
        catch (UnsupportedEncodingException e2) {
            throw new InvalidEncodingException(e2);
        }
    }

    public static String stringUtf8(byte[] bytes) throws InvalidDataException {
        return Charsetfunctions.stringUtf8(ByteBuffer.wrap(bytes));
    }

    public static String stringUtf8(ByteBuffer bytes) throws InvalidDataException {
        String s2;
        CharsetDecoder decode = Charset.forName("UTF8").newDecoder();
        decode.onMalformedInput(codingErrorAction);
        decode.onUnmappableCharacter(codingErrorAction);
        try {
            bytes.mark();
            s2 = decode.decode(bytes).toString();
            bytes.reset();
        }
        catch (CharacterCodingException e2) {
            throw new InvalidDataException(1007, (Throwable)e2);
        }
        return s2;
    }

    public static boolean isValidUTF8(ByteBuffer data, int off) {
        int len = data.remaining();
        if (len < off) {
            return false;
        }
        int state = 0;
        for (int i2 = off; i2 < len; ++i2) {
            if ((state = utf8d[256 + (state << 4) + utf8d[0xFF & data.get(i2)]]) != 1) continue;
            return false;
        }
        return true;
    }

    public static boolean isValidUTF8(ByteBuffer data) {
        return Charsetfunctions.isValidUTF8(data, 0);
    }
}

