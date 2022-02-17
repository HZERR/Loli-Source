/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.input;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Locale;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

public abstract class KeyCombination {
    public static final Modifier SHIFT_DOWN = new Modifier(KeyCode.SHIFT, ModifierValue.DOWN);
    public static final Modifier SHIFT_ANY = new Modifier(KeyCode.SHIFT, ModifierValue.ANY);
    public static final Modifier CONTROL_DOWN = new Modifier(KeyCode.CONTROL, ModifierValue.DOWN);
    public static final Modifier CONTROL_ANY = new Modifier(KeyCode.CONTROL, ModifierValue.ANY);
    public static final Modifier ALT_DOWN = new Modifier(KeyCode.ALT, ModifierValue.DOWN);
    public static final Modifier ALT_ANY = new Modifier(KeyCode.ALT, ModifierValue.ANY);
    public static final Modifier META_DOWN = new Modifier(KeyCode.META, ModifierValue.DOWN);
    public static final Modifier META_ANY = new Modifier(KeyCode.META, ModifierValue.ANY);
    public static final Modifier SHORTCUT_DOWN = new Modifier(KeyCode.SHORTCUT, ModifierValue.DOWN);
    public static final Modifier SHORTCUT_ANY = new Modifier(KeyCode.SHORTCUT, ModifierValue.ANY);
    private static final Modifier[] POSSIBLE_MODIFIERS = new Modifier[]{SHIFT_DOWN, SHIFT_ANY, CONTROL_DOWN, CONTROL_ANY, ALT_DOWN, ALT_ANY, META_DOWN, META_ANY, SHORTCUT_DOWN, SHORTCUT_ANY};
    public static final KeyCombination NO_MATCH = new KeyCombination(new Modifier[0]){

        @Override
        public boolean match(KeyEvent keyEvent) {
            return false;
        }
    };
    private final ModifierValue shift;
    private final ModifierValue control;
    private final ModifierValue alt;
    private final ModifierValue meta;
    private final ModifierValue shortcut;

    public final ModifierValue getShift() {
        return this.shift;
    }

    public final ModifierValue getControl() {
        return this.control;
    }

    public final ModifierValue getAlt() {
        return this.alt;
    }

    public final ModifierValue getMeta() {
        return this.meta;
    }

    public final ModifierValue getShortcut() {
        return this.shortcut;
    }

    protected KeyCombination(ModifierValue modifierValue, ModifierValue modifierValue2, ModifierValue modifierValue3, ModifierValue modifierValue4, ModifierValue modifierValue5) {
        if (modifierValue == null || modifierValue2 == null || modifierValue3 == null || modifierValue4 == null || modifierValue5 == null) {
            throw new NullPointerException("Modifier value must not be null!");
        }
        this.shift = modifierValue;
        this.control = modifierValue2;
        this.alt = modifierValue3;
        this.meta = modifierValue4;
        this.shortcut = modifierValue5;
    }

    protected KeyCombination(Modifier ... arrmodifier) {
        this(KeyCombination.getModifierValue(arrmodifier, KeyCode.SHIFT), KeyCombination.getModifierValue(arrmodifier, KeyCode.CONTROL), KeyCombination.getModifierValue(arrmodifier, KeyCode.ALT), KeyCombination.getModifierValue(arrmodifier, KeyCode.META), KeyCombination.getModifierValue(arrmodifier, KeyCode.SHORTCUT));
    }

    public boolean match(KeyEvent keyEvent) {
        KeyCode keyCode = Toolkit.getToolkit().getPlatformShortcutKey();
        return KeyCombination.test(KeyCode.SHIFT, this.shift, keyCode, this.shortcut, keyEvent.isShiftDown()) && KeyCombination.test(KeyCode.CONTROL, this.control, keyCode, this.shortcut, keyEvent.isControlDown()) && KeyCombination.test(KeyCode.ALT, this.alt, keyCode, this.shortcut, keyEvent.isAltDown()) && KeyCombination.test(KeyCode.META, this.meta, keyCode, this.shortcut, keyEvent.isMetaDown());
    }

    public String getName() {
        StringBuilder stringBuilder = new StringBuilder();
        this.addModifiersIntoString(stringBuilder);
        return stringBuilder.toString();
    }

