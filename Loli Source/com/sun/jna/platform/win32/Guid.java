/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import java.security.SecureRandom;
import java.util.Arrays;

public interface Guid {
    public static final IID IID_NULL = new IID();

    public static class IID
    extends GUID {
        public IID() {
        }

        public IID(Pointer memory) {
            super(memory);
        }

        public IID(String iid) {
            super(iid);
        }

        public IID(byte[] data) {
            super(data);
        }

        public IID(GUID guid) {
            this(guid.toGuidString());
        }
    }

    public static class REFIID
    extends PointerType {
        public REFIID() {
        }

        public REFIID(Pointer memory) {
            super(memory);
        }

        public REFIID(IID guid) {
            super(guid.getPointer());
        }

        public void setValue(IID value) {
            this.setPointer(value.getPointer());
        }

        public IID getValue() {
            return new IID(this.getPointer());
        }

        @Override
        public boolean equals(Object o2) {
            if (o2 == null) {
                return false;
            }
            if (this == o2) {
                return true;
            }
            if (this.getClass() != o2.getClass()) {
                return false;
            }
            REFIID other = (REFIID)o2;
            return this.getValue().equals(other.getValue());
        }

        @Override
        public int hashCode() {
            return this.getValue().hashCode();
        }
    }

    public static class CLSID
    extends GUID {
        public CLSID() {
        }

        public CLSID(String guid) {
            super(guid);
        }

        public CLSID(GUID guid) {
            super(guid);
        }

        public static class ByReference
        extends GUID {
            public ByReference() {
            }

            public ByReference(GUID guid) {
                super(guid);
            }

            public ByReference(Pointer memory) {
                super(memory);
            }
        }
    }

