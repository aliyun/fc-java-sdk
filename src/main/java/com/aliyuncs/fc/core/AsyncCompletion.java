package com.aliyuncs.fc.core;

import com.aliyuncs.fc.client.FcCallback;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

public class AsyncCompletion<Req extends HttpRequest, Res extends  HttpResponse> extends AbstractWatchableCallback<Req, Res>
        implements FutureCallback<Res> {
    private Req request;
    private ExecutorService executor;

    public AsyncCompletion(Req request, ExecutorService executor){
        this.request = request;
        this.executor = executor;
    }

    @Override
    public void completed(Res res) {
        this.onCompleted(request, res);
    }

    @Override
    public void failed(Exception ex) {
        final Object e;
        if (ex instanceof ClientException) {
            e = ex;
        } else if (ex instanceof ServerException) {
            e = ex;
        } else if(ex instanceof SocketTimeoutException) {
            e = new ClientException("SDK.ServerUnreachable",
                    "SocketTimeoutException has occurred on a socket read or accept.");
        } else if (ex instanceof InvalidKeyException) {
            e = new ClientException("SDK.InvalidAccessSecret",
                    "Speicified access secret is not valid.");
        } else if (ex instanceof IOException) {
            e = new ClientException("SDK.ServerUnreachable",
                    "Server unreachable: " + ex.toString());
        } else if (ex instanceof NoSuchAlgorithmException) {
            e = new ClientException("SDK.InvalidMD5Algorithm",
                    "MD5 hash is not supported by client side.");
        } else if (ex instanceof URISyntaxException) {
            throw new ClientException("SDK.InvalidURL", "url is not valid: " + ex.getMessage());
        } else {
            ex.printStackTrace();
            e = new ClientException("SDK.UnknownError", "Unknown client error", ex.getCause());
        }
        this.onFailed(request, (Exception) e);
    }

    @Override
    public void cancelled() {

    }

    public void onCompleted(final Req req, final Res res) {
        Iterator it = callbacks.iterator();

        while(it.hasNext()) {
            final FcCallback<Req, Res> cb = (FcCallback)it.next();
            executor.submit(new Runnable() {
                public void run() {
                    cb.onCompleted(req, res);
                }
            });
        }
    }

    public void onFailed(final Req req, final Exception exception) {
        Iterator it = callbacks.iterator();

        while(it.hasNext()) {
            final FcCallback<Req, Res> cb = (FcCallback)it.next();
            executor.submit(new Runnable() {
                public void run() {
                    cb.onFailed(req, exception);
                }
            });
        }
    }
}
