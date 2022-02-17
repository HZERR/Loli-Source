/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.ptr.ByReference;

public class O0iIl1ilaND
extends ByReference {
    public O0iIl1ilaND() {
        this(new LibCAPI.size_t());
    }

    public O0iIl1ilaND(LibCAPI.size_t size_t2) {
        super(Native.SIZE_T_SIZE);
        this.setValue(size_t2);
    }

    public void setValue(LibCAPI.size_t size_t2) {
        if (Native.SIZE_T_SIZE > 4) {
            this.getPointer().setLong(0L, size_t2.longValue());
        } else {
            this.getPointer().setInt(0L, size_t2.intValue());
        }
    }

    public LibCAPI.size_t getValue() {
        return new LibCAPI.size_t(Native.SIZE_T_SIZE > 4 ? this.getPointer().getLong(0L) : (long)this.getPointer().getInt(0L));
    }

    @Override
    public String toString() {
        if (Native.SIZE_T_SIZE > 4) {
            return String.format("size_t@0x1$%x=0x%2$x (%2$d)", Pointer.nativeValue(this.getPointer()), this.getValue().longValue());
        }
        return String.format("size_t@0x1$%x=0x%2$x (%2$d)", Pointer.nativeValue(this.getPointer()), this.getValue().intValue());
    }
}

