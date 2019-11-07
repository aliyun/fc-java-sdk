package com.aliyuncs.fc.http.consumers;

import java.io.IOException;
import java.io.InputStream;

import com.aliyuncs.fc.constants.HeaderKeys;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.utils.FcUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.apache.commons.io.IOUtils;
import org.apache.http.ContentTooLongException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.entity.ContentBufferEntity;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.nio.util.SimpleInputBuffer;
import org.apache.http.protocol.HttpContext;


public abstract class AbstractResponseConsumer<Res> extends AbstractAsyncResponseConsumer<Res> {
    protected volatile HttpResponse httpResponse;
    protected volatile SimpleInputBuffer buf;

    protected abstract Res parseResult() throws Exception;

    protected Res buildResult(HttpContext context) throws Exception {
        Res result = this.parseResult();
        return result;
    }

    protected void onResponseReceived(HttpResponse response) throws IOException {
        this.httpResponse = response;
    }

    protected void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
        long len = entity.getContentLength();
        if (len > 2147483647L) {
            throw new ContentTooLongException("Entity content is too long: " + len);
        } else {
            if (len < 0L) {
                len = 4096L;
            }

            this.buf = new SimpleInputBuffer((int)len, new HeapByteBufferAllocator());
            this.httpResponse.setEntity(new ContentBufferEntity(entity, this.buf));
        }
    }

    /**
     * Convert apache HttpResponse to FC HttpResponse
     * @return
     */
    public com.aliyuncs.fc.http.HttpResponse getFcHttpResponse() throws Exception {
        com.aliyuncs.fc.http.HttpResponse response = new com.aliyuncs.fc.http.HttpResponse();

        // Status
        response.setStatus(httpResponse.getStatusLine().getStatusCode());

        // Headers
        Header[] headers = httpResponse.getAllHeaders();
        for(int i = 0; i < headers.length; i ++){
            Header header = headers[i];
            response.setHeader(header.getName(), header.getValue());
        }

        // Content
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                InputStream in = entity.getContent();
                response.setContent(IOUtils.toByteArray(in));
            }
            closeResponse(httpResponse);
        } catch (IOException e) {
            throw new ClientException("SDK.ServerUnreachable",
                    "Server unreachable: " + e.toString());
        }

        if (response.getStatus() >= 500) {
            String requestId = response.getHeader(HeaderKeys.REQUEST_ID);
            String stringContent =
                    response.getContent() == null ? "" : FcUtil.toDefaultCharset(response.getContent());
            ServerException se;
            try {
                se = new Gson().fromJson(stringContent, ServerException.class);
            } catch (JsonParseException e) {
                se = new ServerException("InternalServiceError",
                        "Failed to parse response content", requestId);
            }
            se.setStatusCode(response.getStatus());
            se.setRequestId(requestId);
            throw se;
        } else if (response.getStatus() >= 300) {
            ClientException ce;
            if (response.getContent() == null) {
                ce = new ClientException("SDK.ServerUnreachable",
                        "Failed to get response content from server");
            } else {
                try {
                    ce = new Gson()
                            .fromJson(FcUtil.toDefaultCharset(response.getContent()), ClientException.class);
                } catch (JsonParseException e) {
                    ce = new ClientException("SDK.ResponseNotParsable",
                            "Failed to parse response content", e);
                }
            }
            if (ce == null) {
                ce = new ClientException("SDK.UnknownError", "Unknown client error");
            }
            ce.setStatusCode(response.getStatus());
            ce.setRequestId(response.getHeader(HeaderKeys.REQUEST_ID));
            throw ce;
        }
        return response;
    }

    protected void onContentReceived(ContentDecoder decoder, IOControl ioControl) throws IOException {
        this.buf.consumeContent(decoder);
    }

    protected void releaseResources() {
        this.httpResponse = null;
        this.buf = null;
    }

    private void closeResponse(HttpResponse resp){
        try{
            resp.getEntity().getContent().close();
        } catch (Exception e){
        }
    }
}