    public String getDisplayText() {
        StringBuilder stringBuilder = new StringBuilder();
        if (PlatformUtil.isMac()) {
            if (this.getControl() == ModifierValue.DOWN) {
                stringBuilder.append("\u2303");
            }
            if (this.getAlt() == ModifierValue.DOWN) {
                stringBuilder.append("\u2325");
            }
            if (this.getShift() == ModifierValue.DOWN) {
                stringBuilder.append("\u21e7");
            }
            if (this.getMeta() == ModifierValue.DOWN || this.getShortcut() == ModifierValue.DOWN) {
                stringBuilder.append("\u2318");
            }
        } else {
            if (this.getControl() == ModifierValue.DOWN || this.getShortcut() == ModifierValue.DOWN) {
                stringBuilder.append("Ctrl+");
            }
            if (this.getAlt() == ModifierValue.DOWN) {
                stringBuilder.append("Alt+");
            }
            if (this.getShift() == ModifierValue.DOWN) {
                stringBuilder.append("Shift+");
            }
            if (this.getMeta() == ModifierValue.DOWN) {
                stringBuilder.append("Meta+");
            }
        }
        return stringBuilder.toString();
    }

    public boolean equals(Object object) {
        if (!(object instanceof KeyCombination)) {
            return false;
        }
        KeyCombination keyCombination = (KeyCombination)object;
        return this.shift == keyCombination.shift && this.control == keyCombination.control && this.alt == keyCombination.alt && this.meta == keyCombination.meta && this.shortcut == keyCombination.shortcut;
    }

    public int hashCode() {
        int n2 = 7;
        n2 = 23 * n2 + this.shift.hashCode();
        n2 = 23 * n2 + this.control.hashCode();
        n2 = 23 * n2 + this.alt.hashCode();
        n2 = 23 * n2 + this.meta.hashCode();
        n2 = 23 * n2 + this.shortcut.hashCode();
        return n2;
    }

    public String toString() {
        return this.getName();
    }

    public static KeyCombination valueOf(String string) {
        ArrayList<Modifier> arrayList = new ArrayList<Modifier>(4);
        String[] arrstring = KeyCombination.splitName(string);
        KeyCode keyCode = null;
        String string2 = null;
        for (String string3 : arrstring) {
            if (string3.length() > 2 && string3.charAt(0) == '\'' && string3.charAt(string3.length() - 1) == '\'') {
                if (keyCode != null || string2 != null) {
                    throw new IllegalArgumentException("Cannot parse key binding " + string);
                }
                string2 = string3.substring(1, string3.length() - 1).replace("\\'", "'");
                continue;
            }
            String string4 = KeyCombination.normalizeToken(string3);
            Modifier modifier = KeyCombination.getModifier(string4);
            if (modifier != null) {
                arrayList.add(modifier);
                continue;
            }
            if (keyCode != null || string2 != null) {
                throw new IllegalArgumentException("Cannot parse key binding " + string);
            }
            keyCode = KeyCode.getKeyCode(string4);
            if (keyCode != null) continue;
            string2 = string3;
        }
        if (keyCode == null && string2 == null) {
            throw new IllegalArgumentException("Cannot parse key binding " + string);
        }
        Object[] arrobject = arrayList.toArray(new Modifier[arrayList.size()]);
        return keyCode != null ? new KeyCodeCombination(keyCode, (Modifier[])arrobject) : new KeyCharacterCombination(string2, (Modifier[])arrobject);
    }

    public static KeyCombination keyCombination(String string) {
        return KeyCombination.valueOf(string);
    }

    private void addModifiersIntoString(StringBuilder stringBuilder) {
        KeyCombination.addModifierIntoString(stringBuilder, KeyCode.SHIFT, this.shift);
        KeyCombination.addModifierIntoString(stringBuilder, KeyCode.CONTROL, this.control);
        KeyCombination.addModifierIntoString(stringBuilder, KeyCode.ALT, this.alt);
        KeyCombination.addModifierIntoString(stringBuilder, KeyCode.META, this.meta);
        KeyCombination.addModifierIntoString(stringBuilder, KeyCode.SHORTCUT, this.shortcut);
    }

    private static void addModifierIntoString(StringBuilder stringBuilder, KeyCode keyCode, ModifierValue modifierValue) {
        if (modifierValue == ModifierValue.UP) {
            return;
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.append("+");
        }
        if (modifierValue == ModifierValue.ANY) {
            stringBuilder.append("Ignore ");
        }
        stringBuilder.append(keyCode.getName());
    }

    private static boolean test(KeyCode keyCode, ModifierValue modifierValue, KeyCode keyCode2, ModifierValue modifierValue2, boolean bl) {
        ModifierValue modifierValue3 = keyCode == keyCode2 ? KeyCombination.resolveModifierValue(modifierValue, modifierValue2) : modifierValue;
        return KeyCombination.test(modifierValue3, bl);
    }

    private static boolean test(ModifierValue modifierValue, boolean bl) {
        switch (modifierValue) {
            case DOWN: {
                return bl;
            }
            case UP: {
                return !bl;
            }
        }
        return true;
    }

