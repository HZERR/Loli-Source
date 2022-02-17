/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Shape;
import org.pushingpixels.substance.api.ColorSchemeTransform;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.StandardBorderPainter;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class DelegateBorderPainter
extends StandardBorderPainter {
    protected String displayName;
    protected StandardBorderPainter delegate;
    protected int topMask;
    protected int midMask;
    protected int bottomMask;
    protected ColorSchemeTransform transform;
    protected static final LazyResettableHashMap<SubstanceColorScheme> transformMap = new LazyResettableHashMap("DelegateBorderPainter");

    public DelegateBorderPainter(String displayName, StandardBorderPainter delegate, ColorSchemeTransform transform) {
        this(displayName, delegate, -1, -1, -1, transform);
    }

    public DelegateBorderPainter(String displayName, StandardBorderPainter delegate, int topMask, int midMask, int bottomMask, ColorSchemeTransform transform) {
        this.displayName = displayName;
        this.delegate = delegate;
        this.topMask = topMask;
        this.midMask = midMask;
        this.bottomMask = bottomMask;
        this.transform = transform;
    }

    @Override
    public Color getTopBorderColor(SubstanceColorScheme borderScheme) {
        return new Color(this.topMask & this.delegate.getTopBorderColor(borderScheme).getRGB(), true);
    }

    @Override
    public Color getMidBorderColor(SubstanceColorScheme borderScheme) {
        return new Color(this.midMask & this.delegate.getMidBorderColor(borderScheme).getRGB(), true);
    }

    @Override
    public Color getBottomBorderColor(SubstanceColorScheme borderScheme) {
        return new Color(this.bottomMask & this.delegate.getBottomBorderColor(borderScheme).getRGB(), true);
    }

    @Override
    public void paintBorder(Graphics g2, Component c2, int width, int height, Shape contour, Shape innerContour, SubstanceColorScheme borderScheme) {
        super.paintBorder(g2, c2, width, height, contour, innerContour, this.getShiftScheme(borderScheme));
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    private SubstanceColorScheme getShiftScheme(SubstanceColorScheme orig) {
        HashMapKey key = SubstanceCoreUtilities.getHashKey(orig.getDisplayName(), this.getDisplayName(), this.transform);
        SubstanceColorScheme result = transformMap.get(key);
        if (result == null) {
            result = this.transform.transform(orig);
            transformMap.put(key, result);
        }
        return result;
    }
}

