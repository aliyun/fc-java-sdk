package com.aliyuncs.fc;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.fc.client.FunctionComputeClient;
import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ErrorCodes;
import com.aliyuncs.fc.model.Code;
import com.aliyuncs.fc.model.FunctionMetadata;
import com.aliyuncs.fc.model.OSSTriggerConfig;
import com.aliyuncs.fc.model.TriggerMetadata;
import com.aliyuncs.fc.request.CreateFunctionRequest;
import com.aliyuncs.fc.request.CreateServiceRequest;
import com.aliyuncs.fc.request.CreateTriggerRequest;
import com.aliyuncs.fc.request.DeleteFunctionRequest;
import com.aliyuncs.fc.request.DeleteServiceRequest;
import com.aliyuncs.fc.request.DeleteTriggerRequest;
import com.aliyuncs.fc.request.GetFunctionRequest;
import com.aliyuncs.fc.request.GetServiceRequest;
import com.aliyuncs.fc.request.GetTriggerRequest;
import com.aliyuncs.fc.request.InvokeFunctionRequest;
import com.aliyuncs.fc.request.ListFunctionsRequest;
import com.aliyuncs.fc.request.ListServicesRequest;
import com.aliyuncs.fc.request.ListTriggersRequest;
import com.aliyuncs.fc.request.UpdateFunctionRequest;
import com.aliyuncs.fc.request.UpdateServiceRequest;
import com.aliyuncs.fc.request.UpdateTriggerRequest;
import com.aliyuncs.fc.response.CreateFunctionResponse;
import com.aliyuncs.fc.response.CreateServiceResponse;
import com.aliyuncs.fc.response.CreateTriggerResponse;
import com.aliyuncs.fc.response.DeleteFunctionResponse;
import com.aliyuncs.fc.response.DeleteServiceResponse;
import com.aliyuncs.fc.response.DeleteTriggerResponse;
import com.aliyuncs.fc.response.GetFunctionResponse;
import com.aliyuncs.fc.response.GetServiceResponse;
import com.aliyuncs.fc.response.GetTriggerResponse;
import com.aliyuncs.fc.response.InvokeFunctionResponse;
import com.aliyuncs.fc.response.ListFunctionsResponse;
import com.aliyuncs.fc.response.ListServicesResponse;
import com.aliyuncs.fc.response.ListTriggersResponse;
import com.aliyuncs.fc.response.UpdateServiceResponse;
import com.aliyuncs.fc.response.UpdateTriggerResponse;
import com.aliyuncs.fc.utils.ZipUtils;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse.Credentials;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

/**
 * Validation for FunctionComputeClient, tests including
 * create/list/get/update service/function/trigger
 */
public class FunctionComputeClientTest {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final String VALIDATE_MSG = "cannot be blank";
    private static final String REGION = System.getenv("REGION");
    private static final String ROLE = System.getenv("ROLE");
    private static final String ACCESS_KEY = System.getenv("ACCESS_KEY");
    private static final String SECRET_KEY = System.getenv("SECRET_KEY");
    private static final String ACCOUNT_ID = System.getenv("ACCOUNT_ID");
    private static final String CODE_BUCKET = System.getenv("CODE_BUCKET");
    private static final String CODE_OBJECT = System.getenv("CODE_OBJECT");
    private static final String INVOCATION_ROLE = System.getenv("INVOCATION_ROLE");

    private static final String OSS_SOURCE_ARN =
        String.format("acs:oss:%s:%s:%s", REGION, ACCOUNT_ID, CODE_BUCKET);
    private static final String SERVICE_NAME = "testServiceJavaSDK";
    private static final String SERVICE_DESC_OLD = "service desc";
    private static final String SERVICE_DESC_NEW = "service desc updated";
    private static final String FUNCTION_NAME = "testFunction";
    private static final String FUNCTION_DESC_OLD = "function desc";
    private static final String FUNCTION_DESC_NEW = "function desc updated";
    private static final String TRIGGER_NAME = "testTrigger";
    private static final String TRIGGER_TYPE_OSS = "oss";
    public static final String STS_API_VERSION = "2015-04-01";

    private FunctionComputeClient client;

