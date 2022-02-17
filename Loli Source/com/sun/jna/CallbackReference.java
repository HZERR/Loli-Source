/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna;

import com.sun.jna.AltCallingConvention;
import com.sun.jna.Callback;
import com.sun.jna.CallbackParameterContext;
import com.sun.jna.CallbackProxy;
import com.sun.jna.CallbackResultContext;
import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.Function;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.NativeString;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import com.sun.jna.Structure;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class CallbackReference
extends WeakReference<Callback> {
    static final Map<Callback, CallbackReference> callbackMap = new WeakHashMap<Callback, CallbackReference>();
    static final Map<Callback, CallbackReference> directCallbackMap = new WeakHashMap<Callback, CallbackReference>();
    static final Map<Pointer, Reference<Callback>[]> pointerCallbackMap = new WeakHashMap<Pointer, Reference<Callback>[]>();
    static final Map<Object, Object> allocations = Collections.synchronizedMap(new WeakHashMap());
    private static final Map<CallbackReference, Reference<CallbackReference>> allocatedMemory = Collections.synchronizedMap(new WeakHashMap());
    private static final Method PROXY_CALLBACK_METHOD;
    private static final Class<?> DLL_CALLBACK_CLASS;
    private static final Map<Callback, CallbackThreadInitializer> initializers;
    Pointer cbstruct;
    Pointer trampoline;
    CallbackProxy proxy;
    Method method;
    int callingConvention;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static CallbackThreadInitializer setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer) {
        Map<Callback, CallbackThreadInitializer> map = initializers;
        synchronized (map) {
            if (initializer != null) {
                return initializers.put(cb, initializer);
            }
            return initializers.remove(cb);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static ThreadGroup initializeThread(Callback cb, AttachOptions args) {
        CallbackThreadInitializer init = null;
        if (cb instanceof DefaultCallbackProxy) {
            cb = ((DefaultCallbackProxy)cb).getCallback();
        }
        Map<Callback, CallbackThreadInitializer> map = initializers;
        synchronized (map) {
            init = initializers.get(cb);
        }
        ThreadGroup group = null;
        if (init != null) {
            group = init.getThreadGroup(cb);
            args.name = init.getName(cb);
            args.daemon = init.isDaemon(cb);
            args.detach = init.detach(cb);
            args.write();
        }
        return group;
    }

    public static Callback getCallback(Class<?> type, Pointer p2) {
        return CallbackReference.getCallback(type, p2, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Callback getCallback(Class<?> type, Pointer p2, boolean direct) {
        if (p2 == null) {
            return null;
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Callback type must be an interface");
        }
        Map<Callback, CallbackReference> map = direct ? directCallbackMap : callbackMap;
        Map<Pointer, Reference<Callback>[]> map2 = pointerCallbackMap;
        synchronized (map2) {
            Reference<Callback>[] array = pointerCallbackMap.get(p2);
            Callback cb = CallbackReference.getTypeAssignableCallback(type, array);
            if (cb != null) {
                return cb;
            }
            cb = CallbackReference.createCallback(type, p2);
            pointerCallbackMap.put(p2, CallbackReference.addCallbackToArray(cb, array));
            map.remove(cb);
            return cb;
        }
    }

    private static Callback getTypeAssignableCallback(Class<?> type, Reference<Callback>[] array) {
        if (array != null) {
            for (int i2 = 0; i2 < array.length; ++i2) {
                Callback cb = array[i2].get();
                if (cb == null || !type.isAssignableFrom(cb.getClass())) continue;
                return cb;
            }
        }
        return null;
    }

    private static Reference<Callback>[] addCallbackToArray(Callback cb, Reference<Callback>[] array) {
        int reqArraySize = 1;
        if (array != null) {
            for (int i2 = 0; i2 < array.length; ++i2) {
                if (array[i2].get() == null) {
                    array[i2] = null;
                    continue;
                }
                ++reqArraySize;
            }
        }
        Reference[] newArray = new Reference[reqArraySize];
        int nidx = 0;
        if (array != null) {
            for (int i3 = 0; i3 < array.length; ++i3) {
                if (array[i3] == null) continue;
                newArray[nidx++] = array[i3];
            }
        }
        newArray[nidx] = new WeakReference<Callback>(cb);
        return newArray;
    }

    private static Callback createCallback(Class<?> type, Pointer p2) {
        int ctype = AltCallingConvention.class.isAssignableFrom(type) ? 63 : 0;
        HashMap<String, Object> foptions = new HashMap<String, Object>(Native.getLibraryOptions(type));
        foptions.put("invoking-method", CallbackReference.getCallbackMethod(type));
        NativeFunctionHandler h2 = new NativeFunctionHandler(p2, ctype, foptions);
        return (Callback)Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, h2);
    }

    private CallbackReference(Callback callback, int callingConvention, boolean direct) {
        super(callback);
        TypeMapper mapper = Native.getTypeMapper(callback.getClass());
        this.callingConvention = callingConvention;
        boolean ppc = Platform.isPPC();
        if (direct) {
            Method m2 = CallbackReference.getCallbackMethod(callback);
            Class<?>[] ptypes = m2.getParameterTypes();
            for (int i2 = 0; i2 < ptypes.length; ++i2) {
                if (ppc && (ptypes[i2] == Float.TYPE || ptypes[i2] == Double.TYPE)) {
                    direct = false;
                    break;
                }
                if (mapper == null || mapper.getFromNativeConverter(ptypes[i2]) == null) continue;
                direct = false;
                break;
            }
            if (mapper != null && mapper.getToNativeConverter(m2.getReturnType()) != null) {
                direct = false;
            }
        }
        String encoding = Native.getStringEncoding(callback.getClass());
        long peer = 0L;
        if (direct) {
            this.method = CallbackReference.getCallbackMethod(callback);
            Class<?>[] nativeParamTypes = this.method.getParameterTypes();
            Class<?> returnType = this.method.getReturnType();
            int flags = 1;
            if (DLL_CALLBACK_CLASS != null && DLL_CALLBACK_CLASS.isInstance(callback)) {
                flags |= 2;
            }
            peer = Native.createNativeCallback(callback, this.method, nativeParamTypes, returnType, callingConvention, flags, encoding);
        } else {
            this.proxy = callback instanceof CallbackProxy ? (CallbackProxy)callback : new DefaultCallbackProxy(CallbackReference.getCallbackMethod(callback), mapper, encoding);
            Class<?>[] nativeParamTypes = this.proxy.getParameterTypes();
            Class<?> returnType = this.proxy.getReturnType();
            if (mapper != null) {
                for (int i3 = 0; i3 < nativeParamTypes.length; ++i3) {
                    FromNativeConverter rc = mapper.getFromNativeConverter(nativeParamTypes[i3]);
                    if (rc == null) continue;
                    nativeParamTypes[i3] = rc.nativeType();
                }
                ToNativeConverter tn = mapper.getToNativeConverter(returnType);
                if (tn != null) {
                    returnType = tn.nativeType();
                }
            }
            for (int i4 = 0; i4 < nativeParamTypes.length; ++i4) {
                nativeParamTypes[i4] = this.getNativeType(nativeParamTypes[i4]);
                if (CallbackReference.isAllowableNativeType(nativeParamTypes[i4])) continue;
                String msg = "Callback argument " + nativeParamTypes[i4] + " requires custom type conversion";
                throw new IllegalArgumentException(msg);
            }
            if (!CallbackReference.isAllowableNativeType(returnType = this.getNativeType(returnType))) {
                String msg = "Callback return type " + returnType + " requires custom type conversion";
                throw new IllegalArgumentException(msg);
            }
            int flags = DLL_CALLBACK_CLASS != null && DLL_CALLBACK_CLASS.isInstance(callback) ? 2 : 0;
            peer = Native.createNativeCallback(this.proxy, PROXY_CALLBACK_METHOD, nativeParamTypes, returnType, callingConvention, flags, encoding);
        }
        this.cbstruct = peer != 0L ? new Pointer(peer) : null;
        allocatedMemory.put(this, new WeakReference<CallbackReference>(this));
    }

    private Class<?> getNativeType(Class<?> cls) {
        if (Structure.class.isAssignableFrom(cls)) {
            Structure.validate(cls);
            if (!Structure.ByValue.class.isAssignableFrom(cls)) {
                return Pointer.class;
            }
        } else {
            if (NativeMapped.class.isAssignableFrom(cls)) {
                return NativeMappedConverter.getInstance(cls).nativeType();
            }
            if (cls == String.class || cls == WString.class || cls == String[].class || cls == WString[].class || Callback.class.isAssignableFrom(cls)) {
                return Pointer.class;
            }
        }
        return cls;
    }

    private static Method checkMethod(Method m2) {
        if (m2.getParameterTypes().length > 256) {
            String msg = "Method signature exceeds the maximum parameter count: " + m2;
            throw new UnsupportedOperationException(msg);
        }
        return m2;
    }

    static Class<?> findCallbackClass(Class<?> type) {
        if (!Callback.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(type.getName() + " is not derived from com.sun.jna.Callback");
        }
        if (type.isInterface()) {
            return type;
        }
        Class<?>[] ifaces = type.getInterfaces();
        for (int i2 = 0; i2 < ifaces.length; ++i2) {
            if (!Callback.class.isAssignableFrom(ifaces[i2])) continue;
            try {
                CallbackReference.getCallbackMethod(ifaces[i2]);
                return ifaces[i2];
            }
            catch (IllegalArgumentException e2) {
                break;
            }
        }
        if (Callback.class.isAssignableFrom(type.getSuperclass())) {
            return CallbackReference.findCallbackClass(type.getSuperclass());
        }
        return type;
    }

    private static Method getCallbackMethod(Callback callback) {
        return CallbackReference.getCallbackMethod(CallbackReference.findCallbackClass(callback.getClass()));
    }

    private static Method getCallbackMethod(Class<?> cls) {
        Method[] pubMethods = cls.getDeclaredMethods();
        Method[] classMethods = cls.getMethods();
        HashSet<Method> pmethods = new HashSet<Method>(Arrays.asList(pubMethods));
        pmethods.retainAll(Arrays.asList(classMethods));
        Iterator i2 = pmethods.iterator();
        while (i2.hasNext()) {
            Method m2 = (Method)i2.next();
            if (!Callback.FORBIDDEN_NAMES.contains(m2.getName())) continue;
            i2.remove();
        }
        Method[] methods = pmethods.toArray(new Method[0]);
        if (methods.length == 1) {
            return CallbackReference.checkMethod(methods[0]);
        }
        for (int i3 = 0; i3 < methods.length; ++i3) {
            Method m3 = methods[i3];
            if (!"callback".equals(m3.getName())) continue;
            return CallbackReference.checkMethod(m3);
        }
        String msg = "Callback must implement a single public method, or one public method named 'callback'";
        throw new IllegalArgumentException(msg);
    }

    private void setCallbackOptions(int options) {
        this.cbstruct.setInt(Native.POINTER_SIZE, options);
    }

    public Pointer getTrampoline() {
        if (this.trampoline == null) {
            this.trampoline = this.cbstruct.getPointer(0L);
        }
        return this.trampoline;
    }

    protected void finalize() {
        this.dispose();
    }

    protected synchronized void dispose() {
        if (this.cbstruct != null) {
            try {
                Native.freeNativeCallback(this.cbstruct.peer);
            }
            finally {
                this.cbstruct.peer = 0L;
                this.cbstruct = null;
                allocatedMemory.remove(this);
            }
        }
    }

    static void disposeAll() {
        LinkedList<CallbackReference> refs = new LinkedList<CallbackReference>(allocatedMemory.keySet());
        for (CallbackReference r2 : refs) {
            r2.dispose();
        }
    }

    private Callback getCallback() {
        return (Callback)this.get();
    }

    private static Pointer getNativeFunctionPointer(Callback cb) {
        InvocationHandler handler;
        if (Proxy.isProxyClass(cb.getClass()) && (handler = Proxy.getInvocationHandler(cb)) instanceof NativeFunctionHandler) {
            return ((NativeFunctionHandler)handler).getPointer();
        }
        return null;
    }

    public static Pointer getFunctionPointer(Callback cb) {
        return CallbackReference.getFunctionPointer(cb, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Pointer getFunctionPointer(Callback cb, boolean direct) {
        Pointer fp = null;
        if (cb == null) {
            return null;
        }
        fp = CallbackReference.getNativeFunctionPointer(cb);
        if (fp != null) {
            return fp;
        }
        Map<String, Object> options = Native.getLibraryOptions(cb.getClass());
        int callingConvention = cb instanceof AltCallingConvention ? 63 : (options != null && options.containsKey("calling-convention") ? (Integer)options.get("calling-convention") : 0);
        Map<Callback, CallbackReference> map = direct ? directCallbackMap : callbackMap;
        Map<Pointer, Reference<Callback>[]> map2 = pointerCallbackMap;
        synchronized (map2) {
            CallbackReference cbref = map.get(cb);
            if (cbref == null) {
                cbref = new CallbackReference(cb, callingConvention, direct);
                map.put(cb, cbref);
                pointerCallbackMap.put(cbref.getTrampoline(), CallbackReference.addCallbackToArray(cb, null));
                if (initializers.containsKey(cb)) {
                    cbref.setCallbackOptions(1);
                }
            }
            return cbref.getTrampoline();
        }
    }

    private static boolean isAllowableNativeType(Class<?> cls) {
        return cls == Void.TYPE || cls == Void.class || cls == Boolean.TYPE || cls == Boolean.class || cls == Byte.TYPE || cls == Byte.class || cls == Short.TYPE || cls == Short.class || cls == Character.TYPE || cls == Character.class || cls == Integer.TYPE || cls == Integer.class || cls == Long.TYPE || cls == Long.class || cls == Float.TYPE || cls == Float.class || cls == Double.TYPE || cls == Double.class || Structure.ByValue.class.isAssignableFrom(cls) && Structure.class.isAssignableFrom(cls) || Pointer.class.isAssignableFrom(cls);
    }

    private static Pointer getNativeString(Object value, boolean wide) {
        if (value != null) {
            NativeString ns = new NativeString(value.toString(), wide);
            allocations.put(value, ns);
            return ns.getPointer();
        }
        return null;
    }

    static {
        try {
            PROXY_CALLBACK_METHOD = CallbackProxy.class.getMethod("callback", Object[].class);
        }
        catch (Exception e2) {
            throw new Error("Error looking up CallbackProxy.callback() method");
        }
        if (Platform.isWindows()) {
            try {
                DLL_CALLBACK_CLASS = Class.forName("com.sun.jna.win32.DLLCallback");
            }
            catch (ClassNotFoundException e3) {
                throw new Error("Error loading DLLCallback class", e3);
            }
        } else {
            DLL_CALLBACK_CLASS = null;
        }
        initializers = new WeakHashMap<Callback, CallbackThreadInitializer>();
    }

    private static class NativeFunctionHandler
    implements InvocationHandler {
        private final Function function;
        private final Map<String, ?> options;

        public NativeFunctionHandler(Pointer address, int callingConvention, Map<String, ?> options) {
            this.options = options;
            this.function = new Function(address, callingConvention, (String)options.get("string-encoding"));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Library.Handler.OBJECT_TOSTRING.equals(method)) {
                String str = "Proxy interface to " + this.function;
                Method m2 = (Method)this.options.get("invoking-method");
                Class<?> cls = CallbackReference.findCallbackClass(m2.getDeclaringClass());
                str = str + " (" + cls.getName() + ")";
                return str;
            }
            if (Library.Handler.OBJECT_HASHCODE.equals(method)) {
                return this.hashCode();
            }
            if (Library.Handler.OBJECT_EQUALS.equals(method)) {
                Object o2 = args[0];
                if (o2 != null && Proxy.isProxyClass(o2.getClass())) {
                    return Function.valueOf(Proxy.getInvocationHandler(o2) == this);
                }
                return Boolean.FALSE;
            }
            if (Function.isVarArgs(method)) {
                args = Function.concatenateVarArgs(args);
            }
            return this.function.invoke(method.getReturnType(), args, this.options);
        }

        public Pointer getPointer() {
            return this.function;
        }
    }

    private class DefaultCallbackProxy
    implements CallbackProxy {
        private final Method callbackMethod;
        private ToNativeConverter toNative;
        private final FromNativeConverter[] fromNative;
        private final String encoding;

        public DefaultCallbackProxy(Method callbackMethod, TypeMapper mapper, String encoding) {
            this.callbackMethod = callbackMethod;
            this.encoding = encoding;
            Class<?>[] argTypes = callbackMethod.getParameterTypes();
            Class<?> returnType = callbackMethod.getReturnType();
            this.fromNative = new FromNativeConverter[argTypes.length];
            if (NativeMapped.class.isAssignableFrom(returnType)) {
                this.toNative = NativeMappedConverter.getInstance(returnType);
            } else if (mapper != null) {
                this.toNative = mapper.getToNativeConverter(returnType);
            }
            for (int i2 = 0; i2 < this.fromNative.length; ++i2) {
                if (NativeMapped.class.isAssignableFrom(argTypes[i2])) {
                    this.fromNative[i2] = new NativeMappedConverter(argTypes[i2]);
                    continue;
                }
                if (mapper == null) continue;
                this.fromNative[i2] = mapper.getFromNativeConverter(argTypes[i2]);
            }
            if (!callbackMethod.isAccessible()) {
                try {
                    callbackMethod.setAccessible(true);
                }
                catch (SecurityException e2) {
                    throw new IllegalArgumentException("Callback method is inaccessible, make sure the interface is public: " + callbackMethod);
                }
            }
        }

        public Callback getCallback() {
            return CallbackReference.this.getCallback();
        }

        private Object invokeCallback(Object[] args) {
            Class<?>[] paramTypes = this.callbackMethod.getParameterTypes();
            Object[] callbackArgs = new Object[args.length];
            for (int i2 = 0; i2 < args.length; ++i2) {
                Class<?> type = paramTypes[i2];
                Object arg = args[i2];
                if (this.fromNative[i2] != null) {
                    CallbackParameterContext context = new CallbackParameterContext(type, this.callbackMethod, args, i2);
                    callbackArgs[i2] = this.fromNative[i2].fromNative(arg, context);
                    continue;
                }
                callbackArgs[i2] = this.convertArgument(arg, type);
            }
            Object result = null;
            Callback cb = this.getCallback();
            if (cb != null) {
                try {
                    result = this.convertResult(this.callbackMethod.invoke(cb, callbackArgs));
                }
                catch (IllegalArgumentException e2) {
                    Native.getCallbackExceptionHandler().uncaughtException(cb, e2);
                }
                catch (IllegalAccessException e3) {
                    Native.getCallbackExceptionHandler().uncaughtException(cb, e3);
                }
                catch (InvocationTargetException e4) {
                    Native.getCallbackExceptionHandler().uncaughtException(cb, e4.getTargetException());
                }
            }
            for (int i3 = 0; i3 < callbackArgs.length; ++i3) {
                if (!(callbackArgs[i3] instanceof Structure) || callbackArgs[i3] instanceof Structure.ByValue) continue;
                ((Structure)callbackArgs[i3]).autoWrite();
            }
            return result;
        }

        @Override
        public Object callback(Object[] args) {
            try {
                return this.invokeCallback(args);
            }
            catch (Throwable t2) {
                Native.getCallbackExceptionHandler().uncaughtException(this.getCallback(), t2);
                return null;
            }
        }

        private Object convertArgument(Object value, Class<?> dstType) {
            if (value instanceof Pointer) {
                if (dstType == String.class) {
                    value = ((Pointer)value).getString(0L, this.encoding);
                } else if (dstType == WString.class) {
                    value = new WString(((Pointer)value).getWideString(0L));
                } else if (dstType == String[].class) {
                    value = ((Pointer)value).getStringArray(0L, this.encoding);
                } else if (dstType == WString[].class) {
                    value = ((Pointer)value).getWideStringArray(0L);
                } else if (Callback.class.isAssignableFrom(dstType)) {
                    value = CallbackReference.getCallback(dstType, (Pointer)value);
                } else if (Structure.class.isAssignableFrom(dstType)) {
                    if (Structure.ByValue.class.isAssignableFrom(dstType)) {
                        Object s2 = Structure.newInstance(dstType);
                        byte[] buf = new byte[((Structure)s2).size()];
                        ((Pointer)value).read(0L, buf, 0, buf.length);
                        ((Structure)s2).getPointer().write(0L, buf, 0, buf.length);
                        ((Structure)s2).read();
                        value = s2;
                    } else {
                        Object s3 = Structure.newInstance(dstType, (Pointer)value);
                        ((Structure)s3).conditionalAutoRead();
                        value = s3;
                    }
                }
            } else if ((Boolean.TYPE == dstType || Boolean.class == dstType) && value instanceof Number) {
                value = Function.valueOf(((Number)value).intValue() != 0);
            }
            return value;
        }

        private Object convertResult(Object value) {
            if (this.toNative != null) {
                value = this.toNative.toNative(value, new CallbackResultContext(this.callbackMethod));
            }
            if (value == null) {
                return null;
            }
            Class<?> cls = value.getClass();
            if (Structure.class.isAssignableFrom(cls)) {
                if (Structure.ByValue.class.isAssignableFrom(cls)) {
                    return value;
                }
                return ((Structure)value).getPointer();
            }
            if (cls == Boolean.TYPE || cls == Boolean.class) {
                return Boolean.TRUE.equals(value) ? Function.INTEGER_TRUE : Function.INTEGER_FALSE;
            }
            if (cls == String.class || cls == WString.class) {
                return CallbackReference.getNativeString(value, cls == WString.class);
            }
            if (cls == String[].class || cls == WString[].class) {
                StringArray sa = cls == String[].class ? new StringArray((String[])value, this.encoding) : new StringArray((WString[])value);
                allocations.put(value, sa);
                return sa;
            }
            if (Callback.class.isAssignableFrom(cls)) {
                return CallbackReference.getFunctionPointer((Callback)value);
            }
            return value;
        }

        @Override
        public Class<?>[] getParameterTypes() {
            return this.callbackMethod.getParameterTypes();
        }

        @Override
        public Class<?> getReturnType() {
            return this.callbackMethod.getReturnType();
        }
    }

    static class AttachOptions
    extends Structure {
        public static final List<String> FIELDS = AttachOptions.createFieldsOrder("daemon", "detach", "name");
        public boolean daemon;
        public boolean detach;
        public String name;

        AttachOptions() {
            this.setStringEncoding("utf8");
        }

        @Override
        protected List<String> getFieldOrder() {
            return FIELDS;
        }
    }
}

