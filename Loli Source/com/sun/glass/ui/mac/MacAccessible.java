/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.mac.MacApplication;
import com.sun.glass.ui.mac.MacVariant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;

final class MacAccessible
extends Accessible {
    private static final List<MacAttribute> baseAttributes;
    private static final List<MacAttribute> textAttributes;
    private static final List<MacAttribute> textParameterizedAttributes;
    private long peer = this._createGlassAccessible();
    private static final int kAXMenuItemModifierNone = 0;
    private static final int kAXMenuItemModifierShift = 1;
    private static final int kAXMenuItemModifierOption = 2;
    private static final int kAXMenuItemModifierControl = 4;
    private static final int kAXMenuItemModifierNoCommand = 8;
    private Boolean inMenu;
    private Boolean inSlider;
    private Boolean ignoreInnerText;

    private static native void _initIDs();

    private static native boolean _initEnum(String var0);

    private native long _createGlassAccessible();

    private native void _destroyGlassAccessible(long var1);

    private static native String getString(long var0);

    private static native boolean isEqualToString(long var0, long var2);

    private static native long NSAccessibilityUnignoredAncestor(long var0);

    private static native long[] NSAccessibilityUnignoredChildren(long[] var0);

    private static native void NSAccessibilityPostNotification(long var0, long var2);

    private static native String NSAccessibilityActionDescription(long var0);

    private static native String NSAccessibilityRoleDescription(long var0, long var2);

    private static native MacVariant idToMacVariant(long var0, int var2);

    private static native MacAccessible GlassAccessibleToMacAccessible(long var0);

    MacAccessible() {
        if (this.peer == 0L) {
            throw new RuntimeException("could not create platform accessible");
        }
    }

    @Override
    public void dispose() {
        if (this.peer != 0L) {
            if (this.getView() == null) {
                MacAccessible.NSAccessibilityPostNotification(this.peer, MacNotification.NSAccessibilityUIElementDestroyedNotification.ptr);
            }
            this._destroyGlassAccessible(this.peer);
            this.peer = 0L;
        }
        super.dispose();
    }

    @Override
    public void sendNotification(AccessibleAttribute accessibleAttribute) {
        Object object;
        if (this.isDisposed()) {
            return;
        }
        MacNotification macNotification = null;
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                object = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]));
                if (object == AccessibleRole.TABLE_VIEW || object == AccessibleRole.TREE_TABLE_VIEW) {
                    macNotification = MacNotification.NSAccessibilitySelectedCellsChangedNotification;
                    break;
                }
                if (object == AccessibleRole.LIST_VIEW || object == AccessibleRole.TREE_VIEW) {
                    macNotification = MacNotification.NSAccessibilitySelectedRowsChangedNotification;
                    break;
                }
                Object object2 = (Node)this.getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
                long l2 = this.getNativeAccessible((Node)object2);
                if (l2 == 0L) break;
                MacAccessible.NSAccessibilityPostNotification(l2, MacNotification.NSAccessibilityFocusedUIElementChangedNotification.ptr);
                break;
            }
            case FOCUS_NODE: {
                Accessible accessible;
                Scene scene;
                Node node = (Node)this.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
                View view = this.getView();
                if (node == null && view == null && (scene = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0])) != null && (accessible = this.getAccessible(scene)) != null) {
                    node = (Node)accessible.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
                }
                long l3 = 0L;
                if (node != null) {
                    Node node2 = (Node)this.getAccessible(node).getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
                    l3 = node2 != null ? this.getNativeAccessible(node2) : this.getNativeAccessible(node);
                } else {
                    if (view == null) {
                        view = this.getRootView((Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]));
                    }
                    if (view != null) {
                        l3 = view.getNativeView();
                    }
                }
                if (l3 != 0L) {
                    MacAccessible.NSAccessibilityPostNotification(l3, MacNotification.NSAccessibilityFocusedUIElementChangedNotification.ptr);
                }
                return;
            }
            case FOCUSED: {
                return;
            }
            case SELECTION_START: 
            case SELECTION_END: {
                macNotification = MacNotification.NSAccessibilitySelectedTextChangedNotification;
                break;
            }
            case EXPANDED: {
                AccessibleRole accessibleRole;
                MacAccessible macAccessible;
                boolean bl = Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.EXPANDED, new Object[0]));
                macNotification = bl ? MacNotification.NSAccessibilityRowExpandedNotification : MacNotification.NSAccessibilityRowCollapsedNotification;
                Object object2 = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]));
                if (object2 != AccessibleRole.TREE_ITEM && object2 != AccessibleRole.TREE_TABLE_ROW || (macAccessible = (MacAccessible)this.getContainerAccessible(accessibleRole = object2 == AccessibleRole.TREE_ITEM ? AccessibleRole.TREE_VIEW : AccessibleRole.TREE_TABLE_VIEW)) == null) break;
                MacAccessible.NSAccessibilityPostNotification(macAccessible.getNativeAccessible(), MacNotification.NSAccessibilityRowCountChangedNotification.ptr);
                break;
            }
            case VISIBLE: {
                MacAccessible macAccessible;
                if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) != AccessibleRole.CONTEXT_MENU) break;
                Boolean bl = (Boolean)this.getAttribute(AccessibleAttribute.VISIBLE, new Object[0]);
                if (Boolean.TRUE.equals(bl)) {
                    macNotification = MacNotification.AXMenuOpened;
                    break;
                }
                macNotification = MacNotification.AXMenuClosed;
                Node node = (Node)this.getAttribute(AccessibleAttribute.PARENT_MENU, new Object[0]);
                MacAccessible macAccessible2 = (MacAccessible)this.getAccessible(node);
                if (macAccessible2 == null || (macAccessible = (MacAccessible)macAccessible2.getContainerAccessible(AccessibleRole.CONTEXT_MENU)) == null) break;
                long l4 = macAccessible.getNativeAccessible();
                MacAccessible.NSAccessibilityPostNotification(l4, MacNotification.AXMenuClosed.ptr);
                MacAccessible.NSAccessibilityPostNotification(l4, MacNotification.AXMenuOpened.ptr);
                break;
            }
            case TEXT: {
                if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.SPINNER) {
                    macNotification = MacNotification.NSAccessibilityTitleChangedNotification;
                    break;
                }
                macNotification = MacNotification.NSAccessibilityValueChangedNotification;
                break;
            }
            case PARENT: {
                this.ignoreInnerText = null;
                break;
            }
            default: {
                macNotification = MacNotification.NSAccessibilityValueChangedNotification;
            }
        }
        if (macNotification != null) {
            object = this.getView();
            long l5 = object != null ? ((View)object).getNativeView() : this.peer;
            MacAccessible.NSAccessibilityPostNotification(l5, macNotification.ptr);
        }
    }

    @Override
    protected long getNativeAccessible() {
        return this.peer;
    }

    private View getRootView(Scene scene) {
        if (scene == null) {
            return null;
        }
        Accessible accessible = this.getAccessible(scene);
        if (accessible == null || accessible.isDisposed()) {
            return null;
        }
        View view = accessible.getView();
        if (view == null || view.isClosed()) {
            return null;
        }
        return view;
    }

    private long[] getUnignoredChildren(ObservableList<Node> observableList) {
        if (observableList == null) {
            return new long[0];
        }
        long[] arrl = observableList.stream().mapToLong(node -> this.getNativeAccessible((Node)node)).filter(l2 -> l2 != 0L).toArray();
        return MacAccessible.NSAccessibilityUnignoredChildren(arrl);
    }

    private boolean isInMenu() {
        if (this.inMenu == null) {
            this.inMenu = this.getContainerAccessible(AccessibleRole.CONTEXT_MENU) != null || this.getContainerAccessible(AccessibleRole.MENU_BAR) != null;
        }
        return this.inMenu;
    }

    private boolean isMenuElement(AccessibleRole accessibleRole) {
        if (accessibleRole == null) {
            return false;
        }
        switch (accessibleRole) {
            case MENU_BAR: 
            case CONTEXT_MENU: 
            case MENU_ITEM: 
            case RADIO_MENU_ITEM: 
            case CHECK_MENU_ITEM: 
            case MENU: {
                return true;
            }
        }
        return false;
    }

    private boolean isInSlider() {
        if (this.inSlider == null) {
            this.inSlider = this.getContainerAccessible(AccessibleRole.SLIDER) != null;
        }
        return this.inSlider;
    }

    private boolean ignoreInnerText() {
        if (this.ignoreInnerText != null) {
            return this.ignoreInnerText;
        }
        AccessibleRole accessibleRole = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]));
        this.ignoreInnerText = false;
        if (accessibleRole == AccessibleRole.TEXT) {
            Node node = (Node)this.getAttribute(AccessibleAttribute.PARENT, new Object[0]);
            if (node == null) {
                return this.ignoreInnerText;
            }
            AccessibleRole accessibleRole2 = (AccessibleRole)((Object)this.getAccessible(node).getAttribute(AccessibleAttribute.ROLE, new Object[0]));
            if (accessibleRole2 == null) {
                return this.ignoreInnerText;
            }
            switch (accessibleRole2) {
                case BUTTON: 
                case TOGGLE_BUTTON: 
                case CHECK_BOX: 
                case RADIO_BUTTON: 
                case COMBO_BOX: 
                case TEXT: 
                case HYPERLINK: 
                case TAB_ITEM: {
                    this.ignoreInnerText = true;
                }
            }
        }
        return this.ignoreInnerText;
    }

    private int getMenuItemCmdGlyph(KeyCode keyCode) {
        switch (keyCode) {
            case ENTER: {
                return 4;
            }
            case SHIFT: {
                return 5;
            }
            case CONTROL: {
                return 6;
            }
            case META: {
                return 7;
            }
            case SPACE: {
                return 9;
            }
            case COMMAND: {
                return 17;
            }
            case ESCAPE: {
                return 27;
            }
            case CLEAR: {
                return 28;
            }
            case PAGE_UP: {
                return 98;
            }
            case CAPS: {
                return 99;
            }
            case LEFT: 
            case KP_LEFT: {
                return 100;
            }
            case RIGHT: 
            case KP_RIGHT: {
                return 101;
            }
            case HELP: {
                return 103;
            }
            case UP: 
            case KP_UP: {
                return 104;
            }
            case DOWN: 
            case KP_DOWN: {
                return 106;
            }
            case PAGE_DOWN: {
                return 107;
            }
            case CONTEXT_MENU: {
                return 109;
            }
            case POWER: {
                return 110;
            }
            case F1: {
                return 111;
            }
            case F2: {
                return 112;
            }
            case F3: {
                return 113;
            }
            case F4: {
                return 114;
            }
            case F5: {
                return 115;
            }
            case F6: {
                return 116;
            }
            case F7: {
                return 117;
            }
            case F8: {
                return 118;
            }
            case F9: {
                return 119;
            }
            case F10: {
                return 120;
            }
            case F11: {
                return 121;
            }
            case F12: {
                return 122;
            }
            case F13: {
                return 135;
            }
            case F14: {
                return 136;
            }
            case F15: {
                return 137;
            }
        }
        return 0;
    }

    private boolean isCmdCharBased(KeyCode keyCode) {
        return keyCode.isLetterKey() || keyCode.isDigitKey() && !keyCode.isKeypadKey();
    }

    private MacRole getRole(AccessibleRole accessibleRole) {
        if (accessibleRole == AccessibleRole.COMBO_BOX) {
            if (Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.EDITABLE, new Object[0]))) {
                return MacRole.NSAccessibilityComboBoxRole;
            }
            return MacRole.NSAccessibilityPopUpButtonRole;
        }
        return MacRole.getRole(accessibleRole);
    }

    private Bounds flipBounds(Bounds bounds) {
        View view = this.getRootView((Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]));
        if (view == null || view.getWindow() == null) {
            return null;
        }
        Screen screen = view.getWindow().getScreen();
        float f2 = screen.getHeight();
        return new BoundingBox(bounds.getMinX(), (double)f2 - bounds.getMinY() - bounds.getHeight(), bounds.getWidth(), bounds.getHeight());
    }

    private long[] accessibilityAttributeNames() {
        if (this.getView() != null) {
            return null;
        }
        AccessibleRole accessibleRole = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]));
        if (accessibleRole != null) {
            MacSubrole macSubrole;
            ArrayList<MacAttribute> arrayList = new ArrayList<MacAttribute>(baseAttributes);
            MacRole macRole = this.getRole(accessibleRole);
            if (macRole != null && macRole.macAttributes != null) {
                arrayList.addAll(macRole.macAttributes);
            }
            if ((macSubrole = MacSubrole.getRole(accessibleRole)) != null && macSubrole.macAttributes != null) {
                arrayList.addAll(macSubrole.macAttributes);
            }
            switch (accessibleRole) {
                case LIST_VIEW: 
                case TREE_VIEW: {
                    arrayList.remove((Object)MacAttribute.NSAccessibilitySelectedCellsAttribute);
                    break;
                }
                case MENU_BAR: 
                case CONTEXT_MENU: 
                case MENU_ITEM: 
                case RADIO_MENU_ITEM: 
                case CHECK_MENU_ITEM: 
                case MENU: {
                    arrayList.remove((Object)MacAttribute.NSAccessibilityWindowAttribute);
                    arrayList.remove((Object)MacAttribute.NSAccessibilityTopLevelUIElementAttribute);
                    break;
                }
                case COMBO_BOX: 
                case TEXT: 
                case TEXT_FIELD: 
                case TEXT_AREA: 
                case PASSWORD_FIELD: {
                    arrayList.addAll(textAttributes);
                    break;
                }
            }
            return arrayList.stream().mapToLong(macAttribute -> macAttribute.ptr).toArray();
        }
        return null;
    }

    private int accessibilityArrayAttributeCount(long l2) {
        MacAttribute macAttribute = MacAttribute.getAttribute(l2);
        if (macAttribute == null) {
            return -1;
        }
        switch (macAttribute) {
            case NSAccessibilityRowsAttribute: {
                AccessibleAttribute accessibleAttribute = this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.LIST_VIEW ? AccessibleAttribute.ITEM_COUNT : AccessibleAttribute.ROW_COUNT;
                Integer n2 = (Integer)this.getAttribute(accessibleAttribute, new Object[0]);
                return n2 != null ? n2 : 0;
            }
            case NSAccessibilityColumnsAttribute: {
                Integer n3 = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_COUNT, new Object[0]);
                return n3 != null ? n3 : 1;
            }
            case NSAccessibilityChildrenAttribute: {
                if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.MENU) {
                    ObservableList observableList = (ObservableList)this.getAttribute(AccessibleAttribute.CHILDREN, new Object[0]);
                    if (observableList == null) {
                        return 0;
                    }
                    long[] arrl = this.getUnignoredChildren(observableList);
                    int n4 = arrl.length;
                    if (this.getAttribute(AccessibleAttribute.SUBMENU, new Object[0]) != null) {
                        ++n4;
                    }
                    return n4;
                }
                return -1;
            }
            case NSAccessibilityDisclosedRowsAttribute: {
                Integer n5 = (Integer)this.getAttribute(AccessibleAttribute.TREE_ITEM_COUNT, new Object[0]);
                return n5 != null ? n5 : 0;
            }
        }
        return -1;
    }

    private long[] accessibilityArrayAttributeValues(long l2, int n2, int n3) {
        MacAttribute macAttribute = MacAttribute.getAttribute(l2);
        if (macAttribute == null) {
            return null;
        }
        AccessibleAttribute accessibleAttribute = null;
        switch (macAttribute) {
            case NSAccessibilityColumnsAttribute: {
                accessibleAttribute = AccessibleAttribute.COLUMN_AT_INDEX;
                break;
            }
            case NSAccessibilityRowsAttribute: {
                if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.LIST_VIEW) {
                    accessibleAttribute = AccessibleAttribute.ITEM_AT_INDEX;
                    break;
                }
                accessibleAttribute = AccessibleAttribute.ROW_AT_INDEX;
                break;
            }
            case NSAccessibilityDisclosedRowsAttribute: {
                accessibleAttribute = AccessibleAttribute.TREE_ITEM_AT_INDEX;
                break;
            }
            case NSAccessibilityChildrenAttribute: {
                Object object;
                if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) != AccessibleRole.MENU) break;
                long[] arrl = new long[n3];
                int n4 = 0;
                if (n2 == 0 && (object = (Node)this.getAttribute(AccessibleAttribute.SUBMENU, new Object[0])) != null) {
                    arrl[n4++] = this.getNativeAccessible((Node)object);
                }
                if (n4 < n3) {
                    object = (ObservableList)this.getAttribute(AccessibleAttribute.CHILDREN, new Object[0]);
                    if (object == null) {
                        return null;
                    }
                    long[] arrl2 = this.getUnignoredChildren((ObservableList<Node>)object);
                    --n2;
                    while (n4 < n3 && n2 < arrl2.length) {
                        arrl[n4++] = arrl2[n2++];
                    }
                }
                if (n4 < n3) {
                    arrl = Arrays.copyOf(arrl, n4);
                }
                return arrl;
            }
        }
        if (accessibleAttribute != null) {
            int n5;
            long[] arrl = new long[n3];
            for (n5 = 0; n5 < n3; ++n5) {
                Node node = (Node)this.getAttribute(accessibleAttribute, n2 + n5);
                if (node == null) break;
                arrl[n5] = this.getNativeAccessible(node);
            }
            if (n5 == n3) {
                return MacAccessible.NSAccessibilityUnignoredChildren(arrl);
            }
        }
        return null;
    }

    private boolean accessibilityIsAttributeSettable(long l2) {
        MacAttribute macAttribute = MacAttribute.getAttribute(l2);
        if (macAttribute == null) {
            return false;
        }
        switch (macAttribute) {
            case NSAccessibilityDisclosingAttribute: {
                Integer n2 = (Integer)this.getAttribute(AccessibleAttribute.TREE_ITEM_COUNT, new Object[0]);
                return n2 != null && n2 > 0;
            }
            case NSAccessibilityFocusedAttribute: 
            case NSAccessibilitySelectedAttribute: 
            case NSAccessibilitySelectedRowsAttribute: 
            case NSAccessibilitySelectedCellsAttribute: {
                return true;
            }
            case NSAccessibilityValueAttribute: 
            case NSAccessibilitySelectedTextRangeAttribute: {
                AccessibleRole accessibleRole = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]));
                if (accessibleRole != AccessibleRole.TEXT_FIELD && accessibleRole != AccessibleRole.TEXT_AREA || !Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.EDITABLE, new Object[0]))) break;
                return true;
            }
        }
        return false;
    }

    private MacVariant accessibilityAttributeValue(long l2) {
        Object object;
        Object object2;
        MacAttribute macAttribute = MacAttribute.getAttribute(l2);
        if (macAttribute == null) {
            return null;
        }
        Function<Object, MacVariant> function = macAttribute.map;
        AccessibleAttribute accessibleAttribute = macAttribute.jfxAttr;
        AccessibleRole accessibleRole = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]));
        if (accessibleRole == null) {
            return null;
        }
        if (accessibleAttribute == null) {
            block0 : switch (macAttribute) {
                case NSAccessibilityValueAttribute: {
                    switch (accessibleRole) {
                        case TAB_PANE: 
                        case PAGINATION: {
                            accessibleAttribute = AccessibleAttribute.FOCUS_ITEM;
                            function = MacVariant::createNSObject;
                            break block0;
                        }
                        case RADIO_BUTTON: 
                        case TAB_ITEM: 
                        case PAGE_ITEM: {
                            accessibleAttribute = AccessibleAttribute.SELECTED;
                            function = MacVariant::createNSNumberForBoolean;
                            break block0;
                        }
                        case SCROLL_BAR: 
                        case SLIDER: 
                        case PROGRESS_INDICATOR: 
                        case THUMB: {
                            accessibleAttribute = AccessibleAttribute.VALUE;
                            function = MacVariant::createNSNumberForDouble;
                            break block0;
                        }
                        case COMBO_BOX: 
                        case TEXT: 
                        case TEXT_FIELD: 
                        case TEXT_AREA: {
                            accessibleAttribute = AccessibleAttribute.TEXT;
                            function = MacVariant::createNSString;
                            break block0;
                        }
                        case TOGGLE_BUTTON: 
                        case CHECK_BOX: {
                            accessibleAttribute = AccessibleAttribute.SELECTED;
                            function = MacVariant::createNSNumberForInt;
                            break block0;
                        }
                        case DATE_PICKER: {
                            accessibleAttribute = AccessibleAttribute.DATE;
                            function = MacVariant::createNSDate;
                            break block0;
                        }
                        case TITLED_PANE: {
                            accessibleAttribute = AccessibleAttribute.EXPANDED;
                            function = MacVariant::createNSNumberForInt;
                            break block0;
                        }
                    }
                    return null;
                }
                case NSAccessibilitySelectedChildrenAttribute: {
                    Object object3;
                    Object object4;
                    Node node = null;
                    if (accessibleRole == AccessibleRole.CONTEXT_MENU && (object4 = this.getAccessible((Scene)(object3 = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0])))) != null) {
                        node = (Node)((Accessible)object4).getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
                    }
                    if (accessibleRole == AccessibleRole.MENU_BAR) {
                        node = (Node)this.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
                    }
                    if (node != null && this.isMenuElement((AccessibleRole)((Object)(object3 = (AccessibleRole)((Object)this.getAccessible(node).getAttribute(AccessibleAttribute.ROLE, new Object[0])))))) {
                        object4 = new long[]{this.getNativeAccessible(node)};
                        return macAttribute.map.apply(object4);
                    }
                    return null;
                }
                case AXDateTimeComponents: {
                    if (this.getAttribute(AccessibleAttribute.DATE, new Object[0]) == null) {
                        return null;
                    }
                    return macAttribute.map.apply(224);
                }
            }
        }
        if (accessibleAttribute == null) {
            return null;
        }
        Object object5 = this.getAttribute(accessibleAttribute, new Object[0]);
        if (object5 == null) {
            block14 : switch (macAttribute) {
                case NSAccessibilityParentAttribute: {
                    break;
                }
                case NSAccessibilityColumnCountAttribute: {
                    object5 = 1;
                    break;
                }
                case NSAccessibilityColumnIndexRangeAttribute: {
                    if (accessibleRole == AccessibleRole.TABLE_COLUMN && (object5 = this.getAttribute(AccessibleAttribute.INDEX, new Object[0])) != null) break;
                    return null;
                }
                case AXMenuItemCmdModifiers: {
                    return macAttribute.map.apply(8);
                }
                case NSAccessibilityRoleDescriptionAttribute: {
                    switch (accessibleRole) {
                        case TITLED_PANE: {
                            object5 = "title pane";
                            break block14;
                        }
                        case SPLIT_MENU_BUTTON: {
                            object5 = "split button";
                            break block14;
                        }
                        case PAGE_ITEM: {
                            object5 = "page";
                            break block14;
                        }
                        case TAB_ITEM: {
                            object5 = "tab";
                            break block14;
                        }
                        case LIST_VIEW: {
                            object5 = "list";
                            break block14;
                        }
                    }
                    object2 = this.getRole(accessibleRole);
                    object = (Object)MacSubrole.getRole(accessibleRole);
                    object5 = MacAccessible.NSAccessibilityRoleDescription(((MacRole)object2).ptr, object != null ? ((MacSubrole)object).ptr : 0L);
                    break;
                }
                default: {
                    return null;
                }
            }
        }
        block28 : switch (macAttribute) {
            case NSAccessibilityWindowAttribute: 
            case NSAccessibilityTopLevelUIElementAttribute: {
                if (this.isMenuElement(accessibleRole)) {
                    return null;
                }
                object2 = (Scene)object5;
                object = this.getRootView((Scene)object2);
                if (object == null || ((View)object).getWindow() == null) {
                    return null;
                }
                object5 = ((View)object).getWindow().getNativeWindow();
                break;
            }
            case NSAccessibilitySubroleAttribute: {
                object2 = MacSubrole.getRole((AccessibleRole)((Object)object5));
                object5 = object2 != null ? ((MacSubrole)object2).ptr : 0L;
                break;
            }
            case NSAccessibilityRoleAttribute: {
                object2 = this.getRole(accessibleRole);
                object5 = object2 != null ? ((MacRole)object2).ptr : 0L;
                break;
            }
            case NSAccessibilityEnabledAttribute: {
                object5 = Boolean.FALSE.equals(object5);
                break;
            }
            case NSAccessibilityTabsAttribute: {
                object2 = (Integer)object5;
                object = new long[((Integer)object2).intValue()];
                for (int i2 = 0; i2 < (Integer)object2; ++i2) {
                    Node node = (Node)this.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, i2);
                    object[i2] = this.getNativeAccessible(node);
                }
                object5 = MacAccessible.NSAccessibilityUnignoredChildren((long[])object);
                break;
            }
            case NSAccessibilityChildrenAttribute: 
            case NSAccessibilitySelectedRowsAttribute: 
            case NSAccessibilitySelectedCellsAttribute: 
            case NSAccessibilityVisibleChildrenAttribute: {
                object2 = (ObservableList)object5;
                object5 = this.getUnignoredChildren((ObservableList<Node>)object2);
                break;
            }
            case NSAccessibilityParentAttribute: {
                if (this.getView() != null) {
                    if (this.getView().getWindow() == null) {
                        return null;
                    }
                    object5 = this.getView().getWindow().getNativeWindow();
                } else if (object5 != null) {
                    if (accessibleRole == AccessibleRole.CONTEXT_MENU && (object2 = (Node)this.getAttribute(AccessibleAttribute.PARENT_MENU, new Object[0])) != null && this.getAccessible((Node)object2).getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.MENU) {
                        object5 = object2;
                    }
                    object5 = this.getNativeAccessible((Node)object5);
                } else {
                    object2 = this.getRootView((Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]));
                    if (object2 == null) {
                        return null;
                    }
                    object5 = ((View)object2).getNativeView();
                }
                object5 = MacAccessible.NSAccessibilityUnignoredAncestor((Long)object5);
                break;
            }
            case NSAccessibilityValueAttribute: {
                switch (accessibleRole) {
                    case TAB_PANE: 
                    case PAGINATION: {
                        object5 = this.getNativeAccessible((Node)object5);
                        break block28;
                    }
                    case TOGGLE_BUTTON: 
                    case CHECK_BOX: {
                        if (Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.INDETERMINATE, new Object[0]))) {
                            object5 = 2;
                            break block28;
                        }
                        object5 = Boolean.TRUE.equals(object5) ? 1 : 0;
                        break block28;
                    }
                    case TITLED_PANE: {
                        object5 = Boolean.TRUE.equals(object5) ? 1 : 0;
                        break block28;
                    }
                }
                break;
            }
            case NSAccessibilityPositionAttribute: {
                object5 = this.flipBounds((Bounds)object5);
                break;
            }
            case NSAccessibilityMaxValueAttribute: {
                if (!Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.INDETERMINATE, new Object[0]))) break;
                return null;
            }
            case NSAccessibilityTitleAttribute: {
                switch (accessibleRole) {
                    case COMBO_BOX: 
                    case TEXT: 
                    case TEXT_FIELD: 
                    case TEXT_AREA: {
                        return null;
                    }
                }
                break;
            }
            case AXMenuItemCmdChar: {
                object2 = (KeyCombination)object5;
                object5 = null;
                if (object2 instanceof KeyCharacterCombination) {
                    object5 = ((KeyCharacterCombination)object2).getCharacter();
                }
                if (object2 instanceof KeyCodeCombination && this.isCmdCharBased((KeyCode)((Object)(object = ((KeyCodeCombination)object2).getCode())))) {
                    object5 = ((KeyCode)((Object)object)).getName();
                }
                if (object5 != null) break;
                return null;
            }
            case AXMenuItemCmdVirtualKey: {
                object2 = (KeyCombination)object5;
                object5 = null;
                if (object2 instanceof KeyCodeCombination && !this.isCmdCharBased((KeyCode)((Object)(object = (Object)((Object)((KeyCodeCombination)object2).getCode()))))) {
                    int n2 = ((KeyCode)((Object)object)).impl_getCode();
                    object5 = MacApplication._getMacKey(n2);
                }
                if (object5 != null) break;
                return null;
            }
            case AXMenuItemCmdGlyph: {
                object2 = (KeyCombination)object5;
                object5 = null;
                if (object2 instanceof KeyCodeCombination && !this.isCmdCharBased((KeyCode)((Object)(object = (Object)((Object)((KeyCodeCombination)object2).getCode()))))) {
                    object5 = this.getMenuItemCmdGlyph((KeyCode)((Object)object));
                }
                if (object5 != null) break;
                return null;
            }
            case AXMenuItemCmdModifiers: {
                object2 = (KeyCombination)object5;
                int n3 = 8;
                if (object2 != null) {
                    if (((KeyCombination)object2).getShortcut() == KeyCombination.ModifierValue.DOWN) {
                        n3 = 0;
                    }
                    if (((KeyCombination)object2).getAlt() == KeyCombination.ModifierValue.DOWN) {
                        n3 |= 2;
                    }
                    if (((KeyCombination)object2).getControl() == KeyCombination.ModifierValue.DOWN) {
                        n3 |= 4;
                    }
                    if (((KeyCombination)object2).getShift() == KeyCombination.ModifierValue.DOWN) {
                        n3 |= 1;
                    }
                }
                object5 = n3;
                break;
            }
            case AXMenuItemMarkChar: {
                if (Boolean.TRUE.equals(object5)) {
                    object5 = "\u2713";
                    break;
                }
                return null;
            }
            case NSAccessibilityNumberOfCharactersAttribute: {
                object2 = (String)object5;
                object5 = ((String)object2).length();
                break;
            }
            case NSAccessibilitySelectedTextAttribute: {
                int n4 = (Integer)object5;
                int n5 = -1;
                if (n4 != -1) {
                    object5 = this.getAttribute(AccessibleAttribute.SELECTION_END, new Object[0]);
                    if (object5 == null) {
                        return null;
                    }
                    n5 = (Integer)object5;
                }
                if (n4 < 0 || n5 < 0 || n4 > n5) {
                    return null;
                }
                String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                if (string == null) {
                    return null;
                }
                if (n5 > string.length()) {
                    return null;
                }
                object5 = string.substring(n4, n5);
                break;
            }
            case NSAccessibilitySelectedTextRangeAttribute: {
                int n6 = (Integer)object5;
                int n7 = -1;
                if (n6 != -1) {
                    object5 = this.getAttribute(AccessibleAttribute.SELECTION_END, new Object[0]);
                    if (object5 == null) {
                        return null;
                    }
                    n7 = (Integer)object5;
                }
                if (n6 < 0 || n7 < 0 || n6 > n7) {
                    return null;
                }
                String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                if (string == null) {
                    return null;
                }
                if (n7 > string.length()) {
                    return null;
                }
                object5 = new int[]{n6, n7 - n6};
                break;
            }
            case NSAccessibilityInsertionPointLineNumberAttribute: {
                if (accessibleRole == AccessibleRole.TEXT_AREA) {
                    object2 = (Integer)this.getAttribute(AccessibleAttribute.LINE_FOR_OFFSET, object5);
                    object5 = object2 != null ? (Integer)object2 : 0;
                    break;
                }
                object5 = 0;
                break;
            }
            case NSAccessibilityVisibleCharacterRangeAttribute: {
                object2 = (String)object5;
                object5 = new int[]{0, ((String)object2).length()};
                break;
            }
            case NSAccessibilityContentsAttribute: {
                if (object5 == null) break;
                object5 = new long[]{this.getNativeAccessible((Node)object5)};
                break;
            }
            case NSAccessibilityColumnIndexRangeAttribute: 
            case NSAccessibilityRowIndexRangeAttribute: {
                object2 = (Integer)object5;
                object5 = new int[]{(Integer)object2, 1};
                break;
            }
            case NSAccessibilityDisclosedByRowAttribute: 
            case NSAccessibilityOverflowButtonAttribute: 
            case NSAccessibilityTitleUIElementAttribute: 
            case NSAccessibilityHeaderAttribute: 
            case NSAccessibilityHorizontalScrollBarAttribute: 
            case NSAccessibilityVerticalScrollBarAttribute: {
                object5 = this.getNativeAccessible((Node)object5);
                break;
            }
            case NSAccessibilityOrientationAttribute: {
                object2 = (Orientation)((Object)object5);
                switch (1.$SwitchMap$javafx$geometry$Orientation[((Enum)object2).ordinal()]) {
                    case 1: {
                        object5 = MacOrientation.NSAccessibilityHorizontalOrientationValue.ptr;
                        break block28;
                    }
                    case 2: {
                        object5 = MacOrientation.NSAccessibilityVerticalOrientationValue.ptr;
                        break block28;
                    }
                }
                return null;
            }
            case NSAccessibilityDisclosingAttribute: {
                if (object5 != Boolean.TRUE || !Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.LEAF, new Object[0]))) break;
                object5 = Boolean.FALSE;
                break;
            }
        }
        return object5 != null ? function.apply(object5) : null;
    }

    private void accessibilitySetValue(long l2, long l3) {
        MacAttribute macAttribute = MacAttribute.getAttribute(l3);
        if (macAttribute != null) {
            switch (macAttribute) {
                case NSAccessibilityFocusedAttribute: {
                    MacVariant macVariant = MacAccessible.idToMacVariant(l2, 9);
                    if (macVariant == null || macVariant.int1 == 0) break;
                    this.executeAction(AccessibleAction.REQUEST_FOCUS, new Object[0]);
                    break;
                }
                case NSAccessibilityDisclosingAttribute: {
                    MacVariant macVariant = MacAccessible.idToMacVariant(l2, 9);
                    if (macVariant == null) break;
                    if (macVariant.int1 != 0) {
                        this.executeAction(AccessibleAction.EXPAND, new Object[0]);
                        break;
                    }
                    this.executeAction(AccessibleAction.COLLAPSE, new Object[0]);
                    break;
                }
                case NSAccessibilityExpandedAttribute: {
                    if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) != AccessibleRole.COMBO_BOX) break;
                    this.executeAction(AccessibleAction.EXPAND, new Object[0]);
                    break;
                }
                case NSAccessibilitySelectedCellsAttribute: {
                    MacVariant macVariant = MacAccessible.idToMacVariant(l2, 1);
                    if (macVariant == null || macVariant.longArray == null || macVariant.longArray.length <= 0) break;
                    long[] arrl = macVariant.longArray;
                    ObservableList<Node> observableList = FXCollections.observableArrayList();
                    for (long l4 : arrl) {
                        MacAccessible macAccessible = MacAccessible.GlassAccessibleToMacAccessible(l4);
                        if (macAccessible == null) continue;
                        Integer n2 = (Integer)macAccessible.getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
                        Integer n3 = (Integer)macAccessible.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
                        if (n2 == null || n3 == null) continue;
                        Node node = (Node)this.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, n2, n3);
                        if (node == null) continue;
                        observableList.add(node);
                    }
                    this.executeAction(AccessibleAction.SET_SELECTED_ITEMS, observableList);
                    break;
                }
                case NSAccessibilitySelectedRowsAttribute: {
                    MacVariant macVariant = MacAccessible.idToMacVariant(l2, 1);
                    if (macVariant == null || macVariant.longArray == null || macVariant.longArray.length <= 0) break;
                    long[] arrl = macVariant.longArray;
                    ObservableList<Node> observableList = FXCollections.observableArrayList();
                    for (long l5 : arrl) {
                        Integer n4;
                        MacAccessible macAccessible = MacAccessible.GlassAccessibleToMacAccessible(l5);
                        if (macAccessible == null || (n4 = (Integer)macAccessible.getAttribute(AccessibleAttribute.INDEX, new Object[0])) == null) continue;
                        Node node = (Node)this.getAttribute(AccessibleAttribute.ROW_AT_INDEX, n4);
                        if (node == null) continue;
                        observableList.add(node);
                    }
                    this.executeAction(AccessibleAction.SET_SELECTED_ITEMS, observableList);
                    break;
                }
                case NSAccessibilitySelectedTextRangeAttribute: {
                    MacVariant macVariant = MacAccessible.idToMacVariant(l2, 18);
                    if (macVariant == null) break;
                    int n5 = macVariant.int1;
                    int n6 = macVariant.int1 + macVariant.int2;
                    this.executeAction(AccessibleAction.SET_TEXT_SELECTION, n5, n6);
                    break;
                }
            }
        }
    }

    private long accessibilityIndexOfChild(long l2) {
        return -1L;
    }

    private long[] accessibilityParameterizedAttributeNames() {
        if (this.getView() != null) {
            return null;
        }
        AccessibleRole accessibleRole = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]));
        if (accessibleRole != null) {
            ArrayList<MacAttribute> arrayList = new ArrayList<MacAttribute>();
            MacRole macRole = this.getRole(accessibleRole);
            if (macRole != null && macRole.macParameterizedAttributes != null) {
                arrayList.addAll(macRole.macParameterizedAttributes);
            }
            switch (accessibleRole) {
                case LIST_VIEW: 
                case TREE_VIEW: {
                    arrayList.remove((Object)MacAttribute.NSAccessibilityCellForColumnAndRowParameterizedAttribute);
                    break;
                }
                case COMBO_BOX: 
                case TEXT: 
                case TEXT_FIELD: 
                case TEXT_AREA: 
                case PASSWORD_FIELD: {
                    arrayList.addAll(textParameterizedAttributes);
                    break;
                }
            }
            return arrayList.stream().mapToLong(macAttribute -> macAttribute.ptr).toArray();
        }
        return null;
    }

    private MacVariant accessibilityAttributeValueForParameter(long l2, long l3) {
        Object object;
        Object object2;
        Object object3;
        MacAttribute macAttribute = MacAttribute.getAttribute(l2);
        if (macAttribute == null || macAttribute.inputType == 0 || macAttribute.jfxAttr == null) {
            return null;
        }
        MacVariant macVariant = MacAccessible.idToMacVariant(l3, macAttribute.inputType);
        if (macVariant == null) {
            return null;
        }
        Object object4 = macVariant.getValue();
        switch (macAttribute) {
            case NSAccessibilityCellForColumnAndRowParameterizedAttribute: {
                object3 = (int[])object4;
                object2 = this.getAttribute(macAttribute.jfxAttr, object3[1], object3[0]);
                break;
            }
            case NSAccessibilityLineForIndexParameterizedAttribute: {
                if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.TEXT_AREA) {
                    object2 = this.getAttribute(macAttribute.jfxAttr, object4);
                    break;
                }
                object2 = 0;
                break;
            }
            case NSAccessibilityRangeForLineParameterizedAttribute: {
                if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.TEXT_AREA) {
                    object3 = (Integer)this.getAttribute(AccessibleAttribute.LINE_START, object4);
                    object = (Integer)this.getAttribute(AccessibleAttribute.LINE_END, object4);
                    if (object3 != null && object != null) {
                        object2 = new int[]{(Integer)object3, (Integer)object - (Integer)object3};
                        break;
                    }
                    object2 = null;
                    break;
                }
                object3 = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                object2 = new int[]{0, object3 != null ? ((String)object3).length() : 0};
                break;
            }
            case NSAccessibilityBoundsForRangeParameterizedAttribute: {
                object3 = (int[])object4;
                object = (Bounds[])this.getAttribute(macAttribute.jfxAttr, object3[0], object3[0] + object3[1] - 1);
                double d2 = Double.POSITIVE_INFINITY;
                double d3 = Double.POSITIVE_INFINITY;
                double d4 = Double.NEGATIVE_INFINITY;
                double d5 = Double.NEGATIVE_INFINITY;
                if (object != null) {
                    for (int i2 = 0; i2 < ((Object)object).length; ++i2) {
                        Object object5 = object[i2];
                        if (object5 == null) continue;
                        if (((Bounds)object5).getMinX() < d2) {
                            d2 = ((Bounds)object5).getMinX();
                        }
                        if (((Bounds)object5).getMinY() < d3) {
                            d3 = ((Bounds)object5).getMinY();
                        }
                        if (((Bounds)object5).getMaxX() > d4) {
                            d4 = ((Bounds)object5).getMaxX();
                        }
                        if (!(((Bounds)object5).getMaxY() > d5)) continue;
                        d5 = ((Bounds)object5).getMaxY();
                    }
                }
                object2 = this.flipBounds(new BoundingBox(d2, d3, d4 - d2, d5 - d3));
                break;
            }
            case NSAccessibilityRangeForPositionParameterizedAttribute: {
                object3 = (float[])object4;
                object = (Bounds[])this.getAttribute(macAttribute.jfxAttr, new Point2D(object3[0], object3[1]));
                if (object != null) {
                    object2 = new int[]{(Integer)object, 1};
                    break;
                }
                object2 = null;
                break;
            }
            default: {
                object2 = this.getAttribute(macAttribute.jfxAttr, object4);
            }
        }
        if (object2 == null) {
            return null;
        }
        switch (macAttribute) {
            case NSAccessibilityAttributedStringForRangeParameterizedAttribute: {
                MacVariant macVariant2;
                object3 = (String)object2;
                object3 = ((String)object3).substring(macVariant.int1, macVariant.int1 + macVariant.int2);
                object = new ArrayList();
                Font font = (Font)this.getAttribute(AccessibleAttribute.FONT, new Object[0]);
                if (font != null) {
                    macVariant2 = new MacVariant();
                    macVariant2.type = 8;
                    macVariant2.longArray = new long[]{MacText.NSAccessibilityFontNameKey.ptr, MacText.NSAccessibilityFontFamilyKey.ptr, MacText.NSAccessibilityVisibleNameKey.ptr, MacText.NSAccessibilityFontSizeKey.ptr};
                    macVariant2.variantArray = new MacVariant[]{MacVariant.createNSString(font.getName()), MacVariant.createNSString(font.getFamily()), MacVariant.createNSString(font.getName()), MacVariant.createNSNumberForDouble(font.getSize())};
                    macVariant2.key = MacText.NSAccessibilityFontTextAttribute.ptr;
                    macVariant2.location = 0;
                    macVariant2.length = ((String)object3).length();
                    object.add(macVariant2);
                }
                macVariant2 = macAttribute.map.apply(object3);
                macVariant2.variantArray = object.toArray(new MacVariant[0]);
                return macVariant2;
            }
            case NSAccessibilityStringForRangeParameterizedAttribute: {
                object3 = (String)object2;
                object2 = ((String)object3).substring(macVariant.int1, macVariant.int1 + macVariant.int2);
                break;
            }
            case NSAccessibilityCellForColumnAndRowParameterizedAttribute: {
                object2 = this.getNativeAccessible((Node)object2);
                break;
            }
        }
        return macAttribute.map.apply(object2);
    }

    private long[] accessibilityActionNames() {
        if (this.getView() != null) {
            return null;
        }
        AccessibleRole accessibleRole = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]));
        ArrayList<MacAction> arrayList = new ArrayList<MacAction>();
        if (accessibleRole != null) {
            MacRole macRole = this.getRole(accessibleRole);
            if (macRole != null && macRole.macActions != null) {
                arrayList.addAll(macRole.macActions);
            }
            if (accessibleRole != AccessibleRole.NODE && accessibleRole != AccessibleRole.PARENT) {
                arrayList.add(MacAction.NSAccessibilityShowMenuAction);
            }
        }
        return arrayList.stream().mapToLong(macAction -> macAction.ptr).toArray();
    }

    private String accessibilityActionDescription(long l2) {
        return MacAccessible.NSAccessibilityActionDescription(l2);
    }

    private void accessibilityPerformAction(long l2) {
        AccessibleRole accessibleRole;
        MacAction macAction = MacAction.getAction(l2);
        boolean bl = false;
        if (macAction == MacAction.NSAccessibilityPressAction && ((accessibleRole = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]))) == AccessibleRole.TITLED_PANE || accessibleRole == AccessibleRole.COMBO_BOX)) {
            bl = true;
        }
        if (macAction == MacAction.NSAccessibilityShowMenuAction && this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.SPLIT_MENU_BUTTON) {
            bl = true;
        }
        if (bl) {
            if (Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.EXPANDED, new Object[0]))) {
                this.executeAction(AccessibleAction.COLLAPSE, new Object[0]);
            } else {
                this.executeAction(AccessibleAction.EXPAND, new Object[0]);
            }
            return;
        }
        if (macAction != null && macAction.jfxAction != null) {
            this.executeAction(macAction.jfxAction, new Object[0]);
        }
    }

    private long accessibilityFocusedUIElement() {
        Node node = (Node)this.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
        if (node == null) {
            return 0L;
        }
        Node node2 = (Node)this.getAccessible(node).getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
        if (node2 != null) {
            return this.getNativeAccessible(node2);
        }
        return this.getNativeAccessible(node);
    }

    private boolean accessibilityIsIgnored() {
        if (this.isIgnored()) {
            return true;
        }
        if (this.isInSlider()) {
            return true;
        }
        if (this.isInMenu()) {
            AccessibleRole accessibleRole = (AccessibleRole)((Object)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]));
            return !this.isMenuElement(accessibleRole);
        }
        return this.ignoreInnerText();
    }

    private long accessibilityHitTest(float f2, float f3) {
        View view = this.getView();
        if (view == null || view.getWindow() == null) {
            return 0L;
        }
        Screen screen = view.getWindow().getScreen();
        f3 = (float)screen.getHeight() - f3;
        Node node = (Node)this.getAttribute(AccessibleAttribute.NODE_AT_POINT, new Point2D(f2, f3));
        return MacAccessible.NSAccessibilityUnignoredAncestor(this.getNativeAccessible(node));
    }

    static {
        MacAccessible._initIDs();
        if (!MacAccessible._initEnum("MacAttribute")) {
            System.err.println("Fail linking MacAttribute");
        }
        if (!MacAccessible._initEnum("MacAction")) {
            System.err.println("Fail linking MacAction");
        }
        if (!MacAccessible._initEnum("MacRole")) {
            System.err.println("Fail linking MacRole");
        }
        if (!MacAccessible._initEnum("MacSubrole")) {
            System.err.println("Fail linking MacSubrole");
        }
        if (!MacAccessible._initEnum("MacNotification")) {
            System.err.println("Fail linking MacNotification");
        }
        if (!MacAccessible._initEnum("MacOrientation")) {
            System.err.println("Fail linking MacOrientation");
        }
        if (!MacAccessible._initEnum("MacText")) {
            System.err.println("Fail linking MacText");
        }
        baseAttributes = Arrays.asList(new MacAttribute[]{MacAttribute.NSAccessibilityRoleAttribute, MacAttribute.NSAccessibilityRoleDescriptionAttribute, MacAttribute.NSAccessibilityHelpAttribute, MacAttribute.NSAccessibilityFocusedAttribute, MacAttribute.NSAccessibilityParentAttribute, MacAttribute.NSAccessibilityChildrenAttribute, MacAttribute.NSAccessibilityPositionAttribute, MacAttribute.NSAccessibilitySizeAttribute, MacAttribute.NSAccessibilityWindowAttribute, MacAttribute.NSAccessibilityTopLevelUIElementAttribute, MacAttribute.NSAccessibilityTitleUIElementAttribute});
        textAttributes = Arrays.asList(new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityValueAttribute, MacAttribute.NSAccessibilityNumberOfCharactersAttribute, MacAttribute.NSAccessibilitySelectedTextAttribute, MacAttribute.NSAccessibilitySelectedTextRangeAttribute, MacAttribute.NSAccessibilityInsertionPointLineNumberAttribute, MacAttribute.NSAccessibilityVisibleCharacterRangeAttribute});
        textParameterizedAttributes = Arrays.asList(new MacAttribute[]{MacAttribute.NSAccessibilityLineForIndexParameterizedAttribute, MacAttribute.NSAccessibilityRangeForLineParameterizedAttribute, MacAttribute.NSAccessibilityAttributedStringForRangeParameterizedAttribute, MacAttribute.NSAccessibilityStringForRangeParameterizedAttribute});
    }

    private static enum MacText {
        NSAccessibilityBackgroundColorTextAttribute,
        NSAccessibilityForegroundColorTextAttribute,
        NSAccessibilityUnderlineTextAttribute,
        NSAccessibilityStrikethroughTextAttribute,
        NSAccessibilityMarkedMisspelledTextAttribute,
        NSAccessibilityFontTextAttribute,
        NSAccessibilityFontNameKey,
        NSAccessibilityFontFamilyKey,
        NSAccessibilityVisibleNameKey,
        NSAccessibilityFontSizeKey;

        long ptr;
    }

    private static enum MacOrientation {
        NSAccessibilityHorizontalOrientationValue,
        NSAccessibilityVerticalOrientationValue,
        NSAccessibilityUnknownOrientationValue;

        long ptr;
    }

    private static enum MacNotification {
        NSAccessibilityCreatedNotification,
        NSAccessibilityFocusedUIElementChangedNotification,
        NSAccessibilityValueChangedNotification,
        NSAccessibilitySelectedChildrenChangedNotification,
        NSAccessibilitySelectedRowsChangedNotification,
        NSAccessibilityTitleChangedNotification,
        NSAccessibilityRowCountChangedNotification,
        NSAccessibilitySelectedCellsChangedNotification,
        NSAccessibilityUIElementDestroyedNotification,
        NSAccessibilitySelectedTextChangedNotification,
        NSAccessibilityRowExpandedNotification,
        NSAccessibilityRowCollapsedNotification,
        AXMenuOpened,
        AXMenuClosed;

        long ptr;
    }

    private static enum MacAction {
        NSAccessibilityCancelAction,
        NSAccessibilityConfirmAction,
        NSAccessibilityDecrementAction(AccessibleAction.DECREMENT),
        NSAccessibilityDeleteAction,
        NSAccessibilityIncrementAction(AccessibleAction.INCREMENT),
        NSAccessibilityPickAction,
        NSAccessibilityPressAction(AccessibleAction.FIRE),
        NSAccessibilityRaiseAction,
        NSAccessibilityShowMenuAction(AccessibleAction.SHOW_MENU);

        long ptr;
        AccessibleAction jfxAction;

        private MacAction() {
        }

        private MacAction(AccessibleAction accessibleAction) {
            this.jfxAction = accessibleAction;
        }

        static MacAction getAction(long l2) {
            for (MacAction macAction : MacAction.values()) {
                if (macAction.ptr != l2 && !MacAccessible.isEqualToString(macAction.ptr, l2)) continue;
                return macAction;
            }
            return null;
        }
    }

    private static enum MacSubrole {
        NSAccessibilityTableRowSubrole(AccessibleRole.LIST_ITEM, AccessibleRole.TABLE_ROW),
        NSAccessibilitySecureTextFieldSubrole(AccessibleRole.PASSWORD_FIELD),
        NSAccessibilityOutlineRowSubrole(new AccessibleRole[]{AccessibleRole.TREE_ITEM, AccessibleRole.TREE_TABLE_ROW}, new MacAttribute[]{MacAttribute.NSAccessibilityDisclosedByRowAttribute, MacAttribute.NSAccessibilityDisclosedRowsAttribute, MacAttribute.NSAccessibilityDisclosingAttribute, MacAttribute.NSAccessibilityDisclosureLevelAttribute}),
        NSAccessibilityDecrementArrowSubrole(new AccessibleRole[]{AccessibleRole.DECREMENT_BUTTON}, new MacAttribute[]{MacAttribute.NSAccessibilitySubroleAttribute}),
        NSAccessibilityIncrementArrowSubrole(new AccessibleRole[]{AccessibleRole.INCREMENT_BUTTON}, new MacAttribute[]{MacAttribute.NSAccessibilitySubroleAttribute});

        long ptr;
        AccessibleRole[] jfxRoles;
        List<MacAttribute> macAttributes;

        private MacSubrole(AccessibleRole ... arraccessibleRole) {
            this(arraccessibleRole, (MacAttribute[])null);
        }

        private MacSubrole(AccessibleRole[] arraccessibleRole, MacAttribute[] arrmacAttribute) {
            this.jfxRoles = arraccessibleRole;
            this.macAttributes = arrmacAttribute != null ? Arrays.asList(arrmacAttribute) : null;
        }

        static MacSubrole getRole(AccessibleRole accessibleRole) {
            if (accessibleRole == null) {
                return null;
            }
            for (MacSubrole macSubrole : MacSubrole.values()) {
                for (AccessibleRole accessibleRole2 : macSubrole.jfxRoles) {
                    if (accessibleRole2 != accessibleRole) continue;
                    return macSubrole;
                }
            }
            return null;
        }
    }

    private static enum MacRole {
        NSAccessibilityUnknownRole(AccessibleRole.NODE, null, null),
        NSAccessibilityGroupRole(AccessibleRole.PARENT, null, null),
        NSAccessibilityButtonRole(new AccessibleRole[]{AccessibleRole.BUTTON, AccessibleRole.INCREMENT_BUTTON, AccessibleRole.DECREMENT_BUTTON, AccessibleRole.SPLIT_MENU_BUTTON}, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityTitleAttribute}, new MacAction[]{MacAction.NSAccessibilityPressAction}, null),
        NSAccessibilityIncrementorRole(AccessibleRole.SPINNER, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityTitleAttribute}, new MacAction[]{MacAction.NSAccessibilityIncrementAction, MacAction.NSAccessibilityDecrementAction}),
        AXJFXTOOLTIP(AccessibleRole.TOOLTIP, null, null),
        NSAccessibilityImageRole(AccessibleRole.IMAGE_VIEW, null, null),
        NSAccessibilityRadioButtonRole(new AccessibleRole[]{AccessibleRole.RADIO_BUTTON, AccessibleRole.TAB_ITEM, AccessibleRole.PAGE_ITEM}, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityTitleAttribute, MacAttribute.NSAccessibilityValueAttribute}, new MacAction[]{MacAction.NSAccessibilityPressAction}, null),
        NSAccessibilityCheckBoxRole(new AccessibleRole[]{AccessibleRole.CHECK_BOX, AccessibleRole.TOGGLE_BUTTON}, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityTitleAttribute, MacAttribute.NSAccessibilityValueAttribute}, new MacAction[]{MacAction.NSAccessibilityPressAction}, null),
        NSAccessibilityComboBoxRole(AccessibleRole.COMBO_BOX, new MacAttribute[]{MacAttribute.NSAccessibilityExpandedAttribute}, new MacAction[]{MacAction.NSAccessibilityPressAction}),
        NSAccessibilityPopUpButtonRole(AccessibleRole.COMBO_BOX, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityValueAttribute}, new MacAction[]{MacAction.NSAccessibilityPressAction}),
        NSAccessibilityTabGroupRole(new AccessibleRole[]{AccessibleRole.TAB_PANE, AccessibleRole.PAGINATION}, new MacAttribute[]{MacAttribute.NSAccessibilityTabsAttribute, MacAttribute.NSAccessibilityValueAttribute}, null, null),
        NSAccessibilityProgressIndicatorRole(AccessibleRole.PROGRESS_INDICATOR, new MacAttribute[]{MacAttribute.NSAccessibilityOrientationAttribute, MacAttribute.NSAccessibilityValueAttribute, MacAttribute.NSAccessibilityMaxValueAttribute, MacAttribute.NSAccessibilityMinValueAttribute}, null),
        NSAccessibilityMenuBarRole(AccessibleRole.MENU_BAR, new MacAttribute[]{MacAttribute.NSAccessibilitySelectedChildrenAttribute, MacAttribute.NSAccessibilityEnabledAttribute}, new MacAction[]{MacAction.NSAccessibilityCancelAction}),
        NSAccessibilityMenuRole(AccessibleRole.CONTEXT_MENU, new MacAttribute[]{MacAttribute.NSAccessibilitySelectedChildrenAttribute, MacAttribute.NSAccessibilityEnabledAttribute}, new MacAction[]{MacAction.NSAccessibilityPressAction, MacAction.NSAccessibilityCancelAction}),
        NSAccessibilityMenuItemRole(new AccessibleRole[]{AccessibleRole.MENU_ITEM, AccessibleRole.RADIO_MENU_ITEM, AccessibleRole.CHECK_MENU_ITEM, AccessibleRole.MENU}, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityTitleAttribute, MacAttribute.NSAccessibilitySelectedAttribute, MacAttribute.AXMenuItemCmdChar, MacAttribute.AXMenuItemCmdVirtualKey, MacAttribute.AXMenuItemCmdGlyph, MacAttribute.AXMenuItemCmdModifiers, MacAttribute.AXMenuItemMarkChar}, new MacAction[]{MacAction.NSAccessibilityPressAction, MacAction.NSAccessibilityCancelAction}, null),
        NSAccessibilityMenuButtonRole(AccessibleRole.MENU_BUTTON, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityTitleAttribute}, new MacAction[]{MacAction.NSAccessibilityPressAction}),
        NSAccessibilityStaticTextRole(new AccessibleRole[]{AccessibleRole.TEXT}, null, null, null),
        NSAccessibilityTextFieldRole(new AccessibleRole[]{AccessibleRole.TEXT_FIELD, AccessibleRole.PASSWORD_FIELD}, null, null, null),
        NSAccessibilityTextAreaRole(AccessibleRole.TEXT_AREA, null, null),
        NSAccessibilitySliderRole(AccessibleRole.SLIDER, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityOrientationAttribute, MacAttribute.NSAccessibilityValueAttribute, MacAttribute.NSAccessibilityMaxValueAttribute, MacAttribute.NSAccessibilityMinValueAttribute}, new MacAction[]{MacAction.NSAccessibilityDecrementAction, MacAction.NSAccessibilityIncrementAction}),
        NSAccessibilityScrollAreaRole(AccessibleRole.SCROLL_PANE, new MacAttribute[]{MacAttribute.NSAccessibilityContentsAttribute, MacAttribute.NSAccessibilityHorizontalScrollBarAttribute, MacAttribute.NSAccessibilityVerticalScrollBarAttribute}, null),
        NSAccessibilityScrollBarRole(AccessibleRole.SCROLL_BAR, new MacAttribute[]{MacAttribute.NSAccessibilityValueAttribute, MacAttribute.NSAccessibilityMinValueAttribute, MacAttribute.NSAccessibilityMaxValueAttribute, MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityOrientationAttribute}, null),
        NSAccessibilityValueIndicatorRole(AccessibleRole.THUMB, new MacAttribute[]{MacAttribute.NSAccessibilityValueAttribute}, null),
        NSAccessibilityRowRole(new AccessibleRole[]{AccessibleRole.LIST_ITEM, AccessibleRole.TABLE_ROW, AccessibleRole.TREE_ITEM, AccessibleRole.TREE_TABLE_ROW}, new MacAttribute[]{MacAttribute.NSAccessibilitySubroleAttribute, MacAttribute.NSAccessibilityIndexAttribute, MacAttribute.NSAccessibilitySelectedAttribute, MacAttribute.NSAccessibilityVisibleChildrenAttribute}, null, null),
        NSAccessibilityTableRole(new AccessibleRole[]{AccessibleRole.LIST_VIEW, AccessibleRole.TABLE_VIEW}, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityColumnsAttribute, MacAttribute.NSAccessibilityHeaderAttribute, MacAttribute.NSAccessibilityRowsAttribute, MacAttribute.NSAccessibilitySelectedRowsAttribute, MacAttribute.NSAccessibilityRowCountAttribute, MacAttribute.NSAccessibilityColumnCountAttribute, MacAttribute.NSAccessibilitySelectedCellsAttribute}, null, new MacAttribute[]{MacAttribute.NSAccessibilityCellForColumnAndRowParameterizedAttribute}),
        NSAccessibilityColumnRole(AccessibleRole.TABLE_COLUMN, new MacAttribute[]{MacAttribute.NSAccessibilityHeaderAttribute, MacAttribute.NSAccessibilityIndexAttribute, MacAttribute.NSAccessibilityRowsAttribute, MacAttribute.NSAccessibilitySelectedAttribute}, null),
        NSAccessibilityCellRole(new AccessibleRole[]{AccessibleRole.TABLE_CELL, AccessibleRole.TREE_TABLE_CELL}, new MacAttribute[]{MacAttribute.NSAccessibilityColumnIndexRangeAttribute, MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityRowIndexRangeAttribute, MacAttribute.NSAccessibilitySelectedAttribute}, null, null),
        NSAccessibilityLinkRole(AccessibleRole.HYPERLINK, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.AXVisited}, null),
        NSAccessibilityOutlineRole(new AccessibleRole[]{AccessibleRole.TREE_VIEW, AccessibleRole.TREE_TABLE_VIEW}, new MacAttribute[]{MacAttribute.NSAccessibilityColumnsAttribute, MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityHeaderAttribute, MacAttribute.NSAccessibilityRowsAttribute, MacAttribute.NSAccessibilitySelectedRowsAttribute, MacAttribute.NSAccessibilitySelectedCellsAttribute}, null, new MacAttribute[]{MacAttribute.NSAccessibilityCellForColumnAndRowParameterizedAttribute}),
        NSAccessibilityDisclosureTriangleRole(AccessibleRole.TITLED_PANE, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityValueAttribute}, new MacAction[]{MacAction.NSAccessibilityPressAction}),
        NSAccessibilityToolbarRole(AccessibleRole.TOOL_BAR, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityOverflowButtonAttribute}, null),
        AXDateTimeArea(AccessibleRole.DATE_PICKER, new MacAttribute[]{MacAttribute.NSAccessibilityEnabledAttribute, MacAttribute.NSAccessibilityValueAttribute, MacAttribute.AXDateTimeComponents}, null);

        long ptr;
        AccessibleRole[] jfxRoles;
        List<MacAttribute> macAttributes;
        List<MacAttribute> macParameterizedAttributes;
        List<MacAction> macActions;

        private MacRole(AccessibleRole accessibleRole, MacAttribute[] arrmacAttribute, MacAction[] arrmacAction) {
            this(new AccessibleRole[]{accessibleRole}, arrmacAttribute, arrmacAction, null);
        }

        private MacRole(AccessibleRole[] arraccessibleRole, MacAttribute[] arrmacAttribute, MacAction[] arrmacAction, MacAttribute[] arrmacAttribute2) {
            this.jfxRoles = arraccessibleRole;
            this.macAttributes = arrmacAttribute != null ? Arrays.asList(arrmacAttribute) : null;
            this.macActions = arrmacAction != null ? Arrays.asList(arrmacAction) : null;
            this.macParameterizedAttributes = arrmacAttribute2 != null ? Arrays.asList(arrmacAttribute2) : null;
        }

        static MacRole getRole(AccessibleRole accessibleRole) {
            if (accessibleRole == null) {
                return null;
            }
            for (MacRole macRole : MacRole.values()) {
                for (AccessibleRole accessibleRole2 : macRole.jfxRoles) {
                    if (accessibleRole2 != accessibleRole) continue;
                    return macRole;
                }
            }
            return null;
        }
    }

    private static enum MacAttribute {
        NSAccessibilityValueAttribute(null, null),
        NSAccessibilityChildrenAttribute(AccessibleAttribute.CHILDREN, MacVariant::createNSArray),
        NSAccessibilityEnabledAttribute(AccessibleAttribute.DISABLED, MacVariant::createNSNumberForBoolean),
        NSAccessibilityHelpAttribute(AccessibleAttribute.HELP, MacVariant::createNSString),
        NSAccessibilityFocusedAttribute(AccessibleAttribute.FOCUSED, MacVariant::createNSNumberForBoolean),
        NSAccessibilityExpandedAttribute(AccessibleAttribute.EXPANDED, MacVariant::createNSNumberForBoolean),
        NSAccessibilityMaxValueAttribute(AccessibleAttribute.MAX_VALUE, MacVariant::createNSNumberForDouble),
        NSAccessibilityMinValueAttribute(AccessibleAttribute.MIN_VALUE, MacVariant::createNSNumberForDouble),
        NSAccessibilityParentAttribute(AccessibleAttribute.PARENT, MacVariant::createNSObject),
        NSAccessibilityPositionAttribute(AccessibleAttribute.BOUNDS, MacVariant::createNSValueForPoint),
        NSAccessibilityRoleAttribute(AccessibleAttribute.ROLE, MacVariant::createNSObject),
        NSAccessibilitySubroleAttribute(AccessibleAttribute.ROLE, MacVariant::createNSObject),
        NSAccessibilityRoleDescriptionAttribute(AccessibleAttribute.ROLE_DESCRIPTION, MacVariant::createNSString),
        NSAccessibilitySizeAttribute(AccessibleAttribute.BOUNDS, MacVariant::createNSValueForSize),
        NSAccessibilityTabsAttribute(AccessibleAttribute.ITEM_COUNT, MacVariant::createNSArray),
        NSAccessibilityTitleAttribute(AccessibleAttribute.TEXT, MacVariant::createNSString),
        NSAccessibilityTopLevelUIElementAttribute(AccessibleAttribute.SCENE, MacVariant::createNSObject),
        NSAccessibilityWindowAttribute(AccessibleAttribute.SCENE, MacVariant::createNSObject),
        NSAccessibilityTitleUIElementAttribute(AccessibleAttribute.LABELED_BY, MacVariant::createNSObject),
        NSAccessibilityOrientationAttribute(AccessibleAttribute.ORIENTATION, MacVariant::createNSObject),
        NSAccessibilityOverflowButtonAttribute(AccessibleAttribute.OVERFLOW_BUTTON, MacVariant::createNSObject),
        AXVisited(AccessibleAttribute.VISITED, MacVariant::createNSNumberForBoolean),
        AXMenuItemCmdChar(AccessibleAttribute.ACCELERATOR, MacVariant::createNSString),
        AXMenuItemCmdVirtualKey(AccessibleAttribute.ACCELERATOR, MacVariant::createNSNumberForInt),
        AXMenuItemCmdGlyph(AccessibleAttribute.ACCELERATOR, MacVariant::createNSNumberForInt),
        AXMenuItemCmdModifiers(AccessibleAttribute.ACCELERATOR, MacVariant::createNSNumberForInt),
        AXMenuItemMarkChar(AccessibleAttribute.SELECTED, MacVariant::createNSString),
        AXDateTimeComponents(null, MacVariant::createNSNumberForInt),
        NSAccessibilitySelectedChildrenAttribute(null, MacVariant::createNSArray),
        NSAccessibilityNumberOfCharactersAttribute(AccessibleAttribute.TEXT, MacVariant::createNSNumberForInt),
        NSAccessibilitySelectedTextAttribute(AccessibleAttribute.SELECTION_START, MacVariant::createNSString),
        NSAccessibilitySelectedTextRangeAttribute(AccessibleAttribute.SELECTION_START, MacVariant::createNSValueForRange),
        NSAccessibilitySelectedTextRangesAttribute(null, null),
        NSAccessibilityInsertionPointLineNumberAttribute(AccessibleAttribute.CARET_OFFSET, MacVariant::createNSNumberForInt),
        NSAccessibilityVisibleCharacterRangeAttribute(AccessibleAttribute.TEXT, MacVariant::createNSValueForRange),
        NSAccessibilityContentsAttribute(AccessibleAttribute.CONTENTS, MacVariant::createNSArray),
        NSAccessibilityHorizontalScrollBarAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, MacVariant::createNSObject),
        NSAccessibilityVerticalScrollBarAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, MacVariant::createNSObject),
        NSAccessibilityIndexAttribute(AccessibleAttribute.INDEX, MacVariant::createNSNumberForInt),
        NSAccessibilitySelectedAttribute(AccessibleAttribute.SELECTED, MacVariant::createNSNumberForBoolean),
        NSAccessibilityVisibleChildrenAttribute(AccessibleAttribute.CHILDREN, MacVariant::createNSArray),
        NSAccessibilityDisclosedByRowAttribute(AccessibleAttribute.TREE_ITEM_PARENT, MacVariant::createNSObject),
        NSAccessibilityDisclosedRowsAttribute(null, null),
        NSAccessibilityDisclosingAttribute(AccessibleAttribute.EXPANDED, MacVariant::createNSNumberForBoolean),
        NSAccessibilityDisclosureLevelAttribute(AccessibleAttribute.DISCLOSURE_LEVEL, MacVariant::createNSNumberForInt),
        NSAccessibilityColumnsAttribute(null, null),
        NSAccessibilityRowsAttribute(null, null),
        NSAccessibilityHeaderAttribute(AccessibleAttribute.HEADER, MacVariant::createNSObject),
        NSAccessibilitySelectedRowsAttribute(AccessibleAttribute.SELECTED_ITEMS, MacVariant::createNSArray),
        NSAccessibilityRowCountAttribute(AccessibleAttribute.ROW_COUNT, MacVariant::createNSNumberForInt),
        NSAccessibilityColumnCountAttribute(AccessibleAttribute.COLUMN_COUNT, MacVariant::createNSNumberForInt),
        NSAccessibilitySelectedCellsAttribute(AccessibleAttribute.SELECTED_ITEMS, MacVariant::createNSArray),
        NSAccessibilityRowIndexRangeAttribute(AccessibleAttribute.ROW_INDEX, MacVariant::createNSValueForRange),
        NSAccessibilityColumnIndexRangeAttribute(AccessibleAttribute.COLUMN_INDEX, MacVariant::createNSValueForRange),
        NSAccessibilityLineForIndexParameterizedAttribute(AccessibleAttribute.LINE_FOR_OFFSET, MacVariant::createNSNumberForInt, 10),
        NSAccessibilityStringForRangeParameterizedAttribute(AccessibleAttribute.TEXT, MacVariant::createNSString, 18),
        NSAccessibilityRangeForLineParameterizedAttribute(AccessibleAttribute.LINE_START, MacVariant::createNSValueForRange, 10),
        NSAccessibilityAttributedStringForRangeParameterizedAttribute(AccessibleAttribute.TEXT, MacVariant::createNSAttributedString, 18),
        NSAccessibilityCellForColumnAndRowParameterizedAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, MacVariant::createNSObject, 3),
        NSAccessibilityRangeForPositionParameterizedAttribute(AccessibleAttribute.OFFSET_AT_POINT, MacVariant::createNSValueForRange, 15),
        NSAccessibilityBoundsForRangeParameterizedAttribute(AccessibleAttribute.BOUNDS_FOR_RANGE, MacVariant::createNSValueForRectangle, 18);

        long ptr;
        AccessibleAttribute jfxAttr;
        Function<Object, MacVariant> map;
        int inputType;

        private MacAttribute(AccessibleAttribute accessibleAttribute, Function<Object, MacVariant> function, int n3) {
            this.jfxAttr = accessibleAttribute;
            this.map = function;
            this.inputType = n3;
        }

        private MacAttribute(AccessibleAttribute accessibleAttribute, Function<Object, MacVariant> function) {
            this.jfxAttr = accessibleAttribute;
            this.map = function;
        }

        static MacAttribute getAttribute(long l2) {
            if (l2 == 0L) {
                return null;
            }
            for (MacAttribute macAttribute : MacAttribute.values()) {
                if (l2 != macAttribute.ptr && !MacAccessible.isEqualToString(macAttribute.ptr, l2)) continue;
                return macAttribute;
            }
            return null;
        }
    }
}

