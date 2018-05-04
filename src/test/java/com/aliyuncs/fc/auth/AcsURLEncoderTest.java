package com.aliyuncs.fc.auth;

import com.google.common.net.UrlEscapers;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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

        Assert.assertEquals("/a%E4%B8%AD%20%E6%96%87/%E4%B8%AD%20%E6%96%87?a=%E4%B8%AD&b=%E6%96%87", AcsURLEncoder.urlEncode("/a中 文/中 文?a=中&b=文"));
    }
}