/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.robot;

import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.impl.BaseFXRobot;
import javafx.scene.Scene;

public class FXRobotFactory {
    public static FXRobot createRobot(Scene scene) {
        return new BaseFXRobot(scene);
    }
}

