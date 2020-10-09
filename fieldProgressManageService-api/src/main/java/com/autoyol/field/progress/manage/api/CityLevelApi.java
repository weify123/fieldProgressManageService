package com.autoyol.field.progress.manage.api;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.fallback.CityLevelApiFailBack;
import com.autoyol.field.progress.manage.request.city.CityReqVo;
import com.autoyol.field.progress.manage.response.city.CityLevelAllRespVo;
import com.autoyol.field.progress.manage.response.city.CityLevelPageRespVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "fieldProgressManageService-service", fallback = CityLevelApiFailBack.class)
public interface CityLevelApi {

    @RequestMapping(value = "/console/common/findCityByCond", method = RequestMethod.GET)
    ResponseData<CityLevelPageRespVo> findCityByCond(@RequestBody CityReqVo reqVo);

    @RequestMapping(value = "/console/city/findAllCity", method = RequestMethod.GET)
    ResponseData<CityLevelAllRespVo> findAllCity();
}
