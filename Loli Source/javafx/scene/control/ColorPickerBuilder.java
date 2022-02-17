/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBoxBaseBuilder;
import javafx.scene.paint.Color;
import javafx.util.Builder;

@Deprecated
public class ColorPickerBuilder<B extends ColorPickerBuilder<B>>
extends ComboBoxBaseBuilder<Color, B>
implements Builder<ColorPicker> {
    private boolean __set;
    private Collection<? extends Color> customColors;

    protected ColorPickerBuilder() {
    }

    public static ColorPickerBuilder<?> create() {
        return new ColorPickerBuilder();
    }

    public void applyTo(ColorPicker colorPicker) {
        super.applyTo(colorPicker);
        if (this.__set) {
            colorPicker.getCustomColors().addAll(this.customColors);
        }
    }

    public B customColors(Collection<? extends Color> collection) {
        this.customColors = collection;
        this.__set = true;
        return (B)this;
    }

    public B customColors(Color ... arrcolor) {
        return this.customColors(Arrays.asList(arrcolor));
    }

    @Override
    public ColorPicker build() {
        ColorPicker colorPicker = new ColorPicker();
        this.applyTo(colorPicker);
        return colorPicker;
    }
}

