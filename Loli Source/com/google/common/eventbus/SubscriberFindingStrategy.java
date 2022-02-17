/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.eventbus;

import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventSubscriber;

interface SubscriberFindingStrategy {
    public Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object var1);
}

