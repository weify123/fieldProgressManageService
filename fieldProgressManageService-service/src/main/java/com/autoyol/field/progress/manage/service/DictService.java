package com.autoyol.field.progress.manage.service;


import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.request.dict.DictReqVo;
import com.autoyol.field.progress.manage.response.SysDictRespVo;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class DictService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    DictCache dictCache;

    public Map<String, List<SysDictRespVo>> findDictByName(DictReqVo reqVo) throws Exception {
        Map<String, List<SysDictRespVo>> map = Maps.newHashMap();
        if (hitSearch(SPLIT_COMMA, reqVo.getTypeNameStr(), String::contains)) {
            List<String> typeList = getList(reqVo.getTypeNameStr(), SPLIT_COMMA, Function.identity());
            Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);
            Optional.ofNullable(dictMap).ifPresent(listMap -> listMap.forEach((k, list) -> {
                map.put(k, convertList(list));
            }));
            return map;
        }
        List<SysDictEntity> list = dictCache.getDictByTypeNameFromCache(reqVo.getTypeNameStr());
        map.put(reqVo.getTypeNameStr(), convertList(list));
        return map;
    }

    private List convertList(List<SysDictEntity> list) {
        return Optional.ofNullable(list).map(ls -> ls.stream().map(d -> {
            SysDictRespVo dict = new SysDictRespVo();
            BeanUtils.copyProperties(d, dict);
            return dict;
        }).collect(Collectors.toList())).orElse(Collections.EMPTY_LIST);
    }
}
