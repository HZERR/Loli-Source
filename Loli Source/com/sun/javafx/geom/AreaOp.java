/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.ChainEnd;
import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.CurveLink;
import com.sun.javafx.geom.Edge;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

public abstract class AreaOp {
    public static final int CTAG_LEFT = 0;
    public static final int CTAG_RIGHT = 1;
    public static final int ETAG_IGNORE = 0;
    public static final int ETAG_ENTER = 1;
    public static final int ETAG_EXIT = -1;
    public static final int RSTAG_INSIDE = 1;
    public static final int RSTAG_OUTSIDE = -1;
    private static Comparator YXTopComparator = (object, object2) -> {
        double d2;
        Curve curve = ((Edge)object).getCurve();
        Curve curve2 = ((Edge)object2).getCurve();
        double d3 = curve.getYTop();
        if (d3 == (d2 = curve2.getYTop()) && (d3 = curve.getXTop()) == (d2 = curve2.getXTop())) {
            return 0;
        }
        if (d3 < d2) {
            return -1;
        }
        return 1;
    };
    private static final CurveLink[] EmptyLinkList = new CurveLink[2];
    private static final ChainEnd[] EmptyChainList = new ChainEnd[2];

    private AreaOp() {
    }

    public abstract void newRow();

    public abstract int classify(Edge var1);

    public abstract int getState();

    public Vector calculate(Vector vector, Vector vector2) {
        Vector vector3 = new Vector();
        AreaOp.addEdges(vector3, vector, 0);
        AreaOp.addEdges(vector3, vector2, 1);
        vector3 = this.pruneEdges(vector3);
        return vector3;
    }

    private static void addEdges(Vector vector, Vector vector2, int n2) {
        Enumeration enumeration = vector2.elements();
        while (enumeration.hasMoreElements()) {
            Curve curve = (Curve)enumeration.nextElement();
            if (curve.getOrder() <= 0) continue;
            vector.add(new Edge(curve, n2));
        }
    }

