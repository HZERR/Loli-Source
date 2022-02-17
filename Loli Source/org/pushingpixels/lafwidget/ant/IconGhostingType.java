/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.types.DataType
 */
package org.pushingpixels.lafwidget.ant;

import org.apache.tools.ant.types.DataType;

public class IconGhostingType
extends DataType {
    private String className;
    private String methodName;

    public void setClassName(String name) {
        this.className = name;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}

