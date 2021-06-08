/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.aliyuncs.fc.client;

import static com.aliyuncs.fc.model.HttpMethod.DELETE;
import static com.aliyuncs.fc.model.HttpMethod.GET;
import static com.aliyuncs.fc.model.HttpMethod.POST;
import static com.aliyuncs.fc.model.HttpMethod.PUT;

import com.aliyuncs.fc.auth.SignURLConfig;
import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.*;
import com.aliyuncs.fc.request.*;
import com.aliyuncs.fc.response.*;
import com.aliyuncs.fc.utils.FcUtil;
import com.google.gson.Gson;

import java.util.logging.Logger;

/**
 * TODO: add javadoc
 */
public class FunctionComputeClient {

    private final static String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private final static String CONTENT_TYPE_APPLICATION_STREAM = "application/octet-stream";
    private static final Logger LOGGER = Logger.getLogger("debug");
    private static final Gson GSON = new Gson();
    private final DefaultFcClient client;
    private final Config config;

    public FunctionComputeClient(String region, String uid, String accessKeyId,
        String accessKeySecret) {
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
     *
     * @param endpoint the endpoint that client sends requests to
     */
    public void setEndpoint(String endpoint) {
        config.setEndpoint(endpoint);
    }

    public GetAccountSettingsResponse getAccountSettings(GetAccountSettingsRequest request) throws ClientException, ServerException {

        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        GetAccountSettingsResponse getAccountSettingsOutput = new GetAccountSettingsResponse();
        AccountSettings accountSettings = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), AccountSettings.class);
        getAccountSettingsOutput.setHeaders(response.getHeaders());
        getAccountSettingsOutput.setStatus(response.getStatus());
        getAccountSettingsOutput.setAccountSettings(accountSettings);
        return getAccountSettingsOutput;
    }

    public DeleteServiceResponse deleteService(DeleteServiceRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        return ResponseFactory.genDeleteServiceResponse(response);
    }

    public DeleteVpcBindingResponse deleteVpcBinding(DeleteVpcBindingRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        return ResponseFactory.genDeleteVpcBindingResponse(response);
    }

    public DeleteFunctionResponse deleteFunction(DeleteFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        return ResponseFactory.genDeleteFunctionResponse(response);
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
        return ResponseFactory.genGetServiceResponse(response);
    }

    public GetFunctionResponse getFunction(GetFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        return ResponseFactory.genGetFunctionResponse(response);
    }

    public GetFunctionCodeResponse getFunctionCode(GetFunctionCodeRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        FunctionCodeMetadata functionCodeMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), FunctionCodeMetadata.class);
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
        CustomDomainMetaData customDomainMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), CustomDomainMetaData.class);
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
        return ResponseFactory.genCreateServiceResponse(response);
    }

    public CreateVpcBindingResponse createVpcBinding(CreateVpcBindingRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        return ResponseFactory.genCreateVpcBindingResponse(response);
    }

    public CreateFunctionResponse createFunction(CreateFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        return ResponseFactory.genCreateFunctionResponse(response);
    }

    public UpdateFunctionResponse updateFunction(UpdateFunctionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        return ResponseFactory.genUpdateFunctionResponse(response);
    }

    public UpdateServiceResponse updateService(UpdateServiceRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        return ResponseFactory.genUpdateServiceResponse(response);
    }

    public CreateCustomDomainResponse createCustomDomain(CreateCustomDomainRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        CustomDomainMetaData customDomainMetaData = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), CustomDomainMetaData.class);
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
        CustomDomainMetaData customDomainMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), CustomDomainMetaData.class);
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
        return ResponseFactory.genListServiceResponse(response);
    }

    public ListFunctionsResponse listFunctions(ListFunctionsRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        return ResponseFactory.genListFunctionResponse(response);
    }

    public ListCustomDomainsResponse listCustomDomains(ListCustomDomainsRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListCustomDomainsResponse listCustomDomainsResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListCustomDomainsResponse.class);
        listCustomDomainsResponse.setHeaders(response.getHeaders());
        listCustomDomainsResponse.setContent(response.getContent());
        listCustomDomainsResponse.setStatus(response.getStatus());
        return listCustomDomainsResponse;
    }

    public ListReservedCapacitiesResponse listReservedCapacities(ListReservedCapacitiesRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListReservedCapacitiesResponse listReservedCapacitiesResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListReservedCapacitiesResponse.class);
        listReservedCapacitiesResponse.setHeaders(response.getHeaders());
        listReservedCapacitiesResponse.setContent(response.getContent());
        listReservedCapacitiesResponse.setStatus(response.getStatus());
        return listReservedCapacitiesResponse;
    }

    public CreateTriggerResponse createTrigger(CreateTriggerRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        TriggerMetadata triggerMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), TriggerMetadata.class);
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
        TriggerMetadata triggerMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), TriggerMetadata.class);
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
        TriggerMetadata triggerMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), TriggerMetadata.class);
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
        ListTriggersResponse listTriggersResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListTriggersResponse.class);
        listTriggersResponse.setHeaders(response.getHeaders());
        listTriggersResponse.setContent(response.getContent());
        listTriggersResponse.setStatus(response.getStatus());
        return listTriggersResponse;
    }

    public ListVpcBindingsResponse listVpcBindings(ListVpcBindingsRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        System.out.println(FcUtil.toDefaultCharset(response.getContent()));
        ListVpcBindingsResponse listVpcBindingsResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListVpcBindingsResponse.class);
        listVpcBindingsResponse.setHeaders(response.getHeaders());
        listVpcBindingsResponse.setStatus(response.getStatus());
        listVpcBindingsResponse.setContent(response.getContent());
        return ResponseFactory.genListVpcBindingsResponse(response);
    }

    public CreateAliasResponse createAlias(CreateAliasRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        AliasMetaData aliasMetaData = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), AliasMetaData.class);
        CreateAliasResponse createAliasResponse = new CreateAliasResponse(aliasMetaData);
        createAliasResponse.setHeaders(response.getHeaders());
        createAliasResponse.setContent(response.getContent());
        createAliasResponse.setStatus(response.getStatus());
        return createAliasResponse;
    }

    public DeleteAliasResponse deleteAlias(DeleteAliasRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        DeleteAliasResponse deleteAliasResponse = new DeleteAliasResponse();
        deleteAliasResponse.setHeaders(response.getHeaders());
        deleteAliasResponse.setStatus(response.getStatus());
        return deleteAliasResponse;
    }

    public UpdateAliasResponse updateAlias(UpdateAliasRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        AliasMetaData aliasMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), AliasMetaData.class);
        UpdateAliasResponse updateAliasResponse = new UpdateAliasResponse(aliasMetadata);
        updateAliasResponse.setHeaders(response.getHeaders());
        updateAliasResponse.setContent(response.getContent());
        updateAliasResponse.setStatus(response.getStatus());
        return updateAliasResponse;
    }

    public GetAliasResponse getAlias(GetAliasRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        AliasMetaData aliasMetadata = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), AliasMetaData.class);
        GetAliasResponse getAliasResponse = new GetAliasResponse(aliasMetadata);
        getAliasResponse.setHeaders(response.getHeaders());
        getAliasResponse.setContent(response.getContent());
        getAliasResponse.setStatus(response.getStatus());
        return getAliasResponse;
    }

    public ListAliasesResponse listAliases(ListAliasesRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListAliasesResponse listAliasesResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListAliasesResponse.class);
        listAliasesResponse.setHeaders(response.getHeaders());
        listAliasesResponse.setContent(response.getContent());
        listAliasesResponse.setStatus(response.getStatus());
        return listAliasesResponse;
    }

    public PublishVersionResponse publishVersion(PublishVersionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        VersionMetaData versionMetaData = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), VersionMetaData.class);
        PublishVersionResponse publishVersionResponse = new PublishVersionResponse(versionMetaData);
        publishVersionResponse.setHeaders(response.getHeaders());
        publishVersionResponse.setContent(response.getContent());
        publishVersionResponse.setStatus(response.getStatus());
        return publishVersionResponse;
    }

    public DeleteVersionResponse deleteVersion(DeleteVersionRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        DeleteVersionResponse deleteVersionResponse = new DeleteVersionResponse();
        deleteVersionResponse.setHeaders(response.getHeaders());
        deleteVersionResponse.setStatus(response.getStatus());
        return deleteVersionResponse;
    }

    public ListVersionsResponse listVersions(ListVersionsRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListVersionsResponse listVersionsResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListVersionsResponse.class);
        listVersionsResponse.setHeaders(response.getHeaders());
        listVersionsResponse.setContent(response.getContent());
        listVersionsResponse.setStatus(response.getStatus());
        return listVersionsResponse;
    }

    public PutProvisionConfigResponse putProvisionConfig(PutProvisionConfigRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        ProvisionTarget provisionTarget = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ProvisionTarget.class);
        PutProvisionConfigResponse putProvisionConfigResponse = new PutProvisionConfigResponse(provisionTarget);
        putProvisionConfigResponse.setHeaders(response.getHeaders());
        putProvisionConfigResponse.setContent(response.getContent());
        putProvisionConfigResponse.setStatus(response.getStatus());
        return putProvisionConfigResponse;
    }

    public GetProvisionConfigResponse getProvisionConfig(GetProvisionConfigRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ProvisionConfig provisionConfig = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ProvisionConfig.class);
        GetProvisionConfigResponse getProvisionConfigResponse = new GetProvisionConfigResponse(provisionConfig);
        getProvisionConfigResponse.setHeaders(response.getHeaders());
        getProvisionConfigResponse.setContent(response.getContent());
        getProvisionConfigResponse.setStatus(response.getStatus());
        return getProvisionConfigResponse;
    }

    public ListProvisionConfigsResponse listProvisionConfigs(ListProvisionConfigsRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListProvisionConfigsResponse listProvisionConfigsResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListProvisionConfigsResponse.class);
        listProvisionConfigsResponse.setHeaders(response.getHeaders());
        listProvisionConfigsResponse.setContent(response.getContent());
        listProvisionConfigsResponse.setStatus(response.getStatus());
        return listProvisionConfigsResponse;
    }

    public TagResourceResponse tagResource(TagResourceRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        TagBaseResponse baseResp = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), TagBaseResponse.class);
        TagResourceResponse tagResourceResponse = new TagResourceResponse();
        tagResourceResponse.setHeaders(response.getHeaders());
        tagResourceResponse.setContent(response.getContent());
        tagResourceResponse.setStatus(response.getStatus());
        return tagResourceResponse;
    }

    public UntagResourceResponse untagResource(UntagResourceRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        TagBaseResponse baseResp = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), TagBaseResponse.class);
        UntagResourceResponse untagResourceResponse = new UntagResourceResponse();
        untagResourceResponse.setHeaders(response.getHeaders());
        untagResourceResponse.setContent(response.getContent());
        untagResourceResponse.setStatus(response.getStatus());
        return  untagResourceResponse;
    }

    public GetResourceTagsResponse getResourceTags (GetResourceTagsRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        TagMetaData tagMetaData = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), TagMetaData.class);
        GetResourceTagsResponse getResourceTagsResponse = new GetResourceTagsResponse();
        getResourceTagsResponse.setTagMetadata(tagMetaData);
        getResourceTagsResponse.setHeaders(response.getHeaders());
        getResourceTagsResponse.setContent(response.getContent());
        getResourceTagsResponse.setStatus(response.getStatus());
        return  getResourceTagsResponse;
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

            response = client
                .doAction(request, form, ((HttpInvokeFunctionRequest) request).getMethod());
        } else {
            response = client.doAction(request, CONTENT_TYPE_APPLICATION_STREAM, POST);
        }

        return ResponseFactory.genInvokeFunctionResponse(response);
    }

    public ListOnDemandConfigsResponse listOnDemandConfigs(ListOnDemandConfigsRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListOnDemandConfigsResponse listOnDemandConfigsResponse = GSON.fromJson(
            new String(response.getContent()), ListOnDemandConfigsResponse.class);
        listOnDemandConfigsResponse.setHeader(response.getHeaders());
        listOnDemandConfigsResponse.setContent(response.getContent());
        listOnDemandConfigsResponse.setStatus(response.getStatus());
        return listOnDemandConfigsResponse;
    }

    public PutOnDemandConfigResponse putOnDemandConfig(PutOnDemandConfigRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        OnDemandConfigMetadata onDemandConfigMetadata = GSON.fromJson(
            new String(response.getContent()), OnDemandConfigMetadata.class);

        PutOnDemandConfigResponse putOnDemandConfigsResponse = new PutOnDemandConfigResponse();
        putOnDemandConfigsResponse.setOnDemandConfigMetadata(onDemandConfigMetadata);
        putOnDemandConfigsResponse.setHeader(response.getHeaders());
        putOnDemandConfigsResponse.setContent(response.getContent());
        putOnDemandConfigsResponse.setStatus(response.getStatus());
        return putOnDemandConfigsResponse;
    }

    public GetOnDemandConfigResponse getOnDemandConfig(GetOnDemandConfigRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);

        OnDemandConfigMetadata onDemandConfigMetadata = GSON.fromJson(
                new String(response.getContent()), OnDemandConfigMetadata.class);

        GetOnDemandConfigResponse getOnDemandConfigsResponse = new GetOnDemandConfigResponse();
        getOnDemandConfigsResponse.setOnDemandConfigMetadata(onDemandConfigMetadata);
        getOnDemandConfigsResponse.setHeader(response.getHeaders());
        getOnDemandConfigsResponse.setContent(response.getContent());
        getOnDemandConfigsResponse.setStatus(response.getStatus());
        return getOnDemandConfigsResponse;
    }

    public DeleteOnDemandConfigResponse deleteOnDemandConfig(DeleteOnDemandConfigRequest request)
        throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        DeleteOnDemandConfigResponse deleteOnDemandConfigsResponse = new DeleteOnDemandConfigResponse();
        deleteOnDemandConfigsResponse.setHeader(response.getHeaders());
        deleteOnDemandConfigsResponse.setContent(response.getContent());
        deleteOnDemandConfigsResponse.setStatus(response.getStatus());
        return deleteOnDemandConfigsResponse;
    }

    public PutFunctionAsyncConfigResponse putFunctionAsyncConfig(PutFunctionAsyncConfigRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        AsyncConfig config = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), AsyncConfig.class);
        PutFunctionAsyncConfigResponse putFunctionAsyncConfigResponse = new PutFunctionAsyncConfigResponse(config);
        putFunctionAsyncConfigResponse.setAsyncConfig(config);
        putFunctionAsyncConfigResponse.setContent(response.getContent());
        putFunctionAsyncConfigResponse.setHeader(response.getHeaders());
        putFunctionAsyncConfigResponse.setStatus(response.getStatus());
        return putFunctionAsyncConfigResponse;
    }

    public GetFunctionAsyncConfigResponse getFunctionAsyncConfig(GetFunctionAsyncConfigRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        AsyncConfig asyncConfig = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), AsyncConfig.class);
        GetFunctionAsyncConfigResponse getFunctionAsyncConfigResponse = new GetFunctionAsyncConfigResponse(asyncConfig);
        getFunctionAsyncConfigResponse.setAsyncConfig(asyncConfig);
        getFunctionAsyncConfigResponse.setHeader(response.getHeaders());
        getFunctionAsyncConfigResponse.setContent(response.getContent());
        getFunctionAsyncConfigResponse.setStatus(response.getStatus());
        return getFunctionAsyncConfigResponse;
    }

    public ListFunctionAsyncConfigsResponse listFunctionAsyncConfigs(ListFunctionAsyncConfigsRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListFunctionAsyncConfigsResponse listFunctionAsyncConfigsResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListFunctionAsyncConfigsResponse.class);
        listFunctionAsyncConfigsResponse.setHeader(response.getHeaders());
        listFunctionAsyncConfigsResponse.setContent(response.getContent());
        listFunctionAsyncConfigsResponse.setStatus(response.getStatus());
        return listFunctionAsyncConfigsResponse;
    }

    public DeleteFunctionAsyncConfigResponse deleteFunctionAsyncConfig(DeleteFunctionAsyncConfigRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        DeleteFunctionAsyncConfigResponse deleteFunctionAsyncConfigResponse = new DeleteFunctionAsyncConfigResponse();
        deleteFunctionAsyncConfigResponse.setHeaders(response.getHeaders());
        deleteFunctionAsyncConfigResponse.setContent(response.getContent());
        deleteFunctionAsyncConfigResponse.setStatus(response.getStatus());
        return deleteFunctionAsyncConfigResponse;
    }

    public OpenFunctionComputeResponse openFunctionComputeService(OpenFunctionComputeRequest request) throws ClientException, ServerException {
        PopClient popClient = new PopClient();
        OpenFunctionComputeResponse response = popClient.openFCService(client.getConfig(), request);
        return response;
    }


    public PublishLayerVersionResponse publishLayerVersion(PublishLayerVersionRequest request) throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, POST);
        PublishLayerVersionResponse publishLayerVersionResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), PublishLayerVersionResponse.class);
        publishLayerVersionResponse.setHeaders(response.getHeaders());
        publishLayerVersionResponse.setContent(response.getContent());
        publishLayerVersionResponse.setStatus(response.getStatus());
        return publishLayerVersionResponse;
    }

    public GetLayerVersionResponse getLayerVerion(GetLayerVersionRequest request) throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        GetLayerVersionResponse getLayerVersionResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), GetLayerVersionResponse.class);
        getLayerVersionResponse.setHeaders(response.getHeaders());
        getLayerVersionResponse.setContent(response.getContent());
        getLayerVersionResponse.setStatus(response.getStatus());
        return getLayerVersionResponse;
    }

    public DeleteLayerVersionReponse deleteLayerVersion(DeleteLayerVersionRequest request) throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, DELETE);
        DeleteLayerVersionReponse deleteLayerVersionReponse = new DeleteLayerVersionReponse();
        deleteLayerVersionReponse.setHeaders(response.getHeaders());
        deleteLayerVersionReponse.setContent(response.getContent());
        deleteLayerVersionReponse.setStatus(response.getStatus());
        return deleteLayerVersionReponse;
    }

    public ListLayerVersionResponse listLayerVersion(ListLayerVersionRequest request) throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListLayerVersionResponse listLayerVersionResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListLayerVersionResponse.class);
        listLayerVersionResponse.setHeaders(response.getHeaders());
        listLayerVersionResponse.setContent(response.getContent());
        listLayerVersionResponse.setStatus(response.getStatus());
        return listLayerVersionResponse;
    }

    public ListLayerResponse listLayer(ListLayerRequest request)  throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListLayerResponse listLayerResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListLayerResponse.class);
        listLayerResponse.setHeaders(response.getHeaders());
        listLayerResponse.setContent(response.getContent());
        listLayerResponse.setStatus(response.getStatus());
        return listLayerResponse;
    }

    public String SignURL(SignURLConfig input) throws Exception {
        return input.signURL(this.config.getApiVersion(),
                this.config.getEndpoint(),
                this.config.getAccessKeyID(),
                this.config.getAccessKeySecret(),
                this.config.getSecurityToken());
    }

    public GetStatefulAsyncInvocationResponse getStatefulAsyncInvocation(GetStatefulAsyncInvocationRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        StatefulAsyncInvocation invocation = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), StatefulAsyncInvocation.class);
        GetStatefulAsyncInvocationResponse getStatefulAsyncInvocationResponse = new GetStatefulAsyncInvocationResponse(invocation);
        getStatefulAsyncInvocationResponse.setStatefulAsyncInvocation(invocation);
        getStatefulAsyncInvocationResponse.setHeaders(response.getHeaders());
        getStatefulAsyncInvocationResponse.setContent(response.getContent());
        getStatefulAsyncInvocationResponse.setStatus(response.getStatus());
        return getStatefulAsyncInvocationResponse;
    }

    public ListStatefulAsyncInvocationsResponse listStatefulAsyncInvocations(ListStatefulAsyncInvocationsRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, GET);
        ListStatefulAsyncInvocationsResponse listStatefulAsyncInvocationsResponse = GSON.fromJson(FcUtil.toDefaultCharset(response.getContent()), ListStatefulAsyncInvocationsResponse.class);
        listStatefulAsyncInvocationsResponse.setHeaders(response.getHeaders());
        listStatefulAsyncInvocationsResponse.setContent(response.getContent());
        listStatefulAsyncInvocationsResponse.setStatus(response.getStatus());
        return listStatefulAsyncInvocationsResponse;
    }

    public StopStatefulAsyncInvocationResponse stopStatefulAsyncInvocation(StopStatefulAsyncInvocationRequest request)
            throws ClientException, ServerException {
        HttpResponse response = client.doAction(request, CONTENT_TYPE_APPLICATION_JSON, PUT);
        StopStatefulAsyncInvocationResponse stopStatefulAsyncInvocationResponse = new StopStatefulAsyncInvocationResponse();
        stopStatefulAsyncInvocationResponse.setHeaders(response.getHeaders());
        stopStatefulAsyncInvocationResponse.setContent(response.getContent());
        stopStatefulAsyncInvocationResponse.setStatus(response.getStatus());
        return stopStatefulAsyncInvocationResponse;
    }
}