    @Before
    public void setup() {
        // Create or clean up everything under the test service
        client = new FunctionComputeClient(REGION, ACCOUNT_ID, ACCESS_KEY, SECRET_KEY);
        GetServiceRequest getSReq = new GetServiceRequest(SERVICE_NAME);
        try {
            client.getService(getSReq);
            ListFunctionsRequest listFReq = new ListFunctionsRequest(SERVICE_NAME);
            ListFunctionsResponse listFResp = client.listFunctions(listFReq);
            cleanUpFunctions(SERVICE_NAME, listFResp.getFunctions());
            cleanupService(SERVICE_NAME);
        } catch (ClientException e) {
            if (!ErrorCodes.SERVICE_NOT_FOUND.equals(e.getErrorCode())) {
                throw e;
            }
        }
    }

    public FunctionComputeClient overrideFCClient(boolean useSts, boolean useHttps)
        throws com.aliyuncs.exceptions.ClientException {
        if (useSts) {
            Credentials creds = getAssumeRoleCredentials(null);
            return new FunctionComputeClient(new Config(REGION, ACCOUNT_ID,
                creds.getAccessKeyId(), creds.getAccessKeySecret(), creds.getSecurityToken(),
                useHttps));
        }
        return new FunctionComputeClient(new Config(REGION, ACCOUNT_ID,
            ACCESS_KEY, SECRET_KEY, null, useHttps));
    }

    private void cleanupService(String serviceName) {
        DeleteServiceRequest request = new DeleteServiceRequest(serviceName);
        client.deleteService(request);
        System.out.println("Service " + serviceName + " is deleted");
    }

    private void cleanUpFunctions(String serviceName, FunctionMetadata[] functions) {
        for (FunctionMetadata function : functions) {
            ListTriggersRequest listReq = new ListTriggersRequest(serviceName,
                function.getFunctionName());
            ListTriggersResponse listTResp = client.listTriggers(listReq);
            cleanUpTriggers(serviceName, function.getFunctionName(), listTResp.getTriggers());
            System.out.println(
                "All triggers for Function " + function.getFunctionName() + " are deleted");
            DeleteFunctionRequest deleteFReq = new DeleteFunctionRequest(serviceName,
                function.getFunctionName());
            client.deleteFunction(deleteFReq);
        }
    }

    private void cleanUpTriggers(String serviceName, String functionName,
        TriggerMetadata[] triggers) {
        for (TriggerMetadata trigger : triggers) {
            DeleteTriggerRequest request = new DeleteTriggerRequest(serviceName, functionName,
                trigger.getTriggerName());
            DeleteTriggerResponse response = client.deleteTrigger(request);
            assertTrue(response.isSuccess());
            System.out.println("Trigger " + trigger.getTriggerName() + " is deleted");
        }
    }

    private CreateFunctionResponse createFunction(String functionName) {
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);
        createFuncReq.setFunctionName(functionName);
        createFuncReq.setDescription(FUNCTION_DESC_OLD);
        createFuncReq.setMemorySize(128);
        createFuncReq.setHandler("hello_world.handler");
        createFuncReq.setRuntime("nodejs4.4");
        createFuncReq
            .setCode(new Code().setOssBucketName(CODE_BUCKET).setOssObjectName(CODE_OBJECT));
        createFuncReq.setTimeout(10);

