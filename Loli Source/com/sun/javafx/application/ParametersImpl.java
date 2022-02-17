/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;

public class ParametersImpl
extends Application.Parameters {
    private List<String> rawArgs = new ArrayList<String>();
    private Map<String, String> namedParams = new HashMap<String, String>();
    private List<String> unnamedParams = new ArrayList<String>();
    private List<String> readonlyRawArgs = null;
    private Map<String, String> readonlyNamedParams = null;
    private List<String> readonlyUnnamedParams = null;
    private static Map<Application, Application.Parameters> params = new HashMap<Application, Application.Parameters>();

    public ParametersImpl() {
    }

    public ParametersImpl(List<String> list) {
        if (list != null) {
            this.init(list);
        }
    }

    public ParametersImpl(String[] arrstring) {
        if (arrstring != null) {
            this.init(Arrays.asList(arrstring));
        }
    }

    public ParametersImpl(Map map, String[] arrstring) {
        this.init(map, arrstring);
    }

    private void init(List<String> list) {
        for (String string : list) {
            if (string == null) continue;
            this.rawArgs.add(string);
        }
        this.computeNamedParams();
        this.computeUnnamedParams();
    }

    private void init(Map map, String[] arrstring) {
        for (Map.Entry entry : map.entrySet()) {
            Object object;
            Object k2 = entry.getKey();
            if (!this.validKey(k2) || !((object = map.get(k2)) instanceof String)) continue;
            this.namedParams.put((String)k2, (String)object);
        }
        this.computeRawArgs();
        if (arrstring != null) {
            for (String string : arrstring) {
                this.unnamedParams.add(string);
                this.rawArgs.add(string);
            }
        }
    }

    private boolean validFirstChar(char c2) {
        return Character.isLetter(c2) || c2 == '_';
    }

    private boolean validKey(Object object) {
        String string;
        if (object instanceof String && (string = (String)object).length() > 0 && string.indexOf(61) < 0) {
            return this.validFirstChar(string.charAt(0));
        }
        return false;
    }

    private boolean isNamedParam(String string) {
        if (string.startsWith("--")) {
            return string.indexOf(61) > 2 && this.validFirstChar(string.charAt(2));
        }
        return false;
    }

    private void computeUnnamedParams() {
        for (String string : this.rawArgs) {
            if (this.isNamedParam(string)) continue;
            this.unnamedParams.add(string);
        }
    }

    private void computeNamedParams() {
        for (String string : this.rawArgs) {
            if (!this.isNamedParam(string)) continue;
            int n2 = string.indexOf(61);
            String string2 = string.substring(2, n2);
            String string3 = string.substring(n2 + 1);
            this.namedParams.put(string2, string3);
        }
    }

    private void computeRawArgs() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(this.namedParams.keySet());
        Collections.sort(arrayList);
        for (String string : arrayList) {
            this.rawArgs.add("--" + string + "=" + this.namedParams.get(string));
        }
    }

    @Override
    public List<String> getRaw() {
        if (this.readonlyRawArgs == null) {
            this.readonlyRawArgs = Collections.unmodifiableList(this.rawArgs);
        }
        return this.readonlyRawArgs;
    }

    @Override
    public Map<String, String> getNamed() {
        if (this.readonlyNamedParams == null) {
            this.readonlyNamedParams = Collections.unmodifiableMap(this.namedParams);
        }
        return this.readonlyNamedParams;
    }

    @Override
    public List<String> getUnnamed() {
        if (this.readonlyUnnamedParams == null) {
            this.readonlyUnnamedParams = Collections.unmodifiableList(this.unnamedParams);
        }
        return this.readonlyUnnamedParams;
    }

    public static Application.Parameters getParameters(Application application) {
        Application.Parameters parameters = params.get(application);
        return parameters;
    }

    public static void registerParameters(Application application, Application.Parameters parameters) {
        params.put(application, parameters);
    }
}

