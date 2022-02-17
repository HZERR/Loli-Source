/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.types.DataType
 */
package org.pushingpixels.lafwidget.ant;

import org.apache.tools.ant.types.DataType;

public class UiDelegateType
extends DataType {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

