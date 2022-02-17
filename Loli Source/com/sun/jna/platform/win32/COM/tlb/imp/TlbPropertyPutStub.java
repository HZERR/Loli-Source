/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbAbstractMethod;
import com.sun.jna.platform.win32.OaIdl;

public class TlbPropertyPutStub
extends TlbAbstractMethod {
    public TlbPropertyPutStub(int index, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC funcDesc, TypeInfoUtil typeInfoUtil) {
        super(index, typeLibUtil, funcDesc, typeInfoUtil);
        TypeInfoUtil.TypeInfoDoc typeInfoDoc = typeInfoUtil.getDocumentation(funcDesc.memid);
        String docStr = typeInfoDoc.getDocString();
        String methodname = "set" + typeInfoDoc.getName();
        String[] names = typeInfoUtil.getNames(funcDesc.memid, this.paramCount + 1);
        for (int i2 = 0; i2 < this.paramCount; ++i2) {
            OaIdl.ELEMDESC elemdesc = funcDesc.lprgelemdescParam.elemDescArg[i2];
            String varType = this.getType(elemdesc);
            this.methodparams = this.methodparams + varType + " " + this.replaceJavaKeyword(names[i2].toLowerCase());
            if (i2 >= this.paramCount - 1) continue;
            this.methodparams = this.methodparams + ", ";
        }
        this.replaceVariable("helpstring", docStr);
        this.replaceVariable("methodname", methodname);
        this.replaceVariable("methodparams", this.methodparams);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyPutStub.template";
    }
}

