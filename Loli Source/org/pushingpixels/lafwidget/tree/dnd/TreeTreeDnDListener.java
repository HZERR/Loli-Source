/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tree.dnd;

import java.util.EventListener;
import org.pushingpixels.lafwidget.tree.dnd.DnDVetoException;
import org.pushingpixels.lafwidget.tree.dnd.TreeTreeDnDEvent;

public interface TreeTreeDnDListener
extends EventListener {
    public void mayDrop(TreeTreeDnDEvent var1) throws DnDVetoException;

    public void drop(TreeTreeDnDEvent var1) throws DnDVetoException;
}

