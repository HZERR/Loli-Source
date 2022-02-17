/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Color;

public class SubstanceColorResource
extends Color {
    public SubstanceColorResource(Color c2) {
        super(c2.getRGB(), (c2.getRGB() & 0xFF000000) != -16777216);
    }
}

