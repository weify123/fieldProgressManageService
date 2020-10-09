package com.autoyol.field.progress.manage.api;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.fallback.CommonApiFailBack;
import com.autoyol.field.progress.manage.request.dict.DictReqVo;
import com.autoyol.field.progress.manage.response.SysDictRespVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "fieldProgressManageService-service", fallback = CommonApiFailBack.class)
public interface CommonApi {

    @RequestMapping(value = "/console/common/findDictByName", method = RequestMethod.GET)
    ResponseData<List<SysDictRespVo>> findDictByName(@RequestBody DictReqVo reqVo);
}
