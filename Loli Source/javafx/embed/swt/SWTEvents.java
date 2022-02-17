/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.SWT
 *  org.eclipse.swt.events.MouseEvent
 */
package javafx.embed.swt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

class SWTEvents {
    static final int[][] KeyTable = new int[][]{{0, 0}, {10, 13}, {10, 10}, {8, 8}, {9, 9}, {27, 27}, {32, 32}, {127, 127}, {155, 0x1000009}, {156, 0x1000051}, {16, 131072}, {17, 262144}, {18, 65536}, {524, 0x400000}, {20, 16777298}, {144, 16777299}, {145, 16777300}, {33, 0x1000005}, {34, 0x1000006}, {35, 0x1000008}, {36, 0x1000007}, {37, 0x1000003}, {38, 0x1000001}, {39, 0x1000004}, {40, 0x1000002}, {44, 44}, {45, 45}, {46, 46}, {47, 47}, {59, 59}, {61, 61}, {91, 91}, {92, 92}, {93, 93}, {106, 16777258}, {107, 16777259}, {109, 16777261}, {110, 16777262}, {111, 16777263}, {150, 64}, {151, 42}, {152, 34}, {153, 60}, {160, 62}, {161, 123}, {162, 125}, {192, 96}, {222, 39}, {512, 64}, {513, 58}, {514, 94}, {515, 36}, {517, 33}, {519, 40}, {520, 35}, {521, 43}, {522, 41}, {523, 95}, {48, 48}, {49, 49}, {50, 50}, {51, 51}, {52, 52}, {53, 53}, {54, 54}, {55, 55}, {56, 56}, {57, 57}, {65, 97}, {66, 98}, {67, 99}, {68, 100}, {69, 101}, {70, 102}, {71, 103}, {72, 104}, {73, 105}, {74, 106}, {75, 107}, {76, 108}, {77, 109}, {78, 110}, {79, 111}, {80, 112}, {81, 113}, {82, 114}, {83, 115}, {84, 116}, {85, 117}, {86, 118}, {87, 119}, {88, 120}, {89, 121}, {90, 122}, {96, 0x1000030}, {97, 0x1000031}, {98, 16777266}, {99, 0x1000033}, {100, 16777268}, {101, 16777269}, {102, 16777270}, {103, 16777271}, {104, 16777272}, {105, 16777273}, {112, 0x100000A}, {113, 0x100000B}, {114, 0x100000C}, {115, 0x100000D}, {116, 0x100000E}, {117, 0x100000F}, {118, 0x1000010}, {119, 0x1000011}, {120, 0x1000012}, {121, 0x1000013}, {122, 0x1000014}, {123, 0x1000015}};

    SWTEvents() {
    }

    static int mouseButtonToEmbedMouseButton(int n2, int n3) {
        switch (n2) {
            case 1: {
                return 1;
            }
            case 2: {
                return 4;
            }
            case 3: {
                return 2;
            }
        }
        return 0;
    }

    static int getWheelRotation(MouseEvent mouseEvent, int n2) {
        int n3 = 1;
        if (n2 == 7) {
            if ("win32".equals(SWT.getPlatform())) {
                int[] arrn = new int[1];
                try {
                    Class<?> class_ = Class.forName("org.eclipse.swt.internal.win32.OS");
                    Method method = class_.getDeclaredMethod("SystemParametersInfo", Integer.TYPE, Integer.TYPE, int[].class, Integer.TYPE);
                    method.invoke(class_, 104, 0, arrn, 0);
                }
                catch (IllegalAccessException illegalAccessException) {
                }
                catch (InvocationTargetException invocationTargetException) {
                }
                catch (NoSuchMethodException noSuchMethodException) {
                }
                catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
                if (arrn[0] != -1) {
                    n3 = arrn[0];
                }
            } else if ("gtk".equals(SWT.getPlatform())) {
                n3 = 3;
            }
            return -mouseEvent.count / Math.max(1, n3);
        }
        return 0;
    }

    static int keyIDToEmbedKeyType(int n2) {
        switch (n2) {
            case 1: {
                return 0;
            }
            case 2: {
                return 1;
            }
        }
        return 0;
    }

    static int keyCodeToEmbedKeyCode(int n2) {
        for (int i2 = 0; i2 < KeyTable.length; ++i2) {
            if (KeyTable[i2][1] != n2) continue;
            return KeyTable[i2][0];
        }
        return 0;
    }

    static int keyModifiersToEmbedKeyModifiers(int n2) {
        int n3 = 0;
        if ((n2 & 0x20000) != 0) {
            n3 |= 1;
        }
        if ((n2 & 0x40000) != 0) {
            n3 |= 2;
        }
        if ((n2 & 0x10000) != 0) {
            n3 |= 4;
        }
        if ((n2 & 0x400000) != 0) {
            n3 |= 8;
        }
        return n3;
    }
}

