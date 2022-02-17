/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;

public interface ColorSchemeSingleColorQuery {
    public static final ColorSchemeSingleColorQuery ULTRALIGHT = new ColorSchemeSingleColorQuery(){

        @Override
        public Color query(SubstanceColorScheme scheme) {
            return scheme.getUltraLightColor();
        }
    };
    public static final ColorSchemeSingleColorQuery EXTRALIGHT = new ColorSchemeSingleColorQuery(){

        @Override
        public Color query(SubstanceColorScheme scheme) {
            return scheme.getExtraLightColor();
        }
    };
    public static final ColorSchemeSingleColorQuery LIGHT = new ColorSchemeSingleColorQuery(){

        @Override
        public Color query(SubstanceColorScheme scheme) {
            return scheme.getLightColor();
        }
    };
    public static final ColorSchemeSingleColorQuery MID = new ColorSchemeSingleColorQuery(){

        @Override
        public Color query(SubstanceColorScheme scheme) {
            return scheme.getMidColor();
        }
    };
    public static final ColorSchemeSingleColorQuery DARK = new ColorSchemeSingleColorQuery(){

        @Override
        public Color query(SubstanceColorScheme scheme) {
            return scheme.getDarkColor();
        }
    };
    public static final ColorSchemeSingleColorQuery ULTRADARK = new ColorSchemeSingleColorQuery(){

        @Override
        public Color query(SubstanceColorScheme scheme) {
            return scheme.getUltraDarkColor();
        }
    };
    public static final ColorSchemeSingleColorQuery FOREGROUND = new ColorSchemeSingleColorQuery(){

        @Override
        public Color query(SubstanceColorScheme scheme) {
            return scheme.getForegroundColor();
        }
    };

    public Color query(SubstanceColorScheme var1);
}

