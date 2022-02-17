/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.Settings;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.animation.AnimationPulse;
import java.util.Map;

public final class MasterTimer
extends AbstractMasterTimer {
    private static final Object MASTER_TIMER_KEY = new StringBuilder("MasterTimerKey");

    private MasterTimer() {
    }

    public static synchronized MasterTimer getInstance() {
        Map<Object, Object> map = Toolkit.getToolkit().getContextMap();
        MasterTimer masterTimer = (MasterTimer)map.get(MASTER_TIMER_KEY);
        if (masterTimer == null) {
            masterTimer = new MasterTimer();
            map.put(MASTER_TIMER_KEY, masterTimer);
            if (Settings.getBoolean("com.sun.scenario.animation.AnimationMBean.enabled", false)) {
                AnimationPulse.getDefaultBean().setEnabled(true);
            }
        }
        return masterTimer;
    }

    @Override
    protected int getPulseDuration(int n2) {
        int n3 = n2 / 60;
        if (Settings.get("javafx.animation.framerate") != null) {
            int n4 = Settings.getInt("javafx.animation.framerate", 60);
            if (n4 > 0) {
                n3 = n2 / n4;
            }
        } else if (Settings.get("javafx.animation.pulse") != null) {
            int n5 = Settings.getInt("javafx.animation.pulse", 60);
            if (n5 > 0) {
                n3 = n2 / n5;
            }
        } else {
            int n6 = Toolkit.getToolkit().getRefreshRate();
            if (n6 > 0) {
                n3 = n2 / n6;
            }
        }
        return n3;
    }

    @Override
    protected void postUpdateAnimationRunnable(DelayedRunnable delayedRunnable) {
        Toolkit.getToolkit().setAnimationRunnable(delayedRunnable);
    }

    @Override
    protected void recordStart(long l2) {
        AnimationPulse.getDefaultBean().recordStart(l2);
    }

    @Override
    protected void recordEnd() {
        AnimationPulse.getDefaultBean().recordEnd();
    }

    @Override
    protected void recordAnimationEnd() {
        AnimationPulse.getDefaultBean().recordAnimationEnd();
    }
}

