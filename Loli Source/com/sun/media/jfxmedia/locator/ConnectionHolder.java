/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.locator.HLSConnectionHolder;
import com.sun.media.jfxmedia.locator.Locator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import sun.nio.ch.DirectBuffer;

public abstract class ConnectionHolder {
    private static int DEFAULT_BUFFER_SIZE = 4096;
    ReadableByteChannel channel;
    ByteBuffer buffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);

    static ConnectionHolder createMemoryConnectionHolder(ByteBuffer byteBuffer) {
        return new MemoryConnectionHolder(byteBuffer);
    }

    static ConnectionHolder createURIConnectionHolder(URI uRI, Map<String, Object> map) throws IOException {
        return new URIConnectionHolder(uRI, map);
    }

    static ConnectionHolder createFileConnectionHolder(URI uRI) throws IOException {
        return new FileConnectionHolder(uRI);
    }

    static ConnectionHolder createHLSConnectionHolder(URI uRI) throws IOException {
        return new HLSConnectionHolder(uRI);
    }

    public int readNextBlock() throws IOException {
        this.buffer.rewind();
        if (this.buffer.limit() < this.buffer.capacity()) {
            this.buffer.limit(this.buffer.capacity());
        }
        if (null == this.channel) {
            throw new ClosedChannelException();
        }
        return this.channel.read(this.buffer);
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    abstract int readBlock(long var1, int var3) throws IOException;

    abstract boolean needBuffer();

    abstract boolean isSeekable();

    abstract boolean isRandomAccess();

    public abstract long seek(long var1);

    public void closeConnection() {
        try {
            if (this.channel != null) {
                this.channel.close();
            }
        }
        catch (IOException iOException) {
        }
        finally {
            this.channel = null;
        }
    }

    int property(int n2, int n3) {
        return 0;
    }

    int getStreamSize() {
        return -1;
    }

    private static class MemoryConnectionHolder
    extends ConnectionHolder {
        private final ByteBuffer backingBuffer;

        public MemoryConnectionHolder(ByteBuffer byteBuffer) {
            if (null == byteBuffer) {
                throw new IllegalArgumentException("Can't connect to null buffer...");
            }
            if (byteBuffer.isDirect()) {
                this.backingBuffer = byteBuffer.duplicate();
            } else {
                this.backingBuffer = ByteBuffer.allocateDirect(byteBuffer.capacity());
                this.backingBuffer.put(byteBuffer);
            }
            this.backingBuffer.rewind();
            this.channel = new ReadableByteChannel(){

                @Override
                public int read(ByteBuffer byteBuffer) throws IOException {
                    int n2;
                    if (backingBuffer.remaining() <= 0) {
                        return -1;
                    }
                    if (byteBuffer.equals(buffer)) {
                        n2 = Math.min(DEFAULT_BUFFER_SIZE, backingBuffer.remaining());
                        if (n2 > 0) {
                            buffer = backingBuffer.slice();
                            buffer.limit(n2);
                        }
                    } else {
                        n2 = Math.min(byteBuffer.remaining(), backingBuffer.remaining());
                        if (n2 > 0) {
                            backingBuffer.limit(backingBuffer.position() + n2);
                            byteBuffer.put(backingBuffer);
                            backingBuffer.limit(backingBuffer.capacity());
                        }
                    }
                    return n2;
                }

                @Override
                public boolean isOpen() {
                    return true;
                }

                @Override
                public void close() throws IOException {
                }
            };
        }

        @Override
        int readBlock(long l2, int n2) throws IOException {
            if (null == this.channel) {
                throw new ClosedChannelException();
            }
            if ((int)l2 > this.backingBuffer.capacity()) {
                return -1;
            }
            this.backingBuffer.position((int)l2);
            this.buffer = this.backingBuffer.slice();
            int n3 = Math.min(this.backingBuffer.remaining(), n2);
            this.buffer.limit(n3);
            this.backingBuffer.position(this.backingBuffer.position() + n3);
            return n3;
        }

        @Override
        boolean needBuffer() {
            return false;
        }

        @Override
        boolean isSeekable() {
            return true;
        }

        @Override
        boolean isRandomAccess() {
            return true;
        }

        @Override
        public long seek(long l2) {
            if ((int)l2 < this.backingBuffer.capacity()) {
                this.backingBuffer.limit(this.backingBuffer.capacity());
                this.backingBuffer.position((int)l2);
                return l2;
            }
            return -1L;
        }

        @Override
        public void closeConnection() {
            this.channel = null;
        }
    }

    private static class URIConnectionHolder
    extends ConnectionHolder {
        private URI uri;
        private URLConnection urlConnection;

        URIConnectionHolder(URI uRI, Map<String, Object> map) throws IOException {
            this.uri = uRI;
            this.urlConnection = uRI.toURL().openConnection();
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Object object = entry.getValue();
                    if (!(object instanceof String)) continue;
                    this.urlConnection.setRequestProperty(entry.getKey(), (String)object);
                }
            }
            this.channel = this.openChannel(null);
        }

        @Override
        boolean needBuffer() {
            String string = this.uri.getScheme().toLowerCase();
            return "http".equals(string) || "https".equals(string);
        }

        @Override
        boolean isSeekable() {
            return this.urlConnection instanceof HttpURLConnection || this.urlConnection instanceof JarURLConnection;
        }

        @Override
        boolean isRandomAccess() {
            return false;
        }

        @Override
        int readBlock(long l2, int n2) throws IOException {
            throw new IOException();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public long seek(long l2) {
            if (this.urlConnection instanceof HttpURLConnection) {
                URLConnection uRLConnection = null;
                try {
                    uRLConnection = this.uri.toURL().openConnection();
                    HttpURLConnection httpURLConnection = (HttpURLConnection)uRLConnection;
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestProperty("Range", "bytes=" + l2 + "-");
                    if (httpURLConnection.getResponseCode() == 206) {
                        this.closeConnection();
                        this.urlConnection = uRLConnection;
                        uRLConnection = null;
                        this.channel = this.openChannel(null);
                        long l3 = l2;
                        return l3;
                    }
                    long l4 = -1L;
                    return l4;
                }
                catch (IOException iOException) {
                    long l5 = -1L;
                    return l5;
                }
                finally {
                    if (uRLConnection != null) {
                        Locator.closeConnection(uRLConnection);
                    }
                }
            }
            if (this.urlConnection instanceof JarURLConnection) {
                try {
                    long l6;
                    this.closeConnection();
                    this.urlConnection = this.uri.toURL().openConnection();
                    long l7 = l2;
                    InputStream inputStream = this.urlConnection.getInputStream();
                    while ((l7 -= (l6 = inputStream.skip(l7))) > 0L) {
                    }
                    this.channel = this.openChannel(inputStream);
                    return l2;
                }
                catch (IOException iOException) {
                    return -1L;
                }
            }
            return -1L;
        }

        @Override
        public void closeConnection() {
            super.closeConnection();
            Locator.closeConnection(this.urlConnection);
            this.urlConnection = null;
        }

        private ReadableByteChannel openChannel(InputStream inputStream) throws IOException {
            return inputStream == null ? Channels.newChannel(this.urlConnection.getInputStream()) : Channels.newChannel(inputStream);
        }
    }

    private static class FileConnectionHolder
    extends ConnectionHolder {
        private RandomAccessFile file = null;

        FileConnectionHolder(URI uRI) throws IOException {
            this.channel = this.openFile(uRI);
        }

        @Override
        boolean needBuffer() {
            return false;
        }

        @Override
        boolean isRandomAccess() {
            return true;
        }

        @Override
        boolean isSeekable() {
            return true;
        }

        @Override
        public long seek(long l2) {
            try {
                ((FileChannel)this.channel).position(l2);
                return l2;
            }
            catch (IOException iOException) {
                return -1L;
            }
        }

        @Override
        int readBlock(long l2, int n2) throws IOException {
            if (null == this.channel) {
                throw new ClosedChannelException();
            }
            if (this.buffer.capacity() < n2) {
                this.buffer = ByteBuffer.allocateDirect(n2);
            }
            this.buffer.rewind().limit(n2);
            return ((FileChannel)this.channel).read(this.buffer, l2);
        }

        private ReadableByteChannel openFile(URI uRI) throws IOException {
            if (this.file != null) {
                this.file.close();
            }
            this.file = new RandomAccessFile(new File(uRI), "r");
            return this.file.getChannel();
        }

        @Override
        public void closeConnection() {
            super.closeConnection();
            if (this.file != null) {
                try {
                    this.file.close();
                }
                catch (IOException iOException) {
                }
                finally {
                    this.file = null;
                }
            }
            if (this.buffer instanceof DirectBuffer) {
                ((DirectBuffer)((Object)this.buffer)).cleaner().clean();
            }
        }
    }
}

