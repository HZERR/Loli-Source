/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbBase;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Variant;

public class TlbInterface
extends TlbBase {
    public TlbInterface(int index, String packagename, TypeLibUtil typeLibUtil) {
        super(index, typeLibUtil, null);
        TypeLibUtil.TypeLibDoc typeLibDoc = this.typeLibUtil.getDocumentation(index);
        String docString = typeLibDoc.getDocString();
        if (typeLibDoc.getName().length() > 0) {
            this.name = typeLibDoc.getName();
        }
        this.logInfo("Type of kind 'Interface' found: " + this.name);
        this.createPackageName(packagename);
        this.createClassName(this.name);
        this.setFilename(this.name);
        TypeInfoUtil typeInfoUtil = typeLibUtil.getTypeInfoUtil(index);
        OaIdl.TYPEATTR typeAttr = typeInfoUtil.getTypeAttr();
        this.createJavaDocHeader(typeAttr.guid.toGuidString(), docString);
        int cVars = typeAttr.cVars.intValue();
        for (int i2 = 0; i2 < cVars; ++i2) {
            OaIdl.VARDESC varDesc = typeInfoUtil.getVarDesc(i2);
            Variant.VARIANT.ByReference constValue = varDesc._vardesc.lpvarValue;
            Object value = constValue.getValue();
            OaIdl.MEMBERID memberID = varDesc.memid;
            TypeInfoUtil.TypeInfoDoc typeInfoDoc2 = typeInfoUtil.getDocumentation(memberID);
            this.content = this.content + "\t\t//" + typeInfoDoc2.getName() + "\n";
            this.content = this.content + "\t\tpublic static final int " + typeInfoDoc2.getName() + " = " + value.toString() + ";";
            if (i2 >= cVars - 1) continue;
            this.content = this.content + "\n";
        }
        this.createContent(this.content);
    }

    protected void createJavaDocHeader(String guid, String helpstring) {
        this.replaceVariable("uuid", guid);
        this.replaceVariable("helpstring", helpstring);
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbInterface.template";
    }
}

