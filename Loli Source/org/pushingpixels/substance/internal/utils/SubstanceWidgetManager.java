/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.util.EnumSet;
import java.util.Set;
import java.util.WeakHashMap;
import javax.swing.JRootPane;
import org.pushingpixels.substance.api.SubstanceConstants;

public class SubstanceWidgetManager {
    private static SubstanceWidgetManager instance;
    private Set<SubstanceConstants.SubstanceWidgetType> globalAllowed = EnumSet.noneOf(SubstanceConstants.SubstanceWidgetType.class);
    private Set<SubstanceConstants.SubstanceWidgetType> globalDisallowed = EnumSet.noneOf(SubstanceConstants.SubstanceWidgetType.class);
    private WeakHashMap<JRootPane, Set<SubstanceConstants.SubstanceWidgetType>> specificAllowed = new WeakHashMap();
    private WeakHashMap<JRootPane, Set<SubstanceConstants.SubstanceWidgetType>> specificDisallowed = new WeakHashMap();

    public static synchronized SubstanceWidgetManager getInstance() {
        if (instance == null) {
            instance = new SubstanceWidgetManager();
        }
        return instance;
    }

    private SubstanceWidgetManager() {
    }

    public void register(JRootPane rootPane, boolean isAllowed, SubstanceConstants.SubstanceWidgetType ... substanceWidgets) {
        if (rootPane == null) {
            for (SubstanceConstants.SubstanceWidgetType widget : substanceWidgets) {
                if (isAllowed) {
                    this.globalAllowed.add(widget);
                    this.globalDisallowed.remove((Object)widget);
                    continue;
                }
                this.globalDisallowed.add(widget);
                this.globalAllowed.remove((Object)widget);
            }
        } else {
            Set<SubstanceConstants.SubstanceWidgetType> toAddTo = null;
            Set<SubstanceConstants.SubstanceWidgetType> toRemoveFrom = null;
            if (isAllowed) {
                toAddTo = this.specificAllowed.get(rootPane);
                if (toAddTo == null) {
                    toAddTo = EnumSet.noneOf(SubstanceConstants.SubstanceWidgetType.class);
                    this.specificAllowed.put(rootPane, toAddTo);
                }
                toRemoveFrom = this.specificDisallowed.get(rootPane);
            } else {
                toAddTo = this.specificDisallowed.get(rootPane);
                if (toAddTo == null) {
                    toAddTo = EnumSet.noneOf(SubstanceConstants.SubstanceWidgetType.class);
                    this.specificDisallowed.put(rootPane, toAddTo);
                }
                toRemoveFrom = this.specificAllowed.get(rootPane);
            }
            for (SubstanceConstants.SubstanceWidgetType widget : substanceWidgets) {
                toAddTo.add(widget);
                if (toRemoveFrom == null) continue;
                toRemoveFrom.remove((Object)widget);
            }
        }
    }

    public boolean isAllowed(JRootPane rootPane, SubstanceConstants.SubstanceWidgetType widget) {
        if (this.specificDisallowed.containsKey(rootPane) && this.specificDisallowed.get(rootPane).contains((Object)widget)) {
            return false;
        }
        if (this.specificAllowed.containsKey(rootPane) && this.specificAllowed.get(rootPane).contains((Object)widget)) {
            return true;
        }
        if (this.globalDisallowed.contains((Object)widget)) {
            return false;
        }
        return this.globalAllowed.contains((Object)widget);
    }

    public boolean isAllowedAnywhere(SubstanceConstants.SubstanceWidgetType widget) {
        if (this.specificAllowed.size() > 0) {
            return true;
        }
        if (this.globalDisallowed.contains((Object)widget)) {
            return false;
        }
        return this.globalAllowed.contains((Object)widget);
    }
}

