package com.aliyuncs.fc.request;

import com.aliyuncs.fc.model.HttpAuthType;
import org.junit.Test;

import static com.aliyuncs.fc.model.HttpAuthType.ANONYMOUS;
import static java.lang.String.format;
import static org.junit.Assert.*;

public class HttpInvokeFunctionRequestTest {

    private static final String SERVICE_NAME = "service";

    private static final String FUNCTION_NAME = "function";

    @Test
    public void testGetPath() {
        HttpInvokeFunctionRequest request = createHttpInvoke("a");

        assertEquals(generatePath("/a"), request.getPath());

        request = createHttpInvoke("/a/b/c");

        assertEquals(generatePath("/a/b/c"), request.getPath());

        request = createHttpInvoke("/a/b/c?a=1&b=2");

        assertEquals(generatePath("/a/b/c"), request.getPath());

        request = createHttpInvoke("/a/b/c?a");

        assertEquals(generatePath("/a/b/c"), request.getPath());
    }

    @Test
    public void testGetParamters() {
        HttpInvokeFunctionRequest request = createHttpInvoke("a");

        assertEquals(0, request.getQueryParams().size());

        request = createHttpInvoke("/a/b/c");

        assertEquals(0, request.getQueryParams().size());

        request = createHttpInvoke("/a/b/c?a=1&b=2");

        assertEquals(2, request.getQueryParams().size());
        assertEquals("1", request.getQueryParams().get("a"));
        assertEquals("2", request.getQueryParams().get("b"));

        request = createHttpInvoke("/a/b/c?a");

        assertEquals(1, request.getQueryParams().size());
        assertEquals(null, request.getQueryParams().get("a"));

        request.addQuery("a", "1");
        assertEquals("1", request.getQueryParams().get("a"));
    }

    private static HttpInvokeFunctionRequest createHttpInvoke(String path) {
        return new HttpInvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME, ANONYMOUS, "GET", path);
    }

    private static String generatePath(String path) {
        return format("/2016-08-15/proxy/service/function%s", path);
    }

}