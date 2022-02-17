/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.fonts;

import javax.swing.plaf.FontUIResource;
import org.pushingpixels.substance.api.fonts.FontSet;

public class ScaledFontSet
implements FontSet {
    private float scaleFactor;
    private FontSet delegate;

    public ScaledFontSet(FontSet delegate, float scaleFactor) {
        this.delegate = delegate;
        this.scaleFactor = scaleFactor;
    }

    private FontUIResource getWrappedFont(FontUIResource systemFont) {
        return new FontUIResource(systemFont.getFontName(), systemFont.getStyle(), (int)((float)systemFont.getSize() * this.scaleFactor));
    }

    @Override
    public FontUIResource getControlFont() {
        return this.getWrappedFont(this.delegate.getControlFont());
    }

    @Override
    public FontUIResource getMenuFont() {
        return this.getWrappedFont(this.delegate.getMenuFont());
    }

    @Override
    public FontUIResource getMessageFont() {
        return this.getWrappedFont(this.delegate.getMessageFont());
    }

    @Override
    public FontUIResource getSmallFont() {
        return this.getWrappedFont(this.delegate.getSmallFont());
    }

    @Override
    public FontUIResource getTitleFont() {
        return this.getWrappedFont(this.delegate.getTitleFont());
    }

    @Override
    public FontUIResource getWindowTitleFont() {
        return this.getWrappedFont(this.delegate.getWindowTitleFont());
    }
}