    private Vector pruneEdges(Vector vector) {
        int n2 = vector.size();
        if (n2 < 2) {
            return vector;
        }
        Edge[] arredge = vector.toArray(new Edge[n2]);
        Arrays.sort(arredge, YXTopComparator);
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        double[] arrd = new double[2];
        Vector vector2 = new Vector();
        Vector vector3 = new Vector();
        Vector<CurveLink> vector4 = new Vector<CurveLink>();
        while (n3 < n2) {
            int n7;
            Edge edge;
            double d2 = arrd[0];
            for (n5 = n6 = n4 - 1; n5 >= n3; --n5) {
                edge = arredge[n5];
                if (!(edge.getCurve().getYBot() > d2)) continue;
                if (n6 > n5) {
                    arredge[n6] = edge;
                }
                --n6;
            }
            n3 = n6 + 1;
            if (n3 >= n4) {
                if (n4 >= n2) break;
                d2 = arredge[n4].getCurve().getYTop();
                if (d2 > arrd[0]) {
                    AreaOp.finalizeSubCurves(vector2, vector3);
                }
                arrd[0] = d2;
            }
            while (n4 < n2 && !((edge = arredge[n4]).getCurve().getYTop() > d2)) {
                ++n4;
            }
            arrd[1] = arredge[n3].getCurve().getYBot();
            if (n4 < n2 && arrd[1] > (d2 = arredge[n4].getCurve().getYTop())) {
                arrd[1] = d2;
            }
            int n8 = 1;
            for (n5 = n3; n5 < n4; ++n5) {
                edge = arredge[n5];
                edge.setEquivalence(0);
                for (n6 = n5; n6 > n3; --n6) {
                    Edge edge2 = arredge[n6 - 1];
                    int n9 = edge.compareTo(edge2, arrd);
                    if (arrd[1] <= arrd[0]) {
                        throw new InternalError("backstepping to " + arrd[1] + " from " + arrd[0]);
                    }
                    if (n9 >= 0) {
                        if (n9 != 0) break;
                        int n10 = edge2.getEquivalence();
                        if (n10 == 0) {
                            n10 = n8++;
                            edge2.setEquivalence(n10);
                        }
                        edge.setEquivalence(n10);
                        break;
                    }
                    arredge[n6] = edge2;
                }
                arredge[n6] = edge;
            }
            this.newRow();
            double d3 = arrd[0];
            double d4 = arrd[1];
            for (n5 = n3; n5 < n4; ++n5) {
                edge = arredge[n5];
                int n11 = edge.getEquivalence();
                if (n11 != 0) {
                    int n12 = this.getState();
                    n7 = n12 == 1 ? -1 : 1;
                    Edge edge3 = null;
                    Edge edge4 = edge;
                    double d5 = d4;
                    do {
                        this.classify(edge);
                        if (edge3 == null && edge.isActiveFor(d3, n7)) {
                            edge3 = edge;
                        }
                        if (!((d2 = edge.getCurve().getYBot()) > d5)) continue;
                        edge4 = edge;
                        d5 = d2;
                    } while (++n5 < n4 && (edge = arredge[n5]).getEquivalence() == n11);
                    --n5;
                    if (this.getState() == n12) {
                        n7 = 0;
                    } else {
                        edge = edge3 != null ? edge3 : edge4;
                    }
                } else {
                    n7 = this.classify(edge);
                }
                if (n7 == 0) continue;
                edge.record(d4, n7);
                vector4.add(new CurveLink(edge.getCurve(), d3, d4, n7));
            }
            if (this.getState() != -1) {
                System.out.println("Still inside at end of active edge list!");
                System.out.println("num curves = " + (n4 - n3));
                System.out.println("num links = " + vector4.size());
                System.out.println("y top = " + arrd[0]);
                if (n4 < n2) {
                    System.out.println("y top of next curve = " + arredge[n4].getCurve().getYTop());
                } else {
                    System.out.println("no more curves");
                }
                for (n5 = n3; n5 < n4; ++n5) {
                    edge = arredge[n5];
                    System.out.println(edge);
                    n7 = edge.getEquivalence();
                    if (n7 == 0) continue;
                    System.out.println("  was equal to " + n7 + "...");
                }
            }
            AreaOp.resolveLinks(vector2, vector3, vector4);
            vector4.clear();
            arrd[0] = d4;
        }
        AreaOp.finalizeSubCurves(vector2, vector3);
        Vector<Curve> vector5 = new Vector<Curve>();
        Enumeration enumeration = vector2.elements();
        while (enumeration.hasMoreElements()) {
            CurveLink curveLink = (CurveLink)enumeration.nextElement();
            vector5.add(curveLink.getMoveto());
            CurveLink curveLink2 = curveLink;
            while ((curveLink2 = curveLink2.getNext()) != null) {
                if (curveLink.absorb(curveLink2)) continue;
                vector5.add(curveLink.getSubCurve());
                curveLink = curveLink2;
            }
            vector5.add(curveLink.getSubCurve());
        }
        return vector5;
    }

    public static void finalizeSubCurves(Vector vector, Vector vector2) {
        int n2 = vector2.size();
        if (n2 == 0) {
            return;
        }
        if ((n2 & 1) != 0) {
            throw new InternalError("Odd number of chains!");
        }
        ChainEnd[] arrchainEnd = new ChainEnd[n2];
        vector2.toArray(arrchainEnd);
        for (int i2 = 1; i2 < n2; i2 += 2) {
            ChainEnd chainEnd = arrchainEnd[i2 - 1];
            ChainEnd chainEnd2 = arrchainEnd[i2];
            CurveLink curveLink = chainEnd.linkTo(chainEnd2);
            if (curveLink == null) continue;
            vector.add(curveLink);
        }
        vector2.clear();
    }

