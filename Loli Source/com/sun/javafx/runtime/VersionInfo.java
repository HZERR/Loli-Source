/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.runtime;

public class VersionInfo {
    private static final String BUILD_TIMESTAMP = "Tue Mar 14 20:41:39 PDT 2017";
    private static final String HUDSON_JOB_NAME = "8u131";
    private static final String HUDSON_BUILD_NUMBER = "23";
    private static final String PROMOTED_BUILD_NUMBER = "11";
    private static final String PRODUCT_NAME = "Java(TM)";
    private static final String RAW_VERSION = "8.0.131";
    private static final String RELEASE_MILESTONE = "fcs";
    private static final String RELEASE_NAME = "8u131";
    private static final String VERSION;
    private static final String RUNTIME_VERSION;

    public static synchronized void setupSystemProperties() {
        if (System.getProperty("javafx.version") == null) {
            System.setProperty("javafx.version", VersionInfo.getVersion());
            System.setProperty("javafx.runtime.version", VersionInfo.getRuntimeVersion());
        }
    }

    public static String getBuildTimestamp() {
        return BUILD_TIMESTAMP;
    }

    public static String getHudsonJobName() {
        if ("8u131".equals("not_hudson")) {
            return "";
        }
        return "8u131";
    }

    public static String getHudsonBuildNumber() {
        return HUDSON_BUILD_NUMBER;
    }

    public static String getReleaseMilestone() {
        if (RELEASE_MILESTONE.equals(RELEASE_MILESTONE)) {
            return "";
        }
        return RELEASE_MILESTONE;
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getRuntimeVersion() {
        return RUNTIME_VERSION;
    }

    static {
        String string = RAW_VERSION;
        if (VersionInfo.getReleaseMilestone().length() > 0) {
            string = string + "-fcs";
        }
        VERSION = string;
        string = VersionInfo.getHudsonJobName().length() > 0 ? string + "-b11" : string + " (Tue Mar 14 20:41:39 PDT 2017)";
        RUNTIME_VERSION = string;
    }
}

