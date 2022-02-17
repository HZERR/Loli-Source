/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import org.w3c.dom.css.CSSUnknownRule;

public class CSSUnknownRuleImpl
extends CSSRuleImpl
implements CSSUnknownRule {
    CSSUnknownRuleImpl(long l2) {
        super(l2);
    }

    static CSSUnknownRule getImpl(long l2) {
        return (CSSUnknownRule)CSSUnknownRuleImpl.create(l2);
    }
}

