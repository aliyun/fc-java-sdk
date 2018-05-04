package com.aliyuncs.fc.auth;

import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;

public class AcsURLEncoderTest {

    @Test
    public void urlEncode() throws URISyntaxException {
        Assert.assertEquals("http://aliyun.com", AcsURLEncoder.urlEncode("http://aliyun.com"));

        Assert.assertEquals("http://aliyun.com/a", AcsURLEncoder.urlEncode("http://aliyun.com/a"));

        Assert.assertEquals("/a", AcsURLEncoder.urlEncode("/a"));

        Assert.assertEquals("a/%E4%B8%AD%E6%96%87", AcsURLEncoder.urlEncode("a/中文"));

        Assert.assertEquals("/%E4%B8%AD%E6%96%87", AcsURLEncoder.urlEncode("/中文"));

        Assert.assertEquals("/a%E4%B8%AD%E6%96%87/%E4%B8%AD%E6%96%87", AcsURLEncoder.urlEncode("/a中文/中文"));

        Assert.assertEquals("/a%E4%B8%AD%E6%96%87/%E4%B8%AD%E6%96%87?a=%E4%B8%AD&b=%E6%96%87", AcsURLEncoder.urlEncode("/a中文/中文?a=中&b=文"));
    }

    @Test(expected = URISyntaxException.class)
    public void testInvliadUrl() throws URISyntaxException {
        AcsURLEncoder.urlEncode("/a中文/中文?a=中&b=中 文");
    }
}