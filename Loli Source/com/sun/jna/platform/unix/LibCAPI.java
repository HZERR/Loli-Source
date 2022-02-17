/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.unix;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.Reboot;
import com.sun.jna.platform.unix.Resource;

public interface LibCAPI
extends Reboot,
Resource {
    public static final int HOST_NAME_MAX = 255;

    public int getuid();

    public int geteuid();

    public int getgid();

    public int getegid();

    public int setuid(int var1);

    public int seteuid(int var1);

    public int setgid(int var1);

    public int setegid(int var1);

    public int gethostname(byte[] var1, int var2);

    public int sethostname(String var1, int var2);

    public int getdomainname(byte[] var1, int var2);

    public int setdomainname(String var1, int var2);

    public String getenv(String var1);

    public int setenv(String var1, String var2, int var3);

    public int unsetenv(String var1);

    public int getloadavg(double[] var1, int var2);

    public int close(int var1);

    public int msync(Pointer var1, size_t var2, int var3);

    public int munmap(Pointer var1, size_t var2);

    public static class ssize_t
    extends IntegerType {
        public static final ssize_t ZERO = new ssize_t();
        private static final long serialVersionUID = 1L;

        public ssize_t() {
            this(0L);
        }

        public ssize_t(long value) {
            super(Native.SIZE_T_SIZE, value, false);
        }
    }

    public static class size_t
    extends IntegerType {
        public static final size_t ZERO = new size_t();
        private static final long serialVersionUID = 1L;

        public size_t() {
            this(0L);
        }

        public size_t(long value) {
            super(Native.SIZE_T_SIZE, value, true);
        }
    }
}

