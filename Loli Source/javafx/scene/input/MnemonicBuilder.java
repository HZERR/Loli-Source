/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.input;

import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.util.Builder;

@Deprecated
public class MnemonicBuilder<B extends MnemonicBuilder<B>>
implements Builder<Mnemonic> {
    private KeyCombination keyCombination;
    private Node node;

    protected MnemonicBuilder() {
    }

    public static MnemonicBuilder<?> create() {
        return new MnemonicBuilder();
    }

    public B keyCombination(KeyCombination keyCombination) {
        this.keyCombination = keyCombination;
        return (B)this;
    }

    public B node(Node node) {
        this.node = node;
        return (B)this;
    }

    @Override
    public Mnemonic build() {
        Mnemonic mnemonic = new Mnemonic(this.node, this.keyCombination);
        return mnemonic;
    }
}

