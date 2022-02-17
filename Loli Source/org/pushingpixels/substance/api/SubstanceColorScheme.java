/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

import java.awt.Color;
import org.pushingpixels.substance.api.SchemeBaseColors;
import org.pushingpixels.substance.api.SchemeDerivedColors;
import org.pushingpixels.substance.api.trait.SubstanceTrait;

public interface SubstanceColorScheme
extends SubstanceTrait,
SchemeBaseColors,
SchemeDerivedColors {
    public boolean isDark();

    public SubstanceColorScheme shift(Color var1, double var2, Color var4, double var5);

    public SubstanceColorScheme shiftBackground(Color var1, double var2);

    public SubstanceColorScheme tint(double var1);

    public SubstanceColorScheme tone(double var1);

    public SubstanceColorScheme shade(double var1);

    public SubstanceColorScheme saturate(double var1);

    public SubstanceColorScheme invert();

    public SubstanceColorScheme negate();

    public SubstanceColorScheme hueShift(double var1);

    public SubstanceColorScheme named(String var1);
}

