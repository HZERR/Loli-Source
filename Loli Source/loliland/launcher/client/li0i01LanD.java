/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WinNT;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.l10lO11lanD;
import loliland.launcher.client.ll10lIO1lANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class li0i01LanD {
    private static final Logger lI00OlAND = LoggerFactory.getLogger(li0i01LanD.class);
    private static int lli0OiIlAND = l10lO11lanD.I1O1I1LaNd("oshi.util.wmi.timeout", -1);
    protected int I1O1I1LaNd = lli0OiIlAND;
    protected final Set OOOIilanD = new HashSet();
    private int li0iOILAND = 0;
    private boolean O1il1llOLANd = false;
    private static final Class[] Oill1LAnD;
    private static final Object[] lIOILand;
    private static Class lil0liLand;

    public static synchronized li0i01LanD I1O1I1LaNd() {
        if (lil0liLand == null) {
            return new li0i01LanD();
        }
        try {
            return (li0i01LanD)lil0liLand.getConstructor(Oill1LAnD).newInstance(lIOILand);
        }
        catch (NoSuchMethodException | SecurityException exception) {
            lI00OlAND.error("Failed to find or access a no-arg constructor for {}", (Object)lil0liLand);
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException exception) {
            lI00OlAND.error("Failed to create a new instance of {}", (Object)lil0liLand);
        }
        return null;
    }

    public static synchronized void I1O1I1LaNd(Class class_) {
        lil0liLand = class_;
    }

    public WbemcliUtil.WmiResult I1O1I1LaNd(WbemcliUtil.WmiQuery wmiQuery) {
        return this.I1O1I1LaNd(wmiQuery, true);
    }

    public WbemcliUtil.WmiResult I1O1I1LaNd(WbemcliUtil.WmiQuery wmiQuery, boolean bl) {
        WbemcliUtil wbemcliUtil = WbemcliUtil.INSTANCE;
        wbemcliUtil.getClass();
        WbemcliUtil.WmiResult wmiResult = wbemcliUtil.new WbemcliUtil.WmiResult(wmiQuery.getPropertyEnum());
        if (this.OOOIilanD.contains(wmiQuery.getWmiClassName())) {
            return wmiResult;
        }
        boolean bl2 = false;
        try {
            if (bl) {
                bl2 = this.OOOIilanD();
            }
            wmiResult = wmiQuery.execute(this.I1O1I1LaNd);
        }
        catch (COMException cOMException) {
            if (!"ROOT\\OpenHardwareMonitor".equals(wmiQuery.getNameSpace())) {
                int n2 = cOMException.getHresult() == null ? -1 : cOMException.getHresult().intValue();
                switch (n2) {
                    case -2147217394: {
                        lI00OlAND.warn("COM exception: Invalid Namespace {}", (Object)wmiQuery.getNameSpace());
                        break;
                    }
                    case -2147217392: {
                        lI00OlAND.warn("COM exception: Invalid Class {}", (Object)wmiQuery.getWmiClassName());
                        break;
                    }
                    case -2147217385: {
                        lI00OlAND.warn("COM exception: Invalid Query: {}", (Object)ii00llanD.I1O1I1LaNd(wmiQuery));
                        break;
                    }
                    default: {
                        this.I1O1I1LaNd(wmiQuery, cOMException);
                    }
                }
                this.OOOIilanD.add(wmiQuery.getWmiClassName());
            }
        }
        catch (TimeoutException timeoutException) {
            lI00OlAND.warn("WMI query timed out after {} ms: {}", (Object)this.I1O1I1LaNd, (Object)ii00llanD.I1O1I1LaNd(wmiQuery));
        }
        if (bl2) {
            this.lI00OlAND();
        }
        return wmiResult;
    }

    protected void I1O1I1LaNd(WbemcliUtil.WmiQuery wmiQuery, COMException cOMException) {
        lI00OlAND.warn("COM exception querying {}, which might not be on your system. Will not attempt to query it again. Error was {}: {}", wmiQuery.getWmiClassName(), cOMException.getHresult().intValue(), cOMException.getMessage());
    }

    public boolean OOOIilanD() {
        boolean bl = false;
        bl = this.I1O1I1LaNd(this.lli0OiIlAND());
        if (!bl) {
            bl = this.I1O1I1LaNd(this.li0iOILAND());
        }
        if (bl && !this.O1il1llOLANd()) {
            WinNT.HRESULT hRESULT = Ole32.INSTANCE.CoInitializeSecurity(null, -1, null, null, 0, 3, null, 0, null);
            if (COMUtils.FAILED(hRESULT) && hRESULT.intValue() != -2147417831) {
                Ole32.INSTANCE.CoUninitialize();
                throw new COMException("Failed to initialize security.", hRESULT);
            }
            this.O1il1llOLANd = true;
        }
        return bl;
    }

    protected boolean I1O1I1LaNd(int n2) {
        WinNT.HRESULT hRESULT = Ole32.INSTANCE.CoInitializeEx(null, n2);
        switch (hRESULT.intValue()) {
            case 0: 
            case 1: {
                return true;
            }
            case -2147417850: {
                return false;
            }
        }
        throw new COMException("Failed to initialize COM library.", hRESULT);
    }

    public void lI00OlAND() {
        Ole32.INSTANCE.CoUninitialize();
    }

    public int lli0OiIlAND() {
        return this.li0iOILAND;
    }

    public int li0iOILAND() {
        this.li0iOILAND = this.li0iOILAND == 2 ? 0 : 2;
        return this.li0iOILAND;
    }

    public boolean O1il1llOLANd() {
        return this.O1il1llOLANd;
    }

    public int Oill1LAnD() {
        return this.I1O1I1LaNd;
    }

    public void OOOIilanD(int n2) {
        this.I1O1I1LaNd = n2;
    }

    static {
        if (lli0OiIlAND == 0 || lli0OiIlAND < -1) {
            throw new ll10lIO1lANd("oshi.util.wmi.timeout");
        }
        Oill1LAnD = new Class[0];
        lIOILand = new Object[0];
        lil0liLand = null;
    }
}

