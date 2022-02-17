/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

class TreeUtil {
    TreeUtil() {
    }

    static <T> int getExpandedDescendantCount(TreeItem<T> treeItem, boolean bl) {
        if (treeItem == null) {
            return 0;
        }
        if (treeItem.isLeaf()) {
            return 1;
        }
        return treeItem.getExpandedDescendentCount(bl);
    }

    static int updateExpandedItemCount(TreeItem treeItem, boolean bl, boolean bl2) {
        if (treeItem == null) {
            return 0;
        }
        if (!treeItem.isExpanded()) {
            return 1;
        }
        int n2 = TreeUtil.getExpandedDescendantCount(treeItem, bl);
        if (!bl2) {
            --n2;
        }
        return n2;
    }

    static <T> TreeItem<T> getItem(TreeItem<T> treeItem, int n2, boolean bl) {
        if (treeItem == null) {
            return null;
        }
        if (n2 == 0) {
            return treeItem;
        }
        if (n2 >= TreeUtil.getExpandedDescendantCount(treeItem, bl)) {
            return null;
        }
        ObservableList<TreeItem<T>> observableList = treeItem.getChildren();
        if (observableList == null) {
            return null;
        }
        int n3 = n2 - 1;
        int n4 = observableList.size();
        for (int i2 = 0; i2 < n4; ++i2) {
            TreeItem treeItem2 = (TreeItem)observableList.get(i2);
            if (n3 == 0) {
                return treeItem2;
            }
            if (treeItem2.isLeaf() || !treeItem2.isExpanded()) {
                --n3;
                continue;
            }
            int n5 = TreeUtil.getExpandedDescendantCount(treeItem2, bl);
            if (n3 >= n5) {
                n3 -= n5;
                continue;
            }
            TreeItem<T> treeItem3 = TreeUtil.getItem(treeItem2, n3, bl);
            if (treeItem3 != null) {
                return treeItem3;
            }
            --n3;
        }
        return null;
    }

    static <T> int getRow(TreeItem<T> treeItem, TreeItem<T> treeItem2, boolean bl, boolean bl2) {
        if (treeItem == null) {
            return -1;
        }
        if (bl2 && treeItem.equals(treeItem2)) {
            return 0;
        }
        int n2 = 0;
        TreeItem<T> treeItem3 = treeItem;
        TreeItem<T> treeItem4 = treeItem.getParent();
        boolean bl3 = false;
        while (!treeItem3.equals(treeItem2) && treeItem4 != null) {
            if (!treeItem4.isExpanded()) {
                bl3 = true;
                break;
            }
            ObservableList observableList = treeItem4.children;
            int n3 = observableList.indexOf(treeItem3);
            for (int i2 = n3 - 1; i2 > -1; --i2) {
                TreeItem treeItem5 = (TreeItem)observableList.get(i2);
                if (treeItem5 == null) continue;
                n2 += TreeUtil.getExpandedDescendantCount(treeItem5, bl);
                if (!treeItem5.equals(treeItem2)) continue;
                if (!bl2) {
                    return -1;
                }
                return n2;
            }
            treeItem3 = treeItem4;
            treeItem4 = treeItem4.getParent();
            ++n2;
        }
        return treeItem4 == null && n2 == 0 || bl3 ? -1 : (bl2 ? n2 : n2 - 1);
    }
}

