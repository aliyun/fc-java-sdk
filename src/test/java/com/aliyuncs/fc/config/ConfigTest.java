package com.aliyuncs.fc.config;

import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {

    @Test
    public void createConfigTest() {
        String region = "cn-hangzhou";
        String illegalRegion1 = "www.aliyun.com";
        String illegalRegion2 = "jiok@oskxlji(wes";

        String uid = "8927918269979";
        String illegalUid1 = "xaiy0x0z098.sxl;SOXZ";
        String illegalUid2 = "si@l8s6X)LOks.Ilihs";

        crateConfig(region, uid, null);
        crateConfig(region, illegalUid1, "Illegal Account ID");
        crateConfig(region, illegalUid2, "Illegal Account ID");
        crateConfig(illegalRegion1, uid, "Illegal region");
        crateConfig(illegalRegion2, uid, "Illegal region");
    }

    private Config crateConfig(String region, String uid, String errMsg) {
        String accessKeyID = "TMPkeyxxxxxx";
        String accessKeySecret = "TMPSecretxxxxxx";
        String securityToken = "TMPtokenxxxx";
        boolean isHttps = false;

        try {
            return new Config(region, uid, accessKeyID, accessKeySecret, securityToken, isHttps);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), errMsg);
            return null;
        }
    }
}
