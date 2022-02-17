/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class SplitPaneSkin
extends BehaviorSkinBase<SplitPane, BehaviorBase<SplitPane>> {
    private ObservableList<Content> contentRegions;
    private ObservableList<ContentDivider> contentDividers;
    private boolean horizontal = ((SplitPane)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL;
    private double previousSize = -1.0;
    private int lastDividerUpdate = 0;
    private boolean resize = false;
    private boolean checkDividerPos = true;

    public SplitPaneSkin(SplitPane splitPane) {
        super(splitPane, new BehaviorBase<SplitPane>(splitPane, Collections.emptyList()));
        this.contentRegions = FXCollections.observableArrayList();
        this.contentDividers = FXCollections.observableArrayList();
        int n2 = 0;
        for (Node object : ((SplitPane)this.getSkinnable()).getItems()) {
            this.addContent(n2++, object);
        }
        this.initializeContentListener();
        for (SplitPane.Divider divider : ((SplitPane)this.getSkinnable()).getDividers()) {
            this.addDivider(divider);
        }
        this.registerChangeListener(splitPane.orientationProperty(), "ORIENTATION");
        this.registerChangeListener(splitPane.widthProperty(), "WIDTH");
        this.registerChangeListener(splitPane.heightProperty(), "HEIGHT");
    }

    private void addContent(int n2, Node node) {
        Content content = new Content(node);
        this.contentRegions.add(n2, content);
        this.getChildren().add(n2, content);
    }

    private void removeContent(Node node) {
        for (Content content : this.contentRegions) {
            if (!content.getContent().equals(node)) continue;
            this.getChildren().remove(content);
            this.contentRegions.remove(content);
            break;
        }
    }

    private void initializeContentListener() {
        ((SplitPane)this.getSkinnable()).getItems().addListener(change -> {
            while (change.next()) {
                int n2;
                if (change.wasPermutated() || change.wasUpdated()) {
                    this.getChildren().clear();
                    this.contentRegions.clear();
                    n2 = 0;
                    for (Node node : change.getList()) {
                        this.addContent(n2++, node);
                    }
                    continue;
                }
                for (Node node : change.getRemoved()) {
                    this.removeContent(node);
                }
                n2 = change.getFrom();
                for (Node node : change.getAddedSubList()) {
                    this.addContent(n2++, node);
                }
            }
            this.removeAllDividers();
            for (SplitPane.Divider divider : ((SplitPane)this.getSkinnable()).getDividers()) {
                this.addDivider(divider);
            }
        });
    }

    private void checkDividerPosition(ContentDivider contentDivider, double d2, double d3) {
        double d4;
        double d5;
        double d6;
        double d7 = contentDivider.prefWidth(-1.0);
        Content content = this.getLeft(contentDivider);
        Content content2 = this.getRight(contentDivider);
        double d8 = content == null ? 0.0 : (d6 = this.horizontal ? content.minWidth(-1.0) : content.minHeight(-1.0));
        double d9 = content2 == null ? 0.0 : (d5 = this.horizontal ? content2.minWidth(-1.0) : content2.minHeight(-1.0));
        double d10 = content == null ? 0.0 : (content.getContent() != null ? (this.horizontal ? content.getContent().maxWidth(-1.0) : content.getContent().maxHeight(-1.0)) : (d4 = 0.0));
        double d11 = content2 == null ? 0.0 : (content2.getContent() != null ? (this.horizontal ? content2.getContent().maxWidth(-1.0) : content2.getContent().maxHeight(-1.0)) : 0.0);
        double d12 = 0.0;
        double d13 = this.getSize();
        int n2 = this.contentDividers.indexOf(contentDivider);
        if (n2 - 1 >= 0 && (d12 = ((ContentDivider)this.contentDividers.get(n2 - 1)).getDividerPos()) == -1.0) {
            d12 = this.getAbsoluteDividerPos((ContentDivider)this.contentDividers.get(n2 - 1));
        }
        if (n2 + 1 < this.contentDividers.size() && (d13 = ((ContentDivider)this.contentDividers.get(n2 + 1)).getDividerPos()) == -1.0) {
            d13 = this.getAbsoluteDividerPos((ContentDivider)this.contentDividers.get(n2 + 1));
        }
        this.checkDividerPos = false;
        if (d2 > d3) {
            double d14;
            double d15 = d12 == 0.0 ? d4 : d12 + d7 + d4;
            double d16 = Math.min(d15, d14 = d13 - d5 - d7);
            if (d2 >= d16) {
                this.setAbsoluteDividerPos(contentDivider, d16);
            } else {
                double d17 = d13 - d11 - d7;
                if (d2 <= d17) {
                    this.setAbsoluteDividerPos(contentDivider, d17);
                } else {
                    this.setAbsoluteDividerPos(contentDivider, d2);
                }
            }
        } else {
            double d18 = d13 - d11 - d7;
            double d19 = d12 == 0.0 ? d6 : d12 + d6 + d7;
            double d20 = Math.max(d18, d19);
            if (d2 <= d20) {
                this.setAbsoluteDividerPos(contentDivider, d20);
            } else {
                double d21 = d12 + d4 + d7;
                if (d2 >= d21) {
                    this.setAbsoluteDividerPos(contentDivider, d21);
                } else {
                    this.setAbsoluteDividerPos(contentDivider, d2);
                }
            }
        }
        this.checkDividerPos = true;
    }

    private void addDivider(SplitPane.Divider divider) {
        ContentDivider contentDivider = new ContentDivider(divider);
        contentDivider.setInitialPos(divider.getPosition());
        contentDivider.setDividerPos(-1.0);
        PosPropertyListener posPropertyListener = new PosPropertyListener(contentDivider);
        contentDivider.setPosPropertyListener(posPropertyListener);
        divider.positionProperty().addListener(posPropertyListener);
        this.initializeDivderEventHandlers(contentDivider);
        this.contentDividers.add(contentDivider);
        this.getChildren().add(contentDivider);
    }

    private void removeAllDividers() {
        ListIterator listIterator = this.contentDividers.listIterator();
        while (listIterator.hasNext()) {
            ContentDivider contentDivider = (ContentDivider)listIterator.next();
            this.getChildren().remove(contentDivider);
            contentDivider.getDivider().positionProperty().removeListener(contentDivider.getPosPropertyListener());
            listIterator.remove();
        }
        this.lastDividerUpdate = 0;
    }

    private void initializeDivderEventHandlers(ContentDivider contentDivider) {
        contentDivider.addEventHandler(MouseEvent.ANY, mouseEvent -> mouseEvent.consume());
        contentDivider.setOnMousePressed(mouseEvent -> {
            if (this.horizontal) {
                contentDivider.setInitialPos(contentDivider.getDividerPos());
                contentDivider.setPressPos(mouseEvent.getSceneX());
                contentDivider.setPressPos(((SplitPane)this.getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? ((SplitPane)this.getSkinnable()).getWidth() - mouseEvent.getSceneX() : mouseEvent.getSceneX());
            } else {
                contentDivider.setInitialPos(contentDivider.getDividerPos());
                contentDivider.setPressPos(mouseEvent.getSceneY());
            }
            mouseEvent.consume();
        });
        contentDivider.setOnMouseDragged(mouseEvent -> {
            double d2 = 0.0;
            d2 = this.horizontal ? (((SplitPane)this.getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? ((SplitPane)this.getSkinnable()).getWidth() - mouseEvent.getSceneX() : mouseEvent.getSceneX()) : mouseEvent.getSceneY();
            this.setAndCheckAbsoluteDividerPos(contentDivider, Math.ceil(contentDivider.getInitialPos() + (d2 -= contentDivider.getPressPos())));
            mouseEvent.consume();
        });
    }

    private Content getLeft(ContentDivider contentDivider) {
        int n2 = this.contentDividers.indexOf(contentDivider);
        if (n2 != -1) {
            return (Content)this.contentRegions.get(n2);
        }
        return null;
    }

    private Content getRight(ContentDivider contentDivider) {
        int n2 = this.contentDividers.indexOf(contentDivider);
        if (n2 != -1) {
            return (Content)this.contentRegions.get(n2 + 1);
        }
        return null;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ORIENTATION".equals(string)) {
            this.horizontal = ((SplitPane)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL;
            this.previousSize = -1.0;
            for (ContentDivider contentDivider : this.contentDividers) {
                contentDivider.setGrabberStyle(this.horizontal);
            }
            ((SplitPane)this.getSkinnable()).requestLayout();
        } else if ("WIDTH".equals(string) || "HEIGHT".equals(string)) {
            ((SplitPane)this.getSkinnable()).requestLayout();
        }
    }

    private void setAbsoluteDividerPos(ContentDivider contentDivider, double d2) {
        if (((SplitPane)this.getSkinnable()).getWidth() > 0.0 && ((SplitPane)this.getSkinnable()).getHeight() > 0.0 && contentDivider != null) {
            SplitPane.Divider divider = contentDivider.getDivider();
            contentDivider.setDividerPos(d2);
            double d3 = this.getSize();
            if (d3 != 0.0) {
                double d4 = d2 + contentDivider.prefWidth(-1.0) / 2.0;
                divider.setPosition(d4 / d3);
            } else {
                divider.setPosition(0.0);
            }
        }
    }

    private double getAbsoluteDividerPos(ContentDivider contentDivider) {
        if (((SplitPane)this.getSkinnable()).getWidth() > 0.0 && ((SplitPane)this.getSkinnable()).getHeight() > 0.0 && contentDivider != null) {
            SplitPane.Divider divider = contentDivider.getDivider();
            double d2 = this.posToDividerPos(contentDivider, divider.getPosition());
            contentDivider.setDividerPos(d2);
            return d2;
        }
        return 0.0;
    }

    private double posToDividerPos(ContentDivider contentDivider, double d2) {
        double d3 = this.getSize() * d2;
        d3 = d2 == 1.0 ? (d3 -= contentDivider.prefWidth(-1.0)) : (d3 -= contentDivider.prefWidth(-1.0) / 2.0);
        return Math.round(d3);
    }

    private double totalMinSize() {
        double d2 = !this.contentDividers.isEmpty() ? (double)this.contentDividers.size() * ((ContentDivider)this.contentDividers.get(0)).prefWidth(-1.0) : 0.0;
        double d3 = 0.0;
        for (Content content : this.contentRegions) {
            if (this.horizontal) {
                d3 += content.minWidth(-1.0);
                continue;
            }
            d3 += content.minHeight(-1.0);
        }
        return d3 + d2;
    }

    private double getSize() {
        SplitPane splitPane = (SplitPane)this.getSkinnable();
        double d2 = this.totalMinSize();
        if (this.horizontal) {
            if (splitPane.getWidth() > d2) {
                d2 = splitPane.getWidth() - this.snappedLeftInset() - this.snappedRightInset();
            }
        } else if (splitPane.getHeight() > d2) {
            d2 = splitPane.getHeight() - this.snappedTopInset() - this.snappedBottomInset();
        }
        return d2;
    }

    private double distributeTo(List<Content> list, double d2) {
        if (list.isEmpty()) {
            return d2;
        }
        d2 = this.snapSize(d2);
        int n2 = (int)d2 / list.size();
        while (d2 > 0.0 && !list.isEmpty()) {
            Iterator<Content> iterator = list.iterator();
            while (iterator.hasNext()) {
                double d3;
                Content content = iterator.next();
                double d4 = Math.min(this.horizontal ? content.maxWidth(-1.0) : content.maxHeight(-1.0), Double.MAX_VALUE);
                double d5 = d3 = this.horizontal ? content.minWidth(-1.0) : content.minHeight(-1.0);
                if (content.getArea() >= d4) {
                    content.setAvailable(content.getArea() - d3);
                    iterator.remove();
                    continue;
                }
                if ((double)n2 >= d4 - content.getArea()) {
                    d2 -= d4 - content.getArea();
                    content.setArea(d4);
                    content.setAvailable(d4 - d3);
                    iterator.remove();
                } else {
                    content.setArea(content.getArea() + (double)n2);
                    content.setAvailable(content.getArea() - d3);
                    d2 -= (double)n2;
                }
                if ((int)d2 != 0) continue;
                return d2;
            }
            if (list.isEmpty()) {
                return d2;
            }
            n2 = (int)d2 / list.size();
            int n3 = (int)d2 % list.size();
            if (n2 != 0 || n3 == 0) continue;
            n2 = n3;
            n3 = 0;
        }
        return d2;
    }

    private double distributeFrom(double d2, List<Content> list) {
        if (list.isEmpty()) {
            return d2;
        }
        d2 = this.snapSize(d2);
        int n2 = (int)d2 / list.size();
        while (d2 > 0.0 && !list.isEmpty()) {
            Iterator<Content> iterator = list.iterator();
            while (iterator.hasNext()) {
                Content content = iterator.next();
                if ((double)n2 >= content.getAvailable()) {
                    content.setArea(content.getArea() - content.getAvailable());
                    d2 -= content.getAvailable();
                    content.setAvailable(0.0);
                    iterator.remove();
                } else {
                    content.setArea(content.getArea() - (double)n2);
                    content.setAvailable(content.getAvailable() - (double)n2);
                    d2 -= (double)n2;
                }
                if ((int)d2 != 0) continue;
                return d2;
            }
            if (list.isEmpty()) {
                return d2;
            }
            n2 = (int)d2 / list.size();
            int n3 = (int)d2 % list.size();
            if (n2 != 0 || n3 == 0) continue;
            n2 = n3;
            n3 = 0;
        }
        return d2;
    }

    private void setupContentAndDividerForLayout() {
        double d2 = this.contentDividers.isEmpty() ? 0.0 : ((ContentDivider)this.contentDividers.get(0)).prefWidth(-1.0);
        double d3 = 0.0;
        double d4 = 0.0;
        for (Content stackPane : this.contentRegions) {
            if (this.resize && !stackPane.isResizableWithParent()) {
                stackPane.setArea(stackPane.getResizableWithParentArea());
            }
            stackPane.setX(d3);
            stackPane.setY(d4);
            if (this.horizontal) {
                d3 += stackPane.getArea() + d2;
                continue;
            }
            d4 += stackPane.getArea() + d2;
        }
        d3 = 0.0;
        d4 = 0.0;
        this.checkDividerPos = false;
        for (int i2 = 0; i2 < this.contentDividers.size(); ++i2) {
            ContentDivider contentDivider = (ContentDivider)this.contentDividers.get(i2);
            if (this.horizontal) {
                d3 += this.getLeft(contentDivider).getArea() + (i2 == 0 ? 0.0 : d2);
            } else {
                d4 += this.getLeft(contentDivider).getArea() + (i2 == 0 ? 0.0 : d2);
            }
            contentDivider.setX(d3);
            contentDivider.setY(d4);
            this.setAbsoluteDividerPos(contentDivider, this.horizontal ? contentDivider.getX() : contentDivider.getY());
            contentDivider.posExplicit = false;
        }
        this.checkDividerPos = true;
    }

    private void layoutDividersAndContent(double d2, double d3) {
        double d4 = this.snappedLeftInset();
        double d5 = this.snappedTopInset();
        double d6 = this.contentDividers.isEmpty() ? 0.0 : ((ContentDivider)this.contentDividers.get(0)).prefWidth(-1.0);
        for (Content stackPane : this.contentRegions) {
            if (this.horizontal) {
                stackPane.setClipSize(stackPane.getArea(), d3);
                this.layoutInArea(stackPane, stackPane.getX() + d4, stackPane.getY() + d5, stackPane.getArea(), d3, 0.0, HPos.CENTER, VPos.CENTER);
                continue;
            }
            stackPane.setClipSize(d2, stackPane.getArea());
            this.layoutInArea(stackPane, stackPane.getX() + d4, stackPane.getY() + d5, d2, stackPane.getArea(), 0.0, HPos.CENTER, VPos.CENTER);
        }
        for (ContentDivider contentDivider : this.contentDividers) {
            if (this.horizontal) {
                contentDivider.resize(d6, d3);
                this.positionInArea(contentDivider, contentDivider.getX() + d4, contentDivider.getY() + d5, d6, d3, 0.0, HPos.CENTER, VPos.CENTER);
                continue;
            }
            contentDivider.resize(d2, d6);
            this.positionInArea(contentDivider, contentDivider.getX() + d4, contentDivider.getY() + d5, d2, d6, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    protected void layoutChildren(double var1_1, double var3_2, double var5_3, double var7_4) {
        block57: {
            block55: {
                block56: {
                    block54: {
                        var9_5 = (SplitPane)this.getSkinnable();
                        var10_6 = var9_5.getWidth();
                        var12_7 = var9_5.getHeight();
                        if (var9_5.isVisible() == false) return;
                        if (this.horizontal) {
                            if (var10_6 == 0.0) {
                                return;
                            }
                        } else if (var12_7 == 0.0) return;
                        if (this.contentRegions.isEmpty()) {
                            return;
                        }
                        v0 = var14_8 = this.contentDividers.isEmpty() != false ? 0.0 : ((ContentDivider)this.contentDividers.get(0)).prefWidth(-1.0);
                        if (this.contentDividers.size() <= 0 || this.previousSize == -1.0 || this.previousSize == (this.horizontal != false ? var10_6 : var12_7)) break block54;
                        var16_9 = new ArrayList<Content>();
                        for (Content var18_13 : this.contentRegions) {
                            if (!var18_13.isResizableWithParent()) continue;
                            var16_9.add(var18_13);
                        }
                        var17_12 = (this.horizontal != false ? var9_5.getWidth() : var9_5.getHeight()) - this.previousSize;
                        var19_16 = var17_12 > 0.0;
                        if ((var17_12 = Math.abs(var17_12)) == 0.0 || var16_9.isEmpty()) break block55;
                        var20_18 = (int)var17_12 / var16_9.size();
                        var21_20 = (int)var17_12 % var16_9.size();
                        var22_23 = 0;
                        if (var20_18 == 0) {
                            var20_18 = var21_20;
                            var22_23 = var21_20;
                            var21_20 = 0;
                        } else {
                            var22_23 = var20_18 * var16_9.size();
                        }
                        break block56;
                    }
                    this.previousSize = this.horizontal != false ? var10_6 : var12_7;
                    break block57;
                }
                while (var22_23 > 0 && !var16_9.isEmpty()) {
                    if (var19_16) {
                        ++this.lastDividerUpdate;
                    } else {
                        --this.lastDividerUpdate;
                        if (this.lastDividerUpdate < 0) {
                            this.lastDividerUpdate = this.contentRegions.size() - 1;
                        }
                    }
                    if (!(var24_29 = (Content)this.contentRegions.get(var23_26 = this.lastDividerUpdate % this.contentRegions.size())).isResizableWithParent() || !var16_9.contains(var24_29)) continue;
                    var25_31 = var24_29.getArea();
                    if (!var19_16) ** GOTO lbl54
                    v1 = var27_34 = this.horizontal != false ? var24_29.maxWidth(-1.0) : var24_29.maxHeight(-1.0);
                    if (var25_31 + (double)var20_18 <= var27_34) {
                        var25_31 += (double)var20_18;
                    } else {
                        var16_9.remove(var24_29);
                        continue;
lbl54:
                        // 1 sources

                        v2 = var27_34 = this.horizontal != false ? var24_29.minWidth(-1.0) : var24_29.minHeight(-1.0);
                        if (var25_31 - (double)var20_18 >= var27_34) {
                            var25_31 -= (double)var20_18;
                        } else {
                            var16_9.remove(var24_29);
                            continue;
                        }
                    }
                    var24_29.setArea(var25_31);
                    if ((var22_23 -= var20_18) == 0 && var21_20 != 0) {
                        var20_18 = var21_20;
                        var22_23 = var21_20;
                        var21_20 = 0;
                        continue;
                    }
                    if (var22_23 != 0) continue;
                }
                for (Content var24_29 : this.contentRegions) {
                    var24_29.setResizableWithParentArea(var24_29.getArea());
                    var24_29.setAvailable(0.0);
                }
                this.resize = true;
            }
            this.previousSize = this.horizontal != false ? var10_6 : var12_7;
        }
        var16_10 = this.totalMinSize();
        if (!(var16_10 > (this.horizontal != false ? var5_3 : var7_4))) {
        } else {
            var18_14 = 0.0;
            var20_18 = 0;
            while (true) {
                if (var20_18 >= this.contentRegions.size()) {
                    this.setupContentAndDividerForLayout();
                    this.layoutDividersAndContent(var5_3, var7_4);
                    this.resize = false;
                    return;
                }
                var21_21 = (Content)this.contentRegions.get(var20_18);
                var22_24 = this.horizontal != false ? var21_21.minWidth(-1.0) : var21_21.minHeight(-1.0);
                var18_14 = var22_24 / var16_10;
                var21_21.setArea(this.snapSpace(var18_14 * (this.horizontal != false ? var5_3 : var7_4)));
                var21_21.setAvailable(0.0);
                ++var20_18;
            }
        }
        for (var18_15 = 0; var18_15 < 10; ++var18_15) {
            var19_17 = null;
            var20_19 = null;
            for (var21_20 = 0; var21_20 < this.contentRegions.size(); ++var21_20) {
                var22_25 = 0.0;
                if (var21_20 < this.contentDividers.size()) {
                    var20_19 = (ContentDivider)this.contentDividers.get(var21_20);
                    if (ContentDivider.access$100(var20_19)) {
                        this.checkDividerPosition(var20_19, this.posToDividerPos(var20_19, ContentDivider.access$200(var20_19).getPosition()), var20_19.getDividerPos());
                    }
                    if (var21_20 == 0) {
                        var22_25 = this.getAbsoluteDividerPos(var20_19);
                    } else {
                        var24_30 = this.getAbsoluteDividerPos(var19_17) + var14_8;
                        if (this.getAbsoluteDividerPos(var20_19) <= this.getAbsoluteDividerPos(var19_17)) {
                            this.setAndCheckAbsoluteDividerPos(var20_19, var24_30);
                        }
                        var22_25 = this.getAbsoluteDividerPos(var20_19) - var24_30;
                    }
                } else if (var21_20 == this.contentDividers.size()) {
                    var22_25 = (this.horizontal != false ? var5_3 : var7_4) - (var19_17 != null ? this.getAbsoluteDividerPos(var19_17) + var14_8 : 0.0);
                }
                if (!this.resize || ContentDivider.access$100(var20_19)) {
                    ((Content)this.contentRegions.get(var21_20)).setArea(var22_25);
                }
                var19_17 = var20_19;
            }
            var21_22 = 0.0;
            var23_28 = 0.0;
            for (Content var26_39 : this.contentRegions) {
                var27_35 = 0.0;
                var29_41 = 0.0;
                if (var26_39 != null) {
                    var27_35 = this.horizontal != false ? var26_39.maxWidth(-1.0) : var26_39.maxHeight(-1.0);
                    v3 = var29_41 = this.horizontal != false ? var26_39.minWidth(-1.0) : var26_39.minHeight(-1.0);
                }
                if (var26_39.getArea() >= var27_35) {
                    var23_28 += var26_39.getArea() - var27_35;
                    var26_39.setArea(var27_35);
                }
                var26_39.setAvailable(var26_39.getArea() - var29_41);
                if (!(var26_39.getAvailable() < 0.0)) continue;
                var21_22 += var26_39.getAvailable();
            }
            var21_22 = Math.abs(var21_22);
            var25_33 = new ArrayList<E>();
            var26_37 = new ArrayList<Content>();
            var27_36 = new ArrayList<Content>();
            var28_40 = 0.0;
            for (Content var31_47 : this.contentRegions) {
                if (var31_47.getAvailable() >= 0.0) {
                    var28_40 += var31_47.getAvailable();
                    var25_33.add(var31_47);
                }
                if (this.resize && !var31_47.isResizableWithParent()) {
                    if (var31_47.getArea() >= var31_47.getResizableWithParentArea()) {
                        var23_28 += var31_47.getArea() - var31_47.getResizableWithParentArea();
                    } else {
                        var21_22 += var31_47.getResizableWithParentArea() - var31_47.getArea();
                    }
                    var31_47.setAvailable(0.0);
                }
                if (this.resize) {
                    if (var31_47.isResizableWithParent()) {
                        var26_37.add(var31_47);
                    }
                } else {
                    var26_37.add(var31_47);
                }
                if (!(var31_47.getAvailable() < 0.0)) continue;
                var27_36.add(var31_47);
            }
            if (var23_28 > 0.0) {
                var23_28 = this.distributeTo(var26_37, var23_28);
                var21_22 = 0.0;
                var27_36.clear();
                var28_40 = 0.0;
                var25_33.clear();
                for (Content var31_49 : this.contentRegions) {
                    if (var31_49.getAvailable() < 0.0) {
                        var21_22 += var31_49.getAvailable();
                        var27_36.add(var31_49);
                        continue;
                    }
                    var28_40 += var31_49.getAvailable();
                    var25_33.add(var31_49);
                }
                var21_22 = Math.abs(var21_22);
            }
            if (var28_40 >= var21_22) {
                for (Content var31_51 : var27_36) {
                    var32_52 = this.horizontal != false ? var31_51.minWidth(-1.0) : var31_51.minHeight(-1.0);
                    var31_51.setArea(var32_52);
                    var31_51.setAvailable(0.0);
                }
                if (var21_22 > 0.0 && !var27_36.isEmpty()) {
                    this.distributeFrom(var21_22, (List<Content>)var25_33);
                }
                if (this.resize) {
                    var30_44 = 0.0;
                    for (Content var33_55 : this.contentRegions) {
                        if (var33_55.isResizableWithParent()) {
                            var30_44 += var33_55.getArea();
                            continue;
                        }
                        var30_44 += var33_55.getResizableWithParentArea();
                    }
                    var30_44 += var14_8 * (double)this.contentDividers.size();
                    v4 = this.horizontal != false ? var5_3 : var7_4;
                    if (var30_44 < v4) {
                        this.distributeTo(var26_37, var23_28 += (this.horizontal != false ? var5_3 : var7_4) - var30_44);
                    } else {
                        this.distributeFrom(var21_22 += var30_44 - (this.horizontal != false ? var5_3 : var7_4), var26_37);
                    }
                }
            }
            this.setupContentAndDividerForLayout();
            var30_42 = true;
            for (Content var32_54 : this.contentRegions) {
                var33_56 = this.horizontal != false ? var32_54.maxWidth(-1.0) : var32_54.maxHeight(-1.0);
                v5 = var35_57 = this.horizontal != false ? var32_54.minWidth(-1.0) : var32_54.minHeight(-1.0);
                if (!(var32_54.getArea() < var35_57) && !(var32_54.getArea() > var33_56)) continue;
                var30_42 = false;
                break;
            }
            if (var30_42) break;
        }
        this.layoutDividersAndContent(var5_3, var7_4);
        this.resize = false;
    }

    private void setAndCheckAbsoluteDividerPos(ContentDivider contentDivider, double d2) {
        double d3 = contentDivider.getDividerPos();
        this.setAbsoluteDividerPos(contentDivider, d2);
        this.checkDividerPosition(contentDivider, d2, d3);
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        double d8 = 0.0;
        for (Content stackPane : this.contentRegions) {
            d7 += stackPane.minWidth(-1.0);
            d8 = Math.max(d8, stackPane.minWidth(-1.0));
        }
        for (ContentDivider contentDivider : this.contentDividers) {
            d7 += contentDivider.prefWidth(-1.0);
        }
        if (this.horizontal) {
            return d7 + d6 + d4;
        }
        return d8 + d6 + d4;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        double d8 = 0.0;
        for (Content stackPane : this.contentRegions) {
            d7 += stackPane.minHeight(-1.0);
            d8 = Math.max(d8, stackPane.minHeight(-1.0));
        }
        for (ContentDivider contentDivider : this.contentDividers) {
            d7 += contentDivider.prefWidth(-1.0);
        }
        if (this.horizontal) {
            return d8 + d3 + d5;
        }
        return d7 + d3 + d5;
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        double d8 = 0.0;
        for (Content stackPane : this.contentRegions) {
            d7 += stackPane.prefWidth(-1.0);
            d8 = Math.max(d8, stackPane.prefWidth(-1.0));
        }
        for (ContentDivider contentDivider : this.contentDividers) {
            d7 += contentDivider.prefWidth(-1.0);
        }
        if (this.horizontal) {
            return d7 + d6 + d4;
        }
        return d8 + d6 + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        double d8 = 0.0;
        for (Content stackPane : this.contentRegions) {
            d7 += stackPane.prefHeight(-1.0);
            d8 = Math.max(d8, stackPane.prefHeight(-1.0));
        }
        for (ContentDivider contentDivider : this.contentDividers) {
            d7 += contentDivider.prefWidth(-1.0);
        }
        if (this.horizontal) {
            return d8 + d3 + d5;
        }
        return d7 + d3 + d5;
    }

    static class Content
    extends StackPane {
        private Node content;
        private Rectangle clipRect = new Rectangle();
        private double x;
        private double y;
        private double area;
        private double resizableWithParentArea;
        private double available;

        public Content(Node node) {
            this.setClip(this.clipRect);
            this.content = node;
            if (node != null) {
                this.getChildren().add(node);
            }
            this.x = 0.0;
            this.y = 0.0;
        }

        public Node getContent() {
            return this.content;
        }

        public double getX() {
            return this.x;
        }

        public void setX(double d2) {
            this.x = d2;
        }

        public double getY() {
            return this.y;
        }

        public void setY(double d2) {
            this.y = d2;
        }

        public double getArea() {
            return this.area;
        }

        public void setArea(double d2) {
            this.area = d2;
        }

        public double getAvailable() {
            return this.available;
        }

        public void setAvailable(double d2) {
            this.available = d2;
        }

        public boolean isResizableWithParent() {
            return SplitPane.isResizableWithParent(this.content);
        }

        public double getResizableWithParentArea() {
            return this.resizableWithParentArea;
        }

        public void setResizableWithParentArea(double d2) {
            this.resizableWithParentArea = !this.isResizableWithParent() ? d2 : 0.0;
        }

        protected void setClipSize(double d2, double d3) {
            this.clipRect.setWidth(d2);
            this.clipRect.setHeight(d3);
        }

        @Override
        protected double computeMaxWidth(double d2) {
            return this.snapSize(this.content.maxWidth(d2));
        }

        @Override
        protected double computeMaxHeight(double d2) {
            return this.snapSize(this.content.maxHeight(d2));
        }
    }

    class ContentDivider
    extends StackPane {
        private double initialPos;
        private double dividerPos;
        private double pressPos;
        private SplitPane.Divider d;
        private StackPane grabber;
        private double x;
        private double y;
        private boolean posExplicit;
        private ChangeListener<Number> listener;

        public ContentDivider(SplitPane.Divider divider) {
            this.getStyleClass().setAll("split-pane-divider");
            this.d = divider;
            this.initialPos = 0.0;
            this.dividerPos = 0.0;
            this.pressPos = 0.0;
            this.grabber = new StackPane(){

                @Override
                protected double computeMinWidth(double d2) {
                    return 0.0;
                }

                @Override
                protected double computeMinHeight(double d2) {
                    return 0.0;
                }

                @Override
                protected double computePrefWidth(double d2) {
                    return this.snappedLeftInset() + this.snappedRightInset();
                }

                @Override
                protected double computePrefHeight(double d2) {
                    return this.snappedTopInset() + this.snappedBottomInset();
                }

                @Override
                protected double computeMaxWidth(double d2) {
                    return this.computePrefWidth(-1.0);
                }

                @Override
                protected double computeMaxHeight(double d2) {
                    return this.computePrefHeight(-1.0);
                }
            };
            this.setGrabberStyle(SplitPaneSkin.this.horizontal);
            this.getChildren().add(this.grabber);
        }

        public SplitPane.Divider getDivider() {
            return this.d;
        }

        public final void setGrabberStyle(boolean bl) {
            this.grabber.getStyleClass().clear();
            this.grabber.getStyleClass().setAll("vertical-grabber");
            this.setCursor(Cursor.V_RESIZE);
            if (bl) {
                this.grabber.getStyleClass().setAll("horizontal-grabber");
                this.setCursor(Cursor.H_RESIZE);
            }
        }

        public double getInitialPos() {
            return this.initialPos;
        }

        public void setInitialPos(double d2) {
            this.initialPos = d2;
        }

        public double getDividerPos() {
            return this.dividerPos;
        }

        public void setDividerPos(double d2) {
            this.dividerPos = d2;
        }

        public double getPressPos() {
            return this.pressPos;
        }

        public void setPressPos(double d2) {
            this.pressPos = d2;
        }

        public double getX() {
            return this.x;
        }

        public void setX(double d2) {
            this.x = d2;
        }

        public double getY() {
            return this.y;
        }

        public void setY(double d2) {
            this.y = d2;
        }

        public ChangeListener<Number> getPosPropertyListener() {
            return this.listener;
        }

        public void setPosPropertyListener(ChangeListener<Number> changeListener) {
            this.listener = changeListener;
        }

        @Override
        protected double computeMinWidth(double d2) {
            return this.computePrefWidth(d2);
        }

        @Override
        protected double computeMinHeight(double d2) {
            return this.computePrefHeight(d2);
        }

        @Override
        protected double computePrefWidth(double d2) {
            return this.snappedLeftInset() + this.snappedRightInset();
        }

        @Override
        protected double computePrefHeight(double d2) {
            return this.snappedTopInset() + this.snappedBottomInset();
        }

        @Override
        protected double computeMaxWidth(double d2) {
            return this.computePrefWidth(d2);
        }

        @Override
        protected double computeMaxHeight(double d2) {
            return this.computePrefHeight(d2);
        }

        @Override
        protected void layoutChildren() {
            double d2 = this.grabber.prefWidth(-1.0);
            double d3 = this.grabber.prefHeight(-1.0);
            double d4 = (this.getWidth() - d2) / 2.0;
            double d5 = (this.getHeight() - d3) / 2.0;
            this.grabber.resize(d2, d3);
            this.positionInArea(this.grabber, d4, d5, d2, d3, 0.0, HPos.CENTER, VPos.CENTER);
        }

        static /* synthetic */ boolean access$100(ContentDivider contentDivider) {
            return contentDivider.posExplicit;
        }

        static /* synthetic */ SplitPane.Divider access$200(ContentDivider contentDivider) {
            return contentDivider.d;
        }
    }

    class PosPropertyListener
    implements ChangeListener<Number> {
        ContentDivider divider;

        public PosPropertyListener(ContentDivider contentDivider) {
            this.divider = contentDivider;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            if (SplitPaneSkin.this.checkDividerPos) {
                this.divider.posExplicit = true;
            }
            ((SplitPane)SplitPaneSkin.this.getSkinnable()).requestLayout();
        }
    }
}

