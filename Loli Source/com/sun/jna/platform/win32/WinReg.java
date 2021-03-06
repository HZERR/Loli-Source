/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.ByReference;

public interface WinReg {
    public static final HKEY HKEY_CLASSES_ROOT = new HKEY(Integer.MIN_VALUE);
    public static final HKEY HKEY_CURRENT_USER = new HKEY(-2147483647);
    public static final HKEY HKEY_LOCAL_MACHINE = new HKEY(-2147483646);
    public static final HKEY HKEY_USERS = new HKEY(-2147483645);
    public static final HKEY HKEY_PERFORMANCE_DATA = new HKEY(-2147483644);
    public static final HKEY HKEY_PERFORMANCE_TEXT = new HKEY(-2147483568);
    public static final HKEY HKEY_PERFORMANCE_NLSTEXT = new HKEY(-2147483552);
    public static final HKEY HKEY_CURRENT_CONFIG = new HKEY(-2147483643);
    public static final HKEY HKEY_DYN_DATA = new HKEY(-2147483642);

    public static class HKEYByReference
    extends ByReference {
        public HKEYByReference() {
            this((HKEY)null);
        }

        public HKEYByReference(HKEY h2) {
            super(Native.POINTER_SIZE);
            this.setValue(h2);
        }

        public void setValue(HKEY h2) {
            this.getPointer().setPointer(0L, h2 != null ? h2.getPointer() : null);
        }

        public HKEY getValue() {
            Pointer p2 = this.getPointer().getPointer(0L);
            if (p2 == null) {
                return null;
            }
            if (WinBase.INVALID_HANDLE_VALUE.getPointer().equals(p2)) {
                return (HKEY)WinBase.INVALID_HANDLE_VALUE;
            }
            HKEY h2 = new HKEY();
            h2.setPointer(p2);
            return h2;
        }
    }

    public static class HKEY
    extends WinNT.HANDLE {
        public HKEY() {
        }

        public HKEY(Pointer p2) {
            super(p2);
        }

        public HKEY(int value) {
            super(new Pointer(value));
        }
    }
}

