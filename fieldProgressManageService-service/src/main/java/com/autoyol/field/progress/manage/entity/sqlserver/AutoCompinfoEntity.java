package com.autoyol.field.progress.manage.entity.sqlserver;

import java.util.Date;

/**
 * null
 *
 * @author feiyu.wei
 * @date 2020/04/15
 */
public class AutoCompinfoEntity {
    /**
     * null
     */
    private Long autoCpinfoId;

    /**
     * null
     */
    private Integer pubEdiflag;

    /**
     * null
     */
    private Date pubCrttime;

    /**
     * null
     */
    private Long pubCrtusid;

    /**
     * null
     */
    private Date pubFupdtime;

    /**
     * null
     */
    private Long pubFupdusid;

    /**
     * null
     */
    private Long pubCompid;

    /**
     * null
     */
    private Integer pubImgflg;

    /**
     * null
     */
    private String autoCpinfoComp1;

    /**
     * null
     */
    private String autoCpinfoComp2;

    /**
     * null
     */
    private String autoCpinfoValid;

    /**
     * null
     */
    private String autoCpinfoEmail;

    public Long getAutoCpinfoId() {
        return autoCpinfoId;
    }

    public void setAutoCpinfoId(Long autoCpinfoId) {
        this.autoCpinfoId = autoCpinfoId;
    }

    public Integer getPubEdiflag() {
        return pubEdiflag;
    }

    public void setPubEdiflag(Integer pubEdiflag) {
        this.pubEdiflag = pubEdiflag;
    }

    public Date getPubCrttime() {
        return pubCrttime;
    }

    public void setPubCrttime(Date pubCrttime) {
        this.pubCrttime = pubCrttime;
    }

    public Long getPubCrtusid() {
        return pubCrtusid;
    }

    public void setPubCrtusid(Long pubCrtusid) {
        this.pubCrtusid = pubCrtusid;
    }

    public Date getPubFupdtime() {
        return pubFupdtime;
    }

    public void setPubFupdtime(Date pubFupdtime) {
        this.pubFupdtime = pubFupdtime;
    }

    public Long getPubFupdusid() {
        return pubFupdusid;
    }

    public void setPubFupdusid(Long pubFupdusid) {
        this.pubFupdusid = pubFupdusid;
    }

    public Long getPubCompid() {
        return pubCompid;
    }

    public void setPubCompid(Long pubCompid) {
        this.pubCompid = pubCompid;
    }

    public Integer getPubImgflg() {
        return pubImgflg;
    }

    public void setPubImgflg(Integer pubImgflg) {
        this.pubImgflg = pubImgflg;
    }

    public String getAutoCpinfoComp1() {
        return autoCpinfoComp1;
    }

    public void setAutoCpinfoComp1(String autoCpinfoComp1) {
        this.autoCpinfoComp1 = autoCpinfoComp1 == null ? null : autoCpinfoComp1.trim();
    }

    public String getAutoCpinfoComp2() {
        return autoCpinfoComp2;
    }

    public void setAutoCpinfoComp2(String autoCpinfoComp2) {
        this.autoCpinfoComp2 = autoCpinfoComp2 == null ? null : autoCpinfoComp2.trim();
    }

    public String getAutoCpinfoValid() {
        return autoCpinfoValid;
    }

    public void setAutoCpinfoValid(String autoCpinfoValid) {
        this.autoCpinfoValid = autoCpinfoValid == null ? null : autoCpinfoValid.trim();
    }

    public String getAutoCpinfoEmail() {
        return autoCpinfoEmail;
    }

    public void setAutoCpinfoEmail(String autoCpinfoEmail) {
        this.autoCpinfoEmail = autoCpinfoEmail == null ? null : autoCpinfoEmail.trim();
    }
}