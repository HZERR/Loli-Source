/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.text;

import org.pushingpixels.lafwidget.utils.LafConstants;

public interface PasswordStrengthChecker {
    public LafConstants.PasswordStrength getStrength(char[] var1);

    public String getDescription(LafConstants.PasswordStrength var1);
}

