/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.SubstancePainterUtils;
import org.pushingpixels.substance.api.painter.decoration.SubstanceDecorationPainter;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class ArcDecorationPainter
implements SubstanceDecorationPainter {
    public static final String DISPLAY_NAME = "Arc";

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public void paintDecorationArea(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        if (decorationAreaType == DecorationAreaType.PRIMARY_TITLE_PANE || decorationAreaType == DecorationAreaType.SECONDARY_TITLE_PANE) {
            this.paintTitleBackground(graphics, comp, width, height, skin.getBackgroundColorScheme(decorationAreaType));
        } else {
            this.paintExtraBackground(graphics, SubstanceCoreUtilities.getHeaderParent(comp), comp, width, height, skin.getBackgroundColorScheme(decorationAreaType));
        }
    }

    private void paintTitleBackground(Graphics2D graphics, Component comp, int width, int height, SubstanceColorScheme scheme) {
        BufferedImage rectangular = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D rgraphics = (Graphics2D)rectangular.getGraphics();
        GeneralPath clipTop = new GeneralPath();
        clipTop.moveTo(0.0f, 0.0f);
        clipTop.lineTo(width, 0.0f);
        clipTop.lineTo(width, height / 2);
        clipTop.quadTo(width / 2, height / 4, 0.0f, height / 2);
        clipTop.lineTo(0.0f, 0.0f);
        rgraphics.setClip(clipTop);
        LinearGradientPaint gradientTop = new LinearGradientPaint(0.0f, 0.0f, width, 0.0f, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{scheme.getLightColor(), scheme.getUltraLightColor(), scheme.getLightColor()}, MultipleGradientPaint.CycleMethod.REPEAT);
        rgraphics.setPaint(gradientTop);
        rgraphics.fillRect(0, 0, width, height);
        GeneralPath clipBottom = new GeneralPath();
        clipBottom.moveTo(0.0f, height);
        clipBottom.lineTo(width, height);
        clipBottom.lineTo(width, height / 2);
        clipBottom.quadTo(width / 2, height / 4, 0.0f, height / 2);
        clipBottom.lineTo(0.0f, height);
        rgraphics.setClip(clipBottom);
        LinearGradientPaint gradientBottom = new LinearGradientPaint(0.0f, 0.0f, width, 0.0f, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{scheme.getMidColor(), scheme.getLightColor(), scheme.getMidColor()}, MultipleGradientPaint.CycleMethod.REPEAT);
        rgraphics.setPaint(gradientBottom);
        rgraphics.fillRect(0, 0, width, height);
        GeneralPath mid = new GeneralPath();
        mid.moveTo(width, height / 2);
        mid.quadTo(width / 2, height / 4, 0.0f, height / 2);
        rgraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rgraphics.setClip(new Rectangle(0, 0, width, height));
        rgraphics.draw(mid);
        graphics.drawImage((Image)rectangular, 0, 0, null);
    }

    private void paintExtraBackground(Graphics2D graphics, Container parent, Component comp, int width, int height, SubstanceColorScheme scheme) {
        int pWidth;
        Point offset = SubstancePainterUtils.getOffsetInRootPaneCoords(comp);
        JRootPane rootPane = SwingUtilities.getRootPane(parent);
        JComponent titlePane = null;
        if (rootPane != null) {
            titlePane = SubstanceCoreUtilities.getTitlePane(rootPane);
        }
        int n2 = pWidth = titlePane == null ? parent.getWidth() : titlePane.getWidth();
        if (pWidth != 0) {
            LinearGradientPaint gradientBottom = new LinearGradientPaint(-offset.x, 0.0f, -offset.x + pWidth, 0.0f, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{scheme.getMidColor(), scheme.getLightColor(), scheme.getMidColor()}, MultipleGradientPaint.CycleMethod.REPEAT);
            Graphics2D g2d = (Graphics2D)graphics.create();
            g2d.setPaint(gradientBottom);
            g2d.fillRect(-offset.x, 0, pWidth, height);
            g2d.dispose();
        }
    }
}

