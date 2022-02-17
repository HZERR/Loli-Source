/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.fill;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class StandardFillPainter
implements SubstanceFillPainter {
    @Override
    public String getDisplayName() {
        return "Standard";
    }

    @Override
    public void paintContourBackground(Graphics g2, Component comp, int width, int height, Shape contour, boolean isFocused, SubstanceColorScheme fillScheme, boolean hasShine) {
        Graphics2D graphics = (Graphics2D)g2.create();
        Color topFillColor = this.getTopFillColor(fillScheme);
        Color midFillColorTop = this.getMidFillColorTop(fillScheme);
        Color midFillColorBottom = this.getMidFillColorBottom(fillScheme);
        Color bottomFillColor = this.getBottomFillColor(fillScheme);
        Color topShineColor = this.getTopShineColor(fillScheme);
        Color bottomShineColor = this.getBottomShineColor(fillScheme);
        LinearGradientPaint gradient = new LinearGradientPaint(0.0f, 0.0f, 0.0f, height, new float[]{0.0f, 0.4999999f, 0.5f, 1.0f}, new Color[]{topFillColor, midFillColorTop, midFillColorBottom, bottomFillColor}, MultipleGradientPaint.CycleMethod.REPEAT);
        graphics.setPaint(gradient);
        graphics.fill(contour);
        if (hasShine && topShineColor != null && bottomShineColor != null) {
            graphics.clip(contour);
            int shineHeight = (int)((double)height / 1.8);
            int kernelSize = (int)Math.min(12.0, Math.pow(Math.min(width, height), 0.8) / 4.0);
            if (kernelSize < 3) {
                kernelSize = 3;
            }
            BufferedImage blurredGhostContour = SubstanceCoreUtilities.getBlankImage(width + 2 * kernelSize, height + 2 * kernelSize);
            Graphics2D blurredGhostGraphics = (Graphics2D)blurredGhostContour.getGraphics().create();
            blurredGhostGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            blurredGhostGraphics.setColor(Color.black);
            blurredGhostGraphics.translate(kernelSize, kernelSize);
            int step = kernelSize > 5 ? 2 : 1;
            for (int strokeSize = 2 * kernelSize - 1; strokeSize > 0; strokeSize -= step) {
                float transp = 1.0f - (float)strokeSize / (2.0f * (float)kernelSize);
                blurredGhostGraphics.setComposite(AlphaComposite.getInstance(2, transp));
                blurredGhostGraphics.setStroke(new BasicStroke(strokeSize));
                blurredGhostGraphics.draw(contour);
            }
            blurredGhostGraphics.dispose();
            BufferedImage reverseGhostContour = SubstanceCoreUtilities.getBlankImage(width + 2 * kernelSize, height + 2 * kernelSize);
            Graphics2D reverseGraphics = (Graphics2D)reverseGhostContour.getGraphics();
            Color bottomShineColorTransp = new Color(bottomShineColor.getRed(), bottomShineColor.getGreen(), bottomShineColor.getBlue(), 64);
            GradientPaint gradientShine = new GradientPaint(0.0f, kernelSize, topShineColor, 0.0f, kernelSize + shineHeight, bottomShineColorTransp, true);
            reverseGraphics.setPaint(gradientShine);
            reverseGraphics.fillRect(0, kernelSize, width + 2 * kernelSize, kernelSize + shineHeight);
            reverseGraphics.setComposite(AlphaComposite.DstOut);
            reverseGraphics.drawImage((Image)blurredGhostContour, 0, 0, null);
            graphics.drawImage(reverseGhostContour, 0, 0, width - 1, shineHeight, kernelSize, kernelSize, kernelSize + width - 1, kernelSize + shineHeight, null);
            BufferedImage overGhostContour = SubstanceCoreUtilities.getBlankImage(width + 2 * kernelSize, height + 2 * kernelSize);
            Graphics2D overGraphics = (Graphics2D)overGhostContour.getGraphics();
            overGraphics.setPaint(new GradientPaint(0.0f, kernelSize, topFillColor, 0.0f, kernelSize + height / 2, midFillColorTop, true));
            overGraphics.fillRect(kernelSize, kernelSize, kernelSize + width, kernelSize + shineHeight);
            overGraphics.setComposite(AlphaComposite.DstIn);
            overGraphics.drawImage((Image)blurredGhostContour, 0, 0, null);
            graphics.drawImage(overGhostContour, 0, 0, width - 1, shineHeight, kernelSize, kernelSize, kernelSize + width - 1, kernelSize + shineHeight, null);
        }
        graphics.dispose();
    }

    public Color getTopFillColor(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getTopFillColor(fillScheme);
    }

    public Color getMidFillColorTop(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getMidFillColor(fillScheme);
    }

    public Color getMidFillColorBottom(SubstanceColorScheme fillScheme) {
        return this.getMidFillColorTop(fillScheme);
    }

    public Color getBottomFillColor(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getBottomFillColor(fillScheme);
    }

    public Color getTopShineColor(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getTopShineColor(fillScheme);
    }

    public Color getBottomShineColor(SubstanceColorScheme fillScheme) {
        return SubstanceColorUtilities.getBottomShineColor(fillScheme);
    }
}

