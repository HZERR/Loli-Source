/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.colorscheme.ShiftColorScheme;

public class ToneColorScheme
extends ShiftColorScheme {
    public ToneColorScheme(SubstanceColorScheme origColorScheme, double toneFactor) {
        super(origColorScheme, Color.gray, toneFactor);
    }
}

