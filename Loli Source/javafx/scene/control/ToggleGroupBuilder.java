/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.util.Builder;

@Deprecated
public class ToggleGroupBuilder<B extends ToggleGroupBuilder<B>>
implements Builder<ToggleGroup> {
    private boolean __set;
    private Collection<? extends Toggle> toggles;

    protected ToggleGroupBuilder() {
    }

    public static ToggleGroupBuilder<?> create() {
        return new ToggleGroupBuilder();
    }

    public void applyTo(ToggleGroup toggleGroup) {
        if (this.__set) {
            toggleGroup.getToggles().addAll(this.toggles);
        }
    }

    public B toggles(Collection<? extends Toggle> collection) {
        this.toggles = collection;
        this.__set = true;
        return (B)this;
    }

    public B toggles(Toggle ... arrtoggle) {
        return this.toggles(Arrays.asList(arrtoggle));
    }

    @Override
    public ToggleGroup build() {
        ToggleGroup toggleGroup = new ToggleGroup();
        this.applyTo(toggleGroup);
        return toggleGroup;
    }
}

