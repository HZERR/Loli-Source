/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceBorder
implements Border,
UIResource {
    protected Insets myInsets;
    protected float alpha = 1.0f;
    protected float radiusScaleFactor = 0.5f;
    private static LazyResettableHashMap<BufferedImage> smallImageCache = new LazyResettableHashMap("SubstanceBorder");

    public SubstanceBorder() {
    }

    public SubstanceBorder(float radiusScaleFactor) {
        this();
        this.radiusScaleFactor = radiusScaleFactor;
    }

    public SubstanceBorder(Insets insets) {
        this();
        this.myInsets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    private void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height, boolean isEnabled, boolean hasFocus, float alpha) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        if (alpha == 0.0f) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        float radius = this.radiusScaleFactor * SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(c2));
        ComponentState state = isEnabled ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
        SubstanceColorScheme borderColorScheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.BORDER, state);
        float finalAlpha = alpha * SubstanceColorSchemeUtilities.getAlpha(c2, state);
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(c2, finalAlpha, g2));
        if (width * height < 100000) {
            HashMapKey hashKey = SubstanceCoreUtilities.getHashKey(SubstanceCoreUtilities.getBorderPainter(c2).getDisplayName(), SubstanceSizeUtils.getComponentFontSize(c2), width, height, Float.valueOf(radius), borderColorScheme.getDisplayName());
            BufferedImage result = smallImageCache.get(hashKey);
            if (result == null) {
                result = SubstanceCoreUtilities.getBlankImage(width, height);
                Graphics2D g2d = result.createGraphics();
                SubstanceImageCreator.paintBorder(c2, g2d, 0, 0, width, height, radius, borderColorScheme);
                g2d.dispose();
                smallImageCache.put(hashKey, result);
            }
            graphics.drawImage((Image)result, x2, y2, null);
        } else {
            graphics.translate(x2, y2);
            SubstanceImageCreator.paintSimpleBorder(c2, graphics, width, height, borderColorScheme);
        }
        graphics.dispose();
    }

    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
        this.paintBorder(c2, g2, x2, y2, width, height, c2.isEnabled(), c2.hasFocus(), this.alpha);
    }

    @Override
    public Insets getBorderInsets(Component c2) {
        if (this.myInsets == null) {
            return SubstanceSizeUtils.getDefaultBorderInsets(SubstanceSizeUtils.getComponentFontSize(c2));
        }
        return this.myInsets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    public float getRadiusScaleFactor() {
        return this.radiusScaleFactor;
    }
}

