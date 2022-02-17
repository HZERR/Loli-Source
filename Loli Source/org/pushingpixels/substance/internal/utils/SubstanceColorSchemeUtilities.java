/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SchemeBaseColors;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.BaseDarkColorScheme;
import org.pushingpixels.substance.api.colorscheme.BaseLightColorScheme;
import org.pushingpixels.substance.api.colorscheme.BottleGreenColorScheme;
import org.pushingpixels.substance.api.colorscheme.LightGrayColorScheme;
import org.pushingpixels.substance.api.colorscheme.MetallicColorScheme;
import org.pushingpixels.substance.api.colorscheme.SunGlareColorScheme;
import org.pushingpixels.substance.api.colorscheme.SunfireRedColorScheme;
import org.pushingpixels.substance.internal.colorscheme.ShiftColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceInternalArrowButton;
import org.pushingpixels.substance.internal.utils.SubstanceTitleButton;

public class SubstanceColorSchemeUtilities {
    public static final SubstanceSkin METALLIC_SKIN = SubstanceColorSchemeUtilities.getMetallicSkin();
    public static final SubstanceColorScheme YELLOW = new SunGlareColorScheme();
    public static final SubstanceColorScheme ORANGE = new SunfireRedColorScheme();
    public static final SubstanceColorScheme GREEN = new BottleGreenColorScheme();

    private static SubstanceSkin getMetallicSkin() {
        SubstanceSkin res = new SubstanceSkin(){

            @Override
            public String getDisplayName() {
                return "Metallic Skin";
            }
        };
        res.registerDecorationAreaSchemeBundle(new SubstanceColorSchemeBundle(new MetallicColorScheme(), new MetallicColorScheme(), new LightGrayColorScheme()), DecorationAreaType.NONE);
        return res;
    }

    private static SubstanceColorScheme getColorizedScheme(Component component, SubstanceColorScheme scheme, boolean isEnabled) {
        Component forQuerying = component;
        if (component != null && component.getParent() != null && (component instanceof SubstanceInternalArrowButton || component instanceof SubstanceTitleButton)) {
            forQuerying = component.getParent();
        }
        return SubstanceColorSchemeUtilities.getColorizedScheme(component, scheme, forQuerying == null ? null : forQuerying.getForeground(), forQuerying == null ? null : forQuerying.getBackground(), isEnabled);
    }

    private static SubstanceColorScheme getColorizedScheme(Component component, SubstanceColorScheme scheme, Color fgColor, Color bgColor, boolean isEnabled) {
        if (component != null) {
            if (bgColor instanceof UIResource) {
                bgColor = null;
            }
            if (fgColor instanceof UIResource) {
                fgColor = null;
            }
            if (bgColor != null || fgColor != null) {
                double colorization = SubstanceCoreUtilities.getColorizationFactor(component);
                if (!isEnabled) {
                    colorization /= 2.0;
                }
                if (colorization > 0.0) {
                    return ShiftColorScheme.getShiftedScheme(scheme, bgColor, colorization, fgColor, colorization);
                }
            }
        }
        return scheme;
    }

    public static SubstanceColorScheme getColorScheme(JTabbedPane jtp, int tabIndex, ColorSchemeAssociationKind associationKind, ComponentState componentState) {
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(jtp);
        if (skin == null) {
            SubstanceCoreUtilities.traceSubstanceApiUsage(jtp, "Substance delegate used when Substance is not the current LAF");
        }
        SubstanceColorScheme nonColorized = skin.getColorScheme(jtp, associationKind, componentState);
        if (tabIndex >= 0) {
            Component component = jtp.getComponentAt(tabIndex);
            SubstanceColorScheme colorized = SubstanceColorSchemeUtilities.getColorizedScheme(component, nonColorized, jtp.getForegroundAt(tabIndex), jtp.getBackgroundAt(tabIndex), !componentState.isDisabled());
            return colorized;
        }
        return SubstanceColorSchemeUtilities.getColorizedScheme(jtp, nonColorized, !componentState.isDisabled());
    }

    public static SubstanceColorScheme getColorScheme(Component component, ComponentState componentState) {
        SubstanceSkin skin;
        boolean isButtonThatIsNeverPainted;
        Component orig = component;
        boolean bl = isButtonThatIsNeverPainted = component instanceof AbstractButton && SubstanceCoreUtilities.isButtonNeverPainted((AbstractButton)component);
        if (isButtonThatIsNeverPainted || SubstanceCoreUtilities.hasFlatAppearance(component, false) && componentState == ComponentState.ENABLED) {
            component = component.getParent();
        }
        if ((skin = SubstanceCoreUtilities.getSkin(component)) == null) {
            SubstanceCoreUtilities.traceSubstanceApiUsage(component, "Substance delegate used when Substance is not the current LAF");
        }
        SubstanceColorScheme nonColorized = skin.getColorScheme(component, componentState);
        return SubstanceColorSchemeUtilities.getColorizedScheme(orig, nonColorized, !componentState.isDisabled());
    }

