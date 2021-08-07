package com.iray.irs_vms.info;

public class NewAlarmInfo {

    /**
     * 事件编号
     * 事件类型码
     * 事件时间
     * 事件级别
     * 设备编号
     * 设备类型
     * 设备名称
     * 设备通道
     * 事件数据
     */
    private  String eventId;  //事件编号
    private  String eventType;  //事件类型码
    private  String eventTime;  //事件时间
    private  String eventLevel;   //事件级别
    private  String devId;   //设备编号
    private String devType;  //设备类型
    private String devName;   //设备名称
    private String devChannel;  //设备通道
    private Object data;   //事件数据

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevChannel() {
        return devChannel;
    }

    public void setDevChannel(String devChannel) {
        this.devChannel = devChannel;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
