package com.aliyuncs.fc.auth;

import com.aliyuncs.fc.model.HttpMethod;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class SignURLConfigTest {
    @Test
    public void testSignURL() throws Exception {
        Date expires = new Date();
        expires.setTime((long) 1583844558 * 1000);
        SignURLConfig conf = new SignURLConfig(HttpMethod.POST, "service", "funcName", expires);
        conf.setQualifier("LATEST");
        conf.setEscapedPath("/test/path/" + AcsURLEncoder.percentEncode("中文"));

        // with header
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Test-Header-Key", "testHeaderValue");
        header.put("ConTENT-Type", "application/json");
        header.put("conTENT-md5", "dummy-md5");
        header.put("x-FC-TRACE-id", "trace-id");
        conf.setHeader(header);

        HashMap<String, String[]> queries = new HashMap<String, String[]>();
        queries.put("a", new String[]{"1"});
        queries.put("x", new String[]{"a", "b"});
        conf.setQueries(queries);

        {
            String url = conf.signURL("2016-08-15", "http://localhost",
                    "akID", "akSec", "stsToken");
            String expectedURL = "http://localhost/2016-08-15/proxy/service.LATEST/funcName/test/path/%E4%B8%AD%E6%96%87"
                    + "?a=1&x-fc-access-key-id=akID&x-fc-expires=1583844558&x-fc-security-token=stsToken"
                    + "&x-fc-signature=PoEmPVmMfNAhQGYUsQhDD%2Bqj%2Brv4YRCjybLgjtRMJ1U%3D&x=a&x=b";
            assertEquals(expectedURL, url);
        }

        // different signature with custom endpoint
        conf.setCustomEndpoint("http://custom.com");
        {
            String urlCustom = conf.signURL("2016-08-15", "http://localhost",
                    "akID", "akSec", "stsToken");
            String expectedURLCustom = "http://custom.com/test/path/%E4%B8%AD%E6%96%87"
                    + "?a=1&x-fc-access-key-id=akID&x-fc-expires=1583844558&x-fc-security-token=stsToken"
                    + "&x-fc-signature=q6nscsHhckwZh3oIa7fP%2BE3JlY8NBOjVHtGMJqvlfbA%3D&x=a&x=b";
            assertEquals(expectedURLCustom, urlCustom);
        }

        // make sure md5 has been signed before
        {
            header.remove("conTENT-md5");
            conf.setHeader(header);
            String urlCustom = conf.signURL("2016-08-15", "http://localhost",
                    "akID", "akSec", "stsToken");
            String expectedURLCustom = "http://custom.com/test/path/%E4%B8%AD%E6%96%87"
                    + "?a=1&x-fc-access-key-id=akID&x-fc-expires=1583844558&x-fc-security-token=stsToken"
                    + "&x-fc-signature=f%2BSz9uZUtt6bpYO1iuH2BpU26FWu%2FovTpbGBR0Tn%2B7s%3D&x=a&x=b";
            assertEquals(expectedURLCustom, urlCustom);
        }

        // make sure content type has been signed before
        {
            header.remove("conTENT-Type");
            conf.setHeader(header);
            String urlCustom = conf.signURL("2016-08-15", "http://localhost",
                    "akID", "akSec", "stsToken");
            String expectedURLCustom = "http://custom.com/test/path/%E4%B8%AD%E6%96%87"
                    + "?a=1&x-fc-access-key-id=akID&x-fc-expires=1583844558&x-fc-security-token=stsToken"
                    + "&x-fc-signature=f%2BSz9uZUtt6bpYO1iuH2BpU26FWu%2FovTpbGBR0Tn%2B7s%3D&x=a&x=b";
            assertEquals(expectedURLCustom, urlCustom);
        }

        // make sure x-fc-trace-id has been signed before
        {
            header.remove("x-FC-TRACE-id");
            conf.setHeader(header);
            String urlCustom = conf.signURL("2016-08-15", "http://localhost",
                    "akID", "akSec", "stsToken");
            String expectedURLCustom = "http://custom.com/test/path/%E4%B8%AD%E6%96%87"
                    + "?a=1&x-fc-access-key-id=akID&x-fc-expires=1583844558&x-fc-security-token=stsToken"
                    + "&x-fc-signature=IalihG51KQhTiwbwrBoFDodjTUTQv9AaAjILPbgoPEU%3D&x=a&x=b";
            assertEquals(expectedURLCustom, urlCustom);
        }
    }
}
