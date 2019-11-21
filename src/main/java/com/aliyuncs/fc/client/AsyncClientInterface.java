package com.aliyuncs.fc.client;

import com.aliyuncs.fc.request.*;
import com.aliyuncs.fc.response.*;

import java.io.IOException;
import java.util.concurrent.Future;

public interface AsyncClientInterface {
    /*
    Function
     */
    Future<CreateFunctionResponse> createFunction(CreateFunctionRequest request, FcCallback<CreateFunctionRequest, CreateFunctionResponse> callback);
    Future<InvokeFunctionResponse> invokeFunction(InvokeFunctionRequest request, FcCallback<InvokeFunctionRequest, InvokeFunctionResponse> callback);
    Future<UpdateFunctionResponse> updateFunction(UpdateFunctionRequest request, FcCallback<UpdateFunctionRequest, UpdateFunctionResponse> callback);
    Future<DeleteFunctionResponse> deleteFunction(DeleteFunctionRequest request, FcCallback<DeleteFunctionRequest, DeleteFunctionResponse> callback);
    Future<ListFunctionsResponse> listFunctions(ListFunctionsRequest request, FcCallback<ListFunctionsRequest, ListFunctionsResponse> callback);
    Future<GetFunctionResponse> getFunction(GetFunctionRequest request, FcCallback<GetFunctionRequest, GetFunctionResponse> callback);

    /*
    Service
     */
    Future<CreateServiceResponse> createService(CreateServiceRequest request, FcCallback<CreateServiceRequest, CreateServiceResponse> callback);
    Future<UpdateServiceResponse> updateService(UpdateServiceRequest request, FcCallback<UpdateServiceRequest, UpdateServiceResponse> callback);
    Future<DeleteServiceResponse> deleteService(DeleteServiceRequest request, FcCallback<DeleteServiceRequest, DeleteServiceResponse> callback);
    Future<ListServicesResponse> listServices(ListServicesRequest request, FcCallback<ListServicesRequest, ListServicesResponse> callback);
    Future<GetServiceResponse> getService(GetServiceRequest request, FcCallback<GetServiceRequest, GetServiceResponse> callback);

    void close() throws IOException;
}
