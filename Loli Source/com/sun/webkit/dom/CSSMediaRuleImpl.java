/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSRuleListImpl;
import com.sun.webkit.dom.MediaListImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.stylesheets.MediaList;

public class CSSMediaRuleImpl
extends CSSRuleImpl
implements CSSMediaRule {
    CSSMediaRuleImpl(long l2) {
        super(l2);
    }

    static CSSMediaRule getImpl(long l2) {
        return (CSSMediaRule)CSSMediaRuleImpl.create(l2);
    }

    @Override
    public MediaList getMedia() {
        return MediaListImpl.getImpl(CSSMediaRuleImpl.getMediaImpl(this.getPeer()));
    }

    static native long getMediaImpl(long var0);

    @Override
    public CSSRuleList getCssRules() {
        return CSSRuleListImpl.getImpl(CSSMediaRuleImpl.getCssRulesImpl(this.getPeer()));
    }

    static native long getCssRulesImpl(long var0);

    @Override
    public int insertRule(String string, int n2) throws DOMException {
        return CSSMediaRuleImpl.insertRuleImpl(this.getPeer(), string, n2);
    }

    static native int insertRuleImpl(long var0, String var2, int var3);

    @Override
    public void deleteRule(int n2) throws DOMException {
        CSSMediaRuleImpl.deleteRuleImpl(this.getPeer(), n2);
    }

    static native void deleteRuleImpl(long var0, int var2);
}

