package com.autoyol.field.progress.manage.entity.sqlserver;

import java.util.Date;

/**
 * null
 *
 * @author feiyu.wei
 * @date 2020/04/14
 */
public class AutoOrdtraposinfoEntity {
    /**
     * null
     */
    private Long autoOtpId;

    /**
     * null
     */
    private Long autoOtpFldtid;

    /**
     * null
     */
    private String autoOtpLon;

    /**
     * null
     */
    private String autoOtpLat;

    /**
     * null
     */
    private String autoOtpAppstep;

    /**
     * null
     */
    private Date autoOtpApptime;

    /**
     * null
     */
    private String autoOtpAddr;

    /**
     * null
     */
    private String autoOtpDistancepoor;

    /**
     * null
     */
    private Integer pubEditflag;

    public Long getAutoOtpId() {
        return autoOtpId;
    }

    public void setAutoOtpId(Long autoOtpId) {
        this.autoOtpId = autoOtpId;
    }

    public Long getAutoOtpFldtid() {
        return autoOtpFldtid;
    }

    public void setAutoOtpFldtid(Long autoOtpFldtid) {
        this.autoOtpFldtid = autoOtpFldtid;
    }

    public String getAutoOtpLon() {
        return autoOtpLon;
    }

    public void setAutoOtpLon(String autoOtpLon) {
        this.autoOtpLon = autoOtpLon == null ? null : autoOtpLon.trim();
    }

    public String getAutoOtpLat() {
        return autoOtpLat;
    }

    public void setAutoOtpLat(String autoOtpLat) {
        this.autoOtpLat = autoOtpLat == null ? null : autoOtpLat.trim();
    }

    public String getAutoOtpAppstep() {
        return autoOtpAppstep;
    }

    public void setAutoOtpAppstep(String autoOtpAppstep) {
        this.autoOtpAppstep = autoOtpAppstep == null ? null : autoOtpAppstep.trim();
    }

    public Date getAutoOtpApptime() {
        return autoOtpApptime;
    }

    public void setAutoOtpApptime(Date autoOtpApptime) {
        this.autoOtpApptime = autoOtpApptime;
    }

    public String getAutoOtpAddr() {
        return autoOtpAddr;
    }

    public void setAutoOtpAddr(String autoOtpAddr) {
        this.autoOtpAddr = autoOtpAddr == null ? null : autoOtpAddr.trim();
    }

    public String getAutoOtpDistancepoor() {
        return autoOtpDistancepoor;
    }

    public void setAutoOtpDistancepoor(String autoOtpDistancepoor) {
        this.autoOtpDistancepoor = autoOtpDistancepoor == null ? null : autoOtpDistancepoor.trim();
    }

    public Integer getPubEditflag() {
        return pubEditflag;
    }

    public void setPubEditflag(Integer pubEditflag) {
        this.pubEditflag = pubEditflag;
    }
}