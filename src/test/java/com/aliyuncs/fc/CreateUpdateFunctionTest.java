package com.aliyuncs.fc;

import com.aliyuncs.fc.client.FunctionComputeClient;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ErrorCodes;
import com.aliyuncs.fc.model.*;
import com.aliyuncs.fc.request.*;
import com.aliyuncs.fc.response.CreateFunctionResponse;
import com.aliyuncs.fc.response.GetFunctionResponse;
import com.aliyuncs.fc.response.ListFunctionsResponse;
import com.aliyuncs.fc.response.UpdateFunctionResponse;
import com.google.common.base.Strings;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static junit.framework.TestCase.*;

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

    private static final String PY3_CODE = "def handler(evt, ctx):" +
            "    print(\"bye\")" +
            "    return \"done\"";

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
            cleanUpFunctions(SERVICE_NAME);
            cleanupService(SERVICE_NAME);
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

    public byte[] createZipCodePython(String indexFile, String filename) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        try {
            ZipEntry zipEntry = new ZipEntry(filename);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(indexFile.getBytes());
            zipOutputStream.closeEntry();
        } finally {
            zipOutputStream.close();
        }
        return byteArrayOutputStream.toByteArray();
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

    private boolean isEqualDNS(CustomDNS a, CustomDNS b) {
        if (!Arrays.equals(a.getNameServers(), b.getNameServers())) {
            return false;
        }
        if (!Arrays.equals(a.getSearches(), b.getSearches())) {
            return false;
        }

        boolean dnsOptionEq = true;
        DNSOption[] dnsOpts1 = a.getDnsOptions();
        DNSOption[] dnsOpts2 = b.getDnsOptions();
        if (dnsOpts1 == null && dnsOpts2 == null) {
            dnsOptionEq = true;
        } else if (dnsOpts1 == null ^ dnsOpts2 == null || dnsOpts1.length != dnsOpts2.length) {
            dnsOptionEq = false;
        } else {
            boolean eq = true;
            for (int i = 0; i < dnsOpts1.length && eq; i++) {
                eq = dnsOpts1[i].getName() == dnsOpts1[i].getName() && dnsOpts2[i].getValue() == dnsOpts2[i].getValue();
            }
            dnsOptionEq = dnsOptionEq && eq;
        }
        return dnsOptionEq;
    }

    private boolean isEqualCustomRuntimeConfig(CustomRuntimeConfig a, CustomRuntimeConfig b) {
        System.out.println(a);
        System.out.println(b);
        if (a == b) return true;
        if (a == null ^ b == null) return false;

        String[] command1 = a.getCommand();
        String[] command2 = b.getCommand();
        String[] arg1 = a.getArgs();
        String[] arg2 = b.getArgs();

        if (command1.length != command2.length || arg1.length != arg2.length) {
            return false;
        }

        return Arrays.equals(command1, command2) && Arrays.equals(arg1, arg2);
    }

    @Test
    public void testCreateUpdateFunctionCustomRuntimeConfig() {
        String functionName = "testCreateUpdateFunctionCustomRuntimeConfig";
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);
        createFuncReq.setFunctionName(functionName);
        createFuncReq.setHandler("index.handler");
        createFuncReq.setRuntime("custom");
        createFuncReq.setTimeout(5);
        createFuncReq.setMemorySize(256);

        CustomRuntimeConfig customRuntimeConfig = new CustomRuntimeConfig();
        customRuntimeConfig.setCommand(new String[]{
            "/code/bootcmd"
        });
        customRuntimeConfig.setArgs(new String[]{
            "1111", "3333", "4444", "2222"
        });
        createFuncReq.setCustomRuntimeConfig(customRuntimeConfig);

        // we don't really run function, it is ok to use arbitrary code file here
        try {
            createFuncReq.setCode(new Code().setZipFile(createZipCodePython(PY3_CODE, "bootcmd")));
        } catch (IOException e) {
            fail(e.getMessage());
        }

        // create function
        CreateFunctionResponse createResp = client.createFunction(createFuncReq);
        assertTrue(isEqualCustomRuntimeConfig(createResp.getCustomRuntimeConfig(), customRuntimeConfig));

        // get function
        GetFunctionRequest getFuncReq = new GetFunctionRequest(SERVICE_NAME, functionName);
        GetFunctionResponse getResp = client.getFunction(getFuncReq);
        assertTrue(isEqualCustomRuntimeConfig(getResp.getCustomRuntimeConfig(), customRuntimeConfig));

        // update function
        customRuntimeConfig.setArgs(new String[]{
                "1111", "3333"
        });
        UpdateFunctionRequest updateFuncReq = new UpdateFunctionRequest(SERVICE_NAME, functionName);
        updateFuncReq.setCustomRuntimeConfig(customRuntimeConfig);
        UpdateFunctionResponse updateResp = client.updateFunction(updateFuncReq);
        assertTrue(isEqualCustomRuntimeConfig(updateResp.getCustomRuntimeConfig(), customRuntimeConfig));
    }

    @Test
    public void testCreateUpdateFunctionCustomDNS() {
        String functionName = "testCreateUpdateFunctionCustomDNS";
        CreateFunctionRequest createFuncReq = new CreateFunctionRequest(SERVICE_NAME);
        createFuncReq.setFunctionName(functionName);
        createFuncReq.setHandler("index.handler");
        createFuncReq.setRuntime("python3");
        createFuncReq.setTimeout(5);
        createFuncReq.setMemorySize(256);

        CustomDNS customDNS = new CustomDNS();
        DNSOption dnsOption = new DNSOption();
        dnsOption.setName("ndots");
        dnsOption.setValue("2");
        DNSOption[] dnsOptions = new DNSOption[]{
                dnsOption
        };
        customDNS.setDnsOptions(dnsOptions);
        customDNS.setNameServers(new String[]{
                "8.8.8.8", "114.114.114.114"
        });
        customDNS.setSearches(new String[]{ "www.google.com" });
        try {
            createFuncReq.setCode(new Code().setZipFile(createZipCodePython(PY3_CODE, "index.py")));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        createFuncReq.setCustomDNS(customDNS);

        // create function
        CreateFunctionResponse createResp = client.createFunction(createFuncReq);
        assertTrue(isEqualDNS(createResp.getCustomDNS(), customDNS));

        // get function
        GetFunctionRequest getFuncReq = new GetFunctionRequest(SERVICE_NAME, functionName);
        GetFunctionResponse getResp = client.getFunction(getFuncReq);
        assertTrue(isEqualDNS(getResp.getCustomDNS(), customDNS));

        // update function
        customDNS.setNameServers(new String[]{
                "114.114.114.114", "8.8.8.8", "1.1.1.1"
        });
        UpdateFunctionRequest updateFuncReq = new UpdateFunctionRequest(SERVICE_NAME, functionName);
        updateFuncReq.setCustomDNS(customDNS);
        UpdateFunctionResponse updateResp = client.updateFunction(updateFuncReq);
        assertTrue(isEqualDNS(updateResp.getCustomDNS(), customDNS));
    }
}

