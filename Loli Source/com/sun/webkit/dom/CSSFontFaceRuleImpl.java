/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSStyleDeclarationImpl;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSStyleDeclaration;

public class CSSFontFaceRuleImpl
extends CSSRuleImpl
implements CSSFontFaceRule {
    CSSFontFaceRuleImpl(long l2) {
        super(l2);
    }

    static CSSFontFaceRule getImpl(long l2) {
        return (CSSFontFaceRule)CSSFontFaceRuleImpl.create(l2);
    }

    @Override
    public CSSStyleDeclaration getStyle() {
        return CSSStyleDeclarationImpl.getImpl(CSSFontFaceRuleImpl.getStyleImpl(this.getPeer()));
    }

    static native long getStyleImpl(long var0);
}

