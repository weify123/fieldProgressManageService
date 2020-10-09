package com.autoyol.field.progress.manage.fallback;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.api.CommonApi;
import com.autoyol.field.progress.manage.request.dict.DictReqVo;
import com.autoyol.field.progress.manage.response.SysDictRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommonApiFailBack implements CommonApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseData<List<SysDictRespVo>> findDictByName(DictReqVo reqVo) {
        logger.info("call fieldProgressManageService.findDictByName fallback param :reqVO:[{}]", reqVo);
        return ResponseData.error();
    }
}
