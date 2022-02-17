/*
 * Decompiled with CFR 0.150.
 */
package javafx.stage;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowBuilder;
import javafx.util.Builder;

@Deprecated
public class StageBuilder<B extends StageBuilder<B>>
extends WindowBuilder<B>
implements Builder<Stage> {
    private int __set;
    private boolean fullScreen;
    private boolean iconified;
    private Collection<? extends Image> icons;
    private double maxHeight;
    private double maxWidth;
    private double minHeight;
    private double minWidth;
    private boolean resizable;
    private Scene scene;
    private StageStyle style = StageStyle.DECORATED;
    private String title;

    protected StageBuilder() {
    }

    public static StageBuilder<?> create() {
        return new StageBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Stage stage) {
        super.applyTo(stage);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    stage.setFullScreen(this.fullScreen);
                    break;
                }
                case 1: {
                    stage.setIconified(this.iconified);
                    break;
                }
                case 2: {
                    stage.getIcons().addAll(this.icons);
                    break;
                }
                case 3: {
                    stage.setMaxHeight(this.maxHeight);
                    break;
                }
                case 4: {
                    stage.setMaxWidth(this.maxWidth);
                    break;
                }
                case 5: {
                    stage.setMinHeight(this.minHeight);
                    break;
                }
                case 6: {
                    stage.setMinWidth(this.minWidth);
                    break;
                }
                case 7: {
                    stage.setResizable(this.resizable);
                    break;
                }
                case 8: {
                    stage.setScene(this.scene);
                    break;
                }
                case 9: {
                    stage.setTitle(this.title);
                }
            }
        }
    }

    public B fullScreen(boolean bl) {
        this.fullScreen = bl;
        this.__set(0);
        return (B)this;
    }

    public B iconified(boolean bl) {
        this.iconified = bl;
        this.__set(1);
        return (B)this;
    }

    public B icons(Collection<? extends Image> collection) {
        this.icons = collection;
        this.__set(2);
        return (B)this;
    }

    public B icons(Image ... arrimage) {
        return this.icons(Arrays.asList(arrimage));
    }

    public B maxHeight(double d2) {
        this.maxHeight = d2;
        this.__set(3);
        return (B)this;
    }

    public B maxWidth(double d2) {
        this.maxWidth = d2;
        this.__set(4);
        return (B)this;
    }

    public B minHeight(double d2) {
        this.minHeight = d2;
        this.__set(5);
        return (B)this;
    }

    public B minWidth(double d2) {
        this.minWidth = d2;
        this.__set(6);
        return (B)this;
    }

    public B resizable(boolean bl) {
        this.resizable = bl;
        this.__set(7);
        return (B)this;
    }

    public B scene(Scene scene) {
        this.scene = scene;
        this.__set(8);
        return (B)this;
    }

    public B style(StageStyle stageStyle) {
        this.style = stageStyle;
        return (B)this;
    }

    public B title(String string) {
        this.title = string;
        this.__set(9);
        return (B)this;
    }

    @Override
    public Stage build() {
        Stage stage = new Stage(this.style);
        this.applyTo(stage);
        return stage;
    }
}

