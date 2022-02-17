/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.BaseColorScheme;

class CopyMutableColorScheme
extends BaseColorScheme {
    Color foregroundColor;
    Color ultraLightColor;
    Color extraLightColor;
    Color lightColor;
    Color midColor;
    Color darkColor;
    Color ultraDarkColor;

    public CopyMutableColorScheme(String name, SubstanceColorScheme copy) {
        super(name, copy.isDark());
        this.foregroundColor = copy.getForegroundColor();
        this.ultraLightColor = copy.getUltraLightColor();
        this.extraLightColor = copy.getExtraLightColor();
        this.lightColor = copy.getLightColor();
        this.midColor = copy.getMidColor();
        this.darkColor = copy.getDarkColor();
        this.ultraDarkColor = copy.getUltraDarkColor();
    }

    public void setDark(boolean isDark) {
        this.isDark = isDark;
    }

    @Override
    public Color getDarkColor() {
        return this.darkColor;
    }

    public void setDarkColor(Color darkColor) {
        this.darkColor = darkColor;
    }

    @Override
    public Color getExtraLightColor() {
        return this.extraLightColor;
    }

    public void setExtraLightColor(Color extraLightColor) {
        this.extraLightColor = extraLightColor;
    }

    @Override
    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    @Override
    public Color getLightColor() {
        return this.lightColor;
    }

    public void setLightColor(Color lightColor) {
        this.lightColor = lightColor;
    }

    @Override
    public Color getMidColor() {
        return this.midColor;
    }

    public void setMidColor(Color midColor) {
        this.midColor = midColor;
    }

    @Override
    public Color getUltraDarkColor() {
        return this.ultraDarkColor;
    }

    public void setUltraDarkColor(Color ultraDarkColor) {
        this.ultraDarkColor = ultraDarkColor;
    }

    @Override
    public Color getUltraLightColor() {
        return this.ultraLightColor;
    }

    public void setUltraLightColor(Color ultraLightColor) {
        this.ultraLightColor = ultraLightColor;
    }
}

