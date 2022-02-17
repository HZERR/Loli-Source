/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.util;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.stage.StageHelper;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Utils {
    public static float clamp(float f2, float f3, float f4) {
        if (f3 < f2) {
            return f2;
        }
        if (f3 > f4) {
            return f4;
        }
        return f3;
    }

    public static int clamp(int n2, int n3, int n4) {
        if (n3 < n2) {
            return n2;
        }
        if (n3 > n4) {
            return n4;
        }
        return n3;
    }

    public static double clamp(double d2, double d3, double d4) {
        if (d3 < d2) {
            return d2;
        }
        if (d3 > d4) {
            return d4;
        }
        return d3;
    }

    public static double clampMin(double d2, double d3) {
        if (d2 < d3) {
            return d3;
        }
        return d2;
    }

    public static int clampMax(int n2, int n3) {
        if (n2 > n3) {
            return n3;
        }
        return n2;
    }

    public static double nearest(double d2, double d3, double d4) {
        double d5 = d3 - d2;
        double d6 = d4 - d3;
        if (d5 < d6) {
            return d2;
        }
        return d4;
    }

    public static String stripQuotes(String string) {
        int n2;
        char c2;
        if (string == null) {
            return string;
        }
        if (string.length() == 0) {
            return string;
        }
        int n3 = 0;
        char c3 = string.charAt(n3);
        if (c3 == '\"' || c3 == '\'') {
            ++n3;
        }
        if ((c2 = string.charAt((n2 = string.length()) - 1)) == '\"' || c2 == '\'') {
            --n2;
        }
        if (n2 - n3 < 0) {
            return string;
        }
        return string.substring(n3, n2);
    }

    public static String[] split(String string, String string2) {
        if (string == null || string.length() == 0) {
            return new String[0];
        }
        if (string2 == null || string2.length() == 0) {
            return new String[0];
        }
        if (string2.length() > string.length()) {
            return new String[0];
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        int n2 = string.indexOf(string2);
        while (n2 >= 0) {
            String string3 = string.substring(0, n2);
            if (string3 != null && string3.length() > 0) {
                arrayList.add(string3);
            }
            string = string.substring(n2 + string2.length());
            n2 = string.indexOf(string2);
        }
        if (string != null && string.length() > 0) {
            arrayList.add(string);
        }
        return arrayList.toArray(new String[0]);
    }

    public static boolean contains(String string, String string2) {
        if (string == null || string.length() == 0) {
            return false;
        }
        if (string2 == null || string2.length() == 0) {
            return false;
        }
        if (string2.length() > string.length()) {
            return false;
        }
        return string.indexOf(string2) > -1;
    }

    public static double calculateBrightness(Color color) {
        return 0.3 * color.getRed() + 0.59 * color.getGreen() + 0.11 * color.getBlue();
    }

    public static Color deriveColor(Color color, double d2) {
        double d3 = Utils.calculateBrightness(color);
        double d4 = d2;
        if (d2 > 0.0) {
            if (d3 > 0.85) {
                d4 *= 1.6;
            } else if (!(d3 > 0.6)) {
                d4 = d3 > 0.5 ? (d4 *= 0.9) : (d3 > 0.4 ? (d4 *= 0.8) : (d3 > 0.3 ? (d4 *= 0.7) : (d4 *= 0.6)));
            }
        } else if (d3 < 0.2) {
            d4 *= 0.6;
        }
        if (d4 < -1.0) {
            d4 = -1.0;
        } else if (d4 > 1.0) {
            d4 = 1.0;
        }
        double[] arrd = Utils.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue());
        if (d4 > 0.0) {
            arrd[1] = arrd[1] * (1.0 - d4);
            arrd[2] = arrd[2] + (1.0 - arrd[2]) * d4;
        } else {
            arrd[2] = arrd[2] * (d4 + 1.0);
        }
        if (arrd[1] < 0.0) {
            arrd[1] = 0.0;
        } else if (arrd[1] > 1.0) {
            arrd[1] = 1.0;
        }
        if (arrd[2] < 0.0) {
            arrd[2] = 0.0;
        } else if (arrd[2] > 1.0) {
            arrd[2] = 1.0;
        }
        Color color2 = Color.hsb((int)arrd[0], arrd[1], arrd[2], color.getOpacity());
        return Color.hsb((int)arrd[0], arrd[1], arrd[2], color.getOpacity());
    }

    private static Color interpolateLinear(double d2, Color color, Color color2) {
        Color color3 = Utils.convertSRGBtoLinearRGB(color);
        Color color4 = Utils.convertSRGBtoLinearRGB(color2);
        return Utils.convertLinearRGBtoSRGB(Color.color(color3.getRed() + (color4.getRed() - color3.getRed()) * d2, color3.getGreen() + (color4.getGreen() - color3.getGreen()) * d2, color3.getBlue() + (color4.getBlue() - color3.getBlue()) * d2, color3.getOpacity() + (color4.getOpacity() - color3.getOpacity()) * d2));
    }

    private static Color ladder(double d2, Stop[] arrstop) {
        Stop stop = null;
        for (int i2 = 0; i2 < arrstop.length; ++i2) {
            Stop stop2 = arrstop[i2];
            if (d2 <= stop2.getOffset()) {
                if (stop == null) {
                    return stop2.getColor();
                }
                return Utils.interpolateLinear((d2 - stop.getOffset()) / (stop2.getOffset() - stop.getOffset()), stop.getColor(), stop2.getColor());
            }
            stop = stop2;
        }
        return stop.getColor();
    }

    public static Color ladder(Color color, Stop[] arrstop) {
        return Utils.ladder(Utils.calculateBrightness(color), arrstop);
    }

    public static double[] HSBtoRGB(double d2, double d3, double d4) {
        double d5 = (d2 % 360.0 + 360.0) % 360.0;
        d2 = d5 / 360.0;
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = 0.0;
        if (d3 == 0.0) {
            d7 = d8 = d4;
            d6 = d8;
        } else {
            double d9 = (d2 - Math.floor(d2)) * 6.0;
            double d10 = d9 - Math.floor(d9);
            double d11 = d4 * (1.0 - d3);
            double d12 = d4 * (1.0 - d3 * d10);
            double d13 = d4 * (1.0 - d3 * (1.0 - d10));
            switch ((int)d9) {
                case 0: {
                    d6 = d4;
                    d7 = d13;
                    d8 = d11;
                    break;
                }
                case 1: {
                    d6 = d12;
                    d7 = d4;
                    d8 = d11;
                    break;
                }
                case 2: {
                    d6 = d11;
                    d7 = d4;
                    d8 = d13;
                    break;
                }
                case 3: {
                    d6 = d11;
                    d7 = d12;
                    d8 = d4;
                    break;
                }
                case 4: {
                    d6 = d13;
                    d7 = d11;
                    d8 = d4;
                    break;
                }
                case 5: {
                    d6 = d4;
                    d7 = d11;
                    d8 = d12;
                }
            }
        }
        double[] arrd = new double[]{d6, d7, d8};
        return arrd;
    }

    public static double[] RGBtoHSB(double d2, double d3, double d4) {
        double d5;
        double d6;
        double d7;
        double[] arrd = new double[3];
        double d8 = d7 = d2 > d3 ? d2 : d3;
        if (d4 > d7) {
            d7 = d4;
        }
        double d9 = d6 = d2 < d3 ? d2 : d3;
        if (d4 < d6) {
            d6 = d4;
        }
        double d10 = d7;
        double d11 = d7 != 0.0 ? (d7 - d6) / d7 : 0.0;
        if (d11 == 0.0) {
            d5 = 0.0;
        } else {
            double d12 = (d7 - d2) / (d7 - d6);
            double d13 = (d7 - d3) / (d7 - d6);
            double d14 = (d7 - d4) / (d7 - d6);
            d5 = d2 == d7 ? d14 - d13 : (d3 == d7 ? 2.0 + d12 - d14 : 4.0 + d13 - d12);
            if ((d5 /= 6.0) < 0.0) {
                d5 += 1.0;
            }
        }
        arrd[0] = d5 * 360.0;
        arrd[1] = d11;
        arrd[2] = d10;
        return arrd;
    }

    public static Color convertSRGBtoLinearRGB(Color color) {
        double[] arrd = new double[]{color.getRed(), color.getGreen(), color.getBlue()};
        for (int i2 = 0; i2 < arrd.length; ++i2) {
            arrd[i2] = arrd[i2] <= 0.04045 ? arrd[i2] / 12.92 : Math.pow((arrd[i2] + 0.055) / 1.055, 2.4);
        }
        return Color.color(arrd[0], arrd[1], arrd[2], color.getOpacity());
    }

    public static Color convertLinearRGBtoSRGB(Color color) {
        double[] arrd = new double[]{color.getRed(), color.getGreen(), color.getBlue()};
        for (int i2 = 0; i2 < arrd.length; ++i2) {
            arrd[i2] = arrd[i2] <= 0.0031308 ? arrd[i2] * 12.92 : 1.055 * Math.pow(arrd[i2], 0.4166666666666667) - 0.055;
        }
        return Color.color(arrd[0], arrd[1], arrd[2], color.getOpacity());
    }

    public static double sum(double[] arrd) {
        double d2 = 0.0;
        for (double d3 : arrd) {
            d2 += d3;
        }
        return d2 / (double)arrd.length;
    }

    public static Point2D pointRelativeTo(Node node, Node node2, HPos hPos, VPos vPos, double d2, double d3, boolean bl) {
        double d4 = node2.getLayoutBounds().getWidth();
        double d5 = node2.getLayoutBounds().getHeight();
        return Utils.pointRelativeTo(node, d4, d5, hPos, vPos, d2, d3, bl);
    }

    public static Point2D pointRelativeTo(Node node, double d2, double d3, HPos hPos, VPos vPos, double d4, double d5, boolean bl) {
        Bounds bounds = Utils.getBounds(node);
        Scene scene = node.getScene();
        NodeOrientation nodeOrientation = node.getEffectiveNodeOrientation();
        if (nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
            if (hPos == HPos.LEFT) {
                hPos = HPos.RIGHT;
            } else if (hPos == HPos.RIGHT) {
                hPos = HPos.LEFT;
            }
            d4 *= -1.0;
        }
        double d6 = Utils.positionX(bounds, d2, hPos) + d4;
        double d7 = Utils.positionY(bounds, d3, vPos) + d5;
        if (nodeOrientation == NodeOrientation.RIGHT_TO_LEFT && hPos == HPos.CENTER) {
            d6 = scene.getWindow() instanceof Stage ? d6 + bounds.getWidth() - d2 : d6 - bounds.getWidth() - d2;
        }
        if (bl) {
            return Utils.pointRelativeTo((Object)node, d2, d3, d6, d7, hPos, vPos);
        }
        return new Point2D(d6, d7);
    }

    public static Point2D pointRelativeTo(Object object, double d2, double d3, double d4, double d5, HPos hPos, VPos vPos) {
        Rectangle2D rectangle2D;
        double d6 = d4;
        double d7 = d5;
        Bounds bounds = Utils.getBounds(object);
        Screen screen = Utils.getScreen(object);
        Rectangle2D rectangle2D2 = rectangle2D = Utils.hasFullScreenStage(screen) ? screen.getBounds() : screen.getVisualBounds();
        if (hPos != null) {
            if (d6 + d2 > rectangle2D.getMaxX()) {
                d6 = Utils.positionX(bounds, d2, Utils.getHPosOpposite(hPos, vPos));
            }
            if (d6 < rectangle2D.getMinX()) {
                d6 = Utils.positionX(bounds, d2, Utils.getHPosOpposite(hPos, vPos));
            }
        }
        if (vPos != null) {
            if (d7 + d3 > rectangle2D.getMaxY()) {
                d7 = Utils.positionY(bounds, d3, Utils.getVPosOpposite(hPos, vPos));
            }
            if (d7 < rectangle2D.getMinY()) {
                d7 = Utils.positionY(bounds, d3, Utils.getVPosOpposite(hPos, vPos));
            }
        }
        if (d6 + d2 > rectangle2D.getMaxX()) {
            d6 -= d6 + d2 - rectangle2D.getMaxX();
        }
        if (d6 < rectangle2D.getMinX()) {
            d6 = rectangle2D.getMinX();
        }
        if (d7 + d3 > rectangle2D.getMaxY()) {
            d7 -= d7 + d3 - rectangle2D.getMaxY();
        }
        if (d7 < rectangle2D.getMinY()) {
            d7 = rectangle2D.getMinY();
        }
        return new Point2D(d6, d7);
    }

    private static double positionX(Bounds bounds, double d2, HPos hPos) {
        if (hPos == HPos.CENTER) {
            return bounds.getMinX();
        }
        if (hPos == HPos.RIGHT) {
            return bounds.getMaxX();
        }
        if (hPos == HPos.LEFT) {
            return bounds.getMinX() - d2;
        }
        return 0.0;
    }

    private static double positionY(Bounds bounds, double d2, VPos vPos) {
        if (vPos == VPos.BOTTOM) {
            return bounds.getMaxY();
        }
        if (vPos == VPos.CENTER) {
            return bounds.getMinY();
        }
        if (vPos == VPos.TOP) {
            return bounds.getMinY() - d2;
        }
        return 0.0;
    }

    private static Bounds getBounds(Object object) {
        if (object instanceof Node) {
            Node node = (Node)object;
            Bounds bounds = node.localToScreen(node.getLayoutBounds());
            return bounds != null ? bounds : new BoundingBox(0.0, 0.0, 0.0, 0.0);
        }
        if (object instanceof Window) {
            Window window = (Window)object;
            return new BoundingBox(window.getX(), window.getY(), window.getWidth(), window.getHeight());
        }
        return new BoundingBox(0.0, 0.0, 0.0, 0.0);
    }

    private static HPos getHPosOpposite(HPos hPos, VPos vPos) {
        if (vPos == VPos.CENTER) {
            if (hPos == HPos.LEFT) {
                return HPos.RIGHT;
            }
            if (hPos == HPos.RIGHT) {
                return HPos.LEFT;
            }
            if (hPos == HPos.CENTER) {
                return HPos.CENTER;
            }
            return HPos.CENTER;
        }
        return HPos.CENTER;
    }

    private static VPos getVPosOpposite(HPos hPos, VPos vPos) {
        if (hPos == HPos.CENTER) {
            if (vPos == VPos.BASELINE) {
                return VPos.BASELINE;
            }
            if (vPos == VPos.BOTTOM) {
                return VPos.TOP;
            }
            if (vPos == VPos.CENTER) {
                return VPos.CENTER;
            }
            if (vPos == VPos.TOP) {
                return VPos.BOTTOM;
            }
            return VPos.CENTER;
        }
        return VPos.CENTER;
    }

    public static boolean hasFullScreenStage(Screen screen) {
        ObservableList<Stage> observableList = StageHelper.getStages();
        for (Stage stage : observableList) {
            if (!stage.isFullScreen() || Utils.getScreen(stage) != screen) continue;
            return true;
        }
        return false;
    }

    public static boolean isQVGAScreen() {
        Rectangle2D rectangle2D = Screen.getPrimary().getBounds();
        return rectangle2D.getWidth() == 320.0 && rectangle2D.getHeight() == 240.0 || rectangle2D.getWidth() == 240.0 && rectangle2D.getHeight() == 320.0;
    }

    public static Screen getScreen(Object object) {
        Bounds bounds = Utils.getBounds(object);
        Rectangle2D rectangle2D = new Rectangle2D(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
        return Utils.getScreenForRectangle(rectangle2D);
    }

    public static Screen getScreenForRectangle(Rectangle2D rectangle2D) {
        ObservableList<Screen> observableList = Screen.getScreens();
        double d2 = rectangle2D.getMinX();
        double d3 = rectangle2D.getMaxX();
        double d4 = rectangle2D.getMinY();
        double d5 = rectangle2D.getMaxY();
        Screen screen = null;
        double d6 = 0.0;
        for (Screen screen2 : observableList) {
            Rectangle2D rectangle2D2 = screen2.getBounds();
            double d7 = Utils.getIntersectionLength(d2, d3, rectangle2D2.getMinX(), rectangle2D2.getMaxX()) * Utils.getIntersectionLength(d4, d5, rectangle2D2.getMinY(), rectangle2D2.getMaxY());
            if (!(d6 < d7)) continue;
            d6 = d7;
            screen = screen2;
        }
        if (screen != null) {
            return screen;
        }
        screen = Screen.getPrimary();
        double d8 = Double.MAX_VALUE;
        for (Screen screen3 : observableList) {
            double d9;
            Rectangle2D rectangle2D3 = screen3.getBounds();
            double d10 = Utils.getOuterDistance(d2, d3, rectangle2D3.getMinX(), rectangle2D3.getMaxX());
            double d11 = d10 * d10 + (d9 = Utils.getOuterDistance(d4, d5, rectangle2D3.getMinY(), rectangle2D3.getMaxY())) * d9;
            if (!(d8 > d11)) continue;
            d8 = d11;
            screen = screen3;
        }
        return screen;
    }

    public static Screen getScreenForPoint(double d2, double d3) {
        ObservableList<Screen> observableList = Screen.getScreens();
        for (Screen screen : observableList) {
            Rectangle2D rectangle2D = screen.getBounds();
            if (!(d2 >= rectangle2D.getMinX()) || !(d2 < rectangle2D.getMaxX()) || !(d3 >= rectangle2D.getMinY()) || !(d3 < rectangle2D.getMaxY())) continue;
            return screen;
        }
        Object object = Screen.getPrimary();
        double d4 = Double.MAX_VALUE;
        for (Screen screen : observableList) {
            double d5;
            Rectangle2D rectangle2D = screen.getBounds();
            double d6 = Utils.getOuterDistance(rectangle2D.getMinX(), rectangle2D.getMaxX(), d2);
            double d7 = d6 * d6 + (d5 = Utils.getOuterDistance(rectangle2D.getMinY(), rectangle2D.getMaxY(), d3)) * d5;
            if (!(d4 >= d7)) continue;
            d4 = d7;
            object = screen;
        }
        return object;
    }

    private static double getIntersectionLength(double d2, double d3, double d4, double d5) {
        return d2 <= d4 ? Utils.getIntersectionLengthImpl(d4, d5, d3) : Utils.getIntersectionLengthImpl(d2, d3, d5);
    }

    private static double getIntersectionLengthImpl(double d2, double d3, double d4) {
        if (d4 <= d2) {
            return 0.0;
        }
        return d4 <= d3 ? d4 - d2 : d3 - d2;
    }

    private static double getOuterDistance(double d2, double d3, double d4, double d5) {
        if (d3 <= d4) {
            return d4 - d3;
        }
        if (d5 <= d2) {
            return d5 - d2;
        }
        return 0.0;
    }

    private static double getOuterDistance(double d2, double d3, double d4) {
        if (d4 <= d2) {
            return d2 - d4;
        }
        if (d4 >= d3) {
            return d4 - d3;
        }
        return 0.0;
    }

    public static boolean assertionEnabled() {
        boolean bl = false;
        if (!$assertionsDisabled) {
            bl = true;
            if (!true) {
                throw new AssertionError();
            }
        }
        return bl;
    }

    public static boolean isWindows() {
        return PlatformUtil.isWindows();
    }

    public static boolean isMac() {
        return PlatformUtil.isMac();
    }

    public static boolean isUnix() {
        return PlatformUtil.isUnix();
    }

    public static String convertUnicode(String string) {
        int n2 = -1;
        char[] arrc = string.toCharArray();
        int n3 = arrc.length;
        int n4 = -1;
        char[] arrc2 = new char[n3];
        int n5 = 0;
        while (n4 < n3 - 1) {
            char c2;
            if ((c2 = arrc[++n4]) == '\\' && n2 != n4) {
                if ((c2 = arrc[++n4]) == 'u') {
                    while ((c2 = arrc[++n4]) == 'u') {
                    }
                    int n6 = n4 + 3;
                    if (n6 < n3) {
                        int n7;
                        char c3 = c2;
                        int n8 = Character.digit(c3, 16);
                        if (n8 >= 0 && c3 > '\u007f') {
                            c2 = "0123456789abcdef".charAt(n8);
                        }
                        int n9 = n7 = n8;
                        while (n4 < n6 && n7 >= 0) {
                            char c4;
                            int n10;
                            if ((n10 = Character.digit(c4 = (c2 = arrc[++n4]), 16)) >= 0 && c4 > '\u007f') {
                                c2 = "0123456789abcdef".charAt(n10);
                            }
                            n7 = n10;
                            n9 = (n9 << 4) + n7;
                        }
                        if (n7 >= 0) {
                            c2 = (char)n9;
                            n2 = n4;
                        }
                    }
                } else {
                    --n4;
                    c2 = '\\';
                }
            }
            arrc2[n5++] = c2;
        }
        return new String(arrc2, 0, n5);
    }
}

