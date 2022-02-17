/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.Accordion;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.TitledPane;
import javafx.util.Builder;

@Deprecated
public class AccordionBuilder<B extends AccordionBuilder<B>>
extends ControlBuilder<B>
implements Builder<Accordion> {
    private int __set;
    private TitledPane expandedPane;
    private Collection<? extends TitledPane> panes;

    protected AccordionBuilder() {
    }

    public static AccordionBuilder<?> create() {
        return new AccordionBuilder();
    }

    public void applyTo(Accordion accordion) {
        super.applyTo(accordion);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            accordion.setExpandedPane(this.expandedPane);
        }
        if ((n2 & 2) != 0) {
            accordion.getPanes().addAll(this.panes);
        }
    }

    public B expandedPane(TitledPane titledPane) {
        this.expandedPane = titledPane;
        this.__set |= 1;
        return (B)this;
    }

    public B panes(Collection<? extends TitledPane> collection) {
        this.panes = collection;
        this.__set |= 2;
        return (B)this;
    }

    public B panes(TitledPane ... arrtitledPane) {
        return this.panes(Arrays.asList(arrtitledPane));
    }

    @Override
    public Accordion build() {
        Accordion accordion = new Accordion();
        this.applyTo(accordion);
        return accordion;
    }
}

