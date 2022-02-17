/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.paint;

import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

public class GradientUtils {
    public static String lengthToString(double d2, boolean bl) {
        if (bl) {
            return d2 * 100.0 + "%";
        }
        return d2 + "px";
    }

    public static class Parser {
        private int index;
        private String[] tokens;
        private boolean proportional;
        private boolean proportionalSet = false;

        private String[] splitString(String string, Delimiter delimiter, boolean bl) {
            LinkedList<String> linkedList = new LinkedList<String>();
            StringBuilder stringBuilder = new StringBuilder();
            char[] arrc = string.toCharArray();
            block0: for (int i2 = 0; i2 < arrc.length; ++i2) {
                char c2 = arrc[i2];
                if (delimiter.isDelimiter(c2)) {
                    if (!bl || stringBuilder.length() > 0) {
                        linkedList.add(stringBuilder.toString());
                    }
                    stringBuilder.setLength(0);
                    continue;
                }
                if (c2 == '(') {
                    while (i2 < arrc.length) {
                        stringBuilder.append(arrc[i2]);
                        if (arrc[i2] == ')') continue block0;
                        ++i2;
                    }
                    continue;
                }
                stringBuilder.append(arrc[i2]);
            }
            if (!bl || stringBuilder.length() > 0) {
                linkedList.add(stringBuilder.toString());
            }
            return linkedList.toArray(new String[linkedList.size()]);
        }

        public Parser(String string) {
            this.tokens = this.splitString(string, c2 -> c2 == ',', false);
            this.index = 0;
        }

        public int getSize() {
            return this.tokens.length;
        }

        public void shift() {
            ++this.index;
        }

        public String getCurrentToken() {
            String string = this.tokens[this.index].trim();
            if (string.isEmpty()) {
                throw new IllegalArgumentException("Invalid gradient specification: found empty token.");
            }
            return string;
        }

        public String[] splitCurrentToken() {
            return this.getCurrentToken().split("\\s");
        }

        public static void checkNumberOfArguments(String[] arrstring, int n2) {
            if (arrstring.length < n2 + 1) {
                throw new IllegalArgumentException("Invalid gradient specification: parameter '" + arrstring[0] + "' needs " + n2 + " argument(s).");
            }
        }

        public static double parseAngle(String string) {
            double d2 = 0.0;
            if (string.endsWith("deg")) {
                string = string.substring(0, string.length() - 3);
                d2 = Double.parseDouble(string);
            } else if (string.endsWith("grad")) {
                string = string.substring(0, string.length() - 4);
                d2 = Double.parseDouble(string);
                d2 = d2 * 9.0 / 10.0;
            } else if (string.endsWith("rad")) {
                string = string.substring(0, string.length() - 3);
                d2 = Double.parseDouble(string);
                d2 = d2 * 180.0 / Math.PI;
            } else if (string.endsWith("turn")) {
                string = string.substring(0, string.length() - 4);
                d2 = Double.parseDouble(string);
                d2 *= 360.0;
            } else {
                throw new IllegalArgumentException("Invalid gradient specification:angle must end in deg, rad, grad, or turn");
            }
            return d2;
        }

        public static double parsePercentage(String string) {
            if (!string.endsWith("%")) {
                throw new IllegalArgumentException("Invalid gradient specification: focus-distance must be specified as percentage");
            }
            string = string.substring(0, string.length() - 1);
            double d2 = Double.parseDouble(string) / 100.0;
            return d2;
        }

        public Point parsePoint(String string) {
            Point point = new Point();
            if (string.endsWith("%")) {
                point.proportional = true;
                string = string.substring(0, string.length() - 1);
            } else if (string.endsWith("px")) {
                string = string.substring(0, string.length() - 2);
            }
            point.value = Double.parseDouble(string);
            if (point.proportional) {
                point.value /= 100.0;
            }
            if (this.proportionalSet && this.proportional != point.proportional) {
                throw new IllegalArgumentException("Invalid gradient specification:cannot mix proportional and non-proportional values");
            }
            this.proportionalSet = true;
            this.proportional = point.proportional;
            return point;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public Stop[] parseStops(boolean bl, double d2) {
            int n2;
            int n3;
            double d3;
            int n4 = this.tokens.length - this.index;
            Color[] arrcolor = new Color[n4];
            double[] arrd = new double[n4];
            Stop[] arrstop = new Stop[n4];
            for (int i2 = 0; i2 < n4; ++i2) {
                String string = this.tokens[i2 + this.index].trim();
                String[] arrstring = this.splitString(string, c2 -> Character.isWhitespace(c2), true);
                if (arrstring.length == 0) {
                    throw new IllegalArgumentException("Invalid gradient specification, empty stop found");
                }
                String string2 = arrstring[0];
                d3 = -1.0;
                Color color = Color.web(string2);
                if (arrstring.length == 2) {
                    String string3 = arrstring[1];
                    if (string3.endsWith("%")) {
                        string3 = string3.substring(0, string3.length() - 1);
                        d3 = Double.parseDouble(string3) / 100.0;
                    } else {
                        if (bl) throw new IllegalArgumentException("Invalid gradient specification, non-proportional stops not permited in proportional gradient: " + string3);
                        if (string3.endsWith("px")) {
                            string3 = string3.substring(0, string3.length() - 2);
                        }
                        d3 = Double.parseDouble(string3) / d2;
                    }
                } else if (arrstring.length > 2) {
                    throw new IllegalArgumentException("Invalid gradient specification, unexpected content in stop specification: " + arrstring[2]);
                }
                arrcolor[i2] = color;
                arrd[i2] = d3;
            }
            if (arrd[0] < 0.0) {
                arrd[0] = 0.0;
            }
            if (arrd[arrd.length - 1] < 0.0) {
                arrd[arrd.length - 1] = 1.0;
            }
            double d4 = arrd[0];
            for (n3 = 1; n3 < arrd.length; ++n3) {
                if (arrd[n3] < d4 && arrd[n3] > 0.0) {
                    arrd[n3] = d4;
                    continue;
                }
                d4 = arrd[n3];
            }
            n3 = -1;
            for (n2 = 1; n2 < arrd.length; ++n2) {
                d3 = arrd[n2];
                if (d3 < 0.0 && n3 < 0) {
                    n3 = n2;
                    continue;
                }
                if (!(d3 >= 0.0) || n3 <= 0) continue;
                int n5 = n2 - n3 + 1;
                double d5 = (arrd[n2] - arrd[n3 - 1]) / (double)n5;
                for (int i3 = 0; i3 < n5 - 1; ++i3) {
                    arrd[n3 + i3] = arrd[n3 - 1] + d5 * (double)(i3 + 1);
                }
            }
            for (n2 = 0; n2 < arrstop.length; ++n2) {
                Stop stop;
                arrstop[n2] = stop = new Stop(arrd[n2], arrcolor[n2]);
            }
            return arrstop;
        }

        private static interface Delimiter {
            public boolean isDelimiter(char var1);
        }
    }

    public static class Point {
        public static final Point MIN = new Point(0.0, true);
        public static final Point MAX = new Point(1.0, true);
        public double value;
        public boolean proportional;

        public String toString() {
            return "value = " + this.value + ", proportional = " + this.proportional;
        }

        public Point(double d2, boolean bl) {
            this.value = d2;
            this.proportional = bl;
        }

        public Point() {
        }
    }
}

