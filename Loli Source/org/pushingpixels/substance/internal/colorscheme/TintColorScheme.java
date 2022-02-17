/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.colorscheme.ShiftColorScheme;

public class TintColorScheme
extends ShiftColorScheme {
    public TintColorScheme(SubstanceColorScheme origColorScheme, double tintFactor) {
        super(origColorScheme, Color.white, tintFactor);
    }
}

