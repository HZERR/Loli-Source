/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.util.Arrays;
import java.util.List;

public class ChangeHelper {
    public static String addRemoveChangeToString(int n2, int n3, List<?> list, List<?> list2) {
        StringBuilder stringBuilder = new StringBuilder();
        if (list2.isEmpty()) {
            stringBuilder.append(list.subList(n2, n3));
            stringBuilder.append(" added at ").append(n2);
        } else {
            stringBuilder.append(list2);
            if (n2 == n3) {
                stringBuilder.append(" removed at ").append(n2);
            } else {
                stringBuilder.append(" replaced by ");
                stringBuilder.append(list.subList(n2, n3));
                stringBuilder.append(" at ").append(n2);
            }
        }
        return stringBuilder.toString();
    }

    public static String permChangeToString(int[] arrn) {
        return "permutated by " + Arrays.toString(arrn);
    }

    public static String updateChangeToString(int n2, int n3) {
        return "updated at range [" + n2 + ", " + n3 + ")";
    }
}

