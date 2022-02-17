/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.BaseColorScheme;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class ShiftColorScheme
extends BaseColorScheme {
    private double backgroundShiftFactor;
    private double foregroundShiftFactor;
    private Color backgroundShiftColor;
    private Color foregroundShiftColor;
    private Color mainUltraLightColor;
    private Color mainExtraLightColor;
    private Color mainLightColor;
    private Color mainMidColor;
    private Color mainDarkColor;
    private Color mainUltraDarkColor;
    private Color foregroundColor;
    private SubstanceColorScheme origScheme;
    protected static final LazyResettableHashMap<SubstanceColorScheme> shiftedCache = new LazyResettableHashMap("ShiftColorScheme.shiftedSchemes");

    public ShiftColorScheme(SubstanceColorScheme origScheme, Color shiftColor, double shiftFactor) {
        this(origScheme, shiftColor, shiftFactor, shiftColor, shiftFactor / 2.0, false);
    }

    public ShiftColorScheme(SubstanceColorScheme origScheme, Color backgroundShiftColor, double backgroundShiftFactor, Color foregroundShiftColor, double foregroundShiftFactor, boolean shiftByBrightness) {
        super("Shift " + origScheme.getDisplayName() + " to backgr [" + backgroundShiftColor + "] " + (int)(100.0 * backgroundShiftFactor) + "%, foregr [" + foregroundShiftColor + "]" + (int)(100.0 * foregroundShiftFactor) + "%", ShiftColorScheme.getResolver(origScheme));
        this.backgroundShiftColor = backgroundShiftColor;
        this.backgroundShiftFactor = backgroundShiftFactor;
        this.foregroundShiftColor = foregroundShiftColor;
        this.foregroundShiftFactor = foregroundShiftFactor;
        this.origScheme = origScheme;
        this.foregroundColor = this.foregroundShiftColor != null ? SubstanceColorUtilities.getInterpolatedColor(this.foregroundShiftColor, origScheme.getForegroundColor(), this.foregroundShiftFactor) : origScheme.getForegroundColor();
        shiftByBrightness = shiftByBrightness && this.backgroundShiftColor != null;
        Color ultraDarkToShiftTo = shiftByBrightness ? SubstanceColorUtilities.deriveByBrightness(this.backgroundShiftColor, origScheme.getUltraDarkColor()) : this.backgroundShiftColor;
        this.mainUltraDarkColor = this.backgroundShiftColor != null ? SubstanceColorUtilities.getInterpolatedColor(ultraDarkToShiftTo, origScheme.getUltraDarkColor(), this.backgroundShiftFactor) : origScheme.getUltraDarkColor();
        Color darkToShiftTo = shiftByBrightness ? SubstanceColorUtilities.deriveByBrightness(this.backgroundShiftColor, origScheme.getDarkColor()) : this.backgroundShiftColor;
        this.mainDarkColor = this.backgroundShiftColor != null ? SubstanceColorUtilities.getInterpolatedColor(darkToShiftTo, origScheme.getDarkColor(), this.backgroundShiftFactor) : origScheme.getDarkColor();
        Color midToShiftTo = shiftByBrightness ? SubstanceColorUtilities.deriveByBrightness(this.backgroundShiftColor, origScheme.getMidColor()) : this.backgroundShiftColor;
        this.mainMidColor = this.backgroundShiftColor != null ? SubstanceColorUtilities.getInterpolatedColor(midToShiftTo, origScheme.getMidColor(), this.backgroundShiftFactor) : origScheme.getMidColor();
        Color lightToShiftTo = shiftByBrightness ? SubstanceColorUtilities.deriveByBrightness(this.backgroundShiftColor, origScheme.getLightColor()) : this.backgroundShiftColor;
        this.mainLightColor = this.backgroundShiftColor != null ? SubstanceColorUtilities.getInterpolatedColor(lightToShiftTo, origScheme.getLightColor(), this.backgroundShiftFactor) : origScheme.getLightColor();
        Color extraLightToShiftTo = shiftByBrightness ? SubstanceColorUtilities.deriveByBrightness(this.backgroundShiftColor, origScheme.getExtraLightColor()) : this.backgroundShiftColor;
        this.mainExtraLightColor = this.backgroundShiftColor != null ? SubstanceColorUtilities.getInterpolatedColor(extraLightToShiftTo, origScheme.getExtraLightColor(), this.backgroundShiftFactor) : origScheme.getExtraLightColor();
        Color ultraLightToShiftTo = shiftByBrightness ? SubstanceColorUtilities.deriveByBrightness(this.backgroundShiftColor, origScheme.getUltraLightColor()) : this.backgroundShiftColor;
        this.mainUltraLightColor = this.backgroundShiftColor != null ? SubstanceColorUtilities.getInterpolatedColor(ultraLightToShiftTo, origScheme.getUltraLightColor(), this.backgroundShiftFactor) : origScheme.getUltraLightColor();
    }

    @Override
    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    @Override
    public Color getUltraLightColor() {
        return this.mainUltraLightColor;
    }

    @Override
    public Color getExtraLightColor() {
        return this.mainExtraLightColor;
    }

    @Override
    public Color getLightColor() {
        return this.mainLightColor;
    }

    @Override
    public Color getMidColor() {
        return this.mainMidColor;
    }

    @Override
    public Color getDarkColor() {
        return this.mainDarkColor;
    }

    @Override
    public Color getUltraDarkColor() {
        return this.mainUltraDarkColor;
    }

    public SubstanceColorScheme getOrigScheme() {
        return this.origScheme;
    }

    public double getShiftFactor() {
        return this.backgroundShiftFactor;
    }

    public static SubstanceColorScheme getShiftedScheme(SubstanceColorScheme orig, Color backgroundShiftColor, double backgroundShiftFactor, Color foregroundShiftColor, double foregroundShiftFactor) {
        HashMapKey key = SubstanceCoreUtilities.getHashKey(orig.getDisplayName(), backgroundShiftColor == null ? "" : Integer.valueOf(backgroundShiftColor.getRGB()), backgroundShiftFactor, foregroundShiftColor == null ? "" : Integer.valueOf(foregroundShiftColor.getRGB()), foregroundShiftFactor);
        SubstanceColorScheme result = shiftedCache.get(key);
        if (result == null) {
            result = orig.shift(backgroundShiftColor, backgroundShiftFactor, foregroundShiftColor, foregroundShiftFactor);
            shiftedCache.put(key, result);
        }
        return result;
    }
}

