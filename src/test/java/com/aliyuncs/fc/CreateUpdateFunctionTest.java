package com.aliyuncs.fc;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import com.aliyuncs.fc.client.FunctionComputeClient;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ErrorCodes;
import com.aliyuncs.fc.model.CustomContainerConfig;
import com.aliyuncs.fc.model.FunctionMetadata;
import com.aliyuncs.fc.request.CreateFunctionRequest;
import com.aliyuncs.fc.request.CreateServiceRequest;
import com.aliyuncs.fc.request.DeleteFunctionRequest;
import com.aliyuncs.fc.request.DeleteServiceRequest;
import com.aliyuncs.fc.request.GetServiceRequest;
import com.aliyuncs.fc.request.ListFunctionsRequest;
import com.aliyuncs.fc.request.UpdateFunctionRequest;
import com.aliyuncs.fc.response.CreateFunctionResponse;
import com.aliyuncs.fc.response.ListFunctionsResponse;
import com.aliyuncs.fc.response.UpdateFunctionResponse;
import com.google.common.base.Strings;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Test;

/**
 * Create and update function related tests
 */
public class CreateUpdateFunctionTest {
    private static final String REGION = System.getenv("REGION_IMAGE_ACCL");
    private static final String ENDPOINT = System.getenv("TEST_ENDPOINT");
    private static final String ROLE = System.getenv("TEST_SERVICE_ROLE");
    private static final String ACCESS_KEY = System.getenv("TEST_ACCESS_KEY");
    private static final String SECRET_KEY = System.getenv("TEST_SECRET_KEY");
    private static final String ACCOUNT_ID = System.getenv("TEST_ACCOUNT_ID");
    private static final String CUSTOM_CONTAINER_IMAGE = System.getenv("TEST_CUSTOM_CONTAINER_IMAGE");
    private static final String SERVICE_NAME = "fcJavaSdkCITest";
    private static final String FUNCTION_DESC_OLD = "function desc";

    private FunctionComputeClient client;

    @BeforeClass
    public static void setupSuite() {
        System.out.println("ENDPOINT: " + ENDPOINT);
        System.out.println("ROLE: " + ROLE);
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
            // Clean up first
            client.getService(getSReq);
            cleanUpFunctions(SERVICE_NAME);
            System.out.println("Cleaned up functions in " + SERVICE_NAME);
            cleanupService(SERVICE_NAME);
            System.out.println("Cleaned up service " + SERVICE_NAME);
        } catch (ClientException e) {
            if (!ErrorCodes.SERVICE_NOT_FOUND.equals(e.getErrorCode())) {
                e.printStackTrace();
                throw e;
            }
        }