    @Structure.FieldOrder(value={"Data1", "Data2", "Data3", "Data4"})
    public static class GUID
    extends Structure {
        public int Data1;
        public short Data2;
        public short Data3;
        public byte[] Data4 = new byte[8];

        public GUID() {
        }

        public GUID(GUID guid) {
            this.Data1 = guid.Data1;
            this.Data2 = guid.Data2;
            this.Data3 = guid.Data3;
            this.Data4 = guid.Data4;
            this.writeFieldsToMemory();
        }

        public GUID(String guid) {
            this(GUID.fromString(guid));
        }

        public GUID(byte[] data) {
            this(GUID.fromBinary(data));
        }

        public GUID(Pointer memory) {
            super(memory);
            this.read();
        }

        @Override
        public boolean equals(Object o2) {
            if (o2 == null) {
                return false;
            }
            if (this == o2) {
                return true;
            }
            if (this.getClass() != o2.getClass()) {
                return false;
            }
            GUID other = (GUID)o2;
            return this.Data1 == other.Data1 && this.Data2 == other.Data2 && this.Data3 == other.Data3 && Arrays.equals(this.Data4, other.Data4);
        }

        @Override
        public int hashCode() {
            return this.Data1 + this.Data2 & 65535 + this.Data3 & 65535 + Arrays.hashCode(this.Data4);
        }

        public static GUID fromBinary(byte[] data) {
            if (data.length != 16) {
                throw new IllegalArgumentException("Invalid data length: " + data.length);
            }
            GUID newGuid = new GUID();
            long data1Temp = data[0] & 0xFF;
            data1Temp <<= 8;
            data1Temp |= (long)(data[1] & 0xFF);
            data1Temp <<= 8;
            data1Temp |= (long)(data[2] & 0xFF);
            data1Temp <<= 8;
            newGuid.Data1 = (int)(data1Temp |= (long)(data[3] & 0xFF));
            int data2Temp = data[4] & 0xFF;
            data2Temp <<= 8;
            newGuid.Data2 = (short)(data2Temp |= data[5] & 0xFF);
            int data3Temp = data[6] & 0xFF;
            data3Temp <<= 8;
            newGuid.Data3 = (short)(data3Temp |= data[7] & 0xFF);
            newGuid.Data4[0] = data[8];
            newGuid.Data4[1] = data[9];
            newGuid.Data4[2] = data[10];
            newGuid.Data4[3] = data[11];
            newGuid.Data4[4] = data[12];
            newGuid.Data4[5] = data[13];
            newGuid.Data4[6] = data[14];
            newGuid.Data4[7] = data[15];
            newGuid.writeFieldsToMemory();
            return newGuid;
        }

        public static GUID fromString(String guid) {
            int i2;
            int y2 = 0;
            char[] _cnewguid = new char[32];
            char[] _cguid = guid.toCharArray();
            byte[] bdata = new byte[16];
            GUID newGuid = new GUID();
            if (guid.length() > 38) {
                throw new IllegalArgumentException("Invalid guid length: " + guid.length());
            }
            for (i2 = 0; i2 < _cguid.length; ++i2) {
                if (_cguid[i2] == '{' || _cguid[i2] == '-' || _cguid[i2] == '}') continue;
                _cnewguid[y2++] = _cguid[i2];
            }
            for (i2 = 0; i2 < 32; i2 += 2) {
                bdata[i2 / 2] = (byte)((Character.digit(_cnewguid[i2], 16) << 4) + Character.digit(_cnewguid[i2 + 1], 16) & 0xFF);
            }
            if (bdata.length != 16) {
                throw new IllegalArgumentException("Invalid data length: " + bdata.length);
            }
            long data1Temp = bdata[0] & 0xFF;
            data1Temp <<= 8;
            data1Temp |= (long)(bdata[1] & 0xFF);
            data1Temp <<= 8;
            data1Temp |= (long)(bdata[2] & 0xFF);
            data1Temp <<= 8;
            newGuid.Data1 = (int)(data1Temp |= (long)(bdata[3] & 0xFF));
            int data2Temp = bdata[4] & 0xFF;
            data2Temp <<= 8;
            newGuid.Data2 = (short)(data2Temp |= bdata[5] & 0xFF);
            int data3Temp = bdata[6] & 0xFF;
            data3Temp <<= 8;
            newGuid.Data3 = (short)(data3Temp |= bdata[7] & 0xFF);
            newGuid.Data4[0] = bdata[8];
            newGuid.Data4[1] = bdata[9];
            newGuid.Data4[2] = bdata[10];
            newGuid.Data4[3] = bdata[11];
            newGuid.Data4[4] = bdata[12];
            newGuid.Data4[5] = bdata[13];
            newGuid.Data4[6] = bdata[14];
            newGuid.Data4[7] = bdata[15];
            newGuid.writeFieldsToMemory();
            return newGuid;
        }

        public static GUID newGuid() {
            SecureRandom ng = new SecureRandom();
            byte[] randomBytes = new byte[16];
            ng.nextBytes(randomBytes);
            randomBytes[6] = (byte)(randomBytes[6] & 0xF);
            randomBytes[6] = (byte)(randomBytes[6] | 0x40);
            randomBytes[8] = (byte)(randomBytes[8] & 0x3F);
            randomBytes[8] = (byte)(randomBytes[8] | 0x80);
            return new GUID(randomBytes);
        }

        public byte[] toByteArray() {
            byte[] guid = new byte[16];
            byte[] bytes1 = new byte[]{(byte)(this.Data1 >> 24), (byte)(this.Data1 >> 16), (byte)(this.Data1 >> 8), (byte)(this.Data1 >> 0)};
            byte[] bytes2 = new byte[]{(byte)(this.Data2 >> 24), (byte)(this.Data2 >> 16), (byte)(this.Data2 >> 8), (byte)(this.Data2 >> 0)};
            byte[] bytes3 = new byte[]{(byte)(this.Data3 >> 24), (byte)(this.Data3 >> 16), (byte)(this.Data3 >> 8), (byte)(this.Data3 >> 0)};
            System.arraycopy(bytes1, 0, guid, 0, 4);
            System.arraycopy(bytes2, 2, guid, 4, 2);
            System.arraycopy(bytes3, 2, guid, 6, 2);
            System.arraycopy(this.Data4, 0, guid, 8, 8);
            return guid;
        }

        public String toGuidString() {
            String HEXES = "0123456789ABCDEF";
            byte[] bGuid = this.toByteArray();
            StringBuilder hexStr = new StringBuilder(2 * bGuid.length);
            hexStr.append("{");
            for (int i2 = 0; i2 < bGuid.length; ++i2) {
                char ch1 = "0123456789ABCDEF".charAt((bGuid[i2] & 0xF0) >> 4);
                char ch2 = "0123456789ABCDEF".charAt(bGuid[i2] & 0xF);
                hexStr.append(ch1).append(ch2);
                if (i2 != 3 && i2 != 5 && i2 != 7 && i2 != 9) continue;
                hexStr.append("-");
            }
            hexStr.append("}");
            return hexStr.toString();
        }

        protected void writeFieldsToMemory() {
            for (String name : this.getFieldOrder()) {
                this.writeField(name);
            }
        }

        public static class ByReference
        extends GUID
        implements Structure.ByReference {
            public ByReference() {
            }

            public ByReference(GUID guid) {
                super(guid.getPointer());
                this.Data1 = guid.Data1;
                this.Data2 = guid.Data2;
                this.Data3 = guid.Data3;
                this.Data4 = guid.Data4;
            }

            public ByReference(Pointer memory) {
                super(memory);
            }
        }

        public static class ByValue
        extends GUID
        implements Structure.ByValue {
            public ByValue() {
            }

            public ByValue(GUID guid) {
                super(guid.getPointer());
                this.Data1 = guid.Data1;
                this.Data2 = guid.Data2;
                this.Data3 = guid.Data3;
                this.Data4 = guid.Data4;
            }

            public ByValue(Pointer memory) {
                super(memory);
            }
        }
    }
}

