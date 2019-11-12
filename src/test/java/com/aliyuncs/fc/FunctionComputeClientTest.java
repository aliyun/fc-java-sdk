package com.aliyuncs.fc;

import static com.aliyuncs.fc.model.HttpAuthType.ANONYMOUS;
import static com.aliyuncs.fc.model.HttpAuthType.FUNCTION;
import static com.aliyuncs.fc.model.HttpMethod.DELETE;
import static com.aliyuncs.fc.model.HttpMethod.GET;
import static com.aliyuncs.fc.model.HttpMethod.HEAD;
import static com.aliyuncs.fc.model.HttpMethod.POST;
import static com.aliyuncs.fc.model.HttpMethod.PUT;
import static java.util.Arrays.asList;
import static java.util.Arrays.deepEquals;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.BasicSessionCredentials;
import com.aliyuncs.auth.InstanceProfileCredentialsProvider;
import com.aliyuncs.fc.client.FunctionComputeClient;
import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ErrorCodes;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.model.*;
import com.aliyuncs.fc.model.NasConfig.NasMountConfig;
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
import com.google.gson.JsonObject;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;
import org.junit.Assert;
import java.util.Map;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Validation for FunctionComputeClient, tests including create/list/get/update
 * service/function/trigger
 */
public class FunctionComputeClientTest {

    public static final String STS_API_VERSION = "2015-04-01";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final String VALIDATE_MSG = "cannot be blank";
    private static final String REGION = System.getenv("REGION");
    private static final String ENDPOINT = System.getenv("ENDPOINT");
    private static final String ROLE = System.getenv("ROLE");
    private static final String STS_ROLE = System.getenv("STS_ROLE");
    private static final String ACCESS_KEY = System.getenv("ACCESS_KEY");
    private static final String SECRET_KEY = System.getenv("SECRET_KEY");
    private static final String ACCOUNT_ID = System.getenv("ACCOUNT_ID");
    private static final String CODE_BUCKET = System.getenv("CODE_BUCKET");
    private static final String INVOCATION_ROLE = System.getenv("INVOCATION_ROLE");
    private static final String LOG_PROJECT = System.getenv("LOG_PROJECT");
    private static final String LOG_STORE = System.getenv("LOG_STORE");
    private static final String VPC_ID = System.getenv("VPC_ID");
    private static final String VSWITCH_IDS = System.getenv("VSWITCH_IDS");
    private static final String SECURITY_GROUP_ID = System.getenv("SECURITY_GROUP_ID");
    private static final String USER_ID = System.getenv("USER_ID");
    private static final String GROUP_ID = System.getenv("GROUP_ID");
    private static final String NAS_SERVER_ADDR = System.getenv("NAS_SERVER_ADDR");
    private static final String NAS_MOUNT_DIR = System.getenv("NAS_MOUNT_DIR");
    private static final String PUBLIC_KEY_CERTIFICATE_01 = System.getenv("PUBLIC_KEY_CERTIFICATE_01");
    private static final String PRIVATE_KEY_01 = System.getenv("PRIVATE_KEY_01");
    private static final String PUBLIC_KEY_CERTIFICATE_02 = System.getenv("PUBLIC_KEY_CERTIFICATE_02");
    private static final String PRIVATE_KEY_02 = System.getenv("PRIVATE_KEY_02");

    private static final String OSS_SOURCE_ARN =
        String.format("acs:oss:%s:%s:%s", REGION, ACCOUNT_ID, CODE_BUCKET);
    private static final String LOG_SOURCE_ARN =
        String.format("acs:log:%s:%s:project/%s", REGION, ACCOUNT_ID, LOG_PROJECT);
    private static final String CDN_SOURCE_ARN =
        String.format("acs:cdn:*:%s", ACCOUNT_ID);
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
    private static final String TRIGGER_TYPE_CDN = "cdn_events";
    private static final String TRIGGER_TYPE_TIMER = "timer";
    private static final String CUSTOMDOMAIN_NAME = "javasdk.cn-hongkong.1221968287646227.cname-test.fc.aliyun-inc.com";
    private static final String CERT_NAME = "CERT_NAME";
    private static final Gson gson = new Gson();
    private FunctionComputeClient client;