    private static ModifierValue resolveModifierValue(ModifierValue modifierValue, ModifierValue modifierValue2) {
        if (modifierValue == ModifierValue.DOWN || modifierValue2 == ModifierValue.DOWN) {
            return ModifierValue.DOWN;
        }
        if (modifierValue == ModifierValue.ANY || modifierValue2 == ModifierValue.ANY) {
            return ModifierValue.ANY;
        }
        return ModifierValue.UP;
    }

    static Modifier getModifier(String string) {
        for (Modifier modifier : POSSIBLE_MODIFIERS) {
            if (!modifier.toString().equals(string)) continue;
            return modifier;
        }
        return null;
    }

    private static ModifierValue getModifierValue(Modifier[] arrmodifier, KeyCode keyCode) {
        ModifierValue modifierValue = ModifierValue.UP;
        for (Modifier modifier : arrmodifier) {
            if (modifier == null) {
                throw new NullPointerException("Modifier must not be null!");
            }
            if (modifier.getKey() != keyCode) continue;
            if (modifierValue != ModifierValue.UP) {
                throw new IllegalArgumentException(modifier.getValue() != modifierValue ? "Conflicting modifiers specified!" : "Duplicate modifiers specified!");
            }
            modifierValue = modifier.getValue();
        }
        return modifierValue;
    }

    private static String normalizeToken(String string) {
        String[] arrstring = string.split("\\s+");
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : arrstring) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(' ');
            }
            stringBuilder.append(string2.substring(0, 1).toUpperCase(Locale.ROOT));
            stringBuilder.append(string2.substring(1).toLowerCase(Locale.ROOT));
        }
        return stringBuilder.toString();
    }

    private static String[] splitName(String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        char[] arrc = string.trim().toCharArray();
        int n2 = 0;
        int n3 = 0;
        int n4 = -1;
        block24: for (int i2 = 0; i2 < arrc.length; ++i2) {
            char c2 = arrc[i2];
            switch (n2) {
                case 0: {
                    switch (c2) {
                        case '\t': 
                        case '\n': 
                        case '\u000b': 
                        case '\f': 
                        case '\r': 
                        case ' ': {
                            n4 = i2;
                            n2 = 1;
                            continue block24;
                        }
                        case '+': {
                            n4 = i2;
                            n2 = 2;
                            continue block24;
                        }
                        case '\'': {
                            if (i2 != 0 && arrc[i2 - 1] == '\\') continue block24;
                            n2 = 3;
                            continue block24;
                        }
                    }
                    continue block24;
                }
                case 1: {
                    switch (c2) {
                        case '\t': 
                        case '\n': 
                        case '\u000b': 
                        case '\f': 
                        case '\r': 
                        case ' ': {
                            continue block24;
                        }
                        case '+': {
                            n2 = 2;
                            continue block24;
                        }
                        case '\'': {
                            n2 = 3;
                            n4 = -1;
                            continue block24;
                        }
                    }
                    n2 = 0;
                    n4 = -1;
                    continue block24;
                }
                case 2: {
                    switch (c2) {
                        case '\t': 
                        case '\n': 
                        case '\u000b': 
                        case '\f': 
                        case '\r': 
                        case ' ': {
                            continue block24;
                        }
                        case '+': {
                            throw new IllegalArgumentException("Cannot parse key binding " + string);
                        }
                    }
                    if (n4 <= n3) {
                        throw new IllegalArgumentException("Cannot parse key binding " + string);
                    }
                    arrayList.add(new String(arrc, n3, n4 - n3));
                    n3 = i2;
                    n4 = -1;
                    n2 = c2 == '\'' ? 3 : 0;
                    continue block24;
                }
                case 3: {
                    if (c2 != '\'' || arrc[i2 - 1] == '\\') continue block24;
                    n2 = 0;
                }
            }
        }
        switch (n2) {
            case 0: 
            case 1: {
                arrayList.add(new String(arrc, n3, arrc.length - n3));
                break;
            }
            case 2: 
            case 3: {
                throw new IllegalArgumentException("Cannot parse key binding " + string);
            }
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }

    public static enum ModifierValue {
        DOWN,
        UP,
        ANY;

    }

    public static final class Modifier {
        private final KeyCode key;
        private final ModifierValue value;

        private Modifier(KeyCode keyCode, ModifierValue modifierValue) {
            this.key = keyCode;
            this.value = modifierValue;
        }

        public KeyCode getKey() {
            return this.key;
        }

        public ModifierValue getValue() {
            return this.value;
        }

        public String toString() {
            return (this.value == ModifierValue.ANY ? "Ignore " : "") + this.key.getName();
        }
    }
}

