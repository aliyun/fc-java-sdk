package com.aliyuncs.fc.auth;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.aliyuncs.fc.model.HttpMethod.GET;
import static org.junit.Assert.*;

public class FcSignatureComposerTest {

    @Test
    public void testGetHeaderWithXFcDate() {
        Map<String, String> headers = new HashMap();
        headers.put("Content-MD5", "1bca714f406993b309bb87fabeb30a6b");
        headers.put("Content-Type", "application/json");
        headers.put("Date", "today");
        headers.put("x-fc-h4", "k4");
        headers.put("x-fc-date", "xxxx");

        Map<String, String> composed = FcSignatureComposer.refreshSignParameters(headers);

        assertEquals("xxxx", composed.get("x-fc-date"));
        assertEquals("xxxx", composed.get("Date"));
    }

    @Test
    public void testComposeWithoutParameters() {
        Map<String, String> headers = new HashMap();
        headers.put("Content-MD5", "1bca714f406993b309bb87fabeb30a6b");
        headers.put("Content-Type", "application/json");
        headers.put("Date", "today");
        headers.put("X-FC-H6", "k6");
        headers.put("x-fc-h2", "k2");
        headers.put("X-Fc-h1", "k1");
        headers.put("x-fc-h4", "k4");
        headers.put("h3", "k3");
        headers.put("x-fc-h5", "k5");

        String composed = FcSignatureComposer.composeStringToSign(GET, "aa", headers, null);

        assertEquals("GET\n" +
                "1bca714f406993b309bb87fabeb30a6b\n" +
                "application/json\n" +
                "today\n" +
                "x-fc-h1:k1\n" +
                "x-fc-h2:k2\n" +
                "x-fc-h4:k4\n" +
                "x-fc-h5:k5\n" +
                "x-fc-h6:k6\n" +
                "aa", composed);
    }

    @Test
    public void testComposeWithMultiValue() {
        Map<String, String> headers = new HashMap();
        headers.put("Content-MD5", "1bca714f406993b309bb87fabeb30a6b");
        headers.put("Content-Type", "application/json");
        headers.put("Date", "today");
        headers.put("X-FC-H6", "k6");
        headers.put("x-fc-h2", "k2");
        headers.put("X-Fc-h1", "k1");
        headers.put("x-fc-h4", "k4");
        headers.put("h3", "k3");
        headers.put("x-fc-h5", "k5");

        Map<String, String[]> queries = new HashMap<String, String[]>();
        queries.put("h6", new String[] {"k6"});
        queries.put("h2", new String[] {"k2"});
        queries.put("h1", new String[] {"k4", "k1"});
        queries.put("h4", new String[] {"k44", "k4", "k4"});
        queries.put("h3", new String[] {"k3"});
        queries.put("h5", new String[] {"k5"});

        String composed = FcSignatureComposer.composeStringToSignWithMultiValue(GET, "aa", headers, queries);

        assertEquals("GET\n" +
                "1bca714f406993b309bb87fabeb30a6b\n" +
                "application/json\n" +
                "today\n" +
                "x-fc-h1:k1\n" +
                "x-fc-h2:k2\n" +
                "x-fc-h4:k4\n" +
                "x-fc-h5:k5\n" +
                "x-fc-h6:k6\n" +
                "aa\n" +
                "h1=k1\n" +
                "h1=k4\n" +
                "h2=k2\n" +
                "h3=k3\n" +
                "h4=k4\n" +
                "h4=k4\n" +
                "h4=k44\n" +
                "h5=k5\n" +
                "h6=k6", composed);
    }

    @Test
    public void testComposeWithMultiValue2() {

        Map<String, String[]> queries = new HashMap<String, String[]>();
        queries.put("xyz", new String[] {});
        queries.put("foo", new String[] {"bar"});
        queries.put("key2", new String[] {"123"});
        queries.put("key1", new String[] {"xyz", "abc"});
        queries.put("key3/~x-y_z.a#b", new String[] {"value/~x-y_z.a#b"});

        String composed = FcSignatureComposer.composeStringToSignWithMultiValue(GET, "/path/action with space", null, queries);

        assertEquals("GET\n" +
                "\n" +
                "\n" +
                "\n" +
                "/path/action with space\n" +
                "foo=bar\n" +
                "key1=abc\n" +
                "key1=xyz\n" +
                "key2=123\n" +
                "key3/~x-y_z.a#b=value/~x-y_z.a#b\n" +
                "xyz", composed);
    }

    @Test
    public void testComposeWithParameters() {
        Map<String, String> headers = new HashMap();
        headers.put("Content-MD5", "1bca714f406993b309bb87fabeb30a6b");
        headers.put("Content-Type", "application/json");
        headers.put("Date", "today");
        headers.put("X-FC-H6", "k6");
        headers.put("x-fc-h2", "k2");
        headers.put("X-Fc-h1", "k1");
        headers.put("x-fc-h4", "k4");
        headers.put("h3", "k3");
        headers.put("x-fc-h5", "k5");

        Map<String, String> queries = new HashMap<String, String>();
        queries.put("h6", "k6");
        queries.put("h2", "k2");
        queries.put("h1", "k1");
        queries.put("h4", "k4");
        queries.put("h3", "k3");
        queries.put("h5", "k5");

        String composed = FcSignatureComposer.composeStringToSign(GET, "aa", headers, queries);

        assertEquals("GET\n" +
                "1bca714f406993b309bb87fabeb30a6b\n" +
                "application/json\n" +
                "today\n" +
                "x-fc-h1:k1\n" +
                "x-fc-h2:k2\n" +
                "x-fc-h4:k4\n" +
                "x-fc-h5:k5\n" +
                "x-fc-h6:k6\n" +
                "aa\n" +
                "h1=k1\n" +
                "h2=k2\n" +
                "h3=k3\n" +
                "h4=k4\n" +
                "h5=k5\n" +
                "h6=k6", composed);

    }
}