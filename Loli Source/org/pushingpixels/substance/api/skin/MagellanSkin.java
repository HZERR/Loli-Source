/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import java.awt.Color;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.ColorSchemeTransform;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.border.CompositeBorderPainter;
import org.pushingpixels.substance.api.painter.border.DelegateBorderPainter;
import org.pushingpixels.substance.api.painter.border.FractionBasedBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomShadowOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopBezelOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopLineOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class MagellanSkin
extends SubstanceSkin {
    public static final String NAME = "Magellan";
    private BottomLineOverlayPainter toolbarBottomLineOverlayPainter;
    private TopLineOverlayPainter toolbarTopLineOverlayPainter;
    private TopBezelOverlayPainter footerTopBezelOverlayPainter;

    @Override
    public String getDisplayName() {
        return NAME;
    }

    public MagellanSkin() {
        SubstanceSkin.ColorSchemes colorSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/magellan.colorschemes");
        SubstanceColorScheme blueControlsActive = colorSchemes.get("Magellan Blue Controls Active");
        SubstanceColorScheme blueControlsEnabled = colorSchemes.get("Magellan Blue Controls Enabled");
        SubstanceColorSchemeBundle defaultColorSchemeBundle = new SubstanceColorSchemeBundle(blueControlsActive, blueControlsEnabled, blueControlsEnabled);
        defaultColorSchemeBundle.registerColorScheme(blueControlsEnabled, 0.5f, ComponentState.DISABLED_UNSELECTED);
        defaultColorSchemeBundle.registerColorScheme(blueControlsActive, 0.5f, ComponentState.DISABLED_SELECTED);
        SubstanceColorScheme blueControlsActiveBorder = colorSchemes.get("Magellan Blue Controls Active Border");
        SubstanceColorScheme blueControlsEnabledBorder = colorSchemes.get("Magellan Blue Controls Enabled Border");
        defaultColorSchemeBundle.registerColorScheme(blueControlsActiveBorder, ColorSchemeAssociationKind.BORDER, ComponentState.getActiveStates());
        defaultColorSchemeBundle.registerColorScheme(blueControlsActiveBorder, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED);
        defaultColorSchemeBundle.registerColorScheme(blueControlsEnabledBorder, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED, ComponentState.DISABLED_UNSELECTED);
        SubstanceColorScheme blueControlsPressed = colorSchemes.get("Magellan Blue Controls Pressed");
        SubstanceColorScheme blueControlsPressedBorder = colorSchemes.get("Magellan Blue Controls Pressed Border");
        defaultColorSchemeBundle.registerColorScheme(blueControlsPressed, ColorSchemeAssociationKind.FILL, ComponentState.PRESSED_SELECTED, ComponentState.PRESSED_UNSELECTED);
        defaultColorSchemeBundle.registerColorScheme(blueControlsPressedBorder, ColorSchemeAssociationKind.BORDER, ComponentState.PRESSED_SELECTED, ComponentState.PRESSED_UNSELECTED);
        SubstanceColorScheme greenControls = colorSchemes.get("Magellan Green Controls");
        SubstanceColorScheme greenControlsMark = colorSchemes.get("Magellan Green Controls Mark");
        SubstanceColorScheme greenControlsBorder = colorSchemes.get("Magellan Green Controls Border");
        defaultColorSchemeBundle.registerColorScheme(greenControls, ColorSchemeAssociationKind.FILL, ComponentState.ROLLOVER_SELECTED, ComponentState.ROLLOVER_UNSELECTED, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        defaultColorSchemeBundle.registerColorScheme(greenControlsMark, ColorSchemeAssociationKind.MARK, ComponentState.ROLLOVER_SELECTED, ComponentState.ROLLOVER_UNSELECTED, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        defaultColorSchemeBundle.registerColorScheme(greenControlsBorder, ColorSchemeAssociationKind.BORDER, ComponentState.ROLLOVER_SELECTED, ComponentState.ROLLOVER_UNSELECTED, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        ComponentState uneditable = new ComponentState("uneditable", new ComponentStateFacet[]{ComponentStateFacet.ENABLE}, new ComponentStateFacet[]{ComponentStateFacet.EDITABLE});
        SubstanceColorScheme uneditableControls = colorSchemes.get("Magellan Uneditable Controls");
        defaultColorSchemeBundle.registerColorScheme(uneditableControls, ColorSchemeAssociationKind.FILL, uneditable);
        defaultColorSchemeBundle.registerColorScheme(blueControlsActive, ColorSchemeAssociationKind.FILL, ComponentState.SELECTED);
        defaultColorSchemeBundle.registerHighlightColorScheme(greenControls, 0.7f, ComponentState.ROLLOVER_UNSELECTED);
        defaultColorSchemeBundle.registerHighlightColorScheme(greenControls, 0.8f, ComponentState.SELECTED);
        defaultColorSchemeBundle.registerHighlightColorScheme(greenControls, 0.95f, ComponentState.ROLLOVER_SELECTED);
        defaultColorSchemeBundle.registerHighlightColorScheme(greenControls, 1.0f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        SubstanceColorScheme lightBlueBackground = colorSchemes.get("Magellan Light Blue Background");
        this.registerDecorationAreaSchemeBundle(defaultColorSchemeBundle, lightBlueBackground, DecorationAreaType.NONE);
        SubstanceColorScheme mediumBlueBackground = colorSchemes.get("Magellan Medium Blue Background");
        SubstanceColorScheme darkBlueBackground = colorSchemes.get("Magellan Dark Blue Background");
        this.registerAsDecorationArea(mediumBlueBackground, DecorationAreaType.GENERAL, DecorationAreaType.TOOLBAR);
        this.registerAsDecorationArea(darkBlueBackground, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
        SubstanceColorScheme lightBlueControlsActive = colorSchemes.get("Magellan Light Blue Controls Active");
        SubstanceColorScheme lightBlueControlsEnabled = colorSchemes.get("Magellan Light Blue Controls Enabled");
        SubstanceColorScheme lightBlueBordersEnabled = colorSchemes.get("Magellan Light Blue Borders Enabled");
        SubstanceColorSchemeBundle footerColorSchemeBundle = new SubstanceColorSchemeBundle(lightBlueControlsActive, lightBlueControlsEnabled, lightBlueControlsEnabled);
        footerColorSchemeBundle.registerColorScheme(lightBlueControlsEnabled, 0.5f, ComponentState.DISABLED_UNSELECTED);
        footerColorSchemeBundle.registerColorScheme(lightBlueControlsActive, 0.5f, ComponentState.DISABLED_SELECTED);
        footerColorSchemeBundle.registerColorScheme(lightBlueBordersEnabled, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED);
        SubstanceColorScheme lightBlueSeparator = colorSchemes.get("Magellan Light Blue Separator");
        footerColorSchemeBundle.registerColorScheme(lightBlueSeparator, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        SubstanceColorScheme ultraLightBlueBackground = colorSchemes.get("Magellan Ultralight Blue Background");
        this.registerDecorationAreaSchemeBundle(footerColorSchemeBundle, ultraLightBlueBackground, DecorationAreaType.FOOTER);
        this.addOverlayPainter(BottomShadowOverlayPainter.getInstance(), DecorationAreaType.TOOLBAR);
        this.toolbarBottomLineOverlayPainter = new BottomLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                return scheme.getUltraDarkColor();
            }
        });
        this.addOverlayPainter(this.toolbarBottomLineOverlayPainter, DecorationAreaType.TOOLBAR);
        this.toolbarTopLineOverlayPainter = new TopLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                Color fg = scheme.getForegroundColor();
                return new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 40);
            }
        });
        this.addOverlayPainter(this.toolbarTopLineOverlayPainter, DecorationAreaType.TOOLBAR);
        this.footerTopBezelOverlayPainter = new TopBezelOverlayPainter(ColorSchemeSingleColorQuery.FOREGROUND, ColorSchemeSingleColorQuery.ULTRALIGHT);
        this.addOverlayPainter(this.footerTopBezelOverlayPainter, DecorationAreaType.FOOTER);
        this.setSelectedTabFadeStart(0.2);
        this.setSelectedTabFadeEnd(0.9);
        FractionBasedBorderPainter outerBorderPainter = new FractionBasedBorderPainter("Magellan Outer", new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.ULTRADARK, ColorSchemeSingleColorQuery.DARK, ColorSchemeSingleColorQuery.DARK});
        DelegateBorderPainter innerBorderPainter = new DelegateBorderPainter("Magellan Inner", new ClassicBorderPainter(), -1593835521, 0x60FFFFFF, 0x40FFFFFF, new ColorSchemeTransform(){

            @Override
            public SubstanceColorScheme transform(SubstanceColorScheme scheme) {
                return scheme.tint(0.5);
            }
        });
        this.borderPainter = new CompositeBorderPainter(NAME, outerBorderPainter, innerBorderPainter);
        this.fillPainter = new FractionBasedFillPainter(NAME, new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.EXTRALIGHT, ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.MID});
        this.highlightPainter = new ClassicHighlightPainter();
        this.decorationPainter = new MatteDecorationPainter();
        this.buttonShaper = new ClassicButtonShaper();
    }
}

