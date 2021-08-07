package com.iray.irs_vms.info;

public class DeviceInfo {
    private String deviceId;
    private String deviceOrg;//区域
    private String deviceName; //名称
    private int deviceTransport;
    private int deviceType;  //型号
    private boolean deviceOnline;
    private String alias;
    private String comment;
    private String deviceModel;
    private String protocolDeviceId;
    private String physicId;
    private String organizationName;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPhysicId() {
        return physicId;
    }

    public void setPhysicId(String physicId) {
        this.physicId = physicId;
    }

    public String getProtocolDeviceId() {
        return protocolDeviceId;
    }

    public void setProtocolDeviceId(String protocolDeviceId) {
        this.protocolDeviceId = protocolDeviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceOrg() {
        return deviceOrg;
    }

    public void setDeviceOrg(String deviceOrg) {
        this.deviceOrg = deviceOrg;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceTransport() {
        return deviceTransport;
    }

    public void setDeviceTransport(int deviceTransport) {
        this.deviceTransport = deviceTransport;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isDeviceOnline() {
        return deviceOnline;
    }

    public void setDeviceOnline(boolean deviceOnline) {
        this.deviceOnline = deviceOnline;
    }
}
