/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.xml;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.xml.ASMContentHandler;
import org.objectweb.asm.xml.SAXClassAdapter;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class Processor {
    public static final int BYTECODE = 1;
    public static final int MULTI_XML = 2;
    public static final int SINGLE_XML = 3;
    private static final String SINGLE_XML_NAME = "classes.xml";
    private final int inRepresentation;
    private final int outRepresentation;
    private final InputStream input;
    private final OutputStream output;
    private final Source xslt;
    private int n = 0;

    public Processor(int inRepresenation, int outRepresentation, InputStream input, OutputStream output, Source xslt) {
        this.inRepresentation = inRepresenation;
        this.outRepresentation = outRepresentation;
        this.input = input;
        this.output = output;
        this.xslt = xslt;
    }

    public int process() throws TransformerException, IOException, SAXException {
        ZipEntry ze;
        ZipInputStream zis = new ZipInputStream(this.input);
        ZipOutputStream zos = new ZipOutputStream(this.output);
        OutputStreamWriter osw = new OutputStreamWriter(zos);
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        TransformerFactory tf = TransformerFactory.newInstance();
        if (!tf.getFeature("http://javax.xml.transform.sax.SAXSource/feature") || !tf.getFeature("http://javax.xml.transform.sax.SAXResult/feature")) {
            return 0;
        }
        SAXTransformerFactory saxtf = (SAXTransformerFactory)tf;
        Templates templates = null;
        if (this.xslt != null) {
            templates = saxtf.newTemplates(this.xslt);
        }
        EntryElement entryElement = this.getEntryElement(zos);
        DefaultHandler outDocHandler = null;
        switch (this.outRepresentation) {
            case 1: {
                outDocHandler = new OutputSlicingHandler(new ASMContentHandlerFactory(zos), entryElement, false);
                break;
            }
            case 2: {
                outDocHandler = new OutputSlicingHandler(new SAXWriterFactory(osw, true), entryElement, true);
                break;
            }
            case 3: {
                ZipEntry outputEntry = new ZipEntry(SINGLE_XML_NAME);
                zos.putNextEntry(outputEntry);
                outDocHandler = new SAXWriter(osw, false);
            }
        }
        DefaultHandler inDocHandler = templates == null ? outDocHandler : new InputSlicingHandler("class", outDocHandler, new TransformerHandlerFactory(saxtf, templates, outDocHandler));
        SubdocumentHandlerFactory inDocHandlerFactory = new SubdocumentHandlerFactory(inDocHandler);
        if (inDocHandler != null && this.inRepresentation != 3) {
            inDocHandler.startDocument();
            inDocHandler.startElement("", "classes", "classes", new AttributesImpl());
        }
        int i2 = 0;
        while ((ze = zis.getNextEntry()) != null) {
            this.update(ze.getName(), this.n++);
            if (this.isClassEntry(ze)) {
                this.processEntry(zis, ze, inDocHandlerFactory);
            } else {
                OutputStream os = entryElement.openEntry(this.getName(ze));
                this.copyEntry(zis, os);
                entryElement.closeEntry();
            }
            ++i2;
        }
        if (inDocHandler != null && this.inRepresentation != 3) {
            inDocHandler.endElement("", "classes", "classes");
            inDocHandler.endDocument();
        }
        if (this.outRepresentation == 3) {
            zos.closeEntry();
        }
        zos.flush();
        zos.close();
        return i2;
    }

    private void copyEntry(InputStream is, OutputStream os) throws IOException {
        int i2;
        if (this.outRepresentation == 3) {
            return;
        }
        byte[] buff = new byte[2048];
        while ((i2 = is.read(buff)) != -1) {
            os.write(buff, 0, i2);
        }
    }

    private boolean isClassEntry(ZipEntry ze) {
        String name = ze.getName();
        return this.inRepresentation == 3 && name.equals(SINGLE_XML_NAME) || name.endsWith(".class") || name.endsWith(".class.xml");
    }

    private void processEntry(ZipInputStream zis, ZipEntry ze, ContentHandlerFactory handlerFactory) {
        ContentHandler handler = handlerFactory.createContentHandler();
        try {
            boolean singleInputDocument;
            boolean bl = singleInputDocument = this.inRepresentation == 3;
            if (this.inRepresentation == 1) {
                ClassReader cr = new ClassReader(Processor.readEntry(zis, ze));
                cr.accept(new SAXClassAdapter(handler, singleInputDocument), 0);
            } else {
                XMLReader reader = XMLReaderFactory.createXMLReader();
                reader.setContentHandler(handler);
                reader.parse(new InputSource(singleInputDocument ? new ProtectedInputStream(zis) : new ByteArrayInputStream(Processor.readEntry(zis, ze))));
            }
        }
        catch (Exception ex) {
            this.update(ze.getName(), 0);
            this.update(ex, 0);
        }
    }

    private EntryElement getEntryElement(ZipOutputStream zos) {
        if (this.outRepresentation == 3) {
            return new SingleDocElement(zos);
        }
        return new ZipEntryElement(zos);
    }

    private String getName(ZipEntry ze) {
        String name = ze.getName();
        if (this.isClassEntry(ze)) {
            if (this.inRepresentation != 1 && this.outRepresentation == 1) {
                name = name.substring(0, name.length() - 4);
            } else if (this.inRepresentation == 1 && this.outRepresentation != 1) {
                name = name + ".xml";
            }
        }
        return name;
    }

    private static byte[] readEntry(InputStream zis, ZipEntry ze) throws IOException {
        int i2;
        long size = ze.getSize();
        if (size > -1L) {
            int n2;
            byte[] buff = new byte[(int)size];
            int k2 = 0;
            while ((n2 = zis.read(buff, k2, buff.length - k2)) > 0) {
                k2 += n2;
            }
            return buff;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        while ((i2 = zis.read(buff)) != -1) {
            bos.write(buff, 0, i2);
        }
        return bos.toByteArray();
    }

    protected void update(Object arg, int n2) {
        if (arg instanceof Throwable) {
            ((Throwable)arg).printStackTrace();
        } else if (n2 % 100 == 0) {
            System.err.println(n2 + " " + arg);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            Processor.showUsage();
            return;
        }
        int inRepresentation = Processor.getRepresentation(args[0]);
        int outRepresentation = Processor.getRepresentation(args[1]);
        InputStream is = System.in;
        BufferedOutputStream os = new BufferedOutputStream(System.out);
        StreamSource xslt = null;
        for (int i2 = 2; i2 < args.length; ++i2) {
            if ("-in".equals(args[i2])) {
                is = new FileInputStream(args[++i2]);
                continue;
            }
            if ("-out".equals(args[i2])) {
                os = new BufferedOutputStream(new FileOutputStream(args[++i2]));
                continue;
            }
            if ("-xslt".equals(args[i2])) {
                xslt = new StreamSource(new FileInputStream(args[++i2]));
                continue;
            }
            Processor.showUsage();
            return;
        }
        if (inRepresentation == 0 || outRepresentation == 0) {
            Processor.showUsage();
            return;
        }
        Processor m2 = new Processor(inRepresentation, outRepresentation, is, os, xslt);
        long l1 = System.currentTimeMillis();
        int n2 = m2.process();
        long l2 = System.currentTimeMillis();
        System.err.println(n2);
        System.err.println(l2 - l1 + "ms  " + 1000.0f * (float)n2 / (float)(l2 - l1) + " resources/sec");
    }

    private static int getRepresentation(String s2) {
        if ("code".equals(s2)) {
            return 1;
        }
        if ("xml".equals(s2)) {
            return 2;
        }
        if ("singlexml".equals(s2)) {
            return 3;
        }
        return 0;
    }

    private static void showUsage() {
        System.err.println("Usage: Main <in format> <out format> [-in <input jar>] [-out <output jar>] [-xslt <xslt fiel>]");
        System.err.println("  when -in or -out is omitted sysin and sysout would be used");
        System.err.println("  <in format> and <out format> - code | xml | singlexml");
    }

    private static final class ZipEntryElement
    implements EntryElement {
        private ZipOutputStream zos;

        ZipEntryElement(ZipOutputStream zos) {
            this.zos = zos;
        }

        public OutputStream openEntry(String name) throws IOException {
            ZipEntry entry = new ZipEntry(name);
            this.zos.putNextEntry(entry);
            return this.zos;
        }

        public void closeEntry() throws IOException {
            this.zos.flush();
            this.zos.closeEntry();
        }
    }

    private static final class SingleDocElement
    implements EntryElement {
        private final OutputStream os;

        SingleDocElement(OutputStream os) {
            this.os = os;
        }

        public OutputStream openEntry(String name) throws IOException {
            return this.os;
        }

        public void closeEntry() throws IOException {
            this.os.flush();
        }
    }

    private static interface EntryElement {
        public OutputStream openEntry(String var1) throws IOException;

        public void closeEntry() throws IOException;
    }

    private static final class OutputSlicingHandler
    extends DefaultHandler {
        private final String subdocumentRoot;
        private ContentHandlerFactory subdocumentHandlerFactory;
        private final EntryElement entryElement;
        private boolean isXml;
        private boolean subdocument = false;
        private ContentHandler subdocumentHandler;

        OutputSlicingHandler(ContentHandlerFactory subdocumentHandlerFactory, EntryElement entryElement, boolean isXml) {
            this.subdocumentRoot = "class";
            this.subdocumentHandlerFactory = subdocumentHandlerFactory;
            this.entryElement = entryElement;
            this.isXml = isXml;
        }

        public final void startElement(String namespaceURI, String localName, String qName, Attributes list) throws SAXException {
            if (this.subdocument) {
                this.subdocumentHandler.startElement(namespaceURI, localName, qName, list);
            } else if (localName.equals(this.subdocumentRoot)) {
                String name = list.getValue("name");
                if (name == null || name.length() == 0) {
                    throw new SAXException("Class element without name attribute.");
                }
                try {
                    this.entryElement.openEntry(this.isXml ? name + ".class.xml" : name + ".class");
                }
                catch (IOException ex) {
                    throw new SAXException(ex.toString(), ex);
                }
                this.subdocumentHandler = this.subdocumentHandlerFactory.createContentHandler();
                this.subdocumentHandler.startDocument();
                this.subdocumentHandler.startElement(namespaceURI, localName, qName, list);
                this.subdocument = true;
            }
        }

        public final void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            if (this.subdocument) {
                this.subdocumentHandler.endElement(namespaceURI, localName, qName);
                if (localName.equals(this.subdocumentRoot)) {
                    this.subdocumentHandler.endDocument();
                    this.subdocument = false;
                    try {
                        this.entryElement.closeEntry();
                    }
                    catch (IOException ex) {
                        throw new SAXException(ex.toString(), ex);
                    }
                }
            }
        }

        public final void startDocument() throws SAXException {
        }

        public final void endDocument() throws SAXException {
        }

        public final void characters(char[] buff, int offset, int size) throws SAXException {
            if (this.subdocument) {
                this.subdocumentHandler.characters(buff, offset, size);
            }
        }
    }

    private static final class InputSlicingHandler
    extends DefaultHandler {
        private String subdocumentRoot;
        private final ContentHandler rootHandler;
        private ContentHandlerFactory subdocumentHandlerFactory;
        private boolean subdocument = false;
        private ContentHandler subdocumentHandler;

        InputSlicingHandler(String subdocumentRoot, ContentHandler rootHandler, ContentHandlerFactory subdocumentHandlerFactory) {
            this.subdocumentRoot = subdocumentRoot;
            this.rootHandler = rootHandler;
            this.subdocumentHandlerFactory = subdocumentHandlerFactory;
        }

        public final void startElement(String namespaceURI, String localName, String qName, Attributes list) throws SAXException {
            if (this.subdocument) {
                this.subdocumentHandler.startElement(namespaceURI, localName, qName, list);
            } else if (localName.equals(this.subdocumentRoot)) {
                this.subdocumentHandler = this.subdocumentHandlerFactory.createContentHandler();
                this.subdocumentHandler.startDocument();
                this.subdocumentHandler.startElement(namespaceURI, localName, qName, list);
                this.subdocument = true;
            } else if (this.rootHandler != null) {
                this.rootHandler.startElement(namespaceURI, localName, qName, list);
            }
        }

        public final void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            if (this.subdocument) {
                this.subdocumentHandler.endElement(namespaceURI, localName, qName);
                if (localName.equals(this.subdocumentRoot)) {
                    this.subdocumentHandler.endDocument();
                    this.subdocument = false;
                }
            } else if (this.rootHandler != null) {
                this.rootHandler.endElement(namespaceURI, localName, qName);
            }
        }

        public final void startDocument() throws SAXException {
            if (this.rootHandler != null) {
                this.rootHandler.startDocument();
            }
        }

        public final void endDocument() throws SAXException {
            if (this.rootHandler != null) {
                this.rootHandler.endDocument();
            }
        }

        public final void characters(char[] buff, int offset, int size) throws SAXException {
            if (this.subdocument) {
                this.subdocumentHandler.characters(buff, offset, size);
            } else if (this.rootHandler != null) {
                this.rootHandler.characters(buff, offset, size);
            }
        }
    }

    private static final class SAXWriter
    extends DefaultHandler
    implements LexicalHandler {
        private static final char[] OFF = "                                                                                                        ".toCharArray();
        private Writer w;
        private final boolean optimizeEmptyElements;
        private boolean openElement = false;
        private int ident = 0;

        SAXWriter(Writer w2, boolean optimizeEmptyElements) {
            this.w = w2;
            this.optimizeEmptyElements = optimizeEmptyElements;
        }

        public final void startElement(String ns, String localName, String qName, Attributes atts) throws SAXException {
            try {
                this.closeElement();
                this.writeIdent();
                this.w.write('<' + qName);
                if (atts != null && atts.getLength() > 0) {
                    this.writeAttributes(atts);
                }
                if (this.optimizeEmptyElements) {
                    this.openElement = true;
                } else {
                    this.w.write(">\n");
                }
                this.ident += 2;
            }
            catch (IOException ex) {
                throw new SAXException(ex);
            }
        }

        public final void endElement(String ns, String localName, String qName) throws SAXException {
            this.ident -= 2;
            try {
                if (this.openElement) {
                    this.w.write("/>\n");
                    this.openElement = false;
                } else {
                    this.writeIdent();
                    this.w.write("</" + qName + ">\n");
                }
            }
            catch (IOException ex) {
                throw new SAXException(ex);
            }
        }

        public final void endDocument() throws SAXException {
            try {
                this.w.flush();
            }
            catch (IOException ex) {
                throw new SAXException(ex);
            }
        }

        public final void comment(char[] ch, int off, int len) throws SAXException {
            try {
                this.closeElement();
                this.writeIdent();
                this.w.write("<!-- ");
                this.w.write(ch, off, len);
                this.w.write(" -->\n");
            }
            catch (IOException ex) {
                throw new SAXException(ex);
            }
        }

        public final void startDTD(String arg0, String arg1, String arg2) throws SAXException {
        }

        public final void endDTD() throws SAXException {
        }

        public final void startEntity(String arg0) throws SAXException {
        }

        public final void endEntity(String arg0) throws SAXException {
        }

        public final void startCDATA() throws SAXException {
        }

        public final void endCDATA() throws SAXException {
        }

        private final void writeAttributes(Attributes atts) throws IOException {
            StringBuffer sb = new StringBuffer();
            int len = atts.getLength();
            for (int i2 = 0; i2 < len; ++i2) {
                sb.append(' ').append(atts.getLocalName(i2)).append("=\"").append(SAXWriter.esc(atts.getValue(i2))).append('\"');
            }
            this.w.write(sb.toString());
        }

        private static final String esc(String str) {
            StringBuffer sb = new StringBuffer(str.length());
            block6: for (int i2 = 0; i2 < str.length(); ++i2) {
                char ch = str.charAt(i2);
                switch (ch) {
                    case '&': {
                        sb.append("&amp;");
                        continue block6;
                    }
                    case '<': {
                        sb.append("&lt;");
                        continue block6;
                    }
                    case '>': {
                        sb.append("&gt;");
                        continue block6;
                    }
                    case '\"': {
                        sb.append("&quot;");
                        continue block6;
                    }
                    default: {
                        if (ch > '\u007f') {
                            sb.append("&#").append(Integer.toString(ch)).append(';');
                            continue block6;
                        }
                        sb.append(ch);
                    }
                }
            }
            return sb.toString();
        }

        private final void writeIdent() throws IOException {
            int n2 = this.ident;
            while (n2 > 0) {
                if (n2 > OFF.length) {
                    this.w.write(OFF);
                    n2 -= OFF.length;
                    continue;
                }
                this.w.write(OFF, 0, n2);
                n2 = 0;
            }
        }

        private final void closeElement() throws IOException {
            if (this.openElement) {
                this.w.write(">\n");
            }
            this.openElement = false;
        }
    }

    private static final class SubdocumentHandlerFactory
    implements ContentHandlerFactory {
        private final ContentHandler subdocumentHandler;

        SubdocumentHandlerFactory(ContentHandler subdocumentHandler) {
            this.subdocumentHandler = subdocumentHandler;
        }

        public final ContentHandler createContentHandler() {
            return this.subdocumentHandler;
        }
    }

    private static final class TransformerHandlerFactory
    implements ContentHandlerFactory {
        private SAXTransformerFactory saxtf;
        private final Templates templates;
        private ContentHandler outputHandler;

        TransformerHandlerFactory(SAXTransformerFactory saxtf, Templates templates, ContentHandler outputHandler) {
            this.saxtf = saxtf;
            this.templates = templates;
            this.outputHandler = outputHandler;
        }

        public final ContentHandler createContentHandler() {
            try {
                TransformerHandler handler = this.saxtf.newTransformerHandler(this.templates);
                handler.setResult(new SAXResult(this.outputHandler));
                return handler;
            }
            catch (TransformerConfigurationException ex) {
                throw new RuntimeException(ex.toString());
            }
        }
    }

    private static final class ASMContentHandlerFactory
    implements ContentHandlerFactory {
        final OutputStream os;

        ASMContentHandlerFactory(OutputStream os) {
            this.os = os;
        }

        public final ContentHandler createContentHandler() {
            final ClassWriter cw = new ClassWriter(1);
            return new ASMContentHandler(cw){

                public void endDocument() throws SAXException {
                    try {
                        ASMContentHandlerFactory.this.os.write(cw.toByteArray());
                    }
                    catch (IOException e2) {
                        throw new SAXException(e2);
                    }
                }
            };
        }
    }

    private static final class SAXWriterFactory
    implements ContentHandlerFactory {
        private final Writer w;
        private final boolean optimizeEmptyElements;

        SAXWriterFactory(Writer w2, boolean optimizeEmptyElements) {
            this.w = w2;
            this.optimizeEmptyElements = optimizeEmptyElements;
        }

        public final ContentHandler createContentHandler() {
            return new SAXWriter(this.w, this.optimizeEmptyElements);
        }
    }

    private static interface ContentHandlerFactory {
        public ContentHandler createContentHandler();
    }

    private static final class ProtectedInputStream
    extends InputStream {
        private final InputStream is;

        ProtectedInputStream(InputStream is) {
            this.is = is;
        }

        public final void close() throws IOException {
        }

        public final int read() throws IOException {
            return this.is.read();
        }

        public final int read(byte[] b2, int off, int len) throws IOException {
            return this.is.read(b2, off, len);
        }

        public final int available() throws IOException {
            return this.is.available();
        }
    }
}

