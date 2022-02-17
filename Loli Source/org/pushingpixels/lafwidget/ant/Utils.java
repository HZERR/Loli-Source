/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.ant;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.pushingpixels.lafwidget.ant.AugmentException;

public class Utils {
    protected Map<String, String> lafMap = new HashMap<String, String>();
    protected static Utils instance = new Utils();
    public static final String[] UI_IDS = new String[]{"ButtonUI", "CheckBoxUI", "CheckBoxMenuItemUI", "ColorChooserUI", "ComboBoxUI", "DesktopIconUI", "DesktopPaneUI", "EditorPaneUI", "FormattedTextFieldUI", "InternalFrameUI", "LabelUI", "ListUI", "MenuUI", "MenuBarUI", "MenuItemUI", "OptionPaneUI", "PanelUI", "PasswordFieldUI", "PopupMenuUI", "PopupMenuSeparatorUI", "ProgressBarUI", "RadioButtonUI", "RadioButtonMenuItemUI", "RootPaneUI", "ScrollBarUI", "ScrollPaneUI", "SplitPaneUI", "SliderUI", "SeparatorUI", "SpinnerUI", "ToolBarSeparatorUI", "TabbedPaneUI", "TableUI", "TableHeaderUI", "TextAreaUI", "TextFieldUI", "TextPaneUI", "ToggleButtonUI", "ToolBarUI", "ToolTipUI", "TreeUI", "ViewportUI"};

    private Utils() {
        this.lafMap.put(BasicLookAndFeel.class.getName(), "javax.swing.plaf.basic.Basic");
        this.lafMap.put(MetalLookAndFeel.class.getName(), "javax.swing.plaf.metal.Metal");
    }

    public static Utils getUtils() {
        return instance;
    }

    public String getUIDelegate(String uiKey, String lafClassName) {
        try {
            lafClassName = lafClassName.replace('/', '.');
            return this.getUIDelegate(uiKey, Class.forName(lafClassName));
        }
        catch (ClassNotFoundException cnfe) {
            throw new AugmentException("Class '" + lafClassName + "' not found", cnfe);
        }
    }

    public String getUIDelegate(String uiKey, Class<?> origLafClazz) {
        for (Class<?> lafClazz = origLafClazz; lafClazz != null; lafClazz = lafClazz.getSuperclass()) {
            String prefix = this.lafMap.get(lafClazz.getName());
            if (prefix == null) continue;
            String fullClassName = prefix + uiKey;
            Class<?> delegateClazz = null;
            try {
                delegateClazz = Class.forName(fullClassName);
            }
            catch (ClassNotFoundException cnfe) {
                // empty catch block
            }
            if (delegateClazz == null) continue;
            return fullClassName;
        }
        throw new AugmentException("No match for '" + uiKey + "' in '" + origLafClazz.getName() + "' hierarchy");
    }

    public static String getTypeDesc(Class<?> clazz) {
        if (clazz.isArray()) {
            return "[" + Utils.getTypeDesc(clazz.getComponentType());
        }
        if (clazz == Void.TYPE) {
            return "V";
        }
        if (clazz == Boolean.TYPE) {
            return "Z";
        }
        if (clazz == Byte.TYPE) {
            return "B";
        }
        if (clazz == Character.TYPE) {
            return "C";
        }
        if (clazz == Short.TYPE) {
            return "S";
        }
        if (clazz == Integer.TYPE) {
            return "I";
        }
        if (clazz == Long.TYPE) {
            return "J";
        }
        if (clazz == Float.TYPE) {
            return "F";
        }
        if (clazz == Double.TYPE) {
            return "D";
        }
        return "L" + clazz.getName().replace('.', '/') + ";";
    }

    public static String getMethodDesc(Method method) {
        StringBuffer result = new StringBuffer();
        result.append("(");
        Class<?>[] paramClasses = method.getParameterTypes();
        for (int i2 = 0; i2 < paramClasses.length; ++i2) {
            Class<?> paramClass = paramClasses[i2];
            result.append(Utils.getTypeDesc(paramClass));
        }
        result.append(")");
        result.append(Utils.getTypeDesc(method.getReturnType()));
        return result.toString();
    }

    public static void main(String[] args) {
        for (Map.Entry<String, String> entry : Utils.instance.lafMap.entrySet()) {
            String lafClassName = entry.getKey();
            System.out.println(lafClassName);
            String prefix = entry.getValue();
            for (int i2 = 0; i2 < UI_IDS.length; ++i2) {
                String uiClassName = prefix + UI_IDS[i2];
                try {
                    Class<?> uiClazz = Class.forName(uiClassName);
                    System.out.println("\t" + UI_IDS[i2]);
                    Constructor<?>[] ctrs = uiClazz.getDeclaredConstructors();
                    for (int j2 = 0; j2 < ctrs.length; ++j2) {
                        Constructor<?> ctr = ctrs[j2];
                        Class<?>[] ctrArgs = ctr.getParameterTypes();
                        System.out.print("\t\t" + ctrArgs.length + " args : ");
                        for (int k2 = 0; k2 < ctrArgs.length; ++k2) {
                            System.out.print(ctrArgs[k2].getName() + ",");
                        }
                        System.out.println();
                    }
                    continue;
                }
                catch (ClassNotFoundException cnfe) {
                    continue;
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}

