/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSStyleDeclarationImpl;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSStyleDeclaration;

public class CSSPageRuleImpl
extends CSSRuleImpl
implements CSSPageRule {
    CSSPageRuleImpl(long l2) {
        super(l2);
    }

    static CSSPageRule getImpl(long l2) {
        return (CSSPageRule)CSSPageRuleImpl.create(l2);
    }

    @Override
    public String getSelectorText() {
        return CSSPageRuleImpl.getSelectorTextImpl(this.getPeer());
    }

    static native String getSelectorTextImpl(long var0);

    @Override
    public void setSelectorText(String string) {
        CSSPageRuleImpl.setSelectorTextImpl(this.getPeer(), string);
    }

    static native void setSelectorTextImpl(long var0, String var2);

    @Override
    public CSSStyleDeclaration getStyle() {
        return CSSStyleDeclarationImpl.getImpl(CSSPageRuleImpl.getStyleImpl(this.getPeer()));
    }

    static native long getStyleImpl(long var0);
}

