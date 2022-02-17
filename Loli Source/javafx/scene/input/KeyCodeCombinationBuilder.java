/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.util.Builder;

@Deprecated
public final class KeyCodeCombinationBuilder
implements Builder<KeyCodeCombination> {
    private KeyCombination.ModifierValue alt;
    private KeyCode code;
    private KeyCombination.ModifierValue control;
    private KeyCombination.ModifierValue meta;
    private KeyCombination.ModifierValue shift;
    private KeyCombination.ModifierValue shortcut;

    protected KeyCodeCombinationBuilder() {
    }

    public static KeyCodeCombinationBuilder create() {
        return new KeyCodeCombinationBuilder();
    }

    public KeyCodeCombinationBuilder alt(KeyCombination.ModifierValue modifierValue) {
        this.alt = modifierValue;
        return this;
    }

    public KeyCodeCombinationBuilder code(KeyCode keyCode) {
        this.code = keyCode;
        return this;
    }

    public KeyCodeCombinationBuilder control(KeyCombination.ModifierValue modifierValue) {
        this.control = modifierValue;
        return this;
    }

    public KeyCodeCombinationBuilder meta(KeyCombination.ModifierValue modifierValue) {
        this.meta = modifierValue;
        return this;
    }

    public KeyCodeCombinationBuilder shift(KeyCombination.ModifierValue modifierValue) {
        this.shift = modifierValue;
        return this;
    }

    public KeyCodeCombinationBuilder shortcut(KeyCombination.ModifierValue modifierValue) {
        this.shortcut = modifierValue;
        return this;
    }

    @Override
    public KeyCodeCombination build() {
        KeyCodeCombination keyCodeCombination = new KeyCodeCombination(this.code, this.shift, this.control, this.alt, this.meta, this.shortcut);
        return keyCodeCombination;
    }
}

