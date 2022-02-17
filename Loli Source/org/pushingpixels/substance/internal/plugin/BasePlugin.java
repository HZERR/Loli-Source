/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.plugin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.InsetsUIResource;
import org.pushingpixels.lafplugin.LafComponentPlugin;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.icon.SubstanceIconFactory;

public class BasePlugin
implements LafComponentPlugin {
    protected static final String commonDir = "/org/pushingpixels/substance/internal/contrib/randelshofer/quaqua/images/";
    protected static final String quaquaColorChooserClassName = "org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.Quaqua14ColorChooserUI";
    protected boolean hasQuaquaColorChooser;

    public BasePlugin() {
        try {
            Class.forName(quaquaColorChooserClassName);
            this.hasQuaquaColorChooser = true;
        }
        catch (ClassNotFoundException cnfe) {
            this.hasQuaquaColorChooser = false;
        }
    }

    protected Object makeImage(String location) {
        return new UIDefaults.ProxyLazyValue("org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.QuaquaIconFactory", "createImage", new Object[]{location});
    }

    protected static Object makeButtonStateIcon(String location, int states) {
        return new UIDefaults.ProxyLazyValue("org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.QuaquaIconFactory", "createButtonStateIcon", new Object[]{location, states});
    }

    protected Object makeBufferedImage(String location) {
        return new UIDefaults.ProxyLazyValue("org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.QuaquaIconFactory", "createBufferedImage", new Object[]{location});
    }

    public static Object makeIcon(Class baseClass, String location) {
        return new UIDefaults.ProxyLazyValue("org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.QuaquaIconFactory", "createIcon", new Object[]{baseClass, location});
    }

    @Override
    public Object[] getDefaults(Object mSkin) {
        if (this.hasQuaquaColorChooser) {
            ResourceBundle bundle = ResourceBundle.getBundle("org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.Labels");
            LinkedList<Object> labelsList = new LinkedList<Object>();
            Enumeration<String> i2 = bundle.getKeys();
            while (i2.hasMoreElements()) {
                String key = i2.nextElement();
                labelsList.add(key);
                labelsList.add(bundle.getObject(key));
            }
            SubstanceSkin skin = (SubstanceSkin)mSkin;
            final SubstanceColorScheme colorScheme = skin.getActiveColorScheme(DecorationAreaType.NONE);
            InsetsUIResource visualMargin = new InsetsUIResource(0, 0, 0, 0);
            ColorUIResource foregroundColor = new ColorUIResource(colorScheme.getForegroundColor());
            Object[] mainDefaults = new Object[]{"Slider.upThumbSmall", new UIDefaults.LazyValue(){

                @Override
                public Object createValue(UIDefaults table) {
                    return SubstanceIconFactory.getSliderHorizontalIcon(SubstanceSizeUtils.getSliderIconSize(SubstanceSizeUtils.getControlFontSize()) - 2, true);
                }
            }, "Slider.leftThumbSmall", new UIDefaults.LazyValue(){

                @Override
                public Object createValue(UIDefaults table) {
                    return SubstanceIconFactory.getSliderVerticalIcon(SubstanceSizeUtils.getSliderIconSize(SubstanceSizeUtils.getControlFontSize()) - 2, true);
                }
            }, "Component.visualMargin", visualMargin, "ColorChooser.foreground", foregroundColor, "ColorChooser.defaultChoosers", new String[]{"org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorWheelChooser", "org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSlidersChooser", "org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorPalettesChooser", "org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.SwatchesChooser", "org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.CrayonsChooser", "org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.Quaqua15ColorPicker", "org.pushingpixels.substance.internal.contrib.xoetrope.editor.color.ColorWheelPanel"}, "ColorChooser.swatchesSwatchSize", new DimensionUIResource(5, 5), "ColorChooser.resetMnemonic", -1, "ColorChooser.crayonsImage", this.makeImage("/org/pushingpixels/substance/internal/contrib/randelshofer/quaqua/images/big_crayons.png"), "ColorChooser.textSliderGap", 0, "ColorChooser.colorPalettesIcon", BasePlugin.makeButtonStateIcon("/org/pushingpixels/substance/internal/contrib/randelshofer/quaqua/images/palette.png", 1), "ColorChooser.colorSlidersIcon", BasePlugin.makeButtonStateIcon("/org/pushingpixels/substance/internal/contrib/randelshofer/quaqua/images/chart_bar.png", 1), "ColorChooser.colorSwatchesIcon", BasePlugin.makeButtonStateIcon("/org/pushingpixels/substance/internal/contrib/randelshofer/quaqua/images/color_swatch.png", 1), "ColorChooser.colorWheelIcon", BasePlugin.makeButtonStateIcon("/org/pushingpixels/substance/internal/contrib/randelshofer/quaqua/images/color_wheel.png", 1), "ColorChooser.crayonsIcon", BasePlugin.makeButtonStateIcon("/org/pushingpixels/substance/internal/contrib/randelshofer/quaqua/images/pencil.png", 1), "ColorChooser.imagePalettesIcon", BasePlugin.makeButtonStateIcon("/org/pushingpixels/substance/internal/contrib/randelshofer/quaqua/images/image.png", 1), "ColorChooser.colorPickerIcon", new UIDefaults.LazyValue(){

                @Override
                public Object createValue(UIDefaults table) {
                    return new IconUIResource(SubstanceImageCreator.getSearchIcon(15, colorScheme, true));
                }
            }, "ColorChooser.colorPickerMagnifier", new UIDefaults.LazyValue(){

                @Override
                public Object createValue(UIDefaults table) {
                    BufferedImage result = SubstanceCoreUtilities.getBlankImage(48, 48);
                    Graphics2D g2 = result.createGraphics();
                    g2.setColor(Color.black);
                    g2.translate(-4, -6);
                    int xc = 20;
                    int yc = 22;
                    int r2 = 15;
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawOval(xc - r2, yc - r2, 2 * r2, 2 * r2);
                    g2.setStroke(new BasicStroke(4.0f));
                    GeneralPath handle = new GeneralPath();
                    handle.moveTo((float)((double)xc + (double)r2 / Math.sqrt(2.0)), (float)((double)yc + (double)r2 / Math.sqrt(2.0)));
                    handle.lineTo(45.0f, 47.0f);
                    g2.draw(handle);
                    g2.translate(4, 6);
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.drawLine(16, 4, 16, 13);
                    g2.drawLine(4, 16, 13, 16);
                    g2.drawLine(16, 19, 16, 28);
                    g2.drawLine(19, 16, 28, 16);
                    return result;
                }
            }, "ColorChooser.colorPickerHotSpot", new UIDefaults.ProxyLazyValue("java.awt.Point", new Object[]{29, 29}), "ColorChooser.colorPickerPickOffset", new UIDefaults.ProxyLazyValue("java.awt.Point", new Object[]{-13, -13}), "ColorChooser.colorPickerGlassRect", new UIDefaults.ProxyLazyValue("java.awt.Rectangle", new Object[]{3, 3, 26, 26}), "ColorChooser.colorPickerCaptureRect", new UIDefaults.ProxyLazyValue("java.awt.Rectangle", new Object[]{-15, -15, 5, 5}), "ColorChooser.colorPickerZoomRect", new UIDefaults.ProxyLazyValue("java.awt.Rectangle", new Object[]{4, 4, 25, 25})};
            FontSet substanceFontSet = SubstanceLookAndFeel.getFontPolicy().getFontSet("Substance", null);
            FontUIResource controlFont = substanceFontSet.getControlFont();
            FontUIResource fontBoldBaseP1 = new FontUIResource(controlFont.deriveFont(1, controlFont.getSize() + 1));
            FontUIResource fontPlainBaseM2 = new FontUIResource(controlFont.deriveFont((float)(controlFont.getSize() - 2)));
            Object[] colorDefaults = new Object[]{"ColorChooserUI", "org.pushingpixels.substance.internal.ui.SubstanceColorChooserUI", "ColorChooser.font", controlFont, "ColorChooser.smallFont", fontPlainBaseM2, "ColorChooser.crayonsFont", fontBoldBaseP1};
            Object[] labelDefaults = new Object[mainDefaults.length + labelsList.size()];
            for (int i3 = 0; i3 < mainDefaults.length; ++i3) {
                labelDefaults[i3] = mainDefaults[i3];
            }
            int start = mainDefaults.length;
            for (int i4 = 0; i4 < labelsList.size(); ++i4) {
                labelDefaults[start + i4] = labelsList.get(i4);
            }
            mainDefaults = labelDefaults;
            if (colorDefaults != null) {
                int i5;
                Object[] defaults = new Object[mainDefaults.length + colorDefaults.length];
                for (i5 = 0; i5 < mainDefaults.length; ++i5) {
                    defaults[i5] = mainDefaults[i5];
                }
                start = mainDefaults.length;
                for (i5 = 0; i5 < colorDefaults.length; ++i5) {
                    defaults[start + i5] = colorDefaults[i5];
                }
                return defaults;
            }
            return mainDefaults;
        }
        return new Object[0];
    }

    @Override
    public void uninitialize() {
    }

    @Override
    public void initialize() {
    }
}

