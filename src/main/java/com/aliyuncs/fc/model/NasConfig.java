package com.aliyuncs.fc.model;

public class NasConfig {

    private int userId;

    private int groupId;

    private NASMountConfig[] mountPoints;

    public NasConfig(int userId, int groupId, NASMountConfig[] mountPoints) {
        this.userId = userId;
        this.groupId = groupId;
        this.mountPoints = mountPoints;
    }

    public int getUserId() {
        return userId;
    }

    public NasConfig setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getGroupId() {
        return groupId;
    }

    public NasConfig setGroupId(int groupId) {
        this.groupId = groupId;
        return this;
    }

    public NASMountConfig[] getMountPoints() {
        return mountPoints;
    }

    public NasConfig setMountPoints(NASMountConfig[] mountPoints) {
        this.mountPoints = mountPoints;
        return this;
    }

    public static class NASMountConfig {

        private String serverAddr;

        private String mountDir;

        public NASMountConfig(String serverAddr, String mountDir) {
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
