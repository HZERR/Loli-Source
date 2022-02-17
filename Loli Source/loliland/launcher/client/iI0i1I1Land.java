/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.SetupApi;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APITypeMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import loliland.launcher.client.I0Oiland;
import loliland.launcher.client.IiOOlilLAND;
import loliland.launcher.client.O001l1laNd;
import loliland.launcher.client.iIl1iillaNd;
import loliland.launcher.client.iiIiiOiLAnD;
import loliland.launcher.client.l1i0Land;
import loliland.launcher.client.lO10i0I1land;
import loliland.launcher.client.liilililLAND;
import loliland.launcher.client.lilO1llaND;
import loliland.launcher.client.llOIlIilAND;

public final class iI0i1I1Land
extends I0Oiland {
    private static final Guid.GUID I1O1I1LaNd = Guid.GUID.fromString("{72631E54-78A4-11D0-BCF7-00AA00B7B32A}");
    private static final int OOOIilanD = W32APITypeMapper.DEFAULT == W32APITypeMapper.UNICODE ? 2 : 1;
    private static final boolean lI00OlAND = Platform.is64Bit();
    private static final int lli0OiIlAND = Integer.MIN_VALUE;
    private static final int li0iOILAND = 0x20000000;
    private static final int O1il1llOLANd = 1;
    private static final int Oill1LAnD = 2;
    private static final int lIOILand = 4;
    private static final int lil0liLand = 0x40000000;
    private static final int iilIi1laND = 2703424;
    private static final int lli011lLANd = 2703436;
    private static final int l0illAND = 2703428;

    public iI0i1I1Land(String string, String string2, double d2, double d3, double d4, double d5, double d6, double d7, boolean bl, boolean bl2, boolean bl3, llOIlIilAND llOIlIilAND2, int n2, int n3, int n4, int n5, String string3, LocalDate localDate, String string4, String string5, double d8) {
        super(string, string2, d2, d3, d4, d5, d6, d7, bl, bl2, bl3, llOIlIilAND2, n2, n3, n4, n5, string3, localDate, string4, string5, d8);
    }

    public static List iOl10IlLAnd() {
        return Arrays.asList(iI0i1I1Land.I1O1I1LaNd("System Battery"));
    }

    private static iI0i1I1Land I1O1I1LaNd(String string) {
        Object object;
        String string2 = string;
        String string3 = "unknown";
        double d2 = 1.0;
        double d3 = -1.0;
        double d4 = 0.0;
        int n2 = 0;
        double d5 = -1.0;
        double d6 = 0.0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        llOIlIilAND llOIlIilAND2 = llOIlIilAND.lI00OlAND;
        int n3 = 0;
        int n4 = 1;
        int n5 = 1;
        int n6 = -1;
        String string4 = "unknown";
        LocalDate localDate = null;
        String string5 = "unknown";
        String string6 = "unknown";
        double d7 = 0.0;
        int n7 = new l1i0Land().size();
        Memory memory = new Memory(n7);
        if (0 == lilO1llaND.INSTANCE.CallNtPowerInformation(5, null, 0, memory, n7)) {
            object = new l1i0Land(memory);
            if (((l1i0Land)object).batteryPresent > 0) {
                if (((l1i0Land)object).acOnLine == 0 && ((l1i0Land)object).charging == 0 && ((l1i0Land)object).discharging > 0) {
                    d3 = ((l1i0Land)object).estimatedTime;
                } else if (((l1i0Land)object).charging > 0) {
                    d3 = -2.0;
                }
                n4 = ((l1i0Land)object).maxCapacity;
                n3 = ((l1i0Land)object).remainingCapacity;
                d2 = Math.min(1.0, (double)n3 / (double)n4);
                n2 = ((l1i0Land)object).rate;
            }
        }
        if (WinBase.INVALID_HANDLE_VALUE != (object = SetupApi.INSTANCE.SetupDiGetClassDevs(I1O1I1LaNd, null, null, 18))) {
            boolean bl4 = false;
            for (int i2 = 0; !bl4 && i2 < 100; ++i2) {
                SetupApi.SP_DEVICE_INTERFACE_DATA sP_DEVICE_INTERFACE_DATA = new SetupApi.SP_DEVICE_INTERFACE_DATA();
                sP_DEVICE_INTERFACE_DATA.cbSize = sP_DEVICE_INTERFACE_DATA.size();
                if (SetupApi.INSTANCE.SetupDiEnumDeviceInterfaces((WinNT.HANDLE)object, null, I1O1I1LaNd, i2, sP_DEVICE_INTERFACE_DATA)) {
                    String string7;
                    WinNT.HANDLE hANDLE;
                    IntByReference intByReference = new IntByReference(0);
                    SetupApi.INSTANCE.SetupDiGetDeviceInterfaceDetail((WinNT.HANDLE)object, sP_DEVICE_INTERFACE_DATA, null, 0, intByReference, null);
                    if (122 != Kernel32.INSTANCE.GetLastError()) continue;
                    Memory memory2 = new Memory(intByReference.getValue());
                    memory2.setInt(0L, 4 + (lI00OlAND ? 4 : OOOIilanD));
                    if (!SetupApi.INSTANCE.SetupDiGetDeviceInterfaceDetail((WinNT.HANDLE)object, sP_DEVICE_INTERFACE_DATA, memory2, (int)memory2.size(), intByReference, null) || WinBase.INVALID_HANDLE_VALUE.equals(hANDLE = Kernel32.INSTANCE.CreateFile(string7 = OOOIilanD > 1 ? memory2.getWideString(4L) : memory2.getString(4L), -1073741824, 3, null, 3, 128, null))) continue;
                    O001l1laNd o001l1laNd = new O001l1laNd();
                    IntByReference intByReference2 = new IntByReference(0);
                    IntByReference intByReference3 = new IntByReference();
                    IntByReference intByReference4 = new IntByReference();
                    if (Kernel32.INSTANCE.DeviceIoControl(hANDLE, 2703424, intByReference2.getPointer(), 4, intByReference3.getPointer(), 4, intByReference4, null)) {
                        o001l1laNd.BatteryTag = intByReference3.getValue();
                        if (o001l1laNd.BatteryTag > 0) {
                            o001l1laNd.InformationLevel = IiOOlilLAND.I1O1I1LaNd.ordinal();
                            o001l1laNd.write();
                            liilililLAND liilililLAND2 = new liilililLAND();
                            if (Kernel32.INSTANCE.DeviceIoControl(hANDLE, 2703428, o001l1laNd.getPointer(), o001l1laNd.size(), liilililLAND2.getPointer(), liilililLAND2.size(), intByReference4, null)) {
                                Object object2;
                                Structure structure;
                                liilililLAND2.read();
                                if (0 != (liilililLAND2.Capabilities & Integer.MIN_VALUE) && 0 == (liilililLAND2.Capabilities & 0x20000000)) {
                                    if (0 == (liilililLAND2.Capabilities & 0x40000000)) {
                                        llOIlIilAND2 = llOIlIilAND.I1O1I1LaNd;
                                    }
                                    string4 = Native.toString(liilililLAND2.Chemistry, StandardCharsets.US_ASCII);
                                    n5 = liilililLAND2.DesignedCapacity;
                                    n4 = liilililLAND2.FullChargedCapacity;
                                    n6 = liilililLAND2.CycleCount;
                                    structure = new iiIiiOiLAnD();
                                    ((iiIiiOiLAnD)structure).BatteryTag = o001l1laNd.BatteryTag;
                                    structure.write();
                                    object2 = new lO10i0I1land();
                                    if (Kernel32.INSTANCE.DeviceIoControl(hANDLE, 2703436, structure.getPointer(), structure.size(), ((Structure)object2).getPointer(), ((Structure)object2).size(), intByReference4, null)) {
                                        ((Structure)object2).read();
                                        if (0 != (((lO10i0I1land)object2).PowerState & 1)) {
                                            bl = true;
                                        }
                                        if (0 != (((lO10i0I1land)object2).PowerState & 2)) {
                                            bl3 = true;
                                        }
                                        if (0 != (((lO10i0I1land)object2).PowerState & 4)) {
                                            bl2 = true;
                                        }
                                        n3 = ((lO10i0I1land)object2).Capacity;
                                        d5 = ((lO10i0I1land)object2).Voltage > 0 ? (double)((lO10i0I1land)object2).Voltage / 1000.0 : (double)((lO10i0I1land)object2).Voltage;
                                        n2 = ((lO10i0I1land)object2).Rate;
                                        if (d5 > 0.0) {
                                            d6 = (double)n2 / d5;
                                        }
                                    }
                                }
                                string3 = iI0i1I1Land.I1O1I1LaNd(hANDLE, intByReference3.getValue(), IiOOlilLAND.li0iOILAND.ordinal());
                                string5 = iI0i1I1Land.I1O1I1LaNd(hANDLE, intByReference3.getValue(), IiOOlilLAND.Oill1LAnD.ordinal());
                                string6 = iI0i1I1Land.I1O1I1LaNd(hANDLE, intByReference3.getValue(), IiOOlilLAND.lil0liLand.ordinal());
                                o001l1laNd.InformationLevel = IiOOlilLAND.O1il1llOLANd.ordinal();
                                o001l1laNd.write();
                                structure = new iIl1iillaNd();
                                if (Kernel32.INSTANCE.DeviceIoControl(hANDLE, 2703428, o001l1laNd.getPointer(), o001l1laNd.size(), structure.getPointer(), structure.size(), intByReference4, null)) {
                                    structure.read();
                                    if (((iIl1iillaNd)structure).Year > 1900 && ((iIl1iillaNd)structure).Month > 0 && ((iIl1iillaNd)structure).Day > 0) {
                                        localDate = LocalDate.of((int)((iIl1iillaNd)structure).Year, ((iIl1iillaNd)structure).Month, (int)((iIl1iillaNd)structure).Day);
                                    }
                                }
                                o001l1laNd.InformationLevel = IiOOlilLAND.lI00OlAND.ordinal();
                                o001l1laNd.write();
                                object2 = new IntByReference();
                                if (Kernel32.INSTANCE.DeviceIoControl(hANDLE, 2703428, o001l1laNd.getPointer(), o001l1laNd.size(), ((PointerType)object2).getPointer(), 4, intByReference4, null)) {
                                    d7 = (double)((IntByReference)object2).getValue() / 10.0 - 273.15;
                                }
                                o001l1laNd.InformationLevel = IiOOlilLAND.lli0OiIlAND.ordinal();
                                if (n2 != 0) {
                                    o001l1laNd.AtRate = n2;
                                }
                                o001l1laNd.write();
                                IntByReference intByReference5 = new IntByReference();
                                if (Kernel32.INSTANCE.DeviceIoControl(hANDLE, 2703428, o001l1laNd.getPointer(), o001l1laNd.size(), intByReference5.getPointer(), 4, intByReference4, null)) {
                                    d4 = intByReference5.getValue();
                                }
                                if (d4 < 0.0 && n2 != 0 && (d4 = (double)(n4 - n3) * 3600.0 / (double)n2) < 0.0) {
                                    d4 *= -1.0;
                                }
                                bl4 = true;
                            }
                        }
                    }
                    Kernel32.INSTANCE.CloseHandle(hANDLE);
                    continue;
                }
                if (259 == Kernel32.INSTANCE.GetLastError()) break;
            }
            SetupApi.INSTANCE.SetupDiDestroyDeviceInfoList((WinNT.HANDLE)object);
        }
        return new iI0i1I1Land(string2, string3, d2, d3, d4, n2, d5, d6, bl, bl2, bl3, llOIlIilAND2, n3, n4, n5, n6, string4, localDate, string5, string6, d7);
    }

    private static String I1O1I1LaNd(WinNT.HANDLE hANDLE, int n2, int n3) {
        Memory memory;
        O001l1laNd o001l1laNd = new O001l1laNd();
        o001l1laNd.BatteryTag = n2;
        o001l1laNd.InformationLevel = n3;
        o001l1laNd.write();
        IntByReference intByReference = new IntByReference();
        boolean bl = false;
        long l2 = 0L;
        do {
            memory = new Memory(l2 += 256L);
        } while (!(bl = Kernel32.INSTANCE.DeviceIoControl(hANDLE, 2703428, o001l1laNd.getPointer(), o001l1laNd.size(), memory, (int)memory.size(), intByReference, null)) && l2 < 4096L);
        return OOOIilanD > 1 ? memory.getWideString(0L) : memory.getString(0L);
    }
}

