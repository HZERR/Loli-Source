/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.dom.AttrImpl;
import com.sun.webkit.dom.CDATASectionImpl;
import com.sun.webkit.dom.CommentImpl;
import com.sun.webkit.dom.DocumentFragmentImpl;
import com.sun.webkit.dom.DocumentImpl;
import com.sun.webkit.dom.DocumentTypeImpl;
import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.EntityImpl;
import com.sun.webkit.dom.EntityReferenceImpl;
import com.sun.webkit.dom.EventImpl;
import com.sun.webkit.dom.EventListenerImpl;
import com.sun.webkit.dom.HTMLAnchorElementImpl;
import com.sun.webkit.dom.HTMLAppletElementImpl;
import com.sun.webkit.dom.HTMLAreaElementImpl;
import com.sun.webkit.dom.HTMLBRElementImpl;
import com.sun.webkit.dom.HTMLBaseElementImpl;
import com.sun.webkit.dom.HTMLBaseFontElementImpl;
import com.sun.webkit.dom.HTMLBodyElementImpl;
import com.sun.webkit.dom.HTMLButtonElementImpl;
import com.sun.webkit.dom.HTMLDListElementImpl;
import com.sun.webkit.dom.HTMLDirectoryElementImpl;
import com.sun.webkit.dom.HTMLDivElementImpl;
import com.sun.webkit.dom.HTMLDocumentImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFieldSetElementImpl;
import com.sun.webkit.dom.HTMLFontElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import com.sun.webkit.dom.HTMLFrameElementImpl;
import com.sun.webkit.dom.HTMLFrameSetElementImpl;
import com.sun.webkit.dom.HTMLHRElementImpl;
import com.sun.webkit.dom.HTMLHeadElementImpl;
import com.sun.webkit.dom.HTMLHeadingElementImpl;
import com.sun.webkit.dom.HTMLIFrameElementImpl;
import com.sun.webkit.dom.HTMLImageElementImpl;
import com.sun.webkit.dom.HTMLInputElementImpl;
import com.sun.webkit.dom.HTMLLIElementImpl;
import com.sun.webkit.dom.HTMLLabelElementImpl;
import com.sun.webkit.dom.HTMLLegendElementImpl;
import com.sun.webkit.dom.HTMLLinkElementImpl;
import com.sun.webkit.dom.HTMLMapElementImpl;
import com.sun.webkit.dom.HTMLMenuElementImpl;
import com.sun.webkit.dom.HTMLMetaElementImpl;
import com.sun.webkit.dom.HTMLModElementImpl;
import com.sun.webkit.dom.HTMLOListElementImpl;
import com.sun.webkit.dom.HTMLObjectElementImpl;
import com.sun.webkit.dom.HTMLOptGroupElementImpl;
import com.sun.webkit.dom.HTMLOptionElementImpl;
import com.sun.webkit.dom.HTMLParagraphElementImpl;
import com.sun.webkit.dom.HTMLParamElementImpl;
import com.sun.webkit.dom.HTMLPreElementImpl;
import com.sun.webkit.dom.HTMLQuoteElementImpl;
import com.sun.webkit.dom.HTMLScriptElementImpl;
import com.sun.webkit.dom.HTMLSelectElementImpl;
import com.sun.webkit.dom.HTMLStyleElementImpl;
import com.sun.webkit.dom.HTMLTableCaptionElementImpl;
import com.sun.webkit.dom.HTMLTableCellElementImpl;
import com.sun.webkit.dom.HTMLTableColElementImpl;
import com.sun.webkit.dom.HTMLTableElementImpl;
import com.sun.webkit.dom.HTMLTableRowElementImpl;
import com.sun.webkit.dom.HTMLTableSectionElementImpl;
import com.sun.webkit.dom.HTMLTextAreaElementImpl;
import com.sun.webkit.dom.HTMLTitleElementImpl;
import com.sun.webkit.dom.HTMLUListElementImpl;
import com.sun.webkit.dom.JSObject;
import com.sun.webkit.dom.NamedNodeMapImpl;
import com.sun.webkit.dom.NodeListImpl;
import com.sun.webkit.dom.ProcessingInstructionImpl;
import com.sun.webkit.dom.TextImpl;
import java.lang.ref.Reference;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class NodeImpl
extends JSObject
implements Node,
EventTarget {
    private static SelfDisposer[] hashTable = new SelfDisposer[64];
    private static int hashCount;
    public static final int ELEMENT_NODE = 1;
    public static final int ATTRIBUTE_NODE = 2;
    public static final int TEXT_NODE = 3;
    public static final int CDATA_SECTION_NODE = 4;
    public static final int ENTITY_REFERENCE_NODE = 5;
    public static final int ENTITY_NODE = 6;
    public static final int PROCESSING_INSTRUCTION_NODE = 7;
    public static final int COMMENT_NODE = 8;
    public static final int DOCUMENT_NODE = 9;
    public static final int DOCUMENT_TYPE_NODE = 10;
    public static final int DOCUMENT_FRAGMENT_NODE = 11;
    public static final int NOTATION_NODE = 12;
    public static final int DOCUMENT_POSITION_DISCONNECTED = 1;
    public static final int DOCUMENT_POSITION_PRECEDING = 2;
    public static final int DOCUMENT_POSITION_FOLLOWING = 4;
    public static final int DOCUMENT_POSITION_CONTAINS = 8;
    public static final int DOCUMENT_POSITION_CONTAINED_BY = 16;
    public static final int DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;

    private static int hashPeer(long l2) {
        return (int)(l2 ^ 0xFFFFFFFFFFFFFFFFL ^ l2 >> 7) & hashTable.length - 1;
    }

    private static Node getCachedImpl(long l2) {
        SelfDisposer selfDisposer;
        if (l2 == 0L) {
            return null;
        }
        int n2 = NodeImpl.hashPeer(l2);
        SelfDisposer selfDisposer2 = hashTable[n2];
        Object object = null;
        Object object2 = selfDisposer2;
        while (object2 != null) {
            selfDisposer = ((SelfDisposer)object2).next;
            if (((SelfDisposer)object2).peer == l2) {
                NodeImpl nodeImpl = (NodeImpl)((Reference)object2).get();
                if (nodeImpl != null) {
                    NodeImpl.dispose(l2);
                    return nodeImpl;
                }
                if (object != null) {
                    ((SelfDisposer)object).next = selfDisposer;
                    break;
                }
                NodeImpl.hashTable[n2] = selfDisposer;
                break;
            }
            object = object2;
            object2 = selfDisposer;
        }
        object2 = (NodeImpl)NodeImpl.createInterface(l2);
        selfDisposer = new SelfDisposer(object2, l2);
        selfDisposer.next = selfDisposer2;
        NodeImpl.hashTable[n2] = selfDisposer;
        if (3 * hashCount >= 2 * hashTable.length) {
            NodeImpl.rehash();
        }
        ++hashCount;
        return object2;
    }

    private static void rehash() {
        SelfDisposer[] arrselfDisposer = hashTable;
        int n2 = arrselfDisposer.length;
        SelfDisposer[] arrselfDisposer2 = new SelfDisposer[2 * n2];
        hashTable = arrselfDisposer2;
        int n3 = n2;
        while (--n3 >= 0) {
            SelfDisposer selfDisposer = arrselfDisposer[n3];
            while (selfDisposer != null) {
                SelfDisposer selfDisposer2 = selfDisposer.next;
                int n4 = NodeImpl.hashPeer(selfDisposer.peer);
                selfDisposer.next = arrselfDisposer2[n4];
                arrselfDisposer2[n4] = selfDisposer;
                selfDisposer = selfDisposer2;
            }
        }
    }

    NodeImpl(long l2) {
        super(l2, 1);
    }

    static Node createInterface(long l2) {
        if (l2 == 0L) {
            return null;
        }
        switch (NodeImpl.getNodeTypeImpl(l2)) {
            case 1: {
                if (!ElementImpl.isHTMLElementImpl(l2)) {
                    return new ElementImpl(l2);
                }
                String string = ElementImpl.getTagNameImpl(l2).toUpperCase();
                if ("A".equals(string)) {
                    return new HTMLAnchorElementImpl(l2);
                }
                if ("APPLET".equals(string)) {
                    return new HTMLAppletElementImpl(l2);
                }
                if ("AREA".equals(string)) {
                    return new HTMLAreaElementImpl(l2);
                }
                if ("BASE".equals(string)) {
                    return new HTMLBaseElementImpl(l2);
                }
                if ("BASEFONT".equals(string)) {
                    return new HTMLBaseFontElementImpl(l2);
                }
                if ("BODY".equals(string)) {
                    return new HTMLBodyElementImpl(l2);
                }
                if ("BR".equals(string)) {
                    return new HTMLBRElementImpl(l2);
                }
                if ("BUTTON".equals(string)) {
                    return new HTMLButtonElementImpl(l2);
                }
                if ("DIR".equals(string)) {
                    return new HTMLDirectoryElementImpl(l2);
                }
                if ("DIV".equals(string)) {
                    return new HTMLDivElementImpl(l2);
                }
                if ("DL".equals(string)) {
                    return new HTMLDListElementImpl(l2);
                }
                if ("FIELDSET".equals(string)) {
                    return new HTMLFieldSetElementImpl(l2);
                }
                if ("FONT".equals(string)) {
                    return new HTMLFontElementImpl(l2);
                }
                if ("FORM".equals(string)) {
                    return new HTMLFormElementImpl(l2);
                }
                if ("FRAME".equals(string)) {
                    return new HTMLFrameElementImpl(l2);
                }
                if ("FRAMESET".equals(string)) {
                    return new HTMLFrameSetElementImpl(l2);
                }
                if ("HEAD".equals(string)) {
                    return new HTMLHeadElementImpl(l2);
                }
                if (string.length() == 2 && string.charAt(0) == 'H' && string.charAt(1) >= '1' && string.charAt(1) <= '6') {
                    return new HTMLHeadingElementImpl(l2);
                }
                if ("HR".equals(string)) {
                    return new HTMLHRElementImpl(l2);
                }
                if ("IFRAME".equals(string)) {
                    return new HTMLIFrameElementImpl(l2);
                }
                if ("IMG".equals(string)) {
                    return new HTMLImageElementImpl(l2);
                }
                if ("INPUT".equals(string)) {
                    return new HTMLInputElementImpl(l2);
                }
                if ("LABEL".equals(string)) {
                    return new HTMLLabelElementImpl(l2);
                }
                if ("LEGEND".equals(string)) {
                    return new HTMLLegendElementImpl(l2);
                }
                if ("LI".equals(string)) {
                    return new HTMLLIElementImpl(l2);
                }
                if ("LINK".equals(string)) {
                    return new HTMLLinkElementImpl(l2);
                }
                if ("MAP".equals(string)) {
                    return new HTMLMapElementImpl(l2);
                }
                if ("MENU".equals(string)) {
                    return new HTMLMenuElementImpl(l2);
                }
                if ("META".equals(string)) {
                    return new HTMLMetaElementImpl(l2);
                }
                if ("INS".equals(string) || "DEL".equals(string)) {
                    return new HTMLModElementImpl(l2);
                }
                if ("OBJECT".equals(string)) {
                    return new HTMLObjectElementImpl(l2);
                }
                if ("OL".equals(string)) {
                    return new HTMLOListElementImpl(l2);
                }
                if ("OPTGROUP".equals(string)) {
                    return new HTMLOptGroupElementImpl(l2);
                }
                if ("OPTION".equals(string)) {
                    return new HTMLOptionElementImpl(l2);
                }
                if ("P".equals(string)) {
                    return new HTMLParagraphElementImpl(l2);
                }
                if ("PARAM".equals(string)) {
                    return new HTMLParamElementImpl(l2);
                }
                if ("PRE".equals(string)) {
                    return new HTMLPreElementImpl(l2);
                }
                if ("Q".equals(string)) {
                    return new HTMLQuoteElementImpl(l2);
                }
                if ("SCRIPT".equals(string)) {
                    return new HTMLScriptElementImpl(l2);
                }
                if ("SELECT".equals(string)) {
                    return new HTMLSelectElementImpl(l2);
                }
                if ("STYLE".equals(string)) {
                    return new HTMLStyleElementImpl(l2);
                }
                if ("CAPTION".equals(string)) {
                    return new HTMLTableCaptionElementImpl(l2);
                }
                if ("TD".equals(string)) {
                    return new HTMLTableCellElementImpl(l2);
                }
                if ("COL".equals(string)) {
                    return new HTMLTableColElementImpl(l2);
                }
                if ("TABLE".equals(string)) {
                    return new HTMLTableElementImpl(l2);
                }
                if ("TR".equals(string)) {
                    return new HTMLTableRowElementImpl(l2);
                }
                if ("THEAD".equals(string) || "TFOOT".equals(string) || "TBODY".equals(string)) {
                    return new HTMLTableSectionElementImpl(l2);
                }
                if ("TEXTAREA".equals(string)) {
                    return new HTMLTextAreaElementImpl(l2);
                }
                if ("TITLE".equals(string)) {
                    return new HTMLTitleElementImpl(l2);
                }
                if ("UL".equals(string)) {
                    return new HTMLUListElementImpl(l2);
                }
                return new HTMLElementImpl(l2);
            }
            case 2: {
                return new AttrImpl(l2);
            }
            case 3: {
                return new TextImpl(l2);
            }
            case 4: {
                return new CDATASectionImpl(l2);
            }
            case 5: {
                return new EntityReferenceImpl(l2);
            }
            case 6: {
                return new EntityImpl(l2);
            }
            case 7: {
                return new ProcessingInstructionImpl(l2);
            }
            case 8: {
                return new CommentImpl(l2);
            }
            case 9: {
                if (DocumentImpl.isHTMLDocumentImpl(l2)) {
                    return new HTMLDocumentImpl(l2);
                }
                return new DocumentImpl(l2);
            }
            case 10: {
                return new DocumentTypeImpl(l2);
            }
            case 11: {
                return new DocumentFragmentImpl(l2);
            }
        }
        return new NodeImpl(l2);
    }

    static Node create(long l2) {
        return NodeImpl.getCachedImpl(l2);
    }

    static long getPeer(Node node) {
        return node == null ? 0L : ((NodeImpl)node).getPeer();
    }

    private static native void dispose(long var0);

    static Node getImpl(long l2) {
        return NodeImpl.create(l2);
    }

    @Override
    public String getNodeName() {
        return NodeImpl.getNodeNameImpl(this.getPeer());
    }

    static native String getNodeNameImpl(long var0);

    @Override
    public String getNodeValue() {
        return NodeImpl.getNodeValueImpl(this.getPeer());
    }

    static native String getNodeValueImpl(long var0);

    @Override
    public void setNodeValue(String string) throws DOMException {
        NodeImpl.setNodeValueImpl(this.getPeer(), string);
    }

    static native void setNodeValueImpl(long var0, String var2);

    @Override
    public short getNodeType() {
        return NodeImpl.getNodeTypeImpl(this.getPeer());
    }

    static native short getNodeTypeImpl(long var0);

    @Override
    public Node getParentNode() {
        return NodeImpl.getImpl(NodeImpl.getParentNodeImpl(this.getPeer()));
    }

    static native long getParentNodeImpl(long var0);

    @Override
    public NodeList getChildNodes() {
        return NodeListImpl.getImpl(NodeImpl.getChildNodesImpl(this.getPeer()));
    }

    static native long getChildNodesImpl(long var0);

    @Override
    public Node getFirstChild() {
        return NodeImpl.getImpl(NodeImpl.getFirstChildImpl(this.getPeer()));
    }

    static native long getFirstChildImpl(long var0);

    @Override
    public Node getLastChild() {
        return NodeImpl.getImpl(NodeImpl.getLastChildImpl(this.getPeer()));
    }

    static native long getLastChildImpl(long var0);

    @Override
    public Node getPreviousSibling() {
        return NodeImpl.getImpl(NodeImpl.getPreviousSiblingImpl(this.getPeer()));
    }

    static native long getPreviousSiblingImpl(long var0);

    @Override
    public Node getNextSibling() {
        return NodeImpl.getImpl(NodeImpl.getNextSiblingImpl(this.getPeer()));
    }

    static native long getNextSiblingImpl(long var0);

    @Override
    public Document getOwnerDocument() {
        return DocumentImpl.getImpl(NodeImpl.getOwnerDocumentImpl(this.getPeer()));
    }

    static native long getOwnerDocumentImpl(long var0);

    @Override
    public String getNamespaceURI() {
        return NodeImpl.getNamespaceURIImpl(this.getPeer());
    }

    static native String getNamespaceURIImpl(long var0);

    @Override
    public String getPrefix() {
        return NodeImpl.getPrefixImpl(this.getPeer());
    }

    static native String getPrefixImpl(long var0);

    @Override
    public void setPrefix(String string) throws DOMException {
        NodeImpl.setPrefixImpl(this.getPeer(), string);
    }

    static native void setPrefixImpl(long var0, String var2);

    @Override
    public String getLocalName() {
        return NodeImpl.getLocalNameImpl(this.getPeer());
    }

    static native String getLocalNameImpl(long var0);

    @Override
    public NamedNodeMap getAttributes() {
        return NamedNodeMapImpl.getImpl(NodeImpl.getAttributesImpl(this.getPeer()));
    }

    static native long getAttributesImpl(long var0);

    @Override
    public String getBaseURI() {
        return NodeImpl.getBaseURIImpl(this.getPeer());
    }

    static native String getBaseURIImpl(long var0);

    @Override
    public String getTextContent() {
        return NodeImpl.getTextContentImpl(this.getPeer());
    }

    static native String getTextContentImpl(long var0);

    @Override
    public void setTextContent(String string) throws DOMException {
        NodeImpl.setTextContentImpl(this.getPeer(), string);
    }

    static native void setTextContentImpl(long var0, String var2);

    public Element getParentElement() {
        return ElementImpl.getImpl(NodeImpl.getParentElementImpl(this.getPeer()));
    }

    static native long getParentElementImpl(long var0);

    @Override
    public Node insertBefore(Node node, Node node2) throws DOMException {
        return NodeImpl.getImpl(NodeImpl.insertBeforeImpl(this.getPeer(), NodeImpl.getPeer(node), NodeImpl.getPeer(node2)));
    }

    static native long insertBeforeImpl(long var0, long var2, long var4);

    @Override
    public Node replaceChild(Node node, Node node2) throws DOMException {
        return NodeImpl.getImpl(NodeImpl.replaceChildImpl(this.getPeer(), NodeImpl.getPeer(node), NodeImpl.getPeer(node2)));
    }

    static native long replaceChildImpl(long var0, long var2, long var4);

    @Override
    public Node removeChild(Node node) throws DOMException {
        return NodeImpl.getImpl(NodeImpl.removeChildImpl(this.getPeer(), NodeImpl.getPeer(node)));
    }

    static native long removeChildImpl(long var0, long var2);

    @Override
    public Node appendChild(Node node) throws DOMException {
        return NodeImpl.getImpl(NodeImpl.appendChildImpl(this.getPeer(), NodeImpl.getPeer(node)));
    }

    static native long appendChildImpl(long var0, long var2);

    @Override
    public boolean hasChildNodes() {
        return NodeImpl.hasChildNodesImpl(this.getPeer());
    }

    static native boolean hasChildNodesImpl(long var0);

    @Override
    public Node cloneNode(boolean bl) throws DOMException {
        return NodeImpl.getImpl(NodeImpl.cloneNodeImpl(this.getPeer(), bl));
    }

    static native long cloneNodeImpl(long var0, boolean var2);

    @Override
    public void normalize() {
        NodeImpl.normalizeImpl(this.getPeer());
    }

    static native void normalizeImpl(long var0);

    @Override
    public boolean isSupported(String string, String string2) {
        return NodeImpl.isSupportedImpl(this.getPeer(), string, string2);
    }

    static native boolean isSupportedImpl(long var0, String var2, String var3);

    @Override
    public boolean hasAttributes() {
        return NodeImpl.hasAttributesImpl(this.getPeer());
    }

    static native boolean hasAttributesImpl(long var0);

    @Override
    public boolean isSameNode(Node node) {
        return NodeImpl.isSameNodeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native boolean isSameNodeImpl(long var0, long var2);

    @Override
    public boolean isEqualNode(Node node) {
        return NodeImpl.isEqualNodeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native boolean isEqualNodeImpl(long var0, long var2);

    @Override
    public String lookupPrefix(String string) {
        return NodeImpl.lookupPrefixImpl(this.getPeer(), string);
    }

    static native String lookupPrefixImpl(long var0, String var2);

    @Override
    public boolean isDefaultNamespace(String string) {
        return NodeImpl.isDefaultNamespaceImpl(this.getPeer(), string);
    }

    static native boolean isDefaultNamespaceImpl(long var0, String var2);

    @Override
    public String lookupNamespaceURI(String string) {
        return NodeImpl.lookupNamespaceURIImpl(this.getPeer(), string);
    }

    static native String lookupNamespaceURIImpl(long var0, String var2);

    @Override
    public short compareDocumentPosition(Node node) {
        return NodeImpl.compareDocumentPositionImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native short compareDocumentPositionImpl(long var0, long var2);

    public boolean contains(Node node) {
        return NodeImpl.containsImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native boolean containsImpl(long var0, long var2);

    @Override
    public void addEventListener(String string, EventListener eventListener, boolean bl) {
        NodeImpl.addEventListenerImpl(this.getPeer(), string, EventListenerImpl.getPeer(eventListener), bl);
    }

    static native void addEventListenerImpl(long var0, String var2, long var3, boolean var5);

    @Override
    public void removeEventListener(String string, EventListener eventListener, boolean bl) {
        NodeImpl.removeEventListenerImpl(this.getPeer(), string, EventListenerImpl.getPeer(eventListener), bl);
    }

    static native void removeEventListenerImpl(long var0, String var2, long var3, boolean var5);

    @Override
    public boolean dispatchEvent(Event event) throws DOMException {
        return NodeImpl.dispatchEventImpl(this.getPeer(), EventImpl.getPeer(event));
    }

    static native boolean dispatchEventImpl(long var0, long var2);

    @Override
    public Object getUserData(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object setUserData(String string, Object object, UserDataHandler userDataHandler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getFeature(String string, String string2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static final class SelfDisposer
    extends Disposer.WeakDisposerRecord {
        private final long peer;
        SelfDisposer next;

        SelfDisposer(Object object, long l2) {
            super(object);
            this.peer = l2;
        }

        @Override
        public void dispose() {
            int n2 = NodeImpl.hashPeer(this.peer);
            SelfDisposer selfDisposer = hashTable[n2];
            SelfDisposer selfDisposer2 = null;
            SelfDisposer selfDisposer3 = selfDisposer;
            while (selfDisposer3 != null) {
                SelfDisposer selfDisposer4 = selfDisposer3.next;
                if (selfDisposer3.peer == this.peer) {
                    selfDisposer3.clear();
                    if (selfDisposer2 != null) {
                        selfDisposer2.next = selfDisposer4;
                    } else {
                        hashTable[n2] = selfDisposer4;
                    }
                    hashCount--;
                    break;
                }
                selfDisposer2 = selfDisposer3;
                selfDisposer3 = selfDisposer4;
            }
            NodeImpl.dispose(this.peer);
        }
    }
}

