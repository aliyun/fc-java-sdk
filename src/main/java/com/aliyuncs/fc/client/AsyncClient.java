package com.aliyuncs.fc.client;

import com.aliyuncs.fc.core.AsyncCompletion;
import com.aliyuncs.fc.core.FutureCallback;
import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.http.consumers.*;
import com.aliyuncs.fc.request.*;
import com.aliyuncs.fc.response.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.aliyuncs.fc.model.HttpMethod.*;


public class AsyncClient implements AsyncClientInterface {
    private final static String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private final static String CONTENT_TYPE_APPLICATION_STREAM = "application/octet-stream";

    private Config config;
    private AsyncInternalClient internalClient;
    private ExecutorService executor;

    public AsyncClient(Config config){
        this.config = config;
        internalClient = new AsyncInternalClient(config);
        executor = Executors.newFixedThreadPool(config.getThreadCount());
    }

    @Override
    public Future<CreateFunctionResponse> createFunction(CreateFunctionRequest request, FcCallback<CreateFunctionRequest, CreateFunctionResponse> callback) {
        AsyncCompletion<CreateFunctionRequest, CreateFunctionResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<CreateFunctionRequest, CreateFunctionResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new CreateFunctionResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, POST, false);
        return f;
    }

    @Override
    public Future<InvokeFunctionResponse> invokeFunction(InvokeFunctionRequest request,
                                                         FcCallback<InvokeFunctionRequest, InvokeFunctionResponse> callback)
            throws ClientException, ServerException {
        AsyncCompletion<InvokeFunctionRequest, InvokeFunctionResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<InvokeFunctionRequest, InvokeFunctionResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        if (request instanceof HttpInvokeFunctionRequest) {
            String contentType = CONTENT_TYPE_APPLICATION_STREAM;

            if (request.getHeaders() != null && request.getHeaders().containsKey("Content-Type")) {
                contentType = request.getHeaders().get("Content-Type");
            }

            internalClient.asyncSend(request, new InvokeFunctionResponseConsumer(), completion, contentType,
                    ((HttpInvokeFunctionRequest) request).getMethod(), true);
        } else {
            internalClient.asyncSend(request, new InvokeFunctionResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_STREAM,
                    POST, false);
        }

        return f;
    }

    @Override
    public Future<UpdateFunctionResponse> updateFunction(UpdateFunctionRequest request, FcCallback<UpdateFunctionRequest, UpdateFunctionResponse> callback) {
        AsyncCompletion<UpdateFunctionRequest, UpdateFunctionResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<UpdateFunctionRequest, UpdateFunctionResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new UpdateFunctionResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, PUT, false);
        return f;
    }

    @Override
    public Future<DeleteFunctionResponse> deleteFunction(DeleteFunctionRequest request, FcCallback<DeleteFunctionRequest, DeleteFunctionResponse> callback) {
        AsyncCompletion<DeleteFunctionRequest, DeleteFunctionResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<DeleteFunctionRequest, DeleteFunctionResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new DeleteFunctionResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, DELETE, false);
        return f;
    }

    @Override
    public Future<ListFunctionsResponse> listFunctions(ListFunctionsRequest request, FcCallback<ListFunctionsRequest, ListFunctionsResponse> callback) {
        AsyncCompletion<ListFunctionsRequest, ListFunctionsResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<ListFunctionsRequest, ListFunctionsResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new ListFunctionsResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, GET, false);
        return f;
    }

    @Override
    public Future<GetFunctionResponse> getFunction(GetFunctionRequest request, FcCallback<GetFunctionRequest, GetFunctionResponse> callback) {
        AsyncCompletion<GetFunctionRequest, GetFunctionResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<GetFunctionRequest, GetFunctionResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new GetFunctionResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, GET, false);
        return f;
    }

    @Override
    public Future<CreateServiceResponse> createService(CreateServiceRequest request, FcCallback<CreateServiceRequest, CreateServiceResponse> callback) {
        AsyncCompletion<CreateServiceRequest, CreateServiceResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<CreateServiceRequest, CreateServiceResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new CreateServiceResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, POST, false);
        return f;
    }

    @Override
    public Future<UpdateServiceResponse> updateService(UpdateServiceRequest request, FcCallback<UpdateServiceRequest, UpdateServiceResponse> callback) {
        AsyncCompletion<UpdateServiceRequest, UpdateServiceResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<UpdateServiceRequest, UpdateServiceResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new UpdateServiceResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, PUT, false);
        return f;
    }

    @Override
    public Future<DeleteServiceResponse> deleteService(DeleteServiceRequest request, FcCallback<DeleteServiceRequest, DeleteServiceResponse> callback) {
        AsyncCompletion<DeleteServiceRequest, DeleteServiceResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<DeleteServiceRequest, DeleteServiceResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new DeleteServiceResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, DELETE, false);
        return f;
    }

    @Override
    public Future<ListServicesResponse> listServices(ListServicesRequest request, FcCallback<ListServicesRequest, ListServicesResponse> callback) {
        AsyncCompletion<ListServicesRequest, ListServicesResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<ListServicesRequest, ListServicesResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new ListServicesResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, GET, false);
        return f;
    }

    @Override
    public Future<GetServiceResponse> getService(GetServiceRequest request, FcCallback<GetServiceRequest, GetServiceResponse> callback) {
        AsyncCompletion<GetServiceRequest, GetServiceResponse> completion = new AsyncCompletion(request, executor);
        FutureCallback<GetServiceRequest, GetServiceResponse> f = new FutureCallback();
        completion.addCallback(f);
        if (callback != null) {
            f.addCallback(callback);
        }

        internalClient.asyncSend(request, new GetServiceResponseConsumer(), completion, CONTENT_TYPE_APPLICATION_JSON, GET, false);
        return f;
    }

    @Override
    public void close() throws IOException {
        executor.shutdownNow();
        internalClient.close();
    }
}