    public static void resolveLinks(Vector vector, Vector vector2, Vector vector3) {
        ChainEnd[] arrchainEnd;
        CurveLink[] arrcurveLink;
        int n2 = vector3.size();
        if (n2 == 0) {
            arrcurveLink = EmptyLinkList;
        } else {
            if ((n2 & 1) != 0) {
                throw new InternalError("Odd number of new curves!");
            }
            arrcurveLink = new CurveLink[n2 + 2];
            vector3.toArray(arrcurveLink);
        }
        int n3 = vector2.size();
        if (n3 == 0) {
            arrchainEnd = EmptyChainList;
        } else {
            if ((n3 & 1) != 0) {
                throw new InternalError("Odd number of chains!");
            }
            arrchainEnd = new ChainEnd[n3 + 2];
            vector2.toArray(arrchainEnd);
        }
        int n4 = 0;
        int n5 = 0;
        vector2.clear();
        ChainEnd chainEnd = arrchainEnd[0];
        ChainEnd chainEnd2 = arrchainEnd[1];
        CurveLink curveLink = arrcurveLink[0];
        CurveLink curveLink2 = arrcurveLink[1];
        while (chainEnd != null || curveLink != null) {
            boolean bl;
            boolean bl2 = curveLink == null;
            boolean bl3 = bl = chainEnd == null;
            if (!bl2 && !bl) {
                bl2 = !(n4 & true) && chainEnd.getX() == chainEnd2.getX();
                boolean bl4 = bl = !(n5 & true) && curveLink.getX() == curveLink2.getX();
                if (!bl2 && !bl) {
                    double d2 = chainEnd.getX();
                    double d3 = curveLink.getX();
                    bl2 = chainEnd2 != null && d2 < d3 && AreaOp.obstructs(chainEnd2.getX(), d3, n4);
                    boolean bl5 = bl = curveLink2 != null && d3 < d2 && AreaOp.obstructs(curveLink2.getX(), d2, n5);
                }
            }
            if (bl2) {
                CurveLink curveLink3 = chainEnd.linkTo(chainEnd2);
                if (curveLink3 != null) {
                    vector.add(curveLink3);
                }
                chainEnd = arrchainEnd[n4 += 2];
                chainEnd2 = arrchainEnd[n4 + 1];
            }
            if (bl) {
                ChainEnd chainEnd3 = new ChainEnd(curveLink, null);
                ChainEnd chainEnd4 = new ChainEnd(curveLink2, chainEnd3);
                chainEnd3.setOtherEnd(chainEnd4);
                vector2.add(chainEnd3);
                vector2.add(chainEnd4);
                curveLink = arrcurveLink[n5 += 2];
                curveLink2 = arrcurveLink[n5 + 1];
            }
            if (bl2 || bl) continue;
            chainEnd.addLink(curveLink);
            vector2.add(chainEnd);
            chainEnd = chainEnd2;
            chainEnd2 = arrchainEnd[++n4 + 1];
            curveLink = curveLink2;
            curveLink2 = arrcurveLink[++n5 + 1];
        }
        if ((vector2.size() & 1) != 0) {
            System.out.println("Odd number of chains!");
        }
    }

    public static boolean obstructs(double d2, double d3, int n2) {
        return (n2 & 1) == 0 ? d2 <= d3 : d2 < d3;
    }

    public static class EOWindOp
    extends AreaOp {
        private boolean inside;

        @Override
        public void newRow() {
            this.inside = false;
        }

        @Override
        public int classify(Edge edge) {
            boolean bl;
            this.inside = bl = !this.inside;
            return bl ? 1 : -1;
        }

        @Override
        public int getState() {
            return this.inside ? 1 : -1;
        }
    }

    public static class NZWindOp
    extends AreaOp {
        private int count;

        @Override
        public void newRow() {
            this.count = 0;
        }

        @Override
        public int classify(Edge edge) {
            int n2 = this.count;
            int n3 = n2 == 0 ? 1 : 0;
            this.count = n2 += edge.getCurve().getDirection();
            return n2 == 0 ? -1 : n3;
        }

        @Override
        public int getState() {
            return this.count == 0 ? -1 : 1;
        }
    }

    public static class XorOp
    extends CAGOp {
        @Override
        public boolean newClassification(boolean bl, boolean bl2) {
            return bl != bl2;
        }
    }

    public static class IntOp
    extends CAGOp {
        @Override
        public boolean newClassification(boolean bl, boolean bl2) {
            return bl && bl2;
        }
    }

    public static class SubOp
    extends CAGOp {
        @Override
        public boolean newClassification(boolean bl, boolean bl2) {
            return bl && !bl2;
        }
    }

    public static class AddOp
    extends CAGOp {
        @Override
        public boolean newClassification(boolean bl, boolean bl2) {
            return bl || bl2;
        }
    }

    public static abstract class CAGOp
    extends AreaOp {
        boolean inLeft;
        boolean inRight;
        boolean inResult;

        @Override
        public void newRow() {
            this.inLeft = false;
            this.inRight = false;
            this.inResult = false;
        }

        @Override
        public int classify(Edge edge) {
            if (edge.getCurveTag() == 0) {
                this.inLeft = !this.inLeft;
            } else {
                this.inRight = !this.inRight;
            }
            boolean bl = this.newClassification(this.inLeft, this.inRight);
            if (this.inResult == bl) {
                return 0;
            }
            this.inResult = bl;
            return bl ? 1 : -1;
        }

        @Override
        public int getState() {
            return this.inResult ? 1 : -1;
        }

        public abstract boolean newClassification(boolean var1, boolean var2);
    }
}

