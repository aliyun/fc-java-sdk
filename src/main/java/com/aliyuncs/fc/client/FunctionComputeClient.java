package com.aliyuncs.fc.client;

import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.constants.HeaderKeys;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.*;
import com.aliyuncs.fc.request.*;
import com.aliyuncs.fc.response.*;
import com.aliyuncs.fc.utils.Base64Helper;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Logger;

import static com.aliyuncs.fc.model.HttpMethod.*;

/**
 * TODO: add javadoc
 */
public class FunctionComputeClient {

    private final static String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private final static String CONTENT_TYPE_APPLICATION_STREAM = "application/octet-stream";
    private static final Logger LOGGER = Logger.getLogger("debug");

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

        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        DeleteServiceResponse deleteServiceResponse = new DeleteServiceResponse();
        deleteServiceResponse.setHeaders(response.getHeaders());
        deleteServiceResponse.setStatus(response.getStatus());
        return deleteServiceResponse;
    }

    public DeleteFunctionResponse deleteFunction(DeleteFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        DeleteFunctionResponse deleteFunctionResponse = new DeleteFunctionResponse();
        deleteFunctionResponse.setHeaders(response.getHeaders());
        deleteFunctionResponse.setStatus(response.getStatus());
        return deleteFunctionResponse;
    }

    public DeleteCustomDomainResponse deleteCustomDomain(DeleteCustomDomainRequest request)
            throws ClientException, ServerException {

        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        DeleteCustomDomainResponse deleteCustomDomainResponse = new DeleteCustomDomainResponse();
        deleteCustomDomainResponse.setHeaders(response.getHeaders());
        deleteCustomDomainResponse.setStatus(response.getStatus());
        return deleteCustomDomainResponse;
    }

    public GetServiceResponse getService(GetServiceRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ServiceMetadata serviceMetadata = GSON.fromJson(
            new String(response.getContent()), ServiceMetadata.class);
        GetServiceResponse getServiceResponse = new GetServiceResponse();
        getServiceResponse.setServiceMetadata(serviceMetadata);
        getServiceResponse.setHeaders(response.getHeaders());
        getServiceResponse.setContent(response.getContent());
        getServiceResponse.setStatus(response.getStatus());
        return getServiceResponse;
    }

    public GetFunctionResponse getFunction(GetFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        FunctionMetadata functionMetadata = GSON.fromJson(
            new String(response.getContent()), FunctionMetadata.class);
        GetFunctionResponse getFunctionResponse = new GetFunctionResponse();
        getFunctionResponse.setFunctionMetadata(functionMetadata);
        getFunctionResponse.setHeaders(response.getHeaders());
        getFunctionResponse.setContent(response.getContent());
        getFunctionResponse.setStatus(response.getStatus());
        return getFunctionResponse;
    }

    public GetFunctionCodeResponse getFunctionCode(GetFunctionCodeRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        FunctionCodeMetadata functionCodeMetadata = GSON.fromJson(
            new String(response.getContent()), FunctionCodeMetadata.class);
        GetFunctionCodeResponse getFunctionCodeResponse = new GetFunctionCodeResponse();
        getFunctionCodeResponse.setFunctionCodeMetadata(functionCodeMetadata);
        getFunctionCodeResponse.setHeaders(response.getHeaders());
        getFunctionCodeResponse.setContent(response.getContent());
        getFunctionCodeResponse.setStatus(response.getStatus());
        return getFunctionCodeResponse;
    }

    public GetCustomDomainResponse getCustomDomain(GetCustomDomainRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        CustomDomainMetaData customDomainMetadata = GSON.fromJson(
                new String(response.getContent()), CustomDomainMetaData.class);
        GetCustomDomainResponse getCustomDomainResponse = new GetCustomDomainResponse();
        getCustomDomainResponse.setCustomDomainMetadata(customDomainMetadata);
        getCustomDomainResponse.setHeaders(response.getHeaders());
        getCustomDomainResponse.setContent(response.getContent());
        getCustomDomainResponse.setStatus(response.getStatus());
        return getCustomDomainResponse;
    }

    public CreateServiceResponse createService(CreateServiceRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
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
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        FunctionMetadata functionMetadata = GSON.fromJson(
            new String(response.getContent()), FunctionMetadata.class);
        CreateFunctionResponse createFunctionResponse = new CreateFunctionResponse();
        createFunctionResponse.setFunctionMetadata(functionMetadata);
        createFunctionResponse.setHeaders(response.getHeaders());
        createFunctionResponse.setContent(response.getContent());
        createFunctionResponse.setStatus(response.getStatus());
        return createFunctionResponse;
    }

    public UpdateFunctionResponse updateFunction(UpdateFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        FunctionMetadata functionMetadata = GSON.fromJson(
            new String(response.getContent()), FunctionMetadata.class);
        UpdateFunctionResponse updateFunctionResponse = new UpdateFunctionResponse();
        updateFunctionResponse.setFunctionMetadata(functionMetadata);
        updateFunctionResponse.setHeaders(response.getHeaders());
        updateFunctionResponse.setContent(response.getContent());
        updateFunctionResponse.setStatus(response.getStatus());
        return updateFunctionResponse;
    }

    public UpdateServiceResponse updateService(UpdateServiceRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        ServiceMetadata serviceMetadata = GSON.fromJson(
                new String(response.getContent()), ServiceMetadata.class);
        UpdateServiceResponse updateServiceResponse = new UpdateServiceResponse();
        updateServiceResponse.setServiceMetadata(serviceMetadata);
        updateServiceResponse.setHeaders(response.getHeaders());
        updateServiceResponse.setContent(response.getContent());
        updateServiceResponse.setStatus(response.getStatus());
        return updateServiceResponse;
    }

    public CreateCustomDomainResponse createCustomDomain(CreateCustomDomainRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        CustomDomainMetaData customDomainMetaData = GSON.fromJson(
                new String(response.getContent()), CustomDomainMetaData.class);
        CreateCustomDomainResponse createCustomDomainResponse = new CreateCustomDomainResponse();
        createCustomDomainResponse.setCustomDomainMetadata(customDomainMetaData);
        createCustomDomainResponse.setHeaders(response.getHeaders());
        createCustomDomainResponse.setContent(response.getContent());
        createCustomDomainResponse.setStatus(response.getStatus());
        return createCustomDomainResponse;
    }

    public UpdateCustomDomainResponse updateCustomDomain(UpdateCustomDomainRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        CustomDomainMetaData customDomainMetadata = GSON.fromJson(
                new String(response.getContent()), CustomDomainMetaData.class);
        UpdateCustomDomainResponse updateCustomDomainResponse = new UpdateCustomDomainResponse();
        updateCustomDomainResponse.setCustomDomainMetadata(customDomainMetadata);
        updateCustomDomainResponse.setHeaders(response.getHeaders());
        updateCustomDomainResponse.setContent(response.getContent());
        updateCustomDomainResponse.setStatus(response.getStatus());
        return updateCustomDomainResponse;
    }

    public ListServicesResponse listServices(ListServicesRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListServicesResponse listServicesResponse = GSON.fromJson(
            new String(response.getContent()), ListServicesResponse.class);
        listServicesResponse.setHeaders(response.getHeaders());
        listServicesResponse.setContent(response.getContent());
        listServicesResponse.setStatus(response.getStatus());
        return listServicesResponse;
    }

    public ListFunctionsResponse listFunctions(ListFunctionsRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListFunctionsResponse listFunctionsResponse = GSON.fromJson(
            new String(response.getContent()), ListFunctionsResponse.class);
        listFunctionsResponse.setHeaders(response.getHeaders());
        listFunctionsResponse.setContent(response.getContent());
        listFunctionsResponse.setStatus(response.getStatus());
        return listFunctionsResponse;
    }

    public ListCustomDomainsResponse listCustomDomains(ListCustomDomainsRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListCustomDomainsResponse listCustomDomainsResponse = GSON.fromJson(
                new String(response.getContent()), ListCustomDomainsResponse.class);
        listCustomDomainsResponse.setHeaders(response.getHeaders());
        listCustomDomainsResponse.setContent(response.getContent());
        listCustomDomainsResponse.setStatus(response.getStatus());
        return listCustomDomainsResponse;
    }

    public CreateTriggerResponse createTrigger(CreateTriggerRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        TriggerMetadata triggerMetadata = GSON.fromJson(
            new String(response.getContent()), TriggerMetadata.class);
        CreateTriggerResponse createTriggerResponse = new CreateTriggerResponse();
        createTriggerResponse.setTriggerMetadata(triggerMetadata);
        createTriggerResponse.setHeaders(response.getHeaders());
        createTriggerResponse.setContent(response.getContent());
        createTriggerResponse.setStatus(response.getStatus());
        return createTriggerResponse;
    }

    public DeleteTriggerResponse deleteTrigger(DeleteTriggerRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        DeleteTriggerResponse deleteTriggerResponse = new DeleteTriggerResponse();
        deleteTriggerResponse.setHeaders(response.getHeaders());
        deleteTriggerResponse.setStatus(response.getStatus());
        return deleteTriggerResponse;
    }

    public UpdateTriggerResponse updateTrigger(UpdateTriggerRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        TriggerMetadata triggerMetadata = GSON.fromJson(
            new String(response.getContent()), TriggerMetadata.class);
        UpdateTriggerResponse updateTriggerResponse = new UpdateTriggerResponse();
        updateTriggerResponse.setTriggerMetadata(triggerMetadata);
        updateTriggerResponse.setHeaders(response.getHeaders());
        updateTriggerResponse.setContent(response.getContent());
        updateTriggerResponse.setStatus(response.getStatus());
        return updateTriggerResponse;
    }

    public GetTriggerResponse getTrigger(GetTriggerRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        TriggerMetadata triggerMetadata = GSON.fromJson(
            new String(response.getContent()), TriggerMetadata.class);
        GetTriggerResponse getTriggerResponse = new GetTriggerResponse();
        getTriggerResponse.setTriggerMetadata(triggerMetadata);
        getTriggerResponse.setHeaders(response.getHeaders());
        getTriggerResponse.setContent(response.getContent());
        getTriggerResponse.setStatus(response.getStatus());
        return getTriggerResponse;
    }

    public ListTriggersResponse listTriggers(ListTriggersRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListTriggersResponse listTriggersResponse = GSON.fromJson(
            new String(response.getContent()), ListTriggersResponse.class);
        listTriggersResponse.setHeaders(response.getHeaders());
        listTriggersResponse.setContent(response.getContent());
        listTriggersResponse.setStatus(response.getStatus());
        return listTriggersResponse;
    }

    public InvokeFunctionResponse invokeFunction(InvokeFunctionRequest request)
        throws ClientException, ServerException {

        HttpResponse response = null;

        if (request instanceof HttpInvokeFunctionRequest) {
            String form = null;

            if (request.getHeaders() != null && request.getHeaders().containsKey("Content-Type")) {
                form = request.getHeaders().get("Content-Type");
            }

            if (form == null) {
                form = CONTENT_TYPE_APPLICATION_STREAM;
            }

            response = client.doAction(request, form, ((HttpInvokeFunctionRequest) request).getMethod());
        } else {
            response = client.doAction(request, CONTENT_TYPE_APPLICATION_STREAM, POST);
        }

        InvokeFunctionResponse invokeFunctionResponse = new InvokeFunctionResponse();
        invokeFunctionResponse.setContent(response.getContent());
        invokeFunctionResponse.setPayload(response.getContent());

        invokeFunctionResponse.setHeaders(response.getHeaders());
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

}
