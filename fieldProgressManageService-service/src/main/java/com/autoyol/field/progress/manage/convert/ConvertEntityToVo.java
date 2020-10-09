package com.autoyol.field.progress.manage.convert;

import com.autoyol.field.progress.manage.entity.AttrLabelEntity;
import com.autoyol.field.progress.manage.response.AttrFeeVo;

public class ConvertEntityToVo {

    public static AttrFeeVo convert(AttrLabelEntity entity){
        AttrFeeVo attrFeeVo = new AttrFeeVo();
        attrFeeVo.setAttrCode(entity.getAttrCode());
        attrFeeVo.setAttrName(entity.getAttrName());
        return attrFeeVo;
    }
}
