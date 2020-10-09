package com.autoyol.field.progress.manage.fallback;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.api.CityLevelApi;
import com.autoyol.field.progress.manage.request.city.CityReqVo;
import com.autoyol.field.progress.manage.response.city.CityLevelAllRespVo;
import com.autoyol.field.progress.manage.response.city.CityLevelPageRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CityLevelApiFailBack implements CityLevelApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseData<CityLevelPageRespVo> findCityByCond(CityReqVo reqVo) {
        logger.info("call fieldProgressManageService.findCityByCond fallback param :reqVO:[{}]", reqVo);
        return ResponseData.error();
    }

    @Override
    public ResponseData<CityLevelAllRespVo> findAllCity() {
        logger.info("call fieldProgressManageService.findAllCity fallback");
        return ResponseData.error();
    }
}