        return client.createFunction(createFuncReq);
    }

    private CreateServiceResponse createService(String serviceName) {
        CreateServiceRequest createSReq = new CreateServiceRequest();
        createSReq.setServiceName(serviceName);
        createSReq.setDescription(SERVICE_DESC_OLD);
        createSReq.setRole(ROLE);
        return client.createService(createSReq);
    }

    private CreateTriggerResponse createTrigger(String triggerName, String prefix, String suffix) {
        CreateTriggerRequest createTReq = new CreateTriggerRequest(SERVICE_NAME, FUNCTION_NAME);
        createTReq.setTriggerName(triggerName);
        createTReq.setTriggerType(TRIGGER_TYPE_OSS);
        createTReq.setInvocationRole(INVOCATION_ROLE);
        createTReq.setSourceArn(OSS_SOURCE_ARN);
        createTReq.setTriggerConfig(
            new OSSTriggerConfig(new String[]{"oss:ObjectCreated:*"}, prefix, suffix));
        CreateTriggerResponse resp = client.createTrigger(createTReq);
        try {
            // Add some sleep since OSS notifications create is not strongly consistent
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resp;
    }

    @Test
    public void testCRUD()
        throws ClientException, JSONException, NoSuchAlgorithmException, InterruptedException, ParseException {
        testCRUDHelper(true);
    }

    @Test
    public void testCRUDStsToken() throws com.aliyuncs.exceptions.ClientException,
        ParseException, InterruptedException {
        client = overrideFCClient(true, false);
        testCRUDHelper(false);
    }

    @Test
    public void testCRUDStsTokenHttps() throws com.aliyuncs.exceptions.ClientException,
        ParseException, InterruptedException {
        client = overrideFCClient(true, true);
        testCRUDHelper(false);
    }

    @Test
    public void testCreateServiceStsTokenNoPassRole()
        throws com.aliyuncs.exceptions.ClientException {
        // Use a policy that does not have ram:PassRole, this policy will intersect with the role policy
        // Access denied is expected if using STS without PassRole allowed
        // Policy intersection doc: https://help.aliyun.com/document_detail/31935.html
        String policy = "{\"Version\": \"1\",\"Statement\": [{\"Effect\": \"Allow\",\"Action\": [\"fc:*\"],\"Resource\": [\"*\"]}]}";
        Credentials creds = getAssumeRoleCredentials(policy);
        client = new FunctionComputeClient(new Config(REGION, ACCOUNT_ID,
            creds.getAccessKeyId(), creds.getAccessKeySecret(), creds.getSecurityToken(),
            false));

        try {
            createService(SERVICE_NAME);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getErrorMessage()
                .contains("the caller is not authorized to perform 'ram:PassRole'"));
        }
    }

    @Test
    public void testListServices() {
        final int numServices = 10;
        final int limit = 3;

        // Create multiple services
        for (int i = 0; i < numServices; i++) {
            try {
                client.getService(new GetServiceRequest(SERVICE_NAME + i));
                cleanupService(SERVICE_NAME + i);
            } catch (ClientException e) {
                if (!ErrorCodes.SERVICE_NOT_FOUND.equals(e.getErrorCode())) {
                    throw new RuntimeException("Cleanup failed");
                }
            }
            CreateServiceRequest request = new CreateServiceRequest();
            request.setServiceName(SERVICE_NAME + i);
            request.setDescription(SERVICE_DESC_OLD);
            request.setRole(ROLE);
            CreateServiceResponse response = client.createService(request);
            assertFalse(Strings.isNullOrEmpty(response.getRequestId()));
        }
        ListServicesRequest listRequest = new ListServicesRequest();
        listRequest.setLimit(limit);
        listRequest.setPrefix(SERVICE_NAME);
        ListServicesResponse listResponse = client.listServices(listRequest);
        int numCalled = 1;
        String nextToken = listResponse.getNextToken();
        while (nextToken != null) {
            listRequest.setNextToken(nextToken);
            listResponse = client.listServices(listRequest);
            nextToken = listResponse.getNextToken();
            numCalled++;
        }
        assertEquals(numServices / limit + 1, numCalled);

        // Delete services
        for (int i = 0; i < numServices; i++) {
            cleanupService(SERVICE_NAME + i);
        }
    }

    @Test
    public void testListFunctions() {
        final int numServices = 10;
        final int limit = 3;

        // Create service
        createService(SERVICE_NAME);

        // Create multiple functions under the test service
        for (int i = 0; i < numServices; i++) {
            CreateFunctionResponse createFResp = createFunction(FUNCTION_NAME + i);
            assertFalse(Strings.isNullOrEmpty(createFResp.getRequestId()));
        }
        ListFunctionsRequest listRequest = new ListFunctionsRequest(SERVICE_NAME);
        listRequest.setLimit(limit);
        listRequest.setPrefix(FUNCTION_NAME);
        ListFunctionsResponse listResponse = client.listFunctions(listRequest);
        int numCalled = 1;
        String nextToken = listResponse.getNextToken();
        while (nextToken != null) {
            listRequest.setNextToken(nextToken);
            listResponse = client.listFunctions(listRequest);
            nextToken = listResponse.getNextToken();
            numCalled++;
        }
        assertEquals(numServices / limit + 1, numCalled);
    }

    @Test
    public void testListTriggers() {
        final int numTriggers = 5;
        final int limit = 2;

        // Create service
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        // Create multiple trigger under the test function
        for (int i = 0; i < numTriggers; i++) {
            String prefix = "prefix";
            String suffix = "suffix";
            CreateTriggerResponse createTResp = createTrigger(TRIGGER_NAME + i,
                prefix + i, suffix + i);
            assertFalse(Strings.isNullOrEmpty(createTResp.getRequestId()));
        }

        ListTriggersRequest listTReq = new ListTriggersRequest(SERVICE_NAME, FUNCTION_NAME);
        listTReq.setLimit(limit);
        ListTriggersResponse listTResp = client.listTriggers(listTReq);
        int numCalled = 1;
        String nextToken = listTResp.getNextToken();
        while (nextToken != null) {
            listTReq.setNextToken(nextToken);
            listTResp = client.listTriggers(listTReq);
            nextToken = listTResp.getNextToken();
            numCalled++;
        }

        assertEquals(numTriggers / limit + 1, numCalled);

        for (int i = 0; i < numTriggers; i++) {
            DeleteTriggerResponse deleteTResp = client.deleteTrigger(
                new DeleteTriggerRequest(SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME + i));
            assertFalse(Strings.isNullOrEmpty(deleteTResp.getRequestId()));
        }
    }

    @Test
    public void testCreateServiceValidate() {
        try {
            CreateServiceRequest request = new CreateServiceRequest();
            client.createService(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertEquals(ErrorCodes.INVALID_ARGUMENT, e.getErrorCode());
        }

        try {
            CreateServiceRequest request = new CreateServiceRequest();
            client.createService(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertEquals(ErrorCodes.INVALID_ARGUMENT, e.getErrorCode());
        }
    }

    @Test
    public void testCreateFunctionValidate() {
        try {
            CreateFunctionRequest request = new CreateFunctionRequest(null);
            client.createFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            CreateFunctionRequest request = new CreateFunctionRequest("");
            client.createFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testCreateTriggerValidate() {
        try {
            CreateTriggerRequest request = new CreateTriggerRequest(SERVICE_NAME, null);
            client.createTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            CreateTriggerRequest request = new CreateTriggerRequest(SERVICE_NAME, "");
            client.createTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            CreateTriggerRequest request = new CreateTriggerRequest(null, FUNCTION_NAME);
            client.createTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            CreateTriggerRequest request = new CreateTriggerRequest("", FUNCTION_NAME);
            client.createTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testGetServiceValidate() {
        try {
            GetServiceRequest request = new GetServiceRequest(null);
            client.getService(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            GetServiceRequest request = new GetServiceRequest("");
            client.getService(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testGetFunctionValidate() {
        try {
            GetFunctionRequest request = new GetFunctionRequest(SERVICE_NAME, null);
            client.getFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            GetFunctionRequest request = new GetFunctionRequest(SERVICE_NAME, "");
            client.getFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            GetFunctionRequest request = new GetFunctionRequest(null, FUNCTION_NAME);
            client.getFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            GetFunctionRequest request = new GetFunctionRequest("", FUNCTION_NAME);
            client.getFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testGetTriggerValidate() {
        try {
            GetTriggerRequest request = new GetTriggerRequest(SERVICE_NAME, FUNCTION_NAME, null);
            client.getTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            GetTriggerRequest request = new GetTriggerRequest(SERVICE_NAME, FUNCTION_NAME, "");
            client.getTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            GetTriggerRequest request = new GetTriggerRequest(SERVICE_NAME, null, TRIGGER_NAME);
            client.getTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            GetTriggerRequest request = new GetTriggerRequest(SERVICE_NAME, "", TRIGGER_NAME);
            client.getTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            GetTriggerRequest request = new GetTriggerRequest(null, FUNCTION_NAME, TRIGGER_NAME);
            client.getTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            GetTriggerRequest request = new GetTriggerRequest("", FUNCTION_NAME, TRIGGER_NAME);
            client.getTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testInvokeFunctionValidate() {
        try {
            InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, null);
            client.invokeFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, "");
            client.invokeFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            InvokeFunctionRequest request = new InvokeFunctionRequest("", FUNCTION_NAME);
            client.invokeFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            InvokeFunctionRequest request = new InvokeFunctionRequest(null, FUNCTION_NAME);
            client.invokeFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testListFunctionsValidate() {
        try {
            ListFunctionsRequest request = new ListFunctionsRequest(null);
            client.listFunctions(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            ListFunctionsRequest request = new ListFunctionsRequest("");
            client.listFunctions(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testListTriggersValidate() {
        try {
            ListTriggersRequest request = new ListTriggersRequest(null, FUNCTION_NAME);
            client.listTriggers(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            ListTriggersRequest request = new ListTriggersRequest("", FUNCTION_NAME);
            client.listTriggers(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            ListTriggersRequest request = new ListTriggersRequest(SERVICE_NAME, null);
            client.listTriggers(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            ListTriggersRequest request = new ListTriggersRequest(SERVICE_NAME, "");
            client.listTriggers(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testUpdateServiceValidate() {
        try {
            UpdateServiceRequest request = new UpdateServiceRequest(null);
            client.updateService(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            UpdateServiceRequest request = new UpdateServiceRequest("");
            client.updateService(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testUpdateFunctionValidate() {
        try {
            UpdateFunctionRequest request = new UpdateFunctionRequest(SERVICE_NAME, null);
            client.updateFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            UpdateFunctionRequest request = new UpdateFunctionRequest(SERVICE_NAME, "");
            client.updateFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            UpdateFunctionRequest request = new UpdateFunctionRequest(null, FUNCTION_NAME);
            client.updateFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            UpdateFunctionRequest request = new UpdateFunctionRequest("", FUNCTION_NAME);
            client.updateFunction(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testUpdateTriggerValidate() {
        try {
            UpdateTriggerRequest request = new UpdateTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
                null);
            client.updateTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            UpdateTriggerRequest request = new UpdateTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
                "");
            client.updateTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            UpdateTriggerRequest request = new UpdateTriggerRequest(SERVICE_NAME, null,
                TRIGGER_NAME);
            client.updateTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            UpdateTriggerRequest request = new UpdateTriggerRequest(SERVICE_NAME, "", TRIGGER_NAME);
            client.updateTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            UpdateTriggerRequest request = new UpdateTriggerRequest(null, FUNCTION_NAME,
                TRIGGER_NAME);
            client.updateTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            UpdateTriggerRequest request = new UpdateTriggerRequest("", FUNCTION_NAME,
                TRIGGER_NAME);
            client.updateTrigger(request);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }
    }

    @Test
    public void testCreateFunctionSetDir() throws IOException {
        createService(SERVICE_NAME);

        // Create a function
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);
        createFuncReq.setFunctionName(FUNCTION_NAME);
        createFuncReq.setDescription("Function for test");
        createFuncReq.setMemorySize(128);
        createFuncReq.setHandler("hello_world.handler");
        createFuncReq.setRuntime("nodejs4.4");

        // Setup code directory
        String tmpDir = "/tmp/fc_test_" + UUID.randomUUID();
        String funcFilePath = tmpDir + "/" + "hello_world.js";
        new File(tmpDir).mkdir();

        PrintWriter out = new PrintWriter(funcFilePath);
        out.println(
            "'use strict'; module.exports.handler = function(event, context, callback) {console.log('hello world'); callback(null, 'hello world');};");
        out.close();

        Code code = new Code().setDir(tmpDir);
        createFuncReq.setCode(code);
        createFuncReq.setTimeout(10);
        client.createFunction(createFuncReq);

        // Invoke the function
        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        InvokeFunctionResponse response = client.invokeFunction(request);

        assertEquals("hello world", new String(response.getContent()));

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME));
        client.deleteService(new DeleteServiceRequest(SERVICE_NAME));

        new File(funcFilePath).delete();
        new File(tmpDir).delete();
    }

    @Test
    public void testCreateFunctionSetZipFile() throws IOException {
        createService(SERVICE_NAME);

        // Create a function
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);
        createFuncReq.setFunctionName(FUNCTION_NAME);
        createFuncReq.setDescription("Function for test");
        createFuncReq.setMemorySize(128);
        createFuncReq.setHandler("hello_world.handler");
        createFuncReq.setRuntime("nodejs4.4");

        // Setup code directory
        String tmpDir = "/tmp/fc_test_" + UUID.randomUUID();
        String funcFilePath = tmpDir + "/" + "hello_world.js";
        new File(tmpDir).mkdir();
        PrintWriter out = new PrintWriter(funcFilePath);
        out.println(
            "'use strict'; module.exports.handler = function(event, context, callback) {console.log('hello world'); callback(null, 'hello world');};");
        out.close();
        String zipFilePath = tmpDir + "/" + "hello_world.zip";
        ZipUtils.zipDir(new File(tmpDir), zipFilePath);

        File zipFile = new File(zipFilePath);
        byte[] buffer = new byte[(int) zipFile.length()];
        FileInputStream fis = new FileInputStream(zipFilePath);
        fis.read(buffer);
        fis.close();
        Code code = new Code().setZipFile(buffer);
        createFuncReq.setCode(code);
        createFuncReq.setTimeout(10);
        client.createFunction(createFuncReq);

        // Invoke the function
        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        InvokeFunctionResponse response = client.invokeFunction(request);

        assertEquals("hello world", new String(response.getContent()));

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME));
        client.deleteService(new DeleteServiceRequest(SERVICE_NAME));

        new File(zipFilePath).delete();
        new File(funcFilePath).delete();
        new File(tmpDir).delete();
    }

    private Credentials getAssumeRoleCredentials(String policy)
        throws com.aliyuncs.exceptions.ClientException {
        IClientProfile profile = DefaultProfile
            .getProfile(REGION, ACCESS_KEY, SECRET_KEY);
        //DefaultProfile.addEndpoint("sts.us-west-1.aliyuncs.com", "us-west-1", "Sts", "sts.us-west-1.aliyuncs.com");
        DefaultAcsClient client = new DefaultAcsClient(profile);

        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setVersion(STS_API_VERSION);
        request.setMethod(MethodType.POST);
        request.setProtocol(ProtocolType.HTTPS);
        request.setRoleArn(ROLE);
        request.setRoleSessionName("test-session");
        if (policy != null) {
            request.setPolicy(policy);
        }

        AssumeRoleResponse stsResponse;
        try {
            stsResponse = client.getAcsResponse(request);
        } catch (com.aliyuncs.exceptions.ClientException e) {
            throw new RuntimeException(e);
        }

        String accessKey = stsResponse.getCredentials().getAccessKeyId();
        String secretKey = stsResponse.getCredentials().getAccessKeySecret();
        String stsToken = stsResponse.getCredentials().getSecurityToken();

        assertNotNull(accessKey);
        assertNotNull(secretKey);
        assertNotNull(stsToken);

        return stsResponse.getCredentials();
    }

    private void testCRUDHelper(boolean testTrigger) throws ParseException, InterruptedException {
        // Create Service
        CreateServiceResponse createSResp = createService(SERVICE_NAME);
        assertFalse(Strings.isNullOrEmpty(createSResp.getRequestId()));
        assertFalse(Strings.isNullOrEmpty(createSResp.getServiceId()));
        assertEquals(SERVICE_NAME, createSResp.getServiceName());
        assertEquals(SERVICE_DESC_OLD, createSResp.getDescription());
        assertEquals(ROLE, createSResp.getRole());
        GetServiceResponse svcOldResp = client.getService(new GetServiceRequest(SERVICE_NAME));

        // Update Service
        UpdateServiceRequest updateSReq = new UpdateServiceRequest(SERVICE_NAME);
        updateSReq.setDescription(SERVICE_DESC_NEW);
        Thread.sleep(1000L);
        UpdateServiceResponse updateSResp = client.updateService(updateSReq);
        verifyUpdate(svcOldResp.getServiceName(), updateSResp.getServiceName(),
            svcOldResp.getServiceId(), updateSResp.getServiceId(),
            svcOldResp.getLastModifiedTime(), updateSResp.getLastModifiedTime(),
            svcOldResp.getCreatedTime(), updateSResp.getCreatedTime(),
            svcOldResp.getDescription(), updateSResp.getDescription());

        // Get Service
        GetServiceRequest getSReq = new GetServiceRequest(SERVICE_NAME);
        GetServiceResponse getSResp = client.getService(getSReq);
        assertEquals(SERVICE_NAME, getSResp.getServiceName());
        assertEquals(svcOldResp.getServiceId(), getSResp.getServiceId());
        assertEquals(ROLE, getSResp.getRole());

        // Create Function
        CreateFunctionResponse createFResp = createFunction(FUNCTION_NAME);
        assertFalse(Strings.isNullOrEmpty(createFResp.getRequestId()));
        assertFalse(Strings.isNullOrEmpty(createFResp.getFunctionId()));
        assertEquals(FUNCTION_NAME, createFResp.getFunctionName());
        assertEquals(FUNCTION_DESC_OLD, createFResp.getDescription());

        // List Functions
        ListFunctionsRequest listFReq = new ListFunctionsRequest(SERVICE_NAME);
        ListFunctionsResponse listFResp = client.listFunctions(listFReq);
        assertFalse(Strings.isNullOrEmpty(listFResp.getRequestId()));
        assertEquals(1, listFResp.getFunctions().length);
        FunctionMetadata funcOld = listFResp.getFunctions()[0];
        assertEquals(FUNCTION_NAME, funcOld.getFunctionName());

        // Update Function
        UpdateFunctionRequest updateFReq = new UpdateFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        updateFReq.setDescription(FUNCTION_DESC_NEW);
        Thread.sleep(1000L);
        client.updateFunction(updateFReq);
        listFResp = client.listFunctions(listFReq);
        assertFalse(Strings.isNullOrEmpty(listFResp.getRequestId()));
        assertEquals(1, listFResp.getFunctions().length);
        FunctionMetadata funcNew = listFResp.getFunctions()[0];
        verifyUpdate(funcOld.getFunctionName(), funcNew.getFunctionName(),
            funcOld.getFunctionId(), funcNew.getFunctionId(),
            funcOld.getLastModifiedTime(), funcNew.getLastModifiedTime(),
            funcOld.getCreatedTime(), funcNew.getCreatedTime(),
            funcOld.getDescription(), funcNew.getDescription());

        // Get Function
        GetFunctionRequest getFReq = new GetFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        GetFunctionResponse getFResp = client.getFunction(getFReq);
        assertFalse(Strings.isNullOrEmpty(getFResp.getRequestId()));
        assertEquals(FUNCTION_NAME, getFResp.getFunctionName());

        // Invoke Function
        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        InvokeFunctionResponse response = client.invokeFunction(request);
        assertTrue(!Strings.isNullOrEmpty(response.getRequestId()));
        assertEquals("hello world", new String(response.getContent()));

        if (testTrigger) {
            // Create Trigger
            String tfPrefix = "prefix";
            String tfSuffix = "suffix";

            createTrigger(TRIGGER_NAME, tfPrefix, tfSuffix);

            // List Triggers
            ListTriggersRequest listTReq = new ListTriggersRequest(SERVICE_NAME, FUNCTION_NAME);
            ListTriggersResponse listTResp = client.listTriggers(listTReq);
            assertFalse(Strings.isNullOrEmpty(listTResp.getRequestId()));
            assertEquals(1, listTResp.getTriggers().length);
            TriggerMetadata triggerOld = listTResp.getTriggers()[0];
            assertEquals(TRIGGER_NAME, triggerOld.getTriggerName());

            // Update Trigger
            String newInvocationRole = INVOCATION_ROLE + "_new";
            String tfPrefixNew = "prefix_new";
            String tfSuffixNew = "suffix_new";
            String[] eventsNew = new String[]{"oss:ObjectCreated:PutObject"};
            OSSTriggerConfig updateTriggerConfig = new OSSTriggerConfig(
                new String[]{"oss:ObjectCreated:PutObject"}, tfPrefixNew, tfSuffixNew);

            UpdateTriggerRequest updateTReq = new UpdateTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
                TRIGGER_NAME);
            updateTReq.setInvocationRole(newInvocationRole);
            updateTReq.setTriggerConfig(updateTriggerConfig);
            Thread.sleep(1000L);
            UpdateTriggerResponse updateTResp = client.updateTrigger(updateTReq);
            assertEquals(triggerOld.getTriggerName(), updateTResp.getTriggerName());
            assertNotEquals(triggerOld.getInvocationRole(), updateTResp.getInvocationRole());
            assertEquals(triggerOld.getSourceArn(), updateTResp.getSourceArn());
            Gson gson = new Gson();
            OSSTriggerConfig tcOld = gson
                .fromJson(gson.toJson(triggerOld.getTriggerConfig()), OSSTriggerConfig.class);
            OSSTriggerConfig tcNew = gson
                .fromJson(gson.toJson(updateTResp.getTriggerConfig()), OSSTriggerConfig.class);
            assertFalse(Arrays.deepEquals(tcOld.getEvents(), tcNew.getEvents()));
            assertNotEquals(tcOld.getFilter().getKey().getPrefix(),
                tcNew.getFilter().getKey().getPrefix());
            assertNotEquals(tcOld.getFilter().getKey().getSuffix(),
                tcNew.getFilter().getKey().getSuffix());
            assertEquals(triggerOld.getCreatedTime(), updateTResp.getCreatedTime());
            assertEquals(triggerOld.getTriggerType(), updateTResp.getTriggerType());
            assertNotEquals(triggerOld.getInvocationRole(), updateTResp.getInvocationRole());

            Date dateOld = DATE_FORMAT.parse(triggerOld.getLastModifiedTime());
            Date dateNew = DATE_FORMAT.parse(updateTResp.getLastModifiedTime());
            assertTrue(dateOld.before(dateNew));

            // Get Trigger
            GetTriggerRequest getTReq = new GetTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
                TRIGGER_NAME);
            GetTriggerResponse getTResp = client.getTrigger(getTReq);
            OSSTriggerConfig getTConfig = gson
                .fromJson(gson.toJson(getTResp.getTriggerConfig()), OSSTriggerConfig.class);
            assertFalse(Strings.isNullOrEmpty(getTResp.getRequestId()));
            assertEquals(TRIGGER_NAME, getTResp.getTriggerName());
            assertEquals(OSS_SOURCE_ARN, getTResp.getSourceARN());
            assertEquals(TRIGGER_TYPE_OSS, getTResp.getTriggerType());
            assertEquals(newInvocationRole, getTResp.getInvocationRole());
            assertEquals(tfPrefixNew, getTConfig.getFilter().getKey().getPrefix());
            assertEquals(tfSuffixNew, getTConfig.getFilter().getKey().getSuffix());
            assertTrue(Arrays.deepEquals(eventsNew, getTConfig.getEvents()));

            // Delete Trigger
            client
                .deleteTrigger(new DeleteTriggerRequest(SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME));

        }
        // Delete Function
        DeleteFunctionRequest deleteFReq = new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        int numFunctionsOld = listFResp.getFunctions().length;
        DeleteFunctionResponse deleteFResp = client.deleteFunction(deleteFReq);
        assertFalse(Strings.isNullOrEmpty(deleteFResp.getRequestId()));
        listFResp = client.listFunctions(listFReq);

        try {
            getFunction(FUNCTION_NAME, listFResp.getFunctions());
            fail("Function " + FUNCTION_NAME + " failed to be deleted");
        } catch (RuntimeException e) {
            int numFunctionsNew = (listFResp.getFunctions() == null) ? 0 :
                listFResp.getFunctions().length;
            assertEquals(numFunctionsOld, numFunctionsNew + 1);
        }
        GetFunctionResponse getFResp2 = null;
        try {
            getFResp2 = client.getFunction(getFReq);
            fail(
                "Get Function " + FUNCTION_NAME + " should have no function returned after delete");
        } catch (ClientException e) {
            assertNull(getFResp2);
        }

        // Delete Service
        DeleteServiceRequest deleteSReq = new DeleteServiceRequest(SERVICE_NAME);
        DeleteServiceResponse deleteSResp = client.deleteService(deleteSReq);
        assertFalse(Strings.isNullOrEmpty(deleteSResp.getRequestId()));

        GetServiceResponse getSResp2 = null;
        try {
            getSResp2 = client.getService(getSReq);
            fail("Get service " + FUNCTION_NAME + " should have no service returned after delete");
        } catch (ClientException e) {
            assertNull(getSResp2);
        }
    }

    private void verifyUpdate(String nameOld, String nameNew, String idOld,
        String idNew, String lastModifiedTimeOld, String lastModifiedTimeNew,
        String createdTimeOld, String createdTimeNew, String descOld, String descNew)
        throws ParseException {
        assertEquals(nameNew, nameOld);
        assertEquals(idNew, idOld);
        Date dateOld = DATE_FORMAT.parse(lastModifiedTimeOld);
        Date dateNew = DATE_FORMAT.parse(lastModifiedTimeNew);
        assertTrue(dateOld.before(dateNew));
        Date cDateOld = DATE_FORMAT.parse(createdTimeOld);
        Date cDateNew = DATE_FORMAT.parse(createdTimeNew);
        assertEquals(cDateNew, cDateOld);

        assertNotEquals(descNew, descOld);
    }

    private FunctionMetadata getFunction(String functionName, FunctionMetadata[] functions) {
        for (FunctionMetadata function : functions) {
            if (functionName.equals(function.getFunctionName())) {
                return function;
            }
        }
        throw new RuntimeException("Function " + functionName + " does not exist");
    }
}
