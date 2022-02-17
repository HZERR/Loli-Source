/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.utils;

public class LafConstants {

    public static class TabOverviewKind {
        public static final TabOverviewKind GRID = new TabOverviewKind("grid");
        public static final TabOverviewKind ROUND_CAROUSEL = new TabOverviewKind("round carousel");
        public static final TabOverviewKind MENU_CAROUSEL = new TabOverviewKind("menu carousel");
        private String name;

        public TabOverviewKind(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class PasswordStrength {
        public static final PasswordStrength WEAK = new PasswordStrength();
        public static final PasswordStrength MEDIUM = new PasswordStrength();
        public static final PasswordStrength STRONG = new PasswordStrength();

        private PasswordStrength() {
        }
    }
}

