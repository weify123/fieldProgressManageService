package com.autoyol.field.progress.manage.response;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DictMulRespVo implements Serializable {
    private static final long serialVersionUID = -6062018503747675464L;

    @AutoDocProperty(value = "多个字典列表信息")
    private Map<String, List<SysDictRespVo>> dictMap;

    public Map<String, List<SysDictRespVo>> getDictMap() {
        return dictMap;
    }

    public void setDictMap(Map<String, List<SysDictRespVo>> dictMap) {
        this.dictMap = dictMap;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
