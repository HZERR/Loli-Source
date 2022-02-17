/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import org.pushingpixels.substance.api.ComponentStateFacet;

public final class ComponentState {
    private static Set<ComponentState> allStates = new HashSet<ComponentState>();
    public static final ComponentState DISABLED_DEFAULT = new ComponentState("disabled default", new ComponentStateFacet[]{ComponentStateFacet.DEFAULT}, new ComponentStateFacet[]{ComponentStateFacet.ENABLE});
    public static final ComponentState DEFAULT = new ComponentState("default", new ComponentStateFacet[]{ComponentStateFacet.DEFAULT, ComponentStateFacet.ENABLE}, null);
    public static final ComponentState DISABLED_SELECTED = new ComponentState("disabled selected", new ComponentStateFacet[]{ComponentStateFacet.SELECTION}, new ComponentStateFacet[]{ComponentStateFacet.ENABLE});
    public static final ComponentState DISABLED_UNSELECTED = new ComponentState("disabled unselected", null, new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.SELECTION});
    public static final ComponentState PRESSED_SELECTED = new ComponentState("pressed selected", new ComponentStateFacet[]{ComponentStateFacet.SELECTION, ComponentStateFacet.PRESS, ComponentStateFacet.ENABLE}, null);
    public static final ComponentState PRESSED_UNSELECTED = new ComponentState("pressed unselected", new ComponentStateFacet[]{ComponentStateFacet.PRESS, ComponentStateFacet.ENABLE}, new ComponentStateFacet[]{ComponentStateFacet.SELECTION});
    public static final ComponentState SELECTED = new ComponentState("selected", new ComponentStateFacet[]{ComponentStateFacet.SELECTION, ComponentStateFacet.ENABLE}, null);
    public static final ComponentState ROLLOVER_SELECTED = new ComponentState("rollover selected", new ComponentStateFacet[]{ComponentStateFacet.SELECTION, ComponentStateFacet.ROLLOVER, ComponentStateFacet.ENABLE}, null);
    public static final ComponentState ARMED = new ComponentState("armed", new ComponentStateFacet[]{ComponentStateFacet.ARM, ComponentStateFacet.ENABLE}, null);
    public static final ComponentState ROLLOVER_ARMED = new ComponentState("rollover armed", new ComponentStateFacet[]{ComponentStateFacet.ROLLOVER, ComponentStateFacet.ARM, ComponentStateFacet.ENABLE}, null);
    public static final ComponentState ROLLOVER_UNSELECTED = new ComponentState("rollover unselected", new ComponentStateFacet[]{ComponentStateFacet.ROLLOVER, ComponentStateFacet.ENABLE}, new ComponentStateFacet[]{ComponentStateFacet.SELECTION});
    public static final ComponentState ENABLED = new ComponentState("enabled", new ComponentStateFacet[]{ComponentStateFacet.ENABLE}, null);
    private Set<ComponentStateFacet> facetsTurnedOn;
    private Set<ComponentStateFacet> facetsTurnedOff;
    private Map<ComponentStateFacet, Boolean> mapping;
    private String name;
    private ComponentState hardFallback;

    public ComponentState(String name, ComponentStateFacet[] facetsOn, ComponentStateFacet[] facetsOff) {
        this(name, null, facetsOn, facetsOff);
    }

    public ComponentState(String name, ComponentState hardFallback, ComponentStateFacet[] facetsOn, ComponentStateFacet[] facetsOff) {
        if (name == null) {
            throw new IllegalArgumentException("Component state name must be non-null");
        }
        this.name = name;
        this.hardFallback = hardFallback;
        this.facetsTurnedOn = new HashSet<ComponentStateFacet>();
        if (facetsOn != null) {
            for (ComponentStateFacet facetOn : facetsOn) {
                this.facetsTurnedOn.add(facetOn);
            }
        }
        this.facetsTurnedOff = new HashSet<ComponentStateFacet>();
        if (facetsOff != null) {
            for (ComponentStateFacet facetOff : facetsOff) {
                this.facetsTurnedOff.add(facetOff);
            }
        }
        this.mapping = new HashMap<ComponentStateFacet, Boolean>();
        allStates.add(this);
    }

    public String toString() {
        String sep;
        StringBuffer sb = new StringBuffer();
        sb.append(this.name);
        sb.append(" : on {");
        if (this.facetsTurnedOn != null) {
            sep = "";
            for (ComponentStateFacet on : this.facetsTurnedOn) {
                sb.append(sep);
                sep = ", ";
                sb.append(on.toString());
            }
        }
        sb.append("} : off {");
        if (this.facetsTurnedOff != null) {
            sep = "";
            for (ComponentStateFacet off : this.facetsTurnedOff) {
                sb.append(sep);
                sep = ", ";
                sb.append(off.toString());
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean isFacetActive(ComponentStateFacet stateFacet) {
        Boolean result = this.mapping.get(stateFacet);
        if (result != null) {
            return result;
        }
        if (this.facetsTurnedOn != null && this.facetsTurnedOn.contains(stateFacet)) {
            this.mapping.put(stateFacet, Boolean.TRUE);
            return true;
        }
        this.mapping.put(stateFacet, Boolean.FALSE);
        return false;
    }

    public boolean isDisabled() {
        return !this.isFacetActive(ComponentStateFacet.ENABLE);
    }

    public static ComponentState[] getActiveStates() {
        LinkedList<ComponentState> states = new LinkedList<ComponentState>();
        for (ComponentState state : allStates) {
            if (!state.isActive()) continue;
            states.add(state);
        }
        return states.toArray(new ComponentState[0]);
    }

    public static Set<ComponentState> getAllStates() {
        return Collections.synchronizedSet(allStates);
    }

    public boolean isActive() {
        if (this == ENABLED) {
            return false;
        }
        return this.isFacetActive(ComponentStateFacet.ENABLE);
    }

    public static ComponentState getState(ButtonModel model, JComponent component) {
        return ComponentState.getState(model, component, false);
    }

    public static ComponentState getState(AbstractButton button) {
        return ComponentState.getState(button.getModel(), button, false);
    }

    public static ComponentState getState(ButtonModel model, JComponent component, boolean toIgnoreSelection) {
        JButton jb;
        boolean isRollover = model.isRollover();
        if (component instanceof MenuElement) {
            MenuElement[] selectedMenuPath;
            for (MenuElement elem : selectedMenuPath = MenuSelectionManager.defaultManager().getSelectedPath()) {
                if (elem != component) continue;
                isRollover = true;
                break;
            }
        }
        if (component != null && component instanceof JButton && (jb = (JButton)component).isDefaultButton()) {
            if (model.isEnabled()) {
                if (jb.isRolloverEnabled() && jb.getModel().isRollover()) {
                    if (model.isSelected()) {
                        return ROLLOVER_SELECTED;
                    }
                    return ROLLOVER_UNSELECTED;
                }
                if (!model.isPressed() && !model.isArmed()) {
                    return DEFAULT;
                }
            } else {
                return DISABLED_DEFAULT;
            }
        }
        boolean isRolloverEnabled = true;
        if (component instanceof AbstractButton) {
            isRolloverEnabled = ((AbstractButton)component).isRolloverEnabled();
        }
        if (!model.isEnabled()) {
            if (model.isSelected()) {
                return DISABLED_SELECTED;
            }
            return DISABLED_UNSELECTED;
        }
        if (model.isArmed() && model.isPressed()) {
            if (model.isSelected()) {
                return PRESSED_SELECTED;
            }
            return PRESSED_UNSELECTED;
        }
        if (!toIgnoreSelection && model.isSelected()) {
            if ((component == null || isRolloverEnabled) && isRollover) {
                return ROLLOVER_SELECTED;
            }
            return SELECTED;
        }
        if (model.isArmed()) {
            if ((component == null || isRolloverEnabled) && isRollover) {
                return ROLLOVER_ARMED;
            }
            return ARMED;
        }
        if ((component == null || isRolloverEnabled) && isRollover) {
            return ROLLOVER_UNSELECTED;
        }
        return ENABLED;
    }

    public static ComponentState getState(boolean isEnabled, boolean isRollover, boolean isSelected) {
        if (!isEnabled) {
            if (isSelected) {
                return DISABLED_SELECTED;
            }
            return DISABLED_UNSELECTED;
        }
        if (isSelected) {
            if (isRollover) {
                return ROLLOVER_SELECTED;
            }
            return SELECTED;
        }
        if (isRollover) {
            return ROLLOVER_UNSELECTED;
        }
        return ENABLED;
    }

    private int fitValue(ComponentState state) {
        int value = 0;
        if (this.facetsTurnedOn != null) {
            for (ComponentStateFacet on : this.facetsTurnedOn) {
                if (state.facetsTurnedOn == null) {
                    value -= on.value / 2;
                    continue;
                }
                value = state.facetsTurnedOn.contains(on) ? (value += on.value) : (value -= on.value / 2);
                if (!state.facetsTurnedOff.contains(on)) continue;
                value -= on.value;
            }
        }
        if (this.facetsTurnedOff != null) {
            for (ComponentStateFacet off : this.facetsTurnedOff) {
                if (state.facetsTurnedOff == null) {
                    value -= off.value / 2;
                    continue;
                }
                value = state.facetsTurnedOff.contains(off) ? (value += off.value) : (value -= off.value / 2);
                if (!state.facetsTurnedOn.contains(off)) continue;
                value -= off.value;
            }
        }
        return value;
    }

    public ComponentState bestFit(Collection<ComponentState> states) {
        ComponentState bestFit = null;
        int bestFitValue = 0;
        for (ComponentState state : states) {
            if (this.isActive() != state.isActive()) continue;
            int currFitValue = state.fitValue(this) + this.fitValue(state);
            if (bestFit == null) {
                bestFit = state;
                bestFitValue = currFitValue;
                continue;
            }
            if (currFitValue <= bestFitValue) continue;
            bestFit = state;
            bestFitValue = currFitValue;
        }
        if (bestFitValue > 0) {
            return bestFit;
        }
        return null;
    }

    public ComponentState getHardFallback() {
        return this.hardFallback;
    }

    public int hashCode() {
        if (this.facetsTurnedOn.isEmpty() && this.facetsTurnedOff.isEmpty()) {
            return 0;
        }
        if (this.facetsTurnedOn.isEmpty()) {
            boolean isFirst = true;
            int result = 0;
            for (ComponentStateFacet off : this.facetsTurnedOff) {
                if (isFirst) {
                    result = ~off.hashCode();
                    isFirst = false;
                    continue;
                }
                result &= ~off.hashCode();
            }
            return result;
        }
        boolean isFirst = true;
        int result = 0;
        for (ComponentStateFacet on : this.facetsTurnedOn) {
            if (isFirst) {
                result = on.hashCode();
                isFirst = false;
                continue;
            }
            result &= on.hashCode();
        }
        for (ComponentStateFacet off : this.facetsTurnedOff) {
            result &= ~off.hashCode();
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ComponentState)) {
            return false;
        }
        ComponentState second = (ComponentState)obj;
        if (this.facetsTurnedOn.size() != second.facetsTurnedOn.size()) {
            return false;
        }
        if (this.facetsTurnedOff.size() != second.facetsTurnedOff.size()) {
            return false;
        }
        if (!this.facetsTurnedOn.containsAll(second.facetsTurnedOn)) {
            return false;
        }
        return this.facetsTurnedOff.containsAll(second.facetsTurnedOff);
    }
}