    @BeforeClass
    public static void setupSuite() {
        System.out.println("ENDPOINT: " + ENDPOINT);
        System.out.println("ROLE: " + ROLE);
        System.out.println("VPC_ID: " + VPC_ID);
        System.out.println("STS_ROLE: " + STS_ROLE);
    }

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
            cleanUpAliases(SERVICE_NAME);
            cleanUpVersions(SERVICE_NAME);
            cleanUpFunctions(SERVICE_NAME);
            cleanupService(SERVICE_NAME);
            cleanUpFunctions(SERVICE_NAME + "-nas");
            cleanupService(SERVICE_NAME + "-nas");
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
            FunctionComputeClient fcClient = new FunctionComputeClient(
                new Config(REGION, ACCOUNT_ID,
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

    private void cleanupCustomDomain(String customDomainName) {
        DeleteCustomDomainRequest request = new DeleteCustomDomainRequest(customDomainName);
        try {
            client.deleteCustomDomain(request);
        } catch (ClientException e) {
            if (!ErrorCodes.DOMAIN_NAME_NOT_FOUND.equals(e.getErrorCode())) {
                throw e;
            }
        }
        System.out.println("CustomDomain " + customDomainName + " is deleted");
    }

    private TriggerMetadata[] listTriggers(String serviceName, String functionName) {
        ListTriggersRequest listReq = new ListTriggersRequest(serviceName,
            functionName);

        ListTriggersResponse listResp = client.listTriggers(listReq);

        assertFalse(Strings.isNullOrEmpty(listResp.getRequestId()));

        return listResp.getTriggers();
    }

    private void cleanUpFunctions(String serviceName) {
        ListFunctionsRequest listFReq = new ListFunctionsRequest(serviceName);
        ListFunctionsResponse listFResp = client.listFunctions(listFReq);
        FunctionMetadata[] functions = listFResp.getFunctions();

        for (FunctionMetadata function : functions) {
            TriggerMetadata[] triggers = listTriggers(serviceName, function.getFunctionName());
            cleanUpTriggers(serviceName, function.getFunctionName(), triggers);
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

    private String cleanUpVersions(String serviceName) {
        ListVersionsRequest listVersionsReq = new ListVersionsRequest(serviceName);
        ListVersionsResponse listVersionResp = client.listVersions(listVersionsReq);
        VersionMetaData[] versions = listVersionResp.getVersions();
        for (VersionMetaData version : versions) {
            DeleteVersionRequest deleteVersionRequest = new DeleteVersionRequest(serviceName,
                version.getVersionId());
            DeleteVersionResponse response = client.deleteVersion(deleteVersionRequest);
            assertTrue(response.isSuccess());
            System.out.println("Version " + version.getVersionId() + " is deleted");
        }
        return (versions.length > 0) ? versions[0].getVersionId() : "0";
    }

    private void cleanUpAliases(String serviceName) {
        ListAliasesRequest listAliasesRequest = new ListAliasesRequest(serviceName);
        ListAliasesResponse listAliasesResponse = client.listAliases(listAliasesRequest);
        AliasMetaData[] aliases = listAliasesResponse.getAliases();
        for (AliasMetaData alias : aliases) {
            DeleteAliasRequest deleteAliasRequest = new DeleteAliasRequest(serviceName,
                alias.getAliasName());
            DeleteAliasResponse response = client.deleteAlias(deleteAliasRequest);
            assertTrue(response.isSuccess());
            System.out.println(alias.getAliasName() + " is deleted");
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

        CreateFunctionResponse response = client.createFunction(createFuncReq);

        assertFalse(Strings.isNullOrEmpty(response.getRequestId()));
        assertFalse(Strings.isNullOrEmpty(response.getFunctionId()));
        assertEquals(functionName, response.getFunctionName());
        assertEquals(FUNCTION_DESC_OLD, response.getDescription());

        environmentVariables = response.getEnvironmentVariables();
        assertEquals(1, environmentVariables.size());
        assertEquals("testValue", environmentVariables.get("testKey"));
        assertEquals(functionName, response.getFunctionName());
        assertEquals(FUNCTION_DESC_OLD, response.getDescription());

        return response;
    }

    private CreateServiceResponse createService(String serviceName) {
        return createSerivce(serviceName, true);
    }

    private CreateServiceResponse createSerivce(String serviceName, boolean check) {
        CreateServiceRequest createSReq = new CreateServiceRequest();
        createSReq.setServiceName(serviceName);
        createSReq.setDescription(SERVICE_DESC_OLD);
        createSReq.setRole(ROLE);

        CreateServiceResponse response = client.createService(createSReq);

        if (check) {
            assertEquals(serviceName, response.getServiceName());
            assertFalse(Strings.isNullOrEmpty(response.getRequestId()));
            assertFalse(Strings.isNullOrEmpty(response.getServiceId()));
            assertEquals(SERVICE_DESC_OLD, response.getDescription());
            assertEquals(ROLE, response.getRole());
        }

        return response;
    }

    private CreateServiceResponse createVPCService(String serviceName) {
        CreateServiceRequest createSReq = new CreateServiceRequest();
        createSReq.setServiceName(serviceName);
        createSReq.setDescription(SERVICE_DESC_OLD);
        createSReq.setRole(ROLE);
        createSReq
            .setVpcConfig(new VpcConfig(VPC_ID, new String[]{VSWITCH_IDS}, SECURITY_GROUP_ID));
        createSReq.setNasConfig(new NasConfig(Integer.parseInt(USER_ID), Integer.parseInt(GROUP_ID),
            new NasMountConfig[]{
                new NasMountConfig(NAS_SERVER_ADDR, NAS_MOUNT_DIR)
            }));

        CreateServiceResponse response = client.createService(createSReq);

        assertEquals(serviceName, response.getServiceName());
        assertFalse(Strings.isNullOrEmpty(response.getRequestId()));
        assertFalse(Strings.isNullOrEmpty(response.getServiceId()));
        assertEquals(SERVICE_DESC_OLD, response.getDescription());
        assertEquals(ROLE, response.getRole());
        assertEquals(VPC_ID, response.getVpcConfig().getVpcId());
        assertEquals(SECURITY_GROUP_ID, response.getVpcConfig().getSecurityGroupId());
        return response;
    }

    private CreateTriggerResponse createHttpTrigger(String triggerName, HttpAuthType authType,
        HttpMethod[] methods) {
        return createHttpTriggerWithQualifier(triggerName, "", authType, methods);
    }

    private CreateTriggerResponse createHttpTriggerWithQualifier(String triggerName,
        String qualifier,
        HttpAuthType authType, HttpMethod[] methods) {
        CreateTriggerRequest createReq = new CreateTriggerRequest(SERVICE_NAME, FUNCTION_NAME);
        createReq.setTriggerName(triggerName);
        createReq.setTriggerType(TRIGGER_TYPE_HTTP);
        createReq.setTriggerConfig(new HttpTriggerConfig(authType, methods));
        if (!qualifier.isEmpty()) {
            createReq.setQualifier(qualifier);
        }
        return client.createTrigger(createReq);
    }

    private CreateTriggerResponse createOssTrigger(String triggerName, String prefix,
        String suffix) {
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

    private DeleteTriggerResponse deleteTrigger(String serviceName, String funcName,
        String triggerName) {
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
        FunctionComputeClient clientHz = new FunctionComputeClient("cn-hangzhou", ACCOUNT_ID,
            ACCESS_KEY, SECRET_KEY);
        ListServicesResponse lrHz = clientHz.listServices(new ListServicesRequest());
        assertTrue(lrHz.getStatus() == HttpURLConnection.HTTP_OK);

        FunctionComputeClient clientBj = new FunctionComputeClient("cn-beijing", ACCOUNT_ID,
            ACCESS_KEY, SECRET_KEY);
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

    private String generateNASPythonCode() {
        return "# -*- coding: utf-8 -*-\n"
            + "import logging  \n"
            + "import random\n"
            + "import string\n"
            + "import os.path\n"
            + "import shutil\n"
            + "from os import path\n"
            + "\n"
            + "def handler(event, context):\n"
            + "  logger = logging.getLogger()\n"
            + "  root_dir1 = \"" + NAS_MOUNT_DIR + "\"\n"
            + "  logger.info('uid : ' + str(os.geteuid()))\n"
            + "  logger.info('gid : ' + str(os.getgid()))\n"
            + "  file_name = randomString(6)+'.txt'\n"
            + "  dir1 = root_dir1 + '/rzhang-test/'\n"
            + "  content = \"NAS here I come\"\n"
            + "  os.makedirs(dir1)\n"
            + "  fw = open(dir1+file_name, \"w+\")\n"
            + "  fw.write(content)\n"
            + "  fw.close()\n"
            + "  fr = open(dir1+file_name)\n"
            + "  line = fr.readline()\n"
            + "  if line != content:\n"
            + "      return False\n"
            + "  fr.close()\n"
            + "  os.remove(dir1+file_name)\n"
            + "  os.rmdir(dir1)\n"
            + "  return True\n"
            + "  \n"
            + "def randomString(n):\n"
            + "  return ''.join(random.SystemRandom().choice(string.ascii_uppercase + string.digits) for _ in range(n))\n";
    }

    @Test
    public void testCreateNASService() throws IOException {
        String service_name = SERVICE_NAME + "-nas";
        createVPCService(service_name);
        // Create a function that uses NAS
        String source = generateNASPythonCode();

        byte[] data = createZipByteData("main.py", source);
        String funcName = FUNCTION_NAME + "-nas";
        // create function
        createFunction(service_name, funcName, "main.handler", "python2.7", data);

        // Invoke the function
        InvokeFunctionRequest request = new InvokeFunctionRequest(service_name, funcName);
        request.setPayload("".getBytes());

        InvokeFunctionResponse response = client.invokeFunction(request);
        assertEquals("true", new String(response.getPayload()));

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(service_name, funcName));
        client.deleteService(new DeleteServiceRequest(service_name));
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
            assertTrue(e.getErrorMessage(), e.getErrorMessage()
                .contains("the caller is not authorized to perform 'ram:PassRole'"));
        }
    }

    @Test
    public void testCRUDHttpTrigger() throws ParseException, InterruptedException, IOException {

        // create service
        createService(SERVICE_NAME);

        // Create Function
        createFunction(FUNCTION_NAME);

        // create http trigger
        createHttpTrigger(TRIGGER_NAME, ANONYMOUS, new HttpMethod[]{GET, POST});

        // List Triggers
        TriggerMetadata[] triggers = listTriggers(SERVICE_NAME, FUNCTION_NAME);

        assertEquals(1, triggers.length);

        TriggerMetadata trigger = triggers[0];

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
        assertTrue(deepEquals(new HttpMethod[]{GET, POST}, triggerConfig.getMethods()));

        // update http trigger
        GetTriggerResponse triggerOld = getTResp;
        HttpTriggerConfig updateTriggerConfig = new HttpTriggerConfig(
            FUNCTION, new HttpMethod[]{POST});

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
        assertFalse(deepEquals(tcOld.getMethods(), tcNew.getMethods()));
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

            TagResourceRequest req  = new TagResourceRequest();
            req.setResourceArn(String.format("acs:fc:%s:%s:services/%s", REGION, ACCOUNT_ID, SERVICE_NAME + i));
            Map<String, String> tags = new HashMap<String, String>();
            if(i % 2 == 0){
                tags.put("k1", "v1");
            } else{
                tags.put("k2", "v2");
            }
            tags.put("k3", "v3");
            req.setTags(tags);
            TagResourceResponse resp = client.tagResource(req);
            assertFalse(Strings.isNullOrEmpty(resp.getRequestId()));
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

        listRequest = new ListServicesRequest();
        listRequest.setPrefix(SERVICE_NAME);
        Map<String, String> tags = new HashMap<String, String>();
        tags.put("k3", "v3");
        listRequest.setTags(tags);
        listResponse = client.listServices(listRequest);
        assertEquals(numServices, listResponse.getServices().length);

        tags.put("k1", "v1");
        listRequest.setTags(tags);
        listResponse = client.listServices(listRequest);
        assertEquals(numServices/2, listResponse.getServices().length);

        tags.put("k2", "v2");
        listRequest.setTags(tags);
        listResponse = client.listServices(listRequest);
        assertEquals(0, listResponse.getServices().length);

        // Delete services
        for (int i = 0; i < numServices; i++) {
            String resourceArn = String.format("acs:fc:%s:%s:services/%s", REGION, ACCOUNT_ID, SERVICE_NAME + i);
            UntagResourceRequest req  = new UntagResourceRequest();
            req.setResourceArn(resourceArn);
            String[] tagKeys = new String[] {};
            req.setTagKeys(tagKeys);
            req.setAll(true);
            UntagResourceResponse resp = client.untagResource(req);
            assertFalse(Strings.isNullOrEmpty(resp.getRequestId()));
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
    public void testOnDemandConfig() throws Exception {
        final int numOnDemandConfigs = 5;
        for (int i = 0; i < numOnDemandConfigs; i++) {
            // put first
            String qualifier = String.format("test-qualifier-%d", i);
            PutOnDemandConfigRequest putOnDemandConfigRequest = new PutOnDemandConfigRequest(SERVICE_NAME, qualifier, FUNCTION_NAME, i);
            PutOnDemandConfigResponse putOnDemandConfigResponse = client.putOnDemandConfig(putOnDemandConfigRequest);

            // validate put response
            assertNotEquals("", putOnDemandConfigResponse.getRequestId());
            String resource = putOnDemandConfigResponse.getResource();
            assertEquals(String.format("services/%s.%s/functions/%s", SERVICE_NAME, qualifier, FUNCTION_NAME), resource);
            assertEquals(i, putOnDemandConfigResponse.getMaximumInstanceCount());

            // validate get response
            GetOnDemandConfigRequest getOnDemandConfigRequest = new GetOnDemandConfigRequest(SERVICE_NAME, qualifier, FUNCTION_NAME);
            GetOnDemandConfigResponse getOnDemandConfigResponse = client.getOnDemandConfig(getOnDemandConfigRequest);
            assertNotEquals("", getOnDemandConfigResponse.getRequestId());
            resource = getOnDemandConfigResponse.getResource();
            assertEquals(String.format("services/%s.%s/functions/%s", SERVICE_NAME, qualifier, FUNCTION_NAME), resource);
            assertEquals(i, putOnDemandConfigResponse.getMaximumInstanceCount());
        }


        // validate list response
        ListOnDemandConfigsRequest listOnDemandConfigsRequest = new ListOnDemandConfigsRequest();
        ListOnDemandConfigsResponse listOnDemandConfigsResponse = client.listOnDemandConfigs(listOnDemandConfigsRequest);
        assertNotEquals("", listOnDemandConfigsResponse.getRequestId());
        assertEquals(numOnDemandConfigs, listOnDemandConfigsResponse.getOnDemandConfigs().length);

        // with prefix
        listOnDemandConfigsRequest = new ListOnDemandConfigsRequest();
        listOnDemandConfigsRequest.setPrefix(String.format("services/%s.%s", SERVICE_NAME, "test-qualifier-1"));
        listOnDemandConfigsResponse = client.listOnDemandConfigs(listOnDemandConfigsRequest);
        assertNotEquals("", listOnDemandConfigsResponse.getRequestId());
        assertEquals(1, listOnDemandConfigsResponse.getOnDemandConfigs().length);

        listOnDemandConfigsRequest = new ListOnDemandConfigsRequest();
        listOnDemandConfigsRequest.setStartKey(String.format("services/%s.%s", SERVICE_NAME, "test-qualifier-3"));
        listOnDemandConfigsResponse = client.listOnDemandConfigs(listOnDemandConfigsRequest);
        assertNotEquals("", listOnDemandConfigsResponse.getRequestId());
        assertEquals(2, listOnDemandConfigsResponse.getOnDemandConfigs().length);

        // with limit
        listOnDemandConfigsRequest = new ListOnDemandConfigsRequest();
        listOnDemandConfigsRequest.setLimit(3);
        listOnDemandConfigsResponse = client.listOnDemandConfigs(listOnDemandConfigsRequest);
        assertEquals(3, listOnDemandConfigsResponse.getOnDemandConfigs().length);

        HashMap<String, OnDemandConfigMetadata> map = new HashMap<String, OnDemandConfigMetadata>();
        for(OnDemandConfigMetadata data : listOnDemandConfigsResponse.getOnDemandConfigs()) {
            map.put(data.getResource(), data);
        }

        // list again
        listOnDemandConfigsRequest.setNextToken(listOnDemandConfigsResponse.getNextToken());
        listOnDemandConfigsResponse = client.listOnDemandConfigs(listOnDemandConfigsRequest);
        assertEquals(2, listOnDemandConfigsResponse.getOnDemandConfigs().length);
        for(OnDemandConfigMetadata data : listOnDemandConfigsResponse.getOnDemandConfigs()) {
            map.put(data.getResource(), data);
        }

        // validate there's no dup resource
        assertEquals(numOnDemandConfigs, map.keySet().size());

        for (int i = 0; i < numOnDemandConfigs; i++) {
            // delete configs
            String qualifier = String.format("test-qualifier-%d", i);
            DeleteOnDemandConfigRequest deleteOnDemandConfigRequest = new DeleteOnDemandConfigRequest(SERVICE_NAME, qualifier, FUNCTION_NAME);
            DeleteOnDemandConfigResponse deleteOnDemandConfigResponse = client.deleteOnDemandConfig(deleteOnDemandConfigRequest);
            assertNotEquals("", deleteOnDemandConfigResponse.getRequestId());

            try {
                // validate config not found
                GetOnDemandConfigRequest getOnDemandConfigRequest = new GetOnDemandConfigRequest(SERVICE_NAME, qualifier, FUNCTION_NAME);
                GetOnDemandConfigResponse getOnDemandConfigResponse = client.getOnDemandConfig(getOnDemandConfigRequest);
            } catch (ClientException e) {
                assertEquals("OnDemandConfigNotFound", e.getErrorCode());
            }
        }

        // validate no configs can be listed
        listOnDemandConfigsRequest = new ListOnDemandConfigsRequest();
        listOnDemandConfigsResponse = client.listOnDemandConfigs(listOnDemandConfigsRequest);
        assertNotEquals("", listOnDemandConfigsResponse.getRequestId());
        assertEquals(null, listOnDemandConfigsResponse.getOnDemandConfigs());
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
            listTriggers(null, FUNCTION_NAME);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            listTriggers("", FUNCTION_NAME);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            listTriggers(SERVICE_NAME, null);
            fail("ClientException is expected");
        } catch (ClientException e) {
            assertTrue(e.getMessage().contains(VALIDATE_MSG));
        }

        try {
            listTriggers(SERVICE_NAME, "");
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

    @Test(expected = ClientException.class)
    public void testInvokeFunctionTimeout() throws IOException {
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);

        // 1 milliseconds timeout
        client.getConfig().setReadTimeoutMillis(1);

        client.invokeFunction(request);
    }

    @Test
    public void testInvokeFunctionLogTypeSyncNone() throws IOException {
        createService(SERVICE_NAME);
        String funcName = FUNCTION_NAME + "-logtype";
        createFunction(funcName);

        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, funcName);
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
            assertEquals(
                "LogType is set to an invalid value (allowed: Tail | None, actual: 'Invalid')",
                e.getErrorMessage());
            return;
        }

        fail("ClientException is expected");
    }

    @Test
    public void testInvokeFunctionLogTypeAsyncNone() throws IOException, InterruptedException {
        createService(SERVICE_NAME);
        createFunction(FUNCTION_NAME);

        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        request.setInvocationType(Const.INVOCATION_TYPE_ASYNC);
        request.setLogType("None");
        InvokeFunctionResponse response = client.invokeFunction(request);
        assertEquals(HttpURLConnection.HTTP_ACCEPTED, response.getStatus());

        Thread.sleep(1000);
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
            assertEquals("LogType is set to an invalid value (allowed: None, actual: 'Tail')",
                e.getErrorMessage());
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

    private void createFunction(String serviceName, String functionName, String handler,
        String runtime, byte[] data) {
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(serviceName);

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
    public void testHttpInvokeFunction() throws IOException, InterruptedException {
        createService(SERVICE_NAME);

        // Create a function
        String source = generatePythonHttpCode();

        byte[] data = createZipByteData("main.py", source);

        // create function
        createFunction(SERVICE_NAME, FUNCTION_NAME, "main.echo_handler", "python2.7", data);

        for (HttpAuthType auth : new HttpAuthType[]{ANONYMOUS, FUNCTION}) {
            // create http trigger
            createHttpTrigger(TRIGGER_NAME.concat(auth.toString()), auth,
                new HttpMethod[]{GET, POST});
            // sleep sometime so that the function cache in the API server will
            // be updated (default is 10 seconds)
            Thread.sleep(15000);

            // Invoke the function
            HttpInvokeFunctionRequest request = new HttpInvokeFunctionRequest(SERVICE_NAME,
                FUNCTION_NAME, auth, POST, "/test/path/中文");

            request.addQuery("a", "1");
            request.addQuery("aaa", null);

            request.setHeader("Test-Header-Key", "testHeaderValue");
            request.setHeader("Content-Type", "application/json");

            request.setPayload(new String("data").getBytes());

            InvokeFunctionResponse response = client.invokeFunction(request);

            assertEquals(200, response.getStatus());
            assertTrue(response.getHeader("Content-Type").startsWith("application/json"));
            assertEquals("testHeaderValue", response.getHeader("Test-Header-Key"));

            JsonObject jsonObject = gson
                .fromJson(new String(response.getPayload()), JsonObject.class);

            assertEquals("/test/path/中文", jsonObject.get("path").getAsString());
            assertEquals("aaa=&a=1", jsonObject.get("queries").getAsString());
            assertEquals("data", jsonObject.get("body").getAsString());

            // delete trigger
            deleteTrigger(SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME.concat(auth.toString()));
        }

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME));
        client.deleteService(new DeleteServiceRequest(SERVICE_NAME));
    }

    private String generatePythonHttpCode() {
        return "import json\n" +
            "from cgi import parse_qs, escape\n" +
            "\n" +
            "def echo_handler(environ, start_response):\n" +
            "  \n" +
            "    resp_body_map = {\n" +
            "      \"headers\": {},\n" +
            "      \"queries\": environ.get('QUERY_STRING',''),\n" +
            "      \"body\": environ[\"wsgi.input\"].read(int(environ.get('CONTENT_LENGTH', 0))),\n"
            +
            "      \"path\": environ[\"PATH_INFO\"],\n" +
            "      \"request_uri\": environ['fc.request_uri']\n" +
            "    }\n" +
            "    \n" +
            "    for k, v in environ.items():\n" +
            "      if k.startswith(\"HTTP_\"):\n" +
            "        resp_body_map[\"headers\"][k[5:]] = v\n" +
            "      \n" +
            "    body = json.dumps(resp_body_map)\n" +
            "    \n" +
            "    # do something here\n" +
            "    status = '200 OK'\n" +
            "    response_headers = [('Content-type', 'application/json'),('Test-Header-Key', environ['HTTP_TEST_HEADER_KEY'])]\n"
            +
            "    start_response(status, response_headers)\n" +
            "    return [body]";
    }

    @Test
    public void testHttpInvokeFunctionWithoutQueriesAndBody() throws IOException {
        createService(SERVICE_NAME);

        // Create a function
        String source = generatePythonHttpCode();

        byte[] data = createZipByteData("main.py", source);

        // create function
        createFunction(SERVICE_NAME, FUNCTION_NAME, "main.echo_handler", "python2.7", data);

        for (HttpAuthType auth : new HttpAuthType[]{ANONYMOUS, FUNCTION}) {
            // create http trigger
            createHttpTrigger(TRIGGER_NAME, auth, new HttpMethod[]{GET, POST, PUT, HEAD, DELETE});

            // Invoke the function
            HttpInvokeFunctionRequest request = new HttpInvokeFunctionRequest(SERVICE_NAME,
                FUNCTION_NAME, auth, POST, "/test/path");

            request.setHeader("Test-Header-Key", "testHeaderValue");
            request.setHeader("Content-Type", "application/json");

            InvokeFunctionResponse response = client.invokeFunction(request);

            assertEquals(200, response.getStatus());
            assertTrue(response.getHeader("Content-Type").startsWith("application/json"));
            assertEquals("testHeaderValue", response.getHeader("Test-Header-Key"));

            JsonObject jsonObject = gson
                .fromJson(new String(response.getPayload()), JsonObject.class);

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
        request.setRoleArn(STS_ROLE);
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

    private void testCRUDHelper(boolean testTrigger)
        throws ParseException, InterruptedException, IOException {
        // Create Service
        createService(SERVICE_NAME);

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
        assertEquals("testValueNew", envGet.get("testKey"));
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
            TriggerMetadata[] triggers = listTriggers(SERVICE_NAME, FUNCTION_NAME);

            assertEquals(1, triggers.length);
            TriggerMetadata triggerOld = triggers[0];
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
            assertFalse(deepEquals(tcOld.getEvents(), tcNew.getEvents()));
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
            assertTrue(deepEquals(eventsNew, getTConfig.getEvents()));

            // Delete Trigger
            deleteTrigger(SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME);
        }

        testLogTrigger();
        testTimeTrigger();
        testCdnEventsTrigger();

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

    private void testCdnEventsTrigger() throws ParseException, InterruptedException {
        String triggerName = TRIGGER_TYPE_CDN + "_" + TRIGGER_NAME;

        String EVENT_NAME = "LogFileCreated";

        String EVENT_VERSION = "1.0.0";

        String NOTES = "notes";
        String NEW_NOTES = "updateNotes";

        CdnEventsTriggerConfig config = new CdnEventsTriggerConfig();
        config.setEventName(EVENT_NAME);
        config.setEventVersion(EVENT_VERSION);
        config.setNotes(NOTES);

        Map<String, List<String>> filters = new HashMap<String, List<String>>();
        filters.put("domain", asList("www.taobao.com"));
        filters.put("stream", asList("def"));

        config.setFilter(filters);

        CreateTriggerRequest createTReq = new CreateTriggerRequest(SERVICE_NAME, FUNCTION_NAME);
        createTReq.setTriggerName(triggerName);
        createTReq.setTriggerType(TRIGGER_TYPE_CDN);
        createTReq.setInvocationRole(INVOCATION_ROLE);
        createTReq.setSourceArn(CDN_SOURCE_ARN);
        createTReq.setTriggerConfig(config);
        client.createTrigger(createTReq);

        // List Triggers
        TriggerMetadata[] triggers = listTriggers(SERVICE_NAME, FUNCTION_NAME);

        assertEquals(1, triggers.length);
        TriggerMetadata triggerOld = triggers[0];
        assertEquals(triggerName, triggerOld.getTriggerName());

        Thread.sleep(300);

        Map<String, List<String>> newFilters = new HashMap<String, List<String>>();
        newFilters.put("a", asList("b"));

        CdnEventsTriggerConfig updateConfig = new CdnEventsTriggerConfig();
        updateConfig.setNotes(NEW_NOTES);
        updateConfig.setFilter(newFilters);

        UpdateTriggerRequest req = new UpdateTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
            triggerName);
        req.setInvocationRole(INVOCATION_ROLE);
        req.setTriggerConfig(updateConfig);

        UpdateTriggerResponse updateTResp = client.updateTrigger(req);
        assertEquals(triggerOld.getTriggerName(), updateTResp.getTriggerName());
        assertEquals(triggerOld.getInvocationRole(), updateTResp.getInvocationRole());
        assertEquals(triggerOld.getSourceArn(), updateTResp.getSourceArn());
        Gson gson = new Gson();
        CdnEventsTriggerConfig tcOld = gson
            .fromJson(gson.toJson(triggerOld.getTriggerConfig()), CdnEventsTriggerConfig.class);
        CdnEventsTriggerConfig tcNew = gson
            .fromJson(gson.toJson(updateTResp.getTriggerConfig()), CdnEventsTriggerConfig.class);
        assertEquals(triggerOld.getCreatedTime(), updateTResp.getCreatedTime());
        assertEquals(triggerOld.getTriggerType(), updateTResp.getTriggerType());
        assertEquals(triggerOld.getInvocationRole(), updateTResp.getInvocationRole());
        assertEquals(EVENT_NAME, tcNew.getEventName());
        assertEquals(tcOld.getEventVersion(), tcNew.getEventVersion());
        assertEquals(NEW_NOTES, tcNew.getNotes());
        assertNotEquals(tcOld.getFilter(), tcNew.getFilter());

        Date dateOld = DATE_FORMAT.parse(triggerOld.getLastModifiedTime());
        Date dateNew = DATE_FORMAT.parse(updateTResp.getLastModifiedTime());
        assertTrue(dateOld.before(dateNew));

        // Get Trigger
        GetTriggerRequest getTReq = new GetTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
            triggerName);
        GetTriggerResponse getTResp = client.getTrigger(getTReq);
        config = gson
            .fromJson(gson.toJson(getTResp.getTriggerConfig()), CdnEventsTriggerConfig.class);
        assertFalse(Strings.isNullOrEmpty(getTResp.getRequestId()));
        assertEquals(triggerName, getTResp.getTriggerName());
        assertEquals(CDN_SOURCE_ARN, getTResp.getSourceARN());
        assertEquals(TRIGGER_TYPE_CDN, getTResp.getTriggerType());
        assertEquals(EVENT_NAME, config.getEventName());
        assertEquals(EVENT_VERSION, config.getEventVersion());

        // Delete Trigger
        deleteTrigger(SERVICE_NAME, FUNCTION_NAME, triggerName);
    }

    private CreateTriggerResponse createLogTrigger(String triggerName,
        LogTriggerConfig triggerConfig) {

        CreateTriggerRequest createTReq = new CreateTriggerRequest(SERVICE_NAME, FUNCTION_NAME);
        createTReq.setTriggerName(triggerName);
        createTReq.setTriggerType(TRIGGER_TYPE_LOG);
        createTReq.setInvocationRole(INVOCATION_ROLE);
        createTReq.setSourceArn(LOG_SOURCE_ARN);
        createTReq.setTriggerConfig(triggerConfig);

        return client.createTrigger(createTReq);
    }

    private void testLogTrigger() throws ParseException {

        String triggerName = TRIGGER_TYPE_LOG + "_" + TRIGGER_NAME;
        LogTriggerConfig triggerConfig = new LogTriggerConfig()
            .setSourceConfig(new LogTriggerConfig.SourceConfig(LOG_STORE)).
                setJobConfig(
                    new LogTriggerConfig.JobConfig().setMaxRetryTime(3).setTriggerInterval(60)).
                setLogConfig(new LogTriggerConfig.LogConfig("", "")).
                setFunctionParameter(new HashMap<String, Object>()).setEnable(true);

        createLogTrigger(triggerName, triggerConfig);

        // List Triggers
        TriggerMetadata[] triggers = listTriggers(SERVICE_NAME, FUNCTION_NAME);

        assertEquals(1, triggers.length);
        TriggerMetadata triggerOld = triggers[0];

        assertEquals(triggerName, triggerOld.getTriggerName());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        UpdateTriggerRequest req = new UpdateTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
            triggerName);
        req.setInvocationRole(INVOCATION_ROLE);
        req.setTriggerConfig(
            new LogTriggerConfig().
                setJobConfig(
                    new LogTriggerConfig.JobConfig().setMaxRetryTime(5).setTriggerInterval(120)));
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

    private CreateTriggerResponse createTimeTrigger(String triggerName,
        TimeTriggerConfig timeTriggerConfig) {
        CreateTriggerRequest createTReq = new CreateTriggerRequest(SERVICE_NAME, FUNCTION_NAME);
        createTReq.setTriggerName(triggerName);
        createTReq.setTriggerType(TRIGGER_TYPE_TIMER);
        createTReq.setTriggerConfig(timeTriggerConfig);

        return client.createTrigger(createTReq);
    }

    @Test
    public void testInvokeFunctionWithInitializer()
        throws ParseException, InterruptedException, IOException {
        createService(SERVICE_NAME);
        String functionName = "testInitializer";
        String source = "'use strict';\n" +
            "var counter = 0;\n" +
            "exports.initializer = function(ctx, callback) {\n" +
            "++counter;\n" +
            "callback(null, '');};\n" +
            "exports.handler = function(event, context, callback) {\n" +
            "console.log('hello world, counter is %d', counter);\n" +
            "callback(null, String(counter));};\n";

        byte[] data = createZipByteData("counter.js", source);

        // Create a function
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);
        createFuncReq.setFunctionName(functionName);
        createFuncReq.setDescription("Function for initializer test");
        createFuncReq.setMemorySize(128);
        createFuncReq.setHandler("counter.handler");
        createFuncReq.setInitializer("counter.initializer");
        createFuncReq.setRuntime("nodejs4.4");

        Code code = new Code().setZipFile(data);
        createFuncReq.setCode(code);
        createFuncReq.setTimeout(10);
        client.createFunction(createFuncReq);

        // Update Function
        ListFunctionsRequest listFReq = new ListFunctionsRequest(SERVICE_NAME);
        ListFunctionsResponse listFResp = client.listFunctions(listFReq);
        FunctionMetadata funcOld = listFResp.getFunctions()[0];
        UpdateFunctionRequest updateFReq = new UpdateFunctionRequest(SERVICE_NAME, functionName);
        updateFReq.setDescription(FUNCTION_DESC_NEW);
        GetFunctionRequest getFReq = new GetFunctionRequest(SERVICE_NAME, functionName);
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

        // Invoke the function
        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, functionName);
        InvokeFunctionResponse response = client.invokeFunction(request);

        assertEquals("1", new String(response.getPayload()));

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(SERVICE_NAME, functionName));
        client.deleteService(new DeleteServiceRequest(SERVICE_NAME));
    }

    private void testTimeTrigger() throws ParseException {
        String cronEvery = "@every 5m";
        String cronExpression = "0 2 * * * *";
        String payload = "awesome-fc";
        String triggerName = TRIGGER_TYPE_TIMER + "_" + TRIGGER_NAME;
        Gson gson = new Gson();

        // Create Trigger
        TimeTriggerConfig timeTriggerConfig = new TimeTriggerConfig(cronEvery, payload, true);

        CreateTriggerResponse createTriggerResponse = createTimeTrigger(triggerName,
            timeTriggerConfig);

        assertEquals(triggerName, createTriggerResponse.getTriggerName());
        assertEquals(TRIGGER_TYPE_TIMER, createTriggerResponse.getTriggerType());

        String createTime = createTriggerResponse.getCreatedTime();
        String lastModifiedTime = createTriggerResponse.getLastModifiedTime();
        TimeTriggerConfig tRConfig = gson
            .fromJson(gson.toJson(createTriggerResponse.getTriggerConfig()),
                TimeTriggerConfig.class);

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
        TriggerMetadata[] triggers = listTriggers(SERVICE_NAME, FUNCTION_NAME);

        assertEquals(1, triggers.length);
        TriggerMetadata triggerOld = triggers[0];
        assertEquals(triggerName, triggerOld.getTriggerName());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // Update Triggers
        UpdateTriggerRequest req = new UpdateTriggerRequest(SERVICE_NAME, FUNCTION_NAME,
            triggerName);
        req.setTriggerConfig(
            new TimeTriggerConfig().setCronExpression(cronExpression).setPayload(payload)
                .setEnable(true));
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

    @Test
    public void testCustomDomain()
        throws ClientException, JSONException, InterruptedException, ParseException, IOException {
        cleanupCustomDomain(CUSTOMDOMAIN_NAME);
        // Create custom domain
        CreateCustomDomainRequest createCustomDomainRequest = new CreateCustomDomainRequest();
        PathConfig pathConfig = new PathConfig("/", "serviceName", "functionName", null);
        PathConfig[] routes = new PathConfig[1];
        routes[0] = pathConfig;
        RouteConfig routeConfig = new RouteConfig(routes);
        createCustomDomainRequest.setDomainName(CUSTOMDOMAIN_NAME);
        createCustomDomainRequest.setProtocol("HTTP");
        createCustomDomainRequest.setRouteConfig(routeConfig);
        CreateCustomDomainResponse createCustomDomainResponse = client
            .createCustomDomain(createCustomDomainRequest);
        assertEquals(CUSTOMDOMAIN_NAME, createCustomDomainResponse.getDomainName());
        assertEquals("HTTP", createCustomDomainResponse.getProtocol());
        assertNotNull(createCustomDomainResponse.getRouteConfig().getRoutes());
        assertEqualsRouteConfig(routeConfig, createCustomDomainResponse.getRouteConfig());

        // Update custom domain
        UpdateCustomDomainRequest updateCustomDomainRequest = new UpdateCustomDomainRequest(
            CUSTOMDOMAIN_NAME);
        PathConfig pathConfig1 = new PathConfig("/login", "serviceName1", "functionName1", null);
        PathConfig[] routes1 = new PathConfig[2];
        routes1[0] = pathConfig;
        routes1[1] = pathConfig1;
        RouteConfig routeConfig1 = new RouteConfig(routes1);
        updateCustomDomainRequest.setRouteConfig(routeConfig1);
        UpdateCustomDomainResponse updateCustomDomainResponse = client
            .updateCustomDomain(updateCustomDomainRequest);
        assertEqualsRouteConfig(routeConfig1, updateCustomDomainResponse.getRouteConfig());

        // Get custom domain
        GetCustomDomainRequest getCustomDomainRequest = new GetCustomDomainRequest(
            CUSTOMDOMAIN_NAME);
        GetCustomDomainResponse getCustomDomainResponse = client
            .getCustomDomain(getCustomDomainRequest);
        assertEquals(CUSTOMDOMAIN_NAME, getCustomDomainResponse.getDomainName());
        assertEquals("HTTP", getCustomDomainResponse.getProtocol());
        assertEqualsRouteConfig(routeConfig1, getCustomDomainResponse.getRouteConfig());

        // List custom domain
        ListCustomDomainsRequest listCustomDomainsRequest = new ListCustomDomainsRequest();
        ListCustomDomainsResponse listCustomDomainsResponse = client
            .listCustomDomains(listCustomDomainsRequest);
        assertTrue(listCustomDomainsResponse.getStatus() == HttpURLConnection.HTTP_OK);

        // Delete custom domain
        DeleteCustomDomainRequest deleteCustomDomainRequest = new DeleteCustomDomainRequest(
            CUSTOMDOMAIN_NAME);
        client.deleteCustomDomain(deleteCustomDomainRequest);
    }

    @Test
    public void testCustomDomainWithHTTPS()
            throws ClientException, JSONException, InterruptedException, ParseException, IOException {
        cleanupCustomDomain(CUSTOMDOMAIN_NAME);
        String certificate1 = PUBLIC_KEY_CERTIFICATE_01.replace("\\n", "\n").replace("\"","");
        String certificate2 = PUBLIC_KEY_CERTIFICATE_02.replace("\\n", "\n").replace("\"","");
        String privateKey1 = PRIVATE_KEY_01.replace("\\n", "\n").replace("\"","");
        String privateKey2 = PRIVATE_KEY_02.replace("\\n", "\n").replace("\"","");
        // Create custom domain
        CreateCustomDomainRequest createCustomDomainRequest = new CreateCustomDomainRequest();
        PathConfig pathConfig = new PathConfig("/", "serviceName", "functionName", null);
        PathConfig[] routes = new PathConfig[1];
        routes[0] = pathConfig;
        RouteConfig routeConfig = new RouteConfig(routes);
        CertConfig certConfig = new CertConfig(CERT_NAME, certificate1, privateKey1);
        createCustomDomainRequest.setDomainName(CUSTOMDOMAIN_NAME);
        createCustomDomainRequest.setProtocol("HTTP,HTTPS");
        createCustomDomainRequest.setRouteConfig(routeConfig);
        createCustomDomainRequest.setCertConfig(certConfig);
        CreateCustomDomainResponse createCustomDomainResponse = client
                .createCustomDomain(createCustomDomainRequest);
        assertEquals(CUSTOMDOMAIN_NAME, createCustomDomainResponse.getDomainName());
        assertEquals("HTTP,HTTPS", createCustomDomainResponse.getProtocol());
        assertNotNull(createCustomDomainResponse.getRouteConfig().getRoutes());
        assertEqualsRouteConfig(routeConfig, createCustomDomainResponse.getRouteConfig());
        assertNotNull(createCustomDomainResponse.getCertConfig());
        assertEquals(certificate1, createCustomDomainResponse.getCertConfig().getCertificate());

        // Update custom domain
        UpdateCustomDomainRequest updateCustomDomainRequest = new UpdateCustomDomainRequest(
                CUSTOMDOMAIN_NAME);
        PathConfig pathConfig1 = new PathConfig("/login", "serviceName1", "functionName1", null);
        PathConfig[] routes1 = new PathConfig[2];
        routes1[0] = pathConfig;
        routes1[1] = pathConfig1;
        RouteConfig routeConfig1 = new RouteConfig(routes1);
        certConfig = new CertConfig(CERT_NAME, certificate2, privateKey2);
        updateCustomDomainRequest.setRouteConfig(routeConfig1);
        updateCustomDomainRequest.setCertConfig(certConfig);
        UpdateCustomDomainResponse updateCustomDomainResponse = client
                .updateCustomDomain(updateCustomDomainRequest);
        assertEqualsRouteConfig(routeConfig1, updateCustomDomainResponse.getRouteConfig());
        assertNotNull(updateCustomDomainResponse.getCertConfig());
        assertEquals(certificate2, updateCustomDomainResponse.getCertConfig().getCertificate());

        // Get custom domain
        GetCustomDomainRequest getCustomDomainRequest = new GetCustomDomainRequest(
                CUSTOMDOMAIN_NAME);
        GetCustomDomainResponse getCustomDomainResponse = client
                .getCustomDomain(getCustomDomainRequest);
        assertEquals(CUSTOMDOMAIN_NAME, getCustomDomainResponse.getDomainName());
        assertEquals("HTTP,HTTPS", getCustomDomainResponse.getProtocol());
        assertEqualsRouteConfig(routeConfig1, getCustomDomainResponse.getRouteConfig());
        assertNotNull(getCustomDomainResponse.getCertConfig());
        assertEquals(certificate2, getCustomDomainResponse.getCertConfig().getCertificate());

        // List custom domain
        ListCustomDomainsRequest listCustomDomainsRequest = new ListCustomDomainsRequest();
        ListCustomDomainsResponse listCustomDomainsResponse = client
                .listCustomDomains(listCustomDomainsRequest);
        assertTrue(listCustomDomainsResponse.getStatus() == HttpURLConnection.HTTP_OK);

        // Delete custom domain
        DeleteCustomDomainRequest deleteCustomDomainRequest = new DeleteCustomDomainRequest(
                CUSTOMDOMAIN_NAME);
        client.deleteCustomDomain(deleteCustomDomainRequest);
    }

    @Test
    public void testTag()
            throws ClientException, JSONException, InterruptedException, ParseException, IOException {

        final int numServices = 3;
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

            TagResourceRequest req  = new TagResourceRequest();
            req.setResourceArn(String.format("acs:fc:%s:%s:services/%s", REGION, ACCOUNT_ID, SERVICE_NAME + i));
            Map<String, String> tags = new HashMap<String, String>();
            if(i % 2 == 0){
                tags.put("k1", "v1");
            } else{
                tags.put("k2", "v2");
            }
            tags.put("k3", "v3");
            req.setTags(tags);
            TagResourceResponse resp = client.tagResource(req);
            assertFalse(Strings.isNullOrEmpty(resp.getRequestId()));
        }

        for (int i = 0; i < numServices; i++) {
            String resourceArn = String.format("acs:fc:%s:%s:services/%s", REGION, ACCOUNT_ID, SERVICE_NAME + i);
            GetResourceTagsRequest getReq = new GetResourceTagsRequest(resourceArn);
            GetResourceTagsResponse getResp = client.getResourceTags(getReq);
            Assert.assertEquals(resourceArn, getResp.getResourceArn());
            Assert.assertEquals("v3", getResp.getTags().get("k3"));
            if(i % 2 == 0){
                Assert.assertFalse(getResp.getTags().containsKey("k2"));
                Assert.assertEquals("v1", getResp.getTags().get("k1"));
            } else{
                Assert.assertFalse(getResp.getTags().containsKey("k1"));
                Assert.assertEquals("v2", getResp.getTags().get("k2"));
            }

            // unTag k3
            UntagResourceRequest req  = new UntagResourceRequest();
            req.setResourceArn(resourceArn);
            String[] tagKeys = new String[] {"k3"};
            req.setTagKeys(tagKeys);
            UntagResourceResponse resp = client.untagResource(req);
            assertFalse(Strings.isNullOrEmpty(resp.getRequestId()));

            getReq = new GetResourceTagsRequest(resourceArn);
            getResp = client.getResourceTags(getReq);
            Assert.assertEquals(resourceArn, getResp.getResourceArn());
            Assert.assertFalse(getResp.getTags().containsKey("k3"));
            if(i % 2 == 0){
                Assert.assertEquals("v1", getResp.getTags().get("k1"));
            } else{
                Assert.assertEquals("v2", getResp.getTags().get("k2"));
            }

            // unTag all
            req  = new UntagResourceRequest();
            req.setResourceArn(resourceArn);
            tagKeys = new String[] {};
            req.setTagKeys(tagKeys);
            req.setAll(true);
            resp = client.untagResource(req);
            assertFalse(Strings.isNullOrEmpty(resp.getRequestId()));
            getReq = new GetResourceTagsRequest(resourceArn);
            getResp = client.getResourceTags(getReq);
            Assert.assertFalse(getResp.getTags().containsKey("k1"));
            Assert.assertFalse(getResp.getTags().containsKey("k2"));
            Assert.assertFalse(getResp.getTags().containsKey("k3"));
        }

        // Delete services
        for (int i = 0; i < numServices; i++) {
            cleanupService(SERVICE_NAME + i);
        }
    }

    @Test
    public void testVersions() throws ClientException {
        createService(SERVICE_NAME);
        String lastVersion = "0";
        // publish a version
        PublishVersionRequest publishVersionRequest = new PublishVersionRequest(SERVICE_NAME);
        PublishVersionResponse publishVersionResponse = client
            .publishVersion(publishVersionRequest);
        assertEquals(String.format("%d", Integer.parseInt(lastVersion) + 1),
            publishVersionResponse.getVersionId());

        // List versions
        ListVersionsRequest listVersionsRequest = new ListVersionsRequest(SERVICE_NAME);
        ListVersionsResponse listVersionsResponse = client.listVersions(listVersionsRequest);
        assertTrue(listVersionsResponse.getStatus() == HttpURLConnection.HTTP_OK);
        assertEquals(1, listVersionsResponse.getVersions().length);
        assertEquals(publishVersionResponse.getVersionId(),
            listVersionsResponse.getVersions()[0].getVersionId());

        // Delete version
        DeleteVersionRequest deleteVersionRequest = new DeleteVersionRequest(SERVICE_NAME,
            publishVersionResponse.getVersionId());
        DeleteVersionResponse deleteVersionResponse = client.deleteVersion(deleteVersionRequest);
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, deleteVersionResponse.getStatus());
    }

    @Test
    public void testAlias() throws ClientException {
        createService(SERVICE_NAME);
        String lastVersion = cleanUpVersions(SERVICE_NAME);
        // publish a version
        PublishVersionRequest publishVersionRequest = new PublishVersionRequest(SERVICE_NAME);
        PublishVersionResponse publishVersionResponse = client
            .publishVersion(publishVersionRequest);
        assertEquals(String.format("%d", Integer.parseInt(lastVersion) + 1),
            publishVersionResponse.getVersionId());
        lastVersion = publishVersionResponse.getVersionId();

        //Create a Alias against it
        String aliasName = "myAlias";
        CreateAliasRequest createAliasRequest = new CreateAliasRequest(SERVICE_NAME, aliasName,
            lastVersion);
        CreateAliasResponse createAliasResponse = client.createAlias(createAliasRequest);
        assertEquals(HttpURLConnection.HTTP_OK, createAliasResponse.getStatus());
        assertEquals(lastVersion, createAliasResponse.getVersionId());
        assertEquals(aliasName, createAliasResponse.getAliasName());

        //Get Alias
        GetAliasRequest getAliasRequest = new GetAliasRequest(SERVICE_NAME, aliasName);
        GetAliasResponse getAliasResponse = client.getAlias(getAliasRequest);
        assertEquals(HttpURLConnection.HTTP_OK, getAliasResponse.getStatus());
        assertEquals(lastVersion, getAliasResponse.getVersionId());
        assertEquals(aliasName, getAliasResponse.getAliasName());
        assertEquals(0, getAliasResponse.getDescription().length());
        assertNull(getAliasResponse.getAdditionalVersionWeight());

        //Update the Alias
        String description = "my test Alias";
        UpdateAliasRequest updateAliasRequest = new UpdateAliasRequest(SERVICE_NAME, aliasName);
        updateAliasRequest.setDescription(description);
        UpdateAliasResponse updateAliasResponse = client.updateAlias(updateAliasRequest);
        assertEquals(HttpURLConnection.HTTP_OK, updateAliasResponse.getStatus());
        assertEquals(lastVersion, updateAliasResponse.getVersionId());
        assertEquals(aliasName, updateAliasResponse.getAliasName());
        assertEquals(description, updateAliasResponse.getDescription());

        //Get Alias
        getAliasResponse = client.getAlias(getAliasRequest);
        assertEquals(HttpURLConnection.HTTP_OK, getAliasResponse.getStatus());
        assertEquals(lastVersion, getAliasResponse.getVersionId());
        assertEquals(aliasName, getAliasResponse.getAliasName());
        assertEquals(description, getAliasResponse.getDescription());

        // List Alias
        ListAliasesRequest listAliasesRequest = new ListAliasesRequest(SERVICE_NAME);
        ListAliasesResponse listAliasesResponse = client.listAliases(listAliasesRequest);
        assertEquals(HttpURLConnection.HTTP_OK, listAliasesResponse.getStatus());
        assertEquals(1, listAliasesResponse.getAliases().length);
        assertEquals(aliasName, listAliasesResponse.getAliases()[0].getAliasName());
        assertEquals(lastVersion, listAliasesResponse.getAliases()[0].getVersionId());
        assertEquals(description, listAliasesResponse.getAliases()[0].getDescription());

        // Delete Alias
        DeleteAliasRequest deleteAliasRequest = new DeleteAliasRequest(SERVICE_NAME, aliasName);
        DeleteAliasResponse deleteAliasResponse = client
            .deleteAlias(deleteAliasRequest);
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, deleteAliasResponse.getStatus());
    }

