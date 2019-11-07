package com.aliyuncs.fc.client;

import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.utils.FcUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DefaultFcClientTest {

    @Test
    public void testGetHeaderWithoutMd5() {
        Config config = new Config("cn-shanghai", "123", "123", "123", "123", false);

        DefaultFcClient fcClient = new DefaultFcClient(config);

        Map<String, String> headers = new HashMap<String, String>();

        headers.put("x-fc-date", "x-fc-date-value");

        headers = FcUtil.getHeader(config, headers, "123".getBytes(), null);

        assertEquals(headers.get("x-fc-date"), "x-fc-date-value");
        assertEquals(headers.get("Content-MD5"), null);

        headers.remove("x-fc-date");

        headers = FcUtil.getHeader(config, headers, "123".getBytes(), null);

        assertEquals(headers.get("Content-MD5"), "ICy5YqxZB1uWSwcVLSNLcA==");
    }
}