/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.border.FractionBasedBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomShadowOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopBezelOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopLineOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.api.watermark.SubstanceCrosshatchWatermark;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class MarinerSkin
extends SubstanceSkin {
    public static final String NAME = "Mariner";
    private BottomLineOverlayPainter menuOverlayPainter;
    private TopLineOverlayPainter toolbarOverlayPainter;
    private BottomLineOverlayPainter toolbarBottomLineOverlayPainter;
    private TopBezelOverlayPainter footerTopBezelOverlayPainter;

    public MarinerSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/mariner.colorschemes");
        SubstanceColorScheme activeScheme = schemes.get("Mariner Active");
        SubstanceColorScheme enabledScheme = schemes.get("Mariner Enabled");
        SubstanceColorScheme disabledScheme = schemes.get("Mariner Disabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        defaultSchemeBundle.registerColorScheme(activeScheme, 0.5f, ComponentState.DISABLED_SELECTED);
        defaultSchemeBundle.registerColorScheme(disabledScheme, 0.8f, ComponentState.DISABLED_UNSELECTED);
        SubstanceColorScheme activeBorderScheme = schemes.get("Mariner Active Border");
        SubstanceColorScheme enabledBorderScheme = schemes.get("Mariner Enabled Border");
        defaultSchemeBundle.registerColorScheme(activeBorderScheme, ColorSchemeAssociationKind.BORDER, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme(activeBorderScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED);
        defaultSchemeBundle.registerColorScheme(enabledBorderScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED);
        SubstanceColorScheme activeMarkScheme = schemes.get("Mariner Active Mark");
        SubstanceColorScheme enabledMarkScheme = schemes.get("Mariner Enabled Mark");
        defaultSchemeBundle.registerColorScheme(activeMarkScheme, ColorSchemeAssociationKind.MARK, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme(enabledMarkScheme, ColorSchemeAssociationKind.MARK, ComponentState.ENABLED);
        ComponentState uneditable = new ComponentState("uneditable", new ComponentStateFacet[]{ComponentStateFacet.ENABLE}, new ComponentStateFacet[]{ComponentStateFacet.EDITABLE});
        SubstanceColorScheme uneditableControls = schemes.get("Mariner Uneditable");
        defaultSchemeBundle.registerColorScheme(uneditableControls, ColorSchemeAssociationKind.FILL, uneditable);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        SubstanceColorScheme headerColorScheme = schemes.get("Mariner Header");
        SubstanceColorScheme headerBorderColorScheme = schemes.get("Mariner Header Border");
        SubstanceColorSchemeBundle headerSchemeBundle = new SubstanceColorSchemeBundle(headerColorScheme, headerColorScheme, headerColorScheme);
        headerSchemeBundle.registerColorScheme(headerColorScheme, 0.5f, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED);
        headerSchemeBundle.registerColorScheme(headerColorScheme, ComponentState.ROLLOVER_UNSELECTED);
        headerSchemeBundle.registerColorScheme(headerColorScheme, ColorSchemeAssociationKind.MARK, new ComponentState[0]);
        headerSchemeBundle.registerColorScheme(headerBorderColorScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        this.registerDecorationAreaSchemeBundle(headerSchemeBundle, headerColorScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
        SubstanceColorScheme enabledFooterScheme = schemes.get("Mariner Footer Enabled");
        SubstanceColorScheme disabledFooterScheme = schemes.get("Mariner Footer Disabled");
        SubstanceColorSchemeBundle footerSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledFooterScheme, disabledFooterScheme);
        footerSchemeBundle.registerColorScheme(activeScheme, 0.5f, ComponentState.DISABLED_SELECTED);
        footerSchemeBundle.registerColorScheme(disabledFooterScheme, 0.8f, ComponentState.DISABLED_UNSELECTED);
        SubstanceColorScheme footerEnabledBorderScheme = schemes.get("Mariner Footer Enabled Border");
        footerSchemeBundle.registerColorScheme(activeBorderScheme, ColorSchemeAssociationKind.BORDER, ComponentState.getActiveStates());
        footerSchemeBundle.registerColorScheme(activeBorderScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED);
        footerSchemeBundle.registerColorScheme(footerEnabledBorderScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED);
        SubstanceColorScheme footerEnabledMarkScheme = schemes.get("Mariner Footer Enabled Mark");
        footerSchemeBundle.registerColorScheme(activeMarkScheme, ColorSchemeAssociationKind.MARK, ComponentState.getActiveStates());
        footerSchemeBundle.registerColorScheme(footerEnabledMarkScheme, ColorSchemeAssociationKind.MARK, ComponentState.ENABLED);
        SubstanceColorScheme footerSeparatorScheme = schemes.get("Mariner Footer Separator");
        footerSchemeBundle.registerColorScheme(footerSeparatorScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        SubstanceColorScheme footerWatermarkColorScheme = schemes.get("Mariner Footer Watermark");
        this.registerDecorationAreaSchemeBundle(footerSchemeBundle, footerWatermarkColorScheme, DecorationAreaType.FOOTER, DecorationAreaType.TOOLBAR, DecorationAreaType.GENERAL);
        this.setSelectedTabFadeStart(0.15);
        this.setSelectedTabFadeEnd(0.25);
        this.footerTopBezelOverlayPainter = new TopBezelOverlayPainter(ColorSchemeSingleColorQuery.ULTRADARK, ColorSchemeSingleColorQuery.LIGHT);
        this.addOverlayPainter(this.footerTopBezelOverlayPainter, DecorationAreaType.FOOTER);
        this.menuOverlayPainter = new BottomLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                return scheme.getUltraDarkColor().darker();
            }
        });
        this.toolbarOverlayPainter = new TopLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                Color fg = scheme.getForegroundColor();
                return new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 32);
            }
        });
        this.addOverlayPainter(this.menuOverlayPainter, DecorationAreaType.HEADER);
        this.addOverlayPainter(this.toolbarOverlayPainter, DecorationAreaType.TOOLBAR);
        this.addOverlayPainter(BottomShadowOverlayPainter.getInstance(), DecorationAreaType.TOOLBAR);
        this.toolbarBottomLineOverlayPainter = new BottomLineOverlayPainter(ColorSchemeSingleColorQuery.ULTRADARK);
        this.addOverlayPainter(this.toolbarBottomLineOverlayPainter, DecorationAreaType.TOOLBAR);
        this.buttonShaper = new ClassicButtonShaper();
        this.watermark = new TextureWatermark();
        this.fillPainter = new FractionBasedFillPainter(NAME, new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.EXTRALIGHT, ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.MID});
        this.decorationPainter = new MatteDecorationPainter();
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new FractionBasedBorderPainter(NAME, new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.ULTRADARK, ColorSchemeSingleColorQuery.DARK, ColorSchemeSingleColorQuery.MID});
        this.highlightBorderPainter = new ClassicBorderPainter();
        this.watermarkScheme = schemes.get("Mariner Watermark");
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }

    private static class TextureWatermark
    implements SubstanceWatermark {
        private static Image watermarkImage = null;

        private TextureWatermark() {
        }

        @Override
        public void drawWatermarkImage(Graphics graphics, Component c2, int x2, int y2, int width, int height) {
            if (!c2.isShowing()) {
                return;
            }
            int dx = c2.getLocationOnScreen().x;
            int dy = c2.getLocationOnScreen().y;
            graphics.drawImage(watermarkImage, x2, y2, x2 + width, y2 + height, x2 + dx, y2 + dy, x2 + dx + width, y2 + dy + height, null);
        }

        @Override
        public boolean updateWatermarkImage(SubstanceSkin skin) {
            GraphicsDevice[] gds;
            Rectangle virtualBounds = new Rectangle();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            for (GraphicsDevice gd : gds = ge.getScreenDevices()) {
                GraphicsConfiguration gc = gd.getDefaultConfiguration();
                virtualBounds = virtualBounds.union(gc.getBounds());
            }
            int screenWidth = virtualBounds.width;
            int screenHeight = virtualBounds.height;
            watermarkImage = SubstanceCoreUtilities.getBlankImage(screenWidth, screenHeight);
            Graphics2D graphics = (Graphics2D)watermarkImage.getGraphics().create();
            boolean status = this.drawWatermarkImage(skin, graphics, 0, 0, screenWidth, screenHeight, false);
            graphics.dispose();
            return status;
        }

        @Override
        public void previewWatermark(Graphics g2, SubstanceSkin skin, int x2, int y2, int width, int height) {
            this.drawWatermarkImage(skin, (Graphics2D)g2, x2, y2, width, height, true);
        }

        private boolean drawWatermarkImage(SubstanceSkin skin, Graphics2D graphics, int x2, int y2, int width, int height, boolean isPreview) {
            Color stampColorAll;
            Color stampColorDark;
            SubstanceColorScheme scheme = skin.getWatermarkColorScheme();
            if (isPreview) {
                stampColorDark = scheme.isDark() ? Color.white : Color.black;
                stampColorAll = Color.lightGray;
            } else {
                stampColorDark = scheme.getWatermarkDarkColor();
                stampColorAll = scheme.getWatermarkStampColor();
            }
            graphics.setColor(stampColorAll);
            graphics.fillRect(0, 0, width, height);
            BufferedImage tile = SubstanceCoreUtilities.getBlankImage(8, 4);
            int rgbDark = stampColorDark.getRGB();
            tile.setRGB(0, 0, rgbDark);
            tile.setRGB(0, 1, rgbDark);
            tile.setRGB(0, 2, rgbDark);
            tile.setRGB(0, 3, rgbDark);
            tile.setRGB(1, 2, rgbDark);
            tile.setRGB(2, 1, rgbDark);
            tile.setRGB(3, 0, rgbDark);
            tile.setRGB(4, 0, rgbDark);
            tile.setRGB(4, 1, rgbDark);
            tile.setRGB(4, 2, rgbDark);
            tile.setRGB(4, 3, rgbDark);
            tile.setRGB(5, 0, rgbDark);
            tile.setRGB(6, 1, rgbDark);
            tile.setRGB(7, 2, rgbDark);
            Graphics2D g2d = (Graphics2D)graphics.create();
            g2d.setComposite(AlphaComposite.getInstance(3, 0.05f));
            for (int row = y2; row < y2 + height; row += 4) {
                for (int col = x2; col < x2 + width; col += 8) {
                    g2d.drawImage((Image)tile, col, row, null);
                }
            }
            g2d.dispose();
            return true;
        }

        @Override
        public String getDisplayName() {
            return SubstanceCrosshatchWatermark.getName();
        }

        public static String getName() {
            return "Crosshatch";
        }

        @Override
        public void dispose() {
            watermarkImage = null;
        }
    }
}

