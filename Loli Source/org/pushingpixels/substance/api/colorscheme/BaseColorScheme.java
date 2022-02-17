/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SchemeDerivedColorsResolver;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.DerivedColorsResolverDark;
import org.pushingpixels.substance.api.colorscheme.DerivedColorsResolverLight;
import org.pushingpixels.substance.internal.colorscheme.HueShiftColorScheme;
import org.pushingpixels.substance.internal.colorscheme.InvertedColorScheme;
import org.pushingpixels.substance.internal.colorscheme.NegatedColorScheme;
import org.pushingpixels.substance.internal.colorscheme.SaturatedColorScheme;
import org.pushingpixels.substance.internal.colorscheme.ShadeColorScheme;
import org.pushingpixels.substance.internal.colorscheme.ShiftColorScheme;
import org.pushingpixels.substance.internal.colorscheme.TintColorScheme;
import org.pushingpixels.substance.internal.colorscheme.ToneColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public abstract class BaseColorScheme
implements SubstanceColorScheme {
    protected boolean isDark;
    protected String displayName;
    protected SchemeDerivedColorsResolver derivedColorsResolver;

    protected BaseColorScheme(String displayName, boolean isDark) {
        this(displayName, isDark, isDark ? DerivedColorsResolverDark.INSTANCE : DerivedColorsResolverLight.INSTANCE);
    }

    protected BaseColorScheme(String displayName, SchemeDerivedColorsResolver derivedColorsResolver) {
        this(displayName, derivedColorsResolver.isDark(), derivedColorsResolver);
    }

    private BaseColorScheme(String displayName, boolean isDark, SchemeDerivedColorsResolver derivedColorsResolver) {
        if (derivedColorsResolver == null) {
            throw new NullPointerException("derivedColorsResolver cannot be null");
        }
        this.displayName = displayName;
        this.isDark = isDark;
        this.derivedColorsResolver = derivedColorsResolver;
    }

    protected static SchemeDerivedColorsResolver getResolver(SubstanceColorScheme colorScheme) {
        if (colorScheme instanceof BaseColorScheme) {
            return ((BaseColorScheme)colorScheme).derivedColorsResolver;
        }
        return colorScheme.isDark() ? DerivedColorsResolverDark.INSTANCE : DerivedColorsResolverLight.INSTANCE;
    }

    @Override
    public final String getDisplayName() {
        return this.displayName;
    }

    @Override
    public final boolean isDark() {
        return this.isDark;
    }

    @Override
    public final SubstanceColorScheme shift(Color backgroundShiftColor, double backgroundShiftFactor, Color foregroundShiftColor, double foregroundShiftFactor) {
        return new ShiftColorScheme(this, backgroundShiftColor, backgroundShiftFactor, foregroundShiftColor, foregroundShiftFactor, true);
    }

    @Override
    public final SubstanceColorScheme shiftBackground(Color backgroundShiftColor, double backgroundShiftFactor) {
        return this.shift(backgroundShiftColor, backgroundShiftFactor, null, 0.0);
    }

    @Override
    public SubstanceColorScheme tint(double tintFactor) {
        return new TintColorScheme(this, tintFactor);
    }

    @Override
    public SubstanceColorScheme tone(double toneFactor) {
        return new ToneColorScheme(this, toneFactor);
    }

    @Override
    public SubstanceColorScheme shade(double shadeFactor) {
        return new ShadeColorScheme(this, shadeFactor);
    }

    @Override
    public SubstanceColorScheme saturate(double saturateFactor) {
        return new SaturatedColorScheme(this, saturateFactor);
    }

    @Override
    public SubstanceColorScheme invert() {
        return new InvertedColorScheme(this);
    }

    @Override
    public SubstanceColorScheme negate() {
        return new NegatedColorScheme(this);
    }

    @Override
    public SubstanceColorScheme hueShift(double hueShiftFactor) {
        return new HueShiftColorScheme(this, hueShiftFactor);
    }

    @Override
    public final Color getBackgroundFillColor() {
        return this.derivedColorsResolver.getBackgroundFillColor(this);
    }

    @Override
    public final Color getFocusRingColor() {
        return this.derivedColorsResolver.getFocusRingColor(this);
    }

    @Override
    public final Color getLineColor() {
        return this.derivedColorsResolver.getLineColor(this);
    }

    @Override
    public final Color getSelectionForegroundColor() {
        return this.derivedColorsResolver.getSelectionForegroundColor(this);
    }

    @Override
    public final Color getSelectionBackgroundColor() {
        return this.derivedColorsResolver.getSelectionBackgroundColor(this);
    }

    @Override
    public final Color getWatermarkDarkColor() {
        return this.derivedColorsResolver.getWatermarkDarkColor(this);
    }

    @Override
    public final Color getWatermarkLightColor() {
        return this.derivedColorsResolver.getWatermarkLightColor(this);
    }

    @Override
    public final Color getWatermarkStampColor() {
        return this.derivedColorsResolver.getWatermarkStampColor(this);
    }

    @Override
    public final Color getTextBackgroundFillColor() {
        return this.derivedColorsResolver.getTextBackgroundFillColor(this);
    }

    @Override
    public final SubstanceColorScheme named(String colorSchemeDisplayName) {
        this.displayName = colorSchemeDisplayName;
        return this;
    }

    public String toString() {
        return this.getDisplayName() + " {\n    kind=" + (this.isDark() ? "Dark" : "Light") + "\n    colorUltraLight=" + SubstanceColorUtilities.encode(this.getUltraLightColor()) + "\n    colorExtraLight=" + SubstanceColorUtilities.encode(this.getExtraLightColor()) + "\n    colorLight=" + SubstanceColorUtilities.encode(this.getLightColor()) + "\n    colorMid=" + SubstanceColorUtilities.encode(this.getMidColor()) + "\n    colorDark=" + SubstanceColorUtilities.encode(this.getDarkColor()) + "\n    colorUltraDark=" + SubstanceColorUtilities.encode(this.getUltraDarkColor()) + "\n    colorForeground=" + SubstanceColorUtilities.encode(this.getForegroundColor()) + "\n}";
    }
}

