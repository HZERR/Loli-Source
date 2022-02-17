/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tree.dnd;

import java.util.EventListener;
import org.pushingpixels.lafwidget.tree.dnd.DnDVetoException;
import org.pushingpixels.lafwidget.tree.dnd.StringTreeDnDEvent;

public interface StringTreeDnDListener
extends EventListener {
    public void mayDrop(StringTreeDnDEvent var1) throws DnDVetoException;

    public void drop(StringTreeDnDEvent var1) throws DnDVetoException;
}

