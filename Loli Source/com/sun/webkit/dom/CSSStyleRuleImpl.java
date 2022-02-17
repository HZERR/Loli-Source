/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSStyleDeclarationImpl;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

public class CSSStyleRuleImpl
extends CSSRuleImpl
implements CSSStyleRule {
    CSSStyleRuleImpl(long l2) {
        super(l2);
    }

    static CSSStyleRule getImpl(long l2) {
        return (CSSStyleRule)CSSStyleRuleImpl.create(l2);
    }

    @Override
    public String getSelectorText() {
        return CSSStyleRuleImpl.getSelectorTextImpl(this.getPeer());
    }

    static native String getSelectorTextImpl(long var0);

    @Override
    public void setSelectorText(String string) {
        CSSStyleRuleImpl.setSelectorTextImpl(this.getPeer(), string);
    }

    static native void setSelectorTextImpl(long var0, String var2);

    @Override
    public CSSStyleDeclaration getStyle() {
        return CSSStyleDeclarationImpl.getImpl(CSSStyleRuleImpl.getStyleImpl(this.getPeer()));
    }

    static native long getStyleImpl(long var0);
}

