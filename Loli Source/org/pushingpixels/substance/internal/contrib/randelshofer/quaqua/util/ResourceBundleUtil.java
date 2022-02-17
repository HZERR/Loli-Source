/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class ResourceBundleUtil {
    private ResourceBundle resource;

    public ResourceBundleUtil(ResourceBundle r2) {
        this.resource = r2;
    }

    public String getString(String key) {
        try {
            return this.resource.getString(key);
        }
        catch (MissingResourceException e2) {
            return '-' + key + '-';
        }
    }

    public ImageIcon getImageIcon(String key, Class baseClass) {
        try {
            String rsrcName = this.resource.getString(key);
            if (rsrcName.equals("")) {
                return null;
            }
            URL url = baseClass.getResource(rsrcName);
            return url == null ? null : new ImageIcon(url);
        }
        catch (MissingResourceException e2) {
            return null;
        }
    }

    public char getMnemonic(String key) {
        String s2 = this.resource.getString(key);
        return s2 == null || s2.length() == 0 ? (char)'\u0000' : s2.charAt(0);
    }

    public char getMnem(String key) {
        String s2 = this.resource.getString(key + "Mnem");
        return s2 == null || s2.length() == 0 ? (char)'\u0000' : s2.charAt(0);
    }

    public KeyStroke getKeyStroke(String key) {
        KeyStroke ks = null;
        try {
            String s2 = this.resource.getString(key);
            ks = s2 == null ? (KeyStroke)null : KeyStroke.getKeyStroke(s2);
        }
        catch (NoSuchElementException noSuchElementException) {
            // empty catch block
        }
        return ks;
    }

    public KeyStroke getAcc(String key) {
        KeyStroke ks = null;
        try {
            String s2 = this.resource.getString(key + "Acc");
            ks = s2 == null ? (KeyStroke)null : KeyStroke.getKeyStroke(s2);
        }
        catch (NoSuchElementException noSuchElementException) {
            // empty catch block
        }
        return ks;
    }

    public String getFormatted(String key, Object argument) {
        return MessageFormat.format(this.resource.getString(key), argument);
    }

    public String getFormatted(String key, Object[] arguments) {
        return MessageFormat.format(this.resource.getString(key), arguments);
    }

    public Locale getLocale() {
        return this.resource.getLocale();
    }

    public static ResourceBundleUtil getBundle(String baseName) throws MissingResourceException {
        return new ResourceBundleUtil(ResourceBundle.getBundle(baseName, Locale.getDefault()));
    }

    public static ResourceBundleUtil getLAFBundle(String baseName) throws MissingResourceException {
        return ResourceBundleUtil.getLAFBundle(baseName, Locale.getDefault());
    }

    public static ResourceBundleUtil getLAFBundle(String baseName, Locale locale) throws MissingResourceException {
        ResourceBundleUtil r2;
        try {
            r2 = new ResourceBundleUtil(ResourceBundle.getBundle(baseName + "_" + UIManager.getLookAndFeel().getID(), locale));
        }
        catch (MissingResourceException e2) {
            r2 = new ResourceBundleUtil(ResourceBundle.getBundle(baseName, locale));
        }
        return r2;
    }
}

