/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.input;

import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.util.Builder;

@Deprecated
public final class KeyCharacterCombinationBuilder
implements Builder<KeyCharacterCombination> {
    private KeyCombination.ModifierValue alt;
    private String character;
    private KeyCombination.ModifierValue control;
    private KeyCombination.ModifierValue meta;
    private KeyCombination.ModifierValue shift;
    private KeyCombination.ModifierValue shortcut;

    protected KeyCharacterCombinationBuilder() {
    }

    public static KeyCharacterCombinationBuilder create() {
        return new KeyCharacterCombinationBuilder();
    }

    public KeyCharacterCombinationBuilder alt(KeyCombination.ModifierValue modifierValue) {
        this.alt = modifierValue;
        return this;
    }

    public KeyCharacterCombinationBuilder character(String string) {
        this.character = string;
        return this;
    }

    public KeyCharacterCombinationBuilder control(KeyCombination.ModifierValue modifierValue) {
        this.control = modifierValue;
        return this;
    }

    public KeyCharacterCombinationBuilder meta(KeyCombination.ModifierValue modifierValue) {
        this.meta = modifierValue;
        return this;
    }

    public KeyCharacterCombinationBuilder shift(KeyCombination.ModifierValue modifierValue) {
        this.shift = modifierValue;
        return this;
    }

    public KeyCharacterCombinationBuilder shortcut(KeyCombination.ModifierValue modifierValue) {
        this.shortcut = modifierValue;
        return this;
    }

    @Override
    public KeyCharacterCombination build() {
        KeyCharacterCombination keyCharacterCombination = new KeyCharacterCombination(this.character, this.shift, this.control, this.alt, this.meta, this.shortcut);
        return keyCharacterCombination;
    }
}

