/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.ManagedResource;
import com.sun.prism.impl.ResourcePool;

public abstract class DisposerManagedResource<T>
extends ManagedResource<T> {
    Object referent = new Object();

    public DisposerManagedResource(T t2, ResourcePool resourcePool, Disposer.Record record) {
        super(t2, resourcePool);
        Disposer.addRecord(this.referent, record);
    }
}

