/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class TabPaneBehavior
extends BehaviorBase<TabPane> {
    private static final String HOME = "Home";
    private static final String END = "End";
    private static final String CTRL_PAGE_UP = "Ctrl_Page_Up";
    private static final String CTRL_PAGE_DOWN = "Ctrl_Page_Down";
    private static final String CTRL_TAB = "Ctrl_Tab";
    private static final String CTRL_SHIFT_TAB = "Ctrl_Shift_Tab";
    protected static final List<KeyBinding> TAB_PANE_BINDINGS = new ArrayList<KeyBinding>();

    @Override
    protected void callAction(String string) {
        boolean bl;
        boolean bl2 = bl = ((TabPane)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
        if ("TraverseLeft".equals(string) && !bl || "TraverseRight".equals(string) && bl || "TraverseUp".equals(string)) {
            if (((TabPane)this.getControl()).isFocused()) {
                this.selectPreviousTab();
            }
        } else if ("TraverseRight".equals(string) && !bl || "TraverseLeft".equals(string) && bl || "TraverseDown".equals(string)) {
            if (((TabPane)this.getControl()).isFocused()) {
                this.selectNextTab();
            }
        } else if (CTRL_TAB.equals(string) || CTRL_PAGE_DOWN.equals(string)) {
            this.selectNextTab();
        } else if (CTRL_SHIFT_TAB.equals(string) || CTRL_PAGE_UP.equals(string)) {
            this.selectPreviousTab();
        } else if (HOME.equals(string)) {
            if (((TabPane)this.getControl()).isFocused()) {
                this.moveSelection(0, 1);
            }
        } else if (END.equals(string)) {
            if (((TabPane)this.getControl()).isFocused()) {
                this.moveSelection(((TabPane)this.getControl()).getTabs().size() - 1, -1);
            }
        } else {
            super.callAction(string);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        TabPane tabPane = (TabPane)this.getControl();
        tabPane.requestFocus();
    }

    public TabPaneBehavior(TabPane tabPane) {
        super(tabPane, TAB_PANE_BINDINGS);
    }

    public void selectTab(Tab tab) {
        ((TabPane)this.getControl()).getSelectionModel().select(tab);
    }

    public boolean canCloseTab(Tab tab) {
        Event event = new Event(tab, tab, Tab.TAB_CLOSE_REQUEST_EVENT);
        Event.fireEvent(tab, event);
        return !event.isConsumed();
    }

    public void closeTab(Tab tab) {
        TabPane tabPane = (TabPane)this.getControl();
        int n2 = tabPane.getTabs().indexOf(tab);
        if (n2 != -1) {
            tabPane.getTabs().remove(n2);
        }
        if (tab.getOnClosed() != null) {
            Event.fireEvent(tab, new Event(Tab.CLOSED_EVENT));
        }
    }

    public void selectNextTab() {
        this.moveSelection(1);
    }

    public void selectPreviousTab() {
        this.moveSelection(-1);
    }

    private void moveSelection(int n2) {
        this.moveSelection(((TabPane)this.getControl()).getSelectionModel().getSelectedIndex(), n2);
    }

    private void moveSelection(int n2, int n3) {
        TabPane tabPane = (TabPane)this.getControl();
        int n4 = this.findValidTab(n2, n3);
        if (n4 > -1) {
            SingleSelectionModel<Tab> singleSelectionModel = tabPane.getSelectionModel();
            ((SelectionModel)singleSelectionModel).select(n4);
        }
        tabPane.requestFocus();
    }

    private int findValidTab(int n2, int n3) {
        TabPane tabPane = (TabPane)this.getControl();
        ObservableList<Tab> observableList = tabPane.getTabs();
        int n4 = observableList.size();
        int n5 = n2;
        do {
            Tab tab;
            if ((tab = (Tab)observableList.get(n5 = this.nextIndex(n5 + n3, n4))) == null || tab.isDisable()) continue;
            return n5;
        } while (n5 != n2);
        return -1;
    }

    private int nextIndex(int n2, int n3) {
        int n4 = n2 % n3;
        if (n4 > 0 && n3 < 0) {
            n4 = n4 + n3 - 0;
        } else if (n4 < 0 && n3 > 0) {
            n4 = n4 + n3 - 0;
        }
        return n4;
    }

    static {
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.HOME, HOME));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.END, END));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, CTRL_PAGE_UP).ctrl());
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, CTRL_PAGE_DOWN).ctrl());
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_TAB).ctrl());
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_SHIFT_TAB).shift().ctrl());
    }
}

