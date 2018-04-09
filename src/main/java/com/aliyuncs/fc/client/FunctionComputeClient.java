package com.aliyuncs.fc.client;

import com.aliyuncs.fc.constants.HeaderKeys;
import com.aliyuncs.fc.request.*;
import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.FunctionMetadata;
import com.aliyuncs.fc.model.FunctionCodeMetadata;
import com.aliyuncs.fc.model.ServiceMetadata;
import com.aliyuncs.fc.model.TriggerMetadata;
import com.aliyuncs.fc.response.*;
import com.aliyuncs.fc.utils.Base64Helper;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class FunctionComputeClient {

    private final static String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private final static String CONTENT_TYPE_APPLICATION_STREAM = "application/octet-stream";

    private final DefaultFcClient client;
    private static final Gson GSON = new Gson();
    private final Config config;

    public FunctionComputeClient(String region, String uid, String accessKeyId, String accessKeySecret) {
        this.config = new Config(region, uid, accessKeyId, accessKeySecret, null, false);
        client = new DefaultFcClient(config);
    }

    public FunctionComputeClient(Config config) {
        this.config = config;
        client = new DefaultFcClient(config);
    }

    public Config getConfig() {
        return config;
    }

    /**
     * Override the default endpoint for the region
     * @param endpoint the endpoint that client sends requests to
     */
    public void setEndpoint(String endpoint) {
        config.setEndpoint(endpoint);
    }

    public DeleteServiceResponse deleteService(DeleteServiceRequest request)
        throws ClientException, ServerException {

        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "DELETE");
        DeleteServiceResponse deleteServiceResponse = new DeleteServiceResponse();
        deleteServiceResponse.setHeaders(response.getHeaders());
        deleteServiceResponse.setStatus(response.getStatus());
        return deleteServiceResponse;
    }

    public DeleteFunctionResponse deleteFunction(DeleteFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "DELETE");
        DeleteFunctionResponse deleteFunctionResponse = new DeleteFunctionResponse();
        deleteFunctionResponse.setHeader(response.getHeaders());
        deleteFunctionResponse.setStatus(response.getStatus());
        return deleteFunctionResponse;
    }

    public GetServiceResponse getService(GetServiceRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "GET");
        ServiceMetadata serviceMetadata = GSON.fromJson(
            new String(response.getContent()), ServiceMetadata.class);
        GetServiceResponse getServiceResponse = new GetServiceResponse();
        getServiceResponse.setServiceMetadata(serviceMetadata);
        getServiceResponse.setHeader(response.getHeaders());
        getServiceResponse.setContent(response.getContent());
        getServiceResponse.setStatus(response.getStatus());
        return getServiceResponse;
    }

    public GetFunctionResponse getFunction(GetFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "GET");
        FunctionMetadata functionMetadata = GSON.fromJson(
            new String(response.getContent()), FunctionMetadata.class);
        GetFunctionResponse getFunctionResponse = new GetFunctionResponse();
        getFunctionResponse.setFunctionMetadata(functionMetadata);
        getFunctionResponse.setHeader(response.getHeaders());
        getFunctionResponse.setContent(response.getContent());
        getFunctionResponse.setStatus(response.getStatus());
        return getFunctionResponse;
    }

    public GetFunctionCodeResponse getFunctionCode(GetFunctionCodeRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "GET");
        FunctionCodeMetadata functionCodeMetadata = GSON.fromJson(
            new String(response.getContent()), FunctionCodeMetadata.class);
        GetFunctionCodeResponse getFunctionCodeResponse = new GetFunctionCodeResponse();
        getFunctionCodeResponse.setFunctionCodeMetadata(functionCodeMetadata);
        getFunctionCodeResponse.setHeader(response.getHeaders());
        getFunctionCodeResponse.setContent(response.getContent());
        getFunctionCodeResponse.setStatus(response.getStatus());
        return getFunctionCodeResponse;
    }

    public CreateServiceResponse createService(CreateServiceRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "POST");
        ServiceMetadata serviceMetadata = GSON.fromJson(
            new String(response.getContent()), ServiceMetadata.class);
        CreateServiceResponse createServiceResponse = new CreateServiceResponse();
        createServiceResponse.setServiceMetadata(serviceMetadata);
        createServiceResponse.setHeaders(response.getHeaders());
        createServiceResponse.setContent(response.getContent());
        createServiceResponse.setStatus(response.getStatus());
        return createServiceResponse;
    }

    public CreateFunctionResponse createFunction(CreateFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "POST");
        FunctionMetadata functionMetadata = GSON.fromJson(
            new String(response.getContent()), FunctionMetadata.class);
        CreateFunctionResponse createFunctionResponse = new CreateFunctionResponse();
        createFunctionResponse.setFunctionMetadata(functionMetadata);
        createFunctionResponse.setHeader(response.getHeaders());
        createFunctionResponse.setContent(response.getContent());
        createFunctionResponse.setStatus(response.getStatus());
        return createFunctionResponse;
    }

    public UpdateServiceResponse updateService(UpdateServiceRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "PUT");
        ServiceMetadata serviceMetadata = GSON.fromJson(
            new String(response.getContent()), ServiceMetadata.class);
        UpdateServiceResponse updateServiceResponse = new UpdateServiceResponse();
        updateServiceResponse.setServiceMetadata(serviceMetadata);
        updateServiceResponse.setHeader(response.getHeaders());
        updateServiceResponse.setContent(response.getContent());
        updateServiceResponse.setStatus(response.getStatus());
        return updateServiceResponse;
    }

    public UpdateFunctionResponse updateFunction(UpdateFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "PUT");
        FunctionMetadata functionMetadata = GSON.fromJson(
            new String(response.getContent()), FunctionMetadata.class);
        UpdateFunctionResponse updateFunctionResponse = new UpdateFunctionResponse();
        updateFunctionResponse.setFunctionMetadata(functionMetadata);
        updateFunctionResponse.setHeader(response.getHeaders());
        updateFunctionResponse.setContent(response.getContent());
        updateFunctionResponse.setStatus(response.getStatus());
        return updateFunctionResponse;
    }

    public ListServicesResponse listServices(ListServicesRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "GET");
        ListServicesResponse listServicesResponse = GSON.fromJson(
            new String(response.getContent()), ListServicesResponse.class);
        listServicesResponse.setHeader(response.getHeaders());
        listServicesResponse.setContent(response.getContent());
        listServicesResponse.setStatus(response.getStatus());
        return listServicesResponse;
    }

    public ListFunctionsResponse listFunctions(ListFunctionsRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "GET");
        ListFunctionsResponse listFunctionsResponse = GSON.fromJson(
            new String(response.getContent()), ListFunctionsResponse.class);
        listFunctionsResponse.setHeader(response.getHeaders());
        listFunctionsResponse.setContent(response.getContent());
        listFunctionsResponse.setStatus(response.getStatus());
        return listFunctionsResponse;
    }

    public CreateTriggerResponse createTrigger(CreateTriggerRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "POST");
        TriggerMetadata triggerMetadata = GSON.fromJson(
            new String(response.getContent()), TriggerMetadata.class);
        CreateTriggerResponse createTriggerResponse = new CreateTriggerResponse();
        createTriggerResponse.setTriggerMetadata(triggerMetadata);
        createTriggerResponse.setHeader(response.getHeaders());
        createTriggerResponse.setContent(response.getContent());
        createTriggerResponse.setStatus(response.getStatus());
        return createTriggerResponse;
    }

    public DeleteTriggerResponse deleteTrigger(DeleteTriggerRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "DELETE");
        DeleteTriggerResponse deleteTriggerResponse = new DeleteTriggerResponse();
        deleteTriggerResponse.setHeader(response.getHeaders());
        deleteTriggerResponse.setStatus(response.getStatus());
        return deleteTriggerResponse;
    }

    public UpdateTriggerResponse updateTrigger(UpdateTriggerRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "PUT");
        TriggerMetadata triggerMetadata = GSON.fromJson(
            new String(response.getContent()), TriggerMetadata.class);
        UpdateTriggerResponse updateTriggerResponse = new UpdateTriggerResponse();
        updateTriggerResponse.setTriggerMetadata(triggerMetadata);
        updateTriggerResponse.setHeader(response.getHeaders());
        updateTriggerResponse.setContent(response.getContent());
        updateTriggerResponse.setStatus(response.getStatus());
        return updateTriggerResponse;
    }

    public GetTriggerResponse getTrigger(GetTriggerRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "GET");
        TriggerMetadata triggerMetadata = GSON.fromJson(
            new String(response.getContent()), TriggerMetadata.class);
        GetTriggerResponse getTriggerResponse = new GetTriggerResponse();
        getTriggerResponse.setTriggerMetadata(triggerMetadata);
        getTriggerResponse.setHeader(response.getHeaders());
        getTriggerResponse.setContent(response.getContent());
        getTriggerResponse.setStatus(response.getStatus());
        return getTriggerResponse;
    }

    public ListTriggersResponse listTriggers(ListTriggersRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "GET");
        ListTriggersResponse listTriggersResponse = GSON.fromJson(
            new String(response.getContent()), ListTriggersResponse.class);
        listTriggersResponse.setHeader(response.getHeaders());
        listTriggersResponse.setContent(response.getContent());
        listTriggersResponse.setStatus(response.getStatus());
        return listTriggersResponse;
    }

    public InvokeFunctionResponse invokeFunction(InvokeFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_STREAM, "POST");
        InvokeFunctionResponse invokeFunctionResponse = new InvokeFunctionResponse();
        invokeFunctionResponse.setContent(response.getContent());
        invokeFunctionResponse.setPayload(response.getContent());

        invokeFunctionResponse.setHeader(response.getHeaders());
        invokeFunctionResponse.setStatus(response.getStatus());
        Map<String, String> headers = response.getHeaders();
        if (headers != null && headers.containsKey(HeaderKeys.INVOCATION_LOG_RESULT)) {
            try {
                String logResult = Base64Helper.decode(headers.get(HeaderKeys.INVOCATION_LOG_RESULT),
                        Charset.defaultCharset().name());
                invokeFunctionResponse.setLogResult(logResult);
            } catch (IOException e) {
                throw new ClientException(e);
            }
        }
        return invokeFunctionResponse;
    }

    public Map createEnvironmentVariables(String serviceName, String functionName, String key, String value)
            throws ClientException, ServerException {
        //Get environmentVariables first
        Map<String, String> environmentVariables = listEnvironmentVariables(serviceName, functionName);
        environmentVariables.put(key, value);
        UpdateFunctionRequest request = new UpdateFunctionRequest(serviceName, functionName);
        request.setEnvironmentVariables(environmentVariables);
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "PUT");
        FunctionMetadata functionMetadata = GSON.fromJson(
                new String(response.getContent()), FunctionMetadata.class);
        return functionMetadata.getEnvironmentVariables();
    }

    public Map updateEnvironmentVariables(String serviceName, String functionName, String key, String value)
            throws ClientException, ServerException {
        return createEnvironmentVariables(serviceName, functionName, key, value);
    }

    // return the previous value associated with key, or null if there was no mapping for key.
    public String deleteEnvironmentVariables(String serviceName, String functionName, String key)
            throws ClientException, ServerException {
        Map<String, String> environmentVariables = listEnvironmentVariables(serviceName, functionName);
        Preconditions.checkArgument(environmentVariables != null);
        String value = environmentVariables.remove(key);
        UpdateFunctionRequest request = new UpdateFunctionRequest(serviceName, functionName);
        request.setEnvironmentVariables(environmentVariables);
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "PUT");
        FunctionMetadata functionMetadata = GSON.fromJson(
                new String(response.getContent()), FunctionMetadata.class);
        return value;
    }

    public String getEnvironmentVariables(String serviceName, String functionName, String key)
            throws ClientException, ServerException {
        GetFunctionRequest request = new GetFunctionRequest(serviceName, functionName);
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "GET");
        FunctionMetadata functionMetadata = GSON.fromJson(
                new String(response.getContent()), FunctionMetadata.class);
        String value = (String) functionMetadata.getEnvironmentVariables().get(key);
        return value;
    }

    public Map listEnvironmentVariables(String serviceName, String functionName)
            throws ClientException, ServerException {
        GetFunctionRequest request = new GetFunctionRequest(serviceName, functionName);
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, "GET");
        FunctionMetadata functionMetadata = GSON.fromJson(
                new String(response.getContent()), FunctionMetadata.class);
        Map environmentVariables = functionMetadata.getEnvironmentVariables();
        return environmentVariables;
    }
}
