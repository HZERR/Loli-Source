/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafplugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.swing.UIManager;
import org.pushingpixels.lafplugin.XMLElement;

public class PluginManager {
    private String mainTag;
    private String pluginTag;
    private String xmlName;
    private Set plugins;

    public PluginManager(String xmlName, String mainTag, String pluginTag) {
        this.xmlName = xmlName;
        this.mainTag = mainTag;
        this.pluginTag = pluginTag;
        this.plugins = null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected String getPluginClass(URL pluginUrl) {
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            XMLElement child;
            XMLElement xml = new XMLElement();
            is = pluginUrl.openStream();
            isr = new InputStreamReader(is);
            xml.parseFromReader(isr);
            if (!this.mainTag.equals(xml.getName())) {
                String string = null;
                return string;
            }
            Enumeration children = xml.enumerateChildren();
            while (children.hasMoreElements()) {
                child = (XMLElement)children.nextElement();
                if (!this.pluginTag.equals(child.getName())) continue;
                if (child.countChildren() != 0) {
                    String string = null;
                    return string;
                }
                String string = child.getContent();
                return string;
            }
            child = null;
            return child;
        }
        catch (Exception exc) {
            String string = null;
            return string;
        }
        finally {
            if (isr != null) {
                try {
                    isr.close();
                }
                catch (Exception e2) {}
            }
            if (is != null) {
                try {
                    is.close();
                }
                catch (Exception e3) {}
            }
        }
    }

    protected Object getPlugin(URL pluginUrl) throws Exception {
        Class<?> pluginClass;
        String pluginClassName = this.getPluginClass(pluginUrl);
        if (pluginClassName == null) {
            return null;
        }
        ClassLoader classLoader = (ClassLoader)UIManager.get("ClassLoader");
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        if ((pluginClass = Class.forName(pluginClassName, true, classLoader)) == null) {
            return null;
        }
        Object pluginInstance = pluginClass.newInstance();
        if (pluginInstance == null) {
            return null;
        }
        return pluginInstance;
    }

    public Set getAvailablePlugins() {
        return this.getAvailablePlugins(false);
    }

    public Set getAvailablePlugins(boolean toReload) {
        if (!toReload && this.plugins != null) {
            return this.plugins;
        }
        this.plugins = new HashSet();
        ClassLoader cl = (ClassLoader)UIManager.get("ClassLoader");
        if (cl == null) {
            cl = Thread.currentThread().getContextClassLoader();
        }
        try {
            Enumeration<URL> urls = cl.getResources(this.xmlName);
            while (urls.hasMoreElements()) {
                URL pluginUrl = urls.nextElement();
                Object pluginInstance = this.getPlugin(pluginUrl);
                if (pluginInstance == null) continue;
                this.plugins.add(pluginInstance);
            }
        }
        catch (Exception exc) {
            return null;
        }
        return this.plugins;
    }
}

