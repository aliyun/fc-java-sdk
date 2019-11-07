package com.aliyuncs.fc.client;

import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ErrorCodes;
import com.aliyuncs.fc.model.Code;
import com.aliyuncs.fc.model.FunctionMetadata;
import com.aliyuncs.fc.model.ServiceMetadata;
import com.aliyuncs.fc.request.*;
import com.aliyuncs.fc.response.*;
import com.aliyuncs.fc.utils.Util;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;


public class AsyncClientTest {
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
    private Config config;
    private AsyncClient client;

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
        config = new Config(REGION, ACCOUNT_ID, ACCESS_KEY, SECRET_KEY, "", true);
        if (!Strings.isNullOrEmpty(ENDPOINT)) {
            config.setEndpoint(ENDPOINT);
        }
        config.setThreadCount(10);
        config.setMaxConnectCount(100);
        config.setMaxPerRoute(100);

        client = new AsyncClient(config);

        try{
            cleanupService(SERVICE_NAME, true);
        }catch (Exception e) {
        }
    }

    @Test
    public void testCommon() throws  Exception {
        // Create service
        createService(SERVICE_NAME, true);
        GetServiceResponse getServiceResponse = getService(SERVICE_NAME, true);

        // List services
        List<ServiceMetadata> services = listAllServices("", true);
        boolean flag = false;
        for(ServiceMetadata meta : services) {
            if(meta.getServiceName().equals(SERVICE_NAME)) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);

        // Update service
        updateService(SERVICE_NAME, "hello world service description", true);

        // Create function
        createFunction(SERVICE_NAME, FUNCTION_NAME, true);
        getFunction(SERVICE_NAME, FUNCTION_NAME, true);

        // List functions
        List<FunctionMetadata> functions = listAllFunctions(SERVICE_NAME, true);
        flag = false;
        for(FunctionMetadata meta : functions) {
            if(meta.getFunctionName().equals(FUNCTION_NAME)) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);


        // Update function
        updateFunction(SERVICE_NAME, FUNCTION_NAME, "hello world function description", true);

        // Invoke function
        invokeFunction(SERVICE_NAME, FUNCTION_NAME, true);

        // Delete function
        deleteFunction(SERVICE_NAME, FUNCTION_NAME, true);
        try {
            GetFunctionResponse getFunctionResponse = getFunction(SERVICE_NAME, FUNCTION_NAME, false);
        } catch (ClientException e) {
            assertEquals(ErrorCodes.FUNCTION_NOT_FOUND, e.getErrorCode());
        }

        // Delete service
        deleteService(SERVICE_NAME, true);
        try {
            GetServiceResponse response = getService(SERVICE_NAME, false);
        } catch (ClientException e) {
            assertEquals(ErrorCodes.SERVICE_NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void testConcurrentInvokeFunction() throws  Exception {
        // Create service
        createService(SERVICE_NAME, true);

        // Create function
        createFunction(SERVICE_NAME, FUNCTION_NAME, true);
        getFunction(SERVICE_NAME, FUNCTION_NAME, true);

        // Invoke function
        InvokeFunctionRequest request = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        final int concurrent = 1000;

        final CountDownLatch latch = new CountDownLatch(concurrent);
        class SelfCallback implements FcCallback<InvokeFunctionRequest, InvokeFunctionResponse> {
            private final long startTime;
            public SelfCallback(long startTime) {
                this.startTime = startTime;
            }

            @Override
            public void onCompleted(InvokeFunctionRequest request, InvokeFunctionResponse response) {
                latch.countDown();
                assertEquals(200, response.getStatus());
                assertEquals("hello world", new String(response.getPayload()));
            }

            @Override
            public void onFailed(InvokeFunctionRequest request, Exception exception) {
                latch.countDown();
                fail();
            }
        }

        long timeStart = System.currentTimeMillis();
        for (int i = 0; i < concurrent; i ++) {
            client.invokeFunction(request, new SelfCallback(System.currentTimeMillis()));
        }
        System.out.println("All request send complete in " + (System.currentTimeMillis() - timeStart) + "ms");
        latch.await();
    }

    private void createService(String serviceName, final boolean check) throws Exception{
        CreateServiceRequest request = new CreateServiceRequest();
        request.setServiceName(serviceName);

        FcCallback<CreateServiceRequest, CreateServiceResponse> callback = new FcCallback<CreateServiceRequest, CreateServiceResponse>() {
            @Override
            public void onCompleted(CreateServiceRequest request, CreateServiceResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                }
            }

            @Override
            public void onFailed(CreateServiceRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<CreateServiceResponse> res = client.createService(request, callback);
        res.get();
    }

    private void updateService(String serviceName, final String desc, final boolean check) throws Exception {
        UpdateServiceRequest request = new UpdateServiceRequest(serviceName);
        request.setDescription(desc);

        FcCallback<UpdateServiceRequest, UpdateServiceResponse> callback = new FcCallback<UpdateServiceRequest, UpdateServiceResponse>() {
            @Override
            public void onCompleted(UpdateServiceRequest request, UpdateServiceResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                    assertEquals(desc, response.getDescription());
                }
            }

            @Override
            public void onFailed(UpdateServiceRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<UpdateServiceResponse> res = client.updateService(request, callback);
        res.get();
    }

    private void deleteService(String serviceName, final boolean check) throws Exception {
        DeleteServiceRequest request = new DeleteServiceRequest(serviceName);

        FcCallback<DeleteServiceRequest, DeleteServiceResponse> callback = new FcCallback<DeleteServiceRequest, DeleteServiceResponse>() {
            @Override
            public void onCompleted(DeleteServiceRequest request, DeleteServiceResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                }
            }

            @Override
            public void onFailed(DeleteServiceRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<DeleteServiceResponse> res = client.deleteService(request, callback);
        res.get();
    }

    private ListServicesResponse listServices(String nextToken, final boolean check) throws Exception {
        ListServicesRequest request = new ListServicesRequest();
        request.setNextToken(nextToken);

        FcCallback<ListServicesRequest, ListServicesResponse> callback = new FcCallback<ListServicesRequest, ListServicesResponse>() {
            @Override
            public void onCompleted(ListServicesRequest request, ListServicesResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                }
            }

            @Override
            public void onFailed(ListServicesRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<ListServicesResponse> res = client.listServices(request, callback);
        return res.get();
    }

    private GetServiceResponse getService(final String serviceName, final boolean check) throws Exception {
        GetServiceRequest request = new GetServiceRequest(serviceName);

        FcCallback<GetServiceRequest, GetServiceResponse> callback = new FcCallback<GetServiceRequest, GetServiceResponse>() {
            @Override
            public void onCompleted(GetServiceRequest request, GetServiceResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                    assertEquals(serviceName, response.getServiceName());
                }
            }

            @Override
            public void onFailed(GetServiceRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<GetServiceResponse> res = client.getService(request, callback);
        return res.get();
    }

    private List<ServiceMetadata> listAllServices(String serviceName, final boolean check) throws Exception {
        List<ServiceMetadata> services = new ArrayList<ServiceMetadata>();
        String nextToken = "";
        while (true) {
            ListServicesResponse resp = listServices(nextToken, check);
            for (ServiceMetadata meta : resp.getServices()) {
                services.add(meta);
            }

            nextToken = resp.getNextToken();
            if (StringUtils.isEmpty(nextToken)) {
                break;
            }
        }
        return services;
    }


    private void createFunction(final String serviceName, final String functionName, final boolean check) throws Exception {
        String source = "exports.handler = function(event, context, callback) {\n" +
                "  callback(null, 'hello world');\n" +
                "};";

        byte[] code = Util.createZipByteData("hello_world.js", source);

        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(serviceName);
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

        FcCallback<CreateFunctionRequest, CreateFunctionResponse> callback = new FcCallback<CreateFunctionRequest, CreateFunctionResponse>() {
            @Override
            public void onCompleted(CreateFunctionRequest request, CreateFunctionResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                    assertTrue(StringUtils.isNotEmpty(response.getRequestId()));
                    assertTrue(StringUtils.isNotEmpty(response.getFunctionId()));
                    assertEquals(functionName, response.getFunctionName());
                    assertEquals(FUNCTION_DESC_OLD, response.getDescription());

                    Map<String, String> environmentVariables = response.getEnvironmentVariables();
                    assertEquals(1, environmentVariables.size());
                    assertEquals("testValue", environmentVariables.get("testKey"));
                }
            }

            @Override
            public void onFailed(CreateFunctionRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<CreateFunctionResponse> res = client.createFunction(createFuncReq, callback);
        res.get();
    }


    private void updateFunction(String serviceName, String functionName, final String desc, final boolean check) throws Exception {
        UpdateFunctionRequest request = new UpdateFunctionRequest(serviceName, functionName);
        request.setDescription(desc);

        FcCallback<UpdateFunctionRequest, UpdateFunctionResponse> callback = new FcCallback<UpdateFunctionRequest, UpdateFunctionResponse>() {
            @Override
            public void onCompleted(UpdateFunctionRequest request, UpdateFunctionResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                    assertEquals(desc, response.getDescription());
                }
            }

            @Override
            public void onFailed(UpdateFunctionRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<UpdateFunctionResponse> res = client.updateFunction(request, callback);
        res.get();
    }

    private void invokeFunction(String serviceName, String functionName, final boolean check) throws Exception {
        InvokeFunctionRequest request = new InvokeFunctionRequest(serviceName, functionName);

        FcCallback<InvokeFunctionRequest, InvokeFunctionResponse> callback = new FcCallback<InvokeFunctionRequest, InvokeFunctionResponse>() {
            @Override
            public void onCompleted(InvokeFunctionRequest request, InvokeFunctionResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                    assertEquals("hello world", new String(response.getPayload()));
                }
            }

            @Override
            public void onFailed(InvokeFunctionRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<InvokeFunctionResponse> res = client.invokeFunction(request, callback);
        res.get();
    }

    private void deleteFunction(String serviceName, String functionName, final boolean check) throws Exception {
        DeleteFunctionRequest request = new DeleteFunctionRequest(serviceName, functionName);

        FcCallback<DeleteFunctionRequest, DeleteFunctionResponse> callback = new FcCallback<DeleteFunctionRequest, DeleteFunctionResponse>() {
            @Override
            public void onCompleted(DeleteFunctionRequest request, DeleteFunctionResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                    assertTrue(StringUtils.isNotEmpty(response.getRequestId()));
                }
            }

            @Override
            public void onFailed(DeleteFunctionRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<DeleteFunctionResponse> res = client.deleteFunction(request, callback);
        res.get();
    }

    private ListFunctionsResponse listFunctions(String serviceName, String nextToken, final boolean check) throws Exception {
        ListFunctionsRequest request = new ListFunctionsRequest(serviceName);
        request.setNextToken(nextToken);

        FcCallback<ListFunctionsRequest, ListFunctionsResponse> callback = new FcCallback<ListFunctionsRequest, ListFunctionsResponse>() {
            @Override
            public void onCompleted(ListFunctionsRequest request, ListFunctionsResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                }
            }

            @Override
            public void onFailed(ListFunctionsRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<ListFunctionsResponse> res = client.listFunctions(request, callback);
        return res.get();
    }

    private GetFunctionResponse getFunction(final String serviceName, final String functionName, final boolean check) throws Exception {
        GetFunctionRequest request = new GetFunctionRequest(serviceName, functionName);

        FcCallback<GetFunctionRequest, GetFunctionResponse> callback = new FcCallback<GetFunctionRequest, GetFunctionResponse>() {
            @Override
            public void onCompleted(GetFunctionRequest request, GetFunctionResponse response) {
                if(check) {
                    assertEquals(200, response.getStatus());
                    assertEquals(functionName, response.getFunctionName());
                }
            }

            @Override
            public void onFailed(GetFunctionRequest request, Exception exception) {
                if (check){
                    fail();
                }
            }
        };

        Future<GetFunctionResponse> res = client.getFunction(request, callback);
        return res.get();
    }

    private List<FunctionMetadata> listAllFunctions(String serviceName, final boolean check) throws Exception {
        List<FunctionMetadata> functions = new ArrayList<FunctionMetadata>();
        String nextToken = "";
        while (true) {
            ListFunctionsResponse resp = listFunctions(serviceName, nextToken, check);
            for (FunctionMetadata meta : resp.getFunctions()) {
                functions.add(meta);
            }

            nextToken = resp.getNextToken();
            if (StringUtils.isEmpty(nextToken)) {
                break;
            }
        }
        return functions;
    }

    private void cleanupFunctions(String serviceName, final boolean check) throws Exception {
        List<FunctionMetadata> functions = listAllFunctions(serviceName, check);

        for(FunctionMetadata meta : functions) {
            deleteFunction(serviceName, meta.getFunctionName(), check);
        }
    }

    private void cleanupService(String serviceName, final boolean check) throws Exception {
        cleanupFunctions(serviceName, check);
        deleteService(serviceName, check);
    }
}
