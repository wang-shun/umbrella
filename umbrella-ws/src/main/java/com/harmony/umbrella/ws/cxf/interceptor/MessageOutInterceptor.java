package com.harmony.umbrella.ws.cxf.interceptor;

import static com.harmony.umbrella.ws.cxf.CXFMessageUtils.*;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import com.harmony.umbrella.ws.cxf.log.CXFLogMessageHandler;

/**
 * @author wuxii@foxmail.com
 */
public class MessageOutInterceptor extends AbstractMessageInterceptor {

    protected CXFLogMessageHandler handler;

    public MessageOutInterceptor() {
        this("Outbound");
    }

    public MessageOutInterceptor(String heading, String phase) {
        super(heading, phase);
        addBefore(StaxOutInterceptor.class.getName());
    }

    public MessageOutInterceptor(String heading, CXFLogMessageHandler handler) {
        super(heading, Phase.PRE_STREAM);
        this.handler = handler;
        addBefore(StaxOutInterceptor.class.getName());
    }

    public MessageOutInterceptor(String heading) {
        this(heading, Phase.PRE_STREAM);
    }

    @Override
    public void handleMessage(final Message message) throws Fault {
        final OutputStream os = message.getContent(OutputStream.class);
        final Writer writer = message.getContent(Writer.class);
        if (os != null) {
            CacheAndWriteOutputStream cos = new CacheAndWriteOutputStream(os);

            cos.registerCallback(new CachedOutputStreamCallback() {
                @Override
                public void onFlush(CachedOutputStream os) {
                }

                @Override
                public void onClose(CachedOutputStream cos) {
                    message.setContent(OutputStream.class, cos);
                    logging(buildLoggingMessage(message));
                    message.setContent(OutputStream.class, os);
                }
            });

            message.setContent(OutputStream.class, cos);
        } else {
            message.setContent(Writer.class, new FilterWriter(writer) {
                @Override
                public void close() throws IOException {
                    message.setContent(Writer.class, this.out);
                    logging(buildLoggingMessage(message));
                    message.setContent(Writer.class, writer);
                }
            });
        }
    }

    @Override
    protected String getPayload(Message message) {
        final OutputStream os = message.getContent(OutputStream.class);
        final Writer writer = message.getContent(Writer.class);
        if (os != null) {
            return getPayloadFromOutputStream(message, os);
        } else if (writer != null) {
            return getPayloadFromWriter(message, writer);
        }
        return "";
    }

    protected String getPayloadFromWriter(Message message, Writer writer) {
        StringBuilder payload = new StringBuilder();
        try {
            writePayload(payload, (StringWriter) (writer instanceof StringWriter ? writer : new StringWriter()), getContentType(message));
        } catch (Exception e) {
            return "Error load payload > " + e.toString();
        }
        return payload.toString();
    }

    protected String getPayloadFromOutputStream(Message message, OutputStream os) {
        CacheAndWriteOutputStream cos = os instanceof CacheAndWriteOutputStream ? (CacheAndWriteOutputStream) os : new CacheAndWriteOutputStream(os);
        StringBuilder payload = new StringBuilder();
        try {
            writePayload(payload, cos, getEncoding(message), getContentType(message));
        } catch (Exception e) {
            return "Error load payload > " + e.toString();
        }
        return payload.toString();
    }

    public CXFLogMessageHandler getHandler() {
        return handler;
    }

    public void setHandler(CXFLogMessageHandler handler) {
        this.handler = handler;
    }

}
