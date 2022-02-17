/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.inputmaps;

import org.pushingpixels.substance.api.inputmaps.SubstanceInputMap;
import org.pushingpixels.substance.internal.inputmaps.BaseInputMapSet;

public class GnomeInputMapSet
extends BaseInputMapSet {
    @Override
    public SubstanceInputMap getButtonFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("SPACE", "pressed");
        result.put("released SPACE", "released");
        result.put("ENTER", "pressed");
        result.put("released ENTER", "released");
        return result;
    }

    @Override
    public SubstanceInputMap getFileChooserAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl ENTER", "approveSelection");
        result.put("ESCAPE", "cancelSelection");
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
    public SubstanceInputMap getTreeFocusInputMap() {
        SubstanceInputMap result = super.getTreeFocusInputMap();
        result.remove("ADD");
        result.put("BACK_SPACE", "moveSelectionToParent");
        result.remove("SUBTRACT");
        result.put("typed +", "expand");
        result.put("typed -", "collapse");
        return result;
    }
}

