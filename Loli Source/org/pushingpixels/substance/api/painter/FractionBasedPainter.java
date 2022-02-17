/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter;

import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.trait.SubstanceTrait;

public abstract class FractionBasedPainter
implements SubstanceTrait {
    private String displayName;
    protected float[] fractions;
    protected ColorSchemeSingleColorQuery[] colorQueries;

    public FractionBasedPainter(String displayName, float[] fractions, ColorSchemeSingleColorQuery[] colorQueries) {
        int i2;
        this.displayName = displayName;
        if (fractions == null || colorQueries == null) {
            throw new IllegalArgumentException("Cannot pass null arguments");
        }
        if (fractions.length != colorQueries.length) {
            throw new IllegalArgumentException("Argument length does not match");
        }
        int length = fractions.length;
        if (fractions[0] != 0.0f || fractions[length - 1] != 1.0f) {
            throw new IllegalArgumentException("End fractions must be 0.0 and 1.0");
        }
        for (i2 = 0; i2 < length - 1; ++i2) {
            if (!(fractions[i2 + 1] <= fractions[i2])) continue;
            throw new IllegalArgumentException("Fractions must be strictly increasing");
        }
        for (i2 = 0; i2 < length; ++i2) {
            if (colorQueries[i2] != null) continue;
            throw new IllegalArgumentException("Cannot pass null query");
        }
        this.colorQueries = new ColorSchemeSingleColorQuery[length];
        System.arraycopy(colorQueries, 0, this.colorQueries, 0, length);
        this.fractions = new float[length];
        System.arraycopy(fractions, 0, this.fractions, 0, length);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public float[] getFractions() {
        float[] result = new float[this.fractions.length];
        System.arraycopy(this.fractions, 0, result, 0, this.fractions.length);
        return result;
    }

    public ColorSchemeSingleColorQuery[] getColorQueries() {
        ColorSchemeSingleColorQuery[] result = new ColorSchemeSingleColorQuery[this.colorQueries.length];
        System.arraycopy(this.colorQueries, 0, result, 0, this.colorQueries.length);
        return result;
    }
}