    public void testHTTPTriggerWithVersion() throws ClientException, IOException {
        createService(SERVICE_NAME);
        String lastVersion = "0";

        // Create a function
        String source = generatePythonHttpCode();

        byte[] data = createZipByteData("main.py", source);

        // create function
        createFunction(SERVICE_NAME, FUNCTION_NAME, "main.echo_handler", "python2.7", data);

        // publish a version
        PublishVersionRequest publishVersionRequest = new PublishVersionRequest(SERVICE_NAME);
        PublishVersionResponse publishVersionResponse = client
            .publishVersion(publishVersionRequest);
        assertEquals(String.format("%d", Integer.parseInt(lastVersion) + 1),
            publishVersionResponse.getVersionId());

        for (HttpAuthType auth : new HttpAuthType[]{ANONYMOUS, FUNCTION}) {
            // create http trigger
            createHttpTriggerWithQualifier(TRIGGER_NAME, publishVersionResponse.getVersionId(),
                auth, new HttpMethod[]{GET, POST});

            // Invoke the function
            HttpInvokeFunctionRequest request = new HttpInvokeFunctionRequest(SERVICE_NAME,
                FUNCTION_NAME, auth, POST, "/test/path/中文");
            request.setQualifier(publishVersionResponse.getVersionId());
            request.addQuery("a", "1");
            request.addQuery("aaa", null);

            request.setHeader("Test-Header-Key", "testHeaderValue");
            request.setHeader("Content-Type", "application/json");

            request.setPayload(new String("data").getBytes());

            InvokeFunctionResponse response = client.invokeFunction(request);

            assertEquals(200, response.getStatus());
            assertTrue(response.getHeader("Content-Type").startsWith("application/json"));
            assertEquals("testHeaderValue", response.getHeader("Test-Header-Key"));

            JsonObject jsonObject = gson
                .fromJson(new String(response.getPayload()), JsonObject.class);

            assertEquals("/test/path/中文", jsonObject.get("path").getAsString());
            assertEquals("aaa=&a=1", jsonObject.get("queries").getAsString());
            assertEquals("data", jsonObject.get("body").getAsString());

            // delete trigger
            deleteTrigger(SERVICE_NAME, FUNCTION_NAME, TRIGGER_NAME);
        }

        // Delete version
        DeleteVersionRequest deleteVersionRequest = new DeleteVersionRequest(SERVICE_NAME,
            publishVersionResponse.getVersionId());
        DeleteVersionResponse deleteVersionResponse = client.deleteVersion(deleteVersionRequest);
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, deleteVersionResponse.getStatus());

