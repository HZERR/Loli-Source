/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml.builder;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

@DefaultProperty(value="root")
public class JavaFXSceneBuilder
implements Builder<Scene> {
    private Parent root = null;
    private double width = -1.0;
    private double height = -1.0;
    private Paint fill = Color.WHITE;
    private ArrayList<String> stylesheets = new ArrayList();

    public Parent getRoot() {
        return this.root;
    }

    public void setRoot(Parent parent) {
        this.root = parent;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double d2) {
        if (d2 < -1.0) {
            throw new IllegalArgumentException();
        }
        this.width = d2;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double d2) {
        if (d2 < -1.0) {
            throw new IllegalArgumentException();
        }
        this.height = d2;
    }

    public Paint getFill() {
        return this.fill;
    }

    public void setFill(Paint paint) {
        if (paint == null) {
            throw new NullPointerException();
        }
        this.fill = paint;
    }

    public List<String> getStylesheets() {
        return this.stylesheets;
    }

    @Override
    public Scene build() {
        Scene scene = new Scene(this.root, this.width, this.height, this.fill);
        for (String string : this.stylesheets) {
            scene.getStylesheets().add(string);
        }
        return scene;
    }
}

