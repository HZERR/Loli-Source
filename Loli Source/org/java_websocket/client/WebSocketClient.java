/*
 * Decompiled with CFR 0.150.
 */
package org.java_websocket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import org.java_websocket.AbstractWebSocket;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketListener;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.Opcode;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.HandshakeImpl1Client;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;

public abstract class WebSocketClient
extends AbstractWebSocket
implements Runnable,
WebSocket {
    protected URI uri = null;
    private WebSocketImpl engine = null;
    private Socket socket = null;
    private SocketFactory socketFactory = null;
    private OutputStream ostream;
    private Proxy proxy = Proxy.NO_PROXY;
    private Thread writeThread;
    private Thread connectReadThread;
    private Draft draft;
    private Map<String, String> headers;
    private CountDownLatch connectLatch = new CountDownLatch(1);
    private CountDownLatch closeLatch = new CountDownLatch(1);
    private int connectTimeout = 0;

    public WebSocketClient(URI serverUri) {
        this(serverUri, new Draft_6455());
    }

    public WebSocketClient(URI serverUri, Draft protocolDraft) {
        this(serverUri, protocolDraft, null, 0);
    }

    public WebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        this(serverUri, new Draft_6455(), httpHeaders);
    }

    public WebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
        this(serverUri, protocolDraft, httpHeaders, 0);
    }

    public WebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        if (serverUri == null) {
            throw new IllegalArgumentException();
        }
        if (protocolDraft == null) {
            throw new IllegalArgumentException("null as draft is permitted for `WebSocketServer` only!");
        }
        this.uri = serverUri;
        this.draft = protocolDraft;
        this.headers = httpHeaders;
        this.connectTimeout = connectTimeout;
        this.setTcpNoDelay(false);
        this.setReuseAddr(false);
        this.engine = new WebSocketImpl((WebSocketListener)this, protocolDraft);
    }

    public URI getURI() {
        return this.uri;
    }

    @Override
    public Draft getDraft() {
        return this.draft;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void reconnect() {
        this.reset();
        this.connect();
    }

    public boolean reconnectBlocking() throws InterruptedException {
        this.reset();
        return this.connectBlocking();
    }

    private void reset() {
        Thread current = Thread.currentThread();
        if (current == this.writeThread || current == this.connectReadThread) {
            throw new IllegalStateException("You cannot initialize a reconnect out of the websocket thread. Use reconnect in another thread to insure a successful cleanup.");
        }
        try {
            this.closeBlocking();
            if (this.writeThread != null) {
                this.writeThread.interrupt();
                this.writeThread = null;
            }
            if (this.connectReadThread != null) {
                this.connectReadThread.interrupt();
                this.connectReadThread = null;
            }
            this.draft.reset();
            if (this.socket != null) {
                this.socket.close();
                this.socket = null;
            }
        }
        catch (Exception e2) {
            this.onError(e2);
            this.engine.closeConnection(1006, e2.getMessage());
            return;
        }
        this.connectLatch = new CountDownLatch(1);
        this.closeLatch = new CountDownLatch(1);
        this.engine = new WebSocketImpl((WebSocketListener)this, this.draft);
    }

    public void connect() {
        if (this.connectReadThread != null) {
            throw new IllegalStateException("WebSocketClient objects are not reuseable");
        }
        this.connectReadThread = new Thread(this);
        this.connectReadThread.setName("WebSocketConnectReadThread-" + this.connectReadThread.getId());
        this.connectReadThread.start();
    }

    public boolean connectBlocking() throws InterruptedException {
        this.connect();
        this.connectLatch.await();
        return this.engine.isOpen();
    }

    public boolean connectBlocking(long timeout, TimeUnit timeUnit) throws InterruptedException {
        this.connect();
        return this.connectLatch.await(timeout, timeUnit) && this.engine.isOpen();
    }

    @Override
    public void close() {
        if (this.writeThread != null) {
            this.engine.close(1000);
        }
    }

    public void closeBlocking() throws InterruptedException {
        this.close();
        this.closeLatch.await();
    }

    @Override
    public void send(String text) {
        this.engine.send(text);
    }

    @Override
    public void send(byte[] data) {
        this.engine.send(data);
    }

    @Override
    public <T> T getAttachment() {
        return this.engine.getAttachment();
    }

    @Override
    public <T> void setAttachment(T attachment) {
        this.engine.setAttachment(attachment);
    }

    @Override
    protected Collection<WebSocket> getConnections() {
        return Collections.singletonList(this.engine);
    }

    @Override
    public void sendPing() {
        this.engine.sendPing();
    }

    @Override
    public void run() {
        InputStream istream;
        try {
            boolean isNewSocket = false;
            if (this.socketFactory != null) {
                this.socket = this.socketFactory.createSocket();
            } else if (this.socket == null) {
                this.socket = new Socket(this.proxy);
                isNewSocket = true;
            } else if (this.socket.isClosed()) {
                throw new IOException();
            }
            this.socket.setTcpNoDelay(this.isTcpNoDelay());
            this.socket.setReuseAddress(this.isReuseAddr());
            if (!this.socket.isBound()) {
                this.socket.connect(new InetSocketAddress(this.uri.getHost(), this.getPort()), this.connectTimeout);
            }
            if (isNewSocket && "wss".equals(this.uri.getScheme())) {
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, null, null);
                SSLSocketFactory factory = sslContext.getSocketFactory();
                this.socket = factory.createSocket(this.socket, this.uri.getHost(), this.getPort(), true);
            }
            istream = this.socket.getInputStream();
            this.ostream = this.socket.getOutputStream();
            this.sendHandshake();
        }
        catch (Exception e2) {
            this.onWebsocketError(this.engine, e2);
            this.engine.closeConnection(-1, e2.getMessage());
            return;
        }
        this.writeThread = new Thread(new WebsocketWriteThread(this));
        this.writeThread.start();
        byte[] rawbuffer = new byte[16384];
        try {
            int readBytes;
            while (!this.isClosing() && !this.isClosed() && (readBytes = istream.read(rawbuffer)) != -1) {
                this.engine.decode(ByteBuffer.wrap(rawbuffer, 0, readBytes));
            }
            this.engine.eot();
        }
        catch (IOException e3) {
            this.handleIOException(e3);
        }
        catch (RuntimeException e4) {
            this.onError(e4);
            this.engine.closeConnection(1006, e4.getMessage());
        }
        this.connectReadThread = null;
    }

    private int getPort() {
        int port = this.uri.getPort();
        if (port == -1) {
            String scheme = this.uri.getScheme();
            if ("wss".equals(scheme)) {
                return 443;
            }
            if ("ws".equals(scheme)) {
                return 80;
            }
            throw new IllegalArgumentException("unknown scheme: " + scheme);
        }
        return port;
    }

    private void sendHandshake() throws InvalidHandshakeException {
        String part1 = this.uri.getRawPath();
        String part2 = this.uri.getRawQuery();
        String path = part1 == null || part1.length() == 0 ? "/" : part1;
        if (part2 != null) {
            path = path + '?' + part2;
        }
        int port = this.getPort();
        String host = this.uri.getHost() + (port != 80 && port != 443 ? ":" + port : "");
        HandshakeImpl1Client handshake = new HandshakeImpl1Client();
        handshake.setResourceDescriptor(path);
        handshake.put("Host", host);
        if (this.headers != null) {
            for (Map.Entry<String, String> kv : this.headers.entrySet()) {
                handshake.put(kv.getKey(), kv.getValue());
            }
        }
        this.engine.startHandshake(handshake);
    }

    @Override
    public ReadyState getReadyState() {
        return this.engine.getReadyState();
    }

    @Override
    public final void onWebsocketMessage(WebSocket conn, String message) {
        this.onMessage(message);
    }

    @Override
    public final void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {
        this.onMessage(blob);
    }

    @Override
    public final void onWebsocketOpen(WebSocket conn, Handshakedata handshake) {
        this.startConnectionLostTimer();
        this.onOpen((ServerHandshake)handshake);
        this.connectLatch.countDown();
    }

    @Override
    public final void onWebsocketClose(WebSocket conn, int code, String reason, boolean remote) {
        this.stopConnectionLostTimer();
        if (this.writeThread != null) {
            this.writeThread.interrupt();
        }
        this.onClose(code, reason, remote);
        this.connectLatch.countDown();
        this.closeLatch.countDown();
    }

    @Override
    public final void onWebsocketError(WebSocket conn, Exception ex) {
        this.onError(ex);
    }

    @Override
    public final void onWriteDemand(WebSocket conn) {
    }

    @Override
    public void onWebsocketCloseInitiated(WebSocket conn, int code, String reason) {
        this.onCloseInitiated(code, reason);
    }

    @Override
    public void onWebsocketClosing(WebSocket conn, int code, String reason, boolean remote) {
        this.onClosing(code, reason, remote);
    }

    public void onCloseInitiated(int code, String reason) {
    }

    public void onClosing(int code, String reason, boolean remote) {
    }

    public WebSocket getConnection() {
        return this.engine;
    }

    @Override
    public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
        if (this.socket != null) {
            return (InetSocketAddress)this.socket.getLocalSocketAddress();
        }
        return null;
    }

    @Override
    public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
        if (this.socket != null) {
            return (InetSocketAddress)this.socket.getRemoteSocketAddress();
        }
        return null;
    }

    public abstract void onOpen(ServerHandshake var1);

    public abstract void onMessage(String var1);

    public abstract void onClose(int var1, String var2, boolean var3);

    public abstract void onError(Exception var1);

    public void onMessage(ByteBuffer bytes) {
    }

    public void setProxy(Proxy proxy) {
        if (proxy == null) {
            throw new IllegalArgumentException();
        }
        this.proxy = proxy;
    }

    @Deprecated
    public void setSocket(Socket socket) {
        if (this.socket != null) {
            throw new IllegalStateException("socket has already been set");
        }
        this.socket = socket;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    @Override
    public void sendFragmentedFrame(Opcode op, ByteBuffer buffer, boolean fin) {
        this.engine.sendFragmentedFrame(op, buffer, fin);
    }

    @Override
    public boolean isOpen() {
        return this.engine.isOpen();
    }

    @Override
    public boolean isFlushAndClose() {
        return this.engine.isFlushAndClose();
    }

    @Override
    public boolean isClosed() {
        return this.engine.isClosed();
    }

    @Override
    public boolean isClosing() {
        return this.engine.isClosing();
    }

    @Override
    public boolean hasBufferedData() {
        return this.engine.hasBufferedData();
    }

    @Override
    public void close(int code) {
        this.engine.close(code);
    }

    @Override
    public void close(int code, String message) {
        this.engine.close(code, message);
    }

    @Override
    public void closeConnection(int code, String message) {
        this.engine.closeConnection(code, message);
    }

    @Override
    public void send(ByteBuffer bytes) {
        this.engine.send(bytes);
    }

    @Override
    public void sendFrame(Framedata framedata) {
        this.engine.sendFrame(framedata);
    }

    @Override
    public void sendFrame(Collection<Framedata> frames) {
        this.engine.sendFrame(frames);
    }

    @Override
    public InetSocketAddress getLocalSocketAddress() {
        return this.engine.getLocalSocketAddress();
    }

    @Override
    public InetSocketAddress getRemoteSocketAddress() {
        return this.engine.getRemoteSocketAddress();
    }

    @Override
    public String getResourceDescriptor() {
        return this.uri.getPath();
    }

    private void handleIOException(IOException e2) {
        if (e2 instanceof SSLException) {
            this.onError(e2);
        }
        this.engine.eot();
    }

    private class WebsocketWriteThread
    implements Runnable {
        private final WebSocketClient webSocketClient;

        WebsocketWriteThread(WebSocketClient webSocketClient2) {
            this.webSocketClient = webSocketClient2;
        }

        @Override
        public void run() {
            Thread.currentThread().setName("WebSocketWriteThread-" + Thread.currentThread().getId());
            try {
                this.runWriteData();
            }
            catch (IOException e2) {
                WebSocketClient.this.handleIOException(e2);
            }
            finally {
                this.closeSocket();
                WebSocketClient.this.writeThread = null;
            }
        }

        private void runWriteData() throws IOException {
            try {
                while (!Thread.interrupted()) {
                    ByteBuffer buffer = ((WebSocketClient)WebSocketClient.this).engine.outQueue.take();
                    WebSocketClient.this.ostream.write(buffer.array(), 0, buffer.limit());
                    WebSocketClient.this.ostream.flush();
                }
            }
            catch (InterruptedException e2) {
                for (ByteBuffer buffer : ((WebSocketClient)WebSocketClient.this).engine.outQueue) {
                    WebSocketClient.this.ostream.write(buffer.array(), 0, buffer.limit());
                    WebSocketClient.this.ostream.flush();
                }
                Thread.currentThread().interrupt();
            }
        }

        private void closeSocket() {
            try {
                if (WebSocketClient.this.socket != null) {
                    WebSocketClient.this.socket.close();
                }
            }
            catch (IOException ex) {
                WebSocketClient.this.onWebsocketError(this.webSocketClient, ex);
            }
        }
    }
}

