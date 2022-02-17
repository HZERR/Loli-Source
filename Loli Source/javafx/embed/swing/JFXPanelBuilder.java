/*
 * Decompiled with CFR 0.150.
 */
package javafx.embed.swing;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.util.Builder;

@Deprecated
public class JFXPanelBuilder<B extends JFXPanelBuilder<B>>
implements Builder<JFXPanel> {
    private int __set;
    private boolean opaque;
    private Scene scene;

    protected JFXPanelBuilder() {
    }

    public static JFXPanelBuilder<?> create() {
        return new JFXPanelBuilder();
    }

    public void applyTo(JFXPanel jFXPanel) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            jFXPanel.setOpaque(this.opaque);
        }
        if ((n2 & 2) != 0) {
            jFXPanel.setScene(this.scene);
        }
    }

    public B opaque(boolean bl) {
        this.opaque = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B scene(Scene scene) {
        this.scene = scene;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public JFXPanel build() {
        JFXPanel jFXPanel = new JFXPanel();
        this.applyTo(jFXPanel);
        return jFXPanel;
    }
}

