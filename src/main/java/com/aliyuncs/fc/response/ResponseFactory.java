package com.aliyuncs.fc.response;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.constants.HeaderKeys;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.FunctionMetadata;
import com.aliyuncs.fc.model.ServiceMetadata;
import com.aliyuncs.fc.request.ListFunctionsRequest;
import com.aliyuncs.fc.utils.Base64Helper;
import com.aliyuncs.fc.utils.FcUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import static com.aliyuncs.fc.model.HttpMethod.DELETE;

/**
 *
 * Convert HttpResponse to specified event type
 *
 */
public class ResponseFactory {
    private static final Gson GSON = new Gson();

    public static CreateFunctionResponse genCreateFunctionResponse(HttpResponse response) throws ClientException, ServerException {
        FunctionMetadata functionMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), FunctionMetadata.class);
        CreateFunctionResponse createFunctionResponse = new CreateFunctionResponse();
        createFunctionResponse.setFunctionMetadata(functionMetadata);
        createFunctionResponse.setHeaders(response.getHeaders());
        createFunctionResponse.setContent(response.getContent());
        createFunctionResponse.setStatus(response.getStatus());
        return createFunctionResponse;
    }

    public static InvokeFunctionResponse genInvokeFunctionResponse(HttpResponse response) throws ClientException, ServerException {
        InvokeFunctionResponse invokeFunctionResponse = new InvokeFunctionResponse();
        invokeFunctionResponse.setContent(response.getContent());
        invokeFunctionResponse.setPayload(response.getContent());

        invokeFunctionResponse.setHeaders(response.getHeaders());
        invokeFunctionResponse.setStatus(response.getStatus());
        Map<String, String> headers = response.getHeaders();
        if (headers != null && headers.containsKey(HeaderKeys.INVOCATION_LOG_RESULT)) {
            try {
                String logResult = Base64Helper
                        .decode(headers.get(HeaderKeys.INVOCATION_LOG_RESULT), Const.DEFAULT_CHARSET);
                invokeFunctionResponse.setLogResult(logResult);
            } catch (IOException e) {
                throw new ClientException(e);
            }
        }
        return invokeFunctionResponse;
    }

    public static DeleteFunctionResponse genDeleteFunctionResponse(HttpResponse response) throws ClientException, ServerException {
        DeleteFunctionResponse deleteFunctionResponse = new DeleteFunctionResponse();
        deleteFunctionResponse.setHeaders(response.getHeaders());
        deleteFunctionResponse.setStatus(response.getStatus());
        return deleteFunctionResponse;
    }

    public static UpdateFunctionResponse genUpdateFunctionResponse(HttpResponse response) throws ClientException, ServerException {
        FunctionMetadata functionMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), FunctionMetadata.class);
        UpdateFunctionResponse updateFunctionResponse = new UpdateFunctionResponse();
        updateFunctionResponse.setFunctionMetadata(functionMetadata);
        updateFunctionResponse.setHeaders(response.getHeaders());
        updateFunctionResponse.setContent(response.getContent());
        updateFunctionResponse.setStatus(response.getStatus());
        return updateFunctionResponse;
    }

    public static ListFunctionsResponse genListFunctionResponse(HttpResponse response) throws ClientException, ServerException {
        ListFunctionsResponse listFunctionsResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListFunctionsResponse.class);
        listFunctionsResponse.setHeaders(response.getHeaders());
        listFunctionsResponse.setContent(response.getContent());
        listFunctionsResponse.setStatus(response.getStatus());
        return listFunctionsResponse;
    }

    public static GetFunctionResponse genGetFunctionResponse(HttpResponse response) throws ClientException, ServerException {
        FunctionMetadata functionMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), FunctionMetadata.class);
        GetFunctionResponse getFunctionResponse = new GetFunctionResponse();
        getFunctionResponse.setFunctionMetadata(functionMetadata);
        getFunctionResponse.setHeaders(response.getHeaders());
        getFunctionResponse.setContent(response.getContent());
        getFunctionResponse.setStatus(response.getStatus());
        return getFunctionResponse;
    }

    public static CreateServiceResponse genCreateServiceResponse(HttpResponse response) throws ClientException, ServerException {
        ServiceMetadata serviceMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ServiceMetadata.class);
        CreateServiceResponse createServiceResponse = new CreateServiceResponse();
        createServiceResponse.setServiceMetadata(serviceMetadata);
        createServiceResponse.setHeaders(response.getHeaders());
        createServiceResponse.setContent(response.getContent());
        createServiceResponse.setStatus(response.getStatus());
        return createServiceResponse;
    }

    public static UpdateServiceResponse genUpdateServiceResponse(HttpResponse response) throws ClientException, ServerException {
        ServiceMetadata serviceMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ServiceMetadata.class);
        UpdateServiceResponse updateServiceResponse = new UpdateServiceResponse();
        updateServiceResponse.setServiceMetadata(serviceMetadata);
        updateServiceResponse.setHeaders(response.getHeaders());
        updateServiceResponse.setContent(response.getContent());
        updateServiceResponse.setStatus(response.getStatus());
        return updateServiceResponse;
    }

    public static ListServicesResponse genListServiceResponse(HttpResponse response) throws ClientException, ServerException {
        ListServicesResponse listServicesResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListServicesResponse.class);
        listServicesResponse.setHeaders(response.getHeaders());
        listServicesResponse.setContent(response.getContent());
        listServicesResponse.setStatus(response.getStatus());
        return listServicesResponse;
    }

    public static GetServiceResponse genGetServiceResponse(HttpResponse response) throws ClientException, ServerException {
        ServiceMetadata serviceMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ServiceMetadata.class);
        GetServiceResponse getServiceResponse = new GetServiceResponse();
        getServiceResponse.setServiceMetadata(serviceMetadata);
        getServiceResponse.setHeaders(response.getHeaders());
        getServiceResponse.setContent(response.getContent());
        getServiceResponse.setStatus(response.getStatus());
        return getServiceResponse;
    }

    public static DeleteServiceResponse genDeleteServiceResponse(HttpResponse response) throws ClientException, ServerException {
        DeleteServiceResponse deleteServiceResponse = new DeleteServiceResponse();
        deleteServiceResponse.setHeaders(response.getHeaders());
        deleteServiceResponse.setStatus(response.getStatus());
        return deleteServiceResponse;
    }
}
