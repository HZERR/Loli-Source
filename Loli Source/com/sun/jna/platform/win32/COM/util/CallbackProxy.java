/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.DispatchListener;
import com.sun.jna.platform.win32.COM.IDispatchCallback;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.util.Convert;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackListener;
import com.sun.jna.platform.win32.COM.util.ObjectFactory;
import com.sun.jna.platform.win32.COM.util.annotation.ComEventCallback;
import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CallbackProxy
implements IDispatchCallback {
    private static boolean DEFAULT_BOOLEAN;
    private static byte DEFAULT_BYTE;
    private static short DEFAULT_SHORT;
    private static int DEFAULT_INT;
    private static long DEFAULT_LONG;
    private static float DEFAULT_FLOAT;
    private static double DEFAULT_DOUBLE;
    ObjectFactory factory;
    Class<?> comEventCallbackInterface;
    IComEventCallbackListener comEventCallbackListener;
    Guid.REFIID listenedToRiid;
    public DispatchListener dispatchListener;
    Map<OaIdl.DISPID, Method> dsipIdMap;

    public CallbackProxy(ObjectFactory factory, Class<?> comEventCallbackInterface, IComEventCallbackListener comEventCallbackListener) {
        this.factory = factory;
        this.comEventCallbackInterface = comEventCallbackInterface;
        this.comEventCallbackListener = comEventCallbackListener;
        this.listenedToRiid = this.createRIID(comEventCallbackInterface);
        this.dsipIdMap = this.createDispIdMap(comEventCallbackInterface);
        this.dispatchListener = new DispatchListener(this);
    }

    Guid.REFIID createRIID(Class<?> comEventCallbackInterface) {
        ComInterface comInterfaceAnnotation = comEventCallbackInterface.getAnnotation(ComInterface.class);
        if (null == comInterfaceAnnotation) {
            throw new COMException("advise: Interface must define a value for either iid via the ComInterface annotation");
        }
        String iidStr = comInterfaceAnnotation.iid();
        if (null == iidStr || iidStr.isEmpty()) {
            throw new COMException("ComInterface must define a value for iid");
        }
        return new Guid.REFIID(new Guid.IID(iidStr).getPointer());
    }

    Map<OaIdl.DISPID, Method> createDispIdMap(Class<?> comEventCallbackInterface) {
        HashMap<OaIdl.DISPID, Method> map = new HashMap<OaIdl.DISPID, Method>();
        for (Method meth : comEventCallbackInterface.getMethods()) {
            int dispId;
            ComEventCallback callbackAnnotation = meth.getAnnotation(ComEventCallback.class);
            ComMethod methodAnnotation = meth.getAnnotation(ComMethod.class);
            if (methodAnnotation != null) {
                dispId = methodAnnotation.dispId();
                if (-1 == dispId) {
                    dispId = this.fetchDispIdFromName(callbackAnnotation);
                }
                if (dispId == -1) {
                    this.comEventCallbackListener.errorReceivingCallbackEvent("DISPID for " + meth.getName() + " not found", null);
                }
                map.put(new OaIdl.DISPID(dispId), meth);
                continue;
            }
            if (null == callbackAnnotation) continue;
            dispId = callbackAnnotation.dispid();
            if (-1 == dispId) {
                dispId = this.fetchDispIdFromName(callbackAnnotation);
            }
            if (dispId == -1) {
                this.comEventCallbackListener.errorReceivingCallbackEvent("DISPID for " + meth.getName() + " not found", null);
            }
            map.put(new OaIdl.DISPID(dispId), meth);
        }
        return map;
    }

    int fetchDispIdFromName(ComEventCallback annotation) {
        return -1;
    }

    void invokeOnThread(OaIdl.DISPID dispIdMember, Guid.REFIID riid, WinDef.LCID lcid, WinDef.WORD wFlags, OleAuto.DISPPARAMS.ByReference pDispParams) {
        int i2;
        Variant.VARIANT[] arguments = pDispParams.getArgs();
        Method eventMethod = this.dsipIdMap.get(dispIdMember);
        if (eventMethod == null) {
            this.comEventCallbackListener.errorReceivingCallbackEvent("No method found with dispId = " + dispIdMember, null);
            return;
        }
        OaIdl.DISPID[] positionMap = pDispParams.getRgdispidNamedArgs();
        Class<?>[] paramTypes = eventMethod.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (i2 = 0; i2 < params.length && arguments.length - positionMap.length - i2 > 0; ++i2) {
            Class<?> targetClass = paramTypes[i2];
            Variant.VARIANT varg = arguments[arguments.length - i2 - 1];
            params[i2] = Convert.toJavaObject(varg, targetClass, this.factory, true, false);
        }
        for (i2 = 0; i2 < positionMap.length; ++i2) {
            int targetPosition = positionMap[i2].intValue();
            if (targetPosition >= params.length) continue;
            Class<?> targetClass = paramTypes[targetPosition];
            Variant.VARIANT varg = arguments[i2];
            params[targetPosition] = Convert.toJavaObject(varg, targetClass, this.factory, true, false);
        }
        for (i2 = 0; i2 < params.length; ++i2) {
            if (params[i2] != null || !paramTypes[i2].isPrimitive()) continue;
            if (paramTypes[i2].equals(Boolean.TYPE)) {
                params[i2] = DEFAULT_BOOLEAN;
                continue;
            }
            if (paramTypes[i2].equals(Byte.TYPE)) {
                params[i2] = DEFAULT_BYTE;
                continue;
            }
            if (paramTypes[i2].equals(Short.TYPE)) {
                params[i2] = DEFAULT_SHORT;
                continue;
            }
            if (paramTypes[i2].equals(Integer.TYPE)) {
                params[i2] = DEFAULT_INT;
                continue;
            }
            if (paramTypes[i2].equals(Long.TYPE)) {
                params[i2] = DEFAULT_LONG;
                continue;
            }
            if (paramTypes[i2].equals(Float.TYPE)) {
                params[i2] = Float.valueOf(DEFAULT_FLOAT);
                continue;
            }
            if (paramTypes[i2].equals(Double.TYPE)) {
                params[i2] = DEFAULT_DOUBLE;
                continue;
            }
            throw new IllegalArgumentException("Class type " + paramTypes[i2].getName() + " not mapped to primitive default value.");
        }
        try {
            eventMethod.invoke(this.comEventCallbackListener, params);
        }
        catch (Exception e2) {
            ArrayList<String> decodedClassNames = new ArrayList<String>(params.length);
            for (Object o2 : params) {
                if (o2 == null) {
                    decodedClassNames.add("NULL");
                    continue;
                }
                decodedClassNames.add(o2.getClass().getName());
            }
            this.comEventCallbackListener.errorReceivingCallbackEvent("Exception invoking method " + eventMethod + " supplied: " + ((Object)decodedClassNames).toString(), e2);
        }
    }

    @Override
    public Pointer getPointer() {
        return this.dispatchListener.getPointer();
    }

    @Override
    public WinNT.HRESULT GetTypeInfoCount(WinDef.UINTByReference pctinfo) {
        return new WinNT.HRESULT(-2147467263);
    }

    @Override
    public WinNT.HRESULT GetTypeInfo(WinDef.UINT iTInfo, WinDef.LCID lcid, PointerByReference ppTInfo) {
        return new WinNT.HRESULT(-2147467263);
    }

    @Override
    public WinNT.HRESULT GetIDsOfNames(Guid.REFIID riid, WString[] rgszNames, int cNames, WinDef.LCID lcid, OaIdl.DISPIDByReference rgDispId) {
        return new WinNT.HRESULT(-2147467263);
    }

    @Override
    public WinNT.HRESULT Invoke(OaIdl.DISPID dispIdMember, Guid.REFIID riid, WinDef.LCID lcid, WinDef.WORD wFlags, OleAuto.DISPPARAMS.ByReference pDispParams, Variant.VARIANT.ByReference pVarResult, OaIdl.EXCEPINFO.ByReference pExcepInfo, IntByReference puArgErr) {
        assert (COMUtils.comIsInitialized()) : "Assumption about COM threading broken.";
        this.invokeOnThread(dispIdMember, riid, lcid, wFlags, pDispParams);
        return WinError.S_OK;
    }

    @Override
    public WinNT.HRESULT QueryInterface(Guid.REFIID refid, PointerByReference ppvObject) {
        if (null == ppvObject) {
            return new WinNT.HRESULT(-2147467261);
        }
        if (refid.equals(this.listenedToRiid)) {
            ppvObject.setValue(this.getPointer());
            return WinError.S_OK;
        }
        if (refid.getValue().equals(Unknown.IID_IUNKNOWN)) {
            ppvObject.setValue(this.getPointer());
            return WinError.S_OK;
        }
        if (refid.getValue().equals(Dispatch.IID_IDISPATCH)) {
            ppvObject.setValue(this.getPointer());
            return WinError.S_OK;
        }
        return new WinNT.HRESULT(-2147467262);
    }

    @Override
    public int AddRef() {
        return 0;
    }

    @Override
    public int Release() {
        return 0;
    }
}

