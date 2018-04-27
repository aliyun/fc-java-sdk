package com.aliyuncs.fc;

import static com.aliyuncs.fc.model.HttpAuthType.ANONYMOUS;
import static com.aliyuncs.fc.model.HttpAuthType.FUNCTION;
import static com.aliyuncs.fc.model.HttpMethod.*;
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
import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ErrorCodes;
import com.aliyuncs.fc.model.*;
import com.aliyuncs.fc.request.*;
import com.aliyuncs.fc.response.*;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.JsonObject;
import org.json.JSONException;
import org.junit.Assert;
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
    private static final String ENDPOINT = System.getenv("ENDPOINT");
    private static final String ROLE = System.getenv("ROLE");
    private static final String ACCESS_KEY = System.getenv("ACCESS_KEY");
    private static final String SECRET_KEY = System.getenv("SECRET_KEY");
    private static final String ACCOUNT_ID = System.getenv("ACCOUNT_ID");
    private static final String CODE_BUCKET = System.getenv("CODE_BUCKET");
    private static final String CODE_OBJECT = System.getenv("CODE_OBJECT");
    private static final String INVOCATION_ROLE = System.getenv("INVOCATION_ROLE");
    private static final String LOG_PROJECT = System.getenv("LOG_PROJECT");
    private static final String LOG_STORE = System.getenv("LOG_STORE");

    private static final String OSS_SOURCE_ARN =
        String.format("acs:oss:%s:%s:%s", REGION, ACCOUNT_ID, CODE_BUCKET);
    private static final String LOG_SOURCE_ARN =
            String.format("acs:log:%s:%s:project/%s", REGION, ACCOUNT_ID, LOG_PROJECT);
    private static final String SERVICE_NAME = "testServiceJavaSDK";
    private static final String SERVICE_DESC_OLD = "service desc";
    private static final String SERVICE_DESC_NEW = "service desc updated";
    private static final String FUNCTION_NAME = "testFunction";
    private static final String FUNCTION_DESC_OLD = "function desc";
    private static final String FUNCTION_DESC_NEW = "function desc updated";
    private static final String TRIGGER_NAME = "testTrigger";
    private static final String TRIGGER_TYPE_OSS = "oss";
    private static final String TRIGGER_TYPE_HTTP = "http";
    private static final String TRIGGER_TYPE_LOG = "log";
    private static final String TRIGGER_TYPE_TIMER = "timer";
    public static final String STS_API_VERSION = "2015-04-01";

    private FunctionComputeClient client;

    private static final Gson gson = new Gson();

    @Before
    public void setup() {
        // Create or clean up everything under the test service
        client = new FunctionComputeClient(REGION, ACCOUNT_ID, ACCESS_KEY, SECRET_KEY);
        if (!Strings.isNullOrEmpty(ENDPOINT)) {
            client.setEndpoint(ENDPOINT);
        }

        GetServiceRequest getSReq = new GetServiceRequest(SERVICE_NAME);
        try {
            client.getService(getSReq);

            cleanUpFunctions(SERVICE_NAME);
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
            FunctionComputeClient fcClient = new FunctionComputeClient(new Config(REGION, ACCOUNT_ID,
                creds.getAccessKeyId(), creds.getAccessKeySecret(), creds.getSecurityToken(),
                useHttps));

            if (!Strings.isNullOrEmpty(ENDPOINT)) {
                fcClient.setEndpoint(ENDPOINT);
            }

            return fcClient;
        }
        return new FunctionComputeClient(new Config(REGION, ACCOUNT_ID,
            ACCESS_KEY, SECRET_KEY, null, useHttps));
    }

    private void cleanupService(String serviceName) {
        DeleteServiceRequest request = new DeleteServiceRequest(serviceName);
        client.deleteService(request);
        System.out.println("Service " + serviceName + " is deleted");
    }

    private void cleanUpFunctions(String serviceName) {
        ListFunctionsRequest listFReq = new ListFunctionsRequest(SERVICE_NAME);
        ListFunctionsResponse listFResp = client.listFunctions(listFReq);
        FunctionMetadata[] functions = listFResp.getFunctions();

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
            DeleteTriggerResponse response = deleteTrigger(serviceName, functionName,
                trigger.getTriggerName());
            assertTrue(response.isSuccess());
            System.out.println("Trigger " + trigger.getTriggerName() + " is deleted");
        }
    }

    private CreateFunctionResponse createFunction(String functionName) throws IOException {
        String source = "exports.handler = function(event, context, callback) {\n" +
                "  callback(null, 'hello world');\n" +
                "};";

        byte[] code = createZipByteData("hello_world.js", source);

        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);
        createFuncReq.setFunctionName(functionName);
        createFuncReq.setDescription(FUNCTION_DESC_OLD);
        createFuncReq.setMemorySize(128);
        createFuncReq.setHandler("hello_world.handler");
        createFuncReq.setRuntime("nodejs4.4");
        Map<String, String> environmentVariables = new HashMap<String, String>();
        environmentVariables.put("testKey", "testValue");
        createFuncReq.setEnvironmentVariables(environmentVariables);
        createFuncReq
            .setCode(new Code().setZipFile(code));
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

    private CreateTriggerResponse createHttpTrigger(String triggerName, HttpAuthType authType, HttpMethod[] methods) {
        CreateTriggerRequest createReq = new CreateTriggerRequest(SERVICE_NAME, FUNCTION_NAME);
        createReq.setTriggerName(triggerName);
        createReq.setTriggerType(TRIGGER_TYPE_HTTP);
        createReq.setTriggerConfig(new HttpTriggerConfig(authType, methods));

        return client.createTrigger(createReq);
    }

    private CreateTriggerResponse createOssTrigger(String triggerName, String prefix, String suffix) {
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
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resp;
    }

    private DeleteTriggerResponse deleteTrigger(String serviceName, String funcName, String triggerName) {
        DeleteTriggerRequest req = new DeleteTriggerRequest(serviceName, funcName, triggerName);
        DeleteTriggerResponse resp = client.deleteTrigger(req);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resp;
    }

    private UpdateTriggerResponse updateTrigger(UpdateTriggerRequest req) {
        UpdateTriggerResponse resp = client.updateTrigger(req);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resp;
    }

    // fetch from OSS url and returns crc64 header value
    private String fetchFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(60 * 1000);
            httpConn.setReadTimeout(120 * 1000);

            httpConn.connect();
            assertEquals(200, httpConn.getResponseCode());
            String headerKey = "X-Oss-Hash-Crc64ecma";
            Map<String, List<String>> headers = httpConn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();
                if (null == key || !key.equalsIgnoreCase(headerKey)) {
                    continue;
                }
                List<String> values = entry.getValue();
                StringBuilder builder = new StringBuilder(values.get(0));
                for (int i = 1; i < values.size(); i++) {
                    builder.append(",");
                    builder.append(values.get(i));
                }
                return builder.toString();
            }
        } catch (Exception e) {
            assertFalse(String.format("fetchFromURL %s error: %s", urlString, e.toString()), true);
        }

        return "";
    }

    @Test
    public void testNewRegions() {
        FunctionComputeClient clientHz = new FunctionComputeClient("cn-hangzhou", ACCOUNT_ID, ACCESS_KEY, SECRET_KEY);
        ListServicesResponse lrHz = clientHz.listServices(new ListServicesRequest());
        assertTrue(lrHz.getStatus() == HttpURLConnection.HTTP_OK);

        FunctionComputeClient clientBj = new FunctionComputeClient("cn-beijing", ACCOUNT_ID, ACCESS_KEY, SECRET_KEY);
        ListServicesResponse lrBj = clientBj.listServices(new ListServicesRequest());
        assertTrue(lrBj.getStatus() == HttpURLConnection.HTTP_OK);
    }

    @Test
    public void testCRUD()
            throws ClientException, JSONException, NoSuchAlgorithmException, InterruptedException, ParseException, IOException {
        testCRUDHelper(true);
    }

    @Test
    public void testCRUDStsToken() throws com.aliyuncs.exceptions.ClientException,
            ParseException, InterruptedException, IOException {
        client = overrideFCClient(true, false);
        testCRUDHelper(false);
    }

    @Test
    public void testCRUDStsTokenHttps() throws com.aliyuncs.exceptions.ClientException,
            ParseException, InterruptedException, IOException {
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
    public void testCRUDHttpTrigger() throws ParseException, InterruptedException, IOException {

        // create service
        CreateServiceResponse createSResp = createService(SERVICE_NAME);
        assertEquals(SERVICE_NAME, createSResp.getServiceName());

        // Create Function
        CreateFunctionResponse createFResp = createFunction(FUNCTION_NAME);

        assertFalse(Strings.isNullOrEmpty(createFResp.getRequestId()));
        assertFalse(Strings.isNullOrEmpty(createFResp.getFunctionId()));
        assertEquals(FUNCTION_NAME, createFResp.getFunctionName());
        assertEquals(FUNCTION_DESC_OLD, createFResp.getDescription());

        // create http trigger
        createHttpTrigger(TRIGGER_NAME, ANONYMOUS, new HttpMethod[] {GET, POST});

        // List Triggers
        ListTriggersRequest listTReq = new ListTriggersRequest(SERVICE_NAME, FUNCTION_NAME);
        ListTriggersResponse listTResp = client.listTriggers(listTReq);

        assertFalse(Strings.isNullOrEmpty(listTResp.getRequestId()));
        assertEquals(1, listTResp.getTriggers().length);

        TriggerMetadata trigger = listTResp.getTriggers()[0];

        assertEquals(TRIGGER_NAME, trigger.getTriggerName());
        assertEquals("http", trigger.getTriggerType());

        // retrieve http trigger
        GetTriggerRequest getTReq = new GetTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
                TRIGGER_NAME);

        GetTriggerResponse getTResp = client.getTrigger(getTReq);
        HttpTriggerConfig triggerConfig = gson
                .fromJson(gson.toJson(getTResp.getTriggerConfig()), HttpTriggerConfig.class);

        assertFalse(Strings.isNullOrEmpty(getTResp.getRequestId()));
        assertEquals(TRIGGER_NAME, getTResp.getTriggerName());
        assertEquals(TRIGGER_TYPE_HTTP, getTResp.getTriggerType());
        assertTrue(Arrays.deepEquals(new HttpMethod[] {GET, POST}, triggerConfig.getMethods()));

        // update http trigger
        GetTriggerResponse triggerOld = getTResp;
        HttpTriggerConfig updateTriggerConfig = new HttpTriggerConfig(
                FUNCTION, new HttpMethod[] {POST});

        UpdateTriggerRequest updateTReq = new UpdateTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
                TRIGGER_NAME);
        updateTReq.setTriggerConfig(updateTriggerConfig);

        Thread.sleep(1000);

        UpdateTriggerResponse updateTResp = updateTrigger(updateTReq);
        assertEquals(triggerOld.getTriggerName(), updateTResp.getTriggerName());

        Gson gson = new Gson();
        HttpTriggerConfig tcOld = gson
                .fromJson(gson.toJson(triggerOld.getTriggerConfig()), HttpTriggerConfig.class);
        HttpTriggerConfig tcNew = gson
                .fromJson(gson.toJson(updateTResp.getTriggerConfig()), HttpTriggerConfig.class);
        assertFalse(Arrays.deepEquals(tcOld.getMethods(), tcNew.getMethods()));
        assertNotEquals(tcOld.getAuthType(), tcNew.getAuthType());

        assertEquals(triggerOld.getCreatedTime(), updateTResp.getCreatedTime());
        assertEquals(triggerOld.getTriggerType(), updateTResp.getTriggerType());

        Date dateOld = DATE_FORMAT.parse(triggerOld.getLastModifiedTime());
        Date dateNew = DATE_FORMAT.parse(updateTResp.getLastModifiedTime());

        assertTrue(dateOld.before(dateNew));

        // delete http trigger
        deleteTrigger(SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME);

        getTReq = new GetTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
                TRIGGER_NAME);

        try {
            client.getTrigger(getTReq);
        } catch (ClientException e) {
            assertEquals(404, e.getStatusCode());
        }


        cleanUpFunctions(SERVICE_NAME);
        cleanupService(SERVICE_NAME);
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
    public void testListFunctions() throws IOException {
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

    public void ignoreTestListTriggers() throws IOException {
        final int numTriggers = 5;
        final int limit = 2;

        // Create service
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        // Create multiple trigger under the test function
        for (int i = 0; i < numTriggers; i++) {
            String prefix = "prefix";
            String suffix = "suffix";
            CreateTriggerResponse createTResp = createOssTrigger(TRIGGER_NAME + i,
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
            DeleteTriggerResponse deleteTResp = deleteTrigger(
                    SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME + i);
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

        assertEquals("hello world", new String(response.getPayload()));

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME));
        client.deleteService(new DeleteServiceRequest(SERVICE_NAME));

        new File(funcFilePath).delete();
        new File(tmpDir).delete();
    }

    @Test
    public void testInvokeFunctionLogTypeSyncTail() throws IOException {
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        request.setLogType("Tail");
        InvokeFunctionResponse response = client.invokeFunction(request);
        assertNotNull(response.getLogResult());
        assertTrue(response.getLogResult().contains("Duration"));
        assertTrue(response.getLogResult().contains("Billed Duration"));
        assertTrue(response.getLogResult().contains("Memory Size: 128 MB"));
        assertEquals("hello world", new String(response.getPayload()));
    }

    @Test
    public void testInvokeFunctionLogTypeSyncNone() throws IOException {
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        request.setLogType("None");
        InvokeFunctionResponse response = client.invokeFunction(request);
        assertNull(response.getLogResult());
        assertEquals("hello world", new String(response.getPayload()));
    }

    @Test
    public void testInvokeFunctionLogTypeSyncInvalid() throws IOException {
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        request.setLogType("Invalid");
        try {
            client.invokeFunction(request);
        } catch (ClientException e) {
            assertEquals("InvalidArgument", e.getErrorCode());
            assertEquals("LogType is set to an invalid value (allowed: Tail | None, actual: 'Invalid')", e.getErrorMessage());
            return;
        }

        fail("ClientException is expected");
    }

    @Test
    public void testInvokeFunctionLogTypeAsyncNone() throws IOException {
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        request.setInvocationType(Const.INVOCATION_TYPE_ASYNC);
        request.setLogType("None");
        InvokeFunctionResponse response = client.invokeFunction(request);
        assertEquals(HttpURLConnection.HTTP_ACCEPTED, response.getStatus());
    }

    @Test
    public void testInvokeFunctionLogTypeAsyncInvalid() throws IOException {
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        request.setInvocationType(Const.INVOCATION_TYPE_ASYNC);
        // Anything other than None for async invoke is invalid
        request.setLogType("Tail");
        try {
            client.invokeFunction(request);
        } catch (ClientException e) {
            assertEquals("InvalidArgument", e.getErrorCode());
            assertEquals("LogType is set to an invalid value (allowed: None, actual: 'Tail')", e.getErrorMessage());
            return;
        }

        fail("ClientException is expected");
    }

    private byte[] createZipByteData(String filename, String code) throws IOException {

        // Setup code directory
        String tmpDir = "/tmp/fc_test_" + UUID.randomUUID();
        String funcFilePath = tmpDir + "/" + filename;
        new File(tmpDir).mkdir();
        PrintWriter out = new PrintWriter(funcFilePath);
        out.println(code);
        out.close();
        String zipFilePath = tmpDir + "/" + "main.zip";
        ZipUtils.zipDir(new File(tmpDir), zipFilePath);

        File zipFile = new File(zipFilePath);
        byte[] buffer = new byte[(int) zipFile.length()];
        FileInputStream fis = new FileInputStream(zipFilePath);
        fis.read(buffer);
        fis.close();

        new File(funcFilePath).delete();
        new File(zipFilePath).delete();
        new File(tmpDir).delete();

        return buffer;
    }

    private void createFunction(String functionName, String handler, String runtime, byte[] data) {
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);

        Code code = new Code().setZipFile(data);
        createFuncReq.setFunctionName(functionName);
        createFuncReq.setDescription("test");
        createFuncReq.setHandler(handler);
        createFuncReq.setMemorySize(128);
        createFuncReq.setRuntime(runtime);
        createFuncReq.setCode(code);
        createFuncReq.setTimeout(10);

        client.createFunction(createFuncReq);
    }

    @Test
    public void testHttpInvokeFunction() throws IOException {
        createService(SERVICE_NAME);

        // Create a function
        String source = "import json\n" +
                "\n" +
                "def echo_handler(request, response, context):\n" +
                "\tresp_body_map = {\n" +
                "\t\t\"headers\" : {},\n" +
                "\t\t\"queries\" : {},\n" +
                "\t\t\"body\" : request.body,\n" +
                "\t\t\"path\" : request.path,\n" +
                "\t}\n" +
                "\n" +
                "\tfor headerKey, headerValue in request.headers.items():\n" +
                "\t\tresp_body_map[\"headers\"][headerKey] = headerValue\n" +
                "\n" +
                "\tfor param, value in request.queries.items():\n" +
                "\t\tresp_body_map[\"queries\"][param] = value\n" +
                "\n" +
                "\tbody = json.dumps(resp_body_map)\n" +
                "\tresponse.set_body(body)\n" +
                "\t\n" +
                "\tresponse.set_status_code(200)\n" +
                "\t\n" +
                "\tresponse.set_header(\"Test-Header-Key\", request.headers[\"Test-Header-Key\"])\n" +
                "\tresponse.set_header(\"content-type\", \"application/json\")";

        byte[] data = createZipByteData("main.py", source);

        // create function
        createFunction(FUNCTION_NAME, "main.echo_handler", "python2.7", data);


        for (HttpAuthType auth : new HttpAuthType[] {ANONYMOUS, FUNCTION}) {
            // create http trigger
            createHttpTrigger(TRIGGER_NAME, auth, new HttpMethod[] {GET, POST});

            // Invoke the function
            HttpInvokeFunctionRequest request = new HttpInvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME, auth, POST, "/test/path");

            request.addQuery("a", "1");
            request.addQuery("b", "2");
            request.addQuery("aaa", null);

            request.setHeader("Test-Header-Key", "testHeaderValue");
            request.setHeader("Content-Type", "application/json");

            request.setPayload(new String("data").getBytes());

            InvokeFunctionResponse response = client.invokeFunction(request);

            assertEquals(200, response.getStatus());
            assertTrue(response.getHeader("Content-Type").startsWith("application/json"));
            assertEquals("testHeaderValue", response.getHeader("Test-Header-Key"));

            JsonObject jsonObject = gson.fromJson(new String(response.getPayload()), JsonObject.class);

            assertEquals("/test/path", jsonObject.get("path").getAsString());
            assertEquals("1", jsonObject.get("queries").getAsJsonObject().get("a").getAsString());
            assertEquals("2", jsonObject.get("queries").getAsJsonObject().get("b").getAsString());
            assertEquals("data", jsonObject.get("body").getAsString());

            // delete trigger
            deleteTrigger(SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME);
        }

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME));
        client.deleteService(new DeleteServiceRequest(SERVICE_NAME));
    }

    @Test
    public void testHttpInvokeFunctionWithoutQueriesAndBody() throws IOException {
        createService(SERVICE_NAME);

        // Create a function
        String source = "import json\n" +
                "\n" +
                "def echo_handler(request, response, context):\n" +
                "\tresp_body_map = {\n" +
                "\t\t\"headers\" : {},\n" +
                "\t\t\"queries\" : {},\n" +
                "\t\t\"body\" : request.body,\n" +
                "\t\t\"path\" : request.path,\n" +
                "\t}\n" +
                "\n" +
                "\tfor headerKey, headerValue in request.headers.items():\n" +
                "\t\tresp_body_map[\"headers\"][headerKey] = headerValue\n" +
                "\n" +
                "\tfor param, value in request.queries.items():\n" +
                "\t\tresp_body_map[\"queries\"][param] = value\n" +
                "\n" +
                "\tbody = json.dumps(resp_body_map)\n" +
                "\tresponse.set_body(body)\n" +
                "\t\n" +
                "\tresponse.set_status_code(200)\n" +
                "\t\n" +
                "\tresponse.set_header(\"Test-Header-Key\", request.headers[\"Test-Header-Key\"])\n" +
                "\tresponse.set_header(\"content-type\", \"application/json\")";

        byte[] data = createZipByteData("main.py", source);

        // create function
        createFunction(FUNCTION_NAME, "main.echo_handler", "python2.7", data);


        for (HttpAuthType auth : new HttpAuthType[] {ANONYMOUS, FUNCTION}) {
            // create http trigger
            createHttpTrigger(TRIGGER_NAME, auth, new HttpMethod[] {GET, POST, PUT, HEAD, DELETE});

            // Invoke the function
            HttpInvokeFunctionRequest request = new HttpInvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME, auth, POST, "/test/path");

            request.setHeader("Test-Header-Key", "testHeaderValue");
            request.setHeader("Content-Type", "application/json");

            InvokeFunctionResponse response = client.invokeFunction(request);

            assertEquals(200, response.getStatus());
            assertTrue(response.getHeader("Content-Type").startsWith("application/json"));
            assertEquals("testHeaderValue", response.getHeader("Test-Header-Key"));

            JsonObject jsonObject = gson.fromJson(new String(response.getPayload()), JsonObject.class);

            assertEquals("/test/path", jsonObject.get("path").getAsString());
            assertEquals("", jsonObject.get("body").getAsString());

            // delete trigger
            deleteTrigger(SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME);
        }

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME));
        client.deleteService(new DeleteServiceRequest(SERVICE_NAME));
    }

    @Test
    public void testCreateFunctionSetZipFile() throws IOException {
        createService(SERVICE_NAME);

        String source = "'use strict'; module.exports.handler = function(event, context, callback) {console.log('hello world'); callback(null, 'hello world');};";

        byte[] data = createZipByteData("hello_world.js", source);

        // Create a function
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);
        createFuncReq.setFunctionName(FUNCTION_NAME);
        createFuncReq.setDescription("Function for test");
        createFuncReq.setMemorySize(128);
        createFuncReq.setHandler("hello_world.handler");
        createFuncReq.setRuntime("nodejs4.4");

        Code code = new Code().setZipFile(data);
        createFuncReq.setCode(code);
        createFuncReq.setTimeout(10);
        client.createFunction(createFuncReq);

        // Invoke the function
        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        InvokeFunctionResponse response = client.invokeFunction(request);

        assertEquals("hello world", new String(response.getPayload()));

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME));
        client.deleteService(new DeleteServiceRequest(SERVICE_NAME));
    }

    @Test
    public void testInvokeFunctionSetHeader() throws IOException, InterruptedException {
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        // Headers passed in through setHeader should be respected
        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        request.setHeader("x-fc-invocation-type", Const.INVOCATION_TYPE_ASYNC);

        InvokeFunctionResponse response = client.invokeFunction(request);
        assertEquals(HttpURLConnection.HTTP_ACCEPTED, response.getStatus());

        Thread.sleep(5000);

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

    private void testCRUDHelper(boolean testTrigger) throws ParseException, InterruptedException, IOException {
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
        Map<String, String> environmentVariables = createFResp.getEnvironmentVariables();
        assertEquals(1, environmentVariables.size());
        assertEquals("testValue", environmentVariables.get("testKey"));
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
        GetFunctionRequest getFReq = new GetFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        GetFunctionResponse getFResp = client.getFunction(getFReq);
        Map<String, String> envOriginal = getFResp.getEnvironmentVariables();
        envOriginal.put("testKey", "testValueNew");
        updateFReq.setEnvironmentVariables(envOriginal);
        Thread.sleep(1000L);
        UpdateFunctionResponse updateFResp = client.updateFunction(updateFReq);
        listFResp = client.listFunctions(listFReq);
        Assert.assertEquals("testValueNew", updateFResp.getEnvironmentVariables().get("testKey"));
        assertFalse(Strings.isNullOrEmpty(listFResp.getRequestId()));
        assertEquals(1, listFResp.getFunctions().length);
        FunctionMetadata funcNew = listFResp.getFunctions()[0];
        verifyUpdate(funcOld.getFunctionName(), funcNew.getFunctionName(),
            funcOld.getFunctionId(), funcNew.getFunctionId(),
            funcOld.getLastModifiedTime(), funcNew.getLastModifiedTime(),
            funcOld.getCreatedTime(), funcNew.getCreatedTime(),
            funcOld.getDescription(), funcNew.getDescription());

        // Get Function
        getFReq = new GetFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        getFResp = client.getFunction(getFReq);
        Map<String, String> envGet = getFResp.getEnvironmentVariables();
        assertEquals(1, envGet.size());
        assertEquals("testValueNew",envGet.get("testKey"));
        assertFalse(Strings.isNullOrEmpty(getFResp.getRequestId()));
        assertEquals(FUNCTION_NAME, getFResp.getFunctionName());

        // Get Function Code
        GetFunctionCodeRequest getFCReq = new GetFunctionCodeRequest(SERVICE_NAME, FUNCTION_NAME);
        GetFunctionCodeResponse getFCResp = client.getFunctionCode(getFCReq);
        assertFalse(Strings.isNullOrEmpty(getFResp.getRequestId()));
        String crc64 = fetchFromURL(getFCResp.getCodeUrl());
        assertEquals(crc64, getFCResp.getCodeChecksum());

        // Invoke Function
        InvokeFunctionRequest invkReq = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        InvokeFunctionResponse invkResp = client.invokeFunction(invkReq);
        assertTrue(!Strings.isNullOrEmpty(invkResp.getRequestId()));
        assertEquals("hello world", new String(invkResp.getContent()));

        // Invoke Function Async
        invkReq.setInvocationType(Const.INVOCATION_TYPE_ASYNC);
        invkResp = client.invokeFunction(invkReq);
        assertEquals(HttpURLConnection.HTTP_ACCEPTED, invkResp.getStatus());

        if (testTrigger) {
            // Create Trigger
            String tfPrefix = "prefix";
            String tfSuffix = "suffix";

            createOssTrigger(TRIGGER_NAME, tfPrefix, tfSuffix);

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
            UpdateTriggerResponse updateTResp = updateTrigger(updateTReq);
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
            deleteTrigger(SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME);
        }

        testLogTrigger();
        testTimeTrigger();

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

    private void testLogTrigger() throws ParseException {

        String triggerName = TRIGGER_TYPE_LOG + "_" + TRIGGER_NAME;
        LogTriggerConfig triggerConfig = new LogTriggerConfig().setSourceConfig(new LogTriggerConfig.SourceConfig(LOG_STORE)).
                setJobConfig(new LogTriggerConfig.JobConfig().setMaxRetryTime(3).setTriggerInterval(60)).
                setLogConfig(new LogTriggerConfig.LogConfig("", "")).
                setFunctionParameter(new HashMap<String, Object>()).setEnable(true);
        CreateTriggerRequest createTReq = new CreateTriggerRequest(SERVICE_NAME, FUNCTION_NAME);
        createTReq.setTriggerName(triggerName);
        createTReq.setTriggerType(TRIGGER_TYPE_LOG);
        createTReq.setInvocationRole(INVOCATION_ROLE);
        createTReq.setSourceArn(LOG_SOURCE_ARN);
        createTReq.setTriggerConfig(triggerConfig);
        client.createTrigger(createTReq);

        // List Triggers
        ListTriggersRequest listTReq = new ListTriggersRequest(SERVICE_NAME, FUNCTION_NAME);
        ListTriggersResponse listTResp = client.listTriggers(listTReq);
        assertFalse(Strings.isNullOrEmpty(listTResp.getRequestId()));
        assertEquals(1, listTResp.getTriggers().length);
        TriggerMetadata triggerOld = listTResp.getTriggers()[0];
        assertEquals(triggerName, triggerOld.getTriggerName());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        UpdateTriggerRequest req = new UpdateTriggerRequest(SERVICE_NAME, FUNCTION_NAME, triggerName);
        req.setInvocationRole(INVOCATION_ROLE);
        req.setTriggerConfig(
                new LogTriggerConfig().
                        setJobConfig(new LogTriggerConfig.JobConfig().setMaxRetryTime(5).setTriggerInterval(120)));
        UpdateTriggerResponse updateTResp = client.updateTrigger(req);
        assertEquals(triggerOld.getTriggerName(), updateTResp.getTriggerName());
        assertEquals(triggerOld.getInvocationRole(), updateTResp.getInvocationRole());
        assertEquals(triggerOld.getSourceArn(), updateTResp.getSourceArn());
        Gson gson = new Gson();
        LogTriggerConfig tcOld = gson
                .fromJson(gson.toJson(triggerOld.getTriggerConfig()), LogTriggerConfig.class);
        LogTriggerConfig tcNew = gson
                .fromJson(gson.toJson(updateTResp.getTriggerConfig()), LogTriggerConfig.class);
        assertEquals(triggerOld.getCreatedTime(), updateTResp.getCreatedTime());
        assertEquals(triggerOld.getTriggerType(), updateTResp.getTriggerType());
        assertEquals(triggerOld.getInvocationRole(), updateTResp.getInvocationRole());
        assertEquals(tcOld.getSourceConfig(), tcNew.getSourceConfig());
        assertEquals(tcOld.getLogConfig(), tcNew.getLogConfig());
        assertEquals(tcOld.isEnable(), tcNew.isEnable());
        assertNotEquals(tcOld.getJobConfig(), tcNew.getJobConfig());

        Date dateOld = DATE_FORMAT.parse(triggerOld.getLastModifiedTime());
        Date dateNew = DATE_FORMAT.parse(updateTResp.getLastModifiedTime());
        assertTrue(dateOld.before(dateNew));

        // Get Trigger
        GetTriggerRequest getTReq = new GetTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
                triggerName);
        GetTriggerResponse getTResp = client.getTrigger(getTReq);
        LogTriggerConfig getTConfig = gson
                .fromJson(gson.toJson(getTResp.getTriggerConfig()), LogTriggerConfig.class);
        assertFalse(Strings.isNullOrEmpty(getTResp.getRequestId()));
        assertEquals(triggerName, getTResp.getTriggerName());
        assertEquals(LOG_SOURCE_ARN, getTResp.getSourceARN());
        assertEquals(TRIGGER_TYPE_LOG, getTResp.getTriggerType());
        assertEquals(5, getTConfig.getJobConfig().getMaxRetryTime().intValue());
        assertEquals(120, getTConfig.getJobConfig().getTriggerInterval().intValue());

        // Delete Trigger
        deleteTrigger(SERVICE_NAME, FUNCTION_NAME, triggerName);
    }

    private void testTimeTrigger() throws ParseException {
        String cronEvery = "@every 5m";
        String cronExpression = "0 2 * * * *";
        String payload = "awesome-fc";
        String triggerName = TRIGGER_TYPE_TIMER + "_" + TRIGGER_NAME;
        Gson gson = new Gson();

        // Create Trigger
        CreateTriggerRequest createTReq = new CreateTriggerRequest(SERVICE_NAME, FUNCTION_NAME);
        TimeTriggerConfig timeTriggerConfig = new TimeTriggerConfig(cronEvery, payload, true);
        createTReq.setTriggerName(triggerName);
        createTReq.setTriggerType(TRIGGER_TYPE_TIMER);
        createTReq.setTriggerConfig(timeTriggerConfig);
        CreateTriggerResponse createTriggerResponse = client.createTrigger(createTReq);
        assertEquals(triggerName, createTriggerResponse.getTriggerName());
        assertEquals(TRIGGER_TYPE_TIMER, createTriggerResponse.getTriggerType());
        String createTime = createTriggerResponse.getCreatedTime();
        String lastModifiedTime = createTriggerResponse.getLastModifiedTime();
        TimeTriggerConfig tRConfig = gson
                .fromJson(gson.toJson(createTriggerResponse.getTriggerConfig()), TimeTriggerConfig.class);
        assertEquals(timeTriggerConfig.getCronExpression(), tRConfig.getCronExpression());
        assertEquals(timeTriggerConfig.getPayload(), tRConfig.getPayload());
        assertEquals(timeTriggerConfig.isEnable(), tRConfig.isEnable());
        // Get Trigger
        GetTriggerRequest getTReq = new GetTriggerRequest(SERVICE_NAME, FUNCTION_NAME, triggerName);
        GetTriggerResponse getTResp = client.getTrigger(getTReq);
        TimeTriggerConfig getTConfig = gson
                .fromJson(gson.toJson(getTResp.getTriggerConfig()), TimeTriggerConfig.class);
        assertFalse(Strings.isNullOrEmpty(getTResp.getRequestId()));
        assertEquals(triggerName, getTResp.getTriggerName());
        assertEquals(TRIGGER_TYPE_TIMER, getTResp.getTriggerType());
        assertEquals(timeTriggerConfig.getCronExpression(), getTConfig.getCronExpression());
        assertEquals(timeTriggerConfig.getPayload(), getTConfig.getPayload());
        assertEquals(timeTriggerConfig.isEnable(), getTConfig.isEnable());
        assertEquals(createTime, getTResp.getCreatedTime());
        assertEquals(lastModifiedTime, getTResp.getLastModifiedTime());
        // List Triggers
        ListTriggersRequest listTReq = new ListTriggersRequest(SERVICE_NAME, FUNCTION_NAME);
        ListTriggersResponse listTResp = client.listTriggers(listTReq);
        assertFalse(Strings.isNullOrEmpty(listTResp.getRequestId()));
        assertEquals(1, listTResp.getTriggers().length);
        TriggerMetadata triggerOld = listTResp.getTriggers()[0];
        assertEquals(triggerName, triggerOld.getTriggerName());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // Update Triggers
        UpdateTriggerRequest req = new UpdateTriggerRequest(SERVICE_NAME, FUNCTION_NAME, triggerName);
        req.setTriggerConfig(new TimeTriggerConfig().setCronExpression(cronExpression).setPayload(payload).setEnable(true));
        UpdateTriggerResponse updateTResp = client.updateTrigger(req);
        assertEquals(triggerOld.getTriggerName(), updateTResp.getTriggerName());
        TimeTriggerConfig tcOld = gson
                .fromJson(gson.toJson(triggerOld.getTriggerConfig()), TimeTriggerConfig.class);
        TimeTriggerConfig tcNew = gson
                .fromJson(gson.toJson(updateTResp.getTriggerConfig()), TimeTriggerConfig.class);
        Date dateOld = DATE_FORMAT.parse(triggerOld.getLastModifiedTime());
        Date dateNew = DATE_FORMAT.parse(updateTResp.getLastModifiedTime());
        assertTrue(dateOld.before(dateNew));
        assertNotEquals(tcOld.getCronExpression(), tcNew.getCronExpression());
        assertEquals(tcOld.getPayload(), tcNew.getPayload());
        assertEquals(tcOld.isEnable(), tcNew.isEnable());
        // Delete Trigger
        deleteTrigger(SERVICE_NAME, FUNCTION_NAME, triggerName);
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
