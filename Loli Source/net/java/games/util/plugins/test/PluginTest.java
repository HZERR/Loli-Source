/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.util.plugins.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import net.java.games.util.plugins.Plugins;
import net.java.games.util.plugins.test.ClassRenderer;
import net.java.games.util.plugins.test.ListUpdater;

public class PluginTest {
    static final boolean DEBUG = false;
    Plugins plugins;
    JList plist;
    Class[] piList;

    public PluginTest() {
        try {
            this.plugins = new Plugins(new File("test_plugins"));
        }
        catch (IOException e2) {
            e2.printStackTrace();
            System.exit(1);
        }
        JFrame f2 = new JFrame("PluginTest");
        this.plist = new JList(new DefaultListModel());
        this.plist.setCellRenderer(new ClassRenderer());
        Container c2 = f2.getContentPane();
        c2.setLayout(new BorderLayout());
        c2.add((Component)new JScrollPane(this.plist), "Center");
        JPanel p2 = new JPanel();
        c2.add((Component)p2, "South");
        p2.setLayout(new FlowLayout());
        f2.pack();
        f2.setDefaultCloseOperation(3);
        f2.setVisible(true);
        this.doListAll();
    }

    private void doListAll() {
        SwingUtilities.invokeLater(new ListUpdater(this.plist, this.plugins.get()));
    }

    public static void main(String[] args) {
        new PluginTest();
    }
}

