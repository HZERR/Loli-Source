/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.ParsedValueImpl;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.Style;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;

public class CascadingStyle
implements Comparable<CascadingStyle> {
    private final Style style;
    private final Set<PseudoClass> pseudoClasses;
    private final int specificity;
    private final int ordinal;
    private final boolean skinProp;

    public Style getStyle() {
        return this.style;
    }

    public CascadingStyle(Style style, Set<PseudoClass> set, int n2, int n3) {
        this.style = style;
        this.pseudoClasses = set;
        this.specificity = n2;
        this.ordinal = n3;
        this.skinProp = "-fx-skin".equals(style.getDeclaration().getProperty());
    }

    public String getProperty() {
        return this.style.getDeclaration().getProperty();
    }

    public Selector getSelector() {
        return this.style.getSelector();
    }

    public Rule getRule() {
        return this.style.getDeclaration().getRule();
    }

    public StyleOrigin getOrigin() {
        return this.getRule().getOrigin();
    }

    public ParsedValueImpl getParsedValueImpl() {
        return this.style.getDeclaration().getParsedValueImpl();
    }

    public String toString() {
        return this.getProperty();
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        CascadingStyle cascadingStyle = (CascadingStyle)object;
        String string = this.getProperty();
        String string2 = cascadingStyle.getProperty();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        return !(this.pseudoClasses == null ? cascadingStyle.pseudoClasses != null : !this.pseudoClasses.containsAll(cascadingStyle.pseudoClasses));
    }

    public int hashCode() {
        int n2 = 7;
        String string = this.getProperty();
        n2 = 47 * n2 + (string != null ? string.hashCode() : 0);
        n2 = 47 * n2 + (this.pseudoClasses != null ? this.pseudoClasses.hashCode() : 0);
        return n2;
    }

    @Override
    public int compareTo(CascadingStyle cascadingStyle) {
        Declaration declaration = this.style.getDeclaration();
        boolean bl = declaration != null ? declaration.isImportant() : false;
        Rule rule = declaration != null ? declaration.getRule() : null;
        StyleOrigin styleOrigin = rule != null ? rule.getOrigin() : null;
        Declaration declaration2 = cascadingStyle.style.getDeclaration();
        boolean bl2 = declaration2 != null ? declaration2.isImportant() : false;
        Rule rule2 = declaration2 != null ? declaration2.getRule() : null;
        StyleOrigin styleOrigin2 = rule2 != null ? rule2.getOrigin() : null;
        int n2 = 0;
        n2 = this.skinProp && !cascadingStyle.skinProp ? 1 : (bl != bl2 ? (bl ? -1 : 1) : (styleOrigin != styleOrigin2 ? (styleOrigin == null ? -1 : (styleOrigin2 == null ? 1 : styleOrigin2.compareTo(styleOrigin))) : cascadingStyle.specificity - this.specificity));
        if (n2 == 0) {
            n2 = cascadingStyle.ordinal - this.ordinal;
        }
        return n2;
    }
}

