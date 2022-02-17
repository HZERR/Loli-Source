/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.text;

import java.text.Normalizer;

final class TextNormalizer {
    private static final int FORM_NFC = 0;
    private static final int FORM_NFD = 1;
    private static final int FORM_NFKC = 2;
    private static final int FORM_NFKD = 3;

    TextNormalizer() {
    }

    private static String normalize(String string, int n2) {
        Normalizer.Form form;
        switch (n2) {
            case 0: {
                form = Normalizer.Form.NFC;
                break;
            }
            case 1: {
                form = Normalizer.Form.NFD;
                break;
            }
            case 2: {
                form = Normalizer.Form.NFKC;
                break;
            }
            case 3: {
                form = Normalizer.Form.NFKD;
                break;
            }
            default: {
                throw new IllegalArgumentException("invalid type: " + n2);
            }
        }
        return Normalizer.normalize(string, form);
    }
}

