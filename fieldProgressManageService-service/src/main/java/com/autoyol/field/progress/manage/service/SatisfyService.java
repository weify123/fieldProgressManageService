package com.autoyol.field.progress.manage.service;

import com.autoyol.commons.web.ErrorCode;
import com.autoyol.feign.ShortFeignClient;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.entity.StoreInfoEntity;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.entity.TakeGiveTemplateEntity;
import com.autoyol.field.progress.manage.exception.ShortUrlException;
import com.autoyol.field.progress.manage.mapper.TakeGiveTemplateMapper;
import com.autoyol.field.progress.manage.request.survey.SurveyAddReqVo;
import com.autoyol.field.progress.manage.request.survey.SurveyUpdateReqVo;
import com.autoyol.field.progress.manage.request.survey.SurveyUrlReqVo;
import com.autoyol.field.progress.manage.response.survey.SurveyRespVo;
import com.autoyol.vo.Response;
import com.autoyol.vo.req.ShortUrlReqVO;
import com.autoyol.vo.res.ShortUrlResVO;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.buildTypeList;
import static com.autoyol.field.progress.manage.util.OprUtil.getLabelByCode;

@Service
public class SatisfyService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    TakeGiveTemplateMapper takeGiveTemplateMapper;

    @Resource
    DictCache dictCache;

    @Resource
    ShortFeignClient shortFeignClient;

    public List<SurveyRespVo> findPageByCond() throws Exception {
        List<TakeGiveTemplateEntity> list = takeGiveTemplateMapper.findAll();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }
        List<String> typeList = buildTypeList(TOPIC_TYPE, EFFECTIVE_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        return list.stream().sorted(Comparator.comparing(TakeGiveTemplateEntity::getCreateTime).reversed()).map(entity -> convertEntityToVo(map, entity)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public SurveyRespVo selectById(Integer id) throws Exception {
        TakeGiveTemplateEntity entity = takeGiveTemplateMapper.selectByPrimaryKey(id);
        if (Objects.isNull(entity)) {
            return null;
        }
        List<String> typeList = buildTypeList(TOPIC_TYPE, EFFECTIVE_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        return convertEntityToVo(map, entity);
    }

    public int add(SurveyAddReqVo reqVo) throws Exception {
        TakeGiveTemplateEntity entity = new TakeGiveTemplateEntity();
        BeanUtils.copyProperties(reqVo, entity);
        entity.setCreateOp(reqVo.getHandleUser());
        entity.setTopicType(reqVo.getTopicTypeKey());
        entity.setEffectived(reqVo.getEffectivedKey());
        return takeGiveTemplateMapper.insertSelective(entity);
    }

    public int update(SurveyUpdateReqVo reqVo) throws Exception {
        TakeGiveTemplateEntity entity = new TakeGiveTemplateEntity();
        BeanUtils.copyProperties(reqVo, entity);
        entity.setUpdateOp(reqVo.getHandleUser());
        entity.setTopicType(reqVo.getTopicTypeKey());
        entity.setEffectived(reqVo.getEffectivedKey());
        return takeGiveTemplateMapper.updateSelective(entity);
    }

    @HystrixCommand(commandKey = "shortUrl-saveShortUrl-command", fallbackMethod = "getShortUrlFailBack", groupKey = "ShortUrlGroup", threadPoolKey = "ShortUrlThread")
    public String getShortUrl(SurveyUrlReqVo reqVo) {
        ShortUrlReqVO shortUrl = new ShortUrlReqVO();
        shortUrl.setLongUrl(reqVo.getLongUrl());
        shortUrl.setRemarks(MsgConstraint.SURVEY_SHORT_URL_MARKS);
        shortUrl.setSystemType(MsgConstraint.SURVEY_SHORT_BUSINESS_TYPE);
        shortUrl.setUserName(reqVo.getHandleUser());
        Transaction t = Cat.newTransaction(CAT_TRANSACTION_GET_SHORT_URL, CAT_TRANSACTION_GET_SHORT_URL);
        Response<ShortUrlResVO> response;
        try {
            response = shortFeignClient.saveShortUrl(shortUrl);
            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            t.setStatus(e);
            Cat.logError(e);
            logger.error("shortUrl getShortUrl error e {}", e);
            throw new ShortUrlException("getShortUrl error");
        } finally {
            t.complete();
        }
        logger.info("***PRO*** shortUrl 返回 response={}", response);
        if (response.getResCode().equals(ErrorCode.SUCCESS.getCode())) {
            return response.getData().getShortUrl();
        }
        return STRING_EMPTY;
    }

    private String getShortUrlFailBack(SurveyUrlReqVo reqVo) {
        logger.info("***PRO*** 进入短连接获取熔断 reqVo={}", reqVo);
        return STRING_EMPTY;
    }

    private SurveyRespVo convertEntityToVo(Map<String, List<SysDictEntity>> map, TakeGiveTemplateEntity entity) {
        SurveyRespVo vo = new SurveyRespVo();
        try {
            BeanUtils.copyProperties(entity, vo);
        } catch (Exception e) {
            logger.error("***PRO*** SurveyRespVo copy 异常{}", e);
            return null;
        }
        vo.setTopicTypeKey(entity.getTopicType());
        vo.setTopicTypeVal(getLabelByCode(map.get(TOPIC_TYPE), String.valueOf(entity.getTopicType())));
        vo.setEffectivedKey(entity.getEffectived());
        vo.setEffectivedVal(getLabelByCode(map.get(EFFECTIVE_TYPE), String.valueOf(entity.getEffectived())));
        return vo;
    }
}
