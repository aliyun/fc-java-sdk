package com.aliyuncs.fc.model;

public class VpcConfig {

    private String vpcId;

    private String[] vSwitchIds;

    private String securityGroupId;

    public VpcConfig(String vpcId, String[] vSwitchIds, String securityGroupId) {
        this.vpcId = vpcId;
        this.vSwitchIds = vSwitchIds;
        this.securityGroupId = securityGroupId;
    }

    public String getVpcId() {
        return vpcId;
    }

    public VpcConfig setVpcId(String vpcId) {
        this.vpcId = vpcId;
        return this;
    }

    public String[] getVSwitchIds() {
        return vSwitchIds;
    }

    public VpcConfig setVSwitchIds(String[] vSwitchIds) {
        this.vSwitchIds = vSwitchIds;
        return this;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public VpcConfig setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
        return this;
    }
}
