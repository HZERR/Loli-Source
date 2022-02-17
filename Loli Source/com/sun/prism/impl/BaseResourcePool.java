/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.ManagedResource;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ResourcePool;
import java.lang.ref.WeakReference;

public abstract class BaseResourcePool<T>
implements ResourcePool<T> {
    private static final int FOREVER = 1024;
    private static final int RECENTLY_USEFUL = 100;
    private static final int RECENT = 10;
    private static final Predicate[] stageTesters = new Predicate[6];
    private static final String[] stageReasons = new String[6];
    long managedSize;
    final long origTarget;
    long curTarget;
    final long maxSize;
    final ResourcePool<T> sharedParent;
    private final Thread managerThread;
    private WeakLinkedList<T> resourceHead = new WeakLinkedList();

    protected BaseResourcePool(long l2, long l3) {
        this(null, l2, l3);
    }

    protected BaseResourcePool(ResourcePool<T> resourcePool) {
        this(resourcePool, resourcePool.target(), resourcePool.max());
    }

    protected BaseResourcePool(ResourcePool<T> resourcePool, long l2, long l3) {
        this.sharedParent = resourcePool;
        this.origTarget = this.curTarget = l2;
        this.maxSize = resourcePool == null ? l3 : Math.min(resourcePool.max(), l3);
        this.managerThread = Thread.currentThread();
    }

    /*
     * Exception decompiling
     */
    public boolean cleanup(long var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 6[FORLOOP]
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:429)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:478)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         * async.DecompilerRunnable.cfrDecompilation(DecompilerRunnable.java:259)
         * async.DecompilerRunnable.call(DecompilerRunnable.java:220)
         * async.DecompilerRunnable.call(DecompilerRunnable.java:26)
         * java.util.concurrent.FutureTask.run(FutureTask.java:266)
         * java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
         * java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
         * java.lang.Thread.run(Thread.java:748)
         */
        throw new IllegalStateException(Decompilation failed);
    }

    private void pruneLastChance(boolean bl) {
        System.gc();
        if (bl) {
            try {
                Thread.sleep(20L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        Disposer.cleanUp();
        if (PrismSettings.poolDebug) {
            if (bl) {
                System.err.print("Last chance pruning");
            } else {
                System.err.print("Pruning everything");
            }
            System.err.println(" in pool: " + this);
        }
        this.cleanup(managedResource -> true);
    }

    private void cleanup(Predicate predicate) {
        WeakLinkedList<T> weakLinkedList = this.resourceHead;
        WeakLinkedList weakLinkedList2 = weakLinkedList.next;
        while (weakLinkedList2 != null) {
            ManagedResource managedResource = weakLinkedList2.getResource();
            if (ManagedResource._isgone(managedResource)) {
                if (PrismSettings.poolDebug) {
                    BaseResourcePool.showLink("unlinking", weakLinkedList2, false);
                }
                this.recordFree(weakLinkedList2.size);
                weakLinkedList2 = weakLinkedList2.next;
                weakLinkedList.next = weakLinkedList2;
                continue;
            }
            if (!managedResource.isPermanent() && !managedResource.isLocked() && predicate.test(managedResource)) {
                if (PrismSettings.poolDebug) {
                    BaseResourcePool.showLink("pruning", weakLinkedList2, true);
                }
                managedResource.free();
                managedResource.resource = null;
                this.recordFree(weakLinkedList2.size);
                weakLinkedList2 = weakLinkedList2.next;
                weakLinkedList.next = weakLinkedList2;
                continue;
            }
            weakLinkedList = weakLinkedList2;
            weakLinkedList2 = weakLinkedList2.next;
        }
    }

    static void showLink(String string, WeakLinkedList<?> weakLinkedList, boolean bl) {
        ManagedResource<?> managedResource = weakLinkedList.getResource();
        System.err.printf("%s: %s (size=%,d)", string, managedResource, weakLinkedList.size);
        if (managedResource != null) {
            if (bl) {
                System.err.printf(" (age=%d)", managedResource.getAge());
            }
            if (managedResource.isPermanent()) {
                System.err.print(" perm");
            }
            if (managedResource.isLocked()) {
                System.err.print(" lock");
            }
            if (managedResource.isInteresting()) {
                System.err.print(" int");
            }
        }
        System.err.println();
    }

    @Override
    public void freeDisposalRequestedAndCheckResources(boolean bl) {
        boolean bl2 = false;
        WeakLinkedList<T> weakLinkedList = this.resourceHead;
        WeakLinkedList weakLinkedList2 = weakLinkedList.next;
        while (weakLinkedList2 != null) {
            ManagedResource managedResource = weakLinkedList2.getResource();
            if (ManagedResource._isgone(managedResource)) {
                this.recordFree(weakLinkedList2.size);
                weakLinkedList2 = weakLinkedList2.next;
                weakLinkedList.next = weakLinkedList2;
                continue;
            }
            if (!managedResource.isPermanent()) {
                if (managedResource.isLocked() && !managedResource.wasMismatched()) {
                    if (bl) {
                        managedResource.unlockall();
                    } else {
                        managedResource.setMismatched();
                        bl2 = true;
                    }
                }
                managedResource.bumpAge(1024);
            }
            weakLinkedList = weakLinkedList2;
            weakLinkedList2 = weakLinkedList2.next;
        }
        if (PrismSettings.poolStats || bl2) {
            if (bl2) {
                System.err.println("Outstanding resource locks detected:");
            }
            this.printSummary(true);
            System.err.println();
        }
    }

    static String commas(long l2) {
        return String.format("%,d", l2);
    }

    public void printSummary(boolean bl) {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        long l2 = 0L;
        int n8 = 0;
        boolean bl2 = false;
        double d2 = (double)this.used() * 100.0 / (double)this.max();
        double d3 = (double)this.target() * 100.0 / (double)this.max();
        System.err.printf("%s: %,d used (%.1f%%), %,d target (%.1f%%), %,d max\n", this, this.used(), d2, this.target(), d3, this.max());
        WeakLinkedList weakLinkedList = this.resourceHead.next;
        while (weakLinkedList != null) {
            ManagedResource managedResource = weakLinkedList.getResource();
            ++n8;
            if (managedResource == null || !managedResource.isValid() || managedResource.isDisposalRequested()) {
                ++n2;
            } else {
                int n9 = managedResource.getAge();
                l2 += (long)n9;
                if (n9 >= 1024) {
                    ++n7;
                }
                if (managedResource.wasMismatched()) {
                    ++n6;
                }
                if (managedResource.isPermanent()) {
                    ++n4;
                } else if (managedResource.isLocked()) {
                    ++n3;
                    if (bl2 && bl) {
                        for (Throwable throwable : managedResource.lockedFrom) {
                            throwable.printStackTrace(System.err);
                        }
                        managedResource.lockedFrom.clear();
                    }
                }
                if (managedResource.isInteresting()) {
                    ++n5;
                }
            }
            weakLinkedList = weakLinkedList.next;
        }
        double d4 = (double)l2 / (double)n8;
        System.err.println(n8 + " total resources being managed");
        System.err.printf("average resource age is %.1f frames\n", d4);
        BaseResourcePool.printpoolpercent(n7, n8, "at maximum supported age");
        BaseResourcePool.printpoolpercent(n4, n8, "marked permanent");
        BaseResourcePool.printpoolpercent(n6, n8, "have had mismatched locks");
        BaseResourcePool.printpoolpercent(n3, n8, "locked");
        BaseResourcePool.printpoolpercent(n5, n8, "contain interesting data");
        BaseResourcePool.printpoolpercent(n2, n8, "disappeared");
    }

    private static void printpoolpercent(int n2, int n3, String string) {
        double d2 = (double)n2 * 100.0 / (double)n3;
        System.err.printf("%,d resources %s (%.1f%%)\n", n2, string, d2);
    }

    @Override
    public boolean isManagerThread() {
        return Thread.currentThread() == this.managerThread;
    }

    @Override
    public final long managed() {
        return this.managedSize;
    }

    @Override
    public long used() {
        if (this.sharedParent != null) {
            return this.sharedParent.used();
        }
        return this.managedSize;
    }

    @Override
    public final long max() {
        return this.maxSize;
    }

    @Override
    public final long origTarget() {
        return this.origTarget;
    }

    @Override
    public final long target() {
        return this.curTarget;
    }

    @Override
    public final void setTarget(long l2) {
        if (l2 > this.maxSize) {
            throw new IllegalArgumentException("New target " + l2 + " larger than max " + this.maxSize);
        }
        if (l2 < this.origTarget) {
            throw new IllegalArgumentException("New target " + l2 + " smaller than initial target " + this.origTarget);
        }
        this.curTarget = l2;
    }

    @Override
    public boolean prepareForAllocation(long l2) {
        return this.cleanup(l2);
    }

    @Override
    public final void recordAllocated(long l2) {
        this.managedSize += l2;
    }

    @Override
    public final void resourceManaged(ManagedResource<T> managedResource) {
        long l2 = this.size(managedResource.resource);
        this.resourceHead.insert(managedResource, l2);
        this.recordAllocated(l2);
    }

    @Override
    public final void resourceFreed(ManagedResource<T> managedResource) {
        WeakLinkedList<T> weakLinkedList = this.resourceHead;
        WeakLinkedList weakLinkedList2 = weakLinkedList.next;
        while (weakLinkedList2 != null) {
            ManagedResource managedResource2 = weakLinkedList2.getResource();
            if (managedResource2 == null || managedResource2 == managedResource) {
                this.recordFree(weakLinkedList2.size);
                weakLinkedList2 = weakLinkedList2.next;
                weakLinkedList.next = weakLinkedList2;
                if (managedResource2 != managedResource) continue;
                return;
            }
            weakLinkedList = weakLinkedList2;
            weakLinkedList2 = weakLinkedList2.next;
        }
        throw new IllegalStateException("unmanaged resource freed from pool " + this);
    }

    @Override
    public final void recordFree(long l2) {
        this.managedSize -= l2;
        if (this.managedSize < 0L) {
            throw new IllegalStateException("Negative resource amount");
        }
    }

    private static /* synthetic */ boolean lambda$cleanup$460(ManagedResource managedResource) {
        return false;
    }

    static {
        BaseResourcePool.stageTesters[0] = managedResource -> !managedResource.isInteresting() && managedResource.getAge() > 1024;
        BaseResourcePool.stageReasons[0] = "Pruning unuseful older than 1024";
        BaseResourcePool.stageTesters[1] = managedResource -> !managedResource.isInteresting() && managedResource.getAge() > 512;
        BaseResourcePool.stageReasons[1] = "Pruning unuseful older than 512";
        BaseResourcePool.stageTesters[2] = managedResource -> !managedResource.isInteresting() && managedResource.getAge() > 10;
        BaseResourcePool.stageReasons[2] = "Pruning unuseful older than 10";
        BaseResourcePool.stageTesters[3] = managedResource -> managedResource.getAge() > 1024;
        BaseResourcePool.stageReasons[3] = "Pruning all older than 1024";
        BaseResourcePool.stageTesters[4] = managedResource -> managedResource.getAge() > 512;
        BaseResourcePool.stageReasons[4] = "Pruning all older than 512";
        BaseResourcePool.stageTesters[5] = managedResource -> managedResource.getAge() > 100;
        BaseResourcePool.stageReasons[5] = "Pruning all older than 100";
    }

    static class WeakLinkedList<T> {
        final WeakReference<ManagedResource<T>> theResourceRef;
        final long size;
        WeakLinkedList<T> next;

        WeakLinkedList() {
            this.theResourceRef = null;
            this.size = 0L;
        }

        WeakLinkedList(ManagedResource<T> managedResource, long l2, WeakLinkedList<T> weakLinkedList) {
            this.theResourceRef = new WeakReference<ManagedResource<ManagedResource<T>>>(managedResource);
            this.size = l2;
            this.next = weakLinkedList;
        }

        void insert(ManagedResource<T> managedResource, long l2) {
            this.next = new WeakLinkedList<T>(managedResource, l2, this.next);
        }

        ManagedResource<T> getResource() {
            return (ManagedResource)this.theResourceRef.get();
        }
    }

    static interface Predicate {
        public boolean test(ManagedResource<?> var1);
    }
}

