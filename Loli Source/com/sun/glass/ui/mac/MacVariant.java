/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import javafx.geometry.Bounds;

final class MacVariant {
    static final int NSArray_id = 1;
    static final int NSArray_NSString = 2;
    static final int NSArray_int = 3;
    static final int NSArray_range = 4;
    static final int NSAttributedString = 5;
    static final int NSData = 6;
    static final int NSDate = 7;
    static final int NSDictionary = 8;
    static final int NSNumber_Boolean = 9;
    static final int NSNumber_Int = 10;
    static final int NSNumber_Float = 11;
    static final int NSNumber_Double = 12;
    static final int NSString = 13;
    static final int NSURL = 14;
    static final int NSValue_point = 15;
    static final int NSValue_size = 16;
    static final int NSValue_rectangle = 17;
    static final int NSValue_range = 18;
    static final int NSObject = 19;
    int type;
    long[] longArray;
    int[] intArray;
    String[] stringArray;
    MacVariant[] variantArray;
    float float1;
    float float2;
    float float3;
    float float4;
    int int1;
    int int2;
    String string;
    long long1;
    double double1;
    int location;
    int length;
    long key;

    MacVariant() {
    }

    static MacVariant createNSArray(Object object) {
        MacVariant macVariant = new MacVariant();
        macVariant.type = 1;
        macVariant.longArray = (long[])object;
        return macVariant;
    }

    static MacVariant createNSObject(Object object) {
        MacVariant macVariant = new MacVariant();
        macVariant.type = 19;
        macVariant.long1 = (Long)object;
        return macVariant;
    }

    static MacVariant createNSString(Object object) {
        MacVariant macVariant = new MacVariant();
        macVariant.type = 13;
        macVariant.string = (String)object;
        return macVariant;
    }

    static MacVariant createNSAttributedString(Object object) {
        MacVariant macVariant = new MacVariant();
        macVariant.type = 5;
        macVariant.string = (String)object;
        return macVariant;
    }

    static MacVariant createNSDate(Object object) {
        MacVariant macVariant = new MacVariant();
        macVariant.type = 7;
        macVariant.long1 = ((LocalDate)object).atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        return macVariant;
    }

    static MacVariant createNSValueForSize(Object object) {
        Bounds bounds = (Bounds)object;
        MacVariant macVariant = new MacVariant();
        macVariant.type = 16;
        macVariant.float1 = (float)bounds.getWidth();
        macVariant.float2 = (float)bounds.getHeight();
        return macVariant;
    }

    static MacVariant createNSValueForPoint(Object object) {
        Bounds bounds = (Bounds)object;
        MacVariant macVariant = new MacVariant();
        macVariant.type = 15;
        macVariant.float1 = (float)bounds.getMinX();
        macVariant.float2 = (float)bounds.getMinY();
        return macVariant;
    }

    static MacVariant createNSValueForRectangle(Object object) {
        Bounds bounds = (Bounds)object;
        MacVariant macVariant = new MacVariant();
        macVariant.type = 17;
        macVariant.float1 = (float)bounds.getMinX();
        macVariant.float2 = (float)bounds.getMinY();
        macVariant.float3 = (float)bounds.getWidth();
        macVariant.float4 = (float)bounds.getHeight();
        return macVariant;
    }

    static MacVariant createNSValueForRange(Object object) {
        int[] arrn = (int[])object;
        MacVariant macVariant = new MacVariant();
        macVariant.type = 18;
        macVariant.int1 = arrn[0];
        macVariant.int2 = arrn[1];
        return macVariant;
    }

    static MacVariant createNSNumberForBoolean(Object object) {
        Boolean bl = (Boolean)object;
        MacVariant macVariant = new MacVariant();
        macVariant.type = 9;
        macVariant.int1 = bl != false ? 1 : 0;
        return macVariant;
    }

    static MacVariant createNSNumberForDouble(Object object) {
        MacVariant macVariant = new MacVariant();
        macVariant.type = 12;
        macVariant.double1 = (Double)object;
        return macVariant;
    }

    static MacVariant createNSNumberForInt(Object object) {
        MacVariant macVariant = new MacVariant();
        macVariant.type = 10;
        macVariant.int1 = (Integer)object;
        return macVariant;
    }

    Object getValue() {
        switch (this.type) {
            case 9: {
                return this.int1 != 0;
            }
            case 10: {
                return this.int1;
            }
            case 12: {
                return this.double1;
            }
            case 1: {
                return this.longArray;
            }
            case 3: {
                return this.intArray;
            }
            case 18: {
                return new int[]{this.int1, this.int2};
            }
            case 15: {
                return new float[]{this.float1, this.float2};
            }
            case 16: {
                return new float[]{this.float1, this.float2};
            }
            case 17: {
                return new float[]{this.float1, this.float2, this.float3, this.float4};
            }
            case 13: {
                return this.string;
            }
            case 5: {
                return this.string;
            }
        }
        return null;
    }

    public String toString() {
        Object object = this.getValue();
        switch (this.type) {
            case 1: {
                object = Arrays.toString((long[])object);
                break;
            }
            case 3: {
                object = Arrays.toString((int[])object);
                break;
            }
            case 18: {
                object = Arrays.toString((int[])object);
                break;
            }
            case 5: {
                object = object + Arrays.toString(this.variantArray);
                break;
            }
            case 8: {
                object = "keys: " + Arrays.toString(this.longArray) + " values: " + Arrays.toString(this.variantArray);
            }
        }
        return "MacVariant type: " + this.type + " value " + object;
    }
}

