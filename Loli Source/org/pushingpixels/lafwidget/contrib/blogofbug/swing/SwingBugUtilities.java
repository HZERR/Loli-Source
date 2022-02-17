/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class SwingBugUtilities {
    private SwingBugUtilities() {
    }

    public static void invokeAfter(final Runnable execute, int after) {
        Timer timer = new Timer(after, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                execute.run();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}