    public static SubstanceColorScheme getColorScheme(Component component, ColorSchemeAssociationKind associationKind, ComponentState componentState) {
        if (!(component instanceof JToolBar) && SubstanceCoreUtilities.hasFlatAppearance(component, false) && componentState == ComponentState.ENABLED) {
            component = component.getParent();
        }
        SubstanceColorScheme nonColorized = SubstanceCoreUtilities.getSkin(component).getColorScheme(component, associationKind, componentState);
        return SubstanceColorSchemeUtilities.getColorizedScheme(component, nonColorized, !componentState.isDisabled());
    }

    public static SubstanceColorScheme getActiveColorScheme(Component component, ComponentState componentState) {
        if (!(component instanceof JToolBar) && SubstanceCoreUtilities.hasFlatAppearance(component, false) && componentState == ComponentState.ENABLED) {
            component = component.getParent();
        }
        SubstanceColorScheme nonColorized = SubstanceCoreUtilities.getSkin(component).getActiveColorScheme(SubstanceLookAndFeel.getDecorationType(component));
        return SubstanceColorSchemeUtilities.getColorizedScheme(component, nonColorized, !componentState.isDisabled());
    }

    public static float getHighlightAlpha(Component component, ComponentState componentState) {
        return SubstanceCoreUtilities.getSkin(component).getHighlightAlpha(component, componentState);
    }

    public static float getAlpha(Component component, ComponentState componentState) {
        return SubstanceCoreUtilities.getSkin(component).getAlpha(component, componentState);
    }

