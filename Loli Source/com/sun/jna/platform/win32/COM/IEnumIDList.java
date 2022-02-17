/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface IEnumIDList {
    public static final Guid.IID IID_IEnumIDList = new Guid.IID("{000214F2-0000-0000-C000-000000000046}");

    public WinNT.HRESULT QueryInterface(Guid.REFIID var1, PointerByReference var2);

    public int AddRef();

    public int Release();

    public WinNT.HRESULT Next(int var1, PointerByReference var2, IntByReference var3);

    public WinNT.HRESULT Skip(int var1);

    public WinNT.HRESULT Reset();

    public WinNT.HRESULT Clone(PointerByReference var1);

    public static class Converter {
        public static IEnumIDList PointerToIEnumIDList(PointerByReference ptr) {
            final Pointer interfacePointer = ptr.getValue();
            Pointer vTablePointer = interfacePointer.getPointer(0L);
            final Pointer[] vTable = new Pointer[7];
            vTablePointer.read(0L, vTable, 0, 7);
            return new IEnumIDList(){

                @Override
                public WinNT.HRESULT QueryInterface(Guid.REFIID byValue, PointerByReference pointerByReference) {
                    Function f2 = Function.getFunction(vTable[0], 63);
                    return new WinNT.HRESULT(f2.invokeInt(new Object[]{interfacePointer, byValue, pointerByReference}));
                }

                @Override
                public int AddRef() {
                    Function f2 = Function.getFunction(vTable[1], 63);
                    return f2.invokeInt(new Object[]{interfacePointer});
                }

                @Override
                public int Release() {
                    Function f2 = Function.getFunction(vTable[2], 63);
                    return f2.invokeInt(new Object[]{interfacePointer});
                }

                @Override
                public WinNT.HRESULT Next(int celt, PointerByReference rgelt, IntByReference pceltFetched) {
                    Function f2 = Function.getFunction(vTable[3], 63);
                    return new WinNT.HRESULT(f2.invokeInt(new Object[]{interfacePointer, celt, rgelt, pceltFetched}));
                }

                @Override
                public WinNT.HRESULT Skip(int celt) {
                    Function f2 = Function.getFunction(vTable[4], 63);
                    return new WinNT.HRESULT(f2.invokeInt(new Object[]{interfacePointer, celt}));
                }

                @Override
                public WinNT.HRESULT Reset() {
                    Function f2 = Function.getFunction(vTable[5], 63);
                    return new WinNT.HRESULT(f2.invokeInt(new Object[]{interfacePointer}));
                }

                @Override
                public WinNT.HRESULT Clone(PointerByReference ppenum) {
                    Function f2 = Function.getFunction(vTable[6], 63);
                    return new WinNT.HRESULT(f2.invokeInt(new Object[]{interfacePointer, ppenum}));
                }
            };
        }
    }
}

