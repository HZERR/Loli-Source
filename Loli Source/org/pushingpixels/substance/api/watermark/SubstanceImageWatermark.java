/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.watermark;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceImageWatermark
implements SubstanceWatermark {
    protected Image watermarkImage = null;
    private SubstanceConstants.ImageWatermarkKind kind = SubstanceConstants.ImageWatermarkKind.SCREEN_CENTER_SCALE;
    private float opacity = 0.2f;
    protected BufferedImage origImage;
    protected String origImageLocation;

    public SubstanceImageWatermark(String imageLocation) {
        if (imageLocation != null) {
            try {
                if (imageLocation.startsWith("http")) {
                    URL url = new URL(imageLocation);
                    BufferedImage tempImage = ImageIO.read(url);
                    this.origImage = SubstanceCoreUtilities.createCompatibleImage(tempImage);
                } else {
                    try {
                        this.origImage = SubstanceCoreUtilities.createCompatibleImage(ImageIO.read(new File(imageLocation)));
                    }
                    catch (IIOException iioe) {
                        this.origImage = SubstanceCoreUtilities.createCompatibleImage(ImageIO.read(SubstanceImageWatermark.class.getClassLoader().getResource(imageLocation)));
                    }
                }
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        this.origImageLocation = imageLocation;
    }

    public SubstanceImageWatermark(InputStream inputStream) {
        if (inputStream != null) {
            try {
                BufferedImage tempImage = ImageIO.read(inputStream);
                this.origImage = SubstanceCoreUtilities.createCompatibleImage(tempImage);
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        this.origImageLocation = null;
    }

    @Override
    public void drawWatermarkImage(Graphics graphics, Component c2, int x2, int y2, int width, int height) {
        if (!c2.isShowing()) {
            return;
        }
        int dx = 0;
        int dy = 0;
        Component topParent = null;
        switch (this.getKind()) {
            case SCREEN_CENTER_SCALE: 
            case SCREEN_TILE: {
                dx = c2.getLocationOnScreen().x;
                dy = c2.getLocationOnScreen().y;
                break;
            }
            case APP_ANCHOR: 
            case APP_TILE: {
                if (c2 instanceof JComponent) {
                    topParent = ((JComponent)c2).getTopLevelAncestor();
                } else {
                    Component comp = c2;
                    while (comp.getParent() != null) {
                        comp = comp.getParent();
                    }
                    topParent = comp;
                }
                dx = c2.getLocationOnScreen().x - topParent.getLocationOnScreen().x;
                dy = c2.getLocationOnScreen().y - topParent.getLocationOnScreen().y;
                break;
            }
            case APP_CENTER: {
                if (c2 instanceof JComponent) {
                    topParent = ((JComponent)c2).getTopLevelAncestor();
                } else {
                    Component comp = c2;
                    while (comp.getParent() != null) {
                        comp = comp.getParent();
                    }
                    topParent = comp;
                }
                dx = c2.getLocationOnScreen().x - topParent.getLocationOnScreen().x;
                dy = c2.getLocationOnScreen().y - topParent.getLocationOnScreen().y;
                dx -= topParent.getWidth() / 2 - this.origImage.getWidth() / 2;
                dy -= topParent.getHeight() / 2 - this.origImage.getHeight() / 2;
            }
        }
        graphics.drawImage(this.watermarkImage, x2, y2, x2 + width, y2 + height, x2 + dx, y2 + dy, x2 + dx + width, y2 + dy + height, null);
    }

    @Override
    public void previewWatermark(Graphics g2, SubstanceSkin skin, int x2, int y2, int width, int height) {
    }

    @Override
    public boolean updateWatermarkImage(SubstanceSkin skin) {
        Graphics2D graphics;
        GraphicsDevice[] gds;
        if (this.origImage == null) {
            return false;
        }
        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice gd : gds = ge.getScreenDevices()) {
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            virtualBounds = virtualBounds.union(gc.getBounds());
        }
        int screenWidth = virtualBounds.width;
        int screenHeight = virtualBounds.height;
        int origImageWidth = this.origImage.getWidth();
        int origImageHeight = this.origImage.getHeight();
        if (this.getKind() == SubstanceConstants.ImageWatermarkKind.SCREEN_CENTER_SCALE) {
            boolean isHeightFits;
            this.watermarkImage = SubstanceCoreUtilities.getBlankImage(screenWidth, screenHeight);
            graphics = (Graphics2D)this.watermarkImage.getGraphics().create();
            AlphaComposite comp = AlphaComposite.getInstance(3, this.opacity);
            graphics.setComposite(comp);
            boolean isWidthFits = origImageWidth <= screenWidth;
            boolean bl = isHeightFits = origImageHeight <= screenHeight;
            if (isWidthFits && isHeightFits) {
                graphics.drawImage((Image)this.origImage, (screenWidth - origImageWidth) / 2, (screenHeight - origImageHeight) / 2, null);
                graphics.dispose();
                return true;
            }
            if (isWidthFits) {
                double scaleFact = (double)screenHeight / (double)origImageHeight;
                int dx = (int)((double)screenWidth - scaleFact * (double)origImageWidth) / 2;
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.drawImage(this.origImage, dx, 0, screenWidth - dx, screenHeight, 0, 0, origImageWidth, origImageHeight, null);
                graphics.dispose();
                return true;
            }
            if (isHeightFits) {
                double scaleFact = (double)screenWidth / (double)origImageWidth;
                int dy = (int)((double)screenHeight - scaleFact * (double)origImageHeight) / 2;
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.drawImage(this.origImage, 0, dy, screenWidth, screenHeight - dy, 0, 0, origImageWidth, origImageHeight, null);
                graphics.dispose();
                return true;
            }
            double scaleFactY = (double)screenHeight / (double)origImageHeight;
            double scaleFactX = (double)screenWidth / (double)origImageWidth;
            double scaleFact = Math.min(scaleFactX, scaleFactY);
            int dx = Math.max(0, (int)((double)screenWidth - scaleFact * (double)origImageWidth) / 2);
            int dy = Math.max(0, (int)((double)screenHeight - scaleFact * (double)origImageHeight) / 2);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.drawImage(this.origImage, dx, dy, screenWidth - dx, screenHeight - dy, 0, 0, origImageWidth, origImageHeight, null);
            graphics.dispose();
            return true;
        }
        if (this.getKind() == SubstanceConstants.ImageWatermarkKind.SCREEN_TILE || this.getKind() == SubstanceConstants.ImageWatermarkKind.APP_TILE) {
            this.watermarkImage = SubstanceCoreUtilities.getBlankImage(screenWidth, screenHeight);
            graphics = (Graphics2D)this.watermarkImage.getGraphics().create();
            AlphaComposite comp = AlphaComposite.getInstance(3, this.opacity);
            graphics.setComposite(comp);
            int replicateX = 1 + screenWidth / origImageWidth;
            int replicateY = 1 + screenHeight / origImageHeight;
            for (int i2 = 0; i2 < replicateX; ++i2) {
                for (int j2 = 0; j2 < replicateY; ++j2) {
                    graphics.drawImage((Image)this.origImage, i2 * origImageWidth, j2 * origImageHeight, null);
                }
            }
            graphics.dispose();
            return true;
        }
        if (this.getKind() == SubstanceConstants.ImageWatermarkKind.APP_ANCHOR || this.getKind() == SubstanceConstants.ImageWatermarkKind.APP_CENTER) {
            this.watermarkImage = SubstanceCoreUtilities.getBlankImage(origImageWidth, origImageHeight);
            graphics = (Graphics2D)this.watermarkImage.getGraphics().create();
            AlphaComposite comp = AlphaComposite.getInstance(3, this.opacity);
            graphics.setComposite(comp);
            graphics.drawImage((Image)this.origImage, 0, 0, null);
            graphics.dispose();
            return true;
        }
        return false;
    }

    @Override
    public String getDisplayName() {
        return "Image";
    }

    @Override
    public void dispose() {
        this.watermarkImage = null;
    }

    public String getOrigImageLocation() {
        return this.origImageLocation;
    }

    public void setKind(SubstanceConstants.ImageWatermarkKind kind) {
        if (kind == null) {
            throw new IllegalArgumentException("Can't pass null to SubstanceImageWatermark.setKind()");
        }
        this.kind = kind;
        this.updateWatermarkImage(SubstanceLookAndFeel.getCurrentSkin(null));
    }

    public SubstanceConstants.ImageWatermarkKind getKind() {
        return this.kind;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float opacity) {
        if (opacity < 0.0f || opacity > 1.0f) {
            throw new IllegalArgumentException("SubstanceImageWatermark.setOpacity() can get value in 0.0-1.0 range, was passed value " + opacity);
        }
        this.opacity = opacity;
        this.updateWatermarkImage(SubstanceLookAndFeel.getCurrentSkin(null));
    }
}