    public static SchemeBaseColors getBaseColorScheme(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Color ultraLight = null;
        Color extraLight = null;
        Color light = null;
        Color mid = null;
        Color dark = null;
        Color ultraDark = null;
        Color foreground = null;
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("=");
                if (split.length != 2) {
                    throw new IllegalArgumentException("Unsupported format in line " + line);
                }
                String key = split[0];
                String value = split[1];
                if ("colorUltraLight".equals(key)) {
                    if (ultraLight == null) {
                        ultraLight = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'ultraLight' should only be defined once");
                }
                if ("colorExtraLight".equals(key)) {
                    if (extraLight == null) {
                        extraLight = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'extraLight' should only be defined once");
                }
                if ("colorLight".equals(key)) {
                    if (light == null) {
                        light = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'light' should only be defined once");
                }
                if ("colorMid".equals(key)) {
                    if (mid == null) {
                        mid = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'mid' should only be defined once");
                }
                if ("colorDark".equals(key)) {
                    if (dark == null) {
                        dark = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'dark' should only be defined once");
                }
                if ("colorUltraDark".equals(key)) {
                    if (ultraDark == null) {
                        ultraDark = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'ultraDark' should only be defined once");
                }
                if ("colorForeground".equals(key)) {
                    if (foreground == null) {
                        foreground = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'foreground' should only be defined once");
                }
                throw new IllegalArgumentException("Unsupported format in line " + line);
            }
            final Color[] colors = new Color[]{ultraLight, extraLight, light, mid, dark, ultraDark, foreground};
            SchemeBaseColors schemeBaseColors = new SchemeBaseColors(){

                @Override
                public String getDisplayName() {
                    return null;
                }

                @Override
                public Color getUltraLightColor() {
                    return colors[0];
                }

                @Override
                public Color getExtraLightColor() {
                    return colors[1];
                }

                @Override
                public Color getLightColor() {
                    return colors[2];
                }

                @Override
                public Color getMidColor() {
                    return colors[3];
                }

                @Override
                public Color getDarkColor() {
                    return colors[4];
                }

                @Override
                public Color getUltraDarkColor() {
                    return colors[5];
                }

                @Override
                public Color getForegroundColor() {
                    return colors[6];
                }
            };
            return schemeBaseColors;
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException(ioe);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {}
            }
        }
    }

    public static SubstanceColorScheme getLightColorScheme(String name, final Color[] colors) {
        if (colors == null) {
            throw new IllegalArgumentException("Color encoding cannot be null");
        }
        if (colors.length != 7) {
            throw new IllegalArgumentException("Color encoding must have 7 components");
        }
        return new BaseLightColorScheme(name){

            @Override
            public Color getUltraLightColor() {
                return colors[0];
            }

            @Override
            public Color getExtraLightColor() {
                return colors[1];
            }

            @Override
            public Color getLightColor() {
                return colors[2];
            }

            @Override
            public Color getMidColor() {
                return colors[3];
            }

            @Override
            public Color getDarkColor() {
                return colors[4];
            }

            @Override
            public Color getUltraDarkColor() {
                return colors[5];
            }

            @Override
            public Color getForegroundColor() {
                return colors[6];
            }
        };
    }

    public static SubstanceColorScheme getDarkColorScheme(String name, final Color[] colors) {
        if (colors == null) {
            throw new IllegalArgumentException("Color encoding cannot be null");
        }
        if (colors.length != 7) {
            throw new IllegalArgumentException("Color encoding must have 7 components");
        }
        return new BaseDarkColorScheme(name){

            @Override
            public Color getUltraLightColor() {
                return colors[0];
            }

            @Override
            public Color getExtraLightColor() {
                return colors[1];
            }

            @Override
            public Color getLightColor() {
                return colors[2];
            }

            @Override
            public Color getMidColor() {
                return colors[3];
            }

            @Override
            public Color getDarkColor() {
                return colors[4];
            }

            @Override
            public Color getUltraDarkColor() {
                return colors[5];
            }

            @Override
            public Color getForegroundColor() {
                return colors[6];
            }
        };
    }

    public static SubstanceSkin.ColorSchemes getColorSchemes(URL url) {
        ArrayList<SubstanceColorScheme> schemes;
        block36: {
            schemes = new ArrayList<SubstanceColorScheme>();
            BufferedReader reader = null;
            Color ultraLight = null;
            Color extraLight = null;
            Color light = null;
            Color mid = null;
            Color dark = null;
            Color ultraDark = null;
            Color foreground = null;
            String name = null;
            ColorSchemeKind kind = null;
            boolean inColorSchemeBlock = false;
            try {
                String line;
                block37: {
                    reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    while (true) {
                        if ((line = reader.readLine()) == null) {
                            break block36;
                        }
                        if ((line = line.trim()).length() == 0 || line.startsWith("#")) continue;
                        if (line.indexOf("{") >= 0) {
                            if (inColorSchemeBlock) {
                                throw new IllegalArgumentException("Already in color scheme definition");
                            }
                            inColorSchemeBlock = true;
                            name = line.substring(0, line.indexOf("{")).trim();
                            continue;
                        }
                        if (line.indexOf("}") >= 0) {
                            if (!inColorSchemeBlock) {
                                throw new IllegalArgumentException("Not in color scheme definition");
                            }
                            inColorSchemeBlock = false;
                            if (name == null || kind == null || ultraLight == null || extraLight == null || light == null || mid == null || dark == null || ultraDark == null || foreground == null) {
                                throw new IllegalArgumentException("Incomplete specification");
                            }
                            Color[] colors = new Color[]{ultraLight, extraLight, light, mid, dark, ultraDark, foreground};
                            if (kind == ColorSchemeKind.LIGHT) {
                                schemes.add(SubstanceColorSchemeUtilities.getLightColorScheme(name, colors));
                            } else {
                                schemes.add(SubstanceColorSchemeUtilities.getDarkColorScheme(name, colors));
                            }
                            name = null;
                            kind = null;
                            ultraLight = null;
                            extraLight = null;
                            light = null;
                            mid = null;
                            dark = null;
                            ultraDark = null;
                            foreground = null;
                            continue;
                        }
                        String[] split = line.split("=");
                        if (split.length != 2) {
                            throw new IllegalArgumentException("Unsupported format in line " + line);
                        }
                        String key = split[0].trim();
                        String value = split[1].trim();
                        if ("kind".equals(key)) {
                            if (kind == null) {
                                if ("Light".equals(value)) {
                                    kind = ColorSchemeKind.LIGHT;
                                    continue;
                                }
                                if ("Dark".equals(value)) {
                                    kind = ColorSchemeKind.DARK;
                                    continue;
                                }
                                throw new IllegalArgumentException("Unsupported format in line " + line);
                            }
                            throw new IllegalArgumentException("'kind' should only be defined once");
                        }
                        if ("colorUltraLight".equals(key)) {
                            if (ultraLight == null) {
                                ultraLight = Color.decode(value);
                                continue;
                            }
                            throw new IllegalArgumentException("'ultraLight' should only be defined once");
                        }
                        if ("colorExtraLight".equals(key)) {
                            if (extraLight == null) {
                                extraLight = Color.decode(value);
                                continue;
                            }
                            throw new IllegalArgumentException("'extraLight' should only be defined once");
                        }
                        if ("colorLight".equals(key)) {
                            if (light == null) {
                                light = Color.decode(value);
                                continue;
                            }
                            throw new IllegalArgumentException("'light' should only be defined once");
                        }
                        if ("colorMid".equals(key)) {
                            if (mid == null) {
                                mid = Color.decode(value);
                                continue;
                            }
                            throw new IllegalArgumentException("'mid' should only be defined once");
                        }
                        if ("colorDark".equals(key)) {
                            if (dark == null) {
                                dark = Color.decode(value);
                                continue;
                            }
                            throw new IllegalArgumentException("'dark' should only be defined once");
                        }
                        if ("colorUltraDark".equals(key)) {
                            if (ultraDark == null) {
                                ultraDark = Color.decode(value);
                                continue;
                            }
                            throw new IllegalArgumentException("'ultraDark' should only be defined once");
                        }
                        if (!"colorForeground".equals(key)) break block37;
                        if (foreground != null) break;
                        foreground = Color.decode(value);
                    }
                    throw new IllegalArgumentException("'foreground' should only be defined once");
                }
                throw new IllegalArgumentException("Unsupported format in line " + line);
            }
            catch (IOException ioe) {
                throw new IllegalArgumentException(ioe);
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (IOException ioe) {}
                }
            }
        }
        return new SubstanceSkin.ColorSchemes(schemes);
    }

    private static enum ColorSchemeKind {
        LIGHT,
        DARK;

    }
}

