package com.aliyuncs.fc.client;

import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.consumers.AbstractResponseConsumer;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.HttpMethod;
import com.aliyuncs.fc.model.PrepareUrl;
import com.aliyuncs.fc.request.HttpInvokeFunctionRequest;
import com.aliyuncs.fc.utils.FcUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Map;

import static com.aliyuncs.fc.model.HttpAuthType.ANONYMOUS;


public class AsyncInternalClient {
    private Config config;
    private CloseableHttpAsyncClient httpClient;

    public AsyncInternalClient(Config config){
        this.config = config;

        try {
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom().build();
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
            cm.setMaxTotal(config.getMaxConnectCount());
            cm.setDefaultMaxPerRoute(config.getMaxPerRoute());
            httpClient = createHttpAsyncClient(config, cm);
            httpClient.start();
        } catch (IOReactorException e) {
            throw new ClientException(e);
        }
    }

    private CloseableHttpAsyncClient createHttpAsyncClient(Config config, PoolingNHttpClientConnectionManager cm){
        HttpAsyncClientBuilder httpClientBuilder = HttpAsyncClients.custom();
        httpClientBuilder.setConnectionManager(cm);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(config.getConnectTimeoutMillis())
                .setConnectionRequestTimeout(config.getConnectionRequestTimeoutMillis())
                .setSocketTimeout(config.getReadTimeoutMillis())
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setUserAgent(config.getUserAgent());
        httpClientBuilder.disableCookieManagement();

        return httpClientBuilder.build();
    }

    protected <Res> void asyncSend(HttpRequest request, AbstractResponseConsumer<Res> consumer, FutureCallback<Res> callback,
                                   String contentType, HttpMethod method, boolean httpInvoke)
            throws ClientException {
        try{
            // try refresh credentials if CredentialProvider set
            config.refreshCredentials();

            // Add all needed headers
            if (!httpInvoke
                    || !ANONYMOUS.equals(((HttpInvokeFunctionRequest) request).getAuthType())) {
                FcUtil.signRequest(config, request, contentType, method, httpInvoke);
            }

            // Construct HttpRequest
            PrepareUrl prepareUrl = FcUtil.prepareUrl(request.getPath(), request.getQueryParams(), this.config);
            RequestBuilder requestBuilder = RequestBuilder.create(method.name())
                    .setUri(prepareUrl.getUrl());
            copyToRequest(request, requestBuilder);
            HttpUriRequest httpRequest = requestBuilder.build();

            HttpHost httpHost = URIUtils.extractHost(httpRequest.getURI());
            httpClient.execute(new FcRequestProducer(httpHost, httpRequest), consumer, callback);
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    private void copyToRequest(HttpRequest request, RequestBuilder requestBuilder){
        if (request.getPayload() != null) {
            requestBuilder.setEntity(new ByteArrayEntity(request.getPayload()));
        }

        Map<String, String> headers = request.getHeaders();
        for(Map.Entry<String, String> item : headers.entrySet()) {
            requestBuilder.setHeader(item.getKey(), item.getValue());
        }
    }

    static class FcRequestProducer extends BasicAsyncRequestProducer {

        public FcRequestProducer(HttpHost target, org.apache.http.HttpRequest request) {
            super(target, request);
        }

        public void requestCompleted(HttpContext context) {
            super.requestCompleted(context);
        }
    }

    public void close() throws IOException {
        httpClient.close();
    }

}
