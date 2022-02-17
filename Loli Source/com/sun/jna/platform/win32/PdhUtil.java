/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Pdh;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import java.util.List;

public abstract class PdhUtil {
    private static final int CHAR_TO_BYTES = Boolean.getBoolean("w32.ascii") ? 1 : Native.WCHAR_SIZE;
    private static final String ENGLISH_COUNTER_KEY = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Perflib\\009";
    private static final String ENGLISH_COUNTER_VALUE = "Counter";

    public static String PdhLookupPerfNameByIndex(String szMachineName, int dwNameIndex) {
        WinDef.DWORDByReference pcchNameBufferSize = new WinDef.DWORDByReference(new WinDef.DWORD(0L));
        int result = Pdh.INSTANCE.PdhLookupPerfNameByIndex(szMachineName, dwNameIndex, null, pcchNameBufferSize);
        Pointer mem = null;
        if (result != -1073738819) {
            if (result != 0 && result != -2147481646) {
                throw new PdhException(result);
            }
            if (pcchNameBufferSize.getValue().intValue() < 1) {
                return "";
            }
            mem = new Memory(pcchNameBufferSize.getValue().intValue() * CHAR_TO_BYTES);
            result = Pdh.INSTANCE.PdhLookupPerfNameByIndex(szMachineName, dwNameIndex, mem, pcchNameBufferSize);
        } else {
            for (int bufferSize = 32; bufferSize <= 1024 && ((result = Pdh.INSTANCE.PdhLookupPerfNameByIndex(szMachineName, dwNameIndex, mem = new Memory(bufferSize * CHAR_TO_BYTES), pcchNameBufferSize = new WinDef.DWORDByReference(new WinDef.DWORD((long)bufferSize)))) == -1073738819 || result == -1073738814); bufferSize *= 2) {
            }
        }
        if (result != 0) {
            throw new PdhException(result);
        }
        if (CHAR_TO_BYTES == 1) {
            return mem.getString(0L);
        }
        return ((Memory)mem).getWideString(0L);
    }

    public static int PdhLookupPerfIndexByEnglishName(String szNameBuffer) {
        String[] counters = Advapi32Util.registryGetStringArray(WinReg.HKEY_LOCAL_MACHINE, ENGLISH_COUNTER_KEY, ENGLISH_COUNTER_VALUE);
        for (int i2 = 1; i2 < counters.length; i2 += 2) {
            if (!counters[i2].equals(szNameBuffer)) continue;
            try {
                return Integer.parseInt(counters[i2 - 1]);
            }
            catch (NumberFormatException e2) {
                return 0;
            }
        }
        return 0;
    }

    public static PdhEnumObjectItems PdhEnumObjectItems(String szDataSource, String szMachineName, String szObjectName, int dwDetailLevel) {
        String s2;
        int offset;
        ArrayList<String> counters = new ArrayList<String>();
        ArrayList<String> instances = new ArrayList<String>();
        WinDef.DWORDByReference pcchCounterListLength = new WinDef.DWORDByReference(new WinDef.DWORD(0L));
        WinDef.DWORDByReference pcchInstanceListLength = new WinDef.DWORDByReference(new WinDef.DWORD(0L));
        int result = Pdh.INSTANCE.PdhEnumObjectItems(szDataSource, szMachineName, szObjectName, null, pcchCounterListLength, null, pcchInstanceListLength, dwDetailLevel, 0);
        if (result != 0 && result != -2147481646) {
            throw new PdhException(result);
        }
        Memory mszCounterList = null;
        Memory mszInstanceList = null;
        if (pcchCounterListLength.getValue().intValue() > 0) {
            mszCounterList = new Memory(pcchCounterListLength.getValue().intValue() * CHAR_TO_BYTES);
        }
        if (pcchInstanceListLength.getValue().intValue() > 0) {
            mszInstanceList = new Memory(pcchInstanceListLength.getValue().intValue() * CHAR_TO_BYTES);
        }
        if ((result = Pdh.INSTANCE.PdhEnumObjectItems(szDataSource, szMachineName, szObjectName, mszCounterList, pcchCounterListLength, mszInstanceList, pcchInstanceListLength, dwDetailLevel, 0)) != 0) {
            throw new PdhException(result);
        }
        if (mszCounterList != null) {
            offset = 0;
            while ((long)offset < mszCounterList.size()) {
                s2 = null;
                s2 = CHAR_TO_BYTES == 1 ? mszCounterList.getString(offset) : mszCounterList.getWideString(offset);
                if (s2.isEmpty()) break;
                counters.add(s2);
                offset += (s2.length() + 1) * CHAR_TO_BYTES;
            }
        }
        if (mszInstanceList != null) {
            offset = 0;
            while ((long)offset < mszInstanceList.size()) {
                s2 = null;
                s2 = CHAR_TO_BYTES == 1 ? mszInstanceList.getString(offset) : mszInstanceList.getWideString(offset);
                if (s2.isEmpty()) break;
                instances.add(s2);
                offset += (s2.length() + 1) * CHAR_TO_BYTES;
            }
        }
        return new PdhEnumObjectItems(counters, instances);
    }

    public static final class PdhException
    extends RuntimeException {
        private final int errorCode;

        public PdhException(int errorCode) {
            super(String.format("Pdh call failed with error code 0x%08X", errorCode));
            this.errorCode = errorCode;
        }

        public int getErrorCode() {
            return this.errorCode;
        }
    }

    public static class PdhEnumObjectItems {
        private final List<String> counters;
        private final List<String> instances;

        public PdhEnumObjectItems(List<String> counters, List<String> instances) {
            this.counters = this.copyAndEmptyListForNullList(counters);
            this.instances = this.copyAndEmptyListForNullList(instances);
        }

        public List<String> getCounters() {
            return this.counters;
        }

        public List<String> getInstances() {
            return this.instances;
        }

        private List<String> copyAndEmptyListForNullList(List<String> inputList) {
            if (inputList == null) {
                return new ArrayList<String>();
            }
            return new ArrayList<String>(inputList);
        }

        public String toString() {
            return "PdhEnumObjectItems{counters=" + this.counters + ", instances=" + this.instances + '}';
        }
    }
}

