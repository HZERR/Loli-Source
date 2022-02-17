/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.colorscheme.ShiftColorScheme;

public class ShadeColorScheme
extends ShiftColorScheme {
    public ShadeColorScheme(SubstanceColorScheme origColorScheme, double shadeFactor) {
        super(origColorScheme, Color.black, shadeFactor);
    }
}

