/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.SimpleSelector;
import javafx.geometry.NodeOrientation;

final class Match
implements Comparable<Match> {
    final Selector selector;
    final PseudoClassState pseudoClasses;
    final int idCount;
    final int styleClassCount;
    final int specificity;

    Match(Selector selector, PseudoClassState pseudoClassState, int n2, int n3) {
        SimpleSelector simpleSelector;
        int n4;
        assert (selector != null);
        this.selector = selector;
        this.idCount = n2;
        this.styleClassCount = n3;
        this.pseudoClasses = pseudoClassState;
        int n5 = n4 = pseudoClassState != null ? pseudoClassState.size() : 0;
        if (selector instanceof SimpleSelector && (simpleSelector = (SimpleSelector)selector).getNodeOrientation() != NodeOrientation.INHERIT) {
            ++n4;
        }
        this.specificity = n2 << 8 | n3 << 4 | n4;
    }

    @Override
    public int compareTo(Match match) {
        return this.specificity - match.specificity;
    }
}

