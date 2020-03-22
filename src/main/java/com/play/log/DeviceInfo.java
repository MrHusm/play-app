package com.play.log;

import java.io.Serializable;

/**
 * Created by Loy on 15-11-3.
 */
public class DeviceInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    private String cnId;
    private String cnName;
    private String imei;
    private String imsi;
    private String version;
    private String verCode;
    private String oscode;
    private String model;
    private String brand;
    private String mac;
    private String appname;
    private String platform;
    private String pkgName;
    private String uid = "0";
    private int flag = 0; //0,normal, 非0,dirty data
    public void setCnId(String cnId)
    {
        this.cnId = cnId;
    }
    public String getCnId()
    {
        return cnId;
    }
    public void setCnName(String cnName)
    {
        this.cnName = cnName;
    }
    public String getCnName()
    {
        return cnName;
    }

    public String getAppname() {
        return appname;
    }

    public String getBrand() {
        return brand;
    }

    public String getImei() {
        return imei;
    }

    public String getImsi() {
        return imsi;
    }

    public String getMac() {
        return mac;
    }

    public String getModel() {
        return model;
    }

    public String getOscode() {
        return oscode;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getPlatform() {
        return platform;
    }

    public String getVerCode() {
        return verCode;
    }

    public String getVersion() {
        return version;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setOscode(String oscode) {
        this.oscode = oscode;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }
    @Override
    public boolean equals(Object o) {
        if(o == this)
        {
            return true;
        }
        if(o != null && o instanceof DeviceInfo)
        {
            DeviceInfo info = (DeviceInfo) o;
            return imei != null && imei.equals(info.imei);
        }
        return super.equals(o);
    }
    public void setDirtyFlag()
    {
        flag = 1;
    }
}
