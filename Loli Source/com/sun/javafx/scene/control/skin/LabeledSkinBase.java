/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.TextBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.LabeledText;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Skinnable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public abstract class LabeledSkinBase<C extends Labeled, B extends BehaviorBase<C>>
extends BehaviorSkinBase<C, B> {
    LabeledText text;
    boolean invalidText = true;
    Node graphic;
    double textWidth = Double.NEGATIVE_INFINITY;
    double ellipsisWidth = Double.NEGATIVE_INFINITY;
    final InvalidationListener graphicPropertyChangedListener = observable -> {
        this.invalidText = true;
        if (this.getSkinnable() != null) {
            ((Labeled)this.getSkinnable()).requestLayout();
        }
    };
    private Rectangle textClip;
    private double wrapWidth;
    private double wrapHeight;
    public TextBinding bindings;
    Line mnemonic_underscore;
    private boolean containsMnemonic = false;
    private Scene mnemonicScene = null;
    private KeyCombination mnemonicCode;
    private Node labeledNode = null;

    public LabeledSkinBase(C c2, B b2) {
        super(c2, b2);
        this.text = new LabeledText((Labeled)c2);
        this.updateChildren();
        this.registerChangeListener(((Labeled)c2).ellipsisStringProperty(), "ELLIPSIS_STRING");
        this.registerChangeListener(((Region)c2).widthProperty(), "WIDTH");
        this.registerChangeListener(((Region)c2).heightProperty(), "HEIGHT");
        this.registerChangeListener(((Labeled)c2).textFillProperty(), "TEXT_FILL");
        this.registerChangeListener(((Labeled)c2).fontProperty(), "FONT");
        this.registerChangeListener(((Labeled)c2).graphicProperty(), "GRAPHIC");
        this.registerChangeListener(((Labeled)c2).contentDisplayProperty(), "CONTENT_DISPLAY");
        this.registerChangeListener(((Labeled)c2).labelPaddingProperty(), "LABEL_PADDING");
        this.registerChangeListener(((Labeled)c2).graphicTextGapProperty(), "GRAPHIC_TEXT_GAP");
        this.registerChangeListener(((Labeled)c2).alignmentProperty(), "ALIGNMENT");
        this.registerChangeListener(((Labeled)c2).mnemonicParsingProperty(), "MNEMONIC_PARSING");
        this.registerChangeListener(((Labeled)c2).textProperty(), "TEXT");
        this.registerChangeListener(((Labeled)c2).textAlignmentProperty(), "TEXT_ALIGNMENT");
        this.registerChangeListener(((Labeled)c2).textOverrunProperty(), "TEXT_OVERRUN");
        this.registerChangeListener(((Labeled)c2).wrapTextProperty(), "WRAP_TEXT");
        this.registerChangeListener(((Labeled)c2).underlineProperty(), "UNDERLINE");
        this.registerChangeListener(((Labeled)c2).lineSpacingProperty(), "LINE_SPACING");
        this.registerChangeListener(((Node)c2).sceneProperty(), "SCENE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("WIDTH".equals(string)) {
            this.updateWrappingWidth();
            this.invalidText = true;
        } else if ("HEIGHT".equals(string)) {
            this.invalidText = true;
        } else if ("FONT".equals(string)) {
            this.textMetricsChanged();
            this.invalidateWidths();
            this.ellipsisWidth = Double.NEGATIVE_INFINITY;
        } else if ("GRAPHIC".equals(string)) {
            this.updateChildren();
            this.textMetricsChanged();
        } else if ("CONTENT_DISPLAY".equals(string)) {
            this.updateChildren();
            this.textMetricsChanged();
        } else if ("LABEL_PADDING".equals(string)) {
            this.textMetricsChanged();
        } else if ("GRAPHIC_TEXT_GAP".equals(string)) {
            this.textMetricsChanged();
        } else if ("ALIGNMENT".equals(string)) {
            ((Labeled)this.getSkinnable()).requestLayout();
        } else if ("MNEMONIC_PARSING".equals(string)) {
            this.containsMnemonic = false;
            this.textMetricsChanged();
        } else if ("TEXT".equals(string)) {
            this.updateChildren();
            this.textMetricsChanged();
            this.invalidateWidths();
        } else if (!"TEXT_ALIGNMENT".equals(string)) {
            if ("TEXT_OVERRUN".equals(string)) {
                this.textMetricsChanged();
            } else if ("ELLIPSIS_STRING".equals(string)) {
                this.textMetricsChanged();
                this.invalidateWidths();
                this.ellipsisWidth = Double.NEGATIVE_INFINITY;
            } else if ("WRAP_TEXT".equals(string)) {
                this.updateWrappingWidth();
                this.textMetricsChanged();
            } else if ("UNDERLINE".equals(string)) {
                this.textMetricsChanged();
            } else if ("LINE_SPACING".equals(string)) {
                this.textMetricsChanged();
            } else if ("SCENE".equals(string)) {
                this.sceneChanged();
            }
        }
    }

    protected double topLabelPadding() {
        return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getTop());
    }

    protected double bottomLabelPadding() {
        return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getBottom());
    }

    protected double leftLabelPadding() {
        return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getLeft());
    }

    protected double rightLabelPadding() {
        return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getRight());
    }

    private void textMetricsChanged() {
        this.invalidText = true;
        ((Labeled)this.getSkinnable()).requestLayout();
    }

    protected void mnemonicTargetChanged() {
        if (this.containsMnemonic) {
            this.removeMnemonic();
            Skinnable skinnable = this.getSkinnable();
            if (skinnable instanceof Label) {
                this.labeledNode = ((Label)skinnable).getLabelFor();
                this.addMnemonic();
            } else {
                this.labeledNode = null;
            }
        }
    }

    private void sceneChanged() {
        Labeled labeled = (Labeled)this.getSkinnable();
        Scene scene = labeled.getScene();
        if (scene != null && this.containsMnemonic) {
            this.addMnemonic();
        }
    }

    private void invalidateWidths() {
        this.textWidth = Double.NEGATIVE_INFINITY;
    }

    void updateDisplayedText() {
        this.updateDisplayedText(-1.0, -1.0);
    }

    private void updateDisplayedText(double d2, double d3) {
        if (this.invalidText) {
            String string;
            int n2;
            Labeled labeled = (Labeled)this.getSkinnable();
            String string2 = labeled.getText();
            int n3 = -1;
            if (string2 != null && string2.length() > 0) {
                this.bindings = new TextBinding(string2);
                if (!PlatformUtil.isMac() && ((Labeled)this.getSkinnable()).isMnemonicParsing()) {
                    this.labeledNode = labeled instanceof Label ? ((Label)labeled).getLabelFor() : labeled;
                    if (this.labeledNode == null) {
                        this.labeledNode = labeled;
                    }
                    n3 = this.bindings.getMnemonicIndex();
                }
            }
            if (this.containsMnemonic) {
                if (this.mnemonicScene != null && (n3 == -1 || this.bindings != null && !this.bindings.getMnemonicKeyCombination().equals(this.mnemonicCode))) {
                    this.removeMnemonic();
                    this.containsMnemonic = false;
                }
            } else {
                this.removeMnemonic();
            }
            if (string2 != null && string2.length() > 0 && n3 >= 0 && !this.containsMnemonic) {
                this.containsMnemonic = true;
                this.mnemonicCode = this.bindings.getMnemonicKeyCombination();
                this.addMnemonic();
            }
            if (this.containsMnemonic) {
                string2 = this.bindings.getText();
                if (this.mnemonic_underscore == null) {
                    this.mnemonic_underscore = new Line();
                    this.mnemonic_underscore.setStartX(0.0);
                    this.mnemonic_underscore.setStartY(0.0);
                    this.mnemonic_underscore.setEndY(0.0);
                    this.mnemonic_underscore.getStyleClass().clear();
                    this.mnemonic_underscore.getStyleClass().setAll("mnemonic-underline");
                }
                if (!this.getChildren().contains(this.mnemonic_underscore)) {
                    this.getChildren().add(this.mnemonic_underscore);
                }
            } else {
                string2 = ((Labeled)this.getSkinnable()).isMnemonicParsing() && PlatformUtil.isMac() && this.bindings != null ? this.bindings.getText() : labeled.getText();
                if (this.mnemonic_underscore != null && this.getChildren().contains(this.mnemonic_underscore)) {
                    Platform.runLater(() -> {
                        this.getChildren().remove(this.mnemonic_underscore);
                        this.mnemonic_underscore = null;
                    });
                }
            }
            int n4 = string2 != null ? string2.length() : 0;
            boolean bl = false;
            if (string2 != null && n4 > 0 && (n2 = string2.indexOf(10)) > -1 && n2 < n4 - 1) {
                bl = true;
            }
            boolean bl2 = labeled.getContentDisplay() == ContentDisplay.LEFT || labeled.getContentDisplay() == ContentDisplay.RIGHT;
            double d4 = labeled.getWidth() - this.snappedLeftInset() - this.leftLabelPadding() - this.snappedRightInset() - this.rightLabelPadding();
            d4 = Math.max(d4, 0.0);
            if (d2 == -1.0) {
                d2 = d4;
            }
            double d5 = Math.min(this.computeMinLabeledPartWidth(-1.0, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset()), d4);
            if (bl2 && !this.isIgnoreGraphic()) {
                double d6 = labeled.getGraphic().getLayoutBounds().getWidth() + labeled.getGraphicTextGap();
                d2 -= d6;
                d5 -= d6;
            }
            this.wrapWidth = Math.max(d5, d2);
            boolean bl3 = labeled.getContentDisplay() == ContentDisplay.TOP || labeled.getContentDisplay() == ContentDisplay.BOTTOM;
            double d7 = labeled.getHeight() - this.snappedTopInset() - this.topLabelPadding() - this.snappedBottomInset() - this.bottomLabelPadding();
            d7 = Math.max(d7, 0.0);
            if (d3 == -1.0) {
                d3 = d7;
            }
            double d8 = Math.min(this.computeMinLabeledPartHeight(this.wrapWidth, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset()), d7);
            if (bl3 && labeled.getGraphic() != null) {
                double d9 = labeled.getGraphic().getLayoutBounds().getHeight() + labeled.getGraphicTextGap();
                d3 -= d9;
                d8 -= d9;
            }
            this.wrapHeight = Math.max(d8, d3);
            this.updateWrappingWidth();
            Font font = this.text.getFont();
            OverrunStyle overrunStyle = labeled.getTextOverrun();
            String string3 = labeled.getEllipsisString();
            if (labeled.isWrapText()) {
                string = Utils.computeClippedWrappedText(font, string2, this.wrapWidth, this.wrapHeight, overrunStyle, string3, this.text.getBoundsType());
            } else if (bl) {
                StringBuilder stringBuilder = new StringBuilder();
                String[] arrstring = string2.split("\n");
                for (int i2 = 0; i2 < arrstring.length; ++i2) {
                    stringBuilder.append(Utils.computeClippedText(font, arrstring[i2], this.wrapWidth, overrunStyle, string3));
                    if (i2 >= arrstring.length - 1) continue;
                    stringBuilder.append('\n');
                }
                string = stringBuilder.toString();
            } else {
                string = Utils.computeClippedText(font, string2, this.wrapWidth, overrunStyle, string3);
            }
            if (string != null && string.endsWith("\n")) {
                string = string.substring(0, string.length() - 1);
            }
            this.text.setText(string);
            this.updateWrappingWidth();
            this.invalidText = false;
        }
    }

    private void addMnemonic() {
        if (this.labeledNode != null) {
            this.mnemonicScene = this.labeledNode.getScene();
            if (this.mnemonicScene != null) {
                this.mnemonicScene.addMnemonic(new Mnemonic(this.labeledNode, this.mnemonicCode));
            }
        }
    }

    private void removeMnemonic() {
        if (this.mnemonicScene != null && this.labeledNode != null) {
            this.mnemonicScene.removeMnemonic(new Mnemonic(this.labeledNode, this.mnemonicCode));
            this.mnemonicScene = null;
        }
    }

    private void updateWrappingWidth() {
        Labeled labeled = (Labeled)this.getSkinnable();
        this.text.setWrappingWidth(0.0);
        if (labeled.isWrapText()) {
            double d2 = Math.min(this.text.prefWidth(-1.0), this.wrapWidth);
            this.text.setWrappingWidth(d2);
        }
    }

    protected void updateChildren() {
        Labeled labeled = (Labeled)this.getSkinnable();
        if (this.graphic != null) {
            this.graphic.layoutBoundsProperty().removeListener(this.graphicPropertyChangedListener);
        }
        this.graphic = labeled.getGraphic();
        if (this.graphic instanceof ImageView) {
            this.graphic.setMouseTransparent(true);
        }
        if (this.isIgnoreGraphic()) {
            if (labeled.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY) {
                this.getChildren().clear();
            } else {
                this.getChildren().setAll(this.text);
            }
        } else {
            this.graphic.layoutBoundsProperty().addListener(this.graphicPropertyChangedListener);
            if (this.isIgnoreText()) {
                this.getChildren().setAll(this.graphic);
            } else {
                this.getChildren().setAll(this.graphic, this.text);
            }
            this.graphic.impl_processCSS(false);
        }
    }

    protected boolean isIgnoreGraphic() {
        return this.graphic == null || !this.graphic.isManaged() || ((Labeled)this.getSkinnable()).getContentDisplay() == ContentDisplay.TEXT_ONLY;
    }

    protected boolean isIgnoreText() {
        Labeled labeled = (Labeled)this.getSkinnable();
        String string = labeled.getText();
        return string == null || string.equals("") || labeled.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY;
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        return this.computeMinLabeledPartWidth(d2, d3, d4, d5, d6);
    }

    private double computeMinLabeledPartWidth(double d2, double d3, double d4, double d5, double d6) {
        boolean bl;
        Labeled labeled = (Labeled)this.getSkinnable();
        ContentDisplay contentDisplay = labeled.getContentDisplay();
        double d7 = labeled.getGraphicTextGap();
        double d8 = 0.0;
        Font font = this.text.getFont();
        OverrunStyle overrunStyle = labeled.getTextOverrun();
        String string = labeled.getEllipsisString();
        String string2 = labeled.getText();
        boolean bl2 = bl = string2 == null || string2.isEmpty();
        if (!bl) {
            if (overrunStyle == OverrunStyle.CLIP) {
                if (this.textWidth == Double.NEGATIVE_INFINITY) {
                    this.textWidth = Utils.computeTextWidth(font, string2.substring(0, 1), 0.0);
                }
                d8 = this.textWidth;
            } else {
                if (this.textWidth == Double.NEGATIVE_INFINITY) {
                    this.textWidth = Utils.computeTextWidth(font, string2, 0.0);
                }
                if (this.ellipsisWidth == Double.NEGATIVE_INFINITY) {
                    this.ellipsisWidth = Utils.computeTextWidth(font, string, 0.0);
                }
                d8 = Math.min(this.textWidth, this.ellipsisWidth);
            }
        }
        Node node = labeled.getGraphic();
        double d9 = this.isIgnoreGraphic() ? d8 : (this.isIgnoreText() ? node.minWidth(-1.0) : (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT ? d8 + node.minWidth(-1.0) + d7 : Math.max(d8, node.minWidth(-1.0))));
        return d9 + d6 + this.leftLabelPadding() + d4 + this.rightLabelPadding();
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        return this.computeMinLabeledPartHeight(d2, d3, d4, d5, d6);
    }

    private double computeMinLabeledPartHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7;
        int n2;
        Labeled labeled = (Labeled)this.getSkinnable();
        Font font = this.text.getFont();
        String string = labeled.getText();
        if (string != null && string.length() > 0 && (n2 = string.indexOf(10)) >= 0) {
            string = string.substring(0, n2);
        }
        double d8 = labeled.getLineSpacing();
        double d9 = d7 = Utils.computeTextHeight(font, string, 0.0, d8, this.text.getBoundsType());
        if (!this.isIgnoreGraphic()) {
            Node node = labeled.getGraphic();
            d9 = labeled.getContentDisplay() == ContentDisplay.TOP || labeled.getContentDisplay() == ContentDisplay.BOTTOM ? node.minHeight(d2) + labeled.getGraphicTextGap() + d7 : Math.max(d7, node.minHeight(d2));
        }
        return d3 + d9 + d5 + this.topLabelPadding() - this.bottomLabelPadding();
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        Labeled labeled = (Labeled)this.getSkinnable();
        Font font = this.text.getFont();
        String string = labeled.getText();
        boolean bl = string == null || string.isEmpty();
        double d7 = d6 + this.leftLabelPadding() + d4 + this.rightLabelPadding();
        double d8 = bl ? 0.0 : Utils.computeTextWidth(font, string, 0.0);
        double d9 = this.graphic == null ? 0.0 : Utils.boundedSize(this.graphic.prefWidth(-1.0), this.graphic.minWidth(-1.0), this.graphic.maxWidth(-1.0));
        Node node = labeled.getGraphic();
        if (this.isIgnoreGraphic()) {
            return d8 + d7;
        }
        if (this.isIgnoreText()) {
            return d9 + d7;
        }
        if (labeled.getContentDisplay() == ContentDisplay.LEFT || labeled.getContentDisplay() == ContentDisplay.RIGHT) {
            return d8 + labeled.getGraphicTextGap() + d9 + d7;
        }
        return Math.max(d8, d9) + d7;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7;
        Labeled labeled = (Labeled)this.getSkinnable();
        Font font = this.text.getFont();
        ContentDisplay contentDisplay = labeled.getContentDisplay();
        double d8 = labeled.getGraphicTextGap();
        d2 -= d6 + this.leftLabelPadding() + d4 + this.rightLabelPadding();
        String string = labeled.getText();
        if (string != null && string.endsWith("\n")) {
            string = string.substring(0, string.length() - 1);
        }
        double d9 = d2;
        if (!(this.isIgnoreGraphic() || contentDisplay != ContentDisplay.LEFT && contentDisplay != ContentDisplay.RIGHT)) {
            d9 -= this.graphic.prefWidth(-1.0) + d8;
        }
        double d10 = d7 = Utils.computeTextHeight(font, string, labeled.isWrapText() ? d9 : 0.0, labeled.getLineSpacing(), this.text.getBoundsType());
        if (!this.isIgnoreGraphic()) {
            Node node = labeled.getGraphic();
            d10 = contentDisplay == ContentDisplay.TOP || contentDisplay == ContentDisplay.BOTTOM ? node.prefHeight(d2) + d8 + d7 : Math.max(d7, node.prefHeight(d2));
        }
        return d3 + d10 + d5 + this.topLabelPadding() + this.bottomLabelPadding();
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        return ((Labeled)this.getSkinnable()).prefWidth(d2);
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        return ((Labeled)this.getSkinnable()).prefHeight(d2);
    }

    @Override
    public double computeBaselineOffset(double d2, double d3, double d4, double d5) {
        double d6;
        double d7 = d6 = this.text.getBaselineOffset();
        Labeled labeled = (Labeled)this.getSkinnable();
        Node node = labeled.getGraphic();
        if (!this.isIgnoreGraphic()) {
            ContentDisplay contentDisplay = labeled.getContentDisplay();
            if (contentDisplay == ContentDisplay.TOP) {
                d7 = node.prefHeight(-1.0) + labeled.getGraphicTextGap() + d6;
            } else if (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT) {
                d7 = d6 + (node.prefHeight(-1.0) - this.text.prefHeight(-1.0)) / 2.0;
            }
        }
        return d2 + this.topLabelPadding() + d7;
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        this.layoutLabelInArea(d2, d3, d4, d5);
    }

    protected void layoutLabelInArea(double d2, double d3, double d4, double d5) {
        this.layoutLabelInArea(d2, d3, d4, d5, null);
    }

    protected void layoutLabelInArea(double d2, double d3, double d4, double d5, Pos pos) {
        double d6;
        double d7;
        double d8;
        double d9;
        Labeled labeled = (Labeled)this.getSkinnable();
        ContentDisplay contentDisplay = labeled.getContentDisplay();
        if (pos == null) {
            pos = labeled.getAlignment();
        }
        HPos hPos = pos == null ? HPos.LEFT : pos.getHpos();
        VPos vPos = pos == null ? VPos.CENTER : pos.getVpos();
        boolean bl = this.isIgnoreGraphic();
        boolean bl2 = this.isIgnoreText();
        d2 += this.leftLabelPadding();
        d3 += this.topLabelPadding();
        d4 -= this.leftLabelPadding() + this.rightLabelPadding();
        d5 -= this.topLabelPadding() + this.bottomLabelPadding();
        if (bl) {
            d9 = 0.0;
            d8 = 0.0;
        } else if (bl2) {
            if (this.graphic.isResizable()) {
                Orientation orientation = this.graphic.getContentBias();
                if (orientation == Orientation.HORIZONTAL) {
                    d8 = Utils.boundedSize(d4, this.graphic.minWidth(-1.0), this.graphic.maxWidth(-1.0));
                    d9 = Utils.boundedSize(d5, this.graphic.minHeight(d8), this.graphic.maxHeight(d8));
                } else if (orientation == Orientation.VERTICAL) {
                    d9 = Utils.boundedSize(d5, this.graphic.minHeight(-1.0), this.graphic.maxHeight(-1.0));
                    d8 = Utils.boundedSize(d4, this.graphic.minWidth(d9), this.graphic.maxWidth(d9));
                } else {
                    d8 = Utils.boundedSize(d4, this.graphic.minWidth(-1.0), this.graphic.maxWidth(-1.0));
                    d9 = Utils.boundedSize(d5, this.graphic.minHeight(-1.0), this.graphic.maxHeight(-1.0));
                }
                this.graphic.resize(d8, d9);
            } else {
                d8 = this.graphic.getLayoutBounds().getWidth();
                d9 = this.graphic.getLayoutBounds().getHeight();
            }
        } else {
            this.graphic.autosize();
            d8 = this.graphic.getLayoutBounds().getWidth();
            d9 = this.graphic.getLayoutBounds().getHeight();
        }
        if (bl2) {
            d7 = 0.0;
            d6 = 0.0;
            this.text.setText("");
        } else {
            this.updateDisplayedText(d4, d5);
            d6 = this.snapSize(Math.min(this.text.getLayoutBounds().getWidth(), this.wrapWidth));
            d7 = this.snapSize(Math.min(this.text.getLayoutBounds().getHeight(), this.wrapHeight));
        }
        double d10 = bl2 || bl ? 0.0 : labeled.getGraphicTextGap();
        double d11 = Math.max(d8, d6);
        double d12 = Math.max(d9, d7);
        if (contentDisplay == ContentDisplay.TOP || contentDisplay == ContentDisplay.BOTTOM) {
            d12 = d9 + d10 + d7;
        } else if (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT) {
            d11 = d8 + d10 + d6;
        }
        double d13 = hPos == HPos.LEFT ? d2 : (hPos == HPos.RIGHT ? d2 + (d4 - d11) : d2 + (d4 - d11) / 2.0);
        double d14 = vPos == VPos.TOP ? d3 : (vPos == VPos.BOTTOM ? d3 + (d5 - d12) : d3 + (d5 - d12) / 2.0);
        double d15 = 0.0;
        double d16 = 0.0;
        double d17 = 0.0;
        if (this.containsMnemonic) {
            Font font = this.text.getFont();
            String string = this.bindings.getText();
            d15 = Utils.computeTextWidth(font, string.substring(0, this.bindings.getMnemonicIndex()), 0.0);
            d16 = Utils.computeTextWidth(font, string.substring(this.bindings.getMnemonicIndex(), this.bindings.getMnemonicIndex() + 1), 0.0);
            d17 = Utils.computeTextHeight(font, "_", 0.0, this.text.getBoundsType());
        }
        if (!(bl && bl2 || this.text.isManaged())) {
            this.text.setManaged(true);
        }
        if (bl && bl2) {
            if (this.text.isManaged()) {
                this.text.setManaged(false);
            }
            this.text.relocate(this.snapPosition(d13), this.snapPosition(d14));
        } else if (bl) {
            this.text.relocate(this.snapPosition(d13), this.snapPosition(d14));
            if (this.containsMnemonic) {
                this.mnemonic_underscore.setEndX(d16 - 2.0);
                this.mnemonic_underscore.relocate(d13 + d15, d14 + d17 - 1.0);
            }
        } else if (bl2) {
            this.text.relocate(this.snapPosition(d13), this.snapPosition(d14));
            this.graphic.relocate(this.snapPosition(d13), this.snapPosition(d14));
            if (this.containsMnemonic) {
                this.mnemonic_underscore.setEndX(d16);
                this.mnemonic_underscore.setStrokeWidth(d17 / 10.0);
                this.mnemonic_underscore.relocate(d13 + d15, d14 + d17 - 1.0);
            }
        } else {
            double d18 = 0.0;
            double d19 = 0.0;
            double d20 = 0.0;
            double d21 = 0.0;
            if (contentDisplay == ContentDisplay.TOP) {
                d18 = d13 + (d11 - d8) / 2.0;
                d20 = d13 + (d11 - d6) / 2.0;
                d19 = d14;
                d21 = d19 + d9 + d10;
            } else if (contentDisplay == ContentDisplay.RIGHT) {
                d20 = d13;
                d18 = d20 + d6 + d10;
                d19 = d14 + (d12 - d9) / 2.0;
                d21 = d14 + (d12 - d7) / 2.0;
            } else if (contentDisplay == ContentDisplay.BOTTOM) {
                d18 = d13 + (d11 - d8) / 2.0;
                d20 = d13 + (d11 - d6) / 2.0;
                d21 = d14;
                d19 = d21 + d7 + d10;
            } else if (contentDisplay == ContentDisplay.LEFT) {
                d18 = d13;
                d20 = d18 + d8 + d10;
                d19 = d14 + (d12 - d9) / 2.0;
                d21 = d14 + (d12 - d7) / 2.0;
            } else if (contentDisplay == ContentDisplay.CENTER) {
                d18 = d13 + (d11 - d8) / 2.0;
                d20 = d13 + (d11 - d6) / 2.0;
                d19 = d14 + (d12 - d9) / 2.0;
                d21 = d14 + (d12 - d7) / 2.0;
            }
            this.text.relocate(this.snapPosition(d20), this.snapPosition(d21));
            if (this.containsMnemonic) {
                this.mnemonic_underscore.setEndX(d16);
                this.mnemonic_underscore.setStrokeWidth(d17 / 10.0);
                this.mnemonic_underscore.relocate(this.snapPosition(d20 + d15), this.snapPosition(d21 + d17 - 1.0));
            }
            this.graphic.relocate(this.snapPosition(d18), this.snapPosition(d19));
        }
        if (this.text != null && (this.text.getLayoutBounds().getHeight() > this.wrapHeight || this.text.getLayoutBounds().getWidth() > this.wrapWidth)) {
            if (this.textClip == null) {
                this.textClip = new Rectangle();
            }
            if (labeled.getEffectiveNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT) {
                this.textClip.setX(this.text.getLayoutBounds().getMinX());
            } else {
                this.textClip.setX(this.text.getLayoutBounds().getMaxX() - this.wrapWidth);
            }
            this.textClip.setY(this.text.getLayoutBounds().getMinY());
            this.textClip.setWidth(this.wrapWidth);
            this.textClip.setHeight(this.wrapHeight);
            if (this.text.getClip() == null) {
                this.text.setClip(this.textClip);
            }
        } else if (this.text.getClip() != null) {
            this.text.setClip(null);
        }
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case TEXT: {
                Object object;
                Labeled labeled = (Labeled)this.getSkinnable();
                String string = labeled.getAccessibleText();
                if (string != null && !string.isEmpty()) {
                    return string;
                }
                if (this.bindings != null && (object = this.bindings.getText()) != null && !((String)object).isEmpty()) {
                    return object;
                }
                if (labeled != null && (object = labeled.getText()) != null && !((String)object).isEmpty()) {
                    return object;
                }
                if (this.graphic != null && (object = this.graphic.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0])) != null) {
                    return object;
                }
                return null;
            }
            case MNEMONIC: {
                if (this.bindings != null) {
                    return this.bindings.getMnemonic();
                }
                return null;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