        // Cleanups
        client.deleteFunction(new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME));
        client.deleteService(new DeleteServiceRequest(SERVICE_NAME));
    }


    private void assertEqualsRouteConfig(RouteConfig routeConfig, RouteConfig routeConfigResp) {
        int len = routeConfigResp.getRoutes().length;
        for (int i = 0; i < len; i++) {
            assertEquals(routeConfig.getRoutes()[i].getPath(),
                routeConfigResp.getRoutes()[i].getPath());
            assertEquals(routeConfig.getRoutes()[i].getServiceName(),
                routeConfigResp.getRoutes()[i].getServiceName());
            assertEquals(routeConfig.getRoutes()[i].getFunctionName(),
                routeConfigResp.getRoutes()[i].getFunctionName());
            assertEquals(routeConfig.getRoutes()[i].getQualifier(),
                routeConfigResp.getRoutes()[i].getQualifier());
        }
    }

    @Test
    public void testClientCredentialProviderMock() {
    	// init CredentialProvider
        String ak = "ak";
        String sk = "sk";
        String stsToken = "sts_token";
        BasicSessionCredentials creds = new BasicSessionCredentials(ak, sk, stsToken);

        // mock
        InstanceProfileCredentialsProvider credsProvider = mock(InstanceProfileCredentialsProvider.class);

        try {
            when(credsProvider.getCredentials()).thenReturn(creds);
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }

        // init fc client
        Config config = new Config(REGION, ACCOUNT_ID, credsProvider, false);
        FunctionComputeClient fcClient = new FunctionComputeClient(config);
        client = fcClient;

        // Create a service
        try{
            createService(SERVICE_NAME);
        }catch (Exception e) {
        }

        assertEquals(creds.getAccessKeyId(), config.getAccessKeyID());
        assertEquals(creds.getAccessKeySecret(), config.getAccessKeySecret());
        assertEquals(creds.getSessionToken(), config.getSecurityToken());
    }

    /**
     *  run only on aliyun ecs, and that ecs need bind a RAM Role
     */
    public void testClientCredentialProvider() {
        // init CredentialProvider
        String roleName = "ECSAccessingFCTestRole";
        InstanceProfileCredentialsProvider credsProvider = new InstanceProfileCredentialsProvider(roleName);

        // init fc client
        Config config = new Config(REGION, ACCOUNT_ID, credsProvider, false);
        FunctionComputeClient fcClient = new FunctionComputeClient(config);
        client = fcClient;

        // Create a service
        try{
            createSerivce(SERVICE_NAME, false);
        }catch (Exception e) {
            e.printStackTrace();
        }

        // create a function
        try {
            createFunction(FUNCTION_NAME);
        }catch (Exception e) {
        	e.printStackTrace();
        }

        // Invoke the function with a string as function event parameter, Sync mode
        InvokeFunctionRequest invkReq = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        String payload = "Hello FunctionCompute!";
        invkReq.setPayload(payload.getBytes());
        InvokeFunctionResponse invkResp = client.invokeFunction(invkReq);
        System.out.println(new String(invkResp.getContent()));

        cleanUpFunctions(SERVICE_NAME);
        cleanupService(SERVICE_NAME);
    }

}

