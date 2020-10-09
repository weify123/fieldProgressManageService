package com.autoyol.field.progress.manage.api;

import com.autoyol.commons.web.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 实例
 **/
@FeignClient(name="field")
public interface HelloFeignService {

    @GetMapping(value="/hello")
    ResponseData<String> hello(@RequestParam("name")String name);
}
