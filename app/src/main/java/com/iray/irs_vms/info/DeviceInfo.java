package com.iray.irs_vms.info;

public class DeviceInfo {
    private String deviceOrg;
    private String deviceName;
    private int deviceTransport;
    private int deviceType;
    private boolean deviceOnline;


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
