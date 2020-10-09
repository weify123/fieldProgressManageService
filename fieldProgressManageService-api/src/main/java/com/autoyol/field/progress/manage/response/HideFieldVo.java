package com.autoyol.field.progress.manage.response;

import com.autoyol.doc.annotation.AutoDocProperty;

import java.io.Serializable;
import java.util.List;

public class HideFieldVo implements Serializable {
    private static final long serialVersionUID = -58405947883454363L;

    @AutoDocProperty(value = "模块")
    private String moduleType;

    @AutoDocProperty(value = "隐藏字段")
    private List<String> list;

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
