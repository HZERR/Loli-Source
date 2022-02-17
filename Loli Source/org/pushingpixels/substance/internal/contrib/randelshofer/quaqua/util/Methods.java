/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Methods {
    private Methods() {
    }

    public static Object invoke(Object obj, String methodName) throws NoSuchMethodException {
        try {
            Method method = obj.getClass().getMethod(methodName, new Class[0]);
            Object result = method.invoke(obj, new Object[0]);
            return result;
        }
        catch (IllegalAccessException e2) {
            throw new NoSuchMethodException(methodName + " is not accessible");
        }
        catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    public static Object invoke(Object obj, String methodName, String stringParameter) throws NoSuchMethodException {
        try {
            Method method = obj.getClass().getMethod(methodName, String.class);
            Object result = method.invoke(obj, stringParameter);
            return result;
        }
        catch (IllegalAccessException e2) {
            throw new NoSuchMethodException(methodName + " is not accessible");
        }
        catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    public static Object invoke(Object obj, String methodName, Class[] types, Object[] values) throws NoSuchMethodException {
        try {
            Method method = obj.getClass().getMethod(methodName, types);
            Object result = method.invoke(obj, values);
            return result;
        }
        catch (IllegalAccessException e2) {
            throw new NoSuchMethodException(methodName + " is not accessible");
        }
        catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    public static Object invokeStatic(Class clazz, String methodName) throws NoSuchMethodException {
        try {
            Method method = clazz.getMethod(methodName, new Class[0]);
            Object result = method.invoke(null, new Object[0]);
            return result;
        }
        catch (IllegalAccessException e2) {
            throw new NoSuchMethodException(methodName + " is not accessible");
        }
        catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    public static Object invokeStatic(String clazz, String methodName) throws NoSuchMethodException {
        try {
            return Methods.invokeStatic(Class.forName(clazz), methodName);
        }
        catch (ClassNotFoundException e2) {
            throw new NoSuchMethodException("class " + clazz + " not found");
        }
    }

    public static Object invokeStatic(Class clazz, String methodName, Class[] types, Object[] values) throws NoSuchMethodException {
        try {
            Method method = clazz.getMethod(methodName, types);
            Object result = method.invoke(null, values);
            return result;
        }
        catch (IllegalAccessException e2) {
            throw new NoSuchMethodException(methodName + " is not accessible");
        }
        catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    public static Object invokeStatic(String clazz, String methodName, Class[] types, Object[] values) throws NoSuchMethodException {
        try {
            return Methods.invokeStatic(Class.forName(clazz), methodName, types, values);
        }
        catch (ClassNotFoundException e2) {
            throw new NoSuchMethodException("class " + clazz + " not found");
        }
    }

    public static Object invokeStatic(String clazz, String methodName, Class type, Object value) throws NoSuchMethodException {
        try {
            return Methods.invokeStatic(Class.forName(clazz), methodName, new Class[]{type}, new Object[]{value});
        }
        catch (ClassNotFoundException e2) {
            throw new NoSuchMethodException("class " + clazz + " not found");
        }
    }

    public static Object invokeStatic(String clazz, String methodName, Class[] types, Object[] values, Object defaultValue) {
        try {
            return Methods.invokeStatic(Class.forName(clazz), methodName, types, values);
        }
        catch (ClassNotFoundException e2) {
            return defaultValue;
        }
        catch (NoSuchMethodException e3) {
            return defaultValue;
        }
    }

    public static Object invokeStatic(Class clazz, String methodName, Class type, Object value) throws NoSuchMethodException {
        return Methods.invokeStatic(clazz, methodName, new Class[]{type}, new Object[]{value});
    }

    public static int invokeGetter(Object obj, String methodName, int defaultValue) {
        try {
            Method method = obj.getClass().getMethod(methodName, new Class[0]);
            Object result = method.invoke(obj, new Object[0]);
            return (Integer)result;
        }
        catch (NoSuchMethodException e2) {
            return defaultValue;
        }
        catch (IllegalAccessException e3) {
            return defaultValue;
        }
        catch (InvocationTargetException e4) {
            return defaultValue;
        }
    }

    public static long invokeGetter(Object obj, String methodName, long defaultValue) {
        try {
            Method method = obj.getClass().getMethod(methodName, new Class[0]);
            Object result = method.invoke(obj, new Object[0]);
            return (Long)result;
        }
        catch (NoSuchMethodException e2) {
            return defaultValue;
        }
        catch (IllegalAccessException e3) {
            return defaultValue;
        }
        catch (InvocationTargetException e4) {
            return defaultValue;
        }
    }

    public static boolean invokeGetter(Object obj, String methodName, boolean defaultValue) {
        try {
            Method method = obj.getClass().getMethod(methodName, new Class[0]);
            Object result = method.invoke(obj, new Object[0]);
            return (Boolean)result;
        }
        catch (NoSuchMethodException e2) {
            return defaultValue;
        }
        catch (IllegalAccessException e3) {
            return defaultValue;
        }
        catch (InvocationTargetException e4) {
            return defaultValue;
        }
    }

    public static Object invokeGetter(Object obj, String methodName, Object defaultValue) {
        try {
            Method method = obj.getClass().getMethod(methodName, new Class[0]);
            Object result = method.invoke(obj, new Object[0]);
            return result;
        }
        catch (NoSuchMethodException e2) {
            return defaultValue;
        }
        catch (IllegalAccessException e3) {
            return defaultValue;
        }
        catch (InvocationTargetException e4) {
            return defaultValue;
        }
    }

    public static boolean invokeStaticGetter(Class clazz, String methodName, boolean defaultValue) {
        try {
            Method method = clazz.getMethod(methodName, new Class[0]);
            Object result = method.invoke(null, new Object[0]);
            return (Boolean)result;
        }
        catch (NoSuchMethodException e2) {
            return defaultValue;
        }
        catch (IllegalAccessException e3) {
            return defaultValue;
        }
        catch (InvocationTargetException e4) {
            return defaultValue;
        }
    }

    public static Object invoke(Object obj, String methodName, boolean newValue) throws NoSuchMethodException {
        try {
            Method method = obj.getClass().getMethod(methodName, Boolean.TYPE);
            return method.invoke(obj, newValue);
        }
        catch (IllegalAccessException e2) {
            throw new NoSuchMethodException(methodName + " is not accessible");
        }
        catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    public static Object invoke(Object obj, String methodName, int newValue) throws NoSuchMethodException {
        try {
            Method method = obj.getClass().getMethod(methodName, Integer.TYPE);
            return method.invoke(obj, newValue);
        }
        catch (IllegalAccessException e2) {
            throw new NoSuchMethodException(methodName + " is not accessible");
        }
        catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    public static Object invoke(Object obj, String methodName, float newValue) throws NoSuchMethodException {
        try {
            Method method = obj.getClass().getMethod(methodName, Float.TYPE);
            return method.invoke(obj, Float.valueOf(newValue));
        }
        catch (IllegalAccessException e2) {
            throw new NoSuchMethodException(methodName + " is not accessible");
        }
        catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    public static Object invoke(Object obj, String methodName, Class clazz, Object newValue) throws NoSuchMethodException {
        try {
            Method method = obj.getClass().getMethod(methodName, clazz);
            return method.invoke(obj, newValue);
        }
        catch (IllegalAccessException e2) {
            throw new NoSuchMethodException(methodName + " is not accessible");
        }
        catch (InvocationTargetException e3) {
            throw new InternalError(e3.getMessage());
        }
    }

    public static void invokeIfExists(Object obj, String methodName) {
        try {
            Methods.invoke(obj, methodName);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
    }

    public static void invokeIfExists(Object obj, String methodName, int newValue) {
        try {
            Methods.invoke(obj, methodName, newValue);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
    }

    public static void invokeIfExists(Object obj, String methodName, float newValue) {
        try {
            Methods.invoke(obj, methodName, newValue);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
    }

    public static void invokeIfExists(Object obj, String methodName, boolean newValue) {
        try {
            Methods.invoke(obj, methodName, newValue);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
    }

    public static void invokeIfExists(Object obj, String methodName, Class parameterClass, Object newValue) {
        try {
            Methods.invoke(obj, methodName, parameterClass, newValue);
        }
        catch (NoSuchMethodException e2) {
            // empty catch block
        }
    }

    public static void invokeIfExistsWithEnum(Object obj, String methodName, String enumClassName, String enumValueName) {
        try {
            Class<?> enumClass = Class.forName(enumClassName);
            Object enumValue = Methods.invokeStatic("java.lang.Enum", "valueOf", new Class[]{Class.class, String.class}, new Object[]{enumClass, enumValueName});
            Methods.invoke(obj, methodName, enumClass, enumValue);
        }
        catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }
    }

    public static Object newInstance(Class clazz, Class[] types, Object[] values) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return clazz.getConstructor(types).newInstance(values);
    }
}

