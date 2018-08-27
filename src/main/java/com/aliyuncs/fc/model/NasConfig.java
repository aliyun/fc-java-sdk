package com.aliyuncs.fc.model;

public class NasConfig {
    public static Integer AnyUserId = -1;
    public static Integer AnyGroupId = -1;

    private Integer userId;

    private Integer groupId;

    private NasMountConfig[] mountPoints;

    public NasConfig(Integer userId, Integer groupId, NasMountConfig[] mountPoints) {
        this.userId = userId;
        this.groupId = groupId;
        this.mountPoints = mountPoints;
    }

    public Integer getUserId() {
        return userId;
    }

    public NasConfig setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public NasConfig setGroupId(Integer groupId) {
        this.groupId = groupId;
        return this;
    }

    public NasMountConfig[] getMountPoints() {
        return mountPoints;
    }

    public NasConfig setMountPoints(NasMountConfig[] mountPoints) {
        this.mountPoints = mountPoints;
        return this;
    }

    public static class NasMountConfig {

        private String serverAddr;

        private String mountDir;

        public NasMountConfig(String serverAddr, String mountDir) {
            this.serverAddr = serverAddr;
            this.mountDir = mountDir;
        }

        public String getServerAddr() {
            return serverAddr;
        }

        public String getMountDir() {
            return mountDir;
        }
    }
}
