/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import java.util.ArrayList;
import java.util.Iterator;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;
import net.java.games.input.DefaultControllerEnvironment;

public abstract class ControllerEnvironment {
    private static ControllerEnvironment defaultEnvironment;
    protected final ArrayList controllerListeners = new ArrayList();
    static final /* synthetic */ boolean $assertionsDisabled;

    static void logln(String msg) {
        ControllerEnvironment.log(msg + "\n");
    }

    static void log(String msg) {
        System.out.print(msg);
    }

    protected ControllerEnvironment() {
    }

    public abstract Controller[] getControllers();

    public void addControllerListener(ControllerListener l2) {
        if (!$assertionsDisabled && l2 == null) {
            throw new AssertionError();
        }
        this.controllerListeners.add(l2);
    }

    public abstract boolean isSupported();

    public void removeControllerListener(ControllerListener l2) {
        if (!$assertionsDisabled && l2 == null) {
            throw new AssertionError();
        }
        this.controllerListeners.remove(l2);
    }

    protected void fireControllerAdded(Controller c2) {
        ControllerEvent ev = new ControllerEvent(c2);
        Iterator it = this.controllerListeners.iterator();
        while (it.hasNext()) {
            ((ControllerListener)it.next()).controllerAdded(ev);
        }
    }

    protected void fireControllerRemoved(Controller c2) {
        ControllerEvent ev = new ControllerEvent(c2);
        Iterator it = this.controllerListeners.iterator();
        while (it.hasNext()) {
            ((ControllerListener)it.next()).controllerRemoved(ev);
        }
    }

    public static ControllerEnvironment getDefaultEnvironment() {
        return defaultEnvironment;
    }

    static {
        $assertionsDisabled = !ControllerEnvironment.class.desiredAssertionStatus();
        defaultEnvironment = new DefaultControllerEnvironment();
    }
}

