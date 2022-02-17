/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.TableColumnBase;

class ControlUtils {
    private ControlUtils() {
    }

    public static void scrollToIndex(Control control, int n2) {
        Utils.executeOnceWhenPropertyIsNonNull(control.skinProperty(), skin -> Event.fireEvent(control, new ScrollToEvent<Integer>(control, control, ScrollToEvent.scrollToTopIndex(), n2)));
    }

    public static void scrollToColumn(Control control, TableColumnBase<?, ?> tableColumnBase) {
        Utils.executeOnceWhenPropertyIsNonNull(control.skinProperty(), skin -> control.fireEvent(new ScrollToEvent<TableColumnBase>(control, control, ScrollToEvent.scrollToColumn(), tableColumnBase)));
    }

    static void requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(Control control) {
        Node node;
        Scene scene = control.getScene();
        Node node2 = node = scene == null ? null : scene.getFocusOwner();
        if (node == null) {
            control.requestFocus();
        } else if (!control.equals(node)) {
            for (Parent parent = node.getParent(); parent != null; parent = parent.getParent()) {
                if (!control.equals(parent)) continue;
                control.requestFocus();
                break;
            }
        }
    }

    static <T> ListChangeListener.Change<T> buildClearAndSelectChange(ObservableList<T> observableList, final List<T> list, final int n2) {
        return new ListChangeListener.Change<T>(observableList){
            private final int[] EMPTY_PERM;
            private final int removedSize;
            private final List<T> firstRemovedRange;
            private final List<T> secondRemovedRange;
            private boolean invalid;
            private boolean atFirstRange;
            private int from;
            {
                super(observableList);
                this.EMPTY_PERM = new int[0];
                this.removedSize = list.size();
                this.invalid = true;
                this.atFirstRange = true;
                this.from = -1;
                int n3 = n2 >= this.removedSize ? this.removedSize : (n2 < 0 ? 0 : n2);
                this.firstRemovedRange = list.subList(0, n3);
                this.secondRemovedRange = list.subList(n3, this.removedSize);
            }

            @Override
            public int getFrom() {
                this.checkState();
                return this.from;
            }

            @Override
            public int getTo() {
                return this.getFrom();
            }

            @Override
            public List<T> getRemoved() {
                this.checkState();
                return this.atFirstRange ? this.firstRemovedRange : this.secondRemovedRange;
            }

            @Override
            public int getRemovedSize() {
                return this.atFirstRange ? this.firstRemovedRange.size() : this.secondRemovedRange.size();
            }

            @Override
            protected int[] getPermutation() {
                this.checkState();
                return this.EMPTY_PERM;
            }

            @Override
            public boolean next() {
                if (this.invalid && this.atFirstRange) {
                    this.invalid = false;
                    this.from = 0;
                    return true;
                }
                if (this.atFirstRange && !this.secondRemovedRange.isEmpty()) {
                    this.atFirstRange = false;
                    this.from = 1;
                    return true;
                }
                return false;
            }

            @Override
            public void reset() {
                this.invalid = true;
                this.atFirstRange = true;
            }

            private void checkState() {
                if (this.invalid) {
                    throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
                }
            }
        };
    }
}

