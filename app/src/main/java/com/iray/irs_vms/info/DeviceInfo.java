package com.iray.irs_vms.info;

public class DeviceInfo {
    private String deviceOrg;
    private String deviceName;
    private int deviceTransport;
    private String deviceType;


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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
