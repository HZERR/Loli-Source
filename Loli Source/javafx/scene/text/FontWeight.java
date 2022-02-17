/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.text;

public enum FontWeight {
    THIN(100, "Thin"),
    EXTRA_LIGHT(200, "Extra Light", "Ultra Light"),
    LIGHT(300, "Light"),
    NORMAL(400, "Normal", "Regular"),
    MEDIUM(500, "Medium"),
    SEMI_BOLD(600, "Semi Bold", "Demi Bold"),
    BOLD(700, "Bold"),
    EXTRA_BOLD(800, "Extra Bold", "Ultra Bold"),
    BLACK(900, "Black", "Heavy");

    private final int weight;
    private final String[] names;

    private FontWeight(int n3, String ... arrstring) {
        this.weight = n3;
        this.names = arrstring;
    }

    public int getWeight() {
        return this.weight;
    }

    public static FontWeight findByName(String string) {
        if (string == null) {
            return null;
        }
        for (FontWeight fontWeight : FontWeight.values()) {
            for (String string2 : fontWeight.names) {
                if (!string2.equalsIgnoreCase(string)) continue;
                return fontWeight;
            }
        }
        return null;
    }

    public static FontWeight findByWeight(int n2) {
        if (n2 <= 150) {
            return THIN;
        }
        if (n2 <= 250) {
            return EXTRA_LIGHT;
        }
        if (n2 < 350) {
            return LIGHT;
        }
        if (n2 <= 450) {
            return NORMAL;
        }
        if (n2 <= 550) {
            return MEDIUM;
        }
        if (n2 < 650) {
            return SEMI_BOLD;
        }
        if (n2 <= 750) {
            return BOLD;
        }
        if (n2 <= 850) {
            return EXTRA_BOLD;
        }
        return BLACK;
    }
}

