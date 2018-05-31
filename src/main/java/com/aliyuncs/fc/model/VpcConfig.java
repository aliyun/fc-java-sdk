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

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getVpcId() {
        return vpcId;
    }

    public String[] getVSwitchIds() {
        return vSwitchIds;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setVSwitchIds(String[] vSwitchIds) {
        this.vSwitchIds = vSwitchIds;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }
}
