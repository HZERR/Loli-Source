/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.mac.SystemB;
import com.sun.jna.platform.unix.LibCAPI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.O0iIl1ilaND;
import loliland.launcher.client.O1I01lLANd;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.iOl10IlLAnd;
import loliland.launcher.client.lI110lIIlaND;
import loliland.launcher.client.lIOOOlIlaNd;
import loliland.launcher.client.lIiIii1LAnD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.liiIILaND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IIl111lAnD
extends O1I01lLANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(IIl111lAnD.class);
    private static final int OOOIilanD = 4;
    private static final int lI00OlAND = 1;
    private static final int lli0OiIlAND = 2;
    private static final int li0iOILAND = 3;
    private static final int O1il1llOLANd = 4;
    private static final int Oill1LAnD = 5;
    private static final int lIOILand = 6;
    private int lil0liLand;
    private Supplier iilIi1laND = lii1IO0LaNd.I1O1I1LaNd(this::ll1ILAnd);
    private String lli011lLANd = "";
    private String l0illAND = "";
    private String IO11O0LANd;
    private String l11lLANd;
    private String lO110l1LANd;
    private String l0iIlIO1laNd;
    private String iOIl0LAnD;
    private OOOOO10iLAND iIiO00OLaNd = OOOOO10iLAND.lIOILand;
    private int ii1li00Land;
    private int IOI1LaNd;
    private int lI00ilAND;
    private long l0l00lAND;
    private long iOl10IlLAnd;
    private long lIiIii1LAnD;
    private long II1Iland;
    private long l0IO0LAnd;
    private long liOIOOLANd;
    private long II1i1l0laND;
    private long OOOliOOllANd;
    private long IiiilAnD;
    private int ll1ILAnd;
    private long l1i00lLAnD;
    private long lili0l0laNd;
    private long liIIllIland;

    public IIl111lAnD(int n2, int n3) {
        super(n2);
        this.lil0liLand = n3;
        this.l0IO0LAnd();
    }

    @Override
    public String lI00OlAND() {
        return this.lli011lLANd;
    }

    @Override
    public String lli0OiIlAND() {
        return this.l0illAND;
    }

    @Override
    public String li0iOILAND() {
        return (String)this.iilIi1laND.get();
    }

    private String ll1ILAnd() {
        O0iIl1ilaND o0iIl1ilaND;
        int[] arrn = new int[]{1, 49, this.I1O1I1LaNd()};
        int n2 = liiIILaND.I1O1I1LaNd("kern.argmax", 0);
        Memory memory = new Memory(n2);
        if (0 != lIOOOlIlaNd.INSTANCE.sysctl(arrn, arrn.length, (Pointer)memory, o0iIl1ilaND = new O0iIl1ilaND(new LibCAPI.size_t((long)n2)), null, LibCAPI.size_t.ZERO)) {
            I1O1I1LaNd.warn("Failed syctl call for process arguments (kern.procargs2), process {} may not exist. Error code: {}", (Object)this.I1O1I1LaNd(), (Object)Native.getLastError());
            return "";
        }
        int n3 = ((Pointer)memory).getInt(0L);
        if (n3 < 0 || n3 > 1024) {
            I1O1I1LaNd.error("Nonsensical number of process arguments for pid {}: {}", (Object)this.I1O1I1LaNd(), (Object)n3);
            return "";
        }
        ArrayList<String> arrayList = new ArrayList<String>(n3);
        long l2 = SystemB.INT_SIZE;
        l2 += (long)memory.getString(l2).length();
        while (n3-- > 0 && l2 < o0iIl1ilaND.getValue().longValue()) {
            while (((Pointer)memory).getByte(l2) == 0 && ++l2 < o0iIl1ilaND.getValue().longValue()) {
            }
            String string = memory.getString(l2);
            arrayList.add(string);
            l2 += (long)string.length();
        }
        return String.join((CharSequence)"\u0000", arrayList);
    }

    @Override
    public String O1il1llOLANd() {
        return this.IO11O0LANd;
    }

    @Override
    public String Oill1LAnD() {
        return this.l11lLANd;
    }

    @Override
    public String lIOILand() {
        return this.lO110l1LANd;
    }

    @Override
    public String lil0liLand() {
        return this.l0iIlIO1laNd;
    }

    @Override
    public String iilIi1laND() {
        return this.iOIl0LAnD;
    }

    @Override
    public OOOOO10iLAND lli011lLANd() {
        return this.iIiO00OLaNd;
    }

    @Override
    public int l0illAND() {
        return this.ii1li00Land;
    }

    @Override
    public int IO11O0LANd() {
        return this.IOI1LaNd;
    }

    @Override
    public List liOIOOLANd() {
        long l2 = System.currentTimeMillis();
        ArrayList<lI110lIIlaND> arrayList = new ArrayList<lI110lIIlaND>();
        List list = loliland.launcher.client.iOl10IlLAnd.I1O1I1LaNd(this.I1O1I1LaNd());
        for (lIiIii1LAnD lIiIii1LAnD2 : list) {
            long l3 = l2 - lIiIii1LAnD2.lli0OiIlAND();
            if (l3 < this.IOI1LaNd()) {
                l3 = this.IOI1LaNd();
            }
            arrayList.add(new lI110lIIlaND(this.I1O1I1LaNd(), lIiIii1LAnD2.I1O1I1LaNd(), lIiIii1LAnD2.li0iOILAND(), lIiIii1LAnD2.lI00OlAND(), lIiIii1LAnD2.OOOIilanD(), l3, l2 - l3, lIiIii1LAnD2.O1il1llOLANd()));
        }
        return arrayList;
    }

    @Override
    public int l11lLANd() {
        return this.lI00ilAND;
    }

    @Override
    public long lO110l1LANd() {
        return this.l0l00lAND;
    }

    @Override
    public long l0iIlIO1laNd() {
        return this.iOl10IlLAnd;
    }

    @Override
    public long iOIl0LAnD() {
        return this.lIiIii1LAnD;
    }

    @Override
    public long iIiO00OLaNd() {
        return this.II1Iland;
    }

    @Override
    public long ii1li00Land() {
        return this.liOIOOLANd;
    }

    @Override
    public long IOI1LaNd() {
        return this.l0IO0LAnd;
    }

    @Override
    public long lI00ilAND() {
        return this.II1i1l0laND;
    }

    @Override
    public long l0l00lAND() {
        return this.OOOliOOllANd;
    }

    @Override
    public long iOl10IlLAnd() {
        return this.IiiilAnD;
    }

    @Override
    public int lIiIii1LAnD() {
        return this.ll1ILAnd;
    }

    @Override
    public long II1Iland() {
        int n2 = liiIILaND.I1O1I1LaNd("hw.logicalcpu", 1);
        return n2 < 64 ? (1L << n2) - 1L : -1L;
    }

    @Override
    public long II1i1l0laND() {
        return this.l1i00lLAnD;
    }

    @Override
    public long OOOliOOllANd() {
        return this.lili0l0laNd;
    }

    @Override
    public long IiiilAnD() {
        return this.liIIllIland;
    }

    @Override
    public boolean l0IO0LAnd() {
        Structure structure;
        Object object;
        long l2 = System.currentTimeMillis();
        SystemB.ProcTaskAllInfo procTaskAllInfo = new SystemB.ProcTaskAllInfo();
        if (0 > SystemB.INSTANCE.proc_pidinfo(this.I1O1I1LaNd(), 2, 0L, procTaskAllInfo, procTaskAllInfo.size()) || procTaskAllInfo.ptinfo.pti_threadnum < 1) {
            this.iIiO00OLaNd = OOOOO10iLAND.lIOILand;
            return false;
        }
        Memory memory = new Memory(4096L);
        if (0 < SystemB.INSTANCE.proc_pidpath(this.I1O1I1LaNd(), memory, 4096)) {
            this.l0illAND = memory.getString(0L).trim();
            object = this.l0illAND.split("/");
            if (((String[])object).length > 0) {
                this.lli011lLANd = object[((String[])object).length - 1];
            }
        }
        if (this.lli011lLANd.isEmpty()) {
            this.lli011lLANd = Native.toString(procTaskAllInfo.pbsd.pbi_comm, StandardCharsets.UTF_8);
        }
        switch (procTaskAllInfo.pbsd.pbi_status) {
            case 1: {
                this.iIiO00OLaNd = OOOOO10iLAND.lI00OlAND;
                break;
            }
            case 2: {
                this.iIiO00OLaNd = OOOOO10iLAND.lli0OiIlAND;
                break;
            }
            case 3: {
                this.iIiO00OLaNd = OOOOO10iLAND.OOOIilanD;
                break;
            }
            case 4: {
                this.iIiO00OLaNd = OOOOO10iLAND.I1O1I1LaNd;
                break;
            }
            case 5: {
                this.iIiO00OLaNd = OOOOO10iLAND.li0iOILAND;
                break;
            }
            case 6: {
                this.iIiO00OLaNd = OOOOO10iLAND.O1il1llOLANd;
                break;
            }
            default: {
                this.iIiO00OLaNd = OOOOO10iLAND.Oill1LAnD;
            }
        }
        this.ii1li00Land = procTaskAllInfo.pbsd.pbi_ppid;
        this.lO110l1LANd = Integer.toString(procTaskAllInfo.pbsd.pbi_uid);
        object = SystemB.INSTANCE.getpwuid(procTaskAllInfo.pbsd.pbi_uid);
        if (object != null) {
            this.l11lLANd = object.pw_name;
        }
        this.iOIl0LAnD = Integer.toString(procTaskAllInfo.pbsd.pbi_gid);
        SystemB.Group group = SystemB.INSTANCE.getgrgid(procTaskAllInfo.pbsd.pbi_gid);
        if (group != null) {
            this.l0iIlIO1laNd = group.gr_name;
        }
        this.IOI1LaNd = procTaskAllInfo.ptinfo.pti_threadnum;
        this.lI00ilAND = procTaskAllInfo.ptinfo.pti_priority;
        this.l0l00lAND = procTaskAllInfo.ptinfo.pti_virtual_size;
        this.iOl10IlLAnd = procTaskAllInfo.ptinfo.pti_resident_size;
        this.lIiIii1LAnD = procTaskAllInfo.ptinfo.pti_total_system / 1000000L;
        this.II1Iland = procTaskAllInfo.ptinfo.pti_total_user / 1000000L;
        this.l0IO0LAnd = procTaskAllInfo.pbsd.pbi_start_tvsec * 1000L + procTaskAllInfo.pbsd.pbi_start_tvusec / 1000L;
        this.liOIOOLANd = l2 - this.l0IO0LAnd;
        this.IiiilAnD = procTaskAllInfo.pbsd.pbi_nfiles;
        this.ll1ILAnd = (procTaskAllInfo.pbsd.pbi_flags & 4) == 0 ? 32 : 64;
        this.lili0l0laNd = procTaskAllInfo.ptinfo.pti_pageins;
        this.l1i00lLAnD = procTaskAllInfo.ptinfo.pti_faults - procTaskAllInfo.ptinfo.pti_pageins;
        this.liIIllIland = procTaskAllInfo.ptinfo.pti_csw;
        if (this.lil0liLand >= 9) {
            structure = new SystemB.RUsageInfoV2();
            if (0 == SystemB.INSTANCE.proc_pid_rusage(this.I1O1I1LaNd(), 2, (SystemB.RUsageInfoV2)structure)) {
                this.II1i1l0laND = ((SystemB.RUsageInfoV2)structure).ri_diskio_bytesread;
                this.OOOliOOllANd = ((SystemB.RUsageInfoV2)structure).ri_diskio_byteswritten;
            }
        }
        structure = new SystemB.VnodePathInfo();
        if (0 < SystemB.INSTANCE.proc_pidinfo(this.I1O1I1LaNd(), 9, 0L, structure, structure.size())) {
            this.IO11O0LANd = Native.toString(structure.pvi_cdir.vip_path, StandardCharsets.US_ASCII);
        }
        return true;
    }
}

