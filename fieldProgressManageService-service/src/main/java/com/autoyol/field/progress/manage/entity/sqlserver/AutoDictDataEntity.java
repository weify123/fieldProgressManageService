package com.autoyol.field.progress.manage.entity.sqlserver;

import java.util.List;

public class AutoDictDataEntity {

    private Long id;

    private String dtpCode;

    private String ddtCode;

    private String code;

    private String name;

    private List<String> nameList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDtpCode() {
        return dtpCode;
    }

    public void setDtpCode(String dtpCode) {
        this.dtpCode = dtpCode;
    }

    public String getDdtCode() {
        return ddtCode;
    }

    public void setDdtCode(String ddtCode) {
        this.ddtCode = ddtCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }
}