        try {
            // Create a new service
            CreateServiceRequest createSReq = new CreateServiceRequest();
            createSReq.setServiceName(SERVICE_NAME);
            createSReq.setDescription("FC Java SDK CI Test");
            createSReq.setRole(ROLE);
            client.createService(createSReq);
            System.out.println("Created service " + SERVICE_NAME);
        } catch (ClientException e) {
            e.printStackTrace();
                throw e;
        }
    }

    @After
    public void cleanup() {
        try {
            // cleanUpFunctions(SERVICE_NAME);
            // cleanupService(SERVICE_NAME);
        } catch (ClientException e) {
            if (!ErrorCodes.SERVICE_NOT_FOUND.equals(e.getErrorCode())) {
                throw e;
            }
        }
    }

    private void cleanupService(String serviceName) {
        DeleteServiceRequest request = new DeleteServiceRequest(serviceName);
        try {
            client.deleteService(request);
            System.out.println("Service " + serviceName + " is deleted");
        } catch (ClientException e) {
            if (!ErrorCodes.SERVICE_NOT_FOUND.equals(e.getErrorCode())) {
                throw e;
            }
        }
    }

    private void cleanUpFunctions(String serviceName) {
        ListFunctionsRequest listFReq = new ListFunctionsRequest(serviceName);
        ListFunctionsResponse listFResp = client.listFunctions(listFReq);
        FunctionMetadata[] functions = listFResp.getFunctions();

        for (FunctionMetadata function : functions) {
            DeleteFunctionRequest deleteFReq = new DeleteFunctionRequest(serviceName, function.getFunctionName());
            client.deleteFunction(deleteFReq);
            System.out.println("Function " + serviceName + "/" + function.getFunctionName() + " is deleted");
        }
    }

    @Test
    public void testCreateUpdateFunctionCustomContainer() {
        String functionName = "testFCJavaSdkCITestFunction";
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);
        createFuncReq.setFunctionName(functionName);
        createFuncReq.setDescription(FUNCTION_DESC_OLD);
        createFuncReq.setHandler("not-used");
        createFuncReq.setRuntime("custom-container");

        // Create with AccelerationTypeNone as Default
        CustomContainerConfig ccc = new CustomContainerConfig();
        ccc.setImage(CUSTOM_CONTAINER_IMAGE);
        ccc.setAccelerationType(CustomContainerConfig.AccelerationTypeDefault);
        createFuncReq.setCustomContainerConfig(ccc);
        createFuncReq.setTimeout(60);
        createFuncReq.setMemorySize(1024);
        createFuncReq.setCAPort(8080);
        CreateFunctionResponse response = client.createFunction(createFuncReq);
        assertFalse(Strings.isNullOrEmpty(response.getRequestId()));
        assertFalse(Strings.isNullOrEmpty(response.getFunctionId()));
        assertEquals(functionName, response.getFunctionName());
        assertEquals(FUNCTION_DESC_OLD, response.getDescription());
        CustomContainerConfig respCCC = response.getCustomContainerConfig();
        assertEquals(CUSTOM_CONTAINER_IMAGE, respCCC.getImage());
        assertTrue(Strings.isNullOrEmpty(respCCC.getCommand()));
        assertTrue(Strings.isNullOrEmpty(respCCC.getArgs()));
        assertEquals(functionName, response.getFunctionName());

        // Update AccelerationTypeNone to None
        UpdateFunctionRequest updateFuncReq = new UpdateFunctionRequest(SERVICE_NAME, functionName);
        CustomContainerConfig cccU = new CustomContainerConfig();
        cccU.setImage(CUSTOM_CONTAINER_IMAGE);
        cccU.setAccelerationType(CustomContainerConfig.AccelerationTypeNone);
        updateFuncReq.setCustomContainerConfig(cccU);
        updateFuncReq.setDescription("new desc 1");
        UpdateFunctionResponse responseU = client.updateFunction(updateFuncReq);
        assertFalse(Strings.isNullOrEmpty(responseU.getRequestId()));
        assertFalse(Strings.isNullOrEmpty(responseU.getFunctionId()));
        assertEquals(functionName, responseU.getFunctionName());
        assertEquals("new desc 1", responseU.getDescription());
        CustomContainerConfig respUCCC = responseU.getCustomContainerConfig();
        assertEquals(CUSTOM_CONTAINER_IMAGE, respUCCC.getImage());
        assertTrue(Strings.isNullOrEmpty(respUCCC.getCommand()));
        assertTrue(Strings.isNullOrEmpty(respUCCC.getArgs()));
        assertEquals(functionName, responseU.getFunctionName());

        // Update AccelerationTypeNone back to Default
        UpdateFunctionRequest updateFuncReq1 = new UpdateFunctionRequest(SERVICE_NAME, functionName);
        CustomContainerConfig cccU1 = new CustomContainerConfig();
        cccU1.setImage(CUSTOM_CONTAINER_IMAGE);
        cccU1.setAccelerationType(CustomContainerConfig.AccelerationTypeDefault);
        updateFuncReq1.setCustomContainerConfig(cccU1);
        updateFuncReq1.setDescription("new desc 2");
        UpdateFunctionResponse responseU1 = client.updateFunction(updateFuncReq1);
        assertFalse(Strings.isNullOrEmpty(responseU1.getRequestId()));
        assertFalse(Strings.isNullOrEmpty(responseU1.getFunctionId()));
        assertEquals(functionName, responseU1.getFunctionName());
        assertEquals("new desc 2", responseU1.getDescription());
        CustomContainerConfig respUCCC1 = responseU.getCustomContainerConfig();
        assertEquals(CUSTOM_CONTAINER_IMAGE, respUCCC1.getImage());
        assertTrue(Strings.isNullOrEmpty(respUCCC1.getCommand()));
        assertTrue(Strings.isNullOrEmpty(respUCCC1.getArgs()));
        assertEquals(functionName, responseU1.getFunctionName());
    }
}

