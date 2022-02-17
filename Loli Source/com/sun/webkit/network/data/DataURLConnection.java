/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.LinkedList;

final class DataURLConnection
extends URLConnection {
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private final String mediaType;
    private final byte[] data;
    private final InputStream inputStream;

    DataURLConnection(URL uRL) throws IOException {
        super(uRL);
        Object object;
        String string = uRL.toString();
        string = string.substring(string.indexOf(58) + 1);
        int n2 = string.indexOf(44);
        if (n2 < 0) {
            throw new ProtocolException("Invalid URL, ',' not found in: " + this.getURL());
        }
        String string2 = string.substring(0, n2);
        String string3 = string.substring(n2 + 1);
        String string4 = null;
        LinkedList<String> linkedList = new LinkedList<String>();
        Charset charset = null;
        boolean bl = false;
        String[] arrstring = string2.split(";", -1);
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            object = arrstring[i2];
            if (((String)object).equalsIgnoreCase("base64")) {
                bl = true;
                continue;
            }
            if (i2 == 0 && !((String)object).contains("=")) {
                string4 = object;
                continue;
            }
            linkedList.add((String)object);
            if (!((String)object).toLowerCase().startsWith("charset=")) continue;
            try {
                charset = Charset.forName(((String)object).substring(8));
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                UnsupportedEncodingException unsupportedEncodingException = new UnsupportedEncodingException();
                unsupportedEncodingException.initCause(illegalArgumentException);
                throw unsupportedEncodingException;
            }
        }
        if (string4 == null || string4.isEmpty()) {
            string4 = "text/plain";
        }
        if (charset == null) {
            charset = US_ASCII;
            if (string4.toLowerCase().startsWith("text/")) {
                linkedList.addFirst("charset=" + charset.name());
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string4);
        for (String string5 : linkedList) {
            stringBuilder.append(';').append(string5);
        }
        this.mediaType = stringBuilder.toString();
        if (bl) {
            object = DataURLConnection.urlDecode(string3, US_ASCII);
            object = ((String)object).replaceAll("\\s+", "");
            this.data = Base64.getMimeDecoder().decode((String)object);
        } else {
            object = DataURLConnection.urlDecode(string3, charset);
            this.data = ((String)object).getBytes(charset);
        }
        this.inputStream = new ByteArrayInputStream(this.data);
    }

    @Override
    public void connect() {
        this.connected = true;
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override
    public String getContentType() {
        return this.mediaType;
    }

    @Override
    public String getContentEncoding() {
        return null;
    }

    @Override
    public int getContentLength() {
        return this.data != null ? this.data.length : -1;
    }

    private static String urlDecode(String string, Charset charset) {
        int n2 = string.length();
        StringBuilder stringBuilder = new StringBuilder(n2);
        byte[] arrby = null;
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (c2 == '%') {
                if (arrby == null) {
                    arrby = new byte[(n2 - n3) / 3];
                }
                int n4 = 0;
                int n5 = n3;
                while (n3 < n2 && (c2 = string.charAt(n3)) == '%') {
                    byte by;
                    if (n3 + 2 >= n2) {
                        n5 = n2;
                        break;
                    }
                    try {
                        by = (byte)Integer.parseInt(string.substring(n3 + 1, n3 + 3), 16);
                    }
                    catch (NumberFormatException numberFormatException) {
                        n5 = n3 + 3;
                        break;
                    }
                    arrby[n4++] = by;
                    n3 += 3;
                }
                if (n4 > 0) {
                    stringBuilder.append(new String(arrby, 0, n4, charset));
                }
                while (n3 < n5) {
                    stringBuilder.append(string.charAt(n3++));
                }
                continue;
            }
            stringBuilder.append(c2);
            ++n3;
        }
        return stringBuilder.toString();
    }
}

