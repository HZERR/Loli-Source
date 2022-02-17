/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JTabbedPane;
import org.pushingpixels.substance.api.tabbed.BaseTabCloseListener;

public class TabCloseListenerManager {
    private Set<BaseTabCloseListener> generalListeners = new HashSet<BaseTabCloseListener>();
    private Map<JTabbedPane, Set<BaseTabCloseListener>> specificListeners = new HashMap<JTabbedPane, Set<BaseTabCloseListener>>();
    private static TabCloseListenerManager instance = new TabCloseListenerManager();

    public static TabCloseListenerManager getInstance() {
        return instance;
    }

    public synchronized void unregisterTabbedPane(JTabbedPane tabbedPane) {
        this.specificListeners.remove(tabbedPane);
    }

    public synchronized void registerListener(BaseTabCloseListener listener) {
        this.generalListeners.add(listener);
    }

    public synchronized void unregisterListener(BaseTabCloseListener listener) {
        this.generalListeners.remove(listener);
    }

    public synchronized Set<BaseTabCloseListener> getListeners() {
        return Collections.unmodifiableSet(this.generalListeners);
    }

    public synchronized void registerListener(JTabbedPane tabbedPane, BaseTabCloseListener listener) {
        if (tabbedPane == null) {
            this.registerListener(listener);
        } else {
            Set<BaseTabCloseListener> listeners = this.specificListeners.get(tabbedPane);
            if (listeners == null) {
                listeners = new HashSet<BaseTabCloseListener>();
                this.specificListeners.put(tabbedPane, listeners);
            }
            listeners.add(listener);
        }
    }

    public synchronized void unregisterListener(JTabbedPane tabbedPane, BaseTabCloseListener listener) {
        if (tabbedPane == null) {
            this.unregisterListener(listener);
        } else {
            Set<BaseTabCloseListener> listeners = this.specificListeners.get(tabbedPane);
            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }

    public synchronized Set<BaseTabCloseListener> getListeners(JTabbedPane tabbedPane) {
        if (tabbedPane == null) {
            return this.getListeners();
        }
        HashSet<BaseTabCloseListener> result = new HashSet<BaseTabCloseListener>();
        for (BaseTabCloseListener listener : this.generalListeners) {
            result.add(listener);
        }
        Set<BaseTabCloseListener> listeners = this.specificListeners.get(tabbedPane);
        if (listeners != null) {
            for (BaseTabCloseListener listener : listeners) {
                result.add(listener);
            }
        }
        return Collections.unmodifiableSet(result);
    }
}

