/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.EntityReference;

public class EntityReferenceImpl
extends NodeImpl
implements EntityReference {
    EntityReferenceImpl(long l2) {
        super(l2);
    }

    static EntityReference getImpl(long l2) {
        return (EntityReference)EntityReferenceImpl.create(l2);
    }
}

