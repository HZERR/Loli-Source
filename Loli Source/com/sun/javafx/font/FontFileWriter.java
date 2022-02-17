/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontConstants;
import com.sun.javafx.font.PrismFontFactory;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class FontFileWriter
implements FontConstants {
    byte[] header;
    int pos;
    int headerPos;
    int writtenBytes;
    FontTracker tracker;
    File file;
    RandomAccessFile raFile;

    public FontFileWriter() {
        if (!FontFileWriter.hasTempPermission()) {
            this.tracker = FontTracker.getTracker();
        }
    }

    protected void setLength(int n2) throws IOException {
        if (this.raFile == null) {
            throw new IOException("File not open");
        }
        this.checkTracker(n2);
        this.raFile.setLength(n2);
    }

    public void seek(int n2) throws IOException {
        if (this.raFile == null) {
            throw new IOException("File not open");
        }
        if (n2 != this.pos) {
            this.raFile.seek(n2);
            this.pos = n2;
        }
    }

    public File getFile() {
        return this.file;
    }

    public File openFile() throws PrivilegedActionException {
        this.pos = 0;
        this.writtenBytes = 0;
        this.file = AccessController.doPrivileged(() -> {
            try {
                return Files.createTempFile("+JXF", ".tmp", new FileAttribute[0]).toFile();
            }
            catch (IOException iOException) {
                throw new IOException("Unable to create temporary file");
            }
        });
        if (this.tracker != null) {
            this.tracker.add(this.file);
        }
        this.raFile = AccessController.doPrivileged(() -> new RandomAccessFile(this.file, "rw"));
        if (this.tracker != null) {
            this.tracker.set(this.file, this.raFile);
        }
        if (PrismFontFactory.debugFonts) {
            System.err.println("Temp file created: " + this.file.getPath());
        }
        return this.file;
    }

    public void closeFile() throws IOException {
        if (this.header != null) {
            this.raFile.seek(0L);
            this.raFile.write(this.header);
            this.header = null;
        }
        if (this.raFile != null) {
            this.raFile.close();
            this.raFile = null;
        }
        if (this.tracker != null) {
            this.tracker.remove(this.file);
        }
    }

    public void deleteFile() {
        if (this.file != null) {
            if (this.tracker != null) {
                this.tracker.subBytes(this.writtenBytes);
            }
            try {
                this.closeFile();
            }
            catch (Exception exception) {
                // empty catch block
            }
            try {
                AccessController.doPrivileged(() -> {
                    this.file.delete();
                    return null;
                });
                if (PrismFontFactory.debugFonts) {
                    System.err.println("Temp file delete: " + this.file.getPath());
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.file = null;
            this.raFile = null;
        }
    }

    public boolean isTracking() {
        return this.tracker != null;
    }

    private void checkTracker(int n2) throws IOException {
        if (this.tracker != null) {
            if (n2 < 0 || this.pos > 0x2000000 - n2) {
                throw new IOException("File too big.");
            }
            if (this.tracker.getNumBytes() > 0x14000000 - n2) {
                throw new IOException("Total files too big.");
            }
        }
    }

    private void checkSize(int n2) throws IOException {
        if (this.tracker != null) {
            this.checkTracker(n2);
            this.tracker.addBytes(n2);
            this.writtenBytes += n2;
        }
    }

    private void setHeaderPos(int n2) {
        this.headerPos = n2;
    }

    public void writeHeader(int n2, short s2) throws IOException {
        int n3 = 12 + 16 * s2;
        this.checkSize(n3);
        this.header = new byte[n3];
        short s3 = s2;
        s3 = (short)(s3 | s3 >> 1);
        s3 = (short)(s3 | s3 >> 2);
        s3 = (short)(s3 | s3 >> 4);
        s3 = (short)(s3 | s3 >> 8);
        short s4 = (short)(s3 * 16);
        short s5 = 0;
        for (s3 = (short)(s3 & (s3 >> 1 ^ 0xFFFFFFFF)); s3 > 1; s3 = (short)(s3 >> 1)) {
            s5 = (short)(s5 + 1);
        }
        short s6 = (short)(s2 * 16 - s4);
        this.setHeaderPos(0);
        this.writeInt(n2);
        this.writeShort(s2);
        this.writeShort(s4);
        this.writeShort(s5);
        this.writeShort(s6);
    }

    public void writeDirectoryEntry(int n2, int n3, int n4, int n5, int n6) throws IOException {
        this.setHeaderPos(12 + 16 * n2);
        this.writeInt(n3);
        this.writeInt(n4);
        this.writeInt(n5);
        this.writeInt(n6);
    }

    private void writeInt(int n2) throws IOException {
        this.header[this.headerPos++] = (byte)((n2 & 0xFF000000) >> 24);
        this.header[this.headerPos++] = (byte)((n2 & 0xFF0000) >> 16);
        this.header[this.headerPos++] = (byte)((n2 & 0xFF00) >> 8);
        this.header[this.headerPos++] = (byte)(n2 & 0xFF);
    }

    private void writeShort(short s2) throws IOException {
        this.header[this.headerPos++] = (byte)((s2 & 0xFF00) >> 8);
        this.header[this.headerPos++] = (byte)(s2 & 0xFF);
    }

    public void writeBytes(byte[] arrby) throws IOException {
        this.writeBytes(arrby, 0, arrby.length);
    }

    public void writeBytes(byte[] arrby, int n2, int n3) throws IOException {
        this.checkSize(n3);
        this.raFile.write(arrby, n2, n3);
        this.pos += n3;
    }

    static boolean hasTempPermission() {
        if (System.getSecurityManager() == null) {
            return true;
        }
        File file = null;
        boolean bl = false;
        try {
            file = Files.createTempFile("+JXF", ".tmp", new FileAttribute[0]).toFile();
            file.delete();
            file = null;
            bl = true;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return bl;
    }

    static class FontTracker {
        public static final int MAX_FILE_SIZE = 0x2000000;
        public static final int MAX_TOTAL_BYTES = 0x14000000;
        static int numBytes;
        static FontTracker tracker;
        private static Semaphore cs;

        FontTracker() {
        }

        public static synchronized FontTracker getTracker() {
            if (tracker == null) {
                tracker = new FontTracker();
            }
            return tracker;
        }

        public synchronized int getNumBytes() {
            return numBytes;
        }

        public synchronized void addBytes(int n2) {
            numBytes += n2;
        }

        public synchronized void subBytes(int n2) {
            numBytes -= n2;
        }

        private static synchronized Semaphore getCS() {
            if (cs == null) {
                cs = new Semaphore(5, true);
            }
            return cs;
        }

        public boolean acquirePermit() throws InterruptedException {
            return FontTracker.getCS().tryAcquire(120L, TimeUnit.SECONDS);
        }

        public void releasePermit() {
            FontTracker.getCS().release();
        }

        public void add(File file) {
            TempFileDeletionHook.add(file);
        }

        public void set(File file, RandomAccessFile randomAccessFile) {
            TempFileDeletionHook.set(file, randomAccessFile);
        }

        public void remove(File file) {
            TempFileDeletionHook.remove(file);
        }

        static {
            cs = null;
        }

        private static class TempFileDeletionHook {
            private static HashMap<File, RandomAccessFile> files = new HashMap();
            private static Thread t = null;

            static void init() {
                if (t == null) {
                    AccessController.doPrivileged(() -> {
                        t = new Thread(() -> TempFileDeletionHook.runHooks());
                        Runtime.getRuntime().addShutdownHook(t);
                        return null;
                    });
                }
            }

            private TempFileDeletionHook() {
            }

            static synchronized void add(File file) {
                TempFileDeletionHook.init();
                files.put(file, null);
            }

            static synchronized void set(File file, RandomAccessFile randomAccessFile) {
                files.put(file, randomAccessFile);
            }

            static synchronized void remove(File file) {
                files.remove(file);
            }

            static synchronized void runHooks() {
                if (files.isEmpty()) {
                    return;
                }
                for (Map.Entry<File, RandomAccessFile> entry : files.entrySet()) {
                    try {
                        if (entry.getValue() != null) {
                            entry.getValue().close();
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    entry.getKey().delete();
                }
            }
        }
    }
}

