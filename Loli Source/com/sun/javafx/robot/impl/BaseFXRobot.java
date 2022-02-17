/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.robot.impl;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotImage;
import com.sun.javafx.robot.impl.FXRobotHelper;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class BaseFXRobot
extends FXRobot {
    private static final boolean debugOut;
    private static Map<KeyCode, String> keyTextMap;
    private Scene target;
    private boolean isShiftDown = false;
    private boolean isControlDown = false;
    private boolean isAltDown = false;
    private boolean isMetaDown = false;
    private boolean isButton1Pressed = false;
    private boolean isButton2Pressed = false;
    private boolean isButton3Pressed = false;
    private MouseButton lastButtonPressed = null;
    private double sceneMouseX;
    private double sceneMouseY;
    private double screenMouseX;
    private double screenMouseY;
    private Object lastImage;
    private FXRobotImage lastConvertedImage;

    private static boolean computeDebugOut() {
        boolean bl = false;
        try {
            bl = "true".equals(System.getProperty("fxrobot.verbose", "false"));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return bl;
    }

    private static void out(String string) {
        if (debugOut) {
            System.out.println("[FXRobot] " + string);
        }
    }

    private static String getKeyText(KeyCode keyCode) {
        return keyCode.getName();
    }

    public BaseFXRobot(Scene scene) {
        this.target = scene;
    }

    @Override
    public void waitForIdle() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        PlatformImpl.runLater(() -> countDownLatch.countDown());
        while (true) {
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                continue;
            }
            break;
        }
    }

    @Override
    public void keyPress(KeyCode keyCode) {
        this.doKeyEvent(KeyEvent.KEY_PRESSED, keyCode, "");
    }

    @Override
    public void keyRelease(KeyCode keyCode) {
        this.doKeyEvent(KeyEvent.KEY_RELEASED, keyCode, "");
    }

    @Override
    public void keyType(KeyCode keyCode, String string) {
        this.doKeyEvent(KeyEvent.KEY_TYPED, keyCode, string);
    }

    @Override
    public void mouseMove(int n2, int n3) {
        this.doMouseEvent(n2, n3, this.lastButtonPressed, 0, MouseEvent.MOUSE_MOVED);
    }

    @Override
    public void mousePress(MouseButton mouseButton, int n2) {
        this.doMouseEvent(this.sceneMouseX, this.sceneMouseY, mouseButton, n2, MouseEvent.MOUSE_PRESSED);
    }

    @Override
    public void mouseRelease(MouseButton mouseButton, int n2) {
        this.doMouseEvent(this.sceneMouseX, this.sceneMouseY, mouseButton, n2, MouseEvent.MOUSE_RELEASED);
    }

    @Override
    public void mouseClick(MouseButton mouseButton, int n2) {
        this.doMouseEvent(this.sceneMouseX, this.sceneMouseY, mouseButton, n2, MouseEvent.MOUSE_CLICKED);
    }

    @Override
    public void mouseDrag(MouseButton mouseButton) {
        this.doMouseEvent(this.sceneMouseX, this.sceneMouseY, mouseButton, 0, MouseEvent.MOUSE_DRAGGED);
    }

    @Override
    public void mouseWheel(int n2) {
        this.doScrollEvent(this.sceneMouseX, this.sceneMouseY, n2, ScrollEvent.SCROLL);
    }

    @Override
    public int getPixelColor(int n2, int n3) {
        FXRobotImage fXRobotImage = this.getSceneCapture(0, 0, 100, 100);
        if (fXRobotImage != null) {
            return fXRobotImage.getArgb(n2, n3);
        }
        return 0;
    }

    @Override
    public FXRobotImage getSceneCapture(int n2, int n3, int n4, int n5) {
        Object object = FXRobotHelper.sceneAccessor.renderToImage(this.target, this.lastImage);
        if (object != null) {
            this.lastImage = object;
            this.lastConvertedImage = FXRobotHelper.imageConvertor.convertToFXRobotImage(object);
        }
        return this.lastConvertedImage;
    }

    private void doKeyEvent(EventType<KeyEvent> eventType, KeyCode keyCode, String string) {
        boolean bl;
        boolean bl2 = eventType == KeyEvent.KEY_PRESSED;
        boolean bl3 = bl = eventType == KeyEvent.KEY_TYPED;
        if (keyCode == KeyCode.SHIFT) {
            this.isShiftDown = bl2;
        }
        if (keyCode == KeyCode.CONTROL) {
            this.isControlDown = bl2;
        }
        if (keyCode == KeyCode.ALT) {
            this.isAltDown = bl2;
        }
        if (keyCode == KeyCode.META) {
            this.isMetaDown = bl2;
        }
        String string2 = bl ? "" : BaseFXRobot.getKeyText(keyCode);
        String string3 = bl ? string : KeyEvent.CHAR_UNDEFINED;
        KeyEvent keyEvent = FXRobotHelper.inputAccessor.createKeyEvent(eventType, keyCode, string3, string2, this.isShiftDown, this.isControlDown, this.isAltDown, this.isMetaDown);
        PlatformImpl.runLater(() -> {
            BaseFXRobot.out("doKeyEvent: injecting: {e}");
            FXRobotHelper.sceneAccessor.processKeyEvent(this.target, keyEvent);
        });
        if (this.autoWait) {
            this.waitForIdle();
        }
    }

    private void doMouseEvent(double d2, double d3, MouseButton mouseButton, int n2, EventType<MouseEvent> eventType) {
        boolean bl;
        this.screenMouseX = this.target.getWindow().getX() + this.target.getX() + d2;
        this.screenMouseY = this.target.getWindow().getY() + this.target.getY() + d3;
        this.sceneMouseX = d2;
        this.sceneMouseY = d3;
        MouseButton mouseButton2 = mouseButton;
        EventType<MouseEvent> eventType2 = eventType;
        if (eventType2 == MouseEvent.MOUSE_PRESSED || eventType2 == MouseEvent.MOUSE_RELEASED) {
            boolean bl2 = bl = eventType2 == MouseEvent.MOUSE_PRESSED;
            if (mouseButton2 == MouseButton.PRIMARY) {
                this.isButton1Pressed = bl;
            } else if (mouseButton2 == MouseButton.MIDDLE) {
                this.isButton2Pressed = bl;
            } else if (mouseButton2 == MouseButton.SECONDARY) {
                this.isButton3Pressed = bl;
            }
            if (bl) {
                this.lastButtonPressed = mouseButton2;
            } else if (!(this.isButton1Pressed || this.isButton2Pressed || this.isButton3Pressed)) {
                this.lastButtonPressed = MouseButton.NONE;
            }
        } else if (eventType2 == MouseEvent.MOUSE_MOVED) {
            boolean bl3 = bl = this.isButton1Pressed || this.isButton2Pressed || this.isButton3Pressed;
            if (bl) {
                eventType2 = MouseEvent.MOUSE_DRAGGED;
                mouseButton2 = MouseButton.NONE;
            }
        }
        MouseEvent mouseEvent = FXRobotHelper.inputAccessor.createMouseEvent(eventType2, (int)this.sceneMouseX, (int)this.sceneMouseY, (int)this.screenMouseX, (int)this.screenMouseY, mouseButton2, n2, this.isShiftDown, this.isControlDown, this.isAltDown, this.isMetaDown, mouseButton2 == MouseButton.SECONDARY, this.isButton1Pressed, this.isButton2Pressed, this.isButton3Pressed);
        PlatformImpl.runLater(() -> {
            BaseFXRobot.out("doMouseEvent: injecting: " + mouseEvent);
            FXRobotHelper.sceneAccessor.processMouseEvent(this.target, mouseEvent);
        });
        if (this.autoWait) {
            this.waitForIdle();
        }
    }

    private void doScrollEvent(double d2, double d3, double d4, EventType<ScrollEvent> eventType) {
        this.screenMouseX = this.target.getWindow().getX() + this.target.getX() + d2;
        this.screenMouseY = this.target.getWindow().getY() + this.target.getY() + d3;
        this.sceneMouseX = d2;
        this.sceneMouseY = d3;
        ScrollEvent scrollEvent = FXRobotHelper.inputAccessor.createScrollEvent(eventType, 0, (int)d4 * 40, ScrollEvent.HorizontalTextScrollUnits.NONE, 0, ScrollEvent.VerticalTextScrollUnits.NONE, 0, (int)this.sceneMouseX, (int)this.sceneMouseY, (int)this.screenMouseX, (int)this.screenMouseY, this.isShiftDown, this.isControlDown, this.isAltDown, this.isMetaDown);
        PlatformImpl.runLater(() -> {
            BaseFXRobot.out("doScrollEvent: injecting: " + scrollEvent);
            FXRobotHelper.sceneAccessor.processScrollEvent(this.target, scrollEvent);
        });
        if (this.autoWait) {
            this.waitForIdle();
        }
    }

    static {
        String string = KeyEvent.CHAR_UNDEFINED;
        debugOut = BaseFXRobot.computeDebugOut();
    }
}

