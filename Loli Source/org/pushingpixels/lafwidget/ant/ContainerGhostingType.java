/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.types.DataType
 */
package org.pushingpixels.lafwidget.ant;

import org.apache.tools.ant.types.DataType;

public class ContainerGhostingType
extends DataType {
    private String className;
    private boolean toInjectAfterOriginal;

    public void setClassName(String name) {
        this.className = name;
    }

    public String getClassName() {
        return this.className;
    }

    public boolean isToInjectAfterOriginal() {
        return this.toInjectAfterOriginal;
    }

    public void setToInjectAfterOriginal(boolean toInjectAfterOriginal) {
        this.toInjectAfterOriginal = toInjectAfterOriginal;
    }
}

