/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSRuleListImpl;
import com.sun.webkit.dom.StyleSheetImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

public class CSSStyleSheetImpl
extends StyleSheetImpl
implements CSSStyleSheet {
    CSSStyleSheetImpl(long l2) {
        super(l2);
    }

    static CSSStyleSheet getImpl(long l2) {
        return (CSSStyleSheet)CSSStyleSheetImpl.create(l2);
    }

    @Override
    public CSSRule getOwnerRule() {
        return CSSRuleImpl.getImpl(CSSStyleSheetImpl.getOwnerRuleImpl(this.getPeer()));
    }

    static native long getOwnerRuleImpl(long var0);

    @Override
    public CSSRuleList getCssRules() {
        return CSSRuleListImpl.getImpl(CSSStyleSheetImpl.getCssRulesImpl(this.getPeer()));
    }

    static native long getCssRulesImpl(long var0);

    public CSSRuleList getRules() {
        return CSSRuleListImpl.getImpl(CSSStyleSheetImpl.getRulesImpl(this.getPeer()));
    }

    static native long getRulesImpl(long var0);

    @Override
    public int insertRule(String string, int n2) throws DOMException {
        return CSSStyleSheetImpl.insertRuleImpl(this.getPeer(), string, n2);
    }

    static native int insertRuleImpl(long var0, String var2, int var3);

    @Override
    public void deleteRule(int n2) throws DOMException {
        CSSStyleSheetImpl.deleteRuleImpl(this.getPeer(), n2);
    }

    static native void deleteRuleImpl(long var0, int var2);

    public int addRule(String string, String string2, int n2) throws DOMException {
        return CSSStyleSheetImpl.addRuleImpl(this.getPeer(), string, string2, n2);
    }

    static native int addRuleImpl(long var0, String var2, String var3, int var4);

    public void removeRule(int n2) throws DOMException {
        CSSStyleSheetImpl.removeRuleImpl(this.getPeer(), n2);
    }

    static native void removeRuleImpl(long var0, int var2);
}

