/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.inputmaps;

import org.pushingpixels.substance.api.inputmaps.SubstanceInputMap;
import org.pushingpixels.substance.internal.inputmaps.BaseInputMapSet;

public class WindowsInputMapSet
extends BaseInputMapSet {
    @Override
    public SubstanceInputMap getComboBoxAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ESCAPE", "hidePopup");
        result.put("PAGE_UP", "pageUpPassThrough");
        result.put("PAGE_DOWN", "pageDownPassThrough");
        result.put("HOME", "homePassThrough");
        result.put("END", "endPassThrough");
        result.put("DOWN", "selectNext2");
        result.put("KP_DOWN", "selectNext2");
        result.put("alt DOWN", "togglePopup");
        result.put("alt KP_DOWN", "togglePopup");
        result.put("alt UP", "togglePopup");
        result.put("alt KP_UP", "togglePopup");
        result.put("ENTER", "enterPressed");
        result.put("UP", "selectPrevious2");
        result.put("KP_UP", "selectPrevious2");
        return result;
    }

    @Override
    public SubstanceInputMap getDesktopAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl F5", BaseInputMapSet.DesktopPaneActions.RESTORE);
        result.put("ctrl F4", BaseInputMapSet.DesktopPaneActions.CLOSE);
        result.put("ctrl F7", BaseInputMapSet.DesktopPaneActions.MOVE);
        result.put("ctrl F8", BaseInputMapSet.DesktopPaneActions.RESIZE);
        result.put("RIGHT", BaseInputMapSet.DesktopPaneActions.RIGHT);
        result.put("KP_RIGHT", BaseInputMapSet.DesktopPaneActions.RIGHT);
        result.put("LEFT", BaseInputMapSet.DesktopPaneActions.LEFT);
        result.put("KP_LEFT", BaseInputMapSet.DesktopPaneActions.LEFT);
        result.put("UP", BaseInputMapSet.DesktopPaneActions.UP);
        result.put("KP_UP", BaseInputMapSet.DesktopPaneActions.UP);
        result.put("DOWN", BaseInputMapSet.DesktopPaneActions.DOWN);
        result.put("KP_DOWN", BaseInputMapSet.DesktopPaneActions.DOWN);
        result.put("ESCAPE", BaseInputMapSet.DesktopPaneActions.ESCAPE);
        result.put("ctrl F9", BaseInputMapSet.DesktopPaneActions.MINIMIZE);
        result.put("ctrl F10", BaseInputMapSet.DesktopPaneActions.MAXIMIZE);
        result.put("ctrl F6", BaseInputMapSet.DesktopPaneActions.NEXT_FRAME);
        result.put("ctrl TAB", BaseInputMapSet.DesktopPaneActions.NEXT_FRAME);
        result.put("ctrl alt F6", BaseInputMapSet.DesktopPaneActions.NEXT_FRAME);
        result.put("shift ctrl alt F6", BaseInputMapSet.DesktopPaneActions.PREVIOUS_FRAME);
        result.put("ctrl F12", BaseInputMapSet.DesktopPaneActions.NAVIGATE_NEXT);
        result.put("shift ctrl F12", BaseInputMapSet.DesktopPaneActions.NAVIGATE_PREVIOUS);
        return result;
    }

    @Override
    protected SubstanceInputMap getMultilineTextComponentFocusInputMap() {
        SubstanceInputMap result = super.getMultilineTextComponentFocusInputMap();
        result.remove("ctrl KP_LEFT");
        result.remove("ctrl KP_RIGHT");
        result.remove("KP_DOWN");
        result.remove("KP_UP");
        result.remove("ctrl shift KP_LEFT");
        result.remove("ctrl shift KP_RIGHT");
        result.remove("shift KP_LEFT");
        result.remove("shift KP_RIGHT");
        result.remove("shift KP_DOWN");
        result.remove("shift KP_UP");
        return result;
    }

    @Override
    public SubstanceInputMap getPasswordFieldFocusInputMap() {
        SubstanceInputMap result = super.getPasswordFieldFocusInputMap();
        result.remove("ctrl KP_LEFT");
        result.remove("ctrl KP_RIGHT");
        result.remove("ctrl shift KP_LEFT");
        result.remove("ctrl shift KP_RIGHT");
        result.remove("shift KP_LEFT");
        result.remove("shift KP_RIGHT");
        return result;
    }

    @Override
    public SubstanceInputMap getScrollBarAncestorInputMap() {
        SubstanceInputMap result = super.getScrollBarAncestorInputMap();
        result.put("ctrl PAGE_DOWN", "positiveBlockIncrement");
        result.put("ctrl PAGE_UP", "negativeBlockIncrement");
        return result;
    }

    @Override
    public SubstanceInputMap getSliderFocusInputMap() {
        SubstanceInputMap result = super.getSliderFocusInputMap();
        result.remove("ctrl PAGE_DOWN");
        result.remove("ctrl PAGE_UP");
        return result;
    }

    @Override
    public SubstanceInputMap getTabbedPaneAncestorInputMap() {
        SubstanceInputMap result = super.getTabbedPaneAncestorInputMap();
        result.put("ctrl TAB", "navigateNext");
        result.put("shift ctrl TAB", "navigatePrevious");
        return result;
    }

    @Override
    public SubstanceInputMap getTextFieldFocusInputMap() {
        SubstanceInputMap result = super.getTextFieldFocusInputMap();
        result.remove("ctrl KP_LEFT");
        result.remove("ctrl KP_RIGHT");
        result.remove("ctrl shift KP_LEFT");
        result.remove("ctrl shift KP_RIGHT");
        result.remove("shift KP_LEFT");
        result.remove("shift KP_RIGHT");
        return result;
    }
}

